 package cn.sinobest.framework.comm.transcation;
 
 import java.lang.annotation.Annotation;
 import java.lang.reflect.Method;
 import org.springframework.aop.AfterReturningAdvice;
 import org.springframework.aop.ThrowsAdvice;
 
 public class SessionAfterAdvice
   implements AfterReturningAdvice, ThrowsAdvice
 {
   public void afterReturning(Object arg0, Method method, Object[] arg2, Object arg3)
     throws Throwable
   {
     clear(method);
   }
   
   public void afterThrowing(Method method, Object[] args, Object target, RuntimeException throwable)
   {
     clear(method);
   }
   
   private void clear(Method method)
   {
     Annotation[] annotation = method.getAnnotations();
     for (Annotation tag : annotation) {
       if ((method.isAnnotationPresent(DataSource.class)) && 
         (((DataSource)tag).name() != null)) {
         AppContextHolder.clearCustomerType();
       }
     }
   }
 }