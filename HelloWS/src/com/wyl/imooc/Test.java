package com.wyl.imooc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import com.wyl.imooc.Car;
import com.wyl.imooc.Moveable;

public class Test {

	/**
	 * JDK动态代理测试类
	 */
	public static void main(String[] args) {
		Car car = new Car();
		InvocationHandler h = new TimeHandler(car);
		Class<?> cls = car.getClass();
		/**
		 * loader  被代理类的类加载器
		 * interfaces  实现的接口
		 * h InvocationHandler 事件处理器 
		 */
		Moveable m = (Moveable)Proxy.newProxyInstance(cls.getClassLoader(),
												cls.getInterfaces(), h);
		System.out.println("getInterface:");
		m.move();
	}

}
