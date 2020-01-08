package com.demo.web.controllers;

import java.sql.SQLException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author Wei
 * @time  2017年4月20日 下午4:56:43
 */
@Controller
@RequestMapping(value = "/helloworld")
public class HelloWorldController extends BaseController {
	/**
	 * 拦截所有请求：
	 * @RequestMapping(value="/*", method = {RequestMethod.GET})
	 * http://localhost:8080/SpringMVCTag/helloworld/xd
	 */
	//@AuthPassport
	@RequestMapping(value="/*", method = {RequestMethod.GET})
    public ModelAndView urlTest(){
		ModelAndView modelAndView = new ModelAndView();   
		//跳转到 urltest.jsp或者其他后缀的页面，这个要看springmvc的配置文件里是怎么设置的，
		modelAndView.setViewName("urltest");  
        return modelAndView;
    }
	/**
	 * 拦截多种请求
	 *  如： @RequestMapping(value={"/index","/hello"})
	 *  访问URL:http://localhost:8080/SpringMVCTag/helloworld/index
	 * @return
	 * @throws SQLException
	 */
	//@AuthPassport
	@RequestMapping(value={"/index","/hello"})
    public ModelAndView index() throws SQLException{
		
		//throw new SQLException("数据库异常。");
		
		ModelAndView modelAndView = new ModelAndView();  
		//在jsp中可以通过 ${message} 的形式来获取绑定的值
		modelAndView.addObject("message", "Hello World!");  
		modelAndView.setViewName("index");  
        return modelAndView;
    }
	/**
	 * @RequestMapping 结合 @PathVariable 注解可以获取URL中带占位的那个参数值，
	 * 具体到这个例子中，在方法名中Integer id这个入参的值就是URL中的值，
	 * 假如URL为：
	 * http://localhost:8080/SpringMVCTag/helloworld/detail/yangdandan
	 * 那么在跳转到的detail.jsp中通过
	 * ${cxrr}的形式就可以获取到 yangdandan 这个字符串
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/detail/{thepara}", method = {RequestMethod.GET})
    public ModelAndView getDetail(@PathVariable(value="thepara") String thepara){
		
		ModelAndView modelAndView = new ModelAndView();  
		modelAndView.addObject("crxx", thepara);  
		modelAndView.setViewName("detail");  
        return modelAndView;
    }
	/**
	 * RequestMapping注解结合正则表达式来进行匹配
	 * 例如：
	 * http://localhost:8080/SpringMVCTag/helloworld/reg/name:wrew-age:41
	 * @param name
	 * @param age
	 * @return
	 */
	@RequestMapping(value="/reg/{name:\\w+}-{age:\\d+}", method = {RequestMethod.GET})
    public ModelAndView regUrlTest(@PathVariable(value="name") String name, @PathVariable(value="age") Integer age){
		
		ModelAndView modelAndView = new ModelAndView();   
		modelAndView.addObject("name", name); 
		modelAndView.addObject("age", age); 
		modelAndView.setViewName("regurltest");  
        return modelAndView;
    }
	/*
	 * RequestMapping 匹配参数为 非某某字符串的 URL，例如：
	 * http://localhost:8080/SpringMVCTag/helloworld/paramstest?example=abdaf
	 * 拦截
	 */
	@RequestMapping(value="/paramstest", params="example!=AAA", method = {RequestMethod.GET})
	public ModelAndView paramsTest(){
		
		ModelAndView modelAndView = new ModelAndView();   
		modelAndView.setViewName("paramstest");  
        return modelAndView;
	}
	
}
