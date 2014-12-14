angular.module('sauronApp').controller('HomeCtrl'
    , function ($scope, $rootScope, $location, RESTService, $filter) {

        init();

        function init() {
            setVisible(false);
            refresh();
        }

        function setVisible(visible) {
            $scope.visible = visible;
        }

        function refresh() {

            RESTService.home.cuadroDeMandos().then(function (resp) {

                $scope.resumenSolicitudes = resp.resumenSolicitudes;

                console.log(resp);
                setVisible(true);
            });
        };


    });
