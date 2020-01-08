package com.aop.annotationtype;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.util.Pub;

public class MyAOPTest {

	ICalculator cal;
	
	
	@Test
	public void testAop() {
		cal = new CalculatorImpl();
		cal.doAdd(13, 99);
	}
	
	
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = Pub.getBeanCtx("beans-auto2.xml");
//		CalculatorImpl cal = (CalculatorImpl) ctx.getBean("calculatorImpl");
		ICalculator ical = (ICalculator)ctx.getBean("calculatorImpl");
		ical.doAdd(13, 99);
	}
}
