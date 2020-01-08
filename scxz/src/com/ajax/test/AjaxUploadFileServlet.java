package com.ajax.test;

import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import com.sun.corba.se.spi.orbutil.fsm.State;

public class AjaxUploadFileServlet {
	/**
	 * 返回结果函数
	 * @param response
	 * @param state
	 */
//	private void responseMessage(HttpServletResponse response, State state) {
//		response.setCharacterEncoding("UFT-8");
//		response.setContentType("text/html; charset=" + "UFT-8");
//		Writer writer = null;
//		try {
//			writer = response.getWriter();
//			writer.write("{\"code\":" + state. +",\"message\":\"" + state.getMessage()+ "\"}");
//			writer.flush();
//			writer.close();
//		} catch(Exception e) {
//			logger.error(e.getMessage(), e);
//		} finally {
//			IOUtils.closeQuietly(writer);
//		}
//	}
}
