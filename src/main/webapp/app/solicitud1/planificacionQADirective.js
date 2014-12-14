angular.module('sauronApp').directive('planificacionQa'
    , function (RESTService, $filter, $rootScope,$q) {

        var finicio;
        var ffin;

        function loadData(scope) {
            $q.all([
                    RESTService.solicitud.findById(scope.updateRow.id),
                    RESTService.user.findUsersTester()]
            ).then(function (resp) {

                    scope.form = resp[0];

                    if(scope.form.fechaIniPruebas === null || scope.form.fechaIniPruebas === null) {
                        scope.form.fechaIniPruebas = $filter('date') (new Date(),'dd/MM/yyyy');
                        scope.form.fechaFinPruebas = $filter('date') (new Date(),'dd/MM/yyyy');
                    }

                    finicio.datepicker('setDate', scope.form.fechaIniPruebas);
                    ffin.datepicker('setDate', scope.form.fechaFinPruebas);


                    var equipo = scope.form.equipo;
                    angular.forEach(equipo,function(e){
                        scope.equipo.push(e.user);
                    });

                    angular.forEach(resp[1], function (u) {
                        var exist = false;
                        angular.forEach(scope.equipo, function (e) {
                            if (e.id === u.id) {
                                exist = true;
                            }
                        });
                        if (!exist) scope.users.push(u);
                    });

                });
        }

        function link(scope,element,attrs) {
            scope.form = {};
            scope.visible = false;
            scope.equipo = [];
            scope.users = [];

            finicio = $("#fdesdePlanQA").datepicker({
                format: "dd/mm/yyyy",
                todayBtn: true,
                autoclose: true,
                language: "es",
                todayHighlight: true,
                orientation: "top auto"
            });

            ffin = $("#ffinPlanQA").datepicker({
                format: "dd/mm/yyyy",
                todayBtn: true,
                autoclose: true,
                language: "es",
                todayHighlight: true,
                orientation: "top auto"
            });

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
             * Evento para añadir un recurso al equipo
             ******************************************************************/
            scope.onAdd = function (u) {

                var idx = scope.users.indexOf(u);
                scope.users.splice(idx, 1);
                scope.equipo.push(u);
                scope.equipo = $filter('orderBy')(scope.equipo, '+displayName');
            };

            /******************************************************************
             * Evento para eliminar un recurso al equipo
             ******************************************************************/
            scope.onRemove = function (u) {
                var idx = scope.equipo.indexOf(u);
                scope.equipo.splice(idx, 1);
                scope.users.push(u);
                scope.users = $filter('orderBy')(scope.users, '+displayName');
            };

            /******************************************************************
             * Evento para grabar los datos del formulario de nueva solicitud1
             ******************************************************************/
            scope.onSave = function () {

                var solicitud = angular.copy(scope.form);

                solicitud.equipo = [];
                angular.forEach(scope.equipo, function (u) {
                    solicitud.equipo.push({
                        user: u
                    });
                });

                RESTService.solicitud.savePlanificacionPruebas(solicitud).then(function (o) {
                    scope.close();
                    updateRow(scope,o);
                    $rootScope.alertOk('Equipo de pruebas asignado', ['Solicitud ID: ' + o.id]);
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
            templateUrl: 'app/solicitud1/planificacionQA-form.html'
        };


    });
