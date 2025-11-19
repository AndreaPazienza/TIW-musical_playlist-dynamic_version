<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
	<!-- Head of the page -->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" href="resources/style/login.css">
	    <link rel="stylesheet" href="resources/style/bootstrap_4.4.1.min.css">
		<title>LoginPage</title>
	</head>
	<!-- Body of the page --> 	
	<body>
		<!-- Login form -->
		<c:url value="/login" var="loginUrl"/>	
		<form class="form-signin" method="post" action="${loginUrl}">
			<h1 class="h3 mb-3 font-weight-normal">Playlist manager</h1><hr>
			<!-- Error message -->
			<c:choose>
				<c:when test="${error == true}">
					<div id="alertID" class="alertmessage alert alert-danger">Error: <c:out value="${errorMessage}"></c:out></div>
				</c:when>
			</c:choose>
			<label for="username" class="sr-only">Username</label>
			<input class="form-control" type="text" placeholder="Enter username" name="username" autofocus required/>
			<label for="password" class="sr-only">Password</label>
			<input class="form-control" type="password" placeholder="Enter password" name="password" required/>
			<button class="btn btn-lg btn-primary btn-block" type="submit">LOGIN</button>
		</form>			
	</body>
</html>