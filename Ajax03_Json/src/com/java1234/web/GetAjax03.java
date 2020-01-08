package com.java1234.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class GetAjax03 extends HttpServlet{
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("name", "weiyongle");
		jsonObj.put("sex", "nan");
		System.out.println("jsonObj:"+jsonObj);
		
		String userName = request.getParameter("userName");
		System.out.println("userName:"+userName);
		if(userName.equals("weiyongle")){
			jsonObj.put("userName", userName+"已经注册，请修改注册名");
		}else{
			
			jsonObj.put("userName", userName+"可以正常注册");
		}
		out.print(jsonObj);
		System.out.println("jsonObj:"+jsonObj);
		out.close();
	}
}
