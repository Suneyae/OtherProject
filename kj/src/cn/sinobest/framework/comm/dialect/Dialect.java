 package cn.sinobest.framework.comm.dialect;
 
 import cn.sinobest.framework.comm.Environment;
 import cn.sinobest.framework.comm.transcation.AppContextHolder;
 
 public class Dialect
 {
   public boolean supportsLimit()
   {
     return false;
   }
   
   public boolean supportsLimitOffset()
   {
     return supportsLimit();
   }
   
   public String getLimitString(String sql, int offset, int limit)
   {
     return getLimitString(sql, offset, Integer.toString(offset), limit, Integer.toString(limit));
   }
   
   public String getLimitString(String sql, int offset, String offsetPlaceholder, int limit, String limitPlaceholder)
   {
     throw new UnsupportedOperationException("paged queries not supported");
   }
   
   public static Dialect getDialect()
     throws InstantiationException, IllegalAccessException, ClassNotFoundException
   {
     return Environment.getDialect(AppContextHolder.getCustomerType());
   }
 }