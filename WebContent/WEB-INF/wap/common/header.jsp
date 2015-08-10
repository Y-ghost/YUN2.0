<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en">
<head>
<link rel="stylesheet" href="${requestScope.basePath}bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${requestScope.basePath}bootstrap/css/font-awesome.min.css" />
<link rel="stylesheet" href="${requestScope.basePath}bootstrap/validation/css/bootstrapValidator.min.css" />
<link rel="stylesheet" href="${requestScope.basePath}css/base.css" />
<link rel="stylesheet" href="${requestScope.basePath}wap/css/yun.css" />
</head>
<body>
	<!-- Header -->
	<nav class="navbar navbar-default v-header" role="navigation">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"  data-toggle="collapse" data-target="#navbar">
				<span class="sr-only">Toggle navigation</span> 
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="index">
				<img src="${requestScope.basePath}images/logo.png" style="width:120px;height:30px;"/>
				<span class="navbar-right" style="margin-top:10px;">云灌溉</span>
			</a>
		</div>
		<div id="navbar" class="collapse navbar-collapse">
			<ul class="nav navbar-nav">
				<li><a href="${requestScope.basePath}indexs/index" title="首页"><i class="fa fa-home fa-2x"></i></a></li>
				<li><a href="javascript:void(0);" id="exist" title="退出"><i class="fa fa-power-off fa-2x"></i></a></li>
			</ul>
		</div>
	</div>
	</nav>
<input type="hidden" id="basePath" value="${requestScope.basePath}"/>
</body>
</html>