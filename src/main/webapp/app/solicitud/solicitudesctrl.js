angular.module('sauronApp').controller('SolicitudesCtrl'
    , function ($scope, $rootScope, $location, RESTService, $filter) {

        init();

        function init() {
            setVisible(false);
            $scope.onProjectChange = onProjectChange;
            $scope.onSearch = onSearch;
            $scope.moment = moment;
            refresh();
        }

        function setVisible(visible) {
            $scope.visible = visible;
        }

        function refresh() {

            RESTService.solicitudes.searchFilters().then(function (resp) {

                $scope.estados = resp.estados;
                $scope.users = $filter('orderBy')(resp.users, '+displayName');
                $scope.tipos = $filter('orderBy')(resp.tipos, '+nombre');
                $scope.projects = $filter('orderBy')(resp.projects, '+name');

                setVisible(true);
            });
        }

        function onProjectChange() {
            if($scope.filtro.project == null) {
                $scope.components = [];
            }
            else {
                RESTService.solicitudes.searchFilterComponents({ projectId:$scope.filtro.project.id}).then(function (resp) {
                    $scope.components = $filter('orderBy')(resp, '+name');
                });
            }
        }

        function onSearch() {
            console.log($scope.filtro);

            var filtro = $scope.filtro!=null?angular.copy($scope.filtro):{};

            if(filtro.estado) {
                filtro.estado = filtro.estado.id;
            }

            if(filtro.tipo) {
                filtro.tipo = filtro.tipo.id;
            }

            RESTService.solicitudes.search(filtro).then(function (resp) {
                $scope.solicitudes = resp;
            });
        }


    });
