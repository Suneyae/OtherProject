package com.atguigu.spring.beans;

public class HelloWorld {
	private String name;

	public void setName(String name) {
		this.name = name;
	}
	
	public void setXingming(String para){
		this.name = para;
	}

	public void say() {
		System.out.println("name属性值:"+this.name);
	}
	
}
