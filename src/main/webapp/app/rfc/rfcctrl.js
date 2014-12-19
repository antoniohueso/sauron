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

            RESTService.issue($routeParams.id).then(function (resp) {

                $scope.issue = resp.fields;
                $scope.issue.issuekey = resp.key;

                setVisible(true);
            });
        };


    });
