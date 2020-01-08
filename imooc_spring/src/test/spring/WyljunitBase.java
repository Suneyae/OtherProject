package test.spring;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class WyljunitBase {
	static{
		System.out.println("WyljunitBase...静态代码块,");
	}
	
	public WyljunitBase(){
		System.out.println("WyljunitBase...构造器....----------,");
	}
	
	protected static Object mObj;
	protected static ApplicationContext ctx ;
	protected static ClassPathXmlApplicationContext ctx2;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ctx2 = new ClassPathXmlApplicationContext("beans-auto2.xml");
		ctx2.start();
//		mObj = ctx.getBean("moocAppctx");
		System.out.println("WyljunitBase类的@BeforeClass...");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("WyljunitBase类的@AfterClass...");
		ctx2.close();
	}

	@Before
	public void setUp() throws Exception {
		System.out.println("WyljunitBase类的@Before...");
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("WyljunitBase类的@After...");
	}

	public <T extends Object> T getBean(String id){
		ctx2.getBean(id);
		return null;
		
	}
}
