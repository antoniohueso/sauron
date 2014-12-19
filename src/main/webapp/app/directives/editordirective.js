angular.module('sauronApp').directive('editor'
    , function (RESTService, $filter, $rootScope,$q) {

        var editor = null;

        function link(scope,element,attrs) {

            editor = CKEDITOR.replace(element[0]);

            editor.on('change', function() {
                editor.updateElement();
                scope.ngModel = editor.getData();
                scope.$apply();
            });
        }

        return {

            scope:{
                ngModel:'='
            },
            link:link
        };

    });
