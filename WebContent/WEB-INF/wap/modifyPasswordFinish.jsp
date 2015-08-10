<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="keywords" content="节水灌溉,手机灌溉,智能灌溉,河南锐利特计算机科技有限公司,Rainet,锐利特科技,云灌溉"/>
<meta name="Description" content="Rainet云灌溉系统(yun.rainet.com.cn)是由河南锐利特计算机科技有限公司研发的一款远程智能灌溉监控系统，涉及到传感器技术、自动控制技术、计算机技术、无线通信技术等多种高新技术,锐利特科技一家从事物联网智能灌溉设备研发、生产、销售以及提供信息技术服务的高新技术企业。"/> 
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<link rel="shortcut icon" href="${requestScope.basePath}images/favicon.ico" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<title>找回密码</title>
<link rel="stylesheet" href="${requestScope.basePath}bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${requestScope.basePath}bootstrap/css/font-awesome.min.css" />
<link rel="stylesheet" href="${requestScope.basePath}bootstrap/validation/css/bootstrapValidator.min.css" />
<link rel="stylesheet" href="${requestScope.basePath}css/base.css" />
<link rel="stylesheet" href="${requestScope.basePath}wap/css/yun.css" />
</head>
<body>
<div class="container-fluid v-header">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed">
				<span class="sr-only">Toggle navigation</span> 
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="#">
				<img src="${requestScope.basePath}images/logo.png" style="width:120px;height:30px;"/>
				<span class="navbar-right" style="margin-top:10px;">云灌溉</span>
			</a>
		</div>
	</div>
	<div class="container v-content" id="v-content">
		<div id="passport-title">
			<div class="wrap" style="margin-bottom:30px;">
			</div>
		</div>
		<div id="passport-finder" class="wrap">
			<div class="passport-progress">
				<ul>
					<li>验证身份</li>
					<li>设置新密码<em></em></li>
					<li class="current">完成<em></em></li>
				</ul>
			</div>
		</div>
		<div>
			<div>
				<div class="text-center" style="margin-top:60px;">
					
				</div>
				<div class="ope-tips">
            		<div class="ope-tips-success">
               			<i></i>密码修改成功
              		 </div>
            	</div>
            	<div class="h40"></div>
            	<div>
					<a href="${requestScope.basePath}indexs/login" class="btn btn-primary btn-lg btn-block">马上登录</a>
				</div>
			</div>
		</div>
	</div>
</body>
</html>