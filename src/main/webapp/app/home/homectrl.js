angular.module('sauronApp').controller('HomeCtrl'
    , function ($scope, $rootScope, $location, RESTService, $filter) {

        init();

        function init() {
            setVisible(false);
            $scope.round = Math.round;

            $scope.arr = [
                {data:'anomalia',label:'Anomalías'},
                {data:'vencidas',label:'Vencidas'},
                {data:'semana',label:'Semana'},
                {data:'next',label:'Próxima semana'},
                {data:'futuro',label:'Futuro'}
            ];

            refresh();
        }

        function setVisible(visible) {
            $scope.visible = visible;
        }

        function refresh() {

            var hoy = moment.utc();
            var semana_desde = moment.utc().startOf('week');
            var semana_hasta = moment.utc().endOf('week');
            var next_semana_desde = moment.utc().endOf('week').add(1, 'days');
            var next_semana_hasta = moment.utc().endOf('week').add(1, 'days').endOf('week');

            console.log(semana_desde.format("DD/MM/YYYY"));
            console.log(semana_hasta.format("DD/MM/YYYY"));
            console.log(next_semana_desde.format("DD/MM/YYYY"));
            console.log(next_semana_hasta.format("DD/MM/YYYY"));
            console.log(hoy.format("DD/MM/YYYY"));

            var data = {
                anomalia:[],
                vencidas: [],
                semana: [],
                next: [],
                futuro: []
            };

            RESTService.rfcs().then(function (issues) {

                angular.forEach(issues, function (issue) {

                    issue.perc = Math.round(issue.tareasfinalizadas*100/issue.issuelinks.length);
                    if(issue.perc < 30) issue.percStr = 'danger';
                    else if(issue.perc < 50) issue.percStr = 'warning';
                    else if(issue.perc < 100) issue.percStr = 'info';
                    else if(issue.perc == 100) issue.percStr = 'success';


                    //--- Si no está Abierta
                    if (issue.status.id != 1) {
                        
                        var fecha = null;
                        
                        //--- Si está finalizada debe tener fecha de entrada en producción
                        if (issue.status.id == 10002) {
                            if(issue.fPasoProd == null) {
                                issue.anomalia = 'La tarea está finalizada y no tiene fecha de paso a producción';
                                data.anomalia.push(issue);
                            }
                            else fecha = issue.fPasoProd;
                        }
                        //--- Si está en estado 'probando' o detectado error en pruebas debe tener fecha de planificación
                        //    de pruebas
                        else if (issue.status.id == 10001 || issue.status.id == 10005) {
                            if(issue.fFinTest == null || issue.fInicioTest == null) {
                                issue.anomalia = 'La tarea está en pruebas y no tiene fecha de planificación de calidad';
                                data.anomalia.push(issue);
                            }
                            else fecha = issue.fFinTest;
                        }
                        //--- Para el resto de estados (excepto cerrada y en prod. que no están en la query
                        //    debe tener fecha de planificación
                        else {
                            if(issue.fFinDesa == null || issue.fInicioDesa == null) {
                                issue.anomalia = 'La tarea no tiene fecha de planificación de desarrollo';
                                data.anomalia.push(issue);
                            }
                            else fecha = issue.fFinDesa;
                        }

                        //--- Si no hay anomalías
                        if(fecha != null) {
                            //--- Vencidas
                            if (!fecha.isAfter(hoy)) {
                                data.vencidas.push(issue);
                            }
                            //--- En esta semana
                            else if (!fecha.isAfter(semana_hasta)) {
                                data.semana.push(issue);
                            }
                            //--- Próxima semana
                            else if (!fecha.isAfter(next_semana_hasta)) {
                                data.next.push(issue);
                            }
                            else {
                                data.futuro.push(issue);
                            }
                        }
                    }
                });

                console.log(data);

                $scope.data = data;

                setVisible(true);
            });
        };


    });
