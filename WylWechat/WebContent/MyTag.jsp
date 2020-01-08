
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/MyTag.js"></script>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我的自定义标签</title>
</head>

<body>
	<br> ------发送微信请求--------
	<form></form>
	<form
		action="wechat.do?signature=71a42b55fa387d372718c3b70c9e842a8683fad8&timestamp=1476369983&nonce=30917439&openid=oAcGbwKCtLQB-BzNJoJ_HeK22j1g&encrypt_type=aes&msg_signature=3e8c67453dfcb1b8e2f0fc51f6c8cb033d393cbe"
		method="post">
		<!-- 用于测试token -->
		<input type="submit" value="发送微信请求" id="txt_4" />

	</form>
	------------
	<br>
</body>
</html>