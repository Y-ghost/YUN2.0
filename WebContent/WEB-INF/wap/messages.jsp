<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String module = request.getParameter("type");
	String pId = request.getParameter("pId");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en">
<head>
<meta name="keywords" content="节水灌溉,手机灌溉,智能灌溉,河南锐利特计算机科技有限公司,Rainet,锐利特科技,云灌溉"/>
<meta name="Description" content="Rainet云灌溉系统(yun.rainet.com.cn)是由河南锐利特计算机科技有限公司研发的一款远程智能灌溉监控系统，涉及到传感器技术、自动控制技术、计算机技术、无线通信技术等多种高新技术,锐利特科技一家从事物联网智能灌溉设备研发、生产、销售以及提供信息技术服务的高新技术企业。"/> 
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<link rel="shortcut icon" href="${requestScope.basePath}images/favicon.ico" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<title>Rainet云灌溉-把灌溉装进口袋</title>
</head>
<jsp:include page="common/header.jsp" />
<body>
	<div class="container-fluid v-content" id="v-content" style="padding:0;position: relative;">
	<!-- Container body -->
		<div>
		    <div class="menu" id="wrapper">
		    	<div id="scroller">
		    		<div class="pullDown" id="pullDown">
						<span class="pullDownIcon">&nbsp;</span>
						<span class="pullDownLabel">下拉刷新</span>
					</div>
<!-- 					<ul class="ul-data"> -->
<!-- 		     			<li> -->
<!-- 						<div class="detail" > -->
<!-- 							<div class="fl" style="width:80%;"> -->
<!-- 								<label class="detail-item" style="font-size:16px;">水吧 </label><br/> -->
<!-- 								<label>状态:</label><span>开启</span> -->
<!-- 		     				</div> -->
<!-- 		     				<div class="fr" id=""> -->
<!-- 		     					<i class="fa fa-toggle-on fa-3x"></i> -->
<!-- 		     				</div> -->
<!-- 		     			</div> -->
<!-- 		     			</li> -->
<!-- 		     			<li> -->
<!-- 		     			<div class="detail" id=""> -->
<!-- 							<div class="fl" style="width:80%;" > -->
<!-- 								<label class="detail-item" style="line-height:40px;font-size:16px;">施肥罐 </label><br/> -->
<!-- 		     				</div> -->
<!-- 		     				<div class="fr"> -->
<!-- 		     					<i class="fa fa-toggle-off fa-3x"></i> -->
<!-- 		     				</div> -->
<!-- 		     			</div> -->
<!-- 		     			</li> -->
<!-- 					</ul> -->
					
					<ul class="ul-data" id="list">
					</ul>
					<div class="" id="pullUp">
					<span class="pullUpIcon"></span>
						<span class="pullUpLabel"></span>
				</div>
				</div>
			</div>
		</div>
	</div>
	<div class="busy-layer" id="noData" style="display:none;">
		  <div class="busy-text"><span style="color: #666262;">无数据</span></div>
	</div>
	<input type="hidden" id="module" value="<%=module%>"/>
	<input type="hidden" id="pId" value="<%=pId%>"/>
	<jsp:include page="common/footer.jsp" />
	<script src="${requestScope.basePath}js/message/messages.service.js"></script>
	<script src="${requestScope.basePath}js/message/messages.util.js"></script>
	<script src="${requestScope.basePath}wap/js/message/messages.project.js"></script>
	<script src="${requestScope.basePath}wap/js/message/messages.eqt.js"></script>
	<script src="${requestScope.basePath}wap/js/message/messages.js"></script>

</body>
</html>
