;
"user strict";

var rainet = rainet || {};
rainet.message = rainet.message || {};
rainet.message.controller = rainet.message.controller || {}; 

// 报警信息
rainet.message.controller.systemLog = {
		
		formateLogType : function(data){
			if (data == 0) {
   				return "采集异常";
   			}
   			if (data == 1) {
   				return "湿度报警";
   			}
		},
		setOperateHtml : function(data){
			return "<button class=\"btn btn-info edit\">标记已读</button>";
//			if (data == "未读") {
//   				return "<button class=\"btn btn-info edit\">标记已读</button>";
//   			}
		},
		
		// 从后台获取数据之后，设置对应报警信息的值，以弹出框的形式展示
		setLogInfo : function(data, readonly, $dataTable){
			var $logHtml = $(this.infoTemplate).attr('id', 'logInfo'+data.id);
			$('.type',$logHtml).empty().append('<option>'+this.formateLogType(data.logtype)+'</option>');
			$(".context", $logHtml).val(data.logcontext);
			$(".status", $logHtml).val(data.logstatus);
			$(".logTime", $logHtml).val(rainet.utils.formateDate(data.logtime));
			
			if (readonly) {
				$("input", $logHtml).attr('disabled', true);
				$("select", $logHtml).attr('disabled', true);
			}
			
			var $form = $("form", $logHtml);
			$('button[data-bb-handler=cancel]', $form).off('click').on('click', function(){
				bootbox.hideAll();
			});
			
			bootbox.dialog({
				message : $logHtml,
				title : '报警信息',
				// 支持ESC
				onEscape : function(){
					
				}
			});
		},
	table : {
	  columns : [
		           	{ "sTitle":'序号', "targets": 0, "orderable": false, "render" : rainet.message.util.formateSeq },
		           	{ "sTitle": "消息标题",  "targets": 1, "orderable": false, "render" : rainet.message.util.formateLink },
		           	{ "sTitle": "消息类型", "targets": 2, "render" : function(data){
		           			return rainet.message.controller.systemLog.formateLogType(data);
		           		}
		           	},
		           	{ "sTitle": "接受时间",  "targets": 3, "render" : rainet.message.util.formateDate },
		           	{ "sTitle": "消息状态", "targets": 4 },
		           	{ "sTitle": "操作",   "targets": 5, "orderable": false, "data": null, "render": function(data){
		           			return rainet.message.controller.systemLog.setOperateHtml(data);
		           		}
		           	}
		],
		order : [4, 'desc'],
		
		dataRef : [
		           {'data':'id'},
		           {'data':'logcontext'},
		           {'data':'logtype'},
		           {'data':'logtime'},
		           {'data':'logstatus'},
		],
		
		initEvent : function($datatable){
			rainet.message.util.setSearchForm('log');
		},
		
		row : function(row, data, index){
			$(row).attr('id', data.id);
		}
	},
	
	// 获取单个报警的详细信息
	detail : function(self){
		var logId = $(self).attr('data-id');
		var _this = this;
		rainet.message.service.systemLog.get(logId, function(data){
			_this.setLogInfo(data, true);
		});
	},
	// 更新单个节点信息
	edit : function(self, $dataTable){
		var logId = $(self).parent().parent().attr('id');
		rainet.message.service.systemLog.markLogRead({logId : logId}, function(data){
			if (data) {
				bootbox.hideAll();
				rainet.utils.notification.success('标记成功');
				$dataTable.api().ajax.reload();
			}
		});
	},
	
	infoTemplate : "<div>\n"+
		"<form class=\"form-horizontal\" role=\"form\" onsubmit=\"return false;\">\n"+
			"<div class=\"form-group\">\n"+
				"<label class=\"col-sm-3 control-label\">类型：</label>\n"+
				"<div class=\"col-sm-9\">\n"+
    				"<select class=\"form-control type\" name=\"type\"></select>\n"+
    			"</div>\n"+
    		"</div>\n"+
    		"<div class=\"form-group\">\n"+
    			"<label class=\"col-sm-3 control-label\">内容：</label>\n"+
    			"<div class=\"col-sm-9\">\n"+
    				"<textarea class=\"form-control context\" name=\"context\"/>\n"+
    			"</div>\n"+
    		"</div>\n"+
    		"<div class=\"form-group\">\n"+
    		"<label class=\"col-sm-3 control-label\">状态：</label>\n"+
    		"<div class=\"col-sm-9\">\n"+
    		"<input type=\"text\" class=\"form-control status\" name=\"status\"/>\n"+
    		"</div>\n"+
    		"</div>\n"+
    		"<div class=\"form-group\">\n"+
				"<label class=\"col-sm-3 control-label\">时间：</label>\n"+
				"<div class=\"col-sm-9\">\n"+
					"<input type=\"text\" class=\"form-control logTime\" name=\"logTime\"/>\n"+
				"</div>\n"+
			"</div>\n"+
  			 "<div class=\"dialog-footer\">\n"+
				"<button data-bb-handler=\"cancel\" type=\"button\" class=\"btn btn-warning\">取消</button>\n"+
		     "</div>\n"+
		"</form>\n"+
	"</div>\n",
	
	
};

