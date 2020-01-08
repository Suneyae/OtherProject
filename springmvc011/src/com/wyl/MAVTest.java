package com.wyl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * ModelAndView测试，
 * 
 * @author Wei
 * @time 2016年12月4日 上午10:12:16
 */
@Controller
public class MAVTest {

	public MAVTest() {
		// TODO Auto-generated constructor stub
	}
	// http://localhost:8080/springmvc011/login
	@RequestMapping(value = "login")
	public ModelAndView login() {
		System.out.println("MAVTest.java login()....");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("welcome");
		mav.addObject("msg", "hello kitty");

		// List
		List<String> list = new ArrayList<String>();
		list.add("java");
		list.add("c++");
		list.add("oracle");
		mav.addObject("bookList", list);

		// Map
		Map<String, String> map = new HashMap<String, String>();
		map.put("zhangsan", "北京");
		map.put("lisi", "上海");
		map.put("wangwu", "深圳");
		mav.addObject("map", map);

		return mav;
	}

}
