package com.util;

public class ExceptionWyl extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String exceptionmsg = "异常:ExceptionWyl自定义异常!";

	public ExceptionWyl() {
		System.out.println(exceptionmsg);
	}

	public ExceptionWyl(String msg) {
		System.out.println(msg);
	}

}
