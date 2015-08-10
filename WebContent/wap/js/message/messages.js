;
"user strict";

var rainet = rainet || {};
rainet.message = rainet.message || {};

// 信息管理所用模块的视图
rainet.message.view = function(){
	
	var _bindEvent = function() {
		rainet.event.click($('.detail-item', '#list'), function(self){
			_showDetail(self);
		});
		
	}
	
	var _showDetail = function(self) {
		var $current = $('.detail', $(self).parent());
		$('.detail', '#list').not($current).hide();
		var currentSta = $current.css('display');
		var $nextE = $(self).next();
		if (currentSta =='none') {
			$nextE.removeClass('fa-angle-down');
			$nextE.addClass('fa-angle-up');
			$current.show();
		} else {
			$nextE.removeClass('fa-angle-up');
			$nextE.addClass('fa-angle-down');
			$current.hide();
		}
	}
	
	var _loadData = function(module, refresh) {
		 var param  = {pageSize : 10, pageNow : 1}
		 $ul = $('#wrapper ul');
		 if ($ul.data('page')) {
			 param.pageNow = parseInt($ul.data('page'), 10) + 1;
		 }
		 if (refresh) {
			 param.pageNow = 1;
			 $ul.data('page', param.pageNow);
			 $ul.empty();
		 }
		 if (rainet.message.controller[module].updateParam) {
			 rainet.message.controller[module].updateParam(param);
		 }
		 
		 if (rainet.message.controller[module].loadData) {
			 // 自定义 加载数据
			 rainet.message.controller[module].loadData(param, $ul, _bindEvent);
		 } else {
			 	//默认 加载数据方式
		  		rainet.message.service[module].list(param, function(result){
		  			var datas = result.result;
		  			$ul.data('page', result.pageNum);
		  			if (refresh) {
		  				 $ul.empty();
		  			}
		  			if (!datas.length) {
		  				if (result.pageNum == 1) {
		  					$('#noData').show();
		  				} else {
		  					$('#pullUp').css('text-align', 'center');
			  				$('.pullUpLabel', '#pullUp').text('亲，没有数据！');
			  				setTimeout(function(){
			  					$('.pullUpLabel', '#pullUp').text('');
			  				}, 800);
			  				$('#pullUp').removeClass('pullUp');
		  				}
		  				return ;
		  			}
		  			var lis = rainet.message.controller[module].setData(datas);
		  			$ul.append(lis);
		  			_bindEvent();
		  		});
		 }
	}
	
	var module_init = function(){
		var module = $('#module').val();
		if (!rainet.message.controller[module]) {
			module = 'project';
		}
		if (rainet.message.controller[module].disabledPullUp) {
			//$('.pullDownLabel', '#pullUp').text('');
			$('#pullUp').hide();
			//$('#wrapper').css('overflow','visible');
		}
		initScroll(module);
		_loadData(module);
	};
	// 初始化下拉刷新和上拉分页功能
	var initScroll = function(module){
		
		 var myScroll, 
	         pullDownEl, pullDownOffset,
	         pullUpEl, pullUpOffset,
	         _maxScrollY;
	   // 下拉刷新
	   function pullDownAction(){
		   _loadData(module, 'refresh');
		   if(myScroll){
	          myScroll.refresh();
	       }
	   }
	   
	   // 上拉分页--加载更多数据
	   function pullUpAction () {
		   if (!rainet.message.controller[module].disabledPullUp) {
			   _loadData(module);
			}
		   if(myScroll){
	          myScroll.refresh();
	       }
	   }
	   
	   // 初始化 iscroll
		var load_content = function() {
			  pullDownEl = document.querySelector('#pullDown');
		      if (pullDownEl) {
		          pullDownOffset = pullDownEl.offsetHeight;
		      } else {
		         pullDownOffset = 0;
		      }
		      
		     pullUpEl = document.querySelector('#pullUp');    
		      if (pullUpEl) {
		        pullUpOffset = pullUpEl.offsetHeight || 0;
		      } else {
		         pullUpOffset = 0;
		      }
		      
		      myScroll = new iScroll('wrapper',{
		  		topOffset: pullDownOffset,
		  		onRefresh: function () {
		  			if (pullDownEl.className.match('loading')) {
		  				pullDownEl.className = 'pullDown';
		  				pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉刷新';
		  			} else if (pullUpEl.className.match('loading')) {
		  				pullUpEl.className = 'pullUp';
		  				pullUpEl.querySelector('.pullUpLabel').innerHTML = '加载更多';
		  			}
		  		},
		  		onScrollMove: function () {
		  			if (this.y > 5 && !pullDownEl.className.match('flip')) {
		  				pullDownEl.className = 'pullDown flip';
		  				pullDownEl.querySelector('.pullDownLabel').innerHTML = '释放刷新';
		  				this.minScrollY = 0;
		  			} else if (this.distY > 0 && this.y < 5 && pullDownEl.className.match('flip')) {
		  				pullDownEl.className = 'pullDown';
		  				pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉刷新';
		  				this.minScrollY = -pullDownOffset;
		  			} else if (this.distY < 0  && this.y <= (this.maxScrollY - 5) && !pullUpEl.className.match('flip')) {
		  				pullUpEl.className = 'pullUp flip';
		  				pullUpEl.querySelector('.pullUpLabel').innerHTML = '释放刷新';
		  				this.maxScrollY = this.maxScrollY;
		  			} else if (this.distY < 0 &&  this.y > (this.maxScrollY + 5) && pullUpEl.className.match('flip')) {
		  				pullUpEl.className = 'pullUp';
		  				pullUpEl.querySelector('.pullUpLabel').innerHTML = '加载更多';
		  				this.maxScrollY = pullUpOffset;
		  			}
		  		},
		  		onScrollEnd: function () {
		  			if (pullDownEl.className.match('flip')) {
		  				pullDownEl.className = 'pullDown loading';
		  				pullDownEl.querySelector('.pullDownLabel').innerHTML = '加载中...';
		  				// 刷新数据
		  				pullDownAction();
		  			} else if (pullUpEl.className.match('flip')) {
		  				pullUpEl.className = 'pullUp loading';
		  				pullUpEl.querySelector('.pullUpLabel').innerHTML = '加载中...';
		  				// 加载新数据
		  				pullUpAction();
		  			}
		  		}
		      });
		      
		       
		       setTimeout(function () { $('#wrapper').css({left:0}); }, 100);  
		     
		};
		/*if (rainet.message.controller[module].iscroll) {
			document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
			load_content();
		}*/
		document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
		load_content();
	
	};
	
	// 初始化信息管理页面
	var init = function(){
		rainet.mobile.setBodyHeight();
		module_init();
	}
	
	return {
		init : init
	};
	
}();

$(document).ready(function(){
	rainet.header.view.init();
	rainet.message.view.init();
});
