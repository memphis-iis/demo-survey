
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:basepage>

<jsp:attribute name="pagetitle">Survey Output</jsp:attribute>

<jsp:attribute name="head">
    <!-- We need to jQuery UI and DataTable style sheets for data tables -->
    <link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/themes/smoothness/jquery-ui.min.css">
    <link rel="stylesheet" type="text/css" href="http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/css/jquery.dataTables.css">
</jsp:attribute>

<jsp:attribute name="scripts">

<!-- We need the jQuery UI and DataTables script files  -->
<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/jquery-ui.min.js"></script>
<script type="text/javascript" charset="utf8" src="http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/jquery.dataTables.min.js"></script>

<script>
$(function(){
    //We specify ID's for all our form elements, but we should also specify a name
    $("input").each(function(idx, ele) {
        var e = $(ele);
        e.attr("name", e.attr("id"));
    });

    $(".make-data-table").dataTable();
});
</script>
</jsp:attribute>

<jsp:body>

<div class="row">
    <div class="col-md-12">
        <form class="form-horizontal" id="dataDumpForm" method="post">

            <div class="form-group" id="dataCodeGroup">
                <label for="datacode" class="col-md-2 control-label">Participant Code</label>
                <div class="col-md-4">
                    <input type="text" class="form-control" placeholder="You should enter 'show me the data'"
                        id="datacode" value="" >
                </div>
            </div>

            <div class="form-group">
                <div class="col-md-offset-2 col-md-4" id="submitContainer">
                   <button type="submit" class="btn btn-primary">Show Data</button>
                </div>
            </div>
        </form>
    </div>
</div>

<c:if test="${not empty surveys}">
<br/><br/>
<div class="row">
    <div class="col-md-offset-1 col-md-8">
        <table class="table table-condensed make-data-table">
            <thead><tr>
                <th>Participant</th>
                <th>Favorite Dog</th>
                <th>Likes Cats?</th>
                <th>Favorite Number</th>
            </tr></thead>

            <tbody>
            <c:forEach items="${surveys}" var="survey">
                <tr>
                    <td>${survey.participantCode}</td>
                    <td>${survey.favoriteDogBreed}</td>
                    <td>${survey.catLover}</td>
                    <td>${survey.favoriteNumber}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</c:if>


</jsp:body>

</t:basepage>
