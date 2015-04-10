<%@ tag language="java" pageEncoding="UTF-8"%>

<%@attribute name="pagetitle" fragment="true" %>
<%@attribute name="head" fragment="true" required="false" %>
<%@attribute name="scripts" fragment="true" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    <title><jsp:invoke fragment="pagetitle"/></title>
    
    <link rel="icon" type="image/x-icon" href="http://www.memphis.edu/favicon.ico" />

    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">

    <!-- jQuery UI theme -->
    <link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/themes/smoothness/jquery-ui.min.css">

    <jsp:invoke fragment="head"/>
</head>

<body>

<!-- MAIN CONTENT -->
<div class="container-fluid">
	<div class="row">
	    <div class="col-md-12">
	        <div class="page-header">
	            <h1>
                    <a href="home">Demo Survey</a>
                    <small><jsp:invoke fragment="pagetitle"/></small>
                </h1>
	        </div>
	    </div>
	</div>
	
	<div class="row"><div class="col-md-12">&nbsp;</div></div>

    <jsp:doBody/>
    
    <div class="row"><div class="col-md-12">&nbsp;</div></div>
    
    <hr>

    <!-- FOOTER, ENDMATTER, and STATUS -->
    <footer>
        <img style="float:left;" class="img-responsive img-rounder" src="http://www.memphis.edu/shared/shared_rootsite/images/brandnew.jpg">
        <p style="float:right;"><small>Institute for Intelligent Systems, 2014</small></p>
        <br style="clear:both">
    </footer>
</div> <!-- /container --> 

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/jquery-ui.min.js"></script>
 
<!-- bootstrap -->
<script src="https://netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>

<!-- local scripts -->
<jsp:invoke fragment="scripts"/>

</body>
</html>