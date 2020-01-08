package com.wyl;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class ViewIPTAG extends TagSupport{
	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
		String user = request.getRemoteUser();
		String host = request.getRemoteHost();
		int port = request.getRemotePort();
		
		
		JspWriter out = this.pageContext.getOut();
		try {
			out.print(request.getRemoteHost()+",端口号"+request.getRemotePort());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.doStartTag();
	}
}
