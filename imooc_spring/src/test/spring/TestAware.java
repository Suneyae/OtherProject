package test.spring;

import org.junit.Test;

import imooc_spring.test.aware.MoocBeanNameAware;

public class TestAware extends WyljunitBase{
	
	public TestAware(){
//		super.getBean("moocBeanNameAware");
		System.out.println("TestAware构造器...----------------");
	}
	
	
	@Test
	public void testSpring() throws Exception{
		System.out.println("---------------------------------");
//		super.setUpBeforeClass();
//		@SuppressWarnings("static-access")
//		Object obj = super.mObj;
//		MoocApplicationContext appctx = (MoocApplicationContext)obj;
//		System.out.println("TestAware.testSpring(),MoocApplicationContext:"+appctx);
		
		
//		super.setUpBeforeClass();
//		MoocBeanNameAware bean = super.getBean("moocBeanNameAware");
//		System.out.println("TestAware:"+bean);
	}
}
