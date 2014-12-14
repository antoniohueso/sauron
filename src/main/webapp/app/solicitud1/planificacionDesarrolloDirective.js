angular.module('sauronApp').directive('planificacionDesarrollo'
    , function (RESTService, $filter, $rootScope,$q) {

        var finicio = null;
        var ffin = null;

        function loadData(scope) {
            scope.projects = [];
            scope.components = [];

            scope.issues = [];
            scope.issuessolicitud = [];

            $q.all([
                RESTService.solicitud.findById(scope.updateRow.id),
                RESTService.project.findAll(),
            ]).then(function (resp) {
                scope.form = resp[0];

                if (scope.form.fechaIniDesarrollo === null || scope.form.fechaFinDesarrollo === null) {
                    scope.form.fechaIniDesarrollo = $filter('date') (new Date(),'dd/MM/yyyy');
                    scope.form.fechaFinDesarrollo = $filter('date') (new Date(),'dd/MM/yyyy');
                }

                finicio.datepicker('setDate', scope.form.fechaIniDesarrollo);

                ffin.datepicker('setDate', scope.form.fechaFinDesarrollo);


                scope.projects = resp[1];

                angular.forEach(scope.form.issues, function (is) {
                    scope.issuessolicitud.push(is.issue);
                });

            });
        }

        function link(scope,element,attrs) {

            finicio = $("#planfdesde").datepicker({
                format: "dd/mm/yyyy",
                todayBtn: true,
                autoclose: true,
                language: "es",
                todayHighlight: true,
                orientation: "top auto"
            });

            ffin = $("#planffin").datepicker({
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
             * filtro de componentes por proyecto
             ******************************************************************/
            function findComponents(project_id) {
                RESTService.project.findComponents(project_id).then(function (resp) {
                    scope.components = resp;
                });
            };

            /******************************************************************
             * filtro de issues
             ******************************************************************/
            function findIssues(project, component) {

                var project_id = project ? project.id : null;
                var component_id = component ? component.id : null;

                RESTService.issue.find(project_id, component_id).then(function (resp) {
                    scope.issues = $filter('orderBy')(resp, '+issuekey');
                });
            };


            /******************************************************************
             * Evento para filtrar por proyecto
             ******************************************************************/
            scope.onProjectChange = function () {

                if (scope.selectedProject === null) {
                    scope.components = [];
                    scope.selectedComponent = null;
                }
                else {
                    findComponents(scope.selectedProject.id);
                }

                findIssues(scope.selectedProject, scope.selectedComponent);
            };

            /******************************************************************
             * Evento para filtrar por componente
             ******************************************************************/
            scope.onComponentChange = function () {
                findIssues(scope.selectedProject, scope.selectedComponent);
            };


            /******************************************************************
             * Evento para añadir un recurso al equipo
             ******************************************************************/
            scope.onAdd = function (o) {
                var idx = scope.issues.indexOf(o);
                scope.issues.splice(idx, 1);
                scope.issuessolicitud.push(o);
                scope.issuessolicitud = $filter('orderBy')(scope.issuessolicitud, '+issuekey');
            };

            /******************************************************************
             * Evento para eliminar un recurso al equipo
             ******************************************************************/
            scope.onRemove = function (o) {
                var idx = scope.issuessolicitud.indexOf(o);
                scope.issuessolicitud.splice(idx, 1);
                scope.issues.push(o);
                scope.issues = $filter('orderBy')(scope.issues, '+issuekey');
            };

            /******************************************************************
             * Evento para grabar los datos del formulario de nueva solicitud1
             ******************************************************************/
            scope.onSave = function () {

                var solicitud = angular.copy(scope.form);

                solicitud.issues = [];
                angular.forEach(scope.issuessolicitud, function (i) {
                    solicitud.issues.push({
                        issue: {
                            id: i.id
                        }
                    });
                });

                RESTService.solicitud.savePlanificacionDesarrollo(solicitud).then(function (o) {
                    scope.close();
                    updateRow(scope,o);
                    $rootScope.alertOk('Planificación guardada', ['Solicitud ID: ' + o.id]);
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
            templateUrl: 'app/solicitud1/planificacionDesarrollo-form.html'
        };

    });
