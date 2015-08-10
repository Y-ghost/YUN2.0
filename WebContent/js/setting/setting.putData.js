;
"user strict";

var rainet = rainet || {};
rainet.setting = rainet.setting || {};
rainet.setting.controller = rainet.setting.controller || {}; 
var flag = true;

// 主机信息
rainet.setting.controller.putData = {
		// 全选
		checked : function(){
			$(".checkAll").change(function() {
				if($(this).is(':checked')){
					$("[id='equipmentCheckbox']").each(function(){
						$(this)[0].checked = true;
					});
	            }else{
	            	$("[id='equipmentCheckbox']").each(function(){
	            		$(this)[0].checked = false;
					});
	            }
			}); 
		},
		
		// 查询事件
		findEquipments : function($projectList){
			//查询项目下的节点信息
			$(".findBtn").off('click').on('click', function(e){
				if(flag){
					flag = false;
					var projectId = $projectList.val();
					if(projectId!="-1"){
						var param = {pId:projectId};
						param.handleError = function(result){
							flag = true;
							return true;
						};
						rainet.setting.service.equipment.getRelData(param, function(data){
							var $EquipmentList = $(".EquipmentList");
							var str = "";
							$.each(data,function(index,item){
								var waterStr = ""
									if(item.result[0].watervalue < item.equipmentStatus.watervalue ){
										waterStr = "<label class=\"col-sm-2 control-label\" style='color:#a94442;'>现场实时数据：</label>" +
										"<div class=\"col-sm-2 has-error\"><span class=\"form-control\">"+item.result[0].watervalue+"</span></div>" +
										"<label class=\"col-sm-2 control-label\">数据库最新数据：</label>" +
										"<div class=\"col-sm-2\"><span type=\"text\" class=\"form-control watervalue\" name=\"watervalue\" id=\"watervalue\" data-bv-field=\"watervalue\">"+item.equipmentStatus.watervalue+"</span></div></div>";
									}else{
										waterStr = "<label class=\"col-sm-2 control-label\">现场实时数据：</label>" +
										"<div class=\"col-sm-2\"><span class=\"form-control\">"+item.result[0].watervalue+"</span></div>" +
										"<label class=\"col-sm-2 control-label\">数据库最新数据：</label>" +
										"<div class=\"col-sm-2\"><span type=\"text\" class=\"form-control watervalue\" name=\"watervalue\" id=\"watervalue\" data-bv-field=\"watervalue\">"+item.equipmentStatus.watervalue+"</span></div></div>";
									}
								
								str =str + "<div class=\"col-xs-12 col-md-12\">" +
								"<div class=\"panel panel-default \">" +
								"<div class=\"panel-heading\">" +
								"<span class=\"float-left\"> <input type=\"checkbox\" class=\"cursor\" name=\"id\" value=\""+item.equipment.id+"\" id=\"equipmentCheckbox\"></span><label style='margin-left:5px;' >"+item.equipment.name+"</label> </div>" +
								"<div class=\"panel-body\">" +
								"<form class=\"form-horizontal\" role=\"form\">" +
									"<div class=\"form-group has-feedback\" id=\"form-group\">" +
									"<input type=\"hidden\" name=\"id\" value=\""+item.equipment.id+"\"/>" +
									"<input type=\"hidden\" name=\"name\" value=\""+item.equipment.name+"\"/>" +
									"<input type=\"hidden\" name=\"code\" value=\""+item.equipment.code+"\"/>" +
									"<input type=\"hidden\" name=\"controlhostid\" value=\""+item.equipment.controlhostid+"\"/>"+	
									"<input type=\"hidden\" name=\"watervalue\" value=\""+item.equipmentStatus.watervalue+"\"/>"+	
								"<div class=\"form-group has-feedback\" id=\"form-group\">" +
									"<label class=\"col-sm-2 control-label\">节点地址：</label>" +
									"<div class=\"col-sm-2\"><span class=\"form-control code\" name=\"code\" id=\"code\" data-bv-field=\"code\">"+item.equipment.code+"</span></div>" +
									waterStr +
								"</form></div></div></div></div>";
							});
							$EquipmentList.empty().append(str);
							flag = true;
						});
					}else{
						rainet.utils.notification.warning("请先选择项目!");
						flag = true;
					}
				}
			});
			
		},
		//赋值事件
		putData : function(){
			$(".modifyBtn").off('click').on('click', function(e){
				if(flag){
					flag = false;
					var mark=false;
					$("[id='equipmentCheckbox']").each(function(){
						if($(this).is(":checked")){
							mark = true;
							return false;//跳出循环
						}
					});
					if(mark){
						var list = [];
						$("[id='equipmentCheckbox']").each(function(){
							if($(this).is(":checked")){
								var $form = $(this).parent().parent().parent().find("form");
								var bv = $form.data('bootstrapValidator');
								var formData = $form.serializeArray();
								var jsonData = rainet.utils.serializeObject(formData);
								list.push(jsonData);
							}
						});
						console.log(JSON.stringify(list));
						list.handleError = function(result){
							flag = true;
							return true;
						};
						bootbox.dialog({
							message : "该操作最多需要1分钟才能完成，确认赋值？",
							title : '节点赋值',
							// 支持ESC
							onEscape : function(){
								flag=true;
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
								      label: "提交",
								      className: "btn-success",
								      callback : function(){
								    	  	rainet.setting.service.equipment.putData(list, function(data){
												if(data==""){
													rainet.utils.notification.success("赋值成功!");
												}else{
													bootbox.dialog({
														message : data,
														title : '赋值失败',
														// 支持ESC
														onEscape : function(){
															flag=true;
														},
														buttons :  {
															cancel: {
															      label: "关闭",
															      className: "btn-warning",
															      callback : function(){
															    	  flag = true;
															      }
															}
													}
													});
												}
												flag = true;
											});
								      }
								}
						}
						});
					}else{
						rainet.utils.notification.warning("请先选择节点!");
						flag = true;
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
			// 绑定搜索事件
			this.findEquipments($projectList);
			this.putData();
			this.checked();
		},
		
		// 搜索节点信息
		add : function(){
			var $projectHtml = $(this.infoTempate);
			$(".equipment-container").empty().append($projectHtml);
			this.setEquipmentInfo($projectHtml);
			
		},
		
		infoTempate : "<div class=\"col-xs-9 col-md-9\">" +
						"<div class=\"node-container\" style='height:50px;'>" +
						"<div class=\"node-tools\" style=\"font-size:14px;\">" +
						"<label class=\"col-xs-3 col-md-3 text-center\" style=\"line-height: 34px;\">" +
						"<input type=\"checkbox\" class=\"cursor checkAll\"/> 全选&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
						"所属项目:" +
						"</label>" +
						"<select class=\"col-xs-3 col-md-3 input-sm projectName\" id=\"projectName\" name=\"projectid\"></select>" +
						"<div class=\"col-xs-3 col-md-3 text-center\">" +
						"<button type=\"button\" class=\"btn btn-success findBtn \">查询</button>" +
						"</div>" +
						"<div class=\"col-xs-3 col-md-3\">" +
						"<button type=\"button\" class=\"btn btn-warning modifyBtn\">赋值</button>" +
						"</div>" +
						"</div>" +
						"</div>" +
						"<div class=\"EquipmentList\">" +
						
						"</div>" +
						"</div>" +
						"</div>"
};
