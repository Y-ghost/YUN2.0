<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!-- Header -->
<!-- 放到这里 是header里面的head有效，防止IE8时，自动响应为mobile style-->
<jsp:include page="common/header.jsp" />
<head>
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
	<input type="hidden" id="pageType" value="login"/>
		<form class="form-horizontal form-signin" id="form" role="form" onsubmit="return false;">
			<div class="text-center"><h2 style="margin-bottom:30px;">欢迎登录</h2></div>
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
			<div>
				<button type="submit" class="btn btn-primary btn-lg btn-block">登录</button>
			</div>
		</form>
			<div class="form-horizontal form-signin">
				<div class="col-sm-6">
					<a href="${requestScope.basePath}indexs/findAccount">忘记密码?</a>
				</div>
				<div class="col-sm-6 text-right" style="margin-bottom:20%;">
					没有账号？<a href="${requestScope.basePath}indexs/register">立即注册</a>
				</div>
			</div>
	</div>
	<!-- footer -->
	<jsp:include page="common/footer.jsp" />
	
	<script src="${requestScope.basePath}js/login.js"></script>
</body>
</html>