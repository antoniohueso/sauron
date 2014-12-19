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

    };

    /*****************************************************************
     * Objeto final que retorna esta función
     *****************************************************************/
    return {

        issue:function(issuekey) {

            var url = '/rest/api/2/issue/'+issuekey;

            return jira_rest_client(url);

        }

    }

});
