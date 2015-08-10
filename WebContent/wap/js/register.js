;
"user strict";

var rainet = rainet || {};
rainet.register = rainet.register || {};

rainet.register.view = function() {
	
	var _submit = function() {
		var uname = $('#loginname').val();
		var passwd = $('#password').val();
		var email = $('#email').val();
		var phone = $('#phone').val();
		var address = $('#address').val();
		if (!uname.trim()) {
			rainet.utils.notification.wapError("登录名不能为空");
			return false;
		}
		if (!$('#loginname').data('result')) {
			rainet.utils.notification.wapError("登录名称已存在");
			return false;
		}
		if (!passwd.trim()) {
			rainet.utils.notification.wapError("密码不能为空");
			return false;
		}
		
		if (!email.trim()) {
			rainet.utils.notification.wapError("邮箱不能为空,用于找回密码");
			return false;
		}
		if (!/^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/.test(email)) {
			rainet.utils.notification.wapError("邮箱格式不正确");
			return false;
		}
		
		if (!$('#email').data('result')) {
			rainet.utils.notification.wapError("邮箱已被注册");
			return false;
		}
		
		if (!$('#serviceAgreement').is(':checked')) {
			rainet.utils.notification.wapError("请选择接受用户服务协议");
			return false;
		}
		
		rainet.utils.notification.wapClearError();
		var formData = $('#form').serializeArray();
		var jsonData = rainet.utils.serializeObject(formData);
		//用户注册
		rainet.register.service["User"].register(jsonData, function(data){
			if (data) {
				if(confirm("注册成功，现在就去登录?")){
					location.href = rainet.settings.baseUrl + 'indexs/login';
				}
			}
		});
		
		
	};
	
	var _validLoginName = function($targe, message){
		var param = {loginname : $targe.val()};
		rainet.register.service["User"].validLoginName(param, function(data){
			if (!data) {
				// 存在，更新错误信息的提示
				$targe.data('result', false);
				rainet.utils.notification.wapError(message);
			} else {
				$targe.data('result', true);
				rainet.utils.notification.wapClearError();
			}
		});
	}
	
	var _bindEvent = function() {
		rainet.event.click($('#submitBtn'), function(){
			_submit();
		});
		$('#loginname').off('blur').on('blur', function(){
			var message = '登录名称已存在';
			_validLoginName($('#loginname'), message);
		});
		
		$('#email').off('blur').on('blur', function(){
			var message = '邮箱已被注册';
			_validLoginName($('#email'), message);
		});
		
	};
	
	var init = function(){
		rainet.mobile.setBodyHeight();
		_bindEvent();
	};

	return {
			init : init
	};

}();


rainet.register.url = {
	User : {
		url : rainet.settings.baseUrl + 'User/'
	}
};


rainet.register.service = {
	User : {
		register : function(param, callback) {
			rainet.ajax.execute({
				url : rainet.register.url.User.url+"register/",
				data : JSON.stringify(param),
				customHandleError : function(data){
					rainet.utils.notification.wapError(data.message);
					return true;
				},
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
				url : rainet.register.url.User.url+"validLoginName/",
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
	rainet.register.view.init();
});



