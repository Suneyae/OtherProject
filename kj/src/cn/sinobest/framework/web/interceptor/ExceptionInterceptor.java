 package cn.sinobest.framework.web.interceptor;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IOperator;
 import cn.sinobest.framework.service.CommService;
 import cn.sinobest.framework.util.DTOUtil;
 import cn.sinobest.framework.util.Util;
 import com.opensymphony.xwork2.ActionContext;
 import com.opensymphony.xwork2.ActionInvocation;
 import com.opensymphony.xwork2.ActionProxy;
 import com.opensymphony.xwork2.config.entities.ActionConfig;
 import com.opensymphony.xwork2.config.entities.ExceptionMappingConfig;
 import com.opensymphony.xwork2.interceptor.ExceptionHolder;
 import com.opensymphony.xwork2.interceptor.ExceptionMappingInterceptor;
 import java.io.OutputStream;
 import java.util.List;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 public class ExceptionInterceptor
   extends ExceptionMappingInterceptor
 {
   private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionInterceptor.class);
   private String isAjax;
   
   public String getIsAjax()
   {
     return this.isAjax;
   }
   
   public void setIsAjax(String isAjax)
   {
     this.isAjax = isAjax;
   }
   
   public String intercept(ActionInvocation invocation)
     throws Exception
   {
     String result = "";
     try
     {
       result = invocation.invoke();
     }
     catch (Exception ex)
     {
       try
       {
         LOGGER.error(ex.getMessage(), ex);
         if (isLogEnabled()) {
           handleLogging(ex);
         }
         boolean isAppExpception = false;
         if ((ex instanceof AppException)) {
           isAppExpception = true;
         } else {
           isAppExpception = false;
         }
         filterExcpetion(ex);
         ExceptionHolder exHolder = new ExceptionHolder(ex);
         
         HttpServletRequest request = (HttpServletRequest)invocation.getInvocationContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
         String url = request.getRequestURI();
         String operId = "";
         if (DTOUtil.getUserInfo() != null) {
           operId = DTOUtil.getUserInfo().getOperID();
         }
         CommService commService = (CommService)Util.getBean("commService", CommService.class);
         commService.storeException(url, DTOUtil.getDTO(), operId, ex);
         if ("true".equalsIgnoreCase(this.isAjax))
         {
           OutputStream oOutput = null;
           try
           {
             HttpServletResponse response = (HttpServletResponse)invocation.getInvocationContext().get("com.opensymphony.xwork2.dispatcher.HttpServletResponse");
             response.setContentType("text/plain");
             response.addHeader("Pragma", "no-cache");
             response.addHeader("Cache-Control", "no-cache");
             oOutput = response.getOutputStream();
             StringBuffer respMsg = new StringBuffer();
             respMsg.append("{'MSG':'").append(exHolder.getException().getMessage()).append("'");
             respMsg.append(",'STACK':").append(exHolder.getExceptionStack()).append("'}");
             oOutput.write(respMsg.toString().getBytes());
             oOutput.flush();
           }
           finally
           {
             if (oOutput != null) {
               oOutput.close();
             }
           }
         }
         else
         {
           List<ExceptionMappingConfig> exceptionMappings = invocation.getProxy().getConfig().getExceptionMappings();
           String mappedResult = "";
           
           mappedResult = findResultFromExceptions(exceptionMappings, ex);
           if (mappedResult != null)
           {
             result = mappedResult;
             
 
             publishException(invocation, exHolder);
           }
           else
           {
             throw ex;
           }
         }
       }
       catch (Throwable e)
       {
         LOGGER.error("系统出错，详细:" + e.getMessage(), e);
         publishException(invocation, new ExceptionHolder((Exception)e));
         result = "error";
       }
     }
     LOGGER.debug("result = " + result);
     return result;
   }
   
   private void filterExcpetion(Throwable eh)
   {
     StackTraceElement[] se = eh.getStackTrace();
     StackTraceElement[] se3 = new StackTraceElement[se.length];
     int i = 0;
     String[] exclusion = { "com.opensymphony.xwork2", "org.apache.struts2", "org.springframework" };
     boolean flag;
     for (StackTraceElement seItem : se)
     {
       clsssName = seItem.getClassName();
       flag = true;
       for (String exclStr : exclusion) {
         if (clsssName.indexOf(exclStr) > -1)
         {
           flag = false;
           break;
         }
       }
       if (flag) {
         se3[(i++)] = seItem;
       }
     }
     StackTraceElement[] se4 = new StackTraceElement[i];
     int j = 0;
     String clsssName = (flag = se3).length;
     for (String str1 = 0; str1 < clsssName; str1++)
     {
       StackTraceElement seItem = flag[str1];
       if (seItem != null) {
         se4[(j++)] = seItem;
       }
     }
     eh.setStackTrace(se4);
   }
 }