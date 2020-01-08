package springmvcfreemarker.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ContentAction {
	/**
	 * 访问地址：http://localhost:8080/SpringmvcFreemarker/SpringmvcFm/login.do
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/login.do", method = RequestMethod.GET)
	public String input(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		modelMap.addAttribute("title", "新年快乐");
		modelMap.addAttribute("content", "你好！祝你新年快乐！");
		
		return "index";
	}
	
}

