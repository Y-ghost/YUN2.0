"user strict";

var rainet = rainet || {};

rainet.utils = rainet.utils || {};

rainet.utils.formateDate = function (sTime, format) {
    var _this = new Date();

    if (!!sTime) {
        if (typeof sTime === 'string') {
            sTime = this.replaceAll(sTime, '-', '/');
        }
        _this = new Date(sTime);
    }

    var o = {
        "M+": _this.getMonth() + 1, //month
        "d+": _this.getDate(), //day
        "h+": _this.getHours(), //hour
        "m+": _this.getMinutes(), //minute
        "s+": _this.getSeconds(), //second
        "q+": Math.floor((_this.getMonth() + 3) / 3), //quarter
        "S": _this.getMilliseconds() //millisecond
    };

    if(!format) {
        format = "yyyy-MM-dd hh:mm:ss";
    }

    if (/(y+)/.test(format)) format = format.replace(RegExp.$1, (_this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(format))
            format = format.replace(RegExp.$1,
                RegExp.$1.length == 1 ? o[k] :
                ("00" + o[k]).substr(("" + o[k]).length));
    return format;
}

rainet.utils.serializeObject = function(array){
	var o = {};
    $.each(array, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
}

rainet.utils.notification = {
	success : function(text){
		noty({text : text, type : 'success', timeout : 1500});
	},
	error : function(text){
		noty({text : text, type : 'error', theme : 'bootstrapTheme', timeout : 1500});
	},
	warning : function(text){
		noty({text : text, type : 'warning', theme : 'bootstrapTheme', timeout : 1500});
	},
	wapError : function(text) {
		$('#errorMessage').text(text);
	},
	wapClearError : function() {
		$('#errorMessage').text('');
	}
};

rainet.utils.busy = function(){
	var defaults = {
			template: "<div style=\"filter: progid:DXImageTransform.Microsoft.Alpha(opacity=30);position:absolute;left:0;top:0;right:0;z-index: 9998;bottom:0;height: 100%;width:100%;opacity:.3;background-color:#000;\" ng-show=\"display()\"></div>\n"+
							"<div style=\" position:fixed; *position:absolute;z-index: 9999;top:50%; left:50%;width:200px; margin:-50px 0 0 -50px;font-size:18px;padding-bottom:10px;padding-top:13px;padding-left:30px;padding-right:30px;background-color: #fff;color:#1b926c;\">\n"+
							"<i class=\"fa fa-spinner fa-spin fa-lg\"></i><Label style=\"padding:0 5px;font-size:14px;\"></Label>\n"+
						"</div>\n",
			message:'正在努力加载...'
	};
	
	var mobileStyle = '<div id="busy_layer" class="busy-layer">'+
       '<div class="busy-text"><img src="'+rainet.settings.baseUrl+'images/loading.gif" alt="正在努力加载中。。。" style="width:100px; height:100px"/></div>'+
       '</div>';
	
	var $template = undefined;
	
	var loading = function(text, $ele, mobile) {
		if (!$ele) {
			$ele = $('body');
		}
		var position = $ele.css('position');
		if (position === 'static' || position === '' || typeof position === 'undefined'){
			$ele.css('position','relative');
		}
		if (mobile) {
			$template = $(mobileStyle);
		} else {
			$template = $(defaults.template);
			$('label', $template).text(text || defaults.message);
		}
		$ele.append($template);
	}
	
	var remove = function() {
		if ($template){
			$template.remove();
		}
	}
	
	return {
		loading : loading,
		remove : remove
	};
}();

//获取系统时间
rainet.utils.systime = function(){
	this.current();
}
//每秒增加一秒
rainet.utils.current = function(time){
	rainet.ajax.execute({
		url : rainet.settings.baseUrl+"system/getSystemTime/",
		success : function(data) {
			$(".systime").html(data);
		}
	});
	
	setInterval(function() {
		rainet.utils.current();
	}, 1000); 
}

//退出系统
rainet.utils.exist = function(){
	//添加天气动态
//	$(".systime").html("<iframe width=\"310\" scrolling=\"no\" height=\"25\" frameborder=\"0\" allowtransparency=\"true\" src=\"http://i.tianqi.com/index.php?c=code&id=40&icon=1&num=3\"></iframe>");
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
	$("#exist").click(function(){
		if(confirm("确定退出?")){
			rainet.ajax.execute({
				url : rainet.settings.baseUrl+"User/exist/",
				success : function(data) {
					if(data){
						location.href=rainet.settings.baseUrl+'indexs/login';
//						location.href=rainet.settings.baseUrl+'indexs/index';
					}
				}
			});
		}else{
			return false;
		}
	});
}

rainet.event = {
		click : function($target, callback, event){
			var isSupportTouch = "ontouchend" in document ? true : false;
			if (isSupportTouch) {
				event = event ? event : 'tap';
				$target.each(function(){
					$(this).off(event).on(event, function(){
						callback(this);
					}); 
				});
				
			} else {
				$target.each(function(){
					$(this).off('click').on('click', function(){
						callback(this);
					});
				});
			}
		}
};

rainet.mobile = {
		setBodyHeight : function(){
			var scrollHeight = $('.v-header').height()+$('.v-bottom').height();
			$('#v-content').height( $(window).height()-scrollHeight -10);
		}
}
