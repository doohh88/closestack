<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<% String cp = request.getContextPath(); %> <%--ContextPath 선언 --%>
<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
 	<link href="<%=cp%>/resources/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
 	<link href="<%=cp%>/resources/bower_components/metisMenu/dist/metisMenu.min.css" rel="stylesheet">
 	<link href="<%=cp%>/resources/dist/css/sb-admin-2.css" rel="stylesheet">
 	<link href="<%=cp%>/resources/bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
  	
 	<link href="<%=cp%>/resources/bower_components/morrisjs/morris.css" rel="stylesheet">
  	
  	<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->	
    <title>Overview</title>
</head>
<body>
 		<div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Flot</h1>
                </div>
            </div>
            
            
            <div class="row">
             	<div class="col-xs-6">
                	<div class="flot-chart">
                         <div class="flot-chart-content" id="flot-pie-chart"></div>
                 	</div>
            	 </div>
            </div>
            
        </div>


    <script src="http://code.jquery.com/jquery-2.1.1.min.js" type="text/javascript"></script>
    <script src="<%=cp%>/resources/bower_components/jquery/dist/jquery.min.js"></script>
    <script src="<%=cp%>/resources/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
    <script src="<%=cp%>/resources/bower_components/metisMenu/dist/metisMenu.min.js"></script>
 
    
    <script src="<%=cp%>/resources/bower_components/flot/excanvas.min.js"></script>
    <script src="<%=cp%>/resources/bower_components/flot/jquery.flot.js"></script>
    <script src="<%=cp%>/resources/bower_components/flot/jquery.flot.pie.js"></script>
    <script src="<%=cp%>/resources/bower_components/flot/jquery.flot.resize.js"></script>
    <script src="<%=cp%>/resources/bower_components/flot/jquery.flot.time.js"></script>
    <script src="<%=cp%>/resources/bower_components/flot.tooltip/js/jquery.flot.tooltip.min.js"></script>
   <%--  <script src="<%=cp%>/resources/js/flot-data.js"></script> --%>
    <script src="<%=cp%>/resources/js/flot-pie-data.js"></script> 
    <script src="<%=cp%>/resources/dist/js/sb-admin-2.js"></script>
</body>
</html>


