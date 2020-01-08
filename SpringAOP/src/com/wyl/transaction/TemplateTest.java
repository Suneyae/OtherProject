package com.wyl.transaction;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TemplateTest {
	static ClassPathXmlApplicationContext ctx;
	{
		ctx = new ClassPathXmlApplicationContext("beans-auto2.xml");
	}
	
	public TemplateTest() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
//		ctx.gette
	}
}
