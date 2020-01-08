 package cn.sinobest.framework.service.longtran;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.util.ConfUtil;
 import cn.sinobest.framework.util.RepUtil;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 public class LongTransRepExp2Xls
 {
   private static final Logger LOGGER = LoggerFactory.getLogger(RepUtil.class);
   
   public void execute(IDTO dto)
     throws AppException
   {
     String currentServerUid = ConfUtil.getSysParamOnly("EXP2XLS_SERVERUID", "");
     String targetServerUid = ConfUtil.getParam("EXP2XLS_SERVERUID", "");
     if (LOGGER.isDebugEnabled())
     {
       LOGGER.debug("currentServerUid==" + currentServerUid);
       LOGGER.debug("targetServerUid==" + targetServerUid);
     }
     if (("".equals(currentServerUid)) || ("".equals(targetServerUid)) || (!currentServerUid.equals(targetServerUid)))
     {
       if (LOGGER.isDebugEnabled()) {
         LOGGER.debug("非指定服务器，或没有指定执行报表自动导出的服务器，或没有配置当前服务器的UID，不执行该作业。");
       }
       return;
     }
     long startTime = System.currentTimeMillis();
     
     Long logId = (Long)dto.getValue("_logId");
     
     String transId = (String)dto.getValue("_longTransID");
     
     String key = (String)dto.getValue("_longTransKey");
     
     int step = 1;
     JobLogDetail jobLogDetail = new JobLogDetail(logId, key, transId);
     
 
 
 
     jobLogDetail.setSTEP(step++);
     
     jobLogDetail.setSTEPNAME("处理1");
     
     jobLogDetail.setCLZT("1");
     
     jobLogDetail.setBZ("执行中...");
     
     RepUtil.fwReportExp2excel();
     RepUtil.fwFtpUploadExpExcel();
     jobLogDetail.setBZ("完成...");
     
 
 
     jobLogDetail.setBZ("成功");
     long endTime = System.currentTimeMillis();
     LOGGER.debug("【自动导出报表到Excel】完成，耗时: " + (endTime - startTime) + "ms");
   }
 }