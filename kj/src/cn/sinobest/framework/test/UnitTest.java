 package cn.sinobest.framework.test;
 
 import cn.sinobest.framework.comm.Environment;
 import cn.sinobest.framework.comm.cache.CacheManager;
 import cn.sinobest.framework.comm.iface.IDAO;
 import cn.sinobest.framework.util.PropertiesUtil;
 import java.io.IOException;
 import java.io.InputStream;
 import java.util.Properties;
 import org.junit.Before;
 import org.junit.Test;
 import org.junit.runner.RunWith;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.test.context.ContextConfiguration;
 import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
 import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
 import org.springframework.test.context.transaction.TransactionConfiguration;
 
 @RunWith(SpringJUnit4ClassRunner.class)
 @ContextConfiguration(locations={"classpath*:springContext.xml", "classpath*:appContext.xml"})
 @TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
 public abstract class UnitTest
   extends AbstractJUnit4SpringContextTests
 {
   private static final String JDBC_PROPERTIES = "/sysconfig.properties";
   @Autowired
   IDAO commDAO;
   
   @Before
   public void init()
     throws Exception
   {
     CacheManager cacheManager = new CacheManager(getProperties());
     cacheManager.setDao(this.commDAO);
     Environment.setCacheManager(cacheManager);
     cacheManager.refreshAll();
     Environment.setApplicationContext(this.applicationContext);
   }
   
   private static Properties getProperties()
   {
     cacheProper = new Properties();
     try
     {
       InputStream stream = PropertiesUtil.getResourceAsStream("/sysconfig.properties");
       try
       {
         cacheProper.load(stream);
       }
       catch (IOException e)
       {
         e.printStackTrace();
         try
         {
           stream.close();
         }
         catch (IOException e)
         {
           e.printStackTrace();
         }
       }
       finally
       {
         try
         {
           stream.close();
         }
         catch (IOException e)
         {
           e.printStackTrace();
         }
       }
       try
       {
         stream.close();
       }
       catch (IOException e)
       {
         e.printStackTrace();
       }
       return cacheProper;
     }
     catch (Exception ex)
     {
       ex.printStackTrace();
     }
   }
   
   @Test
   public void doTest()
     throws Exception
   {}
 }