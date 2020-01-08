/**
 * 
 */
package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JUnit4的使用测试Demo
 * @author Wei
 */
public class JUnitFlowTest {

	/**
	 * 1 @BeforeClass 修饰的方法会在所有方法被调用前执行，而且该方法是静态的，
	 * 因此它在内存中只存在一份实例，它比较适合加载配置文件
	 * 当测试类被加载后接着就会执行它。
	 * 2 @AfterClass 修饰的方法通常会用来对资源的清理，比如数据库的关闭
	 * 3 @Before 和  @After 会在每个测试方法的前后各执行一次
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("this is @BeforeClass");
	}

	/**
	 * 修饰的方法通常会用来对资源的清理，比如数据库的关闭
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("this is @AfterClass");
	}

	/**
	 * 会在每个测试方法的前执行一次
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		System.out.println("this is @Before");
	}

	/**
	 * 会在每个测试方法的后执行一次
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		System.out.println("this is @After");
	}
	/**
	 * 测试类1 
	 */
	@Test
	public void test1() {
		System.out.println("JUnitFlowTest.test1()方法...");
	}
	/**
	 * 测试类2
	 */
	@Test
	public void test2() {
		System.out.println("JUnitFlowTest.test2()方法...");
	}
}
