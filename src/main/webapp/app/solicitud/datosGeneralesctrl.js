angular.module('sauronApp').directive('DatosGeneralesCtrl'
    , function (RESTService, $filter, $root$scope,$q) {

        init();

        var editor = null;


        function init() {
            setVisible(false);

            editor = CKEDITOR.replace("descripcion", {});

            refresh();
        }

        function refresh() {

            var restarr = [
                RESTService.solicitudes.tipos()
            ];

            $q.all(restarr).then(function (resp) {
                $scope.tipos = $filter('orderBy')(resp[0], '+nombre');
            });

            setVisible(true);
        }

        function setVisible(visible) {
            $scope.visible = visible;
        }
        

                        if($scope.updateRow && $scope.updateRow.id) {
                            RESTService.solicitud.findById($scope.updateRow.id).then(function (resp) {
                                $scope.form = resp;
                                editor.setData($scope.form.descripcion);

                                angular.forEach($scope.tiposSolicitud, function (ts) {
                                    if (ts.id == $scope.form.tipoSolicitud.id) {
                                        $scope.form.tipoSolicitud = ts;
                                    }
                                });
                                element.modal({backdrop:false});
                            });
                        }
                        else {
                            $scope.form = {};
                            editor.setData('');
                            element.modal('show');
                        }
                    }
                }
            });

            /******************************************************************
             * Evento para grabar los datos del formulario de nueva solicitud1
             ******************************************************************/
            $scope.onSave = function () {

                var solicitud = angular.copy($scope.form);

                solicitud.descripcion = editor.getData();

                RESTService.solicitud.saveDatosGenerales(solicitud).then(function (o) {

                    if (solicitud.id == null) {
                        $scope.dataset.push(o);
                        $root$scope.alertOk('Solicitud creada', ['Solicitud ID: ' + o.id]);
                        $scope.form = {};
                        editor.setData('');
                    }
                    else {
                        updateRow($scope,o);
                        $root$scope.alertOk('Solicitud modificada', ['Solicitud ID: ' + o.id]);
                    }

                    $scope.close();


                });
            };

            /******************************************************************
             * Al cerrar la ventana modal cambia el valor del atributo 'show'
             ******************************************************************/
            $scope.close = function() {
                element.modal('hide');
                $scope.show=false;
            };


        }



        /******************************************************************
         * Modifica la fila del dataset
         ******************************************************************/
        function updateRow($scope,row) {
            var index = 0;
            angular.forEach($scope.dataset,function(o){
                if(o.id == row.id) {
                    $scope.dataset[index] = row;
                }
                index++;
            });
        }

        return {
            $scope: {
                dataset:'=',
                updateRow:'=',
                show:'='
            },
            link:link,
            templateUrl: 'app/solicitud1/datosGenerales-form.html'
        };

    });
