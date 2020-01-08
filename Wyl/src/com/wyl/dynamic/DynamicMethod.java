package com.wyl.dynamic;

import com.opensymphony.xwork2.ActionSupport;

public class DynamicMethod extends ActionSupport{
	
	
	
	public String save(){
		System.out.println("save()....");
		return SUCCESS;
	}
	
	public String update(){
		ThreadLocal<String> t = new ThreadLocal<>();
		String thrLocal = t.get();
		System.out.println("update()...当前的threadlocal的值："+thrLocal);
		return "shibai";
	}
}
