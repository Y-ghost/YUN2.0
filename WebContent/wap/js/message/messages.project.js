;
"user strict";

var rainet = rainet || {};
rainet.message = rainet.message || {};
rainet.message.controller = rainet.message.controller || {}; 

// 项目信息
rainet.message.controller.project = {
	
  setData : function(datas) {
	  var result = '';
	  $.each(datas, function(i, data){
			/*var li = '<li><a class="detail-item">'+data.name+'</a><i class="pull-right fa fa-angle-down"></i><div class="detail" style="display:none;">'+
			'<div><label>项目单位:</label><div class="detail-item-c">'+data.department+'</div></div><div><label>项目地址:</label><div class="detail-item-c">'+data.address+'</div></div>'+
		     '<div><label>创建时间:</label><div class="detail-item-c">'+rainet.message.util.formateDate(data.createtime)+'</div></div></div></li>';*/
			var li = '<li><a class="project-item" href="messages?type=node&pId='+data.id+'">'+data.name+'</a>';
			if (data.wifiStatus == "在线") {
				li += '<i class="fa fa-wifi text-success"></i>';
			} else {
				li += '<i class="fa fa-exclamation-triangle text-danger"></i>';
			}
			li += '<i class="pull-right fa fa-angle-right"></i></li>';
			result +=li;
	 });
	  
	 return result;
  },
  iscroll : true
};

