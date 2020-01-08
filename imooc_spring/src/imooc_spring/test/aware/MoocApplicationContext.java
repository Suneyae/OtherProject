package imooc_spring.test.aware;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;

/**
 * 1、实现了ApplicationContextAware接口，在Bean的实例化时会自动调用setApplicationContext()方法!
   2、通过调用静态方法getBean即可获取,即通过 appcontext.getBean("xxx");的形式来获取bean
   可参考：http://www.cnblogs.com/Dhouse/p/3513705.html
 * @author Wei
 * @time  2016年9月15日 下午3:37:16
 */
public class MoocApplicationContext implements ApplicationContextAware {
	static {
		System.out.println("MoocApplicationContext.class的静态代码库...");
	}
	
	
	public MoocApplicationContext(){
		System.out.println("我是空的MoocApplicationContext构造器....");
	}

	@Override
	public void setApplicationContext(ApplicationContext appcontext) throws BeansException {
//		System.out.println("MoocApplicationContext.setApplicationContext():"+appcontext.getBean("moocAppctx"));
//		System.out.println("MoocApplicationContext.setApplicationContext():"+appcontext.getBean("girl2"));
		System.out.println("MoocApplicationContext类下的：public void setApplicationContext(ApplicationContext appcontext)方法");
		
		boolean flag = appcontext.containsBean("girl");
		String applicationName = appcontext.getApplicationName();
		Environment env = appcontext.getEnvironment();
		System.out.println("包含bean "+"girl:"+flag+",ApplicationName:"+applicationName+",Environment:"+env);
	}

//	public static void main(String[] args) {
//
//		MoocApplicationContext context = new MoocApplicationContext();
//		context.setApplicationContext((ApplicationContext) context);
//	}
	
	public void hhhh(){
		System.out.println("我是Spring配置文件中的init-mothod属性配置的方法hhh()");
	}
}
