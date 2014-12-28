function config_calendar(urlEvents,usersTagId,type) {

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

        _.templateSettings = {
            interpolate: /\{\{(.+?)\}\}/g
        };


        var html = $("#cal-tooltip").html();
        var tpl = _.template(html);
        var statusKey = {
            10200:"label-default glyphicon glyphicon-ban-circle",
            1:"label-default",
            10004:"label-primary",
            5:"label-primary",
            10005:"label-warning",
            10000:"label-warning",
            10001:"label-warning",
            10002:"label-success",
            6:"glyphicon glyphicon-ok label-success",
            10003:"glyphicon glyphicon-ok label-success"
        };

        $('#calendar').fullCalendar({
            firstDay: 1,
            weekends:false,
            lang:"es",

            defaultView:type==null?'month':type,

            events: function(start, end, timezone, callback) {
                var userid = null;
                if(usersTagId) {
                    if ($(usersTagId).val() != "all") userid = $("#users").val();
                }
                change(start,end, userid,callback);
            },

            eventRender:function( event, element, view ) {

                if(event.data != null) {

                    console.log(event.data);

                    if(event.data && event.data.issuekey) {

                        event.title = tpl({
                            r:event.data,
                            statusKey:statusKey
                        });

                        if(event.data.issuekey.indexOf("RFC-") == 0) {

                            /*
                            event.title += "<p class=\"label label-success\">Estado: "+event.data.status.name+"</p>";
                            var arr = [];
                            if(event.data.equipodesarrollo) {
                                for(var i in event.data.equipodesarrollo) {
                                    arr.push(event.data.equipodesarrollo[i].user.name);
                                }
                            }
                            if(event.data.equipocalidad) {
                                for(var i in event.data.equipocalidad) {
                                    arr.push(event.data.equipocalidad[i].user.name);
                                }
                            }

                            event.title += "<p><span style=\"font-weight: 600;\">Equipo: </span> "+arr.join(', ')+"</p>";

                            */


                            element.attr('href', '/rfcs/' + event.data.issuekey + "?app");
                        }
                        else {
                            element.attr('href', '/issues/' + event.data.issuekey);
                        }
                    }


                }
                else {
                    event.title = event.title;
                }
                element.attr('title',event.title);
                element.attr('data-toggle','tooltip');
                element.attr('data-placement','bottom');
                element.attr('target','_blank');
                element.tooltip({ html:true});
            }
        });

    });
}