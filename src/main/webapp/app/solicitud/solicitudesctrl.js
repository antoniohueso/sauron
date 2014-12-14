angular.module('sauronApp').controller('SolicitudesCtrl'
    , function ($scope, $rootScope, $location, RESTService, $filter) {

        init();

        function init() {
            setVisible(false);
            $scope.onProjectChange = onProjectChange;
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
        };

        function onProjectChange() {
            if($scope.filtro.project == null) {
                $scope.components = [];
            }
            else {
                console.log($scope.filtro.project.id);
                RESTService.solicitudes.searchFilterComponents({ projectId:$scope.filtro.project.id}).then(function (resp) {
                    $scope.components = $filter('orderBy')(resp, '+name');
                });
            }
        }


    });
