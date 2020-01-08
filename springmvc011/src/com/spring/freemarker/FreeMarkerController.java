package com.spring.freemarker;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.spring.vo.User;
/**
 * 参见：http://cxl2012.iteye.com/blog/1937006
 * @author Wei
 * @time  2016年12月14日 下午1:54:49
 */
@Controller
@RequestMapping("/home")
public class FreeMarkerController {
	//访问路径：http://localhost:8080/springmvc011/home/index
	@RequestMapping("/index")
	public ModelAndView Add(HttpServletRequest request, HttpServletResponse response) {

		User user = new User();
		User user2 = new User();
		user.setUsername("zhangsan");
		user.setPassword("1234");
		List<User> users = new ArrayList<User>();
		users.add(user);
		user2.setUsername("weiyongle");
		user2.setPassword("43143");
		users.add(user2);
		user2 = null;
		return new ModelAndView("index", "users", users);
	}

}
