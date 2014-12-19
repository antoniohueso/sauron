angular.module('sauronApp').controller('DatosGeneralesCtrl'
    , function (RESTService, $filter, $rootScope,$scope,$routeParams,$q) {

        var editor = null;

        init();

        function init() {
            setVisible(false);

            //editor = CKEDITOR.replace("descripcion", {});

            $scope.form = {};
            $scope.onSave = onSave;

            refresh();

        }

        function refresh() {

            var restarr = [
                RESTService.solicitudes.tipos()
            ];

            if($routeParams.id != null) {
                restarr.push(
                    RESTService.solicitudes.findById($routeParams.id)
                );
            }

            $q.all(restarr).then(function (resp) {
                $scope.tipos = $filter('orderBy')(resp[0], '+nombre');

                if(resp.length > 1) {
                    $scope.form = resp[1];

                    //editor.setData($scope.form.descripcion);

                    angular.forEach($scope.tipos, function (ts) {
                        if (ts.id == $scope.form.tipoSolicitud.id) {
                            $scope.form.tipoSolicitud = ts;
                        }
                    });
                }
            });

            setVisible(true);
        }

        function setVisible(visible) {
            $scope.visible = visible;
        }

        function onSave() {

            var solicitud = {
                id: $scope.form.id,
                titulo: $scope.form.titulo,
                descripcion: $scope.form.descripcion,
                tipoSolicitudId: $scope.form.tipoSolicitud?$scope.form.tipoSolicitud.id:null
            };


            RESTService.solicitudes.saveDatosGenerales(solicitud).then(function (o) {

                if (solicitud.id == null) {
                    //$scope.dataset.push(o);
                    $rootScope.alertOk('Solicitud creada', ['Solicitud ID: ' + o.id]);
                    $scope.form = {};
                }
                else {
                    $rootScope.alertOk('Solicitud modificada', ['Solicitud ID: ' + o.id]);
                }

            });
        }


    });
