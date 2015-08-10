;
"user strict";

var rainet = rainet || {};
rainet.message = rainet.message || {};
rainet.message.controller = rainet.message.controller || {}; 

// 项目信息
rainet.message.controller.node = {
	
  setData : function(datas) {
	  var html = '<li>'+
			'<div class="detail" >'+
		'<div class="fl" style="width:80%;">'+
			'<label class="detail-item" style="font-size:16px;">水泵 </label><br/>'+
			'<label>状态:</label><span>开启</span>'+
		'</div>'+
		'<div class="fr" id="shuibeng">'+
			'<i class="fa fa-toggle-on fa-3x"></i>'+
		'</div>'+
	'</div>'+
	'</li>'+
	'<li>'+
	'<div class="detail" id="shifeiguan">'+
		'<div class="fl" style="width:80%;" >'+
			'<label class="detail-item" style="line-height:40px;font-size:16px;">施肥罐 </label><br/>'+
		'</div>'+
		'<div class="fr">'+
			'<i class="fa fa-toggle-off fa-3x"></i>'+
		'</div>'+
	'</div>'+
	'</li>';
	  var result = html;
	  $.each(datas, function(i, item){
		  var eTemperature = "异常";
		  var eStatus = "异常";
		  var switchBtn = 'on';
		  if (item.equipmentStatus) {
			  eTemperature = item.equipmentStatus.temperature;
			  eStatus = item.equipmentStatus.status;
			  if (eStatus != '阀门开启') {
				  switchBtn = 'off';
			  }
		  }
			var li = '<li style="padding-top:10px;padding-bottom:10px;">'+
				'<div class="detail" >'+
			'<div class="fl" style="width:80%;">'+
				'<label class="detail-item" style="font-size:16px;">'+item.name+'</label><br/>'+
				'<label>土壤湿度：</label><span>'+eTemperature+'&nbsp;&nbsp;℃</span>'+
				'<label>&nbsp;&nbsp;湿度值一：</label><span>80%</span><br/>'+
				'<label>阀门状态：</label><span>'+eStatus+'</span>'+
				'</div>'+
				'<div class="fr switch-js '+switchBtn+'" style="height:75px;" id='+item.id+'>'+
					'<i class="fa fa-toggle-'+switchBtn+'  fa-3x" style="line-height:75px;"></i>'+
				'</div>'+
			'</div>'+
			'</li>';
			result +=li;
	 });
	 return result;
  },
  
  updateParam : function(param) {
	  var pId = $('#pId').val();
	  if (pId.trim()) {
		  param.pId = pId;
	  }
  },
  
  loadData : function(param, $ul, callback) {
	  var data = {param : param};
	  data.handleError = function(result) {
		  $('span','#noData').text(result.message);
		  $('#noData').show();
	  };
	  var that = this;
	  rainet.message.service.node.listByPid(data, function(result){
		  if (!result || !result.length) {
			  $('#noData').show();
			  return;
		  }
		  var lis = that.setData(result);
		  $ul.append(lis);
		  if (callback) {
			  callback();
		  };
		  // 水泵开关
		  rainet.event.click($('#shuibeng'), function(self){
			  
		  });
		  // 施肥罐开关
		  rainet.event.click($('#shifeiguan'), function(self){
			  
		  });
		  // 开关绑定事件
		  rainet.event.click($('.switch-js', '#list'), function(self){
				var param  = {optionType: 0, id: $(self).attr('id') };
				var message = '关闭';
				var currentBtnStatus = 'on';
				var nextBtnStatus = 'off';
				if ($(self).hasClass('off')) {
					param.optionType = 1;
					message = '开启';
					nextBtnStatus = 'on';
					currentBtnStatus = 'off';
				}
				var tipMessage = '确认' + message + '?';
				if(confirm(tipMessage)) {
					rainet.message.service.node.openOrClose(param, function(result){
						if(data){
							$(self).removeClass(currentBtnStatus);
							$(self).addClass(nextBtnStatus);
							var $i = $('i', $(self));
							$i.removeClass('fa-toggle-' + currentBtnStatus);
							$i.addClass('fa-toggle-' + nextBtnStatus);
							alert(message + '实时灌溉成功');
							// 整体刷新
							//window.reload();
							// 或者只更新当前节点信息
						}else{
							alert(message + '实时灌溉失败');
						}
					});
				}
			});
		  
	  });
	  
  },
  disabledPullUp : true
};

