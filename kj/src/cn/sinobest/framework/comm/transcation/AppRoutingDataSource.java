 package cn.sinobest.framework.comm.transcation;
 
 import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
 
 public class AppRoutingDataSource
   extends AbstractRoutingDataSource
 {
   protected Object determineCurrentLookupKey()
   {
     return AppContextHolder.getCustomerType();
   }
 }