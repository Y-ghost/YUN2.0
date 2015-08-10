;
"user strict";

var rainet = rainet || {};
rainet.setting = rainet.setting || {};

// 信息管理所用模块与后台交互的API的URL的配置
rainet.setting.url = {
		project : {
			url : rainet.settings.baseUrl + 'project/'
		},
		host : {
			url : rainet.settings.baseUrl + 'host/'
		},
		node : {
			url : rainet.settings.baseUrl + 'equipment/'
		},
		soil : {
			url : rainet.settings.baseUrl + 'soil/'
		},
		plants : {
			url : rainet.settings.baseUrl + 'plants/'
		}
};

// 设置管理所用模块与后台交互的API
rainet.setting.service = {
		
		project : {
			add: function(param, callback){
				rainet.ajax.execute({
					url : rainet.setting.url.project.url+"save/",
					$busyEle : $('body'),
					data : JSON.stringify(param),
					method : 'POST',
					contentType : 'application/json; charset=utf-8',
					success : function(data){
						callback(data);
					}
				});
			},
			
			validName: function(param, callback){
				rainet.ajax.execute({
					url : rainet.setting.url.project.url + 'validation',
					method : 'GET',
					data : param,
					success : function(data){
						callback(data);
					}
				});
			},
			
			getProjectNames : function(callback){
				rainet.ajax.execute({
					url : rainet.setting.url.project.url + 'names',
					method : 'GET',
					success : function(data){
						callback(data);
					}
				});
			}
		},
		
		host : {
			add: function(param, callback){
				rainet.ajax.execute({
					url : rainet.setting.url.host.url,
					$busyEle : $('body'),
					data : JSON.stringify(param),
					method : 'POST',
					contentType : 'application/json; charset=utf-8',
					success : function(data){
						callback(data);
					}
				});
			},
			validTime: function(param, callback){
				rainet.ajax.execute({
					url : rainet.setting.url.host.url+"validTime/",
					$busyEle : $('body'),
					method : 'GET',
					data : JSON.stringify(param),
					success : function(data){
						callback(data);
					}
				});
			}
		},
		
		equipment : {
			list : function(param, callback) {
				rainet.ajax.execute({
					url : rainet.setting.url.node.url+"selectEquipmentExt/",
					$busyEle : $('body'),
					data : {pId:param.pId},
					method : 'GET',
					customHandleError : function(result){
						if (param.handleError){
							return param.handleError(result);
						}
						return true;
					},
					success : function(data) {
						callback(data);
					}
				});
			},
			getRelData : function(param, callback) {
				rainet.ajax.execute({
					url : rainet.setting.url.node.url+"getRelData/",
					$busyEle : $('body'),
					data : {pId:param.pId},
					method : 'GET',
					customHandleError : function(result){
						if (param.handleError){
							return param.handleError(result);
						}
						return true;
					},
					success : function(data) {
						callback(data);
					}
				});
			},
			selectEquipments : function(param, callback) {
				rainet.ajax.execute({
					url : rainet.setting.url.node.url+"selectEquipments/",
					$busyEle : $('body'),
					data : {pId:param.pId},
					method : 'GET',
					customHandleError : function(result){
						if (param.handleError){
							return param.handleError(result);
						}
						return true;
					},
					success : function(data) {
						callback(data);
					}
				});
			},
			searchEquipment: function(param, callback){
				rainet.ajax.execute({
					url : rainet.setting.url.node.url+"searchEquipment/",
					$busyEle : $('body'),
					data : {pId:param.pId},
					method : 'GET',
					customHandleError : function(result){
						if (param.handleError){
							return param.handleError(result);
						}
						return true;
					},
					success : function(data){
						callback(data);
					}
				});
			},
			add: function(param, callback){
				rainet.ajax.execute({
					url : rainet.setting.url.node.url,
					$busyEle : $('body'),
					data : JSON.stringify(param),
					method : 'POST',
					customHandleError : function(result){
						if (param.handleError){
							return param.handleError(result);
						}
						return true;
					},
					contentType : 'application/json; charset=utf-8',
					success : function(data){
						callback(data);
					}
				});
			},
			updateList: function(param, callback){
				rainet.ajax.execute({
					url : rainet.setting.url.node.url+"updateList/",
					$busyEle : $('body'),
					data : JSON.stringify(param),
					method : 'POST',
					customHandleError : function(result){
						if (param.handleError){
							return param.handleError(result);
						}
						return true;
					},
					contentType : 'application/json; charset=utf-8',
					success : function(data){
						callback(data);
					}
				});
			},
			putData: function(param, callback){
				rainet.ajax.execute({
					url : rainet.setting.url.node.url+"putData/",
					$busyEle : $('body'),
					data : JSON.stringify(param),
					method : 'POST',
					customHandleError : function(result){
						if (param.handleError){
							return param.handleError(result);
						}
						return true;
					},
					contentType : 'application/json; charset=utf-8',
					success : function(data){
						callback(data);
					}
				});
			},
			setListModel: function(param, callback){
				rainet.ajax.execute({
					url : rainet.setting.url.node.url+"setListModel/",
					$busyEle : $('body'),
					data : JSON.stringify(param),
					method : 'POST',
					customHandleError : function(result){
						if (param.handleError){
							return param.handleError(result);
						}
						return true;
					},
					contentType : 'application/json; charset=utf-8',
					success : function(data){
						callback(data);
					}
				});
			},
			setAutoParam: function(param, callback){
				rainet.ajax.execute({
					url : rainet.setting.url.node.url+"setAutoParam/",
					$busyEle : $('body'),
					data : JSON.stringify(param),
					method : 'POST',
					customHandleError : function(result){
						if (param.handleError){
							return param.handleError(result);
						}
						return true;
					},
					contentType : 'application/json; charset=utf-8',
					success : function(data){
						callback(data);
					}
				});
			},
			setTimeLen: function(param, callback){
				rainet.ajax.execute({
					url : rainet.setting.url.node.url+"setTimeLen/",
					$busyEle : $('body'),
					data : JSON.stringify(param),
					method : 'POST',
					customHandleError : function(result){
						if (param.handleError){
							return param.handleError(result);
						}
						return true;
					},
					contentType : 'application/json; charset=utf-8',
					success : function(data){
						callback(data);
					}
				});
			}
		},
		soilInfo : {
			get: function(id, callback){
				rainet.ajax.execute({
					url : rainet.setting.url.soil.url + id,
					$busyEle : $('body'),
					success : function(data){
						callback(data);
					}
				});
			},
			list: function(callback){
				rainet.ajax.execute({
					url : rainet.setting.url.soil.url+"selectSoilInfo/",
					method : 'GET',
					success : function(data){
						callback(data);
					}
				});
			},
			validName: function(param,callback){
				rainet.ajax.execute({
					url : rainet.setting.url.soil.url+"validName/",
					data : param,
					method : 'GET',
					success : function(data){
						callback(data);
					}
				});
			},
			add: function(param, callback){
				rainet.ajax.execute({
					url : rainet.setting.url.soil.url+"save/",
					$busyEle : $('body'),
					data : JSON.stringify(param),
					method : 'POST',
					contentType : 'application/json; charset=utf-8',
					success : function(data){
						callback(data);
					}
				});
			}
		},
		plants : {
			get: function(id, callback){
				rainet.ajax.execute({
					url : rainet.setting.url.plants.url + id,
					$busyEle : $('body'),
					success : function(data){
						callback(data);
					}
				});
			},
			list: function(callback){
				rainet.ajax.execute({
					url : rainet.setting.url.plants.url+"selectPlantsInfo/",
					method : 'GET',
					success : function(data){
						callback(data);
					}
				});
			},
			validName: function(param,callback){
				rainet.ajax.execute({
					url : rainet.setting.url.plants.url+"validName/",
					data : param,
					method : 'GET',
					success : function(data){
						callback(data);
					}
				});
			},
			add: function(param, callback){
				rainet.ajax.execute({
					url : rainet.setting.url.plants.url+"save/",
					$busyEle : $('body'),
					data : JSON.stringify(param),
					method : 'POST',
					contentType : 'application/json; charset=utf-8',
					success : function(data){
						callback(data);
					}
				});
			}
		},
};
