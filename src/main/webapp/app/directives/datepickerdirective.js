angular.module('sauronApp').directive('datepicker'
    , function (RESTService, $filter, $rootScope,$q) {

        var fecha = null;

        function link(scope,element,attrs) {

            fecha = element.datepicker({
                format: "dd/mm/yyyy",
                todayBtn: true,
                autoclose: true,
                language: "es",
                todayHighlight: true
                /*, orientation: "top auto"*/
            });

            /******************************************************************
             * Si cambia el valor del la fecha la asigna al objeto datepicker
             ******************************************************************/
            scope.$watch('ngModel',function(newvalue,oldvalue){
                if(oldvalue !== newvalue) {
                    if(newvalue) {
                        fecha.datepicker('setDate', newvalue);
                    }
                }
            });
        }

        return {
            scope: {
                ngModel:'='
            },
            link:link
        };

    });
