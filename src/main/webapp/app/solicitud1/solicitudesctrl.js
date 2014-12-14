angular.module('sauronApp').controller('solicitudesCtrl'
    , function ($scope, $rootScope, $location, RESTService, $filter, Utils) {

        setVisible(false);
        $scope.solicitudes = [];
        $scope.form = {};
        var confirmModal = $("#confirm-modal").modal({backdrop:false,show:false});
        $scope.showSolicitudForm = false;

        $scope.moment = moment;

        $scope.datosGeneralesVisible = false;
        $scope.equipoVisible = false;
        $scope.planificacionDesarrolloVisible = false;
        $scope.planificacionQAVisible = false;
        $scope.finalizarVisible = false;
        $scope.reabrirVisible = false;
        $scope.pararVisible = false;
        $scope.reanudarVisible = false;
        $scope.comentarVisible = false;
        $scope.cerrarVisible = false;

        /******************************************************************
         * Contiene todas las queries que es necesario ejecutar cada vez que
         * se inserte un valor de la vista principal
         ******************************************************************/
        $scope.refresh = function () {
            RESTService.solicitud.findAll().then(function (resp) {
                //$scope.solicitudes = resp;

                angular.forEach(resp,function(s){

                    $scope.addSolicitud(s);

                });



                setVisible(true);
            });
        };

        $scope.addSolicitud = function(s) {
/*
            var row = {
                estadistica: {
                    total: s.issues.length,
                    totalfinalizadas:0,
                    totaldias:0,
                    timespent:0,
                    totaldiasspent:0
                },
                fIniDesa: s.fechaIniDesarrollo!=null?moment(s.fechaIniDesarrollo,'DD/MM/YYYY'):null,
                fFinDesa: s.fechaFinDesarrollo!=null?moment(s.fechaFinDesarrollo,'DD/MM/YYYY'):null,
                solicitud1: s
            };
*/
            var total = s.issues.length;
            var fIniDesa  = s.fechaIniDesarrollo!=null?moment(s.fechaIniDesarrollo,'DD/MM/YYYY'):null;
            var fFinDesa = s.fechaFinDesarrollo!=null?moment(s.fechaFinDesarrollo,'DD/MM/YYYY'):null;

            if(total > 0) {
                var terminadas = 0;
                angular.forEach(s.issues, function (t) {

                    if ((t.issue.statusId >= 10000 && t.issue.statusId <= 10003) || t.issue.statusId == 6) {
                        terminadas++;
                    }
                });
                s._tareas.totalfin = Math.round((terminadas * 100) / s._tareas.total);
            }

            if(s._fIniDesa != null && s._fFinDesa !=null) {
                var fini = moment(s._fIniDesa);

                var totaldias = 0;
                var diast = 0;
                var today = moment().utc();

                if(fini.isAfter(today)) diast=10000000;

                while(!fini.isAfter(s._fFinDesa)) {
                    if(fini.weekday() >= 0 && fini.weekday() < 5 ) {
                        if(!fini.isAfter(today)) diast++;
                        totaldias++;
                    }
                    fini.add(1,'days');
                }

                s._tareas.totaldias = totaldias;
                s._tareas.diast = diast;
                s._tareas.timespent = Math.round(((diast * 100) / totaldias));
                if(s._tareas.timespent > 100) s._tareas.timespent = 100;
                /*
                 console.log("Dias: ",totaldias, " "+ diast);
                 console.log(moment.utc(s.fFinDesa.diff(s.fIniDesa)).date());*/

            }


            $scope.solicitudes.push(s);

        };

        $scope.ok = function () {
            $scope.refresh();
        };

        $scope.cancel = function () {
            setVisible(true);
        };

        $scope.refresh();

        RESTService.tipoSolicitud.findAll().then(function (resp) {
            $scope.tiposSolicitud = resp;
        });

        RESTService.user.findAll().then(function (resp) {
            $scope.users = resp;
        });

        /******************************************************************
         * Muestra/Oculta la página
         ******************************************************************/
        function setVisible(visible) {
            $scope.pageVisible = visible;
        };


        /******************************************************************
         * Opción de nueva solicitud1
         ******************************************************************/
        $scope.onNew = function () {
            $scope.datosGeneralesVisible = true;
            $scope.row = null;

        };

        /******************************************************************
         * Opción de modificar solicitud1
         ******************************************************************/
        $scope.onUpdate = function (o) {
            $scope.row = angular.copy(o);
            $scope.datosGeneralesVisible = true;
        };

        /******************************************************************
         * Opción de asignar equipo
         ******************************************************************/
        $scope.onEquipo = function (o) {
            $scope.row = angular.copy(o);
            $scope.equipoVisible = true;
        };

        /******************************************************************
         * Opción de planificar Desarrollo
         ******************************************************************/
        $scope.onPlanificacionDesarrollo = function (o) {
            $scope.row = angular.copy(o);
            $scope.planificacionDesarrolloVisible = true;
        };

        /******************************************************************
         * Opción de enviar a QA
         ******************************************************************/
        $scope.onEnviarQA = function (o) {
            $scope.row = angular.copy(o);
            $scope.enviarQAVisible = true;
        };

        /******************************************************************
         * Opción de Planificar QA
         ******************************************************************/
        $scope.onPlanificarQA = function (o) {
            $scope.row = angular.copy(o);
            $scope.planificacionQAVisible = true;

        };

        /******************************************************************
         * Opción de finalizar
         ******************************************************************/
        $scope.onFinalizar = function (o) {
            $scope.row = angular.copy(o);
            $scope.finalizarVisible = true;
        };


        /******************************************************************
         * Opción reabrir
         ******************************************************************/
        $scope.onReabrir = function (o) {
            $scope.row = angular.copy(o);
            $scope.reabrirVisible = true;
        };

        /******************************************************************
         * Opción Parar
         ******************************************************************/
        $scope.onParar = function (o) {
            $scope.row = angular.copy(o);
            $scope.pararVisible = true;
        };

        /******************************************************************
         * Opción reanudar
         ******************************************************************/
        $scope.onReanudar = function (o) {
            $scope.row = angular.copy(o);
            $scope.reanudarVisible = true;
        };

        /******************************************************************
         * Opción comentar
         ******************************************************************/
        $scope.onComentar = function (o) {
            $scope.row = angular.copy(o);
            $scope.comentarVisible = true;
        };

        /******************************************************************
         * Opción cerrar
         ******************************************************************/
        $scope.onCerrar = function (o) {
            $scope.row = angular.copy(o);
            $scope.cerrarVisible = true;
        };


    });
