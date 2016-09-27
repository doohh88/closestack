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

<link
	href="<%=cp%>/resources/bower_components/metisMenu/dist/metisMenu.min.css"
	rel="stylesheet">
<link href="<%=cp%>/resources/dist/css/sb-admin-2.css" rel="stylesheet">
<link
	href="<%=cp%>/resources/bower_components/font-awesome/css/font-awesome.min.css"
	rel="stylesheet" type="text/css">

<link href="<%=cp%>/resources/bower_components/morrisjs/morris.css"
	rel="stylesheet">

<title>Overview</title>
</head>
<body>
	<div id="wrapper">

		<!-- Sidebar -->
		<div id="sidebar-wrapper">
			<ul class="sidebar-nav nav">
				<li class="sidebar-brand"><a href="<%=C.Url.OVERVIEW%>">
						CloseStack </a></li>
				<li class="active"><a href="<%=C.Url.OVERVIEW%>"> Overview
				</a></li>
				<li><a href="<%=C.Url.INSTANCE%>"><i></i> Instance </a></li>
				<li><a href="<%=C.Url.IMAGE%>"> Image </a></li>
				<li><a href="<%=C.Url.NETWORK%>"><i></i> Network </a></li>

			</ul>
		</div>
		<!-- /#sidebar-wrapper -->


		<div class="container-fluid">
			<div class="col-lg-12">
				<h1 class="page-header">Overview</h1>
			</div>

			<div class="col-lg-12">
				<h4>Simple Summary</h4>
			</div>
			<div style="margin-left: 70px; margin-right: 70px">
				<div class="row">
					<div class="col-xs-3">
						<div class="flot-chart" style="height: 150px">
							<div class="flot-chart-content" id="flot-pie-chart"></div>
						</div>

					</div>

					<div class="col-xs-3">
						<div class="flot-chart" style="height: 150px">
							<div class="flot-chart-content" id="flot-pie-chart2"></div>
						</div>
					</div>
					<div class="col-xs-3">
						<div class="flot-chart" style="height: 150px">
							<div class="flot-chart-content" id="flot-pie-chart3"></div>
						</div>
					</div>
					<div class="col-xs-3">
						<div class="flot-chart" style="height: 150px">
							<div class="flot-chart-content" id="flot-pie-chart4"></div>
						</div>
					</div>
				</div>
				<div class="row" style="margin-top: 20px">
					<div class="col-xs-3" align="center">
						<label>인스턴스</label>
					</div>

					<div class="col-xs-3" align="center">

						<label>VCPUs</label>
					</div>
					<div class="col-xs-3" align="center">
						<label>RAM</label>
					</div>

					<div class="col-xs-3" align="center">
						<label>유동 IP</label>
					</div>
				</div>

				<div class="row" style="margin-top: 20px">
					<div class="col-xs-3" align="center">
						<label>10중에서 6사용 중</label>
					</div>

					<div class="col-xs-3" align="center">

						<label>20중에서 13 사용 중</label>
					</div>
					<div class="col-xs-3" align="center">
						<label>50GB 중에서 26GB 사용 중</label>
					</div>

					<div class="col-xs-3" align="center">
						<label>10중에서 1사용 중</label>
					</div>
				</div>

			</div>
			<!--  pie  -->

			<table class="table table-bordered table-striped" data-height="299"
				style="margin-top: 100px">
				<thead>
					<tr class="success">
						<th data-field="instance_name">인스턴스 이름</th>
						<th data-field="vcpus">VCPUs</th>
						<th data-field="disk">디스크</th>
						<th data-field="ram">RAM</th>
						<th data-field="running_time">가동 시간</th>
					</tr>

					<tr>
						<th>kafka1</th>
						<th>1</th>
						<th>20</th>
						<th>2GB</th>
						<th>1 mohth, 3weeks</th>
					</tr>

					<tr>
						<th>hadoop</th>
						<th>4</th>
						<th>40</th>
						<th>8GB</th>
						<th>3weeks, 2 days</th>
					</tr>

				</thead>
			</table>

		</div>
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
	<script
		src="<%=cp%>/resources/bower_components/metisMenu/dist/metisMenu.min.js"></script>

	<script src="<%=cp%>/resources/bower_components/flot/excanvas.min.js"></script>
	<script src="<%=cp%>/resources/bower_components/flot/jquery.flot.js"></script>
	<script
		src="<%=cp%>/resources/bower_components/flot/jquery.flot.pie.js"></script>
	<script
		src="<%=cp%>/resources/bower_components/flot/jquery.flot.resize.js"></script>
	<script
		src="<%=cp%>/resources/bower_components/flot/jquery.flot.time.js"></script>
	<script
		src="<%=cp%>/resources/bower_components/flot.tooltip/js/jquery.flot.tooltip.min.js"></script>

	<script src="<%=cp%>/resources/js/flot-pie-data.js"></script>

</body>
</html>