 package cn.sinobest.framework.comm.transcation;
 
 import org.apache.log4j.Logger;
 import org.springframework.util.Assert;
 
 public class AppContextHolder
 {
   private static final Logger logger = Logger.getLogger(AppContextHolder.class);
   private static final ThreadLocal<String> contextHolder = new ThreadLocal();
   
   public static void setCustomerType(String dsType)
   {
     Assert.notNull(dsType, "dsType cannot be null");
     logger.debug("change datasource into " + dsType);
     contextHolder.set(dsType);
   }
   
   public static String getCustomerType()
   {
     return contextHolder.get() == null ? "ds" : (String)contextHolder.get();
   }
   
   public static void clearCustomerType()
   {
     logger.debug("exit datasource " + getCustomerType());
     contextHolder.remove();
   }
 }