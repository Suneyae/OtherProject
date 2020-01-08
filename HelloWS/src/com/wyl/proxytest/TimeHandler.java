package com.wyl.proxytest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TimeHandler implements InvocationHandler {
	public TimeHandler(Object target) {
		super();
		this.target = target;
	}

	private Object target;

	/**
	 * Parameters:proxy the proxy instance that the method was invoked onmethod
	 * the Method instance corresponding to the interface method invoked on the
	 * proxy instance. The declaring class of the Method object will be the
	 * interface that the method was declared in, which may be a superinterface
	 * of the proxy interface that the proxy class inherits the method
	 * through.args an array of objects containing the values of the arguments
	 * passed in the method invocation on the proxy instance, or null if
	 * interface method takes no arguments. Arguments of primitive types are
	 * wrapped in instances of the appropriate primitive wrapper class, such as
	 * java.lang.Integer or java.lang.Boolean.
	 */
	/**
	 * 参数说明： proxy 被代理对象 method 被代理对象的方法 args 方法的参数
	 * 
	 * 返回值：
	 * 
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		long starttime = System.currentTimeMillis();
		System.out.println("汽车开始行驶....");
		method.invoke(target);
		long endtime = System.currentTimeMillis();
		System.out.println("汽车结束行驶....  汽车行驶时间：" + (endtime - starttime) + "毫秒！");
		return null;
	}

}
