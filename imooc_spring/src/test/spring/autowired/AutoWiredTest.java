package test.spring.autowired;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.util.Pub;
/**
 * 自动装配测试类，使用的JUnit4
 * @author Wei
 *
 */
public class AutoWiredTest {
	@SuppressWarnings("all")
	@Test
	public void test(){
		ClassPathXmlApplicationContext ctx = new Pub().getBeanCtx();
		Person p = (Person) ctx.getBean("people");
		p.introduceSelf();
		ctx.close();
	}
	@SuppressWarnings("all")
	public static void main(String[] args) {
		Person p1,p2;
		ClassPathXmlApplicationContext ctx = new Pub().getBeanCtx();
//		p1 = (Person) ctx.getBean("people");
//		p2 = (Person) ctx.getBean("people");
//		System.out.println("p1:"+p1.hashCode()+",\np2:"+p2.hashCode());
//		System.out.println("cat1:"+p1.getCat().hashCode()+",\ncat2:"+p2.getCat().hashCode());
//		p1.introduceSelf();
//		ctx.close();
	}
}
