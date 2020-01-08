<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	
	acSuccess.jsp
	
	<br><br>
	${applicationScope.lastName }
	${applicationScope.name }
	<br>
	${sessionScope.age }
	<br>
	rquestScope:
	${requestScope.name_byReq }
	<a href="test.action">Test</a>
</body>
</html>