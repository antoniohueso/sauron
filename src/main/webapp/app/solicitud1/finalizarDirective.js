angular.module('sauronApp').directive('finalizar'
    , function (RESTService, $filter, $rootScope,$q) {

        var fimplantacion = null;
        var edInforme = null;
        var edObservaciones = null;

        function loadData(scope) {
            RESTService.solicitud.findById(scope.updateRow.id).then(function(resp){
                scope.form = resp;

                if( scope.form.fechaImplantacion !== null) {
                    fimplantacion.datepicker('setDate', scope.form.fechaImplantacion);
                }

                edInforme.setData(scope.form.informecalidad);
                edObservaciones.setData(scope.form.observaciones);

            });
        }

        function link(scope,element,attrs) {

            fimplantacion = $("#fimplantacion").datepicker({
                format: "dd/mm/yyyy",
                todayBtn: true,
                autoclose: true,
                language: "es",
                todayHighlight: true,
                orientation: "top auto"
            });

            edInforme =  CKEDITOR.replace("informe-text", {});

            edObservaciones =  CKEDITOR.replace("observaciones-text",  {});

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

                var solicitud = scope.form;

                solicitud.informecalidad = edInforme.getData();
                solicitud.observaciones = edObservaciones.getData();

                RESTService.solicitud.saveFinalizar(solicitud).then(function (o) {
                    scope.close();
                    updateRow(scope,o);
                    $rootScope.alertOk('Solicitud finalizada', ['Solicitud ID: ' + o.id]);
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
            templateUrl: 'app/solicitud1/finalizar-form.html'
        };


    });
