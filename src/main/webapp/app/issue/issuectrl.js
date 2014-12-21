angular.module('sauronApp').controller('IssueCtrl'
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
               console.log(resp);
                $scope.issue = resp;
                setVisible(true);
            });
        };


    });
