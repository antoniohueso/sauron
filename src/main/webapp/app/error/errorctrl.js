angular.module('sauronApp').controller('ErrorCtrl'
    , function ($scope) {

        init();

        function init() {
            setVisible(true);
        }

        function setVisible(visible) {
            $scope.visible = visible;
        }


    });
