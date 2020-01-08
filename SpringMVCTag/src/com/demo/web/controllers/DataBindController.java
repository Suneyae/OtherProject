package com.demo.web.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.demo.web.models.AccountModel;
/**
 * 实际上这个web是 20161214号建的
 * @author Wei
 * @time  2017年4月20日 下午2:15:48
 */
@Controller
@RequestMapping(value = "/databind")
@SessionAttributes(value = "sessionaccountmodel")
public class DataBindController {
	/*
	 * http://localhost:8080/SpringMVCTag/databind/parambind
	 * 参考：http://www.cnblogs.com/caoyc/p/5635914.html
	 */
	@RequestMapping(value="/parambind", method = {RequestMethod.GET})
    public ModelAndView paramBind(Map<String,Object> map){
		Map map2 = map;
		map.put("names", "Suneyae");
		map.put("age",28);//这种方式，在parambind.jsp中可以通过 request中的age:${requestScope.age } 这样的形式获取到放在map中的数据
		ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("parambind");  
        return modelAndView;
    }
	/**
	 * @RequestParam  这个注解，可以自动绑定表单以及URL中的数据，
	 * 直接在controller控制器的方法中获取到。
	 * 访问地址：http://localhost:8080/SpringMVCTag/databind/parambind
	 * @param request
	 * @param urlParam
	 * @param formParam
	 * @param formFile
	 * @return
	 */
	@RequestMapping(value="/parambind", method = {RequestMethod.POST})
    public ModelAndView paramBind(HttpServletRequest request, @RequestParam("urlParam") String urlParam, @RequestParam("formParam") String formParam, @RequestParam("formFile") MultipartFile formFile){
		
		//如果不用注解自动绑定，我们还可以像下面一样手动获取数据
		String urlParam1 = ServletRequestUtils.getStringParameter(request, "urlParam", null);
		String formParam1 = ServletRequestUtils.getStringParameter(request, "formParam", null);
        MultipartFile formFile1 = ((MultipartHttpServletRequest) request).getFile("formFile"); 
		
		ModelAndView modelAndView = new ModelAndView();  
		modelAndView.addObject("urlParam", urlParam);  
		modelAndView.addObject("formParam", formParam);  
		modelAndView.addObject("formFileName", formFile.getOriginalFilename());  
		
		modelAndView.addObject("urlParam1", urlParam1);  
		modelAndView.addObject("formParam1", formParam1);  
		modelAndView.addObject("formFileName1", formFile1.getOriginalFilename());  
		modelAndView.setViewName("parambindresult");  
        return modelAndView;
    }
	/*
	 * http://localhost:8080/SpringMVCTag/databind/modelautobind
	 * 注意这里是用的get方法
	 */
	@RequestMapping(value="/modelautobind", method = {RequestMethod.GET})
	public String modelAutoBind(HttpServletRequest request, Model model){
		AccountModel account = new AccountModel();
		model.addAttribute("accountmodel", account);
		return "modelautobind";
	}
	
	/*@RequestMapping(value="/modelautobind", method = {RequestMethod.POST})
	public String modelAutoBind(HttpServletRequest request, Model model, AccountModel accountModel){
		
		model.addAttribute("accountmodel", accountModel);
		return "modelautobindresult";
	}*/
	/*访问URL：
	 * http://localhost:8080/SpringMVCTag/databind/modelautobind
	 * 这里用的是POST请求
	 */
	@RequestMapping(value="/modelautobind", method = {RequestMethod.POST})
	public String modelAutoBind(HttpServletRequest request, @ModelAttribute("accountmodel") AccountModel accountModel){
		
		return "modelautobindresult";
	}
	
	//@CookieValue Test
	@RequestMapping(value="/cookiebind", method = {RequestMethod.GET})
    public String cookieBind(HttpServletRequest request, Model model, @CookieValue(value="JSESSIONID", defaultValue="") String jsessionId){
		model.addAttribute("jsessionId", jsessionId);
        return "cookiebindresult";
    }
	
	//@RequestHeader Test
	@RequestMapping(value="/requestheaderbind", method = {RequestMethod.GET})
    public String requestHeaderBind(HttpServletRequest request, Model model, @RequestHeader(value="User-Agent", defaultValue="") String userAgent){
		
		model.addAttribute("userAgent", userAgent);
        return "requestheaderbindresult";
    }
	/*
	 * 在方法前加上 @ModelAttribute 注解，那么在 springmvc 调用任何目标方法之前都会调用
	 * 拥有 @ModelAttribute 注解的方法，
	 * 参见：http://www.cnblogs.com/solverpeng/p/5753033.html
	 * 
	 */
	//@SessionAttributes Test
	@ModelAttribute("sessionaccountmodel")
	public AccountModel initAccountModel(){
		
		return new AccountModel();
	}
	
	@RequestMapping(value="/usernamebind", method = {RequestMethod.GET})
	public String userNameBind( Model model, AccountModel accountModel){
		
		model.addAttribute("sessionaccountmodel", new AccountModel());
		return "usernamebind";
	}
	
	@RequestMapping(value="/usernamebind", method = {RequestMethod.POST})
	public String userNameBindPost( @ModelAttribute("sessionaccountmodel") AccountModel accountModel){
		
		//重定向到密码绑定测试
		return "redirect:passwordbind";
	}
	
	@RequestMapping(value="/passwordbind", method = {RequestMethod.GET})
	public String passwordBind(@ModelAttribute("sessionaccountmodel") AccountModel accountModel){
		
		return "passwordbind";
	}
	
	@RequestMapping(value="/passwordbind", method = {RequestMethod.POST})
	public String passwordBindPost(@ModelAttribute("sessionaccountmodel") AccountModel accountModel, SessionStatus status){
		
		//销毁@SessionAttributes中value指定名称的数据
		status.setComplete();
		//显示绑定结果
		return "sessionmodelbindresult";
	}
	
	//@RequestBody Test
	@RequestMapping(value="/requestbodybind", method = {RequestMethod.GET})
    public String requestBodyBind(Model model){
		
		model.addAttribute("accountmodel", new AccountModel());
        return "requestbodybind";
    }
	
	@RequestMapping(value="/requestbodybind", method = {RequestMethod.POST})
    public @ResponseBody AccountModel requestBodyBind(@RequestBody AccountModel accountModel){
				
		return accountModel;
    }
		
}