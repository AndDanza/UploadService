$(document).ready(function () {
    var form = $("#form");
    form.on('submit', function (event) {
        event.preventDefault();

        var formData = new FormData();
        var file = $('#file')[0].files[0];
        formData.append('file', file);

        $.ajax({
            url: form[0].action,
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-Upload-File', file.name);
            },
            success: function(response){
                window.location.replace(response);
            },
            error: function(){
                window.location.replace(window.document.location.origin);
            }
        });
    });
});