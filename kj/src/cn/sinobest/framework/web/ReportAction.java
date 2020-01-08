 package cn.sinobest.framework.web;
 
 import cn.sinobest.framework.comm.iface.IReportService;
 import cn.sinobest.framework.service.entities.ReportConfig;
 import org.springframework.beans.factory.annotation.Autowired;
 
 public class ReportAction
   extends BaseActionSupport
 {
   @Autowired
   IReportService reportService;
   private static final long serialVersionUID = 1L;
   
   public String execute()
     throws Exception
   {
     String configid = (String)getValue("configId");
     ReportConfig reportConfig = this.reportService.getConfig(configid);
     setValue("reportConfig", reportConfig);
     return super.execute();
   }
 }