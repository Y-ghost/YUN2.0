;
"user strict";

var rainet = rainet || {};
rainet.message = rainet.message || {};
rainet.message.controller = rainet.message.controller || {}; 

// 项目信息
rainet.message.controller.host = {
	
	// 添加校验信息 当保存或修改host的时候
	setValidate : function($form, operate){
		var _this = this;
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
							message: '项目名称不能为空'
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
				},
			}
		})
		
		$form.data('bootstrapValidator').disableSubmitButtons(true);
	},
	
	// 从后台获取数据之后，设置对应项目信息的值，以弹出框的形式展示
	setHostInfo : function(data, readonly, $dataTable){
		var $hostHtml = $(this.infoTemplate).attr('id', 'hostInfo'+data.id).css('display','block');
		$('.code',$hostHtml).val(data.code);
		$(".id", $hostHtml).val(data.id);
		
		if (readonly) {
			$("input", $hostHtml).attr('disabled', true);
			$("select", $hostHtml).attr('disabled', true);
			$('.projectName',$hostHtml).append('<option>'+data.projectName+'</option>');
			$('button[type=submit]', $hostHtml).hide();
		}
		
		// 如果是更新操作，就添加修改按钮，并绑定修改函数，如果是查看详情，则没有修改按钮
		var $form = $("form", $hostHtml);
		$('button[data-bb-handler=cancel]', $form).off('click').on('click', function(){
			bootbox.hideAll();
		});
		
		if (!readonly) {
			// 初始化项目列表
			var $projectList = $('.projectName',$hostHtml);
			var projectId = data.projectId;
			rainet.message.service.project.getProjectNames(function(data){
				var length = data.length;
				$projectList.empty();
				for (var i = 0; i < length; i++) {
					$projectList.append('<option value='+data[i].id+'>'+data[i].name+'</option>');
				}
				$projectList.val(projectId);
			});
			
			
			// 绑定提交事件
			$('button[type=submit]', $form).off('click').on('click', function(e){
				// 检查验证是否通过
				var bv = $form.data('bootstrapValidator');
				if (bv.$invalidFields.length > 0) {
					return false;
				}
				var formData = $form.serializeArray();
				var jsonData = rainet.utils.serializeObject(formData);
				var config = {jsonData : jsonData};
				config.handleError = function(result){
					// code已存在或项目已有主机，更新错误信息的提示
					$form.data('bootstrapValidator').disableSubmitButtons(true);
					var filed = 'code';
					$('.help-block', $form).hide();
					if (result.message.indexOf('项目') > -1) {
						filed = 'projectid'
						$('.js-placehoder', $form).show();
					}
					$field = bv.getFieldElements(filed);
					bv.updateMessage($field, 'notEmpty', result.message);
					bv.updateStatus($field, 'INVALID');
					return false;
				}
				// 更新host
				rainet.message.service.host.update(config, function(data){
					if (data) {
						bootbox.hideAll();
						rainet.utils.notification.success('修改成功');
						$dataTable.api().ajax.reload();
					}
				});
			});
			
			$form.attr('data-id', data.id);
			this.setValidate($form, 'update');
		}
		bootbox.dialog({
			message : $hostHtml,
			title : '主机信息',
			// 支持ESC
			onEscape : function(){
				
			}
		});
	},
	
	initProjectList : function(data){
		var length = data.length;
		$('#projectNameList').empty();
		$('#projectNameList').append('<option value=-1>-请选择项目-</option>');
		for (var i = 0; i < length; i++) {
			$('#projectNameList').append('<option value='+data[i].id+'>'+data[i].name+'</option>');
		}
		$('#projectNameList').off('change.rainet').on('change.rainet', function(){
			if($datatable){
				var projectId = $('#projectNameList').val();
				$datatable.api().ajax.reload();
			}
		});
	},
	
	table : {
	  parent : this,
	  columns : [
		           	{ "sTitle": "序号", "targets": 0, "orderable": false, "render" : rainet.message.util.formateSeq },
		           	{ "sTitle": "主机编号",  "targets": 1, "render" : rainet.message.util.formateLink },
		           	{ "sTitle": "所属项目名称", "targets": 2 },
		           	{ "sTitle": "创建时间",  "targets": 3, "render" : rainet.message.util.formateDate },
		           	{ "sTitle": "修改时间",    "targets": 4, "render" : rainet.message.util.formateDate },
		           	{ "sTitle": "操作",    "targets": 5, "orderable": false, "data": null,
		    			"defaultContent": rainet.message.util.operaterHtml }
		],
		order : [3, 'desc'],
		
		dataRef : [
		           {'data':'id'},
		           {'data':'code'},
		           {'data':'projectName'},
		           {'data':'createtime'},
		           {'data':'modifytime'},
		],
		
		initEvent : function($datatable){
			rainet.message.service.project.getProjectNames(function(data){
				var length = data.length;
				$('#projectNameList').empty();
				$('#projectNameList').append('<option value=-1>-请选择项目-</option>');
				for (var i = 0; i < length; i++) {
					$('#projectNameList').append('<option value='+data[i].id+'>'+data[i].name+'</option>');
				}
				$('#projectNameList').off('change.rainet').on('change.rainet', function(){
					if($datatable){
						var projectId = $('#projectNameList').val();
						$datatable.api().ajax.reload();
					}
				});
				rainet.message.util.setSearchForm('host');
			});
			
		},
		
		row : function(row, data, index){
			$(row).attr('id', data.id);
			$(row).attr('data-name', data.code);
		}
	},
	
	// 在共用table的js方法提交参数给后台之前，添加不同模块特有的参数信息
	updateParam : function(param){
		var projectId = $.trim($('#projectNameList').val());
		if (projectId != -1) {
			param.projectId = projectId;
		}
	},
	
	// 获取单个主机的详细信息
	detail : function(self){
		var hostId = $(self).attr('data-id');
		var _this = this;
		rainet.message.service.host.get(hostId, function(data){
			_this.setHostInfo(data, true);
		});
	},
	// 更新单个主机信息
	edit : function(self, $dataTable){
		var hostId = $(self).parent().parent().attr('id');
		var _this = this;
		rainet.message.service.host.get(hostId, function(data){
			_this.setHostInfo(data, false, $dataTable);
		});
	},
	// 删除某个项目
	del : function(self, $datatable){
		var hostId = $(self).parent().parent().attr('id');
		var code = $(self).parent().parent().attr('data-name');
		bootbox.dialog({
			message : "确认删除此主机？",
			title : '删除主机"' + code + '"',
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
				    	  rainet.message.service.host.del(hostId, function(data){
				    		rainet.utils.notification.success('删除成功');
				    		$datatable.api().ajax.reload();
				    	  });
				      }
				}
		}
		});
	},
	
	infoTemplate : "<div>\n"+
		"<form class=\"form-horizontal\" role=\"form\" style=\"padding-right:15px;\" onsubmit=\"return false;\">\n"+
			"<div class=\"form-group\">\n"+
				"<label class=\"col-sm-3 control-label\">所属项目：</label>\n"+
				"<div class=\"col-sm-9 selectItem\">\n"+
    				"<select class=\"form-control projectName\" name=\"projectid\"></select>\n"+
    			"</div>\n"+
    		"</div>\n"+
    		"<div class=\"form-group\">\n"+
    			"<label class=\"col-sm-3 control-label\">主机编号：</label>\n"+
    			"<div class=\"col-sm-9 selectItem\">\n"+
    				"<input type=\"text\" class=\"form-control code\" name=\"code\"/>\n"+
    				"<p class=\"help-block js-placehoder\">只允许8位数字</p>\n"+
    			"</div>\n"+
    		"</div>\n"+
  			"<input type=\"hidden\" name=\"id\" class=\"id\"/>\n"+
  			 "<div class=\"dialog-footer\">\n"+
				"<button data-bb-handler=\"cancel\" type=\"button\" class=\"btn btn-warning\">取消</button>\n"+
				"<button type=\"submit\" class=\"btn btn-success\">修改</button>\n"+
		     "</div>\n"+
		"</form>\n"+
	"</div>\n",
	
	
};

