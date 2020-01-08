package com.java1234.action;

import com.opensymphony.xwork2.Action;

public class HelloWorldAction implements Action{

	@Override
	public String execute() throws Exception {
		System.out.println("执行了Action的默认方法");
		return SUCCESS;
	}

}
