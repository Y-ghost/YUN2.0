;
"user strict";

var rainet = rainet || {};
rainet.login = rainet.login || {};

rainet.login.view = function() {
	
	var _submit = function() {
		var uname = $('#loginname').val();
		var passwd = $('#password').val();
		if (!uname.trim()) {
			rainet.utils.notification.wapError("登录名不能为空");
			return false;
		}
		if (!passwd.trim()) {
			rainet.utils.notification.wapError("密码不能为空!");
			return false;
		}
		
		var param = {loginname : uname , password : passwd};
		//用户登录
		rainet.login.service["User"].login(param, function(data){
			if (data) {
				location.href = rainet.settings.baseUrl + 'indexs/index';
			}
		});
		
		
	};
	
	var _bindEvent = function() {
		rainet.event.click($('#submitBtn'), function(){
			_submit();
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
				customHandleError : function(data){
					rainet.utils.notification.wapError(data.message);
					return true;
				},
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
	rainet.login.view.init();
});



