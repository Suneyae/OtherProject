package com.atguigu.springmvc.handlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloWorld {

	/**
	 * 1. 使用 @RequestMapping 注解来映射请求的 URL
	 * 2. 返回值会通过视图解析器解析为实际的物理视图, 对于 InternalResourceViewResolver 视图解析器, 会做如下的解析:
	 * 通过 prefix + returnVal + 后缀 这样的方式得到实际的物理视图, 然会做转发操作
	 * 
	 * /WEB-INF/views/success.jsp
	 * 
	 * @return
	 */
	@RequestMapping("/helloworld")
	public String hello(){
		System.out.println("hello world");
		return "success";
	}
	/**
	 * @PathVariable 注解，作用：绑定URL占位到入参，带占位URL是Spring3.0新增的功能，
	 * 主要用于REST，
	 * 意义：通过 @PathVariable　可以将URL中占位参数绑定到控制器处方法入参中，
	 * 例如以下的例子中：就可以获取到浏览器地址栏中录入的thepara这个占位参数，
	 * 例如：http://localhost:8080/springmvc-1/helloPathVariable/yangdandand?adfaew
	 * @param thepara
	 * @return
	 */
	@RequestMapping("/helloPathVariable/{thepara}")
	public String helloPathViariable(@PathVariable("thepara")String thepara){
		System.out.println("执行方法:"+this.getClass().getName());
		System.out.println("thepara:"+thepara);
		return "success2";
	}
	
	
	@RequestMapping("/helloPathVariable2/{thepara}")
	public String helloPathViariable2(@PathVariable("thepara")String thepara,HttpServletRequest req,HttpServletResponse resp){
		System.out.println("req.getQueryString:"+req.getQueryString());
		System.out.println("执行方法:"+this.getClass().getName());
		System.out.println("thepara:"+thepara);
		return "success2";
	}
	
	
	@RequestMapping("/helloPathVariable3/{thepara}")
	public String helloPathViariable3(@PathVariable("thepara")String thepara,@RequestParam("username")String username, HttpServletRequest req,HttpServletResponse resp){
		System.out.println("req.getQueryString:"+req.getQueryString());
		System.out.println("执行方法:"+this.getClass().getName());
		System.out.println("thepara:"+thepara);
		return "success2";
	}
	
}
