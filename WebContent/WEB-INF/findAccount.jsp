<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!-- header -->
<jsp:include page="common/header.jsp" />
<head>
<link rel="stylesheet"
	href="${requestScope.basePath}css/findAccount.css" />
	
<style type="text/css">
	.checkIE{margin-top:30px;}
</style>
</head>
<body>
<input type="hidden" id="pageType" value="findAccount"/>
	<div class="container">
		<div id="passport-title">
			<div class="wrap">
				<h2>找回密码</h2>
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
			<form class="form-horizontal form-signin " id="form" role="form"
				onsubmit="return false;">
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
							class="glyphicon glyphicon-lock"></span></span> <input type="text"
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
				<div>
					<button type="submit" class="btn btn-primary btn-lg btn-block">下一步</button>
				</div>
			</form>
		</div>
	</div>
	<!-- footer -->
	<jsp:include page="common/footer.jsp" />
	<script src="${requestScope.basePath}js/findAccount.js"></script>
	<script src="${requestScope.basePath}js/lib/bootbox.min.js"></script>
</body>
</html>