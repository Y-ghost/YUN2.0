;
"user strict";

var rainet = rainet || {};
rainet.setting = rainet.setting || {};
rainet.setting.controller = rainet.setting.controller || {}; 
var flag = true;

// 主机信息
rainet.setting.controller.equipment = {
		// 搜索事件
		searchEquipments : function($projectList){
			//搜索节点
			$(".searchBtn").off('click').on('click', function(e){
				if(flag){
					flag = false;
					var projectId = $projectList.val();
					$projectList.off('change.rainet').on('change.rainet', function(){
						$(".EquipmentList").css("color","#333");
					});
					
					if(projectId!="-1"){
						bootbox.dialog({
							message : "该操作最多需要30秒钟的时间，确定搜索?",
							title : '搜索现场节点信息',
							// 支持ESC
							onEscape : function(){
								
							},
							buttons :  {
								cancel: {
								      label: "取消",
								      className: "btn-warning",
								      callback : function(){
								    	  flag = true;
								      }
								},
								success: {
								      label: "确定",
								      className: "btn-success",
								      callback : function(){
											var param = {pId:projectId};
											param.handleError = function(result){
												flag = true;
												return true;
											};
											rainet.setting.service.equipment.searchEquipment(param, function(data){
												var $EquipmentList = $(".EquipmentList");
												var str = "";
												$.each(data,function(index,item){
													var sensorStr = "";
													var iTmp = 10;
													$.each(item.result,function(i,data){
														iTmp = i;
														var headTmp = ""; 
														var endTmp = ""; 
														if(i%2==0){
															headTmp = "<div class=\"form-group has-feedback\" id=\"form-group\">";
														}else{
															headTmp = "";
														}
														
														if(i%2>0){
															endTmp = "</div>";
														}else{
															endTmp = "";
														}
														var Num = "";
														switch(i+1){
														case 1:
															Num = "一";
															break;
														case 2:
															Num = "二";
															break;
														case 3:
															Num = "三";
															break;
														case 4:
															Num = "四";
															break;
														case 5:
															Num = "五";
															break;
														}
														var strContent = "<label class=\"col-sm-3 control-label\">传感器"+Num+"：</label>" +
																		"<div class=\"col-sm-3\">" +
																		"<input type=\"text\" class=\"form-control address\"" +
																		"id=\"inputLab\" name=\"address\" disabled value=\""+data.number+"\"/>" +
																		"</div>"
														sensorStr = sensorStr + headTmp + strContent + endTmp;
													});
													if(iTmp!=10 && iTmp%2 == 0){
														sensorStr = sensorStr + "</div>";
													}
													
													str = str + "<div class=\"col-xs-12 col-md-6\">" +
															"<div class=\"panel panel-default \">" +
															"<div class=\"panel-heading\">" +
															"<label>节点信息</label>" +
															"</div>" +
															"<div class=\"panel-body\">" +
															"<form class=\"form-horizontal\" role=\"form\">" +
															"<div class=\"form-group has-feedback\" id=\"form-group\">" +
															"<label class=\"col-sm-3 control-label\">节点名称：</label>" +
															"<div class=\"col-sm-3\">" +
															"<input type=\"text\" class=\"form-control\"" +
															"id=\"inputLab\" name=\"name\" value=\""+(index+1)+" 号节点\"/>" +
															"</div>" +
															"<label class=\"col-sm-3 control-label\">节点地址：</label>" +
															"<div class=\"col-sm-3\">" +
															"<input type=\"text\" class=\"form-control\"" +
															"id=\"inputLab\" name=\"code\" disabled value=\""+item.code+"\"/>" +
															"</div>" +
															"</div>" +
															"<div class=\"form-group has-feedback\" id=\"form-group\">" +
															"<label class=\"col-sm-3 control-label\">灌溉面积：</label>" +
															"<div class=\"col-sm-3\">" +
															"<input type=\"text\" style='width:80%;float:left;' class=\"form-control\"" +
															"id=\"inputLab\" name=\"area\" value=\"\"/>" +
															"<span class=\"fa fa-exclamation-circle text-primary navbar-right dropdown cursor\" id=\"请输入正整数或保留2位的小数，如：123，123.12 ！\"></span>" +
															"</div>" +
															"<label class=\"col-sm-3 control-label\">流量参数：</label>" +
															"<div class=\"col-sm-3\">" +
															"<input type=\"text\" style='width:80%;float:left;' class=\"form-control\"" +
															"id=\"inputLab\" name=\"fowParameter\" value=\"\"/>" +
															"<span class=\"fa fa-exclamation-circle text-primary navbar-right dropdown cursor\" id=\"请输入正整数，如：123 ！\"></span>" +
															"<input type=\"hidden\" class=\"form-control\" name=\"controlHostId\" value=\""+item.controlHostId+"\"/>" +
															"</div>" +
															"</div>" +
															sensorStr +
															"</form>" +
															"</div>" +
															"</div>" +
															"</div>";
												});
												$EquipmentList.empty().append(str);
												tipShow();
												flag = true;
											});
								      }
								}
							}
						});
					}else{
						rainet.utils.notification.warning("请先选择项目!");
						$(".EquipmentList").css("color","#CC0033");
						flag = true;
					}
				}
			});
			
		},
		//注册事件
		registerEquipments : function($projectList){
			//注册节点
			$(".addBtn").off('click').on('click', function(e){
				if(flag){
					flag = false;
					var $forms = $(".form-horizontal");
					var paramArr=[];
					var mark = true;
					var RegEx = /^[0-9]+(\.[0-9]{1,2})?$/;
					var RegEx2 = /^[1-9]\d*$/;
					$($forms).each(function(){
						var $form = $(this)[0];
						var $name = $form.name;
						var $code = $form.code;
						var $area = $form.area;
						var $fowParameter = $form.fowParameter;
						var $controlHostId = $form.controlHostId;
						var $address = $form.address;
						var str = [];
						if($address != undefined){
							$($address).each(function(){
								str.push({number:$(this).val()});
							});
						}
						if($($name).val()==""){
							$($name).parent().addClass("has-error");
							$($name).parent().parent().find("label").eq(0).attr("id","has-error");
							mark = false;
						}else{
							$($name).parent().removeClass("has-error");
							$($name).parent().parent().find("label").eq(0).removeAttr("id");
						}
						if($($area).val()=="" || !RegEx.test($($area).val())){
							$($area).parent().addClass("has-error");
							$($area).parent().parent().find("label").eq(0).attr("id","has-error");
							mark = false;
						}else{
							$($area).parent().removeClass("has-error");
							$($area).parent().parent().find("label").eq(0).removeAttr("id");
						}
						if($($fowParameter).val()=="" || !RegEx.test($($fowParameter).val())){
							$($fowParameter).parent().addClass("has-error");
							$($fowParameter).parent().parent().find("label").eq(1).attr("id","has-error");
							mark = false;
						}else{
							$($fowParameter).parent().removeClass("has-error");
							$($fowParameter).parent().parent().find("label").eq(1).removeAttr("id");
						}
						
						var param =  {name:$($name).val(),code:$($code).val(),area : $($area).val(),fowParameter : $($fowParameter).val(),controlHostId : $($controlHostId).val()
								,result:str};
						paramArr.push(param);
					});
					if(!mark){
						rainet.utils.notification.warning("请填写正确的节点信息!");
						flag = true;
					}else{
						bootbox.dialog({
							message : "<label style=\"color:red;\">特别注意：</label><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;该操作将删除该主机下所有的数据，包括节点传感器信息、采集的历史数据等，确定注册?",
							title : '注册节点到现场',
							// 支持ESC
							onEscape : function(){
								
							},
							buttons :  {
								cancel: {
									label: "取消",
									className: "btn-warning",
									callback : function(){
										flag = true;
									}
								},
								success: {
									label: "确定",
									className: "btn-success",
									callback : function(){
										bootbox.dialog({
											message : "<label style=\"color:red;\">注意：</label><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;该操作特别，将会删除该主机下所有的数据，请再次确认?",
											title : '再次确认',
											// 支持ESC
											onEscape : function(){
												
											},
											buttons :  {
												cancel: {
													label: "取消",
													className: "btn-warning",
													callback : function(){
														flag = true;
													}
												},
												success: {
													label: "确定",
													className: "btn-success",
													callback : function(){
														paramArr.handleError = function(result){
															flag = true;
															return true;
														};
														rainet.setting.service.equipment.add(paramArr, function(data){
															if(data){
																rainet.utils.notification.success("添加节点成功!");
															}
															flag = true;
														});
													}
												}
											}
										});
									}
								}
							}
						});
					}
				}
			});
		},
		
		setEquipmentInfo : function($hostHtml){
			var $form = $("form", $hostHtml);
			
			// 初始化项目列表
			var $projectList = $('.projectName',$hostHtml);
			rainet.setting.service.project.getProjectNames(function(data){
				var length = data.length;
				$projectList.empty();
				$projectList.append('<option value=\"-1\">-请选择项目-</option>');
				for (var i = 0; i < length; i++) {
					$projectList.append('<option value='+data[i].id+'>'+data[i].name+'</option>');
				}
			});
			// 绑定事件
			this.searchEquipments($projectList);
			this.registerEquipments($projectList);
		},
		
		// 搜索节点信息
		add : function(){
			var $projectHtml = $(this.infoTempate);
			$(".equipment-container").empty().append($projectHtml);
			this.setEquipmentInfo($projectHtml);
		},
		
		infoTempate : "<div class=\"col-xs-9 col-md-9\">" +
						"<div class=\"node-container\">" +
						"<div class=\"node-tools\" style=\"font-size:14px;height:55px;\">" +
						"<label class=\"col-xs-2 col-md-2 text-center\" style=\"line-height: 34px;\">" +
						"所属项目:" +
						"</label>" +
						"<select class=\"col-xs-4 col-md-4 input-sm projectName\" id=\"projectName\" name=\"projectid\"></select>" +
						"<div class=\"col-xs-3 col-md-3 text-center\">" +
						"<button type=\"button\" class=\"btn btn-success searchBtn \">搜索</button>" +
						"</div>" +
						"<div class=\"col-xs-3 col-md-3\">" +
						"<button type=\"button\" class=\"btn btn-warning addBtn\">添加</button>" +
						"</div>" +
						"</div>" +
						"<div class=\"EquipmentList\">" +
						"<label class=\"fa fa-hand-o-up fa-5 col-xs-3 col-md-3 text-right\"></label>"+
						"<label class=\"fa-1 col-xs-7 col-md-7\"> 请先选择项目，搜索正确连接的节点信息!</label>"+
						"</div>" +
						"</div>" +
						"</div>",
	
};

//通讯状态弹框提示
var tipShow = function(){
	var content = "";
	$(".dropdown").mouseenter(function() {
		var tmp = $(this).attr("id");
		content = '<div style="width:180px;text-align:center;font-weight:700;"><font color="#000">'+tmp+'</font></div>';
		$(this).popover({
			title:'',
			trigger:'hover',
			placement:'top',
			html: 'true',
			content : content ,
			animation: false
		});
		var _this = this;
		$(this).popover("show");
		$(this).siblings(".popover").on("mouseleave", function () {
			$(_this).popover('hide');
		});
	});
}