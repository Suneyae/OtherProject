 package cn.sinobest.framework.comm;
 
 import java.lang.reflect.InvocationHandler;
 import java.lang.reflect.Method;
 
 public class CommHandler
   implements InvocationHandler
 {
   private Object proxyed;
   
   public CommHandler(Object obj)
   {
     this.proxyed = obj;
   }
   
   public Object invoke(Object proxy, Method methed, Object[] args)
     throws Throwable
   {
     Object result = methed.invoke(this.proxyed, args);
     return result;
   }
 }