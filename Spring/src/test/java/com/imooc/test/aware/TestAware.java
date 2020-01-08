package com.imooc.test.aware;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.imooc.test.base.UnitTestBase;

@RunWith(BlockJUnit4ClassRunner.class)
public class TestAware extends UnitTestBase {
	static {
		System.out.println("-----TestAware static{}------");
	}

	public TestAware() {
		// 父类构造器
		super("classpath:spring-aware.xml");
		System.out.println("TestAware Construct()...");
	}

	// @Test
	// public void testMoocApplicationContext() {
	// System.out.println("testMoocApplicationContext : " +
	// super.getBean("moocApplicationContext").hashCode());
	// }

	@Test
	public void textMoocBeanName() {
		/*
		 * 使用@Test注解对某个类A里的b方法进行测试的时候，程序的执行顺序依次是
		 * 
		 * 1 静态代码块(如果有的话)
		 * 
		 * 2 A类的构造器里的代码
		 * 
		 * 3 b方法里的代码
		 */
		System.out.println("textMoocBeanName : " + super.getBean("moocBeanName").hashCode());
	}

}
