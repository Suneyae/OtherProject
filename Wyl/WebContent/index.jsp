<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.7.1.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/my.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/myAJAX.js"></script>
<script>
$(function() {
	var len = $("input").length;
	console.log('input元素的个数：'+len);
	if(window.innerHeight){
		posX = window.pageXOffset;
		posY = window.pageYOffset;
		//console.log(posX+','+posY);
		console.log('支持window.innerHeigth啊');
	}else {
		//console.log('不支持window.pageXOffset');
		console.log('不支持window.innerHeigth啊');
	}
	var $obj = $('input :eq(1)');
	//alert('length:'+$obj.length);
	//$.Walert('weiyongel');
	//$.WsetTime('clockmy',true);
	//$.WsetGap(1000);
	//window.setInterval(''+$.WsetTime("clockmy",true)+'',1000);
	//window.setInterval('console.log("aa")',1000);
	//$.WsetTime('clockmy');
	
	//$("#divResult").load("data/AjaxGetCityInfo.aspx", { "resultType": "html" });
	//测试，使用ajax提交请求，第一个请求为action，在struts.xml中可以找到对应的动作
	$("#divResult").load("tosubmit2", { "resultType": "html" },function(){
		console.log('成功获取到了action为tosubmit2的请求的数据');
	});
	$("#divResult2").load("dynamicMethod!update", { "resultType": "html" });
	
	//深度拷贝
	var item1 = {"name":"wyl"};
	var item2 = {"addr":"xinghuaCity"};
	var item3 = $.extend(true,{},item1,item2);
	console.log('深度拷贝的内容：'+JSON.stringify(item3));
	
	//new WylAjax(),myAJAX.js中自定义个的一个function，封装了ajax
	var myAjax = new WylAjax();
	/* myAjax.ajaxSend("dynamicMethod"); */
	/*
		自己写的这个ajaxSend()方法会在http://localhost:8080/Wyl/位置A,如果传入的
		参数m,那么位置A就是m,
		例如：new WylAjax().ajaxSend("dynamicMethod");
	那么浏览器会自动访问：http://localhost:8080/Wyl/dynamicMethod ,
	*/
	myAjax.ajaxSend("helloWorld.jsp");   //能够成功执行，
	/* myAjax.ajaxSend("../www"); *///404 ，找不到对应的资源
	//
	var a = 'd';
	var b = '';
	$.logW(typeof a);
	$.logW(typeof (a||{}));
	
	/**
		json字符串的循环
		
	*/
	var jsonA = {name:'wyl',sex:'male',attr:'xinghuaCity',height:173};
	var len = getJsonLen(jsonA);
	$.logW(JSON.stringify(jsonA)+'.length:'+len);
	$.logW(JSON.stringify(jsonA)+'.length,传入的是json字符串:'+getJsonLen(''));
	//$.logW('-------:'+JSON.parse('wyl'));//会报错
	//$.logW(JSON.stringify(jsonA)+'.length,传入的是json字符串2:'+getJsonLen('wyl'));
	
	//测试获取json的key值
	$.logW(JSON.stringify(jsonA)+'.value:'+typeof getJsonValue(jsonA));
	
	//测试js数组
	//var arr = [45,'we',33,'jiangsu'];
	//var arr = [45,98,33,23];
	var arr = ['we','wyl','jiangsu'];
	alert(typeof arr);	// typeof arr的返回值是"object"
	var arrStr = arr+'';//  arrStr = "we,wyl,jiangsu"
	$.logW(arrStr);
	$.logW('00000000：'+eval("['yongle',3,'haha']"));
	$.logW('00000000：'+eval('('+"['yongle',3,'haha']"+')'));
	//$.logW('11111111'+eval("we,ha,gd"));//报错
	$.logW('00000000：'+eval("alert('ddd')"));
	//var arr2 = eval('('+arrStr+')'); //会报错
	$.logW('数组2的typeof值：'+typeof atrr2);
	//var objA = JSON.parse(jsonA);
	$.logW(jsonA.__defineGetter__.length);
	var jsonStr = JSON.stringify(jsonA);
	var objA = JSON.parse(jsonStr);
	$.logW('objA:'+objA);
	
	var k = 23;
	$.logW('k:'+k);
	
	return;//对以下的js代码进行阻断
	new WylAjax().sendAjax('正在异步加载', function() {
		console.log('成功。。。');
	}, function() {
		console.log('失败了。。。');
	}, false);
})
	
	/* var len = $("input").length;
	alert(len); */
</script>
</head>
<body>

	欢迎你：${UserName }
	<a href="javascript:window.close();">关闭窗口</a>
	<br>
	时间1：<input type="text" id="clockmy" size='30'/>
	<!-- <button onclick="myStop(clockmy_)">停止</button> -->
	<button id="clockmy_" onclick="myStop(this)">停止</button>
	<button onclick="myStart()">开始</button>
	<form action="tosubmit" method="post" class="yongle">
		账号：<input type="text" name="mingzi" ><br>
		密码：<input type="password" name="mima"><br> 
		<input type="submit" value="登陆">
	</form>
	
	<a href="dynamicMethod.do" class="yongle">转到dynamicMethod.jsp</a>
	<a href="dynamicMethod">也是转到dynamicMethod.jsp</a>
	<a href="dynamicMethod.action">哈哈，也是转到dynamicMethod.jsp</a>
	 <br>
	 <!-- 用来盛放ajax请求获取到的数据 -->
	<div id="divResult"></div>
	<br>
	<br>
	------我是分隔线-----
	<!-- 用来盛放ajax请求获取到的数据 -->
	<div id="divResult2"></div>
	<br>
	<input value="后面会自动加载从系统返回来的html数据"><div id="div_last"></div>
	
</body>
<script>
	window.setInterval(function(){
		var time = new Date().toLocaleTimeString();
		$('#clockmy3').attr("value","直接用function,"+time.toString());
	}, 1000);
	//gei id=clockmy的input设置时间
	window.clockmy_ = $.myClock('clockmy',true,1000,'');
	var contextPath = '${ctx}';//封装后的ajax用得到，项目中是放在head.jsp中的
	
	function myStop(timer){
		var tarId = timer.id;
		/* var obj = window.tarId; */
		//var obj = window[tarId];
		
		//转换为对象：
		var obj= eval('('+ tarId +')');
		
		window.clearInterval(obj);
		
		var obj2 = window[tarId];
		var objjj = 321;
	};
	
	
	function myStart(){
		var d = new Date();
		var t = d.toLocaleTimeString();
		t= d.toDateString();//Sat Jan 30 2016
		t=d.toTimeString();//
		
	}
	
	
</script>
</html>