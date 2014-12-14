angular.module('sauronApp').directive('reabrir'
    , function (RESTService, $filter, $rootScope,$q) {

        function link(scope,element,attrs) {

            /******************************************************************
             * Evento de ok
             ******************************************************************/
            scope.onOk = function() {
                RESTService.solicitud.reabrir(scope.form).then(function (o) {
                    scope.close();
                    updateRow(scope,o);
                    $rootScope.alertOk('Solicitud reabierta',['Solicitud ID: ' + o.id]);
                });
            };

            /******************************************************************
             * Si cambia el valor del atributo 'show' a true abre la ventana
             * modal.
             ******************************************************************/
            scope.$watch('show',function(newvalue,oldvalue){
                if(oldvalue !== newvalue) {
                    if(newvalue) {
                        scope.form = scope.updateRow;
                        element.modal({backdrop:false});
                    }
                }
            });
            /******************************************************************
             * Al cerrar la ventana modal cambia el valor del atributo 'show'
             ******************************************************************/
            scope.close = function() {
                element.modal('hide');
                scope.show=false;
            };

        }

        function updateRow(scope,row) {
            var index = 0;
            angular.forEach(scope.dataset,function(o){
                if(o.id == row.id) {
                    scope.dataset[index] = row;
                }
                index++;
            });
        }

        return {
            scope: {
                dataset:'=',
                updateRow:'=',
                show:'='
            },
            link:link,
            templateUrl: 'app/solicitud1/reabrir-form.html'
        };

    });
