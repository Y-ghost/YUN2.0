;
"user strict";

var rainet = rainet || {};
rainet.header = rainet.header || {};

rainet.header.view = function() {
	
	var _bindEvent = function() {
		rainet.event.click($('button', '.navbar-header'), function(){
			var id = $('button', '.navbar-header').attr('data-target');
			var $target = $(id);
			if ($target.hasClass('collapse')) {
				$target.removeClass('collapse');
				$target.addClass('in');
			} else {
				$target.removeClass('in');
				$target.addClass('collapse');
			}
		});
		rainet.event.click($('#exist'), function(){
			if(confirm("确定退出?")){
				rainet.ajax.execute({
					url : rainet.settings.baseUrl + "User/exist/",
					success : function(data) {
						if(data){
							location.href=rainet.settings.baseUrl + 'indexs/login';
						}
					}
				});
			}
		});
	};
	
	var init = function(){
		_bindEvent();
	};

	return {
			init : init
	};

}();
