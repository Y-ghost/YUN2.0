<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
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
<head>
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
					<li class="current">验证身份</li>
					<li>设置新密码<em></em></li>
					<li>完成<em></em></li>
				</ul>
			</div>
		</div>
		<div>
			<form id="form" role="form" onsubmit="return false;">
				<div class="form-group input-group-container">
					<div class="input-group input-group-lg">
						<span class="input-group-addon"><span
							class="glyphicon glyphicon-user"></span></span> <input type="text"
							class="form-control" placeholder="用户名/登录邮箱" name="loginname"
							id="loginname">
					</div>
				</div>

				<div class="form-group input-group-container">
					<div class="input-group input-group-lg">
						<span class="input-group-addon"><span
							class="glyphicon glyphicon-lock"></span></span> <input type="number"
							class="form-control" placeholder="验证码" name="validNum"
							id="validNum">
					</div>
				</div>
				<div class="form-group input-group-container">
					<div class="input-group input-group-lg">
						<div class="code cursor"></div>
						<a class="codeBtn cursor">看不清，换一张</a>
					</div>
				</div>
				
				<div class="h40">
					<span id="errorMessage" class="error_alert"></span>
				</div>
				<div>
					<button type="submit" class="btn btn-primary btn-lg btn-block" id="submitBtn">下一步</button>
				</div>
			</form>
		</div>
	</div>
	
	 <div class="container" style="display:none;" id="email-suc-con">
            <div>
            	<div class="h40"></div>
                <div class="h40"></div>
            	<div class="ope-tips">
            		<div class="ope-tips-success">
               			<i></i>邮件已发送到邮箱
               			<span class="email-address" id="emailMark"></span>
              		 </div>
            	</div>
               <div class="hepl-b-tips">请在60小时内点击邮件中的链接完成验证</div>
               </div> 
    </div>
<input type="hidden" id="basePath" value="${requestScope.basePath}"/>
<input type="hidden" id="pageType" value="findAccount"/>
<script src="${requestScope.basePath}wap/js/lib/zepto.js"></script>
<script src="${requestScope.basePath}wap/js/lib/touch.js"></script>
<script src="${requestScope.basePath}js/settings.js"></script>
<script src="${requestScope.basePath}js/common.js"></script>
<script src="${requestScope.basePath}wap/js/ajax.js"></script>
<script src="${requestScope.basePath}wap/js/findAccount.js"></script>
</body>
</html>