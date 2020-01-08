package com.java1234.util;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 作用：加载IOC容器的公用类，实际上就是加载Spring配置文件
 * 
 * @author Wei
 *
 */
public class Pub {

	private static String NEWlINE = "\n";
	private static ClassPathXmlApplicationContext ctx;

	/**
	 * Spring配置文件的名称，也可以说是路径或者说是 IOC容器配置文件,默认值："beans-auto2.xml"
	 */
	private static String BeanPath = "beans-auto2.xml";

	/**
	 * 作用：获取Spring配置文件，返回上下文对象,即ClassPathXmlApplicationContext对象
	 * 
	 * @return: ClassPathXmlApplicationContext对象
	 */
	public static ClassPathXmlApplicationContext getBeanCtx() {
		ctx = new ClassPathXmlApplicationContext(BeanPath);
		return ctx;
	}

	/**
	 * 根据xmlName返回 ClassPathXmlApplicationContext ,可以强转为ApplicationContext
	 * 如果xmlName为空，那么默认返回xmlName为 "beans-auto2.xml"
	 * 的ClassPathXmlApplicationContext
	 * 
	 * @param xmlName
	 * @return ClassPathXmlApplicationContext
	 */
	public static ClassPathXmlApplicationContext getBeanCtx(String xmlName) {
		/**
		 * 如果xmlName不为空，那么
		 */
		if (!CharUtil.isNull(xmlName)) {
			ctx = new ClassPathXmlApplicationContext(xmlName);
			return ctx;
		} else {
			return getBeanCtx();
		}

	}

	/**
	 * 作用：自动换行打印
	 * 
	 * @param msg
	 */
	public static void printN(String msg) {
		System.out.println(NEWlINE + msg);
	}

	/**
	 * 
	 * @param msg
	 * @return \nmsg,
	 */
	public static String newMsg(String msg) {
		return NEWlINE + msg;
	}
}
