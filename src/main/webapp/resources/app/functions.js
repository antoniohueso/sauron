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
            evaluate : /\{\[([\s\S]+?)\]\}/g,
            interpolate : /\{\{([\s\S]+?)\}\}/g
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
            lang:'es',
            defaultView:type==null?'month':type,

            events: function(start, end, timezone, callback) {
                var userid = null;
                if(usersTagId) {
                    if ($(usersTagId).val() != "all") userid = $("#users").val();
                }
                change(start,end, userid,callback);
            },

            eventRender:function( event, element, view ) {

                var data = event.data;
                var title = null;
                var content = null;

                if(data && data.issuekey) {

                    var statusLabel = statusKey[data.status.id];
                    if(!statusLabel) statusLabel = "label-default";

                    var o = {
                        r:data,
                        statusLabel:statusLabel,
                        event:event
                    };

                    title = data.issuekey + ' - ' + data.summary;

                    if(data.issuekey.indexOf("RFC-") == 0) {

                        data.isRfc = true;

                        var arr = [];

                        if( data.equipodesarrollo || data.equipocalidad) {
                            if (data.equipodesarrollo) {
                                for (var i in data.equipodesarrollo) {
                                    arr.push(data.equipodesarrollo[i].user.name);
                                }
                            }
                            if (data.equipocalidad) {
                                for (var i in data.equipocalidad) {
                                    arr.push(data.equipocalidad[i].user.name);
                                }
                            }

                            data.equipoStr = arr.join(', ');
                        }
                        if(data.porcentajeCompletado < 25) {
                            data.porcentajeLabel = 'text-danger';
                        }
                        else if(data.porcentajeCompletado >= 25 && data.porcentajeCompletado < 50) {
                            data.porcentajeLabel = 'text-warning';
                        }
                        else if(data.porcentajeCompletado >= 50 && data.porcentajeCompletado < 100) {
                            data.porcentajeLabel = 'text-info';
                        }
                        else if(data.porcentajeCompletado == 100) {
                            data.porcentajeLabel = 'text-success';
                        }


                        element.attr('href', '/rfcs/' + data.issuekey + "?app");
                    }
                    else {
                        element.attr('href', '/issues/' + data.issuekey);
                    }

                    content = tpl(o);
                }
                else {
                    title = event.title;
                    if(event.className != "calendar-danger") content = 'Vacaciones';
                }

                element.attr('title',title);
                element.attr('data-toggle','popover');
                element.attr('data-content',content);
                element.attr('data-placement','auto');
                element.attr('data-container','body');
                element.attr('target','_blank');
                element.popover({ html:true, trigger:'hover' });
            }
        });

    });
}