angular.module('sauronApp').factory('RESTService', function ($q, $http, $rootScope) {

    var endpoint = '';

    /*****************************************************************
     * Helper: Cliente rest
     *****************************************************************/
    function rest_client(url, method, params) {

        if (!method) method = 'GET';

        $rootScope.waitModal.modal("show");

        var deferred = $q.defer();

        $http(
            {
                url: endpoint + url,
                method: method,
                params: params,
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function (resp) {
                $rootScope.waitModal.modal("hide");
                deferred.resolve(resp);
            }).error(function (err, status) {
                $rootScope.waitModal.modal("hide");
                deferred.reject(err);
                console.log("Se ha producido un error http: ", err);

                var title = "Atención";

                if (status === 400) {
                    $rootScope.alertError(title, err);
                }
                else {
                    $rootScope.alertError("Error " + status,
                        ['El servidor ha devuelto un error no controlado']);
                }

            });

        return deferred.promise;

    }

/*
        var cf = {
            equipodesarrollo: 'customfield_10310',
            proyecto: 'customfield_10311',
            equipocalidad: 'customfield_10319',
            f_ini_desa: 'customfield_10312',
            f_fin_desa: 'customfield_10313',
            f_ini_test: 'customfield_10314',
            f_fin_test: 'customfield_10315',
            f_paso_prod: 'customfield_10309',
            plan_pruebas: 'customfield_10325',
            observaciones: 'customfield_10320',
            riesgos: 'customfield_10318',
            planpasoprod: 'customfield_10322',
            planmarchaatras: 'customfield_10323'

        };
*/

    function rfc(issuekey) {

        var url = '/rfcs/'+issuekey;

        var deferred = $q.defer();

        rest_client(url).then(function(result){

            console.log(result);

            deferred.resolve(result);

        });

        return deferred.promise;

    }

    function rfcs() {

        var url = '/rfcs';

        var deferred = $q.defer();

        rest_client(url).then(function(result){

            console.log(result);

            deferred.resolve(result);

        });

        return deferred.promise;
    }

    function worklogs(fdesde,fhasta) {

        var deferred = $q.defer();

        search(
            'key in workedIssues("'+fdesde.format('YYYY/MM/DD')+'","'+fhasta.format('YYYY/MM/DD')
            +'","Desarrollo Servicios Centrales") or project = GI').then(function(result){
                deferred.resolve(result);
        });

        return deferred.promise;
    }

    /*********************************************************************************************
     * Devuelve los usuarios del grupo Servicios Centrales
     *********************************************************************************************/
    function users() {
        var url = '/rest/api/2/group?groupname=Desarrollo Servicios Centrales&expand=users';

        var deferred = $q.defer();

        var data = [];

        jira_rest_client(url).then(function(result){

            var total = result.users.size;
            var maxResults = result.users['max-results'];

            if(total <= maxResults) {
                console.log("Total: ",total," MaxResults: ",maxResults," ","No itera ");
                deferred.resolve(result.users.items);
            }
            else {
                Array.prototype.push.apply(data,result.users.items);

                var num = Math.floor(total / maxResults) + ((total % maxResults) != 0?1:0);
                console.log("Total: ",total," MaxResults: ",maxResults," ","Itera: ",num);

                var arr = [];

                for(var i = 1 ; i < num; i++) {
                    var desde = maxResults * i;
                    var hasta = (maxResults * i) + maxResults;

                    var url2 = url + "[" + desde + ":" + hasta + "]";
                    arr.push(jira_rest_client(url2));
                }

                $q.all(arr).then(function (result) {
                    angular.forEach(result,function(r){
                        Array.prototype.push.apply(data,r.users.items);
                    });
                    deferred.resolve(data);
                });
            }

        });


        return deferred.promise;
    }



    /*****************************************************************
     * Objeto final que retorna esta función
     *****************************************************************/
    return {

        rfc:rfc,
        rfcs:rfcs,
        issue:rfc,
        worklogs:worklogs,
        users:users
    }

});
