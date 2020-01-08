package com.wyl.reflect2;

import java.lang.reflect.Proxy;

public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		RealStar realstar = new RealStar("your father");
		MyInvocation invocation = new MyInvocation(realstar);
		ClassLoader loader = realstar.getClass().getClassLoader();
		Star star = (Star)Proxy.newProxyInstance(loader, realstar.getClass().getInterfaces(), invocation);
		
		star.act("Dragon ball");
	}
}
