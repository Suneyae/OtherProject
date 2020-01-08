package com.java1234.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class AjaxUtil extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("req:"+req.toString()+", resp:"+resp);
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		JSONObject jsonObj = new JSONObject();
		
//		jsonObj.put("key_", );
		out.print(jsonObj);
		
	}
}
