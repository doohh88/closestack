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
				<button type="button" class="btn btn-primary" data-toggle="modal"
					data-target="#createImage">+ 이미지 생성</button>
				<button type="button" class="btn btn-danger" id="delete">- 이미지 삭제</button>
			</div>
			<form id="deleteSnapShotProc" action="deleteSnapShotProc"
				method="post">
				<table class="table table-bordered table-striped" data-height="299">
					<thead>
						<tr>
							<th data-field="check"><input name="select" type="checkbox"
								value="select" /></th>
							<th data-field="image_name">이미지 이름</th>
							<th data-field="image_type">가상머신 이름</th>
							<th data-field="image_status">새로운 이름</th>
							<th data-field="image_share">타입</th>
							<th data-field="image_protect">만들어진 시간</th>
						</tr>
	
						<c:forEach items="${snapShopList}" var="dto">
							<tr>
								<td><input name="check" type="checkbox" value="${dto.name}" /></td>
								<td>${dto.name}</td>
								<td>${dto.vmName}</td>
								<td>${dto.newName}</td>
								<td>${dto.type}</td>
								<td>${dto.time}</td>
	
							</tr>
						</c:forEach>
					</thead>
				</table>
			</form>



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
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default"
								data-dismiss="modal">Close</button>
							<button type="button" class="btn btn-primary">Save
								changes</button>
						</div>
					</div>
			</fieldset>
		</form>

	</div>

	<div class="modal fade" id="error" tabindex="-1" role="dialog"
		aria-labelledby="createSnapShot" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">X</button>
					<h4 class="modal-title" id="myModalLabel">Error</h4>
				</div>
				<div class="modal-body" style="padding:20px">
					이미지를 선택해주세요.
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default"
						data-dismiss="modal">확인</button>
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


	<script type="text/javascript">
	$('#delete').on('click', function(e) {
 		console.log("delete click");
			var data;
			var check=true;
			$.each($("input[name='check']:checked"), function() {
				data = $(this).val();
				check=false;
			});
			
			if(check){
				$('#error').modal('toggle');
			}else{
				var input = $("<input>").attr("type", "hidden").attr(
						"name", "snapShotName").val(data);
				$('#deleteSnapShotProc').append($(input));
				$('#deleteSnapShotProc').submit();
				
				$('#delete').modal('toggle');
				
			}
			
	
	});
	
	</script>
</body>
</html>