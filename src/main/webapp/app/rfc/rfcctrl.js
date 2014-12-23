angular.module('sauronApp').controller('RfcCtrl'
    , function ($scope, $rootScope, $routeParams, $location, RESTService, $filter) {

        init();

        function init() {
            setVisible(false);
            $scope.moment = moment;
            refresh();
        }

        function setVisible(visible) {
            $scope.visible = visible;
        }

        function refresh() {
            RESTService.rfc($routeParams.id).then(function (resp) {
                $scope.rfc = resp;

                $scope.rfc.solucion = "FALTA LA SOLUCIÓN!!!!";

                $scope.rfc.docfuncional = "FALTA LA DOC. FUNCIONAL!!!!";

                $scope.rfc.observaciones = $scope.rfc.observaciones.replace(/(\r\n|\n|\r)/gm, '<br/>');
                $scope.rfc.planpasoprod = $scope.rfc.planpasoprod.replace(/(\r\n|\n|\r)/gm, '<br/>');
                $scope.rfc.planmarchaatras = $scope.rfc.planmarchaatras.replace(/(\r\n|\n|\r)/gm, '<br/>');
                setVisible(true);
            });
        };


    });
