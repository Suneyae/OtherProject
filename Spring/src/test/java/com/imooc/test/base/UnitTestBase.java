package com.imooc.test.base;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

public class UnitTestBase {
	static {
		System.out.println("-----UnitTestBase static{}------");
	}

	private ClassPathXmlApplicationContext context;

	private String springXmlpath;

	public UnitTestBase() {
	}

	public UnitTestBase(String springXmlpath) {
		System.out.println("-----UnitTestBase Construct(String) ------");
		this.springXmlpath = springXmlpath;
	}

	@BeforeClass
	public static void beforeclass_() {
		System.out.println("-----UnitTestBase @BeforeClass beforeclass_()... ------");

	}

	@Before
	public void before() {
		System.out.println("-----UnitTestBase @Before 开始()... ------");
		if (StringUtils.isEmpty(springXmlpath)) {
			springXmlpath = "classpath*:spring-*.xml";
		}

		try {
			context = new ClassPathXmlApplicationContext(springXmlpath.split("[,\\s]+"));
			context.start();
			System.out.println("-----UnitTestBase @Before 结束()... ------");
		} catch (BeansException e) {
			e.printStackTrace();
		}
	}

	@After
	public void after() {
		System.out.println("-----UnitTestBase @@After ... ------");

		context.destroy();
	}

	@SuppressWarnings("unchecked")
	protected <T extends Object> T getBean(String beanId) {
		try {
			return (T) context.getBean(beanId);
		} catch (BeansException e) {
			e.printStackTrace();
			return null;
		}
	}

	protected <T extends Object> T getBean(Class<T> clazz) {
		try {
			return context.getBean(clazz);
		} catch (BeansException e) {
			e.printStackTrace();
			return null;
		}
	}

}
