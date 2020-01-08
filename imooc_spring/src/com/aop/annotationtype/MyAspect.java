package com.aop.annotationtype;

import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
/**
 * 1 把这个类声明为一个切面，也就是 @Aspect ;
 * 2 需要把该类放入到IOC容器中,再声明为一个切面,即 @Component
 * @author Wei
 *
 */
@Aspect
@Component
public class MyAspect {
	
//	@Pointcut
//	@Before("execution (public * *(..))")
//	@Before("execution (* com.aop.annotationtype.*.*(..))")
//	public void logBefore(){
//		System.out.println("MyAspect logBefore()...");
//	}
	
	@Pointcut
	@Before("execution (* com.aop.annotationtype.*.*(..))")
	public void logBefore(Joinpoint jp){
		String className = jp.getClass().getName();
		System.out.println("MyAspect logBefore()..."+" ,类名为："+className);
	}
}
