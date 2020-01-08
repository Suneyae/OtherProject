package com.wyl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 * 
 * @author Wei
 * @time  2016年12月3日 下午3:09:30
 */
@RequestMapping(value="/yy")
@Controller
public class HelloWorld2 {

	public HelloWorld2() {
		System.out.println("----HelloWorld.java 的 构造器-------");
	}

	/**
	 * 1. 使用 @RequestMapping 注解来映射请求的 URL
	 * 2. 返回值会通过视图解析器解析为实际的物理视图, 对于 InternalResourceViewResolver 视图解析器, 会做如下的解析:
	 * 通过 prefix + returnVal + suffix 这样的方式得到实际的物理视图, 然会做转发操作
	 * 
	 * /WEB-INF/views/success.jsp
	 * http://localhost:8080/springmvc011/yy/helloworld
	 * @return
	 */
	@RequestMapping("/helloworld")
	public String hello() {
		System.out.println("----/yy/helloworld2------");
		return "success2";
	}
	
	
//	@RequestMapping(value="/hello2",method = RequestMethod.GET)
	//映射所有/yy/下所有的get请求
	@RequestMapping(method=RequestMethod.GET)
	public String hello2() {
		System.out.println("----hello2,GET请求------");
		return "success";
	}
	
	//映射 http://localhost:8080/springmvc011/yy/wyl?username=999&age=887
	@RequestMapping(value="wyl",params={"username","age!=10"},method=RequestMethod.GET)
	public String hello5() {
		System.out.println("----hello5,GET请求------");
		return "success5";
	}
	
	
	/**
	 * 映射 http://localhost:8080/springmvc011/yy/wyl?daibi=999&age=887
	 * 且请求头  Accept-Language=zh-CN,zh;q=0.8
	 * @return
	 */
	@RequestMapping(value="wyl",params={"daibi","age!=10"},method=RequestMethod.GET,headers={"Accept-Language=text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8","Accept-Encoding=gzip, deflate, sdch"})//可以，因为chrome浏览器支持
//	@RequestMapping(value="wyl",params={"daibi","age!=10"},method=RequestMethod.GET,headers={"Accept-Language=zh-CN,zh;q=0.9"})//这个就不行
	public String hello6() {
		System.out.println("----hello6,GET请求------");
		return "success6";
	}
}
