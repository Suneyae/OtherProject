package com.wyl;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 用于接收龙泉安卓项目的报文请求并做出响应
 * @author Wei
 * @time  2017年4月22日 上午8:59:32
 */
public class AndroidServlet extends HttpServlet{
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		System.out.println("AndroidServlet.init()方法...");
	}
	
	/*@Override
	public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
		System.out.println("AndroidServlet.service(ServletRequest arg0, ServletResponse arg1)方法...");
	}
	
	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		System.out.println("AndroidServlet.service(HttpServletRequest arg0, HttpServletResponse arg1)方法...");
	}*/

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String flag = "";

	public AndroidServlet() {
		System.out.println("AndroidServlet()构造器...");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		flag = "GET";
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(!"GET".equals(flag)){
			flag = "POST";
		}
		String queryStr = req.getQueryString();
		queryStr = java.net.URLDecoder.decode(queryStr, "UTF-8");
		System.out.println("queryStr:"+queryStr);
		resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-type", "text/html;charset=UTF-8"); 
		PrintWriter out = resp.getWriter();
		flag = "".equals(flag)?"空":flag;
		out.write(flag+"请求,您录入的参数是:"+"\n"+queryStr);
		
	}
	
	@Override
	public void destroy() {
		System.out.println("AndroidServlet.destroy()方法...");
	}
}
