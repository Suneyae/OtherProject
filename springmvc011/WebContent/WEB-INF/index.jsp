<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%-- <%@page isErrorPage="true" %>
<%@ page errorPage="/error.jsp" %> --%>
<%
System.out.println("--------f888888-----");
String aaa = null;
//String path2 = aaa.toString();
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
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
    This is my JSP page. <br>
    I am index.jsp
    <br>
    <a href="helloworld">我是mvc</a>
  </body>
</html>
