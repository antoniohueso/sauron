angular.module('sauronApp').directive('enviarQa'
    , function (RESTService, $filter, $rootScope,$q) {

        var edSolucion = null;
        var edPasoProd = null;
        var edPasoAtras =  null;

        function loadData(scope) {

            RESTService.solicitud.findById(scope.updateRow.id).then(function(resp){
                scope.form = resp;

                edSolucion.setData(scope.form.solucion);
                edPasoProd.setData(scope.form.planpasoproduccion);
                edPasoAtras.setData(scope.form.planmarchaatras);

                scope.visible = true;
            });

        }

        function link(scope,element,attrs) {

            edSolucion =  CKEDITOR.replace("solucion-text", {});

            edPasoProd =  CKEDITOR.replace("pasoproduccion-text",  {});

            edPasoAtras =  CKEDITOR.replace("planmarchaatras-text",  {});


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
             * Evento para controlar los Tabs de la ventana modal de nueva
             * solicitud1.
             ******************************************************************/
            scope.openTab = function (e) {
                e.preventDefault();
                $(this).tab('show');
            };


            /******************************************************************
             * Evento para grabar los datos del formulario de nueva solicitud1
             ******************************************************************/
            scope.onSave = function () {

                var solicitud = angular.copy(scope.form);

                solicitud.solucion = edSolucion.getData();
                solicitud.planpasoproduccion = edPasoProd.getData();
                solicitud.planmarchaatras = edPasoAtras.getData();

                RESTService.solicitud.saveEnviarQA(solicitud).then(function (o) {
                    scope.close();
                    updateRow(scope,o);
                    $rootScope.alertOk('Solicitud lista para QA', ['Solicitud ID: ' + o.id]);
                });
            };


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
            templateUrl: 'app/solicitud1/enviarQA-form.html'
        };
    });
