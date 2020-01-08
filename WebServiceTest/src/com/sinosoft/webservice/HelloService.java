package com.sinosoft.webservice;

public class HelloService {
	/**
	 * 不带参数的函数
	 * 
	 * @return 返回Hello字符串
	 */
	public String sayHello() {
		return "Hello";
	}

	/**
	 * 带参数的函数
	 * 
	 * @param name
	 *            名称
	 * @return 返回加上名称的欢迎词
	 */
	public String sayHelloToPerson(String name) {
		if (name == null || name.equals("")) {
			name = "nobody";
		}
		return "Hello " + name;
	}
	
	public String sayHello2(String name) {
		if (name == null || name.equals("")) {
			name = "nobody";
		}else if(name.indexOf("Japan")!=-1){
			name = "你输入的参数含有非法字符!";
		}
		return "Hello " + name;
	}
}
