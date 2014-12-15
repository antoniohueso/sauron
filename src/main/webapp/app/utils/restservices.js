angular.module('sauronApp').factory('RESTService', function ($q, $http, $rootScope) {

    var endpoint = '';

    /*****************************************************************
     * Cliente Rest de solicitudes
     *****************************************************************/
    var solicitud = function () {

        var url = endpoint + '/solicitud1';

        return {
            findAll: function () {
                return rest_client(url);
            },
            findById: function (id) {
                return rest_client(url + "/" + id);
            },
            saveDatosGenerales: function (o) {
                return rest_client(url + '/datosgenerales', 'POST', o);
            },
            saveEquipoDesarrollo: function (o) {
                return rest_client(url + '/equipodesarrollo', 'POST', o);
            },
            savePlanificacionDesarrollo: function (o) {
                return rest_client(url + '/planificaciondesarrollo', 'POST', o);
            },
            saveEnviarQA: function (o) {
                return rest_client(url + '/enviarapruebas', 'POST', o);
            },
            savePlanificacionPruebas: function (o) {
                return rest_client(url + "/planificacionpruebas", 'POST', o);
            },
            saveFinalizar: function (o) {
                return rest_client(url + "/finalizar", 'POST', o);
            },
            saveCerrar: function (o) {
                return rest_client(url + "/cerrar", 'POST', o);
            },
            reabrir: function (o) {
                return rest_client(url + "/reabrir", 'POST', o);
            },
            parar: function (o) {
                return rest_client(url + "/parar", 'POST', o);
            },
            reanudar: function (o) {
                return rest_client(url + "/reanudar", 'POST', o);
            },
            comentar: function (o) {
                return rest_client(url + "/comentar", 'POST', o);
            }

        }
    };

    /*****************************************************************
     * Home
     *****************************************************************/
    var home = function () {

        var url = endpoint + '/home';

        return {
            cuadroDeMandos: function () {
                return rest_client(url);
            }
        }
    }

    /*****************************************************************
     * Catálogo de solicitudes
     *****************************************************************/
    var solicitudes = function () {

        var url = endpoint + '/solicitudes';

        return {
            search: function (filter) {
                return rest_client(url,'POST',filter);
            },
            projects: function () {
                return rest_client(url + '/projects');
            },
            components: function (projectId) {
                return rest_client(url + '/components','POST',projectId);
            },
            tipos: function () {
                return rest_client(url + "/tipos");
            },
            users: function () {
                return rest_client(url + "/users");
            },
            estados: function () {
                return rest_client(url + "/estados");
            }
        }
    }


    /*****************************************************************
     * Cliente Rest de tipos de solicitudes
     *****************************************************************/
    var tipoSolicitud = function () {

        var url = endpoint + '/tiposolicitud';

        return {
            findAll: function () {
                return rest_client(url);
            }
        }
    }

    /*****************************************************************
     * Cliente Rest de usuarios autorizados
     *****************************************************************/
    var user = function () {

        var url = endpoint + '/user';

        return {
            findAll: function () {
                return rest_client(url);
            },
            findUsersDesarrollo: function () {
                return rest_client(url + "/desarrollo");
            },
            findUsersTester: function () {
                return rest_client(url + "/tester");
            },
            findUsersDesarrolloAndTester: function () {
                return rest_client(url + "/desarrolloytester");
            }
        }
    };

    /*****************************************************************
     * Cliente Rest de proyectos jira
     *****************************************************************/
    var project = function () {

        var url = endpoint + '/project';

        return {
            findAll: function () {
                return rest_client(url);
            },

            findComponents: function(project_id) {
                return rest_client(url + "/" + project_id + "/component");
            }
        }
    };

    /*****************************************************************
     * Cliente Rest de issues jira
     *****************************************************************/
    var issue = function () {

        var url = endpoint + '/issue';

        return {
            find: function (project_id,component_id) {

                return rest_client(url,'GET', {
                    project_id: project_id, component_id: component_id
                }, true);
            }
        }
    };


    /*****************************************************************
     * Helper: Cliente rest
     *****************************************************************/
    function rest_client(url, method, data, notjson) {

        if (!method) method = 'GET';

        var headers= {
            'Content-Type': 'application/json'
        };

        var data = data;
        var params = '';

        if(notjson === true) {
            headers = null;
            var params = data;
            var data = null;
        }

        $rootScope.waitModal.modal("show");

        var deferred = $q.defer();

        $http(
            {
                url: url,
                method: method,
                data: data,
                params:params,
                headers: headers
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
        home: new home(),
        solicitudes: new solicitudes(),

        solicitud: new solicitud(),
        tipoSolicitud: new tipoSolicitud(),
        user: new user(),
        project: new project(),
        issue: new issue()

    }

});
