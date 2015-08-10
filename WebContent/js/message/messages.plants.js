;
"user strict";

var rainet = rainet || {};
rainet.message = rainet.message || {};
rainet.message.controller = rainet.message.controller || {}; 

// 植物信息
rainet.message.controller.plants = {
	
	// 添加校验信息 当保存或修改plants的时候
	setValidateForPlants : function($form, operate){
		$form.bootstrapValidator({
			feedbackIcons: {
	            valid: 'glyphicon glyphicon-ok',
	            invalid: 'glyphicon glyphicon-remove',
	            validating: 'glyphicon glyphicon-refresh'
	        },
			fields : {
				name : {
					validators : {
						notEmpty : {
							message: '项目名称不能为空'
						}
					}
				},
				department : {
					validators : {
						notEmpty : {
							message: '负责单位不能为空'
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
			}
		})
		
		
		// 修复-->当选择省份时，已经校验过的城市不会重新验证的问题
		.on('change', '.provinceItem', function(){
			$form.bootstrapValidator('revalidateField', 'city');
			
			
		}).on('blur.rainet', '.projectName', function(){
			// 验证项目名称是否存在
			var bv = $form.data('bootstrapValidator');
			$field = bv.getFieldElements('name');
			var value = $field.val();
			if ($.trim(value) === '') {
				bv.updateMessage($field, 'notEmpty');
				return ;
			}
			var data = {name : value};
			if (operate === 'update') {
				data.id = $form.attr('data-id');
			}
			rainet.message.service.plants.validName(data, function(data){
				if (data) {
					// 存在，更新错误信息的提示
					bv.updateMessage($field, 'notEmpty', '项目名称已存在');
					bv.updateStatus($field, 'INVALID');
				}
			});
		});
		
		$form.data('bootstrapValidator').disableSubmitButtons(true);
	},
	
	// 从后台获取数据之后，设置对应项目信息的值，以弹出框的形式展示
	setPlantsInfo : function(data, readonly, $dataTable){
		var $projectHtml = $(this.infoTempate).attr('id', 'projectInfo'+data.id).css('display','block');
		$('.projectName',$projectHtml).val(data.name);
		$('.department',$projectHtml).val(data.department);
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
				rainet.message.service.project.update(jsonData, function(data){
					if (data) {
						rainet.utils.notification.success('修改成功');
						$dataTable.api().ajax.reload();
					}
				});
			});
			// Add validation
			$form.attr('data-id', data.id);
			this.setValidateForPlants($form, 'update');
		}
		bootbox.dialog({
			message : $projectHtml,
			title : '项目信息',
			// 支持ESC
			onEscape : function(){
				
			}
		});
	},
	
  table : {
		// 项目管理的表头
		columns : [
		           	{ "sTitle": "序号", "targets": 0, "orderable": false, "render" : rainet.message.util.formateSeq},
		           	{ "sTitle": "项目名称",  "targets": 1, "render" : rainet.message.util.formateLink},
		           	{ "sTitle": "项目单位", "targets": 2 },
		           	{ "sTitle": "项目地址",  "targets": 3 },
		           	{ "sTitle": "创建时间",    "targets": 4, "render" :rainet.message.util.formateDate},
		           	{ "sTitle": "操作",    "targets": 5, "orderable": false, "data": null,
		    			"defaultContent": rainet.message.util.operaterHtml }
		],
		// 默认排序的字段
		order : [4, 'desc'],
		
		// 映射后台返回的数据
		dataRef : [
		             {'data':'id'},
				     {'data':'name'},
				     {'data':'department'},
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
			$(row).attr('data-name', data.name);
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
		var projectId = $(self).attr('data-id');
		var _this = this;
		rainet.message.service.project.get(projectId, function(data){
			_this.setPlantsInfo(data, true);
		});
	},
	// 更新单个项目信息
	edit : function(self, $datatable){
		var projectId = $(self).parent().parent().attr('id');
		var _this = this;
		rainet.message.service.project.get(projectId, function(data){
			_this.setPlantsInfo(data, false, $datatable);
		});
	},
	// 删除某个项目
	del : function(self, $datatable){
		var projectId = $(self).parent().parent().attr('id');
		var projectName = $(self).parent().parent().attr('data-name');
		bootbox.dialog({
			message : "确认删除此项目？",
			title : '删除项目"'+projectName+'"',
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
				    	  rainet.message.service.plants.del(projectId, function(data){
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
    			"<label class=\"col-sm-3 control-label\">植物名称：</label>\n"+
    			"<div class=\"col-sm-9\">\n"+
    				"<input type=\"text\" class=\"form-control projectName\" name=\"name\"/>\n"+
    			"</div>\n"+
  			"</div>\n"+
  			"<div class=\"form-group\">\n"+
    			"<label class=\"col-sm-3 control-label\">负责单位：</label>\n"+
    			"<div class=\"col-sm-9\">\n"+
    				"<input type=\"text\"  class=\"form-control department\" name=\"department\"/>\n"+
    			"</div>\n"+
  			"</div>\n"+
  		"<div class=\"form-group\">\n"+
  		"<label class=\"col-sm-3 control-label\">项目地址：</label>\n"+
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
  			 "<div class=\"modal-footer\">\n"+
					"<button data-bb-handler=\"cancel\" type=\"button\" class=\"btn btn-warning\">取消</button>\n"+
					"<button data-bb-handler=\"success\" type=\"submit\" class=\"btn btn-success\">修改</button>\n"+
			"</div>\n"+
		"</form>\n"+
	"</div>"
	
};

