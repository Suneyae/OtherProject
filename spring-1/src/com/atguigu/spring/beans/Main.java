package com.atguigu.spring.beans;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	public static void main(String[] args) {

		ApplicationContext ctx = new ClassPathXmlApplicationContext("mybeans.xml");
		HelloWorld hw = (HelloWorld) ctx.getBean("myHelloWorld");
		hw.say();
	}
}
