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
			if(pageType == "modifyPassword" && (username == "" || username == null)){
				location.href=rainet.settings.baseUrl+'indexs/modifyPasswordErr';
			} else {
				rainet.findAccount.controller[pageType].init();
			}
		};

		return {
			init : init
		};

}();


rainet.findAccount.controller.findAccount = {
		// 添加校验信息 找回用户的时候
		bindEvent : function(){
			rainet.findAccount.code.createCode();
			rainet.findAccount.code.changeCode();
			var self = this;
			rainet.event.click($('#submitBtn'), function(){
				self.submit();
			});
			$('#loginname').off('blur').on('blur', function(){
				var value = $('#loginname').val();
				
				if (!value.trim()) {
					rainet.utils.notification.wapError("登录名不能为空");
					return false;
				}
				
				var param = {loginname : value};
				rainet.findAccount.service["User"].validLoginName(param, function(data){
					if (data) {
						// 存在，更新错误信息的提示
						rainet.utils.notification.wapError("登录名称不存在");
					} else {
						rainet.utils.notification.wapClearError();
					}
				});
			});
			
		},
		submit : function() {
			var loginname = $('#loginname').val();
			var validCode = $('#validNum').val();
			
			if (!loginname.trim()) {
				rainet.utils.notification.wapError("登录名不能为空");
				return false;
			}
			
			if (!validCode.trim()) {
				rainet.utils.notification.wapError("验证码不能为空");
				return false;
			}
			
			if (validCode.trim() != code) {
				rainet.utils.notification.wapError("验证码不正确");
				return false;
			}
			
			var param ={loginname:loginname};
        	//注册用户
        	rainet.findAccount.service["User"].sendEmail(param, function(data){
        		data = data.substring(0, 2) + "***" + data.substring(data.indexOf("@"))
        		$('#emailMark').text(data);
        		$('#v-content').hide();
        		$('#email-suc-con').show();
			});
		},
		init : function(){
			rainet.mobile.setBodyHeight();
			this.bindEvent();
		}
};

rainet.findAccount.controller.modifyPassword = {
		// 绑定事件
		bindEvent : function(){
			var self = this;
			rainet.event.click($('#submitBtn'), function(){
				self.submit();
			});
		},
		// 提交
		submit : function() {
			var newPassword = $('#newPassword').val();
			var rePassword = $('#rePassword').val();
			
			if (!newPassword.trim()) {
				rainet.utils.notification.wapError("新密码不能为空");
				return false;
			}
			var length = newPassword.length;
			if (length < 6 || length > 30) {
				rainet.utils.notification.wapError("密码长度为6~30");
				return false;
			}
			
			if (newPassword != rePassword) {
				rainet.utils.notification.wapError("两次输入的密码不一致");
				return false;
			}
			
			var userId = $('#userId').val();
			
			var param ={userId:userId,password:newPassword};
			//修改密码
			rainet.findAccount.service["User"].modifyPassword(param, function(data){
				if (data) {
					location.href = rainet.settings.baseUrl + 'indexs/modifyPasswordFinish';
				}
			});
		},
		init : function(){
			rainet.mobile.setBodyHeight();
			this.bindEvent();
		}
};

rainet.findAccount.code={
			createCode : function() {
				code = "";
				var codeLength = 4;// 验证码的长度
				var checkCode = $(".code");
				var selectChar = new Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);// 所有候选组成验证码的字符，当然也可以用中文的
		
				for (var i = 0; i < codeLength; i++) {
					var charIndex = Math.floor(Math.random() * 10);
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
				customHandleError : function(data){
					rainet.utils.notification.wapError(data.message);
					return true;
				},
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
				customHandleError : function(data){
					rainet.utils.notification.wapError(data.message);
					return true;
				},
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
				customHandleError : function(data){
					rainet.utils.notification.wapError(data.message);
					return true;
				},
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
	rainet.findAccount.view.init();
});



