package com.wyl;

import javax.xml.ws.Endpoint;

public class Test {
	public static void main(String[] args) {
		String addr = "http://192.168.28.2:8898/wyl";
		Endpoint.publish(addr, new MyServiceImpl());
	}
}
