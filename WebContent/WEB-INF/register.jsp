<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!-- Header -->
<!-- 放到这里 是header里面的head有效，防止IE8时，自动响应为mobile style-->
<jsp:include page="common/header.jsp" />
<head>
<title>服务条款</title>
<style type="text/css">
.form-signin {
	width : 500px;
	margin: 30px auto;
}
.checkIE{margin-top:30px;}
.form-signin div {
	padding : 5px 15px 0px 15px;
}
#headLine{
	display:none;
}
.input-group-lg > .form-control{
font-size: 16px;
}
.input-group-container .form-control-feedback{top:15px !important; right:30px !important;}
.input-group-container .help-block{padding : 0 15px;}
</style>
</head>
<body>
	<div class="container">
	<input type="hidden" id="pageType" value="register"/>
		<form class="form-horizontal form-signin " id="form" role="form" onsubmit="return false;">
			<div class="text-center"><h3>用户注册</h3></div>
			<div class="form-group input-group-container">
				<div class="input-group input-group-lg">
					<span class="input-group-addon"><span class="glyphicon glyphicon-user"></span></span> 
					<input type="text" class="form-control" placeholder="用户名" name="loginname" id="loginname">
				</div>
			</div>
			
			<div class="form-group input-group-container">
				<div class="input-group input-group-lg">
					<span class="input-group-addon"><span class="glyphicon glyphicon-lock"></span></span> 
					<input type="password" class="form-control" placeholder="密码" name="password" id="password">
				</div>
			</div>
			
			<div class="form-group input-group-container">
				<div class="input-group input-group-lg">
					<span class="input-group-addon"><span class="glyphicon glyphicon-envelope"></span></span> 
					<input type="text" class="form-control" placeholder="邮箱" name="email" id="email">
				</div>
			</div>
			<div class="form-group input-group-container">
				<div class="input-group input-group-lg">
					<span class="input-group-addon"><span class="glyphicon glyphicon-phone"></span></span> 
					<input type="text" class="form-control" placeholder="电话" name="phone" id="phone">
				</div>
			</div>
			<div class="form-group input-group-container">
				<div class="input-group input-group-lg">
					<span class="input-group-addon"><span class="glyphicon glyphicon-map-marker"></span></span> 
					<input type="text" class="form-control" placeholder="地址" name="address" id="address">
				</div>
			</div>
			<div>
				<button type="submit" class="btn btn-primary btn-lg btn-block">免费注册</button>
			</div>
		</form>
			<div class="form-horizontal form-signin">
				<div class="col-sm-6 ">
					<input type="checkbox" checked name="serviceAgreement" id="serviceAgreement"/>&nbsp;&nbsp;接受<a href="${requestScope.basePath}indexs/userAgreement" target="_blank">用户服务协议</a>
				</div>
				<div class="col-sm-6 text-right" style="margin-bottom:20%;">
					已有账号，<a href="${requestScope.basePath}indexs/login">立马登录</a>
				</div>
			</div>
	</div>
	<!-- footer -->
	<jsp:include page="common/footer.jsp" />
	
	<script src="${requestScope.basePath}js/login.js"></script>
</body>
</html>