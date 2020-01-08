package com.wyl;

import com.opensymphony.xwork2.Action;

public class HelloWorldAction implements Action{

	@Override
	public String execute() throws Exception {
		System.out.println("ִ为什么总是404.....");
		return SUCCESS;
	}
	
}
