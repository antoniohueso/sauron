angular.module('sauronApp').controller('WorklogCtrl'
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

            var fdesde = moment('2014/12/01');
            var fhasta = moment('2015/01/01');


            RESTService.worklogs(fdesde,fhasta).then(function (resp) {
                console.log(resp);
                setVisible(true);
            });
        };


    });
