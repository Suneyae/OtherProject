package com.java1234.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java1234.util.DBUtil;

import net.sf.json.JSONObject;

public class GetSearch extends HttpServlet{
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("utf-8");
		System.out.println("开始了。。。");
		PrintWriter out = response.getWriter();
		JSONObject jsonObj = new JSONObject();
//		jsonObj.put("", value)
		String sousuoValue = request.getParameter("sousuoValue");
		DBUtil util = new DBUtil();
		Connection conn= util.getConnection2();
		PreparedStatement prep = null;
		ResultSet rst = null;
		String aac003 = "";
		try {
			prep = conn.prepareStatement("select aac003 from ac01 where aac002 like '%"+sousuoValue+"%'");
			rst = prep.executeQuery();
			if(rst.next()){
				aac003 = rst.getString("aac003");
				System.out.println("姓名："+aac003);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("获取prep失败");
		}
		jsonObj.put("keyValue", aac003);
		out.print(jsonObj);//向前台传递后台获取到的值
		try {
			rst.close();
			prep.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
