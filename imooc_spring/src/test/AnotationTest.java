package test;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import imooc_spring.test.util.Calculate;

/**
 * 注解Test 将一个普通的方法修饰成一个测试方法
 * 注解Test(expected=XX.class)
 * 注解Test(timeout=毫秒) 
 * 注解BeforeClass:会在所有的方法运行前被执行，static修饰
 * 注解AfterClass: 会在所有的方法运行结束后被执行，static修饰
 * 注解Before: 会在测试方法开始前被执行一次
 * 注解After: 会在测试方法结束后被执行 一次
 * 注解Ignore: 所修饰的测试方法会被测试运行器所忽略
 */
public class AnotationTest {

	@Test(expected = ArithmeticException.class)
	// @Test
	public void testDivide() {
		assertEquals(5, new Calculate().divide(5, 0));
	}

	@Ignore("因为不想执行，所以才加了Ignore这个注解")
	@Test(timeout = 300)
	// @Test
	public void testWhile() {
		int i = 0;
		while (true) {
			i++;
			System.out.println("run forever...,i=" + i);
		}
	}

	@Ignore
	@Test(timeout = 3000)
	public void testReadFille() {
		try {
			Thread.sleep(3100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
