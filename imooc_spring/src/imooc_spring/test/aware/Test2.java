package imooc_spring.test.aware;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test2 {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ApplicationContext ctx = null;
		ctx = new ClassPathXmlApplicationContext("beans-auto2.xml");
		System.out.println("Test.main(),ctx:"+ctx);
		
		/*
		 * MoocApplicationContext.class的静态代码库...
		 * MoocApplicationContext.setApplicationContext()
		 * 我是Spring配置文件中的init-mothod属性配置的方法hhh()
		 */
		// MoocApplicationContext ctx = new MoocApplicationContext();
		/*
		 * MoocApplicationContext.class的静态代码库...
		 */
	}
}
