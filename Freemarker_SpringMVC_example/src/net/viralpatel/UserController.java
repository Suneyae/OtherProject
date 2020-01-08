package net.viralpatel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 * 参考自：http://viralpatel.net/blogs/spring-mvc-freemarker-ftl-example/
 * 本地服务启动后访问地址：http://localhost:8080/Freemarker_SpringMVC_example/index.html
 * 
 * @author Wei
 * @time  2016年12月14日 下午4:15:43
 */
@Controller
public class UserController {
	/**
	 * Static list of users to simulate Database
	 */
	private static List<User> userList = new ArrayList<User>();

	//Initialize the list with some data for index screen
	static {
		userList.add(new User("Bill", "Gates"));
		userList.add(new User("Steve", "Jobs"));
		userList.add(new User("Larry", "Page"));
		userList.add(new User("Sergey", "Brin"));
		userList.add(new User("Larry", "Ellison"));
	}

	/**
	 * Saves the static list of users in model and renders it 
	 * via freemarker template.
	 * 
	 * @param model 
	 * @return The index view (FTL)
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(@ModelAttribute("model") ModelMap model) {

		model.addAttribute("userList", userList);

		return "index";
	}

	/**
	 * Add a new user into static user lists and display the 
	 * same into FTL via redirect 
	 * 
	 * @param user
	 * @return Redirect to /index page to display user list
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@ModelAttribute("user") User user) {

		if (null != user && null != user.getFirstname()
				&& null != user.getLastname() && !user.getFirstname().isEmpty()
				&& !user.getLastname().isEmpty()) {

			synchronized (userList) {
				userList.add(user);
			}

		}

		return "redirect:index.html";
	}

}