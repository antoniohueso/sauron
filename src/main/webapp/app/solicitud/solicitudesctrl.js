angular.module('sauronApp').controller('SolicitudesCtrl'
    , function ($scope, $rootScope, $location, RESTService, $filter,$q) {

        init();

        function init() {
            setVisible(false);
            $scope.filtro = {};
            $scope.onProjectChange = onProjectChange;
            $scope.onSearch = onSearch;
            $scope.moment = moment;
            refresh();
        }

        function setVisible(visible) {
            $scope.visible = visible;
        }

        function refresh() {

            var restarr = [
                RESTService.solicitudes.tipos(),
                RESTService.solicitudes.users(),
                RESTService.solicitudes.projects(),
                RESTService.solicitudes.estados()
            ];

            $q.all(restarr).then(function (resp) {
                $scope.tipos = $filter('orderBy')(resp[0], '+nombre');
                $scope.users = $filter('orderBy')(resp[1], '+displayName');
                $scope.projects = $filter('orderBy')(resp[2], '+name');
                $scope.estados = $filter('orderBy')(resp[3], '+id');
                setVisible(true);
            });
        }

        function onProjectChange() {
            if($scope.filtro.project == null) {
                $scope.components = [];
            }
            else {
                RESTService.solicitudes.components({ projectId:$scope.filtro.project.id}).then(function (resp) {
                    $scope.components = $filter('orderBy')(resp, '+name');
                });
            }
        }

        function onSearch() {
            console.log($scope.filtro);

            var filtro = $scope.filtro!=null?angular.copy($scope.filtro):{};

            if(filtro.estado) {
                filtro.estadoId = filtro.estado.id;
            }

            if(filtro.tipo) {
                filtro.tipoSolicitudId = filtro.tipo.id;
            }

            RESTService.solicitudes.search(filtro).then(function (resp) {
                $scope.solicitudes = resp;
            });
        }


    });
