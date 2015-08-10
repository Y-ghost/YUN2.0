;
"user strict";

var rainet = rainet || {};
rainet.message = rainet.message || {};

// 信息管理所用模块的视图
rainet.message.view = function(){
	var defaultView = 'project';
	var $golabDataTable = null;
	
	var handlMenuView = function(currentEle){
		var $ele = $(currentEle);
		$ele.siblings().removeClass('active');
		$ele.addClass('active');
		
		var module = $ele.attr('data-name');
		setView(module);
	}
	// 根据不同的模块，加载不用的列表信息
	var setView = function(module){
		if ($golabDataTable) {
			// 修复切换模块时，不同table的column个数不同时出现部分column不显示的问题
			$('thead','#table').empty();
			var oSettings = $golabDataTable.fnSettings();
			oSettings.aoData.length = 0;
		}
		$golabDataTable = $('#table').dataTable({
			processing: true,
	        serverSide: true,
			autoWidth: false,
			destroy: true,
			info : true,
			searching : false,
			pageLength : 10,
			language : {
				infoEmpty : '',
				zeroRecords: "<div class='text-center'>无数据</div>",
				info : ' 共_TOTAL_条 ',
				paginate: {
			        next:       "下一页",
			        previous:   "上一页"
			    }
			},
			createdRow : function(row, data, index){
				rainet.message.controller[module].table.row(row, data, index);
			},
			dom : 'tip',
			order: rainet.message.controller[module].table.order,
			columnDefs: rainet.message.controller[module].table.columns,
			ajax : function(data, callback, settings){
				// Get Paging
				var pageNow = settings.oInstance.fnPagingInfo().iPage;
				if (pageNow == 0) {
					pageNow = 1;
				} else {
					pageNow +=1;
				}
				// Get order
				var orderObj = data.order[0];
				var orderColumn = data.columns[orderObj.column].data;
				var orderDir = orderObj.dir;
				
				var criteria = {
						sortField : orderColumn,
						sortDirection  : orderDir
				};
				
				//Update criteria
				if (rainet.message.controller[module].updateParam) {
					rainet.message.controller[module].updateParam(criteria)
				}
				
				var param  = {pageSize : 10, pageNow : pageNow}
				
				
				if (criteria.sortField) {
					param.criteria = JSON.stringify(criteria);
				}
				// Get list
				rainet.message.service[module].list(param, function(data){
					var result = {};
					result.draw = data.pageNum;
					settings.iDraw = data.pageNum;
					result.recordsTotal = data.total;
					result.recordsFiltered = data.total;
					result.data = data.result;
					callback(result);
					bindEventForTable(rainet.message.controller[module]);
				});
			},
			columns : rainet.message.controller[module].table.dataRef
		});
		
		rainet.message.controller[module].table.initEvent($golabDataTable);
	}
	
	// 给table上面的按钮绑定事件
	var bindEventForTable = function(fnConfg){
		$('.edit', '#table').off('click').on('click', function(){
			fnConfg.edit(this, $golabDataTable);
		});
		
		$('.delete', '#table').off('click').on('click', function(){
			fnConfg.del(this, $golabDataTable);
		});
		
		$('.detail', '#table').off('click').on('click', function(){
			fnConfg.detail(this);
		});
		
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
	$("#homeLab").html("信息中心");
	$("#homeHref").attr("href","messages");
	rainet.message.view.init();
});
