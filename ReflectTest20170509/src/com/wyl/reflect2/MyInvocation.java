package com.wyl.reflect2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
/**
 * InvocationHandler的实现类，用于代理模式
 * @author Wei
 * @time  2017年5月9日 下午2:06:54
 */
public class MyInvocation implements InvocationHandler{
	Star star;
	public MyInvocation() {
	}
	public MyInvocation(RealStar star) {
		this.star = star;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		System.out.println("开始执行方法:"+method.getName()+args);
		method.invoke(star, args);
		System.out.println("结束执行方法:"+method.getName());
		return null;
	}

}
