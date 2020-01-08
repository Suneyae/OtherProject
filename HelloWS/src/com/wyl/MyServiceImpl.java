package com.wyl;

import javax.jws.WebService;

@WebService(endpointInterface="com.wyl.IMyService")
public class MyServiceImpl implements IMyService{

	@Override
	public int add(int a, int b) {
		int c = a+b;
		System.out.println(a+"+"+b+"="+c);
		return c;
	}

	@Override
	public int minus(int a, int b) {
		int c = a-b;
		System.out.println(a+"-"+b+"="+c);
		return c;
	}
	
}
