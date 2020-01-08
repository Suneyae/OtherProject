 package cn.sinobest.framework.service.tags;
 
 import freemarker.ext.beans.BeansWrapper;
 import freemarker.template.Configuration;
 import freemarker.template.TemplateException;
 import javax.servlet.ServletContext;
 import org.apache.struts2.views.freemarker.FreemarkerManager;
 
 public class FwFreemarkerManager
   extends FreemarkerManager
 {
   protected Configuration createConfiguration(ServletContext servletContext)
     throws TemplateException
   {
     Configuration configuration = super.createConfiguration(servletContext);
     
     configuration.setSharedVariable("tojson", new ToJson());
     configuration.setSharedVariable("enum", BeansWrapper.getDefaultInstance().getEnumModels());
     configuration.setNumberFormat("0.####");
     return configuration;
   }
 }