package com.aop.xmltype2;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.util.Pub;

/**
 * 主要是用来测试XML方式配置AOP的前置通知等，配置切面切点等
 * 
 * @author Wei
 *
 */
public class MyAOPTest {

	ICalculatorxml cal;

	@Test
	public void testAop() {
		cal = new CalculatorImplxml();
		cal.doAdd(13, 99);
	}

	public static void main(String[] args) {

		// ICalculatorxml cal = (ICalculatorxml)
		// Pub.getBeanCtx().getBean("calImplxml");
		// System.out.println("-----------------分割符号----------\n\n\n\n");
		// int rtn = cal.doAdd(13, 99);

		ApplicationContext ctx = Pub.getBeanCtx("beans-auto5.xml");
		ICalculatorxml cal = (ICalculatorxml) ctx.getBean("calculatorImplxml2");
		cal.doAbs(433, 422);

	}

	// 第三个
	@Test
	public void Test3() {
		ApplicationContext ctx = Pub.getBeanCtx("beans-auto3.xml");
		ICalculatorxml cal = (ICalculatorxml) ctx.getBean("calculatorImplxml");
		// Diary diary = (Diary) ctx.getBean("diary");
		int rtn = cal.doMul(34, 44);
		System.out.println("34*44=" + rtn);

	}

	@Test
	public void Test4() {
		ApplicationContext ctx = Pub.getBeanCtx("beans-auto4.xml");
		ICalculatorxml cal = (ICalculatorxml) ctx.getBean("calculatorImplxml");
		cal.doAbs(433, 422);
	}


}
