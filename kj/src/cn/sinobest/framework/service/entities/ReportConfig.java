 package cn.sinobest.framework.service.entities;
 
 import cn.sinobest.framework.comm.iface.IReportFeedback;
 import cn.sinobest.framework.comm.iface.IReportResultSet;
 import java.io.Serializable;
 import java.util.Map;
 
 public class ReportConfig
   implements Serializable
 {
   private static final long serialVersionUID = 1L;
   String id;
   String templateName;
   Class<? extends IReportResultSet> customBean;
   Class<? extends IReportFeedback> feedbackBean;
   private Map<String, Object> attr = null;
   
   public Map<String, Object> getAttr()
   {
     return this.attr;
   }
   
   public void setAttr(Map<String, Object> attr)
   {
     this.attr = attr;
   }
   
   public String getTemplateName()
   {
     return this.templateName;
   }
   
   public void setTemplateName(String templateName)
   {
     this.templateName = templateName;
   }
   
   public Class<? extends IReportResultSet> getCustomBean()
   {
     return this.customBean;
   }
   
   public void setCustomBean(Class<? extends IReportResultSet> customBean)
   {
     this.customBean = customBean;
   }
   
   public Class<? extends IReportFeedback> getFeedbackBean()
   {
     return this.feedbackBean;
   }
   
   public void setFeedbackBean(Class<? extends IReportFeedback> feedbackBean)
   {
     this.feedbackBean = feedbackBean;
   }
   
   public String getId()
   {
     return this.id;
   }
   
   public void setId(String id)
   {
     this.id = id;
   }
 }