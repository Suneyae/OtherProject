package com.atguigu.hibernate.helloworld;

import java.util.Date;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HibernateTest3 {
	private Configuration cfg;
	private ServiceRegistry serviceRegistry;
	private SessionFactory sessionFactory;
	private Session session;
	Transaction transaction;

	@Before
	public void init() {
		// 1 加载hibernate.cfg.xml配置文件
		this.cfg = new Configuration().configure();

		// 实例化serviceRegistry,用于注册hibernate的所有配置和服务
		this.serviceRegistry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();

		// 实例化sessionfactory
		this.sessionFactory = cfg.buildSessionFactory(serviceRegistry);

		// 获取hibernate session对象，用于操作数据库

		this.session = sessionFactory.openSession();

		transaction = session.beginTransaction();
	}

	@After
	public void after() {
		transaction.commit();
		session.close();
		sessionFactory.close();
	}

	@Test
	public void test() {
		News news = (News) session.get(News.class, 2);
		news.setDescr("我是描述，20160904更改");
	}

	@Test
	public void testAdd() {
		News news = new News("woshibiaoti", "qingdou", new Date(), "i AM THE DESCIRIBTION");
		session.save(news);
	}

	@Test
	public void testAddCat() {
		Cat cat = new Cat("SAXOR", "female");
		cat.setName("xxxx");
		boolean flag = cat instanceof HibernateProxy;
		boolean flag_PersistentCollection = cat instanceof PersistentCollection;
		System.out.println("cat instanceof HibernateProxy :" + flag);
		System.out.println("cat instanceof PersistentCollection :" + flag_PersistentCollection);
		System.out.println("hibernate.isInitialized?" + Hibernate.isInitialized(cat));
		session.save(cat);
		cat.setName("uuopfwefopie");
	}

	@Test
	public void testDynamic() {
		// Cat cat = new Cat("UIUOFSAF", "female");
		Cat cat = (Cat) session.get(Cat.class, 9);
		cat.setSex("不是女");
		session.save(cat);
	}

	@Test
	public void testIncrement() throws InterruptedException {
		Cat cat = new Cat("mimiao", "madam");
		session.save(cat);
		Thread.sleep(9000);
	}

	@Test
	public void testSequence() throws InterruptedException {
		Cat cat = new Cat("hellokitty2", "madam");
		session.save(cat);
		cat.setSex("sir");
	}

	@Test
	public void testMany2One() {
		Cat cat = new Cat("大花猫","female");
		session.save(cat);
		Food food = new Food(cat,"猫粮A",99);
		session.save(food);
	}
	
	@Test
	public void testOne2many(){
		
	}

}
