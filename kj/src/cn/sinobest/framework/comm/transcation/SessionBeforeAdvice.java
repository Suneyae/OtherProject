 package cn.sinobest.framework.comm.transcation;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import java.lang.annotation.Annotation;
 import java.lang.reflect.Method;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.aop.MethodBeforeAdvice;
 
 public class SessionBeforeAdvice
   implements MethodBeforeAdvice
 {
   private static Logger logger = LoggerFactory.getLogger(SessionBeforeAdvice.class);
   
   public void before(Method method, Object[] args, Object targetObj)
     throws Throwable
   {
     Annotation[] annotation = method.getAnnotations();
     for (Annotation tag : annotation) {
       if (method.isAnnotationPresent(DataSource.class))
       {
         String temp = ((DataSource)tag).name();
         if (temp != null)
         {
           AppContextHolder.setCustomerType(temp);
         }
         else
         {
           logger.error("Annotation @DataSource 未指定name值");
           throw new AppException("Annotation @DataSource 未指定name值", null, null);
         }
       }
     }
   }
 }