;
"user strict";

var rainet = rainet || {};
rainet.findAccount = rainet.findAccount || {};
rainet.findAccount.controller = rainet.findAccount.controller || {};
var code ; //在全局 定义验证码 

rainet.findAccount.view = function() {
		var init = function(){
			var pageType = $("#pageType").val();
			var username = $("#username").val();
			if(pageType=="modifyPassword"&&(username=="" || username==null)){
				location.href=rainet.settings.baseUrl+'indexs/modifyPasswordErr';
			}
			rainet.findAccount.controller[pageType].send();
		};

		return {
			init : init
		};

}();


rainet.findAccount.controller.findAccount = {
		// 添加校验信息 找回用户的时候
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
					validNum : {
						validators : {
							notEmpty : {
								message: '验证码不能为空!'
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
				rainet.findAccount.service["User"].validLoginName(param, function(data){
					if (data) {
						// 存在，更新错误信息的提示
						bv.updateMessage($field, 'notEmpty', '登录名称不存在!');
						bv.updateStatus($field, 'INVALID');
					}
				});
			}).on('blur', '#validNum', function(){
				// 验证码是否正确
				var bv = $form.data('bootstrapValidator');
				$field = bv.getFieldElements('validNum');
				var value = $field.val();
				if ($.trim(value) === '') {
					bv.updateMessage($field, 'notEmpty');
					return ;
				}
				if(value.toUpperCase()!=code){
					rainet.findAccount.code.createCode();
					bv.updateMessage($field, 'notEmpty', '验证码输入不正确，请重新输入!');
					bv.updateStatus($field, 'INVALID');
				}
			});
			
			$form.data('bootstrapValidator').disableSubmitButtons(true);
		},
		send : function(){
				var $form = $("#form");
				$('button[type=submit]',$form).off('click').on('click', function(){
					// 检查验证是否通过
					var bv = $form.data('bootstrapValidator');
					$field = bv.getFieldElements('loginname');
					var value = $field.val();
		        	if (bv.$invalidFields.length > 0) {
		        		return false;
		        	}
		        	var param ={loginname:value};
		        	//注册用户
		        	rainet.findAccount.service["User"].sendEmail(param, function(data){
		        		redirect(data,"findAccount");
					});
				});
				rainet.findAccount.code.createCode();
				rainet.findAccount.code.changeCode();
				// Add validation
				this.setValidate($form);
		}
};

rainet.findAccount.controller.modifyPassword = {
		// 添加校验信息 修改密码的时候
		setValidate : function($form){
			$form.bootstrapValidator({
				feedbackIcons: {
					valid: 'glyphicon glyphicon-ok',
					invalid: 'glyphicon glyphicon-remove',
					validating: 'glyphicon glyphicon-refresh'
				},
				fields : {
					newPassword : {
						validators : {
							notEmpty : {
								message: '新密码不能为空!'
							},
							stringLength: {
								min: 6,
								max: 30,
								message: '密码长度为6~30!'
							}
						}
					},
					rePassword : {
						validators : {
							notEmpty : {
								message: '确认密码不能为空!'
							},
							stringLength: {
								min: 6,
								max: 30,
								message: '密码长度为6~30!'
							}
						}
					}
				}
			}).on('blur', '#rePassword', function(){
				// 验证两次输入的密码是否一致
				var bv = $form.data('bootstrapValidator');
				var password = bv.getFieldElements('newPassword').val();
				$field = bv.getFieldElements('rePassword');
				var rePassword = $field.val();
				
				if (bv.$invalidFields.length > 0) {
					return false;
				}
				
				if (password != rePassword) {
					// 存在，更新错误信息的提示
					bv.updateMessage($field, 'notEmpty', '两次输入的密码不一致!');
					bv.updateMessage($field, 'stringLength', '');
					bv.updateStatus($field, 'INVALID');
				}
			});
			
			$form.data('bootstrapValidator').disableSubmitButtons(true);
		},
		send : function(){
			var $form = $("#form");
			$('button[type=submit]',$form).off('click').on('click', function(){
				// 检查验证是否通过
				var bv = $form.data('bootstrapValidator');
				var userId = $('#userId').val();
				var password = bv.getFieldElements('newPassword').val();
				
				if (bv.$invalidFields.length > 0) {
					return false;
				}
				var param ={userId:userId,password:password};
				//注册用户
				rainet.findAccount.service["User"].modifyPassword(param, function(data){
					if (data) {
						redirect(data,"modifyPassword");
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
	if(methodType=="findAccount"){
		var a = "<div style='padding-left:38px;font-size:14px;'>您的申请已提交成功，请查看您的: <b style='color:#1b926c;font-size:16px;font-weight:700;'>";
		var b = "</b> 邮箱，马上前往查看？</div>";
		var msg = a+data+b;
		bootbox.dialog({
			message : msg ,
			title : '发送邮件成功',
			// 支持ESC
			onEscape : function(){
				
			},
			buttons :  {
				cancel: {
				      label: "取消",
				      className: "btn-warning"
				},
				success: {
				      label: "前往",
				      className: "btn-success",
				      callback : function(){
				    	  location.href="http://mail."+data.split("@")[1];
				      }
				}
		}
		});
	}else if(methodType=="modifyPassword"){
		location.href=rainet.settings.baseUrl+'indexs/modifyPasswordFinish';
	}
}
rainet.findAccount.code={
			createCode : function() {
				code = "";
				var codeLength = 6;// 验证码的长度
				var checkCode = $(".code");
				var selectChar = new Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 'A', 'B', 'C',
						'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
						'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');// 所有候选组成验证码的字符，当然也可以用中文的
		
				for (var i = 0; i < codeLength; i++) {
					var charIndex = Math.floor(Math.random() * 36);
					code += selectChar[charIndex];
		
				}
				if (checkCode) {
					checkCode.html(code);
				}
			},
			changeCode:function(){
				$('.code').off('click').on('click', function(){
					rainet.findAccount.code.createCode();
				});
				$('.codeBtn').off('click').on('click', function(){
					rainet.findAccount.code.createCode();
				});
			}
	
}

rainet.findAccount.url = {
	User : {
		url : rainet.settings.baseUrl + 'User/'
	}
};


rainet.findAccount.service = {
	User : {
		sendEmail : function(param, callback) {
			rainet.ajax.execute({
				url : rainet.findAccount.url.User.url+"sendEmail/",
				data : param,
				$busyEle : $('body'),
				method : 'GET',
				success : function(data) {
					callback(data);
				}
			});
		},
		modifyPassword : function(param, callback) {
			rainet.ajax.execute({
				url : rainet.findAccount.url.User.url+"modifyPassword/",
				data : param,
				$busyEle : $('body'),
				method : 'POST',
				success : function(data) {
					callback(data);
				}
			});
		},
		validLoginName : function(param, callback) {
			rainet.ajax.execute({
				url : rainet.findAccount.url.User.url+"validLoginName/",
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
	rainet.findAccount.view.init();
});



