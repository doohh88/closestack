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

<script src="http://code.jquery.com/jquery-2.1.1.min.js"
		type="text/javascript"></script>
<script type="text/javascript">

		$(function(){
				
			console.log("overview ready")
			var totalInstance="${totalInstance}" ;
			var totalVCPUs="${totalVCPUs}" ;
			var totalIP="${totalIP}" ;
			var totalRAM="${totalRAM}" ;
			var totalDisk="${totalDisk}";
			
			var usingInstance="${usingInstance}" ;
			var usingVCPUs="${usingVCPUs}" ;
			var usingIP="${usingIP}" ;
			var usingRAM="${usingRAM}" ;
			
			var rVCPUs = "${rVCPUs}" ;
			var rRam = "${rRam}" ;
			var rDisk = "${rDisk}"
			var rIP ="${rIP}" ;
			
			
			plot("active Instance", "deActive Instance","#flot-pie-chart",usingInstance,totalInstance - usingInstance)
			plot("active VCPUs", "deActive VCPUs","#flot-pie-chart2",usingVCPUs ,totalVCPUs - usingVCPUs)
			plot("active RAM(GB)", "deActive RAM(GB)","#flot-pie-chart3",usingRAM ,totalRAM - usingRAM)
			plot("active Ip", "deActive Ip","#flot-pie-chart4",usingIP,totalIP - usingIP)
			
		
			plot("using VCPUs", "total VCPUs","#flot-pie-chart5",totalVCPUs ,rVCPUs - totalVCPUs)
			plot("using RAM(GB)", "total RAM(GB)","#flot-pie-chart6",totalRAM ,rRam - totalRAM)
			plot("using DISK(GB)", "total DISK(GB)","#flot-pie-chart7",totalDisk,rDisk - totalDisk)
			plot("using Ip", "total Ip","#flot-pie-chart8",totalIP,rIP-totalIP)
			
		});

		
		function plot(usingLabel, totalLabel, target, usingCnt ,totalCnt) {

			var data = [ {
				label :usingLabel,
				data : usingCnt
			}, {
				label :totalLabel,
				data : totalCnt
			} ];

			var plotObj = $.plot($(target), data, {
				series : {
					pie : {
						show : true,
						radius : 1,
						label : {
							show : false,
						},
					}
				},
				grid : {
					hoverable : true
				},
				tooltip : true,
				tooltipOpts : {
					content : "%p.0%, %s", // show percentages, rounding to 2
					// decimal places
					shifts : {
						x : 20,
						y : 0
					},
					defaultTheme : false
				},
				legend : {
					show : false
				}
			});
		}

	</script>

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
				<h4>total Resources</h4>
			</div>
			<div style="margin-left: 70px; margin-right: 70px; margin-bottom:50px">
				<div class="row">
					<div class="col-xs-3">
						<div class="flot-chart" style="height: 150px">
							<div class="flot-chart-content" id="flot-pie-chart5"></div>
						</div>

					</div>

					<div class="col-xs-3">
						<div class="flot-chart" style="height: 150px">
							<div class="flot-chart-content" id="flot-pie-chart6"></div>
						</div>
					</div>
					<div class="col-xs-3">
						<div class="flot-chart" style="height: 150px">
							<div class="flot-chart-content" id="flot-pie-chart7"></div>
						</div>
					</div>
					<div class="col-xs-3">
						<div class="flot-chart" style="height: 150px">
							<div class="flot-chart-content" id="flot-pie-chart8"></div>
						</div>
					</div>
				</div>
				<div class="row" style="margin-top: 20px">
					<div class="col-xs-3" align="center">
						<label>VCPUs</label>
					</div>

					<div class="col-xs-3" align="center">

						<label>RAM</label>
					</div>
					<div class="col-xs-3" align="center">
						<label>DISK</label>
					</div>

					<div class="col-xs-3" align="center">
						<label>유동 IP</label>
					</div>
				</div>

				<div class="row" style="margin-top: 20px">
					<div class="col-xs-3" align="center">
						<label>${rVCPUs}개 중에서 ${totalVCPUs}개 사용 중</label>
					</div>

					<div class="col-xs-3" align="center">

						<label>${rRam}GB 중에서 ${totalRAM}GB 사용 중</label>
					</div>
					<div class="col-xs-3" align="center">
						<label>${rDisk}GB 중에서 ${totalDisk}GB 사용 중</label>
					</div>

					<div class="col-xs-3" align="center">
						<label>${rIP}개 중에서 ${totalIP}개 사용 중</label>
					</div>
				</div>

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
						<label>${totalInstance}개 중에서 ${usingInstance}개 사용 중</label>
					</div>

					<div class="col-xs-3" align="center">

						<label>${totalVCPUs}개 중에서 ${usingVCPUs}개 사용 중</label>
					</div>
					<div class="col-xs-3" align="center">
						<label>${totalRAM}GB 중에서 ${usingRAM}GB 사용 중</label>
					</div>

					<div class="col-xs-3" align="center">
						<label>${totalIP}개 중에서 ${usingIP}개 사용 중</label>
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
					</tr>
					<c:forEach items="${instanceList}" var="dto">
						<tr>
							<td>${dto.name}</td>
							<td>${dto.flavor.vCpus}</td>
							<td>${dto.flavor.disk} GB</td>
							<td>${dto.flavor.ram} GB</td>

					
						</tr>
					</c:forEach>
			

				</thead>
			</table>

		</div>
	</div>
	</div>


	</div>
	<!-- /#wrapper -->




	
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

	<%-- <script src="<%=cp%>/resources/js/flot-pie-data.js"></script> --%>




</body>
</html>