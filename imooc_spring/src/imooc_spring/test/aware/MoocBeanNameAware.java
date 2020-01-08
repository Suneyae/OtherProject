package imooc_spring.test.aware;

import org.springframework.beans.factory.BeanNameAware;

public class MoocBeanNameAware implements BeanNameAware{

	/**
	 * 实际上实现了BeanNameAware接口的类，复写
	 * 的setBeanName方法的入参就是这个类在spring容器
	 * 中配置的该类对应的Bean的id,
	 */
	@Override
	public void setBeanName(String arg0) {
		System.out.println("MoocBeanNameAware.setBeanName:"+arg0);
	}

}
