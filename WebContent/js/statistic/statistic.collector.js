;
"user strict";

var rainet = rainet || {};
rainet.statistic = rainet.statistic || {};
rainet.statistic.controller = rainet.statistic.controller || {}; 
var flag = true;
// 主机信息
rainet.statistic.controller.water = {
		// 初始化项目列表
		initProjectList : function(data){
			var $projectList = $('#projectList');
			var str = "";
			pageNum = data.pageNum;
			pages = data.pages;
			if(pages==0){
				pages = 1;
			}
			$.each(data.result,function(index,item){
				var linkActive = "";
				if(index==0){
					linkActive = " link-heading active";
				}else{
					linkActive = "";
				}
				str = str + "<a href='javascript:void(0);' class='list-group-item panelLink "+linkActive+"' id='"+item.id+"' name='"+item.projecttype+"'>"+item.name+"</a>";
			});
			$projectList.empty().append($(str));
			//分页查询
			$(".pagination").jqPagination({
				link_string : "/?page={page_number}",
				current_page: pageNum, //设置当前页 默认为1
				max_page : pages, //设置最大页 默认为1
				page_string : "{current_page} / {max_page}",
				paged : function(page) {
					var param  = { pageSize: 10, pageNow : page };
					rainet.statistic.service["project"].list(param, function(data){
						rainet.statistic.controller.water.initProjectList(data);
					});
				}
			});
			rainet.statistic.controller.water.addFirstProject();
		},
		
		//注册事件
		addFirstProject : function(){
			//首次加载项目第一个的节点信息
			var id = $("#projectList a:first").attr("id");
			if(id!=undefined){
				rainet.statistic.controller.water.initEquipmentList(id);
			}else{
				rainet.utils.notification.warning("您还没有添加项目，请先添加项目!");
			}
			
			$('.panelLink').off('click').on('click', function() {
				rainet.statistic.controller.water.handlMenuView(this);
				var projectId =$(this).attr("id");
				rainet.statistic.controller.water.initEquipmentList(projectId);
			});
		},
		handlMenuView : function(currentEle){
			var $ele = $(currentEle);
			$ele.siblings().removeClass('active');
			$ele.addClass('active');
		},
		//初始化节点列表
		initEquipmentList : function(id){
			var param = {pId : id};
			$("#equipmentList").empty().append("<option value=\"-1\">-全部-</option>");
			rainet.statistic.service["equipment"].list(param, function(data){
				$.each(data,function(index,item){
					$("#equipmentList").append("<option value=\""+item.equipment.id+"\">"+item.equipment.name+"</option>");
				});
				rainet.statistic.controller.water.statisticMethod(id);
			});
		},
		
		//统计
		statisticMethod : function(pId){
			/*************************************** 设置chart参数 ***************************************/
			Highcharts.setOptions({
				global:{
					useUTC:true
				},
				lang:{
					rangeSelectorFrom:'统计日期',
					rangeSelectorTo:'至',
					rangeSelectorZoom:'范围',
					weekdays:["星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"],
					shortMonths:['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']
				}
			});
			//导出
			$(".exportBtn").off('click').on('click', function(){
				var id = $("#equipmentList").val();
				var type = $("input[name='statisticType']:checked").val();
				var startDate = $(".startDate").val()+" 00:00:00";
				var endDate = $(".endDate").val()+" 00:00:00";
				
				if(startDate==" 00:00:00" ){
					rainet.utils.notification.warning('请选择开始日期!');
					return;
				}else if(endDate==" 00:00:00" ){
					rainet.utils.notification.warning('请选择结束日期!');
					return;
				}
				
				var param = {pId:pId,eId:id,startDate:new Date(startDate),endDate:new Date(endDate)};
				if(type==0){
					rainet.statistic.service["statistic"].waterExport(param, function(data){
						window.location = rainet.settings.baseUrl + 'statistic/'+"exportExcel?fileName="+data;
					});
				}else if(type==1){
					rainet.statistic.service["statistic"].humidityExport(param, function(data){
						window.location = rainet.settings.baseUrl + 'statistic/'+"exportExcel?fileName="+data;
					})
				}
			});
			//统计
			$(".statisticBtn").off('click').on('click', function(){
				var id = $("#equipmentList").val();
				var type = $("input[name='statisticType']:checked").val();
				var startDate = $(".startDate").val()+" 00:00:00";
				var endDate = $(".endDate").val()+" 00:00:00";
				
				if(startDate==" 00:00:00" ){
					rainet.utils.notification.warning('请选择开始日期!');
					return;
				}else if(endDate==" 00:00:00" ){
					rainet.utils.notification.warning('请选择结束日期!');
					return;
				}
				
				var param = {pId:pId,eId:id,startDate:new Date(startDate),endDate:new Date(endDate)};
				/*************************************** 统计灌水量 ***************************************/
				
				if(type==0){
					//组装chart数据
					var list = [];
					rainet.statistic.service["statistic"].waterList(param, function(data){
						//遍历节点
						$.each(data,function(index,item){
							var eName = item.equipment.name;
							var listTmp = [];
							//遍历节点状态数据，获取采集的数据
							$.each(item.result,function(i,water){
								var year = new Date(water.createtime).getFullYear();
								var mouth = new Date(water.createtime).getMonth();//Date.UTC函数月要比实际的少1，所以这里不需要加1
								var day = new Date(water.createtime).getDate();
								var value = water.currentvalue/item.equipment.fowparameter;
								var tmp = [Date.UTC(year,mouth,day),value];
								listTmp.push(tmp);
							});
							//组装chart数据单元
							var dataTmp = {
									name : eName,
									data : listTmp,
									tooltip: {
										valueDecimals: 2//保留2位小数位
									}
							};
							list.push(dataTmp);
						});
						//创建chart
						rainet.statistic.controller.water.waterChart(list);
					});
					
				/*************************************** 统计湿度 ***************************************/
				}else if(type==1){
					//组装chart数据
					var list = [];
					rainet.statistic.service["statistic"].humidityList(param, function(data){
							//遍历节点
							$.each(data,function(index,item){
								var eName = item.equipment.name+"传感器"+item.sensor.number;
								var listTmp = [];
								//遍历节点状态数据，获取采集的数据
								$.each(item.result,function(i,humidity){
									var year = new Date(humidity.createtime).getFullYear();
									var mouth = new Date(humidity.createtime).getMonth();//Date.UTC函数月要比实际的少1，所以这里不需要加1
									var day = new Date(humidity.createtime).getDate();
									var value = humidity.humidity;
									
									var tmp = [Date.UTC(year,mouth,day),value];
									listTmp.push(tmp);
								});
								
								//组装chart数据单元
								var data = {
										name : eName,
										data : listTmp,
										tooltip: {
											valueDecimals: 2//保留2位小数位
										}
								};
								list.push(data);
						});
							//创建chart
							rainet.statistic.controller.water.humidityChart(list);
					});
				}
			});
		},
		// 统计节点灌水量
		waterChart : function(data){
			// 创建chart
			$('#tableContainer').highcharts('StockChart', {
				chart: {
					backgroundColor:"#fff",
					borderColor:"#ddd",
					borderRadius:1,
					borderWidth:1,
					height:450,
					spacing:[20, 10, 15, 10]
				},
				colors: ["#7cb5ec", "#aaeeee", "#f7a35c", "#90ee7e", "#7798BF", "#aaeeee", "#ff0066", "#eeaaee",
				         "#55BF3B", "#DF5353", "#7798BF"],
				         rangeSelector : {
				        	 selected : 14,
				        	 inputBoxWidth:90,
				        	 inputDateFormat:'%Y-%m-%d',
				        	 buttons: [{
				        		 type: 'month',
				        		 count: 1,
				        		 text: '1月'
				        	 }, {
				        		 type: 'month',
				        		 count: 3,
				        		 text: '3月'
				        	 }, {
				        		 type: 'month',
				        		 count: 6,
				        		 text: '6月'
				        	 }, {
				        		 type: 'year',
				        		 count: 1,
				        		 text: '1年'
				        	 }, {
				        		 type: 'all',
				        		 text: '全部'
				        	 }]
				         },
				         title : {
				        	 text : '灌溉水量走势图',
				        	 style:{
				        		 color: 'black',
				        		 fontSize: '20px',
				        		 fontWeight:'700'
				        	 }
				         },
				         credits:{
				        	 enabled: true,
				        	 href: "http://www.rainet.com.cn/",
				        	 text: '锐利特科技',
				        	 style: {
				        		    cursor: 'pointer',
				        		    color: '#4cae4c',
				        		    fontSize: '10px'
				        		}
				         },
				         exporting:{
				        	 // 是否允许导出
				        	 enabled:true,
				        	 // 按钮配置
				        	 buttons:{
				        		 contextButton:{
				        			 enabled:false
				        		 },
				        		 // 导出按钮配置
				        		 exportButton:{
				        			 //menuItems: null,
				        			 onclick: function() {
				        				 this.exportChart();
				        			 },
				        			 text:'导出(PDF)',
				        			 symbolFill:"#eee"
				        		 },
				        		 // 打印按钮配置
				        		 printButton:{
				        			 //enabled:true,
				        			 onclick: function() {
				        				 this.print();
				        			 },
				        			 text:'打印报表'
				        		 }
				        	 },
				        	 // 文件名
				        	 filename: encodeURI("灌溉水量走势图"),
				        	 // 导出文件默认类型
				        	 type:'application/pdf'
				         },
				         xAxis: {
				        	 lineColor: '#000',
				        	 tickPixelInterval: 200,//x轴上的间隔  
				        	 dateTimeLabelFormats:{
				        		 second:'%Y-%m-%d',
				        		 minute:'%Y-%m-%d',
				        		 hour:'%Y-%m-%d',
				        		 day:'%Y-%m-%d',
				        		 week:'%Y-%m-%d',
				        		 month:'%Y-%m',
				        		 year:'%Y'
				        	 },
				        	 title :{  
				        		 text:null,
				        		 align:'middle'  
				        	 },  
				        	 type: 'datetime' //定义x轴上日期的显示格式  
				         },
				         yAxis : {
				        	 lineColor: '#000',
				        	 lineWidth: 1,
				        	 tickWidth: 1,
				        	 tickColor: '#000',
				        	 min:0,
				        	 offset:5,
				        	 tickPixelInterval: 30,
				        	 title: {
				        		 text: '灌溉水量 (L)',
				        		 style: { "font-size": "14","color": "#000","font-weight":"bold"}
				        	 },
				        	 labels:{
				        		 // 标签位置
				        		 align: 'left',
				        		 // 标签格式化
				        		 formatter: function(){
				        			 return ''+this.value;
				        		 }
				        	 }
				         },
				         tooltip: {
				        	 xDateFormat:'%Y-%m-%d %A'
				         },
				         series : data
			});
		},
		// 统计节点湿度
		humidityChart : function(data){
			// 创建chart
			$('#tableContainer').highcharts('StockChart', {
				chart: {
					backgroundColor:"#fff",
					borderColor:"#ddd",
					borderRadius:1,
					borderWidth:1,
					height:450,
					spacing:[20, 10, 15, 10]
				},
				colors: ["#7cb5ec", "#aaeeee", "#f7a35c", "#90ee7e", "#7798BF", "#aaeeee", "#ff0066", "#eeaaee",
				         "#55BF3B", "#DF5353", "#7798BF"],
				         rangeSelector : {
				        	 selected : 14,
				        	 inputBoxWidth:90,
				        	 inputDateFormat:'%Y-%m-%d',
				        	 buttons: [{
				        		 type: 'month',
				        		 count: 1,
				        		 text: '1月'
				        	 }, {
				        		 type: 'month',
				        		 count: 3,
				        		 text: '3月'
				        	 }, {
				        		 type: 'month',
				        		 count: 6,
				        		 text: '6月'
				        	 }, {
				        		 type: 'year',
				        		 count: 1,
				        		 text: '1年'
				        	 }, {
				        		 type: 'all',
				        		 text: '全部'
				        	 }]
				         },
				         title : {
				        	 text : '灌区湿度走势图',
				        	 style:{
				        		 color: 'black',
				        		 fontSize: '20px',
				        		 fontWeight:'700'
				        	 }
				         },
				         credits:{
				        	 enabled: true,
				        	 href: "http://www.rainet.com.cn/",
				        	 text: '锐利特科技',
				        	 style: {
				        		    cursor: 'pointer',
				        		    color: '#4cae4c',
				        		    fontSize: '10px'
				        		}
				         },
				         exporting:{
				        	 // 是否允许导出
				        	 enabled:true,
				        	 // 按钮配置
				        	 buttons:{
				        		 contextButton:{
				        			 enabled:false
				        		 },
				        		 // 导出按钮配置
				        		 exportButton:{
				        			 //menuItems: null,
				        			 onclick: function() {
				        				 this.exportChart();
				        			 },
				        			 text:'导出(PDF)'
				        		 },
				        		 // 打印按钮配置
				        		 printButton:{
				        			 //enabled:true,
				        			 onclick: function() {
				        				 this.print();
				        			 },
				        			 text:'打印报表'
				        		 }
				        	 },
				        	 // 文件名
				        	 filename: encodeURI("灌区湿度走势图"),
				        	 // 导出文件默认类型
				        	 type:'application/pdf'
				         },
				         xAxis: {
				        	 lineColor: '#000',
				        	 tickPixelInterval: 200,//x轴上的间隔  
				        	 dateTimeLabelFormats:{
				        		 second:'%Y-%m-%d',
				        		 minute:'%Y-%m-%d',
				        		 hour:'%Y-%m-%d',
				        		 day:'%Y-%m-%d',
				        		 week:'%Y-%m-%d',
				        		 month:'%Y-%m',
				        		 year:'%Y'
				        	 },
				        	 title :{  
				        		 text:null,
				        		 align:'middle'  
				        	 },  
				        	 type: 'datetime' //定义x轴上日期的显示格式  
				         },
				         yAxis : {
				        	 lineColor: '#000',
				        	 lineWidth: 1,
				        	 tickWidth: 1,
				        	 tickColor: '#000',
				        	 min:0,
				        	 offset:5,
				        	 tickPixelInterval: 30,
				        	 title: {
				        		 text: '灌区湿度 (%)',
				        		 style: { "font-size": "14","color": "#000","font-weight":"bold"}
				        	 },
				        	 labels:{
				        		 // 标签位置
				        		 align: 'left',
				        		 // 标签格式化
				        		 formatter: function(){
				        			 return ''+this.value;
				        		 }
				        	 }
				         },
				         tooltip: {
				        	 xDateFormat:'%Y-%m-%d %A'
				         },
				         series : data
			});
		},
		// 搜索节点信息
		init : function(){
			var param  = { pageSize: 10, pageNow : pageNum };
			rainet.statistic.service["project"].list(param, function(data){
				rainet.statistic.controller.water.initProjectList(data);
			});
		}
};
