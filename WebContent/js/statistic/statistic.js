;
"user strict";

var rainet = rainet || {};
rainet.statistic = rainet.statistic || {};
var pageNum = 1;
var pages = 1;

// 设置管理所用模块的视图
rainet.statistic.view = function(){
	// 查看项目下节点信息
	var setView = function() {
		rainet.statistic.controller.water.init();
	};
	
	// 初始化信息管理页面
	var init = function(){
		setView();
		
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
	$("#homeLab").html("统计分析");
	$("#homeHref").attr("href","statistic");
	$(".startDate").pickadate({
	    today: '今天',
	    clear: '关闭',
	    selectYears: true,
	    selectMonths: true
		});
	$(".endDate").pickadate({
		today: '今天',
		clear: '关闭',
		selectYears: true,
		selectMonths: true
	});
	rainet.statistic.view.init();
});
