<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en">
<!-- Header -->
<!-- 放到这里 是header里面的head有效，防止IE8时，自动响应为mobile style-->
<jsp:include page="common/header.jsp" />
<head>
<link rel="stylesheet" href="${requestScope.basePath}bootstrap/css/default.css" />
<link rel="stylesheet" href="${requestScope.basePath}bootstrap/css/default.date.css" />
<link rel="stylesheet" href="${requestScope.basePath}bootstrap/css/default.time.css" />
<link rel="stylesheet" href="${requestScope.basePath}multiselects/css/jquery-ui.css" />
<link rel="stylesheet" href="${requestScope.basePath}multiselects/css/jquery.multiselect.css" />
<link rel="stylesheet" href="${requestScope.basePath}multiselects/css/style.css" />
<link rel="stylesheet" href="${requestScope.basePath}multiselects/css/prettify.css" />
<style type="text/css">
*html{ overflow:hidden;}
*body{ height:100%; overflow:auto; margin:0;}
#fd{ position:fixed; *position:absolute;z-index: 9999;top:50%; left:50%; margin:-50px 0 0 -50px;font-size:18px;padding-bottom:10px;padding-top:10px;padding-left:30px;padding-right:30px;background-color: #fff;color:#1b926c;}
</style>
</head>
<body>

<select id ="week" title="Basic example" multiple="multiple" name="week" size="1">
	<option value="1"> 周一</option>
	<option value="2"> 周二</option>
	<option value="3"> 周三</option>
	<option value="4"> 周四</option>
	<option value="5"> 周五</option>
	<option value="6"> 周六</option>
	<option value="7"> 周日</option>
	</select>
 <font color="red" style="margin-top:200px;" id="errRed">sdfasf</font>


<script src="${requestScope.basePath}js/lib/jquery-1.11.1.min.js"></script>
<script src="${requestScope.basePath}bootstrap/js/picker.js"></script>
<script src="${requestScope.basePath}bootstrap/js/picker.date.js"></script>
<script src="${requestScope.basePath}bootstrap/js/picker.time.js"></script>

<script src="${requestScope.basePath}multiselects/js/jquery.ui.core.js"></script>
<script src="${requestScope.basePath}multiselects/js/jquery.ui.widget.js"></script>
<script src="${requestScope.basePath}multiselects/js/jquery.multiselect.js"></script>
<script src="${requestScope.basePath}multiselects/js/prettify.js"></script>
	<script type="text/javascript">
		/* $(".startDate").pickadate({
		    today: '今天',
		    clear: '关闭',
		    selectYears: true,
		    selectMonths: true
			}); */
		$("#timeone").pickatime({
			format: 'H:i',
			clear: '关闭'
		});
		$("#timetwo").pickatime({
			format: 'H:i',
			clear: '关闭'
		});
		
		$(function(){
		    $("select").multiselect({
		        noneSelectedText: "-请选择周期-",
		        checkAllText: "全选",
		        uncheckAllText: '全不选',
		        selectedList:7
		    }, function(){   //回调函数  
		        if($("[name='week']:checked").length > 0)  
		        {  
		            $("#errRed").empty();  
		        }  
		        else  
		        {  
		            $("#errRed").text("请选择");  
		        }  
		    });  
		});
		function showValues() {
		    var valuestr = $("#sela").multiselect("MyValues");
		    alert(valuestr);
		}
	</script>
</body>
</html>
