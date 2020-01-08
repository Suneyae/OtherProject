<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<s:debug>调试</s:debug>
	<%= request %>
	<br>
	rq:${rq }
	<br>
	querystring:${querystring }
	attrNames:${attrNames }
	<br>
	<br>
	attrNames_:${attrNames_ }
	<br>
	<br>
	欢迎你：${mingzi } 成功登陆，这是chenggong.jsp
</body>
</html>