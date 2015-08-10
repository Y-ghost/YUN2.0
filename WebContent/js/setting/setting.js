;
"user strict";

var rainet = rainet || {};
rainet.setting = rainet.setting || {};

// 设置管理所用模块的视图
rainet.setting.view = function(){
	var defaultView = 'project';
	
	var handlMenuView = function(currentEle){
		var $ele = $(currentEle);
		$ele.siblings().removeClass('active');
		$ele.addClass('active');
		
		var module = $ele.attr('data-name');
		setView(module);
	}
	// 根据不同的模块，加载不用的列表信息
	var setView = function(module){
		rainet.setting.controller[module].add();
	}
	
	var bindEvent = function(){
		$('.list-group-item').off('click').on('click', function(){
			handlMenuView(this);
		});
	};
	
	// 初始化信息管理页面
	var init = function(){
		bindEvent();
		setView(defaultView);
		//获取系统时间
//		rainet.utils.systime();
		//退出系统
		rainet.utils.exist();
	}
	
	return {
		init : init
	};
	
}();

$(document).ready(function(){
	$("#homeLab").html("系统设置");
	$("#homeHref").attr("href","setting");
	rainet.setting.view.init();
});
