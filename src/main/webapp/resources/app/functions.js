function config_calendar(urlEvents,usersTagId) {

    $(document).ready(function() {

        var waitmodal = $("#waitmodal").modal({ backdrop: false, show:false });
        var errormodal = $("#error-modal").modal({ backdrop: false, show:false });

        moment.locale('es');

        function change(start,end,userid,callback) {

            waitmodal.modal('show');

            $.ajax({
                url:urlEvents,
                dataType:'json',
                data:{
                    start:start.format('YYYY-MM-DD'),
                    end:end.format('YYYY-MM-DD'),
                    userid:userid
                }
            }).success(function(result){
                callback(result);
                waitmodal.modal('hide');
            }).error(function(error){
                waitmodal.modal('hide');
                errormodal.modal('show');
            });
        }

        if(usersTagId) {
            $(usersTagId).change(function () {
                $('#calendar').fullCalendar('refetchEvents');
            });
        }

        $('#calendar').fullCalendar({
            firstDay: 1,
            weekends:false,
            monthNames:['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'],
            dayNamesShort: ["D","L", "M", "X", "J", "V", "S"],

            events: function(start, end, timezone, callback) {
                var userid = null;
                if(usersTagId) {
                    if ($(usersTagId).val() != "all") userid = $("#users").val();
                }
                change(start,end, userid,callback);
            },

            eventRender:function( event, element, view ) {
                element.attr('title',event.title);
                element.attr('data-toggle','tooltip');
                element.attr('data-placement','top');
                if(event.data != null) {
                    if(event.data && event.data.issuekey) {
                        if(event.data.issuekey.indexOf("RFC-") == 0) {
                            element.attr('href', '/rfcs/' + event.data.issuekey + "?app");
                        }
                        else {
                            element.attr('href', '/issues/' + event.data.issuekey);
                        }
                    }

                }
                element.attr('target','_blank');
                element.tooltip();
            }
        });

    });
}