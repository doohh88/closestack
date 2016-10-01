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
<title>Image</title>
</head>
<body>
	<div id="wrapper">

		<!-- Sidebar -->
		<div id="sidebar-wrapper">
			<ul class="sidebar-nav nav">
				<li class="sidebar-brand"><a href="<%=C.Url.OVERVIEW%>">
						CloseStack </a></li>
				<li><a href="<%=C.Url.OVERVIEW%>"> Overview </a></li>
				<li><a href="<%=C.Url.INSTANCE%>"><i></i> Instance </a></li>
				<li class="active"><a href="<%=C.Url.IMAGE%>"> Image </a></li>
				<li><a href="<%=C.Url.NETWORK%>"><i></i> Network </a></li>

			</ul>
		</div>
		<!-- /#sidebar-wrapper -->


		<div class="container-fluid"
			style="margin-left: 50px; margin-right: 50px">

				<div class="col-lg-12">
                    <h1 class="page-header">Image</h1>
                </div>


			<div align="right" style="margin-bottom: 20px">
				<button type="button" class="btn btn-danger" data-toggle="modal"
					data-target="#createImage">+ 이미지생성</button>
			</div>


			<table class="table table-bordered table-striped" data-height="299">
				<thead>
					<tr>
						<th data-field="check"><input name="select" type="checkbox"
							value="select" /></th>
						<th data-field="image_name">이미지 이름</th>
						<th data-field="image_type">타입</th>
						<th data-field="image_status">상태</th>
						<th data-field="image_share">공용</th>
						<th data-field="image_protect">보호함</th>
						<th data-field="image_format">포맷</th>
					</tr>

					<c:forEach items="${imageList}" var="dto">
						<tr>
							<td><input name="select" type="checkbox" value="select" /></td>
							<td><a href="<%=C.Url.IMAGE_DETAIL%>">${dto.name}</a></td>
							<td>${dto.type}</td>
							<td>${dto.status}</td>
							<td>${dto.share}</td>
							<td>${dto.protect}</td>
							<td>${dto.format}</td>

						</tr>
					</c:forEach>
				</thead>
			</table>



		</div>
	</div>


	</div>
	<!-- /#wrapper -->


	<div class="modal fade" id="createImage" tabindex="-1" role="dialog"
		aria-labelledby="createImage" aria-hidden="true">
		
			<form role="form" action="crateImageproc" method="post">
			<fieldset>
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">X</button>
					<h4 class="modal-title" id="myModalLabel">이미지 생성</h4>
				</div>
				<div class="modal-body">
							<div class="form-group">
								<p>이름:*</p>
								<input class="form-control" placeholder="Image name"
									name="ImageName" type="text" autofocus>
							</div>
							<div class="form-group">
								<p>설명:*</p>
								<input class="form-control" placeholder=""
									name="IamgeDescription" type="text">
							</div>
							<div class="form-group">
								<div class="dropdown open">
									<button class="btn btn-secondary dropdown-toggle" type="button"
										id="dropdownMenuButton" data-toggle="dropdown"
										aria-haspopup="true" aria-expanded="false">Dropdown
										button</button>
									<div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
										<a class="dropdown-item" href="#">Action</a> <a
											class="dropdown-item" href="#">Another action</a> <a
											class="dropdown-item" href="#">Something else here</a>
									</div>
								</div>
						
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button type="button" class="btn btn-primary">Save changes</button>
				</div>
			</div>
		</div>
		
		</fieldset>
		</form>
		
	</div>



	<script src="http://code.jquery.com/jquery-2.1.1.min.js"
		type="text/javascript"></script>
	<script
		src="<%=cp%>/resources/bower_components/jquery/dist/jquery.min.js"></script>
	<script
		src="<%=cp%>/resources/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>

</body>
</html>