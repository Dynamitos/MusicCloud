<%-- 
    Document   : login
    Created on : Apr 14, 2018, 4:10:19 PM
    Author     : Dynamitos
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">

        <title>Music Cloud Login</title>

        <!-- Bootstrap core CSS -->
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css" integrity="sha384-9gVQ4dYFwwWSjIDZnLEWnxCjeSWFphJiwGPXr1jddIhOegiu1FwO5qRGvFXOdJZ4" crossorigin="anonymous">

        <!-- Custom styles for this template -->
        <link href="signin.css" rel="stylesheet">
    </head>

    <body class="text-center">
        <form class="form-signin" action="UserServlet" method="POST">
            <img class="mb-4" src="https://png.icons8.com/android/50/000000/musical-notes.png" alt="" width="72" height="72">
            <h1 class="h3 mb-3 font-weight-normal">Please sign in</h1>
            <span class="text-danger font-weight-normal">${error}</span>
            <label for="inputEmail" class="sr-only">Email address</label>
            <input id="inputEmail" class="form-control" placeholder="Email address" required="" autofocus="" type="email">
            <label for="inputPassword" class="sr-only">Password</label>
            <input id="inputPassword" class="form-control" placeholder="Password" required="" type="password">
            <div class="checkbox mb-3">
                <label>
                    <input value="remember-me" type="checkbox"> Remember me
                </label>
            </div>
            <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
            <p class="mt-5 mb-3 text-muted">© 2017-2018</p>
        </form>


    </body>
</html>
