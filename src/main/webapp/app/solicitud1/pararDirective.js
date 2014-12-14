angular.module('sauronApp').directive('parar'
    , function (RESTService, $filter, $rootScope,$q) {

        var editor = null;

        function loadData(scope) {
            $q.all([
                    RESTService.solicitud.findById(scope.updateRow.id)
                ]
            ).then(function (resp) {
                    scope.form = resp[0];
                });
        }

        function link(scope,element,attrs) {

            editor = CKEDITOR.replace("comentario-parar", {});

            /******************************************************************
             * Evento de Ok
             ******************************************************************/
            scope.onSave = function () {

                scope.form.comentarios.push({
                    comentario:editor.getData(),
                    fecha: new Date()
                });

                RESTService.solicitud.parar(scope.form).then(function (o) {
                    scope.close();
                    updateRow(scope,o);
                    $rootScope.alertOk('Solicitud suspendida',['Solicitud ID: ' + o.id]);
                });
            };

            /******************************************************************
             * Si cambia el valor del atributo 'show' a true abre la ventana
             * modal.
             ******************************************************************/
            scope.$watch('show',function(newvalue,oldvalue){
                if(oldvalue !== newvalue) {
                    if(newvalue) {
                        loadData(scope);
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
            templateUrl: 'app/solicitud1/parar-form.html'
        };

    });
