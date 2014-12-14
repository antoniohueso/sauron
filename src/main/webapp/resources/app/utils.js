var notifica = function(title,message,type) {
        var icon = null;
        var delay = 0;

        if(type == 'danger') {
                icon = 'glyphicon glyphicon-exclamation-sign';
        }
        else if(type == 'info') {
                icon = 'glyphicon glyphicon-info-sign';
        }
        else if(type == 'success') {
                icon = 'glyphicon glyphicon-ok-sign';
                delay = 30000;
        }	
        else if(type == 'warning') {
                icon = 'glyphicon glyphicon-warning-sign';
        }

        $.growl({
                title:title,
                message: message,
                icon: icon
        },{
                type: type,
                delay:delay,
                z_index: 50000,
                template:$("#growl-template").html()
        });
};
