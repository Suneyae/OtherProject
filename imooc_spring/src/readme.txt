1 
这是一个关于spring的各种用法的demo
com.aop.xmltype.MyAOPTest
是测试

2
com.aop.springimpl.BeanPostProcessorTest
参考：http://blog.csdn.net/zhu_tianwei/article/details/43348367
用于测试Spring中的钩子接口，BeanPostProcessor，里面包含两个方法，
方法1
public Object postProcessBeforeInitialization(Object bean, String beanName){
	
	return bean;//固定写法，
}

方法2
public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
				
		return bean;//固定写法，
}

