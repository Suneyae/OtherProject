<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.bjsxt.shopping.util.DB" %>
<%
response.setContentType("text/xml");
response.setHeader("Cache-Control", "no-store"); //HTTP1.1
response.setHeader("Pragma", "no-cache"); //HTTP1.0
response.setDateHeader("Expires", 0); //prevents catching at proxy server
System.out.println("\nweiyl注释：Validate.jsp:"+request.getParameter("id"));
//check the database
DB db = new DB();
Connection conn =  db.getConn();
String sql = "select * from user_ where username = '"+request.getParameter("id")+"'";
PreparedStatement prep = conn.prepareStatement(sql);
ResultSet rst =   prep.executeQuery();
if(rst.next()){
	System.out.print("存在该用户，11111111，不可以注册,sql: "+sql);
	response.getWriter().write("<msg>invalid</msg>");
}else{
	System.out.print("不存在该用户，可以正常22222222,sql:"+sql);
	response.getWriter().write("<msg>valid</msg>");
}
String sql2 = "insert into user_ (id, username) values (employees_seq.nextval,'"+request.getParameter("id")+"a')";
prep = conn.prepareStatement(sql2);
prep.executeUpdate();

rst.close();
prep.close();
conn.close();
%>
