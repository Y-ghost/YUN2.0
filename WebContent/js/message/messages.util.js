;
"user strict";

var rainet = rainet || {};
rainet.message = rainet.message || {};

rainet.message.util = {
		operaterHtml : "<button class=\"btn btn-info edit\" style=\"margin-right: 5px;\" title=\"编辑\"><i class=\"fa fa-pencil-square-o\"></i></button>"+
						"<button class=\"btn btn-danger delete\" title=\"删除\"><i class=\"fa fa-trash-o\"></i></button>",
	 
		formateSeq : function(data, type, full, meta){
			
			var pageInfo = meta.settings.oInstance.fnPagingInfo();
			var pageNow = pageInfo.iPage + 1;
			var pageSize = pageInfo.iLength;
			var index = (pageNow - 1) * pageSize + meta.row + 1;
			return index;
		},

		formateLink : function(data, type, full, meta){
			return "<a class='detail' title="+data+" href='javascipt:;' data-id="+full.id+">"+data+"</a>";
		},
	
		formateDate : function(data){
			return rainet.utils.formateDate(data);
		},

		setSearchForm : function(module){
			var $ele = $('.'+ module, '#searchForm');
			$ele.siblings().css('display', 'none');
			$ele.css('display', 'block');
		}
};
