package com.aop.springimpl;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Service;

/**
 * 参见：http://blog.csdn.net/zhu_tianwei/article/details/43348367
 * @author Wei
 * @time  2017年5月8日 下午2:54:07
 * BeanPostProcessor是BeanFactory的钩子允许用户对新建的Bean进行修改  
 */
@Service
public class BeanPostProcessorTest implements BeanPostProcessor{

	public BeanPostProcessorTest() {
		System.out.println("BeanPostProcessorTest()构造器...");
	}
	
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//		System.out.println("BeanPostProcessorTest.postProcessBeforeInitialization(Object arg0, String arg1)方法:"+arg1);
		System.out.println("postProcessBeforeInitialization:"+beanName);
		
		return bean;//固定写法，
	}
	
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//		System.out.println("postProcessAfterInitialization:"+arg1);
		
		
		return bean;//固定写法，
	}

	
	
	
	
}
