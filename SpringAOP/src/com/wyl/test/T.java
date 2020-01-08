package com.wyl.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wyl.service.StudentService;

/**
 * 测试类
 * 
 * @author Wei
 *
 */
public class T {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans-auto2.xml");
		StudentService studentService = (StudentService) ctx.getBean("studentService");
		studentService.addStudent("张三");

	}

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans-auto2.xml");
		StudentService studentService = (StudentService) ctx.getBean("studentService");
		studentService.addStudent("李四");
	}

}
