package com.helloworld.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.helloworld.User;
import com.util.Pub;

/**
 * Test Class
 * 
 * @author Wei
 *
 */
public class Test01 {
	public static void main(String[] args) {
		/**
		 * 1 获取IOC容器
		 */
		ClassPathXmlApplicationContext ctx = new Pub().getBeanCtx();
		/**
		 * 2 获取bean
		 */
		User user = (User) ctx.getBean("girl");
		/**
		 * 3 访问bean的方法
		 */
		user.sayHi();
		System.out.println(
				ctx.hashCode() + ",\n user.myCtx:" + user.getMyCtx().hashCode() + ",IOC容器中该类的id为" + user.getId());// 109961541

		ctx.close();
	}
}
