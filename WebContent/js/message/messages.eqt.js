;
"user strict";

var rainet = rainet || {};
rainet.message = rainet.message || {};
rainet.message.controller = rainet.message.controller || {}; 

// 节点信息
rainet.message.controller.node = {
		
		// 添加校验信息 当保存或修改节点的时候
		setValidate : function($form, operate){
			var _this = this;
			$form.bootstrapValidator({
				feedbackIcons: {
		            valid: 'glyphicon glyphicon-ok',
		            invalid: 'glyphicon glyphicon-remove',
		            validating: 'glyphicon glyphicon-refresh'
		        },
				fields : {
					projectId : {
						validators : {
							notEmpty : {
								message: '项目名称不能为空'
							}
						}
					},
					name: {
						validators : {
							notEmpty : {
								message: '节点名称不能为空'
							}
						}
					},
					code : {
						validators : {
							notEmpty : {
								 message : ' '
							},
							regexp: {
		                        regexp: /^[0-9]{4}$/i,
		                        message : ' '
		                    }
						}
					},
					fowparameter : {
						validators : {
							notEmpty : {
								message : ' '
							},
							between: {
								min: 200,
		                        max: 1000,
		                        message : ' '
		                    }
						}
					},
				}
			})
			
			$form.data('bootstrapValidator').disableSubmitButtons(true);
		},
		
		// 从后台获取数据之后，设置对应节点信息的值，以弹出框的形式展示
		setNodeInfo : function(data, readonly, $dataTable){
			var $nodeHtml = $(this.infoTemplate).attr('id', 'nodeInfo'+data.id).css('display','block');
			$('.code',$nodeHtml).val(data.code);
			$(".id", $nodeHtml).val(data.id);
			$(".fowparameter", $nodeHtml).val(data.fowparameter);
			$(".name", $nodeHtml).val(data.name);
			
			if (readonly) {
				$("input", $nodeHtml).attr('disabled', true);
				$("select", $nodeHtml).attr('disabled', true);
				$('.projectName',$nodeHtml).append('<option>'+data.project.name+'</option>');
				$('button[type=submit]', $nodeHtml).hide();
			}
			
			// 如果是更新操作，就添加修改按钮，并绑定修改函数，如果是查看详情，则没有修改按钮
			var $form = $("form", $nodeHtml);
			$('button[data-bb-handler=cancel]', $form).off('click').on('click', function(){
				bootbox.hideAll();
			});
			
			if (!readonly) {
				// 初始化项目列表
				var $projectList = $('.projectName',$nodeHtml);
				var projectId = data.project.id;
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
					$($form).bootstrapValidator('validate');
					var bv = $form.data('bootstrapValidator');
					if (bv.$invalidFields.length > 0) {
						return false;
					}
					var formData = $form.serializeArray();
					var jsonData = rainet.utils.serializeObject(formData);
					jsonData.project = {id : jsonData.projectId};
					jsonData.projectId = undefined;
					var config = {jsonData : jsonData};
					// 自定义处理错误信息
					config.handleError = function(result){
						$form.data('bootstrapValidator').disableSubmitButtons(true);
						return false;
					}
					// 更新node
					rainet.message.service.node.update(config, function(data){
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
				message : $nodeHtml,
				title : '节点信息',
				// 支持ESC
				onEscape : function(){
					
				}
			});
		},
	
	initProjectList : function(data){
		var length = data.length;
		$('#projectNameListForNode').empty();
		$('#projectNameListForNode').append('<option value=-1>-请选择项目-</option>');
		for (var i = 0; i < length; i++) {
			$('#projectNameListForNode').append('<option value='+data[i].id+'>'+data[i].name+'</option>');
		}
		$('#projectNameListForNode').off('change.rainet').on('change.rainet', function(){
			if($datatable){
				var projectId = $('#projectNameListForNode').val();
				$datatable.api().ajax.reload();
			}
		});
	},
	
	table : {
	  columns : [
		           	{ "sTitle":'序号', "targets": 0, "orderable": false, "render" : rainet.message.util.formateSeq },
		           	{ "sTitle": "节点名称",  "targets": 1, "render" : rainet.message.util.formateLink },
		           	{ "sTitle": "节点编号", "targets": 2 },
		           	{ "sTitle": "流量参数",  "targets": 3 },
		           	{ "sTitle": "所属项目名称", "targets": 4, "orderable": false },
		        	{ "sTitle": "创建时间",  "targets": 5, "render" : rainet.message.util.formateDate },
		           	{ "sTitle": "修改时间", "targets": 6, "render" : rainet.message.util.formateDate },
		           	{ "sTitle": "操作",   "targets": 7, "orderable": false, "data": null,
		    			"defaultContent": rainet.message.util.operaterHtml }
		],
		order : [5, 'desc'],
		
		dataRef : [
		           {'data':'id'},
		           {'data':'name'},
		           {'data':'code'},
		           {'data':'fowparameter'},
		           {'data':'project.name'},
		           {'data':'createtime'},
		           {'data':'modifytime'},
		],
		
		initEvent : function($datatable){
			rainet.message.service.project.getProjectNames(function(data){
				var length = data.length;
				$('#projectNameListForNode').empty();
				$('#projectNameListForNode').append('<option value=-1>-请选择项目-</option>');
				for (var i = 0; i < length; i++) {
					$('#projectNameListForNode').append('<option value='+data[i].id+'>'+data[i].name+'</option>');
				}
				$('#projectNameListForNode').off('change.rainet').on('change.rainet', function(){
					if($datatable){
						var projectId = $('#projectNameListForNode').val();
						$datatable.api().ajax.reload();
					}
				});
				rainet.message.util.setSearchForm('node');
			});
			
		},
		
		row : function(row, data, index){
			$(row).attr('id', data.id);
			$(row).attr('data-name', data.name);
		}
	},
	
	// 在共用table的js方法提交参数给后台之前，添加不同模块特有的参数信息
	updateParam : function(param){
		var projectId = $.trim($('#projectNameListForNode').val());
		if (projectId != -1) {
			param.projectId = projectId;
		}
	},
	// 获取单个节点的详细信息
	detail : function(self){
		var nodeId = $(self).attr('data-id');
		var _this = this;
		rainet.message.service.node.get(nodeId, function(data){
			_this.setNodeInfo(data, true);
		});
	},
	// 更新单个节点信息
	edit : function(self, $dataTable){
		var nodeId = $(self).parent().parent().attr('id');
		var _this = this;
		rainet.message.service.node.get(nodeId, function(data){
			_this.setNodeInfo(data, false, $dataTable);
		});
	},
	// 删除某个节点
	del : function(self, $datatable){
		var nodeId = $(self).parent().parent().attr('id');
		var code = $(self).parent().parent().attr('data-name');
		bootbox.dialog({
			message : "确认删除此节点？",
			title : '删除节点"' + code + '"',
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
				    	  rainet.message.service.node.del(nodeId, function(data){
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
    				"<select class=\"form-control projectName\" name=\"projectId\"></select>\n"+
    			"</div>\n"+
    		"</div>\n"+
    		"<div class=\"form-group\">\n"+
    			"<label class=\"col-sm-3 control-label\">节点名称：</label>\n"+
    			"<div class=\"col-sm-9 selectItem\">\n"+
    				"<input type=\"text\" class=\"form-control name\" name=\"name\"/>\n"+
    			"</div>\n"+
    		"</div>\n"+
    		"<div class=\"form-group\">\n"+
				"<label class=\"col-sm-3 control-label\">节点地址：</label>\n"+
				"<div class=\"col-sm-9 selectItem\">\n"+
					"<input type=\"text\" class=\"form-control code\" name=\"code\"/>\n"+
					"<p class=\"help-block js-placehoder\">*节点地址为4位纯数字</p>\n"+
				"</div>\n"+
			"</div>\n"+
			"<div class=\"form-group\">\n"+
			"<label class=\"col-sm-3 control-label\">流量参数：</label>\n"+
			"<div class=\"col-sm-9 selectItem\">\n"+
				"<input type=\"text\" class=\"form-control fowparameter\" name=\"fowparameter\"/>\n"+
				"<p class=\"help-block js-placehoder\">*参数为【200,1000】的整数</p>\n"+
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

