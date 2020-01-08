 package cn.sinobest.framework.service.longtran;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.util.DateUtil.CurDate;
 import java.lang.reflect.InvocationTargetException;
 import java.lang.reflect.Method;
 import org.quartz.JobDataMap;
 import org.quartz.JobDetail;
 import org.quartz.JobExecutionContext;
 import org.quartz.JobExecutionException;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.util.ClassUtils;
 
 public class JavaJob
   implements IJob
 {
   private static final Logger LOGGER = LoggerFactory.getLogger(LongProcessService.class);
   
   public void execute(JobExecutionContext context)
     throws JobExecutionException
   {
     JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
     IDTO dto = (IDTO)jobDataMap.get("dto");
     String porcess = jobDataMap.getString("processClass");
     int lastDoIndex = porcess.lastIndexOf(".");
     String className = porcess.substring(0, lastDoIndex);
     String methodName = porcess.substring(lastDoIndex + 1);
     Class jobClass = null;
     JobLogger jobLogger = JobLogger.getInstance();
     JobLog jobLog = new JobLog();
     Long logId = null;
     String msg = "";
     try
     {
       jobClass = resolveClassName(className);
       Method jobMethod = jobClass.getMethod(methodName, new Class[] { IDTO.class });
       LOGGER.info(context.getJobDetail().getGroup() + "." + context.getJobDetail().getName() + " ,key=" + context.getJobDetail().getKey());
       long startTime = System.currentTimeMillis();
       
 
       jobLog.setKEY(jobDataMap.getString("_longTransKey"));
       jobLog.setPROCESS(porcess);
       jobLog.setBZ(jobDataMap.getString("memo"));
       jobLog.setSTARTTIME(Long.valueOf(Long.parseLong(DateUtil.CurDate.YYYYMMDDHHmmss.getDate())));
       jobLog.setTRANSID(jobDataMap.getString("_longTransID"));
       jobLog.setXTJGDM((String)dto.getValue("BAE001"));
       logId = Long.valueOf(jobLogger.log(JobLogger.LogIU.INSERT, jobLog));
       dto.setValue("_logId", logId);
       dto.setValue("_longTransKey", jobDataMap.getString("_longTransKey"));
       dto.setValue("_longTransID", jobDataMap.getString("_longTransID"));
       context.setResult(jobMethod.invoke(jobClass.newInstance(), new Object[] { dto }));
       
       long endTime = System.currentTimeMillis();
       LOGGER.info(context.getJobDetail().getKey() + " 耗时 :" + (endTime - startTime) / 1000L + " (秒)");
     }
     catch (ClassNotFoundException e)
     {
       msg = "未找到" + className + "类，详细：" + e.getLocalizedMessage();
       throw new AppException(msg, e);
     }
     catch (IllegalArgumentException e)
     {
       msg = "非法参数，详细：" + e.getLocalizedMessage();
       throw new AppException(msg, e);
     }
     catch (IllegalAccessException e)
     {
       msg = "非法访问，详细：" + e.getLocalizedMessage();
       throw new AppException(msg, e);
     }
     catch (InvocationTargetException e)
     {
       msg = e.getTargetException().getLocalizedMessage();
       throw new AppException(e.getTargetException().getLocalizedMessage(), e.getTargetException());
     }
     catch (SecurityException e)
     {
       msg = "无限访问，详细：" + e.getLocalizedMessage();
       throw new AppException(msg, e);
     }
     catch (NoSuchMethodException e)
     {
       msg = className + "类未找到" + methodName + "方法，详细：" + e.getLocalizedMessage();
       throw new AppException(msg, e);
     }
     catch (InstantiationException e)
     {
       msg = className + "类实例化出现异常，详细：" + e.getLocalizedMessage();
       throw new AppException(msg, e);
     }
     finally
     {
       jobLog.setLOGID(logId);
       jobLog.setBZ(msg);
       jobLog.setENDTIME(Long.valueOf(Long.parseLong(DateUtil.CurDate.YYYYMMDDHHmmss.getDate())));
       jobLogger.log(JobLogger.LogIU.UPDATE, jobLog);
     }
   }
   
   private Class resolveClassName(String className)
     throws ClassNotFoundException
   {
     return ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
   }
 }