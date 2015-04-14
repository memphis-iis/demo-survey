<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:basepage>

<jsp:attribute name="pagetitle">Survey Demo</jsp:attribute>

<jsp:attribute name="head">
    <style>
        .errormsg {
            margin-top: 12px;
        }
    </style>
</jsp:attribute>

<jsp:attribute name="scripts">
<script>
    function strNotBlank(s) {
        return typeof s === "string" && s && $.trim(s);
    }

    $(function(){
        //We specify ID's for all our form elements, but we should also specify a name
        $("input").each(function(idx, ele) {
            var e = $(ele);
            e.attr("name", e.attr("id"));
        });

        $("#surveyForm").submit(function(evt){
            //Clear any previous error display
            $(".errormsg").remove();
            $(".form-group").removeClass("has-error");

            //Set up for error display
            var errors = [];
            var errorCheck = function(groupId, errMsg, booleanTest) {
                if (!booleanTest) {
                    errors.push(errMsg);
                    $("#"+groupId).addClass("has-error");
                }
            };

            try {
                errorCheck(
                    "participantGroup",
                    "Participant Code is required",
                    strNotBlank($("#participantCode").val())
                );
                errorCheck(
                    "dogGroup",
                    "Dog Breed is required",
                    strNotBlank($("#favoriteDogBreed").val())
                );

                var favNum = $("#favoriteNumber").val();

                errorCheck(
                    "numberGroup",
                    "Favorite Number is required",
                    strNotBlank(favNum)
                );
                errorCheck(
                    "numberGroup",
                    "Favorite Number must be a Number",
                    !isNaN(parseInt(favNum))
                );
            }
            catch(e) {
                //Whoops!
                error.push("Unknown Error: " + e.toString());
            }

            if (errors.length) {
                //Error - submission can't go forward
                evt.preventDefault();

                var alert = $('<div class="alert alert-danger errormsg" role="alert"></div>');
                alert.append($("<strong>Your survey has some problems</strong>"));

                $.each(errors, function(index, value) {
                    alert.append($('<div></div>')
                        .append($('<span class="glyphicon glyphicon-thumbs-down"></span>'))
                        .append($('<span></span>').text(" " + value))
                    );
                });

                $("#surveyButtonContainer").append(alert);
            }
        });
    });
</script>
</jsp:attribute>

<jsp:body>

<div class="row">
    <div class="col-md-12">
        <form class="form-horizontal" id="surveyForm" method="post">

            <div class="form-group" id="participantGroup">
                <label for="participantCode" class="col-md-2 control-label">Participant Code</label>
                <div class="col-md-4">
                    <input type="text" class="form-control" placeholder="Your unique code"
                        id="participantCode" value="${survey.participantCode}" >
                </div>
            </div>

            <div class="form-group" id="dogGroup">
                <label for="favoriteDogBreed" class="col-md-2 control-label">Favorite Dog Breed</label>
                <div class="col-md-4">
                    <input type="text" class="form-control" placeholder="Your Favorite Dog Breed"
                        id="favoriteDogBreed" value="${survey.favoriteDogBreed}" >
                </div>
            </div>

            <div class="form-group" id="catGroup">
                <label for="catLover" class="col-md-2 control-label">Cat Lover</label>
                <div class="col-md-4">
                    <div class="checkbox">
                        <label><input id="catLover" type="checkbox"> Yes! I love cats</label>
                    </div>
                </div>
            </div>

            <div class="form-group" id="numberGroup">
                <label for="favoriteNumber" class="col-md-2 control-label">Favorite Number</label>
                <div class="col-md-4">
                    <input type="text" class="form-control" placeholder="Your Favorite INTEGER"
                        id="favoriteNumber" value="" >
                </div>
            </div>

            <div class="form-group">
                <div class="col-md-offset-2 col-md-4" id="surveyButtonContainer">
                   <button type="submit" class="btn btn-primary">Record My Answers</button>
                </div>
            </div>
        </form>
    </div>
</div>

</jsp:body>

</t:basepage>
