;
"user strict";

var rainet = rainet || {};
rainet.setting = rainet.setting || {};
rainet.setting.controller = rainet.setting.controller || {}; 

// 项目信息
rainet.setting.controller.project = {
	
	// 添加校验信息 当保存或修改project的时候
	setValidateForPrjoect : function($form){
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
				projecttype : {
					validators : {
						notEmpty : {
							message: '项目类型不能为空'
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
				}
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
			var data = {projectName : value,projectId:0};
			rainet.setting.service.project.validName(data, function(data){
				if (data) {
					// 存在，更新错误信息的提示
					bv.updateMessage($field, 'notEmpty', '项目名称已存在');
					bv.updateStatus($field, 'INVALID');
				}
			});
		});
		
		$form.data('bootstrapValidator').disableSubmitButtons(true);
	},
	
	// 初始化省份和城市
	setProvinceCity : function($projectHtml){
		$.initProv($('.provinceItem',$projectHtml), $('.cityItem',$projectHtml), "-省份-", "-城市-");
		$.initCities($(".provinceItem", $projectHtml),$(".cityItem", $projectHtml));
		
		// 如果是添加操作，绑定添加函数
		var $form = $("form", $projectHtml);
		$('button[type=submit]', $form).off('click').on('click', function(){
			// 检查验证是否通过
			var bv = $form.data('bootstrapValidator');
			console.log(bv.$invalidFields.length);
			if (bv.$invalidFields.length > 0) {
				return false;
			}
			var formData = $form.serializeArray();
			var jsonData = rainet.utils.serializeObject(formData);
			// 更新项目
			rainet.setting.service.project.add(jsonData, function(data){
				if (data) {
					rainet.utils.notification.success('添加成功');
				}
			});
		});
		// Add validation
		this.setValidateForPrjoect($form);
	},
	// 更新单个项目信息
	add : function(){
		var $projectHtml = $(this.infoTempate);
		$(".equipment-container").empty().append($projectHtml);
		this.setProvinceCity($projectHtml);
	},
	
	infoTempate : "<div class=\"col-xs-7 col-md-7\">"+
		"<div class=\"node-container\">"+
		"<div class=\"node-tools\">"+
		"<div class=\"modal-header\"><h4 class=\"modal-title\">项目信息</h4></div>\n"+
		"<div class=\"modal-body\">\n"+
		"<form class=\"form-horizontal\" role=\"form\" onsubmit=\"return false;\">\n"+
			"<div class=\"form-group\">\n"+
    			"<label class=\"col-sm-3 control-label\">项目名称：</label>\n"+
    			"<div class=\"col-sm-9\">\n"+
    				"<input type=\"text\" style='width:100%;' class=\"form-control projectName\" name=\"name\"/>\n"+
    			"</div>\n"+
  			"</div>\n"+
  			"<div class=\"form-group\">\n"+
    			"<label class=\"col-sm-3 control-label\">项目类型：</label>\n"+
    			"<div class=\"col-sm-9 \" style=\"text-align:left;\">\n"+
	    			"<div class=\"radio-inline\">"+
						"<label>"+
		    				"<input type=\"radio\" class=\"projectType\" name=\"projecttype\" value=\"0\" checked/>绿化"+
		    			"</label>"+
	        		"</div>"+	
	        		"<div class=\"radio-inline\">"+
		        		"<label>"+
		        			"<input type=\"radio\" class=\"projectType\" name=\"projecttype\" value=\"1\" />农业"+
		        		"</label>"+
	        		"</div>"+		
        		"</div>"+		
  			"</div>\n"+
  			"<div class=\"form-group\">\n"+
	  			"<label class=\"col-sm-3 control-label\">负责单位：</label>\n"+
	  			"<div class=\"col-sm-9\">\n"+
	  				"<input type=\"text\" style='width:100%;'  class=\"form-control department\" name=\"department\"/>\n"+
	  			"</div>\n"+
  			"</div>\n"+
  		"<div class=\"form-group\">\n"+
  		"<label class=\"col-sm-3 control-label\">项目地址：</label>\n"+
  				"<div class=\"col-sm-9\">\n"+
    				"<div class=\"col-sm-5 selectItem\" style=\"padding-left:0;\">\n"+
    					"<select class=\"form-control provinceItem\" style='width:100%;' name=\"province\"></select>\n"+
    				"</div>\n"+
    				"<div class=\"col-sm-5 selectItem\">\n"+
    					"<select class=\"form-control cityItem\" style='width:100%;' name=\"city\"></select>\n"+
    				"</div>\n"+
    			"</div>\n"+
    	  	"</div>\n"+
  			"<div class=\"form-group\">\n"+
    			"<label class=\"col-sm-3 control-label\">详细地址：</label>\n"+
    			"<div class=\"col-sm-9\">\n"+
    				"<input type=\"text\" class=\"form-control address\" style='width:100%;' name=\"address\"/>\n"+
    			"</div>\n"+
    		"</div>\n"+
  			 "<div class=\"modal-footer\">\n"+
					"<button data-bb-handler=\"success\" type=\"submit\" class=\"btn btn-success\">添加</button>\n"+
			"</div>\n"+
		"</form>\n"+
		"</div>\n"+
		"</div>\n"+
		"</div>\n"+
	"</div>"
	
};

