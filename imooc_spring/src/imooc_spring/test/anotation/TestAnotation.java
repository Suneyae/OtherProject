package imooc_spring.test.anotation;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.util.Pub;

import imooc_spring.test.anotation.myrepository.DAO;
/**
 * 入口类，用于测试 package imooc_spring.test.anotation 下的类
 * @author Wei
 */
public class TestAnotation {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new Pub().getBeanCtx();
		
//		TestObj obj =  (TestObj) ctx.getBean("testObj");
//		obj.SayHi();
//		
//		UserController ctr = (UserController) ctx.getBean("userController");
//		ctr.execute();
		
//		UserRepositoryImpl user = (UserRepositoryImpl) ctx.getBean("userRepositoryImpl");
//		UserRepositoryImpl user = (UserRepositoryImpl) ctx.getBean("wyl_repo");
//		user.save();
//		
		UserService service = (UserService) ctx.getBean("userService");
//		service.rep.save();
		service.repo.save();
		
//		DAO dao = (DAO)ctx.getBean("wyldao");
//		Pub.print("23*23="+dao.multi(23, 22));
//		Pub.print("xxxxxx");
//		Pub.print("yyyy");
		
	}
}
