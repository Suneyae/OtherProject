package imooc_spring.test.anotation;

import org.springframework.stereotype.Controller;

@Controller
public class UserController {
	public void execute(){
		System.out.println("\nUserController.execute()...");
	}
}
