;
"user strict";

var rainet = rainet || {};
rainet.message = rainet.message || {};
rainet.message.controller = rainet.message.controller || {}; 

// 用户信息
rainet.message.controller.user = {
		
		// 添加校验信息 当保存或修改用户的时候
		setValidate : function($form, operate){
			var _this = this;
			$form.bootstrapValidator({
				feedbackIcons: {
		            valid: 'glyphicon glyphicon-ok',
		            invalid: 'glyphicon glyphicon-remove',
		            validating: 'glyphicon glyphicon-refresh'
		        },
				fields : {
					username: {
						validators : {
							notEmpty : {
								message: '用户名不能为空'
							}
						}
					},
					phone : {
						validators : {
							notEmpty : {
								 message : '电话不能为空'
							},
							regexp: {
		                        regexp: /((^1\d{10})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$)/g,
		                        message : '电话的格式不正确'
		                    }
						}
					},
					email : {
						validators : {
							notEmpty : {
								message : '邮箱不能为空'
							},
							emailAddress: {
		                        message: '邮箱格式不正确'
		                    }
						}
					},
				}
			})
			
			$form.data('bootstrapValidator').disableSubmitButtons(true);
		},
		
		// 从后台获取数据之后，设置对应用户信息的值，以弹出框的形式展示
		setUserInfo : function(data, readonly, $dataTable){
			var $userHtml = $(this.infoTemplate).attr('id', 'userInfo'+data.id);
			$('.loginname',$userHtml).val(data.loginname);
			$(".id", $userHtml).val(data.id);
			$(".username", $userHtml).val(data.username);
			$(".phone", $userHtml).val(data.phone);
			$(".email", $userHtml).val(data.email);
			$(".address", $userHtml).val(data.address);
			$(".website", $userHtml).val(data.website);
			$(".createtime", $userHtml).val(rainet.message.util.formateDate(data.createtime));
			$(".modifytime", $userHtml).val(rainet.message.util.formateDate(data.modifytime));
			
			if (readonly) {
				$("input", $userHtml).attr('disabled', true);
				$("select", $userHtml).attr('disabled', true);
				$('button[type=submit]', $userHtml).hide();
			}
			
			// 如果是更新操作，就添加修改按钮，并绑定修改函数，如果是查看详情，则没有修改按钮
			var $form = $("form", $userHtml);
			$('button[data-bb-handler=cancel]', $form).off('click').on('click', function(){
				bootbox.hideAll();
			});
			
			if (!readonly) {
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
					var config = {jsonData : jsonData};
					// 自定义处理错误信息
					config.handleError = function(result){
						$form.data('bootstrapValidator').disableSubmitButtons(true);
						return false;
					}
					// 更新个人信息
					rainet.message.service.user.update(config, function(data){
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
				message : $userHtml,
				title : '用户信息',
				// 支持ESC
				onEscape : function(){
					
				}
			});
		},
	
	table : {
	  columns : [
		           	{ "sTitle": "序号",  "targets": 0, "orderable": false, "render" : rainet.message.util.formateSeq },
		           	{ "sTitle": "登录名",  "targets": 1, "render" : rainet.message.util.formateLink },
		           	{ "sTitle": "用户名", "targets": 2 },
		           	{ "sTitle": "电话",  "targets": 3 },
		           	{ "sTitle": "邮箱", "targets": 4 },
		        	{ "sTitle": "创建时间",  "targets": 5, "render" : rainet.message.util.formateDate },
		           	{ "sTitle": "操作",   "targets": 6, "orderable": false, "data": null,
		    			"defaultContent": rainet.message.util.operaterHtml }
		],
		order : [5, 'desc'],
		
		dataRef : [
		           {'data':'id'},
		           {'data':'loginname'},
		           {'data':'username'},
		           {'data':'phone'},
		           {'data':'email'},
		           {'data':'createtime'},
		],
		
		initEvent : function($datatable){
			$('#loginName').off('keyup').on('keyup', function(event){
				if(event.keyCode == 13){
			    	$('#userSearchBtn').click();
			    }
			});
			$('#userSearchBtn').off('click').on('click', function(){
				if($datatable){
					$datatable.api().ajax.reload();
				}
			});
			rainet.message.util.setSearchForm('user');
		},
		
		row : function(row, data, index){
			$(row).attr('id', data.id);
			$(row).attr('data-name', data.loginname);
		}
	},
	
	// 在共用table的js方法提交参数给后台之前，添加不同模块特有的参数信息
	updateParam : function(param){
		var loginName = $.trim($('#loginName').val());
		if (loginName) {
			param.loginName = loginName;
		}
	},
	// 获取单个用户的详细信息
	detail : function(self){
		var userId = $(self).attr('data-id');
		var _this = this;
		rainet.message.service.user.get(userId, function(data){
			_this.setUserInfo(data, true);
		});
	},
	// 更新单个用户信息
	edit : function(self, $dataTable){
		var userId = $(self).parent().parent().attr('id');
		var _this = this;
		rainet.message.service.user.get(userId, function(data){
			_this.setUserInfo(data, false, $dataTable);
		});
	},
	// 删除某个用户
	del : function(self, $datatable){
		var userId = $(self).parent().parent().attr('id');
		var code = $(self).parent().parent().attr('data-name');
		bootbox.dialog({
			message : "确认删除此用户？",
			title : '删除用户"' + code + '"',
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
				    	  rainet.message.service.user.del(userId, function(data){
				    		rainet.utils.notification.success('删除成功');
				    		$datatable.api().ajax.reload();
				    	  });
				      }
				}
		}
		});
	},
	
	infoTemplate : "<div>\n"+
		"<form class=\"form-horizontal\" role=\"form\" onsubmit=\"return false;\">\n"+
			"<div class=\"form-group\">\n"+
				"<label class=\"col-sm-3 control-label\">登录名：</label>\n"+
				"<div class=\"col-sm-9\">\n"+
					"<input type=\"text\" class=\"form-control loginname\" readonly/>\n"+
    			"</div>\n"+
    		"</div>\n"+
    		"<div class=\"form-group\">\n"+
    			"<label class=\"col-sm-3 control-label\">用户名：</label>\n"+
    			"<div class=\"col-sm-9\">\n"+
    				"<input type=\"text\" class=\"form-control username\" name=\"username\"/>\n"+
    			"</div>\n"+
    		"</div>\n"+
    		"<div class=\"form-group\">\n"+
				"<label class=\"col-sm-3 control-label\">电话：</label>\n"+
				"<div class=\"col-sm-9\">\n"+
					"<input type=\"text\" class=\"form-control phone\" name=\"phone\"/>\n"+
				"</div>\n"+
			"</div>\n"+
			"<div class=\"form-group\">\n"+
				"<label class=\"col-sm-3 control-label\">邮箱：</label>\n"+
				"<div class=\"col-sm-9\">\n"+
					"<input type=\"text\" class=\"form-control email\" name=\"email\"/>\n"+
				"</div>\n"+
			"</div>\n"+
			"<div class=\"form-group\">\n"+
			"<label class=\"col-sm-3 control-label\">地址：</label>\n"+
			"<div class=\"col-sm-9\">\n"+
				"<input type=\"text\" class=\"form-control address\" name=\"address\"/>\n"+
			"</div>\n"+
			"</div>\n"+
			"<div class=\"form-group\">\n"+
				"<label class=\"col-sm-3 control-label\">网址：</label>\n"+
				"<div class=\"col-sm-9\">\n"+
					"<input type=\"text\" class=\"form-control website\" name=\"website\"/>\n"+
				"</div>\n"+
			"</div>\n"+
			"<div class=\"form-group\">\n"+
				"<label class=\"col-sm-3 control-label\">创建时间：</label>\n"+
				"<div class=\"col-sm-9\">\n"+
					"<input type=\"text\" class=\"form-control createtime\" readonly/>\n"+
				"</div>\n"+
			"</div>\n"+
			"<div class=\"form-group\">\n"+
				"<label class=\"col-sm-3 control-label\">修改时间：</label>\n"+
				"<div class=\"col-sm-9\">\n"+
					"<input type=\"text\" class=\"form-control modifytime\" readonly/>\n"+
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

