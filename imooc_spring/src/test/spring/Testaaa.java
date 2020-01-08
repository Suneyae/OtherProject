package test.spring;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Testaaa {

	// public static void test01(){
	// System.out.println("Testaaa test01()...");
	// }

	@BeforeClass
	public static void beforeClass_() {
		System.out.println("Testaaa beforeClass()...");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("Testaaa @AfterClass...");
	}

	@Before
	public void setUp() throws Exception {
		System.out.println("Testaaa @Before...");
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("Testaaa @After...");
	}

	static {
		System.out.println("Testaaa() static{}块...");
	}

	public Testaaa() {
		System.out.println("Testaaa() Construct...");
	}

	@Test
	public void myTest() {
		System.out.println("Testaaa() myTest()方法...");
	}
}
