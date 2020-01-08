package com.wyl;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 * 
 * @author Wei
 * @time  2016年12月3日 下午1:12:07
 */
@Controller
public class HelloWorld {

	public HelloWorld() {
		System.out.println("----HelloWorld.java 的 构造器-------");
	}

	/**
	 * 映射控制
	 * 使用RequestMapping注解配置需要进行拦截的request，并且提供返回的资源内容
	 * @return String
	 */
	@RequestMapping("/helloworld")
	public String hello() {
		System.out.println("----------");
		//模拟500
		Map<String, Object> map = null;
		map.put("a", "ccc");
		return "success";
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String hello2() {
		System.out.println("----HelloWorld,method=RequestMethod.GET,GET请求------");
		return "success";
	}
	
}
