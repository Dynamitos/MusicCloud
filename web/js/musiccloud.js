//
var availableFiles = [];
var filesToSync = [];

showPleaseWait = function () {
    $('#pleaseWaitDialog').modal('show');
};
hidePleaseWait = function () {
    $('#pleaseWaitDialog').modal('hide');
};

$('div.albumcard').click(function (event) {
    event.preventDefault();
    var content = $('.modal-body');
    content.empty();
    var title = $(this).attr("title");
    $('.modal-title').html(title);
    fetch('ModalServlet?album='+title + '&user=' + readCookie("user"), {
        method: 'GET'
    }).then(function(res)
    {
        return res.text();
    }).then(function(myData)
    {
        content.html(myData);
    });
    $(".modal-profile").modal({show: true});
});


$('div.trackcard').click(function (event) {
    event.preventDefault();
    var content = $('.modal-body');
    content.empty();
    var title = $(this).attr("title");
    $('.modal-title').html(title);
    fetch('AlbumServlet?track='+title + '&user=' + readCookie("user"), {
        method: 'GET'
    }).then(function(res)
    {
        return res.text();
    }).then(function(myData)
    {
        content.html(myData);
    });
    $(".modal-profile").modal({show: true});
});

$(document).ready(function () {
    fetch('FileServlet?user=' + readCookie("user"), {
        method: 'GET'
    }).then(function (res) {
        return res.json();
    }).then(function (myData) {
        availableFiles = myData;
    });
});

function sync() {
    $('#infoText').text('Syncing ' + filesToSync.length + ' files.');

    var formData = new FormData();

    formData.append("user", readCookie("user"));

    $.each(filesToSync, function (idx, item) {
        formData.append('file_' + idx, item);
    });
    showPleaseWait();
    fetch('MusicServlet', {
        method: 'POST',
        body: formData
    }).then(function (res) {
        return res.json();
    }).then(function (myData) {
        if (myData.success === true) {
            $('#infoText').text('Uploaded ' + filesToSync.length + ' files.');
        } else {
            $('#infoText').text('Failed uploading files');
        }
    }).then(function () {
        hidePleaseWait();
        window.location.reload();
    });
}

function changeFolder() {
    $('#filepicker').click();
}

function changed(event) {
    files = event.target.files;

    filesToSync = [];

    $.each(files, function (_, item) {
        if (item.name.endsWith(".mp3")) {
            if ($.inArray(item.name, availableFiles) === -1) {
                // Files that should be synced
                filesToSync.push(item);
            }
        }
    });

    $('#infoText').text('Found '
            + files.length
            + ' files in directory. Uploading '
            + filesToSync.length + ' of them.');
}

function viewAlbum(albumname)
{
    window.location.href = 'AlbumServlet?album=' + albumname;
}

function readCookie(key)
{
    var result;
    return (result = new RegExp('(?:^|; )' + encodeURIComponent(key) + '=([^;]*)').exec(document.cookie)) ? (result[1]) : null;
}