package com.webservice.impl;

import javax.xml.ws.Endpoint;

import com.webservice.HelloWorld;

public class Server {
	public static void main(String[] args) {
		System.out.println("webservice start");
		HelloWorld implementor = new HelloWorldImpl();
		String addr = "http://10.162.133.213/helloworld";
		Endpoint.publish(addr, implementor);// jdk实现 暴露webservice接口
//		Endpoint.
		System.out.println("webservice started");

	}
}
