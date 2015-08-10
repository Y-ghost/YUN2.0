;
"user strict";

var rainet = rainet || {};
rainet.login = rainet.login || {};
rainet.login.controller = rainet.login.controller || {};

rainet.login.view = function() {
		var init = function(){
			var pageType = $("#pageType").val();
			rainet.login.controller[pageType].send();
		};

		return {
			init : init
		};

}();


rainet.login.controller.register= {
		// 添加校验信息 注册新用户的时候
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
					},
					email : {
						validators : {
							notEmpty : {
								message: '邮箱不能为空,用于找回密码!'
							},
							regexp: {
		                        regexp: /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/,
		                        message: '邮箱格式不正确!'
		                    }
						}
					}
					,
					serviceAgreement : {
						validators : {
							notEmpty : {
								message: '请选择接受用户服务协议!'
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
				rainet.login.service["User"].validLoginName(param, function(data){
					if (!data) {
						// 存在，更新错误信息的提示
						bv.updateMessage($field, 'notEmpty', '登录名称已存在!');
						bv.updateStatus($field, 'INVALID');
					}
				});
			}).on('blur', '#email', function(){
				// 验证项目名称是否存在
				var bv = $form.data('bootstrapValidator');
				$field = bv.getFieldElements('email');
				var value = $field.val();
				if ($.trim(value) === '') {
					bv.updateMessage($field, 'notEmpty');
					return ;
				}
				var param = {loginname : value};
				rainet.login.service["User"].validLoginName(param, function(data){
					if (!data) {
						// 存在，更新错误信息的提示
						bv.updateMessage($field, 'notEmpty', '邮箱已注册，您可以使用该邮箱登录，也可以换一个邮箱重新注册!');
						bv.updateMessage($field, 'regexp', '');
						bv.updateStatus($field, 'INVALID');
					}
				});
			});
			
			$form.data('bootstrapValidator').disableSubmitButtons(true);
		},
		send : function(){
				var $form = $("#form");
				$("body").keydown(function() {
			        if (event.keyCode == "13") {
			        	if($("#serviceAgreement").is(':checked')){
			        		$('button[type=submit]',$form).click();
						}else{
							alert("请选择接受用户服务协议!");
							return false;
						}
			        }
				 });
				$('button[type=submit]',$form).off('click').on('click', function(){
					if($("#serviceAgreement").is(':checked')){
						// 检查验证是否通过
						var bv = $form.data('bootstrapValidator');
						if (bv.$invalidFields.length > 0) {
							return false;
						}
						var formData = $form.serializeArray();
						var jsonData = rainet.utils.serializeObject(formData);
						//注册用户
						rainet.login.service["User"].register(jsonData, function(data){
							if (data) {
								if(confirm("注册成功，现在就去登录?")){
									redirect(data,"register");
								}
							}else{
								return false;
							}
						});
					}else{
						alert("请选择接受用户服务协议!");
						return false;
					}
				});
				// Add validation
				this.setValidate($form);
		}
};

rainet.login.controller.login = {
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
				rainet.login.service["User"].validLoginName(param, function(data){
					if (data) {
						// 不存在，更新错误信息的提示
						bv.updateMessage($field, 'notEmpty', '登录名称不存在!');
						bv.updateStatus($field, 'INVALID');
					}
				});
			});
			
			$form.data('bootstrapValidator').disableSubmitButtons(true);
		},
		send : function(){
			var $form = $("#form");
			//keyCode=13是回车键
			$("body").keydown(function() {
				if (event.keyCode == "13") {
					$('button[type=submit]',$form).click();
				}
			});
			$('button[type=submit]',$form).off('click').on('click', function(){
				// 检查验证是否通过
				var bv = $form.data('bootstrapValidator');
				if (bv.$invalidFields.length > 0) {
					return false;
				}
				var loginname = bv.getFieldElements('loginname').val();
				var password = bv.getFieldElements('password').val();
				
				var param = {loginname : loginname , password : password};
				//用户登录
				rainet.login.service["User"].login(param, function(data){
					if (data) {
						redirect(data,"login");
					}else{
						return false;
					}
				});
			});
			// Add validation
			this.setValidate($form);
		}
};

//注册成功跳转
var redirect = function(data,methodType){
	if(methodType=="register"){
		location.href=rainet.settings.baseUrl+'indexs/login';
	}else if(methodType=="login"){
		location.href=rainet.settings.baseUrl+'indexs/control';
	}
}


rainet.login.url = {
	User : {
		url : rainet.settings.baseUrl + 'User/'
	}
};


rainet.login.service = {
	User : {
		login : function(param, callback) {
			rainet.ajax.execute({
				url : rainet.login.url.User.url+"login/",
				data : param,
				$busyEle : $('body'),
				method : 'POST',
				success : function(data) {
					callback(data);
				}
			});
		},
		register : function(param, callback) {
			rainet.ajax.execute({
				url : rainet.login.url.User.url+"register/",
				data : JSON.stringify(param),
				$busyEle : $('body'),
				method : 'POST',
				contentType : 'application/json; charset=utf-8',
				success : function(data) {
					callback(data);
				}
			});
		},
		validLoginName : function(param, callback) {
			rainet.ajax.execute({
				url : rainet.login.url.User.url+"validLoginName/",
				data : param,
				$busyEle : $('body'),
				method : 'GET',
				success : function(data) {
					callback(data);
				}
			});
		}
	}
};

$(document).ready(function() {
	//隐藏退出按钮
	$("#exist").css("display","none");
	//添加IE判断，6、7、8版本的提醒更换浏览器
	if (!$.support.leadingWhitespace){
		$(".header").addClass("checkIE");
		$("body").append("<div class='topDiv' style='overflow:hidden; text-align:center;width:98%;position:fixed; *position:absolute;z-index: 9999;top:1%; left:1%;color:red;'>请选择IE9或更高版本访问，建议使用Google Chrome浏览器，显示效果会更好！<a src='javascript:void(0);' class='topBtn cursor' style='float:right;height:30px;width:30px; margin-right:50px;color:red;'>x</a></div>");
	}
	$(".topBtn").click(function(){
		$(".header").removeClass("checkIE");
		$(".topDiv").css('display','none');
	});
	//添加底部年限
	var myDate = new Date();
	$(".copyYear").html(myDate.getFullYear());
	rainet.login.view.init();
});



