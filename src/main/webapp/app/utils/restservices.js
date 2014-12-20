angular.module('sauronApp').factory('RESTService', function ($q, $http, $rootScope) {

    var endpoint = '/jira';

    /*****************************************************************
     * Helper: Cliente rest
     *****************************************************************/
    function jira_rest_client(url, method, data) {

        if (!method) method = 'GET';

        $rootScope.waitModal.modal("show");

        var deferred = $q.defer();

        $http(
            {
                url: endpoint,
                method: 'POST',
                data: {
                    url: url,
                    method: method,
                    params:data
                },
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


    function parse_issues(issues) {

        var data = [];

        angular.forEach(issues,function(issue){
            data.push(parse_issue(issue));
        });

        return data;

    }

    function parse_issue(issue) {

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

        var iss = issue.fields;
        iss.issuekey = issue.key;
        iss.equipodesarrollo = iss[cf.equipodesarrollo];
        iss.equipocalidad = iss[cf.equipocalidad];
        iss.planpruebas = iss[cf.plan_pruebas];
        iss.docfuncional = "FALTA ESTE CAMPO!!!";
        iss.solucion = "FALTA ESTE CAMPO TAMBIÉN!!!";
        if (iss.solucion != null)
            iss.solucion = iss.solucion.replace(/(\r\n|\n|\r)/gm, "<br/>");
        if (iss.description != null)
            iss.description = iss.description.replace(/(\r\n|\n|\r)/gm, "<br/>");

        iss.observaciones = iss[cf.observaciones];
        if (iss.observaciones != null)
            iss.observaciones = iss.observaciones.replace(/(\r\n|\n|\r)/gm, "<br/>");
        iss.riesgos = iss[cf.riesgos];
        iss.planpasoprod = iss[cf.planpasoprod];
        if (iss.planpasoprod != null)
            iss.planpasoprod = iss.planpasoprod.replace(/(\r\n|\n|\r)/gm, "<br/>");

        iss.planmarchaatras = iss[cf.planmarchaatras];
        if (iss.planmarchaatras != null)
            iss.planmarchaatras = iss.planmarchaatras.replace(/(\r\n|\n|\r)/gm, "<br/>");


        if (iss[cf.f_ini_desa] != null) {
            iss.fInicioDesa = moment(iss[cf.f_ini_desa], 'YYYY-MM-DD');
            iss.fInicioDesaStr = iss.fInicioDesa.format('dd DD [de] MMM [de] YYYY');
        }
        if (iss[cf.f_fin_desa] != null) {
            iss.fFinDesa = moment(iss[cf.f_fin_desa], 'YYYY-MM-DD');
            iss.fFinDesaStr = iss.fFinDesa.format('dd DD [de] MMM [de] YYYY');
        }
        if (iss[cf.f_ini_test] != null) {
            iss.fInicioTest = moment(iss[cf.f_ini_test], 'YYYY-MM-DD');
            iss.fInicioTestStr = iss.fInicioTest.format('dd DD [de] MMM [de] YYYY');
        }
        if(iss[cf.f_fin_test]!=null) {
            iss.fFinTest = moment(iss[cf.f_fin_test], 'YYYY-MM-DD');
            iss.fFinTestStr = iss.fFinTest.format('dd DD [de] MMM [de] YYYY');
        }
        if(iss[cf.f_paso_prod]!=null) {
            iss.fPasoProd = moment(iss[cf.f_paso_prod], 'YYYY-MM-DD');
            iss.fPasoProdStr = iss.fPasoProd.format('dd DD [de] MMM [de] YYYY');
        }
        var arr = [];
        angular.forEach(iss.components,function(component){
            arr.push(component.name);
        });
        iss.components = arr.join(' ');


        var issueln = [];

        iss.impacto = {};

        iss.tareasfinalizadas = 0;

        angular.forEach(iss.issuelinks,function(link){


            if(link.outwardIssue) {

                _issue(link.outwardIssue.id).then(function (result) {
                    result.fields.issuekey = result.key;
                    var i = result.fields;

                    if(i.status.id == 10002 || i.status.id == 10003 || i.status.id == 6)
                        iss.tareasfinalizadas++;

                    var arr = [];
                    angular.forEach(i.components, function (component) {
                        arr.push(component.name);
                    });
                    i.components = arr.join(' ');

                    var k = i.project.name + i.components;

                    if(!iss.impacto.hasOwnProperty(k)) {
                        iss.impacto[k] = {
                            project: i.project,
                            components: i.components,
                            issues:[]
                        };
                    }

                    iss.impacto[k].issues.push(i);

                    issueln.push(i);
                });
            }
        });

        iss.issuelinks = issueln;

        return iss;
    }

    function _issue(issuekey) {

        var url = '/rest/api/2/issue/'+issuekey;

        var deferred = $q.defer();

        jira_rest_client(url).then(function(result){
            deferred.resolve(result);
        });

        return deferred.promise;

    }

    function rfc(issuekey) {

        var deferred = $q.defer();

        _issue(issuekey).then(function(result){

            deferred.resolve(parse_issue(result));

        });

        return deferred.promise;

    }

    function rfcs() {
        var url = '/rest/api/2/search';


        var params = {
                jql: 'project = RFC and status not in (Closed,"En producción")',
                maxResults: 1000
        };

        var deferred = $q.defer();

        jira_rest_client(url,'POST',params).then(function(result){
            deferred.resolve(parse_issues(result.issues));
        });

        return deferred.promise;
    }



    /*****************************************************************
     * Objeto final que retorna esta función
     *****************************************************************/
    return {

        rfc:rfc,
        rfcs:rfcs
    }

});
