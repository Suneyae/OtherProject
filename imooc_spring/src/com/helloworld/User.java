/**
 * 一个简单的User Bean,用于测试Spring IOC容器的配置
 */
package com.helloworld;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * User，实现了ApplicationContextAware,BeanNameAware这两个接口，用来作为测试用
 * 
 * @author Wei
 *
 */
public class User implements ApplicationContextAware, BeanNameAware {
	/**
	 * 成员属性userName
	 */
	private String userName;
	/**
	 * 成员属性myCtx,用来接收 public void setApplicationContext(ApplicationContext
	 * applicationContext)传入的Spring上下文
	 */
	private ApplicationContext myCtx;
	/**
	 * 成员属性id
	 */
	private String id;

	public String getUserName() {
		return userName;
	}

	public ApplicationContext getMyCtx() {
		return myCtx;
	}

	public void setMyCtx(ApplicationContext myCtx) {
		this.myCtx = myCtx;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void sayHi() {
		System.out.println(this.userName + "say:Hello!");
	}

	/**
	 * 复写的ApplicationContextAware的setApplicationContext(ApplicationContext
	 * applicationContext)方法，用户获取Spring的上下文
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		System.out.println("User.setApplicationContext(ApplicationContext applicationContext)方法...");
		this.myCtx = applicationContext;
	}

	/**
	 * 复写的BeanNameAware接口的public void setBeanName(String name)方法，用来获取Spring
	 * IOC容器(Spring配置文件)中bean的id
	 */
	@Override
	public void setBeanName(String name) {
		System.out.println("User.setBeanName(String name)方法...");

		this.id = name;
	}

	public String getId() {
		return id;
	}
	
	public void userinit(){
		System.out.println("我是"+User.class.getName()+"的方法 userinit().....");
	}
}
