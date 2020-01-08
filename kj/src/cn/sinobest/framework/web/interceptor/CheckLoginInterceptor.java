 package cn.sinobest.framework.web.interceptor;
 
 import cn.sinobest.framework.comm.iface.IOperator;
 import cn.sinobest.framework.service.json.JSONUtilities;
 import cn.sinobest.framework.util.DTOUtil;
 import cn.sinobest.framework.util.Util;
 import com.opensymphony.xwork2.ActionContext;
 import com.opensymphony.xwork2.ActionInvocation;
 import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
 import com.opensymphony.xwork2.util.ValueStack;
 import java.io.ByteArrayInputStream;
 import java.io.InputStream;
 import java.util.HashMap;
 import java.util.Map;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpSession;
 
 public class CheckLoginInterceptor
   extends AbstractInterceptor
 {
   public String intercept(ActionInvocation invocation)
     throws Exception
   {
     Map<String, String> actionResult = new HashMap();
     HttpServletRequest request = (HttpServletRequest)invocation.getInvocationContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
     
     IOperator user = (IOperator)request.getSession().getAttribute("OPERATOR");
     String isRelogin = "false";
     String inParameters = (String)DTOUtil.getObject("parameters");
     if (!Util.isEmpty(inParameters))
     {
       Object[] paramObj = (Object[])JSONUtilities.parseJSON(inParameters);
       Map[] params = (Map[])null;
       if (paramObj != null)
       {
         params = (Map[])Util.toTypedArray(paramObj, Map.class);
         if ((params != null) && (params.length > 0)) {
           isRelogin = (String)params[0].get("method");
         }
       }
     }
     if ((!"logout".equals(isRelogin)) && (user == null))
     {
       actionResult.put("MSG", "登录超时，请重新登录!");
       actionResult.put("FHZ", "loginTimeout");
       StringBuffer rstString = new JSONUtilities(1).parseObject(actionResult);
       InputStream inputStream = new ByteArrayInputStream(rstString.toString().getBytes());
       invocation.getStack().set("inputName", "inputStream");
       invocation.getStack().set("inputStream", inputStream);
       return "success";
     }
     return invocation.invoke();
   }
 }