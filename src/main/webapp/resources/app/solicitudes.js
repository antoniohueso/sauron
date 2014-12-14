$(document).ready(function () {
    var editor = CKEDITOR.replace("descripcion", {
        // Define the toolbar groups as it is a more accessible solution.
        toolbarGroups: [
            {"name": "basicstyles", "groups": ["basicstyles"]},
            {"name": "links", "groups": ["links"]},
            {"name": "paragraph", "groups": ["list", "blocks"]},
            {"name": "document", "groups": ["mode"]},
            {"name": "insert", "groups": ["insert"]},
            {"name": "styles", "groups": ["styles"]},
            {"name": "about", "groups": ["about"]}
        ],
        // Remove the redundant buttons from toolbar groups defined above.
        removeButtons: 'Subscript,Superscript'
    });

    $("#save").click(function (e) {
        e.preventDefault();

        editor.updateElement();

        var params = $("form").serialize();

        var modal = $("#msg-modal");
        var modalTitle = $("#msg-modal-title");
        var modalBody = $("#msg-modal-body");
        modalBody.html('');

        $.ajax({
            url: $("form").attr("action"),
            data: params,
            method: 'POST',
            dataType: 'json'
        }).success(function (r) {
            var o = r;
            var id = $("input[name='id']").val();
            console.log("ID: ",id, " , ",(id === null));
            if(id === null || id.length === 0) {
                $('form')[0].reset();
                editor.setData(null);
                modalTitle.text('Solicitud creada');
            }
            else {
                modalTitle.text('Solicitud modificada');
            }
            modalBody.append("<p class='text-success'><span class='glyphicon glyphicon-ok'></span> ID de solicitud1: "+ o.id+"</p>");
            modal.modal({ backdrop:false,show:true});
        }).error(function (err) {
            var errors = err.responseJSON;
            
            modalTitle.text('Por favor revisa el formulario.');
                
            for (var i in errors) {
                modalBody.append("<p class='text-danger'><span class='glyphicon glyphicon-remove'></span> ID de solicitud1: "+errors[i]+"</p>");
            }
            modal.modal({ backdrop:false,show:true});
        });
    });

});

