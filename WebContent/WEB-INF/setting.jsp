<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en">
<!-- Header -->
<!-- 放到这里 是header里面的head有效，防止IE8时，自动响应为mobile style-->
<jsp:include page="common/header.jsp" />
<head>
<link rel="stylesheet" href="${requestScope.basePath}bootstrap/css/default.css" />
<link rel="stylesheet" href="${requestScope.basePath}bootstrap/css/default.date.css" />
<link rel="stylesheet" href="${requestScope.basePath}bootstrap/css/default.time.css" />

<style type="text/css">
.control-label{
	padding-left:1px;
	padding-right:1px;
}
.form-control{
	padding-left:1px;
	padding-right:1px;
	width:65%;
	float:left;
}

.checkIE{margin-top:30px;}

#form-group {
	margin-bottom: 5px;
	margin-right: -5px;
	margin-left: -5px;
}

#has-error {
	color: #a94442;
}

.panel-heading {
	font-size: 14px;
}

#inputLab {
	padding-left: 3px;
	padding-right: 3px;
}

.fa-5 {
	font-size: 4em;
}

.fa-1 {
	font-size: 16px;
	margin-top: 25px;
}

.projectName {
	color: #555;
	border-radius: 4px;
	background-color: #fff;
	margin-top: 2px;
	font-size: 14px;
	border: 1px solid #ccc;
}

#projectName {
	padding: 1 0;
	margin-top: 0px;
}

select.input-sm {
	height: 34px;
	line-height: 34px;
}

button.closeCycle {
	-webkit-appearance: none;
	padding: 0;
	cursor: pointer;
	background: transparent;
	border: 0;
}

.closeCycle {
	float: right;
	font-size: 21px;
	font-weight: bold;
	line-height: 1;
	color: #000;
	text-shadow: 0 1px 0 #fff;
	filter: alpha(opacity = 20);
	opacity: .2;
}

.closeCycle:hover, .closeCycle:focus {
	color: #000;
	text-decoration: none;
	cursor: pointer;
	filter: alpha(opacity = 50);
	opacity: .5;
}
.panel-border {
	border: 1px solid #ddd
}

.panelLink {
	font-size: 14px;
	padding-top: 10px;
	margin-top: 3px;
	color:#000;
}
.panelLink span{
	line-height:22px;
}

.link-heading {
	font-size: 14px;
	padding-top: 10px;
	margin-top: 20px;
}
.border {
	border: 1px solid #ddd;
}

.header {
	border-radius: 0px;
	background-color: #f5f5f5;
}

.header a{
	padding-top:8px;
}

.header span{
	color: #000;
	font-size: 16px;
}
#homeLab{
	font-size:16px;
	font-family: 'Microsoft YaHei', Arial, "宋体", Helvetica, STHeiti;
}
.border-bottom {
	border-bottom: 1px solid #ddd;
}

.border-right {
	border-right: 1px solid #ddd;
}

.driver {
	height: 1px;
	margin: 9px 1px;
	overflow: hidden;
	background-color: #e5e5e5;
	border-bottom: 1px solid #ddd;
}

.no-border .list-group-item {
	border: 0;
	padding: 10px 0;
}

.ul-border-bottom .list-group-item {
	border-top: 0;
	border-left: 0;
	border-right: 0;
	border-bottom: 1px solid #ddd;
}

.float-right {
	float: right;
}

.font-size-18 {
	font-size: 18px;
	font-weight: bold;
}
.font-size-16 {
	font-size: 16px;
}

.node-container {
	margin: 20px 0;
}

.node-containerExt {
	margin-top: 1px;
	margin-bottom: 30px;
}

.node-tools {
	padding-left: 15px;
	padding-bottom: 15px;
}

.selectItem .form-control-feedback {
	top: 0;
	right: -15px !important;
}

.dialog-footer {
	padding: 15px;
	text-align: right;
	border-top: 1px solid #e5e5e5;
}

.cursor {
	cursor: pointer;
}

.table td a {
	max-width: 100px;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	display: inline-block;
}

.footer-bottom {
	clear: both;
	margin-top: 20px;
	margin-bottom: 20px;
	padding: 20px 0;
	text-align: center;
	word-wrap: break-word;
}

.footer {
	margin-top: 50px;
	border-top: 1px solid #e5e5e5;
	background-color: #f5f5f5;
}

body {
	color: #000;
	font-size: 12px;
	line-height: 22px;
	font-family: 'Microsoft YaHei', Arial, "宋体", Helvetica, STHeiti;
	background: #fff;
	_height: 100%;
}

input {
	font: 12px/16px "Microsoft YaHei", Arial, "宋体";
	color: #000;
}

.dialog-footer:after {
	clear: both;
}

.dialog-footer:before, .dialog-footer:after {
	display: table;
	content: " ";
}
</style>
</head>
<body>
	<div class="container-fluid" style="height: 100%">
		<!-- Container body -->
		<div class="row">
			<jsp:include page="common/left.jsp" />
			<div class="col-xs-2 col-md-2 border-right border-bottom hidden-xs">
				<ul class="list-group ul-border-bottom" id="projectList"
					style="height: 602px;">
  					<a href="javascript:void(0);" class="list-group-item panelLink link-heading active" data-name="project">添加项目信息 <span class="fa fa-angle-right navbar-right "></span></a>
  					<a href="javascript:void(0);" class="list-group-item panelLink " data-name="host">添加主机信息 <span class="fa fa-angle-right navbar-right"></span></a>
  					<a href="javascript:void(0);" class="list-group-item panelLink " data-name="equipment">搜索节点信息 <span class="fa fa-angle-right navbar-right"></span></a>
  					<a href="javascript:void(0);" class="list-group-item panelLink " data-name="setEquipment">节点详细参数设置 <span class="fa fa-angle-right navbar-right"></span></a>
  					<a href="javascript:void(0);" class="list-group-item panelLink " data-name="selectModel">查询节点模式设置 <span class="fa fa-angle-right navbar-right"></span></a>
  					<a href="javascript:void(0);" class="list-group-item panelLink " data-name="selectAutoParam">查询节点自控参数 <span class="fa fa-angle-right navbar-right"></span></a>
  					<a href="javascript:void(0);" class="list-group-item panelLink " data-name="selectTimeLen">查询节点时段参数 <span class="fa fa-angle-right navbar-right"></span></a>
  					<a href="javascript:void(0);" class="list-group-item panelLink " data-name="putData">节点赋值管理<span class="fa fa-angle-right navbar-right"></span></a>
				</ul>
			</div>
			<div class="equipment-container">
			</div>

		</div>
	</div>
	<jsp:include page="common/footer.jsp" />

	<!-- Fix IE 6-9  JSON object-->
	<script src="${requestScope.basePath}js/lib/json2.js"></script>
	<script src="${requestScope.basePath}js/lib/bootbox.min.js"></script>
	<script src="${requestScope.basePath}js/jquery.cityInfo.js"></script>
	<script src="${requestScope.basePath}js/setting/setting.js"></script>
	<script src="${requestScope.basePath}js/setting/setting.service.js"></script>
	<script src="${requestScope.basePath}js/setting/setting.project.js"></script>
	<script src="${requestScope.basePath}js/setting/setting.host.js"></script>
	<script src="${requestScope.basePath}js/setting/setting.equipment.js"></script>
	<script src="${requestScope.basePath}js/setting/setting.setEquipment.js"></script>
	<script src="${requestScope.basePath}js/setting/setting.equipmentUtils.js"></script>
	<script src="${requestScope.basePath}js/setting/setting.putData.js"></script>
	
	<script src="${requestScope.basePath}bootstrap/js/picker.js"></script>
	<script src="${requestScope.basePath}bootstrap/js/picker.date.js"></script>
	<script src="${requestScope.basePath}bootstrap/js/picker.time.js"></script>
</body>
</html>
