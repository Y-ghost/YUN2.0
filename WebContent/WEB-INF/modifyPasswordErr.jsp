<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en">
<!-- Header -->
<!-- 放到这里 是header里面的head有效，防止IE8时，自动响应为mobile style-->
<jsp:include page="common/header.jsp" />
<head>
<style type="text/css">
.a {
	height:auto !important;
	height:500px; /*假定最低高度是200px*/
	min-height:500px; 
	}
#headLine{
	display:none;
}
#exist{
	display:none;
}
</style>
</head>
<body>
<div style="font-family: '微软雅黑,宋体';"class="a">
	<div style="height:300px; color: #999999;text-align:center;line-height: 300px;font-family: 'Copperplate Gothic Bold';font-size:120pt;"><strong><font color="red">4</font></strong><strong><font color="green">0</font></strong><strong><font color="blue">0</font></strong></div>
	<div style="padding:10px;">
		<div style="height:30px;font-family: 'Copperplate Gothic Bold'; font-size: 14px;text-align:center;line-height: 30px;">抱歉，该连接已经失效，请返回重新找回密码...</div>
	</div>
	<div style="padding:10px;">
		<div style="height:30px;text-align:center;line-height: 30px;"><a href="findAccount" style="font-family: 'Copperplate Gothic Bold'; font-size: 20px; color: #1b926c;">返回>> </a></div>
	</div>
</div>

<jsp:include page="common/footer.jsp" />
</body>
</html>
