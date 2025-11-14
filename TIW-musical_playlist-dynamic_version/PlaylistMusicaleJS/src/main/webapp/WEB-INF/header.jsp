<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!-- Logged user and logout button -->
<div class="row my-2 p-1">
	<h2 class="col-sm-4" >User: <c:out value="${currentUser.username}"></c:out></h2>
    <button class="col-sm-2 btn btn-primary btn-sm" id="logoutID" onclick="location.href='logout'" type="button">LOGOUT</button>
</div>
<hr>
<div id="alertID" class="alertmessage alert alert-success" style="display:none"></div>
 