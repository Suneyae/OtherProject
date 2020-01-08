 package cn.sinobest.framework.service.longtran;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.util.DateUtil.CurDate;
 import cn.sinobest.framework.util.Util;
 import java.util.HashMap;
 import java.util.Map;
 import org.quartz.JobDataMap;
 import org.quartz.JobDetail;
 import org.quartz.JobExecutionContext;
 import org.quartz.JobExecutionException;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 public class ProcedureJob
   implements IJob
 {
   private static final Logger LOGGER = LoggerFactory.getLogger(LongProcessService.class);
   
   public void execute(JobExecutionContext context)
     throws JobExecutionException, AppException
   {
     JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
     
     IDTO dto = (IDTO)jobDataMap.get("dto");
     Map<String, Object> argsMap = new HashMap(3);
     JobLogger jobLogger = JobLogger.getInstance();
     JobLog jobLog = new JobLog();
     Long logId = null;
     String msg = "";
     try
     {
       long startTime = System.currentTimeMillis();
       
 
       jobLog.setKEY(jobDataMap.getString("_longTransKey"));
       jobLog.setPROCESS(jobDataMap.getString("processClass"));
       jobLog.setBZ(jobDataMap.getString("memo"));
       jobLog.setSTARTTIME(Long.valueOf(Long.parseLong(DateUtil.CurDate.YYYYMMDDHHmmss.getDate())));
       jobLog.setTRANSID(jobDataMap.getString("_longTransID"));
       jobLog.setXTJGDM((String)dto.getValue("BAE001"));
       logId = Long.valueOf(jobLogger.log(JobLogger.LogIU.INSERT, jobLog));
       String key = jobDataMap.getString("_longTransKey");
       dto.setValue("_logId", logId);
       dto.setValue("_longTransKey", key);
       
       argsMap.putAll(dto.getData());
       argsMap.put("pi_logid", logId);
       argsMap.put("pi_key", key);
       argsMap.put("pi_transid", jobDataMap.getString("_longTransID"));
       String process = jobDataMap.getString("processClass");
       Util.doProcedure(process, argsMap, null);
       
       long endTime = System.currentTimeMillis();
       LOGGER.info(context.getJobDetail().getKey() + " 耗时 :" + (endTime - startTime) / 1000L + " (秒)");
     }
     catch (Exception e)
     {
       msg = e.getLocalizedMessage();
       throw new AppException("执行过程" + jobDataMap.getString("processClass") + "出错，详细:" + e.getLocalizedMessage(), e);
     }
     finally
     {
       jobLog.setLOGID(logId);
       jobLog.setBZ(msg);
       jobLog.setENDTIME(Long.valueOf(Long.parseLong(DateUtil.CurDate.YYYYMMDDHHmmss.getDate())));
       jobLogger.log(JobLogger.LogIU.UPDATE, jobLog);
     }
   }
 }