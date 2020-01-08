 package cn.sinobest.framework.web.context;
 
 import cn.sinobest.framework.comm.Environment;
 import java.util.Enumeration;
 import java.util.Properties;
 import javax.servlet.ServletContext;
 import javax.servlet.ServletContextEvent;
 import javax.servlet.ServletContextListener;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.beans.factory.DisposableBean;
 import org.springframework.web.context.ConfigurableWebApplicationContext;
 import org.springframework.web.context.ContextLoader;
 
 public class ContextLoaderListener
   extends ContextLoader
   implements ServletContextListener
 {
   private ContextLoader contextLoader;
   private static final Logger LOGGER = LoggerFactory.getLogger(ContextLoaderListener.class);
   public static final String PRODUCT_CONFIG_LOCATION_PARAM = "contextConfigLocationProduct";
   
   public void contextInitialized(ServletContextEvent event)
   {
     this.contextLoader = createContextLoader();
     if (this.contextLoader == null) {
       this.contextLoader = this;
     }
     this.contextLoader.initWebApplicationContext(event.getServletContext());
   }
   
   protected void customizeContext(ServletContext servletContext, ConfigurableWebApplicationContext applicationContext)
   {
     super.customizeContext(servletContext, applicationContext);
     String val = Environment.getProperties().getProperty("app.productionMode");
     if ("true".equalsIgnoreCase(val))
     {
       LOGGER.info("app.productionMode为" + val + ",框架将以产品模式运行!");
       applicationContext.setConfigLocation(servletContext.getInitParameter("contextConfigLocationProduct"));
     }
     else
     {
       LOGGER.info("app.productionMode为" + val + ",框架将以开发模式运行!要以产模式运行请设置为true.");
     }
   }
   
   @Deprecated
   protected ContextLoader createContextLoader()
   {
     return null;
   }
   
   @Deprecated
   public ContextLoader getContextLoader()
   {
     return this.contextLoader;
   }
   
   public void contextDestroyed(ServletContextEvent event)
   {
     if (this.contextLoader != null) {
       this.contextLoader.closeWebApplicationContext(event.getServletContext());
     }
     ServletContext sc = event.getServletContext();
     Enumeration attrNames = sc.getAttributeNames();
     while (attrNames.hasMoreElements())
     {
       String attrName = (String)attrNames.nextElement();
       if (attrName.startsWith("org.springframework."))
       {
         Object attrValue = sc.getAttribute(attrName);
         if ((attrValue instanceof DisposableBean)) {
           try
           {
             ((DisposableBean)attrValue).destroy();
           }
           catch (Throwable ex)
           {
             LOGGER.error("Couldn't invoke destroy method of attribute with name '" + attrName + "'", ex);
           }
         }
       }
     }
   }
 }