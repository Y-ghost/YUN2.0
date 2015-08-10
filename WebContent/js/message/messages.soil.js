;
"user strict";

var rainet = rainet || {};
rainet.message = rainet.message || {};
rainet.message.controller = rainet.message.controller || {}; 

// 土壤信息
rainet.message.controller.soil = {
	
	// 添加校验信息 当保存或修改soil的时候
		setValidateForSoil : function($form, operate){
		$form.bootstrapValidator({
			feedbackIcons: {
	            valid: 'glyphicon glyphicon-ok',
	            invalid: 'glyphicon glyphicon-remove',
	            validating: 'glyphicon glyphicon-refresh'
	        },
			fields : {
				soiltype : {
					validators : {
						notEmpty : {
							message: '不能为空'
						}
					}
				},
				soilweight : {
					validators : {
						notEmpty : {
							message: '不能为空'
						}
					}
				},
				soilwater : {
					validators : {
						notEmpty : {
							message: '不能为空'
						}
					}
				},
				province : {
					validators : {
						regexp: {
	                        regexp: /^[^1]+$/i,
	                        message: '省份不能为空'
	                    }
					}
				},
				city : {
					validators : {
						regexp: {
	                        regexp: /^[^1]+$/i,
	                        message: '城市不能为空'
	                    }
					}
				},
				
				address : {
					validators : {
						notEmpty : {
							message: '详细地址不能为空'
						}
					}
				},
				waterVal1 : {
					validators : {
						notEmpty : {
							message: '实测湿度值1不能为空'
						},
						regexp: {
							regexp: /^[0-9]+(\.[0-9]{1,2})?$/,
							message: '只能为整数或者保留两位的小数'
						}
					}
				},
				originalVal1 : {
					validators : {
						notEmpty : {
							message: '传感器原始值1不能为空'
						},
						regexp: {
							regexp: /^[0-9]+(\.[0-9]{1,2})?$/,
							message: '只能为整数或者保留两位的小数'
						}
					}
				},
				waterVal2 : {
					validators : {
						notEmpty : {
							message: '实测湿度值2不能为空'
						},
						regexp: {
							regexp: /^[0-9]+(\.[0-9]{1,2})?$/,
							message: '只能为整数或者保留两位的小数'
						}
					}
				},
				originalVal2 : {
					validators : {
						notEmpty : {
							message: '传感器原始值2不能为空'
						},
						regexp: {
							regexp: /^[0-9]+(\.[0-9]{1,2})?$/,
							message: '只能为整数或者保留两位的小数'
						}
					}
				},
				waterVal3 : {
					validators : {
						notEmpty : {
							message: '实测湿度值3不能为空'
						},
						regexp: {
							regexp: /^[0-9]+(\.[0-9]{1,2})?$/,
							message: '只能为整数或者保留两位的小数'
						}
					}
				},
				originalVal3 : {
					validators : {
						notEmpty : {
							message: '传感器原始值3不能为空'
						},
						regexp: {
							regexp: /^[0-9]+(\.[0-9]{1,2})?$/,
							message: '只能为整数或者保留两位的小数'
						}
					}
				},
				waterVal4 : {
					validators : {
						notEmpty : {
							message: '实测湿度值4不能为空'
						},
						regexp: {
							regexp: /^[0-9]+(\.[0-9]{1,2})?$/,
							message: '只能为整数或者保留两位的小数'
						}
					}
				},
				originalVal4 : {
					validators : {
						notEmpty : {
							message: '传感器原始值4不能为空'
						},
						regexp: {
							regexp: /^[0-9]+(\.[0-9]{1,2})?$/,
							message: '只能为整数或者保留两位的小数'
						}
					}
				}
			}
		})
		
		
		// 修复-->当选择省份时，已经校验过的城市不会重新验证的问题
		.on('change', '.provinceItem', function(){
			$form.bootstrapValidator('revalidateField', 'city');
			
			
		}).on('blur.rainet', '.soiltype', function(){
			// 验证土壤名称是否存在
			var bv = $form.data('bootstrapValidator');
			$field = bv.getFieldElements('soiltype');
			var value = $field.val();
			if ($.trim(value) === '') {
				bv.updateMessage($field, 'notEmpty');
				return ;
			}
			var data = {name : value};
			if (operate === 'update') {
				data.id = $form.attr('data-id');
			}
			rainet.message.service.soil.validName(data, function(data){
				if (data) {
					// 存在，更新错误信息的提示
					bv.updateMessage($field, 'notEmpty', '土壤名称已存在');
					bv.updateStatus($field, 'INVALID');
				}
			});
		});
		
		$form.data('bootstrapValidator').disableSubmitButtons(true);
	},
	
	// 从后台获取数据之后，设置对应项目信息的值，以弹出框的形式展示
	setSoilInfo : function(data, readonly, $dataTable){
		var $projectHtml = $(this.infoTempate).attr('id',data.id).css('display','block');
		$('.soiltype',$projectHtml).val(data.soiltype);
		$('.soilweight',$projectHtml).val(data.soilweight);
		$('.soilwater',$projectHtml).val(data.soilwater);
		$('.address',$projectHtml).val(data.address);
		$.initProv($('.provinceItem',$projectHtml), $('.cityItem',$projectHtml), "-省份-", "-城市-");
		$(".provinceItem", $projectHtml).val(data.province);
		$.initCities($(".provinceItem", $projectHtml),$(".cityItem", $projectHtml));
		$(".cityItem", $projectHtml).val(data.city);
		$(".id", $projectHtml).val(data.id);
		
		if (readonly) {
			$("input", $projectHtml).attr('disabled', true);
			$("select", $projectHtml).attr('disabled', true);
			$('button[type=submit]', $projectHtml).css('display', 'none');
		}
		// 如果是更新操作，就添加修改按钮，并绑定修改函数，如果是查看详情，则没有修改按钮
		var $form = $("form", $projectHtml);
		if (!readonly) {
			$('.relValue', $projectHtml).css('display', 'block');
			$('button[type=submit]', $form).off('click').on('click', function(){
				// 检查验证是否通过
				$($form).bootstrapValidator('validate');
				var bv = $form.data('bootstrapValidator');
				if (bv.$invalidFields.length > 0) {
					return false;
				}
				var formData = $form.serializeArray();
				var jsonData = rainet.utils.serializeObject(formData);
				// 更新项目
				rainet.message.service.soil.update(jsonData, function(data){
					if (data) {
						rainet.utils.notification.success('修改成功');
						$dataTable.api().ajax.reload();
					}
				});
			});
			// Add validation
			$form.attr('data-id', data.id);
			this.setValidateForSoil($form, 'update');
		}
		bootbox.dialog({
			message : $projectHtml,
			title : '土壤信息',
			// 支持ESC
			onEscape : function(){
				
			}
		});
	},
	
  table : {
		// 项目管理的表头
		columns : [
		           	{"sTitle": "序号",  "targets": 0, "orderable": false, "render" : rainet.message.util.formateSeq},
		           	{ "sTitle": "土壤名称",  "targets": 1, "render" : rainet.message.util.formateLink},
		           	{ "sTitle": "干容重(g/cm<sup>3</sup>)", "targets": 2 },
		           	{ "sTitle": "饱和持水量(%)",  "targets": 3 },
		           	{ "sTitle": "项目地址",  "targets": 4 },
		           	{ "sTitle": "创建时间",    "targets": 5, "render" :rainet.message.util.formateDate},
		           	{ "sTitle": "操作",    "targets": 6, "orderable": false, "data": null,
		    			"defaultContent": rainet.message.util.operaterHtml }
		],
		// 默认排序的字段
		order : [5, 'desc'],
		
		// 映射后台返回的数据
		dataRef : [
		             {'data':'id'},
				     {'data':'soiltype'},
				     {'data':'soilweight'},
				     {'data':'soilwater'},
				     {'data':'address'},
				     {'data':'createtime'},
		],
		
		// 初始化项目模块的数据
		initEvent : function($datatable){
			// Init province and city selects
			$.initProv("#province","#city","-省份-","-城市-");
			
			// Bind search event
			$('#city').off('change.rainet').on('change.rainet', function(){
				if($datatable){
					var city = $('#city').val();
					var province = $('#province').val();
					if ($.trim(province) == -1 && $.trim(city) == -1) {
						return ;
					}
					$datatable.api().ajax.reload();
				}
			});
			
			
			$('#province').off('change.rainet').on('change.rainet', function(){
				if($datatable){
					var province = $('#province').val();
					if ($.trim(province) == -1) {
						// Reload all data, clear search criteria
						$datatable.api().ajax.reload();
					}
				}
			});
			rainet.message.util.setSearchForm('project');
		},
		row : function(row, data, index){
			$(row).attr('id', data.id);
			$(row).attr('data-name', data.soiltype);
		}
	},
	
	// 在共用table的js方法提交参数给后台之前，添加不同模块特有的参数信息
	updateParam : function(param){
		var city = $.trim($('#city').val());
		var province = $.trim($('#province').val());
		if (city != -1 && province != -1) {
			// 处理中文
			param.city = encodeURIComponent(city);
			param.province = encodeURIComponent(province);
		}
	},
	
	// 获取单个项目的详细信息
	detail : function(self){
		var id = $(self).attr('data-id');
		var _this = this;
		rainet.message.service.soil.get(id, function(data){
			_this.setSoilInfo(data, true);
		});
	},
	// 更新单个土壤信息
	edit : function(self, $datatable){
		var id = $(self).parent().parent().attr('id');
		var _this = this;
		rainet.message.service.soil.get(id, function(data){
			_this.setSoilInfo(data, false, $datatable);
		});
	},
	// 删除某个土壤
	del : function(self, $datatable){
		var id = $(self).parent().parent().attr('id');
		var soilType = $(self).parent().parent().attr('data-name');
		bootbox.dialog({
			message : "确认删除此项目？",
			title : '删除项目"'+soilType+'"',
			// 支持ESC
			onEscape : function(){
				
			},
			buttons :  {
				cancel: {
				      label: "取消",
				      className: "btn-warning",
				},
				success: {
				      label: "确定",
				      className: "btn-success",
				      callback : function(){
				    	  rainet.message.service.soil.del(id, function(data){
				    		rainet.utils.notification.success('删除成功');
				    		$datatable.api().ajax.reload();
				    	  });
				      }
				}
		}
		});
	},
	
	infoTempate : "<div>\n"+
		"<form class=\"form-horizontal\" role=\"form\">\n"+
			"<div class=\"form-group\">\n"+
    			"<label class=\"col-sm-3 control-label\">土壤名称：</label>\n"+
    			"<div class=\"col-sm-9\">\n"+
    				"<input type=\"text\" class=\"form-control soiltype\" name=\"soiltype\"/>\n"+
    			"</div>\n"+
  			"</div>\n"+
  			"<div class=\"form-group\">\n"+
    			"<label class=\"col-sm-3 control-label\">干容重(g/cm<sup>3</sup>)：</label>\n"+
    			"<div class=\"col-sm-9\">\n"+
    				"<input type=\"text\"  class=\"form-control soilweight\" name=\"soilweight\"/>\n"+
    			"</div>\n"+
  			"</div>\n"+
  			"<div class=\"form-group\">\n"+
	  			"<label class=\"col-sm-3 control-label\">饱和持水量(%)：</label>\n"+
	  			"<div class=\"col-sm-9\">\n"+
	  			"<input type=\"text\"  class=\"form-control soilwater\" name=\"soilwater\"/>\n"+
	  			"</div>\n"+
  			"</div>\n"+
  		"<div class=\"form-group\">\n"+
  		"<label class=\"col-sm-3 control-label\">土壤地址：</label>\n"+
  				"<div class=\"col-sm-9\">\n"+
    				"<div class=\"col-sm-5 selectItem\" style=\"padding-left:0;\">\n"+
    					"<select class=\"form-control provinceItem\" name=\"province\"></select>\n"+
    				"</div>\n"+
    				"<div class=\"col-sm-5 selectItem\">\n"+
    					"<select class=\"form-control cityItem\" name=\"city\"></select>\n"+
    				"</div>\n"+
    			"</div>\n"+
    	  	"</div>\n"+
  			"<div class=\"form-group\">\n"+
    			"<label class=\"col-sm-3 control-label\">详细地址：</label>\n"+
    			"<div class=\"col-sm-9\">\n"+
    				"<input type=\"text\" class=\"form-control address\" name=\"address\"/>\n"+
    			"</div>\n"+
    		"</div>\n"+
  			"<input type=\"hidden\" name=\"id\" class=\"id\"/>\n"+
  			"<div class=\"form-group relValue\" style='display:none;'>\n"+
			"<label class=\"col-sm-3 control-label\">实测湿度值1：</label>\n"+
			"<div class=\"col-sm-3\">\n"+
			"<input type=\"text\" style='width:100%;' class=\"form-control waterVal1\" name=\"waterVal1\"/>\n"+
			"</div>\n"+
			"<label class=\"col-sm-3 control-label\">传感器原始值1：</label>\n"+
			"<div class=\"col-sm-3\">\n"+
			"<input type=\"text\" style='width:100%;' class=\"form-control originalVal1\" name=\"originalVal1\"/>\n"+
			"</div>\n"+
			"</div>\n"+
			"<div class=\"form-group relValue\" style='display:none;'>\n"+
			"<label class=\"col-sm-3 control-label\">实测湿度值2：</label>\n"+
			"<div class=\"col-sm-3\">\n"+
			"<input type=\"text\" style='width:100%;' class=\"form-control waterVal2\" name=\"waterVal2\"/>\n"+
			"</div>\n"+
			"<label class=\"col-sm-3 control-label\">传感器原始值2：</label>\n"+
			"<div class=\"col-sm-3\">\n"+
			"<input type=\"text\" style='width:100%;' class=\"form-control originalVal2\" name=\"originalVal2\"/>\n"+
			"</div>\n"+
			"</div>\n"+
			"<div class=\"form-group relValue\" style='display:none;'>\n"+
			"<label class=\"col-sm-3 control-label\">实测湿度值3：</label>\n"+
			"<div class=\"col-sm-3\">\n"+
			"<input type=\"text\" style='width:100%;' class=\"form-control waterVal3\" name=\"waterVal3\"/>\n"+
			"</div>\n"+
			"<label class=\"col-sm-3 control-label\">传感器原始值3：</label>\n"+
			"<div class=\"col-sm-3\">\n"+
			"<input type=\"text\" style='width:100%;' class=\"form-control originalVal3\" name=\"originalVal3\"/>\n"+
			"</div>\n"+
			"</div>\n"+
			"<div class=\"form-group relValue\" style='display:none;'>\n"+
			"<label class=\"col-sm-3 control-label\">实测湿度值4：</label>\n"+
			"<div class=\"col-sm-3\">\n"+
			"<input type=\"text\" style='width:100%;' class=\"form-control waterVal4\" name=\"waterVal4\"/>\n"+
			"</div>\n"+
			"<label class=\"col-sm-3 control-label\">传感器原始值4：</label>\n"+
			"<div class=\"col-sm-3\">\n"+
			"<input type=\"text\" style='width:100%;' class=\"form-control originalVal4\" name=\"originalVal4\"/>\n"+
			"</div>\n"+
			"</div>\n"+
  			 "<div class=\"modal-footer\">\n"+
					"<button data-bb-handler=\"cancel\" type=\"button\" class=\"btn btn-warning\">取消</button>\n"+
					"<button data-bb-handler=\"success\" type=\"submit\" class=\"btn btn-success\">修改</button>\n"+
			"</div>\n"+
		"</form>\n"+
	"</div>"
	
};

