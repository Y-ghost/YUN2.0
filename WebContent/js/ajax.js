"user strict";

var rainet = rainet || {};

rainet.ajax = {
		execute : function(options) {
			var $loginHtml = $(this.infoTempate);
			$.ajax({
				  beforeSend : function(){
					  rainet.utils.busy.remove();
					  rainet.utils.busy.loading(options.busyMsg, options.$busyEle);
					  if (options.beforeSend) {
						  options.beforeSend();
					  }
				  },
				  cache : false,
				  async : options.async || true,
				  type: options.method || 'GET',
				  url: options.url,
				  // data to be added to query string:
				  data: options.data,
				  // type of data we are expecting in return:
				  contentType : options.contentType,
				  dataType: options.dataType || 'json',
				  jsonp: options.jsonp,
				  success: function(data){
					  rainet.utils.busy.remove();
					  var status = data.code;
					  if (status != '200') {
						  // 自定义处理错误
						  if(status == 'A409'){
							  rainet.ajax.controller.send($loginHtml);
						  }
						  var isContinue = true;
						  if (options.customHandleError) {
							  // 如果返回false，说明不会用统一的错误处理
							  isContinue = options.customHandleError(data);
						  }
						  if (isContinue){
							  rainet.utils.notification.error(data.message);
						  }
						  return ;
					  }
					  if (options.success) {
						  return options.success(data.data);
					  }
				  },
				  error: function(xhr, type){
					  rainet.utils.busy.remove();
					  rainet.utils.notification.error('服务异常');
					  return ;
				  }
				});
		},
		infoTempate : "<div style=\"margin-top:20px;\">\n"+
		"<form class=\"form-horizontal\" id=\"loginForm\" role=\"form\" onsubmit=\"return false;\">\n"+
			"<div class=\"form-group\">\n"+
    			"<label class=\"col-sm-3 control-label\">登录名/邮箱：</label>\n"+
    			"<div class=\"col-sm-8\">\n"+
    				"<input type=\"text\" class=\"form-control\" name=\"loginname\" value='' id=\"loginname\"/>\n"+
    			"</div>\n"+
  			"</div>\n"+
  			"<div class=\"form-group\">\n"+
    			"<label class=\"col-sm-3 control-label\">密码：</label>\n"+
    			"<div class=\"col-sm-8\">\n"+
    				"<input type=\"password\"  class=\"form-control\" value='' name=\"password\"/>\n"+
    			"</div>\n"+
  			"</div>\n"+
  			 "<div class=\"dialog-footer\" style=\"margin-top:30px;\">\n"+
		  			 "<div class=\"col-sm-4 control-label\" style=\"text-align:center;\">" +
		  			 "<a href=\"findAccount\">忘记密码?</a>" +
		  			 "</div>" +
		  			 "<button data-bb-handler=\"success\" type=\"button\" id=\"loginButton\" class=\"col-sm-4 btn btn-success\">登录</button>\n"+
		  			 "<div class=\"col-sm-4 control-label\" style=\"text-align:center;\">" +
		  			 "没有账号？<a href=\"register\">立即注册</a>" +
		  			 "</div>" +
			"</div>\n"+
		"</form>\n"+
	"</div>"
};

rainet.ajax.controller = {
		// 用户登录校验
		setValidate : function($form){
			$form.bootstrapValidator({
				feedbackIcons: {
					valid: 'glyphicon glyphicon-ok',
					invalid: 'glyphicon glyphicon-remove',
					validating: 'glyphicon glyphicon-refresh'
				},
				fields : {
					loginname : {
						validators : {
							notEmpty : {
								message: '登录名不能为空!'
							}
						}
					},
					password : {
						validators : {
							notEmpty : {
								message: '密码不能为空!'
							},
							stringLength: {
								min: 6,
								max: 30,
								message: '密码长度为6~30!'
							}
						}
					}
				}
			}).on('blur', '#loginname', function(){
				// 验证项目名称是否存在
				var bv = $form.data('bootstrapValidator');
				$field = bv.getFieldElements('loginname');
				var value = $field.val();
				if ($.trim(value) === '') {
					bv.updateMessage($field, 'notEmpty');
					return ;
				}
				var param = {loginname : value};
				rainet.ajax.service["User"].validLoginName(param, function(data){
					if (data) {
						// 不存在，更新错误信息的提示
						bv.updateMessage($field, 'notEmpty', '登录名称不存在!');
						bv.updateStatus($field, 'INVALID');
					}
				});
			});
			bootbox.dialog({
				message : $form,
				title : '欢迎登录',
				// 支持ESC
				onEscape : function(){
					
				}
			});
			$form.data('bootstrapValidator').disableSubmitButtons(true);
		},
		send : function($loginHtml){
//			alert("登录超时，请重新登录！");
//			location.href=rainet.settings.baseUrl+"indexs/index";
			
			var $form = $loginHtml;
			//keyCode=13是回车键
			$("body").keydown(function() {
				if (event.keyCode == "13") {
					$('button[id=loginButton]',$form).click();
				}
			});
			$('button[id=loginButton]',$form).off('click').on('click', function(){
				var bv = $form.data('bootstrapValidator');
				if (bv.$invalidFields.length > 0) {
					return false;
				}
				var loginname = bv.getFieldElements('loginname').val();
				var password = bv.getFieldElements('password').val();

				var param = {loginname : loginname , password : password};
				//用户登录
				rainet.ajax.service["User"].login(param, function(data){
					if (data) {
						bootbox.hideAll();
						rainet.utils.notification.success("登录成功!");
						location.reload() ;
					}else{
						return false;
					}
				});
			});
			// Add validation
			this.setValidate($form);
		}
};


rainet.ajax.service = {
	User : {
		login : function(param, callback) {
			rainet.ajax.execute({
				url : rainet.settings.baseUrl + "User/" + "login/",
				data : param,
				$busyEle : $('#form'),
				method : 'POST',
				success : function(data) {
					callback(data);
				}
			});
		},
		validLoginName : function(param, callback) {
			rainet.ajax.execute({
				url : rainet.settings.baseUrl + "User/" + "validLoginName/",
				data : param,
				$busyEle : $('#form'),
				method : 'GET',
				success : function(data) {
					callback(data);
				}
			});
		}
	}
};
