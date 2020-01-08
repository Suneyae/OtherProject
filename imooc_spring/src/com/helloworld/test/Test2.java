package com.helloworld.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.helloworld.User;
import com.util.Pub;

/**
 * Test Class
 * 测试实现了ApplicationContextAware以及BeanNameAware这两个接口的类
 * @author Wei
 * @time  2017年3月28日 下午2:34:46
 */
public class Test2 {
	public static void main(String[] args) {
		/**
		 * 1 get IOC container,just the Configure XML file
		 */
		ClassPathXmlApplicationContext ctx = new Pub().getBeanCtx();
		/**
		 * 2 get bean Object
		 */
		User user = (User) ctx.getBean("girl");
		/**
		 * 3 access to bean Object
		 */
		user.sayHi();
		System.out.println(
				ctx.hashCode() + ",\n user.myCtx:" + user.getMyCtx().hashCode() + ",the Class id of IOC Container " + user.getId());// 109961541

		ctx.close();
	}
}
