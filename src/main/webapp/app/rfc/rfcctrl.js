angular.module('sauronApp').controller('RfcCtrl'
    , function ($scope, $rootScope, $routeParams, $location, RESTService, $filter) {

        init();

        function init() {
            setVisible(false);
            refresh();
        }

        function setVisible(visible) {
            $scope.visible = visible;
        }

        function refresh() {
            RESTService.rfc($routeParams.id).then(function (resp) {
                $scope.issue = resp;
                setVisible(true);
            });
        };


    });
