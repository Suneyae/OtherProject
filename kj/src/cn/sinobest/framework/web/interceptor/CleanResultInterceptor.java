 package cn.sinobest.framework.web.interceptor;
 
 import com.opensymphony.xwork2.ActionInvocation;
 import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
 
 public class CleanResultInterceptor
   extends MethodFilterInterceptor
 {
   private static final long serialVersionUID = 1L;
   
   protected String doIntercept(ActionInvocation invocation)
     throws Exception
   {
     String result = null;
     Object action;
     try
     {
       result = invocation.invoke();
     }
     finally
     {
       Object action = invocation.getAction();
       if ((action instanceof Cleanable)) {
         try
         {
           ((Cleanable)action).cleanDo();
         }
         catch (Exception e)
         {
           throw e;
         }
       }
     }
     return result;
   }
 }