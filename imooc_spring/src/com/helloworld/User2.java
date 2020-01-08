package com.helloworld;

import org.springframework.context.ApplicationContext;

public class User2 {
	/**
	 * 静态代码块
	 */
	static {
		System.out.println("User2...static{ }");
	}
	
	/**
	 * 代码块
	 */
	{
		System.out.println("User2...{ }");
	}

	public User2() {
		System.out.println("User2 Construct()...");
	}

	private String userName;
	private ApplicationContext myCtx;
	private String id;

	public void setUserName(String userName) {
		this.userName = userName;
		System.out.println("userName:" + this.userName);
	}

	public void setMyCtx(ApplicationContext myCtx) {
		this.myCtx = myCtx;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "User2 [userName=" + userName + ", myCtx=" + myCtx + ", id=" + id + "]";
	}

}
