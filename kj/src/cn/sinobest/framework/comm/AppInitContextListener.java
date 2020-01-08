 package cn.sinobest.framework.comm;
 
 import cn.sinobest.framework.comm.cache.CacheManager;
 import cn.sinobest.framework.service.longtran.LongProcessService;
 import cn.sinobest.framework.util.ConfUtil;
 import cn.sinobest.framework.util.Util;
 import javax.servlet.ServletContextEvent;
 import javax.servlet.ServletContextListener;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 public class AppInitContextListener
   implements ServletContextListener
 {
   private static final Logger LOGGER = LoggerFactory.getLogger(AppInitContextListener.class);
   
   public void contextDestroyed(ServletContextEvent servletContextEvent)
   {
     CacheManager cacheManager = (CacheManager)Util.getBean(servletContextEvent.getServletContext(), "cacheManager");
     cacheManager.destroy();
   }
   
   public void contextInitialized(ServletContextEvent servletContextEvent)
   {
     CacheManager cacheManager = (CacheManager)Util.getBean(servletContextEvent.getServletContext(), "cacheManager");
     Environment.setCacheManager(cacheManager);
     try
     {
       cacheManager.refreshAll();
       boolean flag = Boolean.getBoolean((String)Util.nvl(ConfUtil.getSysParamOnly("app.timer", "true")));
       if (flag)
       {
         LongProcessService longProcessService = (LongProcessService)Util.getBean("longProcessService", LongProcessService.class);
         longProcessService.initBackJobs(null);
       }
     }
     catch (Exception e)
     {
       LOGGER.error(e.getMessage(), e);
       System.exit(0);
     }
   }
 }