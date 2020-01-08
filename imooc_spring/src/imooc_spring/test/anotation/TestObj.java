package imooc_spring.test.anotation;

import org.springframework.stereotype.Component;
/**
 * Component 注解
 * @author Wei
 *
 */
@Component
public class TestObj {
	public void SayHi(){
		System.out.println("\nHi this is TestObj.SayHi()...");
	}
	
	
}
