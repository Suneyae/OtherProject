 package cn.sinobest.framework.service.webservice;
 
 import org.apache.axis2.context.MessageContext;
 import org.apache.axis2.context.ServiceGroupContext;
 
 public class WebServiceContext
 {
   public static Object getContextProperty(String key)
   {
     MessageContext mc = MessageContext.getCurrentMessageContext();
     ServiceGroupContext sc = mc.getServiceGroupContext();
     return sc.getProperty(key);
   }
   
   public static void setContextProperty(String key, Object value)
   {
     MessageContext mc = MessageContext.getCurrentMessageContext();
     ServiceGroupContext sc = mc.getServiceGroupContext();
     sc.setProperty(key, value);
   }
 }