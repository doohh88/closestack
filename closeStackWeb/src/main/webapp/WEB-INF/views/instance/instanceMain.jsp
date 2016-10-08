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
				<li class="sidebar-brand"><a href="<%=C.Url.OVERVIEW%>">
						CloseStack </a></li>
				<li><a href="<%=C.Url.OVERVIEW%>"> Overview </a></li>
				<li class="active"><a href="<%=C.Url.INSTANCE%>"><i></i>
						Instance </a></li>
				<li><a href="<%=C.Url.IMAGE%>"> Image </a></li>
				<li><a href="<%=C.Url.NETWORK%>"><i></i> Network </a></li>

			</ul>
		</div>
		<!-- /#sidebar-wrapper -->


		<div class="container-fluid"
			style="margin-left: 50px; margin-right: 50px">
			<div class="col-lg-12">
				<h1 class="page-header">Instance</h1>
			</div>


			<div align="right" style="margin-bottom: 20px">
				<button type="button" class="btn btn-success" id="start">인스턴스 시작</button>
				<button type="button" class="btn btn-success" id="reboot">인스턴스 리붓</button>
				<button type="button" class="btn btn-success" id="stop">인스턴스 종료</button>
				<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#createInstance">+ 인스턴스 생성</button>
				<button type="button" class="btn btn-danger" id="delete">- 인스턴스 삭제</button>
				<button type="button" class="btn btn-warning" data-toggle="modal" data-target="#createSnapShot">스냅샷 생성</button>
			</div>

			<form id="commandInstanceProc" action="commandInstanceProc"
				method="post">
				<table class="table table-bordered table-striped" data-height="299">
					<thead>
						<tr class="success">
							<th data-field="check"><input name="select" type="checkbox"
								value="select" /></th>
							<th data-field="instance_name">인스턴스 이름</th>
							<th data-field="image_name">이미지 이름</th>
							<th data-field="ip">IP 주소</th>
							<th data-field="size">크기</th>
							<th data-field="power_status">전원 상태</th>
							<th data-field="running_time">가동 시간</th>
						</tr>


						<c:forEach items="${instanceList}" var="dto">

							<tr>
								<td><input name="check" type="checkbox" value="${dto.name}" /></td>
								<td id="${dto.name}">${dto.name}</td>
								<td>${dto.flavor.name}</td>
								<td>${dto.ip}</td>
								<td>${dto.flavor.vCpus} vCpus | ${dto.flavor.ram} GB Ram | ${dto.flavor.disk} GB Disk</td>
								<td>${dto.status}</td>
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


	<div class="modal fade" id="createInstance" tabindex="-1" role="dialog"
		aria-labelledby="createInstance" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">X</button>
					<h4 class="modal-title" id="myModalLabel">인스턴스 생성</h4>
				</div>
				<form role="form" action="createInstanceProc" method="post">
					<fieldset>
						<div class="modal-body">
							<div class="col-md-6" padding="5px">

								<div class="form-group">
									<h5>인스턴스 이름:</h5>
									<input class="form-control" placeholder="Instance Id"
										name="instanceId" type="text" autofocus>
								</div>

								<div class="form-group">
									<h5>인스턴스 패스워드:</h5>
									<input class="form-control" placeholder="Instance PW"
										name="instancePw" type="passward" autofocus>
								</div>

								<div class="form-group">
									<h5>Flavor:</h5>
									<select class="dropdown selectpicker form-control"
										name="dropFlavorName" id="dropFlavorName">
										<thead>
											<c:forEach items="${flavorList}" var="dto">
												<option value="${dto.name}">
													<li><a href="#" data-bind="click: $parent.status">${dto.name}</a></li>
												</option>
											</c:forEach>
										</thead>
									</select>
								</div>
								<div class="form-group">
									<h5>OS:</h5>
									<select class="dropdown selectpicker form-control"
										name="osName" id="osName">
										<thead>
											<c:forEach items="${osList}" var="name">
												<option value="${name}"><li><a href="#"
														data-bind="click: $parent.status">${name}</a></li>
												</option>
											</c:forEach>
										</thead>
									</select>
								</div>
								<div class="form-group">
									<h5>type:</h5>
									<select class="dropdown selectpicker form-control" name="type"
										id="type">
										<option value="kvm"><li><a href="#"
												data-bind="click: $parent.status">kvm</a></li>
										</option>
										<option value="lxd"><li><a href="#"
												data-bind="click: $parent.status">lxd</a></li>
										</option>

									</select>
								</div>
							</div>
							<div class="col-md-6" padding="5px" style="">
								<div>
									<h6>Flavor 세부 정보</h6>
								</div>
								<div>
									<div class="col-md-6">
										<h4>
											<b>이름</b>
										</h4>
									</div>
									<div class="col-md-6">
										<h4 id="flavorName">tiny</h4>
									</div>
								</div>
								<div>
									<div class="col-md-6">
										<h4>
											<b>vCpus</b>
										</h4>
									</div>
									<div class="col-md-6">
										<h4 id="flavorvCpus">1</h4>
									</div>
								</div>
								<div>
									<div class="col-md-6">
										<h4>
											<b>Disk</b>
										</h4>
									</div>
									<div class="col-md-6">
										<h4 id="flavorDisk">1 GB</h4>
									</div>
								</div>

								<div>
									<div class="col-md-6">
										<h4>
											<b>RAM</b>
										</h4>
									</div>
									<div class="col-md-6">
										<h4 id="flavorRam">1 GB</h4>
									</div>
								</div>

							</div>

							<div style="margin-bottom: 380px"></div>


						</div>
						<div class="modal-footer">
							<input class="btn btn-primary" type="submit" value="생성"></input>
							<button type="button" class="btn btn-default"
								data-dismiss="modal">취소</button>
						</div>
					</fieldset>
				</form>
			</div>
		</div>
	</div>


	<div class="modal fade" id="createSnapShot" tabindex="-1" role="dialog"
		aria-labelledby="createSnapShot" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">X</button>
					<h4 class="modal-title" id="myModalLabel">스냅샷 생성</h4>
				</div>
				<form id="createSnapShotProc" role="form" action=createSnapShotProc method="post">
					<fieldset>
						<div class="modal-body">
							<div class="form-group">
								<h5>스냅샷 이름:</h5>
								<input class="form-control" placeholder="SnapShot Name"
									name="snapShotName" type="text" autofocus>
							</div>
							<div class="form-group">
								<h5>새로운 가상머신 이름:</h5>
								<input class="form-control" placeholder="new Instance Name"
									name="newName" type="text" autofocus>
							</div>
						</div>
					</fieldset>
				</form>		
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" id="snapshot" >생성</button>
					<button type="button" class="btn btn-default"
						data-dismiss="modal">취소</button>
				</div>

			</div>
		</div>
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
					인스턴스를 선택해주세요.
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

	<script
		src="<%=cp%>/resources/bower_components/metisMenu/dist/metisMenu.min.js"></script>
	<script src="<%=cp%>/resources/dist/js/sb-admin-2.js"></script>


	<script type="text/javascript">
 	$('#snapshot').on('click', function(e) {
 		console.log("snapshot click");
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
						"name", "instanceName").val(data);
				$('#createSnapShotProc').append($(input));
				$('#createSnapShotProc').submit();
				
				$('#createSnapShot').modal('toggle');
				
			}
			
	
		});
	
		
	
		$('#reboot').on('click', function(e) {
			console.log("reboot click");
			
			var data;
			var check=true;
			$.each($("input[name='check']:checked"), function() {
				data = $(this).val();
				check=false;
			});
			
			if(check){
				$('#error').modal('toggle');
			}else{
				
			
				var inputCommand = $("<input>").attr("type", "hidden").attr(
						"name", "command").val("restart");
				
				$('#commandInstanceProc').append($(inputCommand));
				
				var input = $("<input>").attr("type", "hidden").attr(
						"name", "instanceName").val(data);
				$('#commandInstanceProc').append($(input));
				$('#commandInstanceProc').submit();
			}
		});

		$('#stop').on('click', function(e) {

			console.log("stop click");
			
			var data;
			var check=true;
			$.each($("input[name='check']:checked"), function() {
				data = $(this).val();
				check=false;
			});
			
			if(check){
				$('#error').modal('toggle');
			}else{
				var inputCommand = $("<input>").attr("type", "hidden").attr("name", "command").val("stop");
				$('#commandInstanceProc').append($(inputCommand));
				
				var input = $("<input>").attr("type", "hidden").attr("name", "instanceName").val(data);
				$('#commandInstanceProc').append($(input));
				
				$('#commandInstanceProc').submit();
			}
		
		});
		
		$('#start').on('click', function(e) {

			console.log("start click");
			var data;
			var check=true;
			$.each($("input[name='check']:checked"), function() {
				data = $(this).val();
				check=false;
			});
			
			if(check){
				$('#error').modal('toggle');
			}else{
				var inputCommand = $("<input>").attr("type", "hidden").attr("name", "command").val("start");
				$('#commandInstanceProc').append($(inputCommand));
				
				var input = $("<input>").attr("type", "hidden").attr("name", "instanceName").val(data);
				$('#commandInstanceProc').append($(input));
				$('#commandInstanceProc').submit();
			}
		});

		$('#delete').on('click',function(e) {

			var data;
			var check=true;
			$.each($("input[name='check']:checked"), function() {
				data = $(this).val();
				check=false;
			});
			
			if(check){
				$('#error').modal('toggle');
			}else{
				var inputCommand = $("<input>").attr("type", "hidden").attr("name", "command").val("delete");
				$('#commandInstanceProc').append($(inputCommand));
			
				var input = $("<input>").attr("type", "hidden").attr("name", "instanceName").val(data);
				$('#commandInstanceProc').append($(input));
				
				$('#commandInstanceProc').submit();
			}
	
		});

		$("select#dropFlavorName").on("change", function(value) {
			var This = $(this);
			var selectedD = $(this).val();
			console.log(selectedD);

			var name;
			var vCpus;
			var ram;
			var disk;

			<c:forEach items="${flavorList2}" var="dto">

			var tmp = "${dto.name}"
			if (tmp == selectedD) {
				name = tmp
				vCpus = "${dto.vCpus}";
				disk = "${dto.disk}" + " GB";
				ram = "${dto.ram}" + " GB";
			}

			</c:forEach>

			$('#flavorName').text(name);
			$('#flavorvCpus').text(vCpus);
			$('#flavorRam').text(ram);
			$('#flavorDisk').text(disk);

		});

		$("select#osName").on("change", function(value) {
			var This = $(this);
			var selectedD = $(this).val();
			console.log(selectedD);
		});
	</script>

</body>


</html>


