<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en">
<!-- Header -->
<!-- 放到这里 是header里面的head有效，防止IE8时，自动响应为mobile style-->
<jsp:include page="common/header.jsp" />
<head>
<link rel="stylesheet" href="${requestScope.basePath}css/jqpagination.css" />
<link rel="stylesheet" href="${requestScope.basePath}bootstrap/css/default.css" />
<link rel="stylesheet" href="${requestScope.basePath}bootstrap/css/default.date.css" />
<link rel="stylesheet" href="${requestScope.basePath}bootstrap/css/default.time.css" />
<style type="text/css">
#form-group {
	margin-bottom: 5px;
	margin-right: -5px;
	margin-left: -5px;
}

#has-error {
	color: #a94442;
}
.checkIE{margin-top:30px;}

#inputLab {
	padding-left: 3px;
	padding-right: 3px;
}

.panel-heading {
	font-size: 14px;
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
					style="height: 490px;">
				</ul>
				<div class="gigantic pagination" style="margin-bottom: 50px;">
					<a href="javascript:void(0);" class="first" data-action="first">&laquo;</a>
					<a href="javascript:void(0);" class="previous" data-action="previous">&lsaquo;</a>
					<input type="text" readonly="readonly" style="width: 50px; text-align: center;padding:8px 0;" />
					<a href="javascript:void(0);" class="next" data-action="next">&rsaquo;</a>
					<a href="javascript:void(0);" class="last" data-action="last">&raquo;</a>
				</div>
			</div>
			<div class="col-xs-9 col-md-9">
				<div class="node-container col-xs-12 col-md-12" style="margin-bottom:10px;">
					<div class="node-tools">
						<label class="col-xs-2 col-md-2 control-label" style="padding-left:1;padding-right:3;font-size:14px;line-height: 35px;">统计日期：<input type="hidden" class="projectId" /></label>
						<div class="col-xs-5 col-md-5 form-group">
							<input type="text" class="form-control startDate cursor" style="width:120px;float:left;margin-right:30px;" name="startDate" placeholder="开始日期" />
							<input type="text" class="form-control endDate cursor" style="width:120px;float:left;" name="endDate" placeholder="结束日期" />
						</div>
						<label class="col-xs-2 col-md-2 control-label" style="padding-left:3;padding-right:3;font-size:14px;line-height: 35px;">统计方式：<input type="hidden" class="projectId" /></label>
						<div class="col-xs-3 col-md-3  control-label" style="padding-left:1px;padding-right:10px;font-size:14px;">
							<div class="radio-inline" style="padding-top: 7px;"><label><input type="radio" name="statisticType" value="0" checked />水量</label></div>
							<div class="radio-inline" style="margin-left:20px;padding-top: 7px;"><label><input type="radio" name="statisticType" value="1" />湿度</label></div>
						</div>
					</div>
				</div>
				<div class="node-containerExt col-xs-12 col-md-12">
					<div class="node-tools" style="margin-bottom:40px;">
						<label class="col-xs-2 col-md-2 control-label" style="padding-left:1;padding-right:3;font-size:14px;line-height: 35px;">统计对象：<input type="hidden" class="projectId" /></label>
						<div class="col-xs-3 col-md-3" style="padding-left:15px;padding-right:10px;">
							<select class="form-control" id="equipmentList">
								<option value="-1">-全部-</option>
							</select>
						</div>
						<div class="col-xs-5 col-md-5" style="float:right;">
							<button type="button" class="btn btn-success statisticBtn" style="margin-right:70px;">统&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;计</button>
							<button type="button" class="btn btn-warning exportBtn">导出Excel</button> 
						</div>
					</div>
					<div class="table-responsive statistic" id="tableContainer">
						<!-- 统计图显示区 -->
					</div>
				</div>
			</div>

		</div>
	</div>
	<jsp:include page="common/footer.jsp" />

	<!-- Fix IE 6-9  JSON object-->
	<script src="${requestScope.basePath}js/lib/json2.js"></script>
	<script src="${requestScope.basePath}js/lib/bootbox.min.js"></script>
	<script src="${requestScope.basePath}js/lib/jquery.jqpagination.min.js"></script>
	<script src="${requestScope.basePath}bootstrap/js/highstock.js"></script>
	<script src="${requestScope.basePath}bootstrap/js/exporting.js"></script>
	<script src="${requestScope.basePath}js/statistic/statistic.util.js"></script>
	<script src="${requestScope.basePath}js/statistic/statistic.js"></script>
	<script src="${requestScope.basePath}js/statistic/statistic.service.js"></script>
	<script src="${requestScope.basePath}js/statistic/statistic.collector.js"></script>
	<script src="${requestScope.basePath}bootstrap/js/picker.js"></script>
	<script src="${requestScope.basePath}bootstrap/js/picker.date.js"></script>
	<script src="${requestScope.basePath}bootstrap/js/picker.time.js"></script>
</body>
</html>

