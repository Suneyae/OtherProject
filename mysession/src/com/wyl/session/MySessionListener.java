package com.wyl.session;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class MySessionListener implements HttpSessionListener{
	private static int NUMBERS_ONLINE = 0;
	public MySessionListener() {
		System.out.println("com.wyl.session.MySessionListener()构造器，");
	}
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		System.out.println("com.wyl.session.sessionCreated()，");
		NUMBERS_ONLINE++;
		System.out.println("在线人数："+NUMBERS_ONLINE);
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		// TODO Auto-generated method stub
		System.out.println("com.wyl.session.sessionDestroyed()，");
		NUMBERS_ONLINE--;
		System.out.println("在线人数："+NUMBERS_ONLINE);
		
	}

}
