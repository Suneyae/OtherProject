package com.aop.xmltype;
/**
 * 切面，就是日记类，记录着日记的具体方法
 * @author Wei
 * @time  2017年4月26日 下午2:28:21
 */
public class MyAspectxml {

	public void logBefore() {
		// String className = j.getClass().getName();
		System.out.println("MyAspectxml logBefore(),开始计算...,");
	}

	public void logAfter() {
		System.out.println("已经计算结束...");
	}
	
	public void logAround(){
		System.out.println("我是环绕通知的记录方法logAround()...");
	}
}
