angular.module('sauronApp').directive('comentar'
    , function (RESTService, $filter, $rootScope,$q,$sce) {

        var editor = null;

        function loadData(scope) {
            scope.comentarios = [];

            $q.all([
                    RESTService.solicitud.findById(scope.updateRow.id)
                ]
            ).then(function (resp) {

                    scope.form = resp[0];

                    scope.comentarios = $filter('orderBy')(scope.form.comentarios, '-fecha');
                });
        }


        function link(scope,element,attrs) {

            editor = CKEDITOR.replace("comentario-text", {});

            var comentarioModal = $("#comentario-modal");

            var comentarioBorrarModal = $("#comentario-borrar-modal");

            scope.sce = $sce.trustAsHtml;

            scope.onAdd = function() {
                scope.selectedComentario = null;
                editor.setData('');
                comentarioModal.modal({backdrop:false});
            };

            scope.saveComentario = function() {

                var comentario = null;

                if(scope.selectedComentario != null) {
                    scope.selectedComentario.comentario = editor.getData();
                }
                else {
                     comentario = {
                        comentario: editor.getData(),
                        fecha: new Date(),
                        solicitud: scope.form
                    };
                    scope.comentarios.push(comentario);
                    scope.comentarios = $filter('orderBy')(scope.comentarios, '-fecha');
                }


                scope.closeModal();
            };

            scope.borrarComentario = function(o) {
                scope.selectedComentario = o;
                var idx = scope.comentarios.indexOf(o);
                scope.comentarios.splice(idx, 1);
                scope.closeBorrarModal();
            };

            scope.onUpdate = function(o) {
                scope.selectedComentario = o;

                editor.setData(o.comentario);
                comentarioModal.modal({backdrop:false});
            };

            scope.onRemove = function(o) {
                comentarioBorrarModal.modal({backdrop:false});
            };

            scope.closeModal = function() {
                comentarioModal.modal('hide');
            };

            scope.closeBorrarModal = function() {
                comentarioBorrarModal.modal('hide');
            };


            /******************************************************************
             * Evento para grabar los datos del formulario de nueva solicitud1
             ******************************************************************/
            scope.onSave = function () {

                var solicitud = angular.copy(scope.form);

                solicitud.comentarios = scope.comentarios;

                RESTService.solicitud.comentar(solicitud).then(function (o) {
                    scope.close();
                    $rootScope.alertOk('Comentarios guardados', ['Solicitud ID: ' + o.id]);

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
            templateUrl: 'app/solicitud1/comentar-form.html'
        };


    });
