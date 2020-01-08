<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String Scheme = request.getScheme();
	String erverName = request.getServerName();
	String ServerPort = ""+request.getServerPort();
	String path2 = "'"+request.getContextPath()+"'";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript">
	
	console.log('path2:'+<%=path2%>);
	$(function() {
		function Tip() {
			if (window.location) {
				alert(window.location);
			} else {
				alert('window不存在属性location');
			}
		}
		function openLogin(){
			alert('openLogin 函数');
		}
		/**
		   传入金融产品编码
		*/
		function Tip2(currentQybm,jrcpbm){
			var basePath = <%=path2%>;
			//var basePath = "/springmvc011/";
			if (currentQybm) {
				var url = basePath + "work/sygl/xdsq/xdsq0.jsp?CPBM=" + jrcpbm;
				url = basePath;
				window.open(url, "_blank");
			} else {
				openLogin();
			}
		}
		$('#id_btn').click(function() {
			Tip();
		});
		
		$('#id_btn2').click(function() {
			Tip2(1,5);
		});
		
		
	})
</script>
<title>welcome页面</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
</head>

<body>
	<!-- 输出普通字符 -->
	i am the variable
	<%=request.getAttribute("msg")%>
	<br> ------------------- ${msg }
	<br />
	<!-- 输出List -->
	<p>书籍列表</p>
	<c:forEach items="${bookList2}" var="node">
		<c:out value="${node}"></c:out>
	</c:forEach>
	<br />
	<br />

	<!-- 输出Map -->
	<c:forEach items="${map}" var="node">  
        姓名：<c:out value="${node.key}"></c:out>  
        住址：<c:out value="${node.value}"></c:out>
		<br />
	</c:forEach>
	<input type="button" value="点我kankan" id="id_btn" />
	<!-- <input type="button" value="testurl" id="id_btn2" onclick="Tip2(1,4);" /> -->
	<input type="button" value="testurl" id="id_btn2"/>
</body>

</html>
