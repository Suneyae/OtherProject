 package cn.sinobest.framework.service;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IDAO;
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.comm.iface.IReportFeedback;
 import cn.sinobest.framework.comm.iface.IReportResultSet;
 import cn.sinobest.framework.comm.iface.IReportResultSet.ReportRS;
 import cn.sinobest.framework.comm.iface.IReportService;
 import cn.sinobest.framework.service.entities.ReportConfig;
 import cn.sinobest.framework.util.ConfUtil;
 import cn.sinobest.framework.util.Util;
 import com.runqian.report4.dataset.DataSet;
 import com.runqian.report4.usermodel.Context;
 import com.runqian.report4.usermodel.CustomDataSetConfig;
 import com.runqian.report4.usermodel.DataSetConfig;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
 
 @Service
 public class ReportService
   implements IReportService
 {
   private static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);
   private static final String KEY_TEMPLATEFILE = "TEMPLATEFILE";
   private static final String KEY_CUSTOMRS = "CUSTOMRS";
   private static final String KEY_FEEDBACK = "FEEDBACKBEAN";
   @Autowired
   IDAO commDAO;
   
   public ReportConfig getConfig(String id)
     throws AppException
   {
     if (Util.isEmpty(id)) {
       throw new AppException("EFW0101", null);
     }
     if (ConfUtil.getPrintConf(id) != null)
     {
       if (LOGGER.isDebugEnabled()) {
         LOGGER.debug("从缓存获取报表配置信息，ID为" + id);
       }
       return (ReportConfig)ConfUtil.getPrintConf(id);
     }
     if (LOGGER.isDebugEnabled()) {
       LOGGER.debug("从数据库获取报表配置信息，ID为" + id);
     }
     Map<String, Object> paramsMap = new HashMap();
     paramsMap.put("ID", id);
     Map<String, Object> rst = this.commDAO.selectOne(
       "FW_CONFIG.FW_REPORT_CONF_Q", paramsMap);
     if (rst == null) {
       throw new AppException("EFW0123", null, new Object[] { id });
     }
     ReportConfig config = new ReportConfig();
     config.setId(id);
     config.setAttr(rst);
     String customBean = (String)Util.nvl(rst.get("CUSTOMRS"));
     String feedBack = (String)Util.nvl(rst.get("FEEDBACKBEAN"));
     String templateFile = (String)Util.nvl(rst.get("TEMPLATEFILE"));
     if (customBean.length() > 0) {
       try
       {
         Class klass = Class.forName(customBean);
         if (!IReportResultSet.class.isAssignableFrom(klass)) {
           throw new AppException("EFW0124", null, new Object[] { customBean, 
             IReportResultSet.class.getName() });
         }
         config.setCustomBean(klass);
       }
       catch (ClassNotFoundException e)
       {
         throw new AppException("EFW0125", e, new Object[] { customBean });
       }
     }
     if (feedBack.length() > 0) {
       try
       {
         Class klass = Class.forName(customBean);
         if (!IReportFeedback.class.isAssignableFrom(klass)) {
           throw new AppException("EFW0124", null, new Object[] { customBean, 
             IReportFeedback.class.getName() });
         }
         config.setFeedbackBean(klass);
       }
       catch (ClassNotFoundException e)
       {
         throw new AppException("EFW0125", e, new Object[] { customBean });
       }
     }
     if (templateFile.length() == 0) {
       throw new AppException("EFW0126", null, new Object[] { id });
     }
     config.setTemplateName(templateFile);
     
     ConfUtil.setPrintConf(id, config);
     return config;
   }
   
   public void afterPrint(IDTO dto)
     throws AppException
   {
     String configId = (String)dto.getValue("configId");
     
     ReportConfig config = getConfig(configId);
     if (config.getFeedbackBean() == null) {
       return;
     }
     if (LOGGER.isDebugEnabled()) {
       LOGGER.debug("触发打印结果反馈，调用自定义类：" + 
         config.getFeedbackBean().getName());
     }
     IReportFeedback feedback = null;
     try
     {
       feedback = (IReportFeedback)config.getFeedbackBean().newInstance();
     }
     catch (AppException e)
     {
       throw e;
     }
     catch (Exception e)
     {
       throw new AppException("EFW0127", e, new Object[] {config.getFeedbackBean()
         .getName() });
     }
     dto.setValue("ReportConfig", config);
     
     feedback.afterPrint(dto);
   }
   
   public DataSet createDataSet(Context ctx, DataSetConfig dsc, boolean arg2)
   {
     String configId = (String)ctx.getParamValue("configId");
     ReportConfig reportConfig = getConfig(configId);
     
     IReportResultSet reportResultSet = null;
     if (reportConfig.getCustomBean() != null) {
       try
       {
         reportResultSet = (IReportResultSet)reportConfig.getCustomBean().newInstance();
       }
       catch (AppException e)
       {
         throw e;
       }
       catch (Exception e)
       {
         throw new AppException("EFW0127", e, new Object[] {reportConfig
           .getCustomBean().getName() });
       }
     }
     if (reportConfig.getCustomBean() == null) {
       return null;
     }
     if (LOGGER.isDebugEnabled()) {
       LOGGER.debug("报表调用自定数据源获取数据，调用自定义类：" + 
         reportConfig.getFeedbackBean().getName());
     }
     CustomDataSetConfig cdsc = (CustomDataSetConfig)dsc;
     String[] args = cdsc.getArgNames();
     String[] vals = cdsc.getArgValue();
     
     Map<String, String> params = new HashMap();
     if (args != null) {
       for (int i = 0; i < args.length; i++) {
         params.put(args[i], vals[i]);
       }
     }
     IReportResultSet.ReportRS rs = reportResultSet.getResultSet(reportConfig, dsc.getName(), 
       params);
     if ((rs.colNames == null) || (rs.colNames.length == 0)) {
       throw new AppException("EFW0128", null, new Object[] { configId, cdsc.getName() });
     }
     int size = rs.datas == null ? 0 : rs.datas.size();
     
     DataSet dataSet = new DataSet(size, rs.colNames.length, 
       dsc.getName());
     for (int i = 0; i < rs.colNames.length; i++) {
       dataSet.addCol(rs.colNames[i]);
     }
     if (rs.datas != null) {
       for (Object[] row : rs.datas)
       {
         Object one = row;
         Object[] rows = dataSet.addRowData();
         System.arraycopy(one, 0, rows, 0, rs.colNames.length);
       }
     }
     return dataSet;
   }
 }