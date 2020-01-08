<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
	window.onload = function() {
		document.getElementsByTagName('a')[0].onclick = function() {
			var req = new XMLHttpRequest();
			var url = this.href + "?time=" + new Date().getTime();
			var method = "GET";
			req.open(method, url);
			req.send(null);
			req.onreadystatechange = function() {
				if (req.readyState == 4) {
					if (req.status == 200 || req.status == 304) {
						var str = req.responseText;
						document.getElementsByTagName("a")[1].innerHTML=str;
					}
				}
			}
			return false;
		}
	}
</script>
</head>
<body>
	<a href="hh.txt">txt</a>
	<a href="hh.html">html</a>
</body>
</html>