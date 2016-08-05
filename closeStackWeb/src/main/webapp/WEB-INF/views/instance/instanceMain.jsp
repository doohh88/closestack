<%@page import="com.ssmksh.closestack.C"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%
	String cp = request.getContextPath();
%>
<%--ContextPath 선언 --%>
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
		<link href="<%=cp%>/resources/bootstrap/css/simple-sidebar.css"
			rel="stylesheet">
		<title>Instance</title>
	</head>
	<body>
		<div id="wrapper">
	
			<!-- Sidebar -->
			<div id="sidebar-wrapper">
				<ul class="sidebar-nav nav">
					<li class="sidebar-brand"><a href="<%=C.Url.OVERVIEW%>"> CloseStack </a></li>
					<li><a href="<%=C.Url.OVERVIEW%>"> Overview </a></li>
					<li  class="active" ><a href="<%=C.Url.INSTANCE%>"><i></i> Instance </a></li>
					<li><a href="<%=C.Url.IMAGE%>"> Image </a></li>
					<li><a href="<%=C.Url.NETWORK%>"><i></i> Network </a></li>
	
				</ul>
			</div>
			<!-- /#sidebar-wrapper -->
	
	
			<div class="container-fluid">
	
				<h3>Instance Page</h3>
			</div>
		</div>
	
	
		</div>
		<!-- /#wrapper -->
	
	
	
		<script src="http://code.jquery.com/jquery-2.1.1.min.js"
			type="text/javascript"></script>
		<script
			src="<%=cp%>/resources/bower_components/jquery/dist/jquery.min.js"></script>
		<script
			src="<%=cp%>/resources/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
	
	</body>
</html>