 package cn.sinobest.framework.service.workflow;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.dao.workflow.WfActionDef2Task;
 import cn.sinobest.framework.util.ConfUtil;
 import cn.sinobest.framework.util.Util;
 import java.lang.reflect.InvocationTargetException;
 import java.lang.reflect.Method;
 import java.util.HashMap;
 import java.util.Map;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.stereotype.Service;
 
 @Service
 public class WorkflowProxyService
   implements IWorkflowProxyService
 {
   private static Logger LOGGER = LoggerFactory.getLogger(WorkflowProxyService.class);
   
   public Object doWork(IDTO dto)
     throws AppException
   {
     String procDefId = (String)dto.getValue("_processDefId");
     String currDefId = (String)dto.getValue("_curActDefId");
     String nextDefId = (String)dto.getValue("_nextActDefId");
     if (Util.isEmpty(procDefId)) {
       throw new AppException("流程定义ID未指定!");
     }
     if (Util.isEmpty(currDefId)) {
       throw new AppException("流程当前环节定义ID未指定!");
     }
     if (Util.isEmpty(nextDefId)) {
       throw new AppException("下一环节定义ID未指定!");
     }
     if ((!Util.isEmpty(nextDefId)) && (nextDefId.startsWith("!!"))) {
       nextDefId = nextDefId.substring("!!".length());
     }
     String key = procDefId + "|" + currDefId + "|" + nextDefId;
     WfActionDef2Task wfTask = ConfUtil.getWfTask(key);
     if (wfTask == null)
     {
       LOGGER.warn("未获取到" + key + "的流程环节与业务处理关联信息");
       return null;
     }
     String tasks = wfTask.getTASK();
     if ((tasks == null) || (tasks.length() == 0)) {
       return null;
     }
     Map<String, Object> respStack = null;
     try
     {
       for (String task : tasks.split(","))
       {
         if (Util.isMatches(task, "^pkg_\\w+\\.\\w+$"))
         {
           respStack = Util.doProcedure(task, dto.getData(), null);
         }
         else if (task.lastIndexOf("Service") > 0)
         {
           String[] exeMsg = task.split("\\.");
           if (exeMsg.length != 2) {
             throw new AppException(task + "未指方法名!");
           }
           Object service = Util.getBean(exeMsg[0]);
           Class<?> cls = service.getClass();
           String exec = exeMsg[0] + "." + exeMsg[1];
           LOGGER.debug("执行任务：service:" + exec + "(IDTO)");
           Method method = cls.getMethod(exeMsg[1], new Class[] { IDTO.class });
           Object respO = method.invoke(service, new Object[] { dto });
           if ((respO instanceof Map))
           {
             respStack = (Map)respO;
           }
           else if ((respO instanceof IDTO))
           {
             respStack = new HashMap();
             respStack.putAll(((IDTO)respO).getData());
           }
           else if (respO != null)
           {
             respStack = new HashMap();
             respStack.put(exeMsg[0] + "." + exeMsg[1], respO);
             LOGGER.debug("返回参数key：" + exec);
           }
         }
         if (respStack != null) {
           dto.getData().putAll(respStack);
         }
       }
     }
     catch (IllegalArgumentException e)
     {
       throw new AppException(e.getMessage(), e);
     }
     catch (IllegalAccessException e)
     {
       throw new AppException(e.getMessage(), e);
     }
     catch (InvocationTargetException e)
     {
       throw new AppException(e.getTargetException().getMessage(), e.getTargetException());
     }
     catch (SecurityException e)
     {
       throw new AppException(e.getMessage(), e);
     }
     catch (NoSuchMethodException e)
     {
       throw new AppException(e.getMessage(), e);
     }
     catch (Exception e)
     {
       throw new AppException(e.getMessage(), e);
     }
     return respStack;
   }
 }