<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%
	String cp = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href="<%=cp%>/resources/bower_components/bootstrap/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="<%=cp%>/resources/bower_components/metisMenu/dist/metisMenu.min.css"
	rel="stylesheet">
<link href="<%=cp%>/resources/dist/css/sb-admin-2.css" rel="stylesheet">
<link
	href="<%=cp%>/resources/bower_components/font-awesome/css/font-awesome.min.css"
	rel="stylesheet" type="text/css">
<title>Sign UP</title>
</head>
<body>
	<div class="container">
	<div class="row">
			<div class="col-md-4 col-md-offset-4">
				<div class="login-panel panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">Sign Up for Free</h3>
					</div>
					<div class="panel-body">
						<form method="post" action="signUpProc">
						<fieldset>
							<div class = "form-group">
								<input class = "form-control" type="text" name="username" placeholder="Pick a username">
							</div>
							<div class = "form-group">
								<input class = "form-control" type="password" name="password" placeholder="Create a password">
							</div>
							<div class = "form-group">
								<input class = "form-control" type="email" name="email" placeholder="Your email address">
							</div>
							<div class = "form-group">
								<input class = "btn btn-danger form-control" type="submit" name="commit" value="Sign Up">
							</div>
						</fieldset>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<script src="http://code.jquery.com/jquery-2.1.1.min.js"
		type="text/javascript"></script>
	<script
		src="<%=cp%>/resources/bower_components/jquery/dist/jquery.min.js"></script>
	<script
		src="<%=cp%>/resources/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
	<script
		src="<%=cp%>/resources/bower_components/metisMenu/dist/metisMenu.min.js"></script>
	<script src="<%=cp%>/resources/dist/js/sb-admin-2.js"></script>


</body>
</html>