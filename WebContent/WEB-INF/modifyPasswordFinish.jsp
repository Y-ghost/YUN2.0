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
</head>
<body>
<input type="hidden" id="pageType" value="modifyPassword"/>
	<div class="container">
		<div id="passport-title">
			<div class="wrap">
				<h2>找回密码</h2>
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
			<div class="form-horizontal form-signin " >
				<div class="form-group input-group-container" style="margin-top:100px;">
					恭喜你！密码修改成功，马上<a href="${requestScope.basePath}indexs/login">登录</a>看看吧！
				</div>
			</div>
		</div>
	</div>
	<!-- footer -->
	<jsp:include page="common/footer.jsp" />
</body>
</html>