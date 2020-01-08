 package cn.sinobest.framework.service.longtran;
 
 import cn.sinobest.framework.comm.dto.DTO;
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IDAO;
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.util.Util;
 import java.text.ParseException;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import org.quartz.CronTrigger;
 import org.quartz.JobDataMap;
 import org.quartz.JobDetail;
 import org.quartz.Scheduler;
 import org.quartz.SchedulerException;
 import org.quartz.Trigger;
 import org.quartz.TriggerUtils;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.context.annotation.Lazy;
 import org.springframework.stereotype.Service;
 
 @Service
 @Lazy(true)
 public class LongProcessService
 {
   private static final Logger LOGGER = LoggerFactory.getLogger(LongProcessService.class);
   @Autowired
   private IDAO commDAO;
   private Scheduler scheduler = (Scheduler)Util.getBean("quartzScheduler");
   
   public LongTran getLongTranConfByID(String transId)
   {
     Map<String, String> args = new HashMap();
     args.put("TRANSID", transId);
     args.put("TRIGGER_TYPE", "FORE");
     LongTran longTranConf = (LongTran)this.commDAO.selectOneType("FW_CONFIG.FW_LONGTRANS_CONF_Q", args);
     return longTranConf;
   }
   
   public List<LongTran> getLongTranConf()
   {
     Map<String, String> args = new HashMap();
     args.put("TRIGGER_TYPE", "BACK");
     List<LongTran> longTranConfList = this.commDAO.selectListType("FW_CONFIG.FW_LONGTRANS_CONF_Q2", args);
     return longTranConfList;
   }
   
   public String scheduler(IDTO dto)
     throws AppException
   {
     String transId = (String)dto.getValue("_longTransID");
     String key = (String)dto.getValue("_longTransKey");
     LongTran longTranConf = getLongTranConfByID(transId);
     if (longTranConf == null) {
       throw new AppException("未找到TRANSID为" + transId + "的长事务配置!");
     }
     longTranConf.setKEY(key);
     Map<String, String> paramsMap = new HashMap(2);
     paramsMap.put("TRANSID", transId);
     paramsMap.put("KEY", key);
     long cnt = this.commDAO.count("FW_CONFIG.FW_LONGTRANS_LOG_CNT", paramsMap).longValue();
     if (cnt > 0L) {
       return "1";
     }
     scheduler(longTranConf, dto);
     
     startScheduler();
     return "0";
   }
   
   public int getTriggerState(String triggerName, String group)
     throws SchedulerException
   {
     return this.scheduler.getTriggerState(triggerName, group);
   }
   
   private String getTriggerStateName(int state)
   {
     String stateName = "";
     switch (state)
     {
     case 4: 
       stateName = "正在排队等待执行"; break;
     case 2: 
       stateName = "已完成"; break;
     case 3: 
       stateName = "执行出错"; break;
     case -1: 
       stateName = "触发器不存在"; break;
     case 0: 
       stateName = "正在执行中"; break;
     case 1: 
       stateName = "暂停执行";
     }
     return stateName;
   }
   
   public void initBackJobs(IDTO dto)
   {
     List<LongTran> longTranConf = getLongTranConf();
     for (LongTran longTran : longTranConf)
     {
       longTran.setKEY(longTran.getTRANSID());
       try
       {
         scheduler(longTran, new DTO());
       }
       catch (Exception ex)
       {
         LOGGER.error(ex.getLocalizedMessage(), ex);
       }
     }
     startScheduler();
   }
   
   private void startScheduler()
     throws AppException
   {
     try
     {
       if (!this.scheduler.isStarted()) {
         this.scheduler.start();
       }
     }
     catch (SchedulerException e)
     {
       throw new AppException("开启调度器出错，详细：" + e.getLocalizedMessage(), e);
     }
   }
   
   public Scheduler scheduler(LongTran longTran, IDTO dto)
     throws AppException
   {
     try
     {
       String key = "_longTransKey_" + longTran.getKEY();
       
       String triggerName = "_longTransTrigger_" + longTran.getKEY();
       String group = longTran.getTRANSID();
       int state = getTriggerState(triggerName, group);
       if (state != -1) {
         throw new AppException(getTriggerStateName(state));
       }
       dto.getData().remove("struts.valueStack");
       dto.getData().remove("struts.actionMapping");
       dto.getData().remove("org.springframework.web.context.request.RequestContextListener.REQUEST_ATTRIBUTES");
       
       JobDetail jobDetail = null;
       Class cls = ProcedureJob.class;
       if ("PROC".equals(longTran.getCTYPE())) {
         cls = ProcedureJob.class;
       } else {
         cls = JavaJob.class;
       }
       jobDetail = new JobDetail(key, group, cls);
       
       jobDetail.getJobDataMap().put("dto", dto);
       jobDetail.getJobDataMap().put("processClass", longTran.getPROCESS());
       jobDetail.getJobDataMap().put("_longTransKey", longTran.getKEY());
       jobDetail.getJobDataMap().put("memo", longTran.getMEMO());
       jobDetail.getJobDataMap().put("_longTransID", longTran.getTRANSID());
       
       Trigger trigger = null;
       if ("BACK".equalsIgnoreCase(longTran.getTRIGGER_TYPE()))
       {
         if (!Util.isEmpty(longTran.getTRANSID())) {
           trigger = new CronTrigger(triggerName, longTran.getTRANSID(), longTran.getCRON_EXPR());
         } else {
           throw new AppException("未指定CRON_EXPR");
         }
       }
       else
       {
         trigger = TriggerUtils.makeImmediateTrigger(0, 0L);
         trigger.setName(triggerName);
         trigger.setGroup(group);
       }
       return scheduler(jobDetail, trigger);
     }
     catch (ParseException e)
     {
       throw new AppException("Cron 表达式解析出错，详细：" + e.getLocalizedMessage(), e);
     }
     catch (SchedulerException e)
     {
       throw new AppException("调度时出现异常，详细：" + e.getLocalizedMessage(), e);
     }
   }
   
   private Scheduler scheduler(JobDetail jobDetail, Trigger trigger)
     throws AppException
   {
     try
     {
       this.scheduler.scheduleJob(jobDetail, trigger);
       return this.scheduler;
     }
     catch (SchedulerException e)
     {
       throw new AppException("调度时出现异常，详细：" + e.getLocalizedMessage(), e);
     }
   }
   
   public String isComplete(IDTO dto)
   {
     Map<String, Object> rtn = this.commDAO.selectOne("FW_CONFIG.FW_LONGTRANS_LOG_Q", dto.getData());
     Object startTime = rtn.get("STARTTIME");
     Object endTime = rtn.get("ENDTIME");
     if ((startTime != null) && (endTime != null)) {
       return "2";
     }
     if ((startTime == null) && (endTime == null)) {
       return "2";
     }
     return "1";
   }
 }