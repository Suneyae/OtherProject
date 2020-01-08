package com.aop.xmltype2;
/**
 * 切面，也就是日志类
 * @author Wei
 * @time  2017年3月28日 下午1:32:57
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
