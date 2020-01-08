package com.imooc.aware;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class MoocBeanName implements BeanNameAware, ApplicationContextAware {

	private String beanName;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		/*
		 * ApplicationContextAware接口的作用
		 * 加载Spring配置文件时，如果Spring配置文件中所定义
		 * 的Bean类实现了ApplicationContextAware
		 * 接口，那么在加载Spring配置文件时，
		 * 会自动调用ApplicationContextAware
		 */
		System.out.println("MoocBeanName.setBeanNamesetApplicationContext() : "
				+ applicationContext.getBean(this.beanName).hashCode());
	}
	
	
	@Override
	public void setBeanName(String name) {
		this.beanName = name;
		System.out.println("MoocBeanName.setBeanName() : " + name);
	}

}
