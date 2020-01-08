package com.demo.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 * 
 * @author Wei
 * @time  2017年4月20日 下午3:56:45
 */
@Controller
@RequestMapping(value = "/account")
public class AccountController {
	/**访问url:
	 * http://localhost:8080/SpringMVCTag/account/login
	 * @return
	 */
	@RequestMapping(value="/login", method = {RequestMethod.GET})
	public String login(){
		
		return "login";
	}
	
}
