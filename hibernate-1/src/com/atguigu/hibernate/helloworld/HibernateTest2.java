package com.atguigu.hibernate.helloworld;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.Test;

public class HibernateTest2 {

	@Test
	public void test() {
		// 1 创建一个SessionFactory 对象
		SessionFactory sessionFactory = null;
		// 1). 创建一个Configuration对象： 对应hibernate的基本配置信息和对象关系映射信息
		/**
		 * Configuration().configure()方法实际上就是 加载hibernate.cfg.xml文件，源码里有写
		 */
		Configuration conficuration = new Configuration().configure();

		// 4.0 之前这样创建 ,现在已经过期了，
		// sessionFactory = configuration.buildSessionFactory();

		// 2). 创建一个 ServiceRegistry 对象: hibernate 4.x 新添加的对象
		// hibernate 的任何配置和服务都需要在该对象中注册后才能有效.
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(conficuration.getProperties())
				.buildServiceRegistry();

		// 3).
		sessionFactory = conficuration.buildSessionFactory(serviceRegistry);

		// 2 创建一个Session对象
		Session session = sessionFactory.openSession();

		// 3 开启事务

		Transaction transaction = session.beginTransaction();

		// 4 执行保存操作
		
//		News news2 = new News("Hibernate", "tom", new Date(), "learning hibernate");
//		
//		session.save(news2);
		News new_get = (News) session.get(News.class, 1);
		new_get.setDescr("20160904新加的描述");
		System.out.println(new_get.toString());
		
		// 5 提交事务
		transaction.commit();

		// 6 关闭Session
		session.close();

		// 7 关闭SessionFactory 对象
		sessionFactory.close();

	}

}
