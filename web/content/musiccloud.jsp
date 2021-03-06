<%-- 
    Document   : music
    Created on : Apr 14, 2018, 12:45:47 PM
    Author     : Dynamitos
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.css">

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <title>Music Cloud</title>
    </head>
    <body>
        <header>
            <div class="collapse bg-dark" id="navbarHeader">
                <div class="container">
                    <div class="row">
                        <div class="col-sm-8 col-md-7 py-4">
                            <h4 class="text-white">About</h4>
                            <p class="text-muted">Music Cloud is a service to synchronize and manage your Songs across multiple devices - independent of the local Player used.</p>
                        </div>
                        <div class="col-sm-4 offset-md-1 py-4">
                            <h4 class="text-white">Social Media</h4>
                            <ul class="list-unstyled">
                                <li><a href="http://twitter.com" class="text-white">Follow on Twitter</a></li>
                                <li><a href="http://facebook.com" class="text-white">Like on Facebook</a></li>
                                <li><a href="http://mail.google.com" class="text-white">Email me</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            <nav class="navbar navbar-dark bg-dark box-shadow">
                <div class="container d-flex">
                    <div class="navbar-header">
                        <a href="#" class="navbar-brand d-flex align-items-center">
                            <img src="https://png.icons8.com/android/50/000000/musical-notes.png">
                            <strong>Music Cloud</strong>
                        </a>
                    </div>
                    
                    <ul class="nav navbar-nav navbar-right">
                        <li><a href="#"><i class="fas fa-user"></i> Sign Up</a></li>
                        <li><a href="#"><i class="fas fa-male"></i> Login</a></li>
                        <li>
                            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarHeader" aria-controls="navbarHeader" aria-expanded="false" aria-label="Toggle navigation">
                                <span class="navbar-toggler-icon"></span>
                            </button>
                        </li>
                    </ul>

                </div>
            </nav>
        </header>
        <input id="filepicker" type="file" onchange="changed(event)" style="display:none;" webkitdirectory multiple/>

        <!--Progress bar -->
        <div class="modal fade" id="pleaseWaitDialog" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h1>Processing...</h1>
                    </div>
                    <div class="modal-body">
                        <div class="progress">
                            <div class="progress-bar progress-bar-success progress-bar-striped" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%">
                                <span class="sr-only">100% Complete (success)</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade modal-profile" tabindex="-1" role="dialog" aria-labelledby="modalProfile" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <!--<button class="close" type="button" data-dismiss="modal">×</button>-->
                        <h3 class="modal-title"></h3>
                    </div>
                    <div class="modal-body">
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
        <main role="main">
            <section class="jumbotron text-center">
                <div class="container">
                    <h1 class="jumbotron-heading">Music Cloud</h1>
                    <p class="lead text-muted">Synchronize and manage your music - no matter where you are or what device you are using</p>
                    <p>
                        <a href="#" onclick="sync()" class="btn btn-primary my-2">Synchronize</a>
                        <a href="#" onclick="changeFolder()" class="btn btn-secondary my-2">Manage directory</a>
                    </p>
                    <p id="infoText" class="lead">Please upload some music.</p>
                </div>
            </section>
            <div class="container">

                <ul class="nav nav-tabs justify-content-center" id="nav-tab" role="tablist">
                    <li class="nav-item col-md-4">
                        <a class="nav-link active" id="album-tab" data-toggle="tab" href="#albums" role="tab" aria-controls="album" aria-selected="true">Albums</a>
                    </li>
                    <li class="nav-item col-md-4">
                        <a class="nav-link" id="track-tab" data-toggle="tab" href="#tracks" role="tab" aria-controls="track" aria-selected="false">Tracks</a>
                    </li>
                    <li class="nav-item col-md-4">
                        <a class="nav-link" id="artist-tab" data-toggle="tab" href="#artists" role="tab" aria-controls="artist" aria-selected="false">Artists</a>
                    </li>
                </ul>
            </div>
            <div class="tab-content" id="myTabContent">
                <div class="tab-pane fade show active" id="albums" role="tabpanel" aria-labelledby="album-tab">
                    <div class="album py-5 bg-light">
                        <div class="container">
                            <div class="row">
                                <c:forEach var="album" items="${albums}">
                                    <div class="albumcard col-md-4" title="${album.name}" data-toggle="modal" data-target=".modal-profile-lg">
                                        <div class="card mb-4 box-shadow">
                                            <img class="card-img-top" style="height: 225px; width: 100%; display: block;" src="ImageServlet?file=${album.coverFilename}" data-holder-rendered="true">
                                            <div class="card-body">
                                                <p class="card-text">${album.name}</p>
                                                <div class="d-flex justify-content-between align-items-center">
                                                    <!--<div class="btn-group">
                                                        <button type="button" class="btn btn-sm btn-outline-secondary">View</button>
                                                        <button type="button" onclick="editAlbum()" class="btn btn-sm btn-outline-secondary">Edit</button>
                                                    </div>-->
                                                    <small class="text-muted">${album.durationMinutes} mins</small>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>

                        </div>
                    </div>
                </div>
                <div class="tab-pane fade" id="tracks" role="tabpanel" aria-labelledby="track-tab">
                    <div class="album py-5 bg-light">
                        <div class="container">
                            <div class="row">
                                <c:forEach var="song" items="${tracks}">
                                    <div class="trackcard col-md-4" title="${song.title}" data-toggle="modal" data-target=".modal-profile-lg">
                                        <div class="card mb-4 box-shadow">
                                            <img class="card-img-top" style="height: 225px; width: 100%; display: block;" src="ImageServlet?file=${song.coverFilename}" data-holder-rendered="true">
                                            <div class="card-body">
                                                <p class="card-text">${song.title}</p>
                                                <div class="d-flex justify-content-between align-items-center">
                                                    <!--<div class="btn-group">
                                                        <button type="button" class="btn btn-sm btn-outline-secondary">View</button>
                                                        <button type="button" onclick="editAlbum()" class="btn btn-sm btn-outline-secondary">Edit</button>
                                                    </div>-->
                                                    <small class="text-muted">${song.durationMinutes} mins</small>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>

                        </div>

                    </div>
                </div>
                <div class="tab-pane fade" id="artists" role="tabpanel" aria-labelledby="artist-tab">
                    <div class="album py-5 bg-light">
                        <div class="container">
                            <div class="row">
                                <c:forEach var="artist" items="${artists}">
                                    <div class="trackcard col-md-4" title="${artist.name}" data-toggle="modal" data-target=".modal-profile-lg">
                                        <div class="card mb-4 box-shadow">
                                            <img class="card-img-top" style="height: 225px; width: 100%; display: block;" src="data:image/svg+xml;charset=UTF-8,%3Csvg%20width%3D%22348%22%20height%3D%22225%22%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20viewBox%3D%220%200%20348%20225%22%20preserveAspectRatio%3D%22none%22%3E%3Cdefs%3E%3Cstyle%20type%3D%22text%2Fcss%22%3E%23holder_162f67e62a8%20text%20%7B%20fill%3A%23eceeef%3Bfont-weight%3Abold%3Bfont-family%3AArial%2C%20Helvetica%2C%20Open%20Sans%2C%20sans-serif%2C%20monospace%3Bfont-size%3A17pt%20%7D%20%3C%2Fstyle%3E%3C%2Fdefs%3E%3Cg%20id%3D%22holder_162f67e62a8%22%3E%3Crect%20width%3D%22348%22%20height%3D%22225%22%20fill%3D%22%2355595c%22%3E%3C%2Frect%3E%3Cg%3E%3Ctext%20x%3D%22116.70000076293945%22%20y%3D%22120.3%22%3EThumbnail%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3C%2Fsvg%3E" data-holder-rendered="true">
                                            <div class="card-body">
                                                <p class="card-text">${artist.name}</p>
                                                <div class="d-flex justify-content-between align-items-center">
                                                    <!--<div class="btn-group">
                                                        <button type="button" class="btn btn-sm btn-outline-secondary">View</button>
                                                        <button type="button" onclick="editAlbum()" class="btn btn-sm btn-outline-secondary">Edit</button>
                                                    </div>-->
                                                    <small class="text-muted">${artist.songs.size()} Songs</small>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>

                        </div>

                    </div>
                </div>   
            </div>

        </main>

        <footer class="text-muted">
            <div class="container">
                <p class="float-right">
                    <a href="#">Back to top</a>
                </p>
                <a href="https://icons8.com">Icon pack by Icons8</a>
            </div>
        </footer>


        <!-- Bootstrap core JavaScript
        ================================================== -->
        <!-- Placed at the end of the document so the pages load faster -->
        <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js" integrity="sha384-cs/chFZiN24E4KMATLdqdvsezGxaGsi4hLGOzlXwp5UZB1LY//20VyM2taTB4QvJ" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.js" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/howler/2.0.9/howler.js" type="text/javascript"></script>
        <script defer src="https://use.fontawesome.com/releases/v5.0.10/js/all.js" integrity="sha384-slN8GvtUJGnv6ca26v8EzVaR9DC58QEwsIk9q1QXdCU8Yu8ck/tL/5szYlBbqmS+" crossorigin="anonymous"></script>
        <script src="js/musiccloud.js" type="text/javascript"></script>


        <svg xmlns="http://www.w3.org/2000/svg" width="348" height="225" viewBox="0 0 348 225" preserveAspectRatio="none" style="display: none; visibility: hidden; position: absolute; top: -100%; left: -100%;"><defs><style type="text/css"></style></defs><text x="0" y="17" style="font-weight:bold;font-size:17pt;font-family:Arial, Helvetica, Open Sans, sans-serif">Thumbnail</text></svg>
    </body>
</html>
