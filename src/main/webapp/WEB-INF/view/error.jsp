<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:basepage>
    
<jsp:attribute name="pagetitle">Survey Error</jsp:attribute>

<jsp:attribute name="head"></jsp:attribute>

<jsp:attribute name="scripts"></jsp:attribute>

<jsp:body>

<div class="row">
    <div class="col-md-6">
        
        <div class="panel panel-danger">
            <div class="panel-heading">
                <h2 class="panel-title">An Error Occurred!</h2>
            </div>
	        <div class="panel-body">
	            <h3 class="alert alert-danger">${errorMessage}</h3>
	            <hr>
                <div class="alert alert-info">${errorDetails}</div>
	        </div>
        </div>

    </div>
</div>

</jsp:body>

</t:basepage>
