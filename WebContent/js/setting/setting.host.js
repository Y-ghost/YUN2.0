;
"user strict";

var rainet = rainet || {};
rainet.setting = rainet.setting || {};
rainet.setting.controller = rainet.setting.controller || {}; 

// 主机信息
rainet.setting.controller.host = {
	
	// 添加校验信息 当保存或修改project的时候
	setValidHost : function($form){
		$form.bootstrapValidator({
			feedbackIcons: {
	            valid: 'glyphicon glyphicon-ok',
	            invalid: 'glyphicon glyphicon-remove',
	            validating: 'glyphicon glyphicon-refresh'
	        },
	        fields : {
				projectid : {
					validators : {
						notEmpty : {
							message: '请选择项目'
						}
					}
				},
				code : {
					validators : {
						notEmpty : {
							message: '主机编号不能为空'
						},
						regexp: {
	                        regexp: /^[0-9]{8}$/i,
	                        message : ' '
	                    }
					}
				}
			}
		});
		
		$form.data('bootstrapValidator').disableSubmitButtons(true);
	},
	
	// 从后台获取数据之后，设置对应主机信息的值，以弹出框的形式展示
	setHostInfo : function($hostHtml){
		// 如果是更新操作，就添加修改按钮，并绑定修改函数，如果是查看详情，则没有修改按钮
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
			
		// 绑定提交事件
		$('button[type=submit]', $form).off('click').on('click', function(e){
			// 检查验证是否通过
			var bv = $form.data('bootstrapValidator');
			$projectId = bv.getFieldElements('projectid');
			var projectId = $projectId.val();
			if ($.trim(projectId) === '-1') {
				bv.updateMessage($projectId, 'notEmpty',"请先选择项目!");
				bv.updateStatus($projectId, 'INVALID');
			}
			if (bv.$invalidFields.length > 0) {
				return false;
			}
			var formData = $form.serializeArray();
			var jsonData = rainet.utils.serializeObject(formData);
			var config = {jsonData : jsonData};
			config.handleError = function(result){
				// code已存在或主机已有主机，更新错误信息的提示
				$form.data('bootstrapValidator').disableSubmitButtons(true);
				var filed = 'code';
				$('.help-block', $form).hide();
				if (result.message.indexOf('主机') > -1) {
					filed = 'projectid'
					$('.js-placehoder', $form).show();
				}
				$field = bv.getFieldElements(filed);
				bv.updateMessage($field, 'notEmpty', result.message);
				bv.updateStatus($field, 'INVALID');
				return false;
			}
			// 更新host
			rainet.setting.service.host.add(jsonData, function(data){
				if (data) {
					rainet.utils.notification.success('添加成功');
				}
			});
		});
		
		this.setValidHost($form);
	},
	
	// 更新单个主机信息
	add : function(){
		var $projectHtml = $(this.infoTempate);
		$(".equipment-container").empty().append($projectHtml);
		this.setHostInfo($projectHtml);
	},
	
	infoTempate : "<div class=\"col-xs-7 col-md-7\">"+
		"<div class=\"node-container\">"+
		"<div class=\"node-tools\">"+
		"<div class=\"modal-header\"><h4 class=\"modal-title\">主机信息</h4></div>\n"+
		"<div class=\"modal-body\">\n"+
		"<form class=\"form-horizontal\" role=\"form\" style=\"padding-right:15px;\" onsubmit=\"return false;\">\n"+
			"<div class=\"form-group\">\n"+
				"<label class=\"col-sm-3 control-label\">所属项目：</label>\n"+
				"<div class=\"col-sm-9 selectItem\">\n"+
    				"<select class=\"form-control projectName\" style='width:100%;' name=\"projectid\"></select>\n"+
    			"</div>\n"+
    		"</div>\n"+
    		"<div class=\"form-group\">\n"+
    			"<label class=\"col-sm-3 control-label\">主机编号：</label>\n"+
    			"<div class=\"col-sm-9 selectItem\">\n"+
    				"<input type=\"text\" class=\"form-control code\" style='width:100%;' name=\"code\"/>\n"+
    				"<p class=\"help-block js-placehoder\">*&nbsp;只允许8位数字</p>\n"+
    			"</div>\n"+
    		"</div>\n"+
  			"<input type=\"hidden\" name=\"id\" class=\"id\"/>\n"+
  			 "<div class=\"dialog-footer\">\n"+
				"<button type=\"submit\" class=\"btn btn-success\">添加</button>\n"+
		     "</div>\n"+
		"</form>\n"+
	"</div>\n"+
	"</div>\n"+
	"</div>\n"+
	"</div>\n",
	
};

