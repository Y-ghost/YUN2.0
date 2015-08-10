<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en">
<!-- Header -->
<!-- 放到这里 是header里面的head有效，防止IE8时，自动响应为mobile style-->
<jsp:include page="common/header.jsp" />
<head>
	<link rel="stylesheet" href="${requestScope.basePath}datatable/css/dataTables.bootstrap.css" />
	<style type="text/css">
	.checkIE{margin-top:30px;}
	</style>
</head>
<body>
	<div class="container-fluid" style="height:100%">
	<!-- Container body -->
		<div class="row">
			<jsp:include page="common/left.jsp" />
			<div class="col-xs-2 col-md-2 border-right hidden-xs border-bottom" style="height:643px;">
				<ul class="list-group ul-border-bottom">
  					<a href="javascript:void(0);" class="list-group-item panelLink link-heading active" data-name="project">项目信息管理 <span class="fa fa-angle-right navbar-right"></span></a>
  					<a href="javascript:void(0);" class="list-group-item panelLink " data-name="host">主机信息管理 <span class="fa fa-angle-right navbar-right"></span></a>
  					<a href="javascript:void(0);" class="list-group-item panelLink " data-name="node">节点信息管理 <span class="fa fa-angle-right navbar-right"></span></a>
  					<a href="javascript:void(0);" class="list-group-item panelLink " data-name="systemLog">报警信息管理 <span class="fa fa-angle-right navbar-right"></span></a>
  					<a href="javascript:void(0);" class="list-group-item panelLink " data-name="soil">土壤信息管理 <span class="fa fa-angle-right navbar-right"></span></a>
  					<a href="javascript:void(0);" class="list-group-item panelLink " data-name="plants">植物信息管理 <span class="fa fa-angle-right navbar-right"></span></a>
  					<a href="javascript:void(0);" class="list-group-item panelLink " data-name="user">用户信息管理<span class="fa fa-angle-right navbar-right"></span></a>
				</ul>
			</div>
			<div class="col-xs-9 col-md-9">
				<div class="node-container">
					<form class="form-horizontal" role="form" id="searchForm" onsubmit="return false;">
						<div class="form-group project">
							<label class="col-xs-2 col-md-2 control-label">项目信息查询</label>
							<div class="col-xs-2 col-md-2">
								<select class="form-control" id="province">
								</select>
							</div>
							<div class="col-xs-2 col-md-2">
								<select class="form-control" id="city">
								</select>
							</div>
						</div>
						<div class="form-group host" style="display:none;">
							<label class="col-xs-2 col-md-2 control-label">主机信息查询</label>
							<div class="col-xs-2 col-md-2">
								<select class="form-control" id="projectNameList">
									<option value="-1">--请选择项目--</option>
								</select>
							</div>
						</div>
						<div class="form-group node" style="display:none;">
							<label class="col-xs-2 col-md-2 control-label">节点信息查询</label>
							<div class="col-xs-2 col-md-2">
								<select class="form-control" id="projectNameListForNode">
									<option value="-1">--请选择项目--</option>
								</select>
							</div>
						</div>
						
						<div class="form-group log" style="display:none;">
							<label class="col-xs-2 col-md-2 control-label">报警信息查询</label>
							<div class="col-xs-2 col-md-2">
								<select class="form-control" id="projectNameListForNode">
									<option value="-1">--请选择项目--</option>
									<option value='0'>采集异常</option>
									<option value='1'>湿度报警</option>
								</select>
							</div>
						</div>
						
						<div class="form-group soil" style="display:none;">
							<label class="col-xs-2 col-md-2 control-label">土壤信息查询</label>
							<div class="col-xs-2 col-md-2">
								<select class="form-control" id="province">
								</select>
							</div>
							<div class="col-xs-2 col-md-2">
								<select class="form-control" id="city">
								</select>
							</div>
						</div>
						
						<div class="form-group plants" style="display:none;">
							<label class="col-xs-2 col-md-2 control-label">植物信息查询:</label>
							<div class="col-xs-4 col-md-4">
								<input id="name" type="text" class="form-control" name="name" placeholder="植物名"/>
							</div>
							<div class="col-xs-2 col-md-2">
								<button id="plantsSearchBtn" type="button" class="btn btn-success">查询</button>
							</div>
						</div>
						
						<div class="form-group user" style="display:none;">
							<label class="col-xs-2 col-md-2 control-label">用户信息查询:</label>
							<div class="col-xs-4 col-md-4">
								<input id="loginName" type="text" class="form-control" name="loginname" placeholder="登录名"/>
							</div>
							<div class="col-xs-2 col-md-2">
								<button id="userSearchBtn" type="button" class="btn btn-success">查询</button>
							</div>
						</div>
						
					</form>
					<div class="table-responsive" id="tableContainer">
						<table class="table table-striped table-hover" id="table">
							<thead>
							</thead>
							<tbody>
							</tbody>
						</table>
						
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<jsp:include page="common/footer.jsp" />
	<script src="${requestScope.basePath}datatable/js/jquery.dataTables.js"></script>
	<script src="${requestScope.basePath}datatable/js/dataTables.bootstrap.js"></script>
	<!-- Fix IE 6-9  JSON object-->
	<script src="${requestScope.basePath}js/lib/json2.js"></script>
	<script src="${requestScope.basePath}js/lib/bootbox.min.js"></script>
	<script src="${requestScope.basePath}js/jquery.cityInfo.js"></script>
	<script src="${requestScope.basePath}js/message/messages.service.js"></script>
	<script src="${requestScope.basePath}js/message/messages.util.js"></script>
	<script src="${requestScope.basePath}js/message/messages.project.js"></script>
	<script src="${requestScope.basePath}js/message/messages.host.js"></script>
	<script src="${requestScope.basePath}js/message/messages.eqt.js"></script>
	<script src="${requestScope.basePath}js/message/messages.systemLog.js"></script>
	<script src="${requestScope.basePath}js/message/messages.soil.js"></script>
	<script src="${requestScope.basePath}js/message/messages.plants.js"></script>
	<script src="${requestScope.basePath}js/message/messages.users.js"></script>
	<script src="${requestScope.basePath}js/message/messages.js"></script>
	
</body>
</html>
