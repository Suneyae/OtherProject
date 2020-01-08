 package cn.sinobest.framework.service;
 
 import cn.sinobest.framework.comm.dto.DTO;
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IDAO;
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.comm.iface.IOperator;
 import cn.sinobest.framework.comm.transcation.DataSourceCallBack;
 import cn.sinobest.framework.comm.transcation.IDataSourceCallBack;
 import cn.sinobest.framework.dao.CommDAO;
 import cn.sinobest.framework.dao.workflow.WfActionDef;
 import cn.sinobest.framework.dao.workflow.WfWorkItem;
 import cn.sinobest.framework.service.json.JSONUtilities;
 import cn.sinobest.framework.service.tags.WfService;
 import cn.sinobest.framework.service.workflow.IWorkflow.Attr;
 import cn.sinobest.framework.service.workflow.IWorkflow.ProcStartOrEnd;
 import cn.sinobest.framework.service.workflow.IWorkflow.RightMsg;
 import cn.sinobest.framework.service.workflow.IWorkflowService;
 import cn.sinobest.framework.util.ConfUtil;
 import cn.sinobest.framework.util.DateUtil.CurDate;
 import cn.sinobest.framework.util.Util;
 import java.lang.reflect.Array;
 import java.lang.reflect.InvocationTargetException;
 import java.lang.reflect.Method;
 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.Collections;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import java.util.Map.Entry;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
 
 @Service("commService")
 public class CommService
 {
   private static final Logger LOGGER = LoggerFactory.getLogger(CommService.class);
   public static final String COMM_DO = "_commDo";
   public static final String ACTION_AFTER = "_afterAction";
   public static final String ACTION_BEFORE = "_beforeAction";
   public static final String DS = "_ds";
   @Autowired
   private CommDAO commDAO;
   
   static enum Transform
   {
     FIRSTROW,  BREAKDOWN;
   }
   
   private static enum ExecType
   {
     MAPSQL,  PROCEDURE,  SERVICE;
   }
   
   private static enum MapSQLSuffix
   {
     I("_I"),  D("_D"),  U("_U"),  NONE("");
     
     private String suffix = "";
     
     private MapSQLSuffix(String suffix)
     {
       this.suffix = suffix;
     }
     
     public String getName()
     {
       return this.suffix;
     }
   }
   
   private class ExecMsg
   {
     private CommService.ExecType execType;
     private String execStr;
     private String objectName = "";
     private String methodName = "";
     
     private ExecMsg() {}
     
     public String getObjectName()
     {
       return this.objectName;
     }
     
     public void setObjectName(String objectName)
     {
       this.objectName = objectName;
     }
     
     public String getMethodName()
     {
       return this.methodName;
     }
     
     public void setMethodName(String methodName)
     {
       this.methodName = methodName;
     }
     
     public CommService.ExecType getExecType()
     {
       return this.execType;
     }
     
     public void setExecType(CommService.ExecType execType)
     {
       this.execType = execType;
     }
     
     public String getExecStr()
     {
       return this.execStr;
     }
     
     public void setExecStr(String execStr)
     {
       this.execStr = execStr;
     }
   }
   
   private int insert(String sqlId, Map<String, ?> paramsMap)
     throws Exception
   {
     return this.commDAO.insert(sqlId, paramsMap);
   }
   
   private int delete(String sqlId, Map<String, ?> paramsMap)
     throws Exception
   {
     return this.commDAO.delete(sqlId, paramsMap);
   }
   
   private int update(String sqlId, Map<String, ?> paramsMap)
     throws Exception
   {
     return this.commDAO.update(sqlId, paramsMap);
   }
   
   public List<Map<String, Object>> query(IDTO dto)
     throws Exception
   {
     String sqlId = (String)dto.getData().get("_commDo");
     return this.commDAO.select(sqlId, dto.getData());
   }
   
   private Map<String, Object> doOneService(ExecMsg executeMsg, IDTO dto)
     throws Throwable
   {
     String doActionCode = "";
     String actionMsg = "没有执行任何操作";
     Map<String, Object> paramsMap = dto.getData();
     Map<String, Object> rtnMap = new HashMap();
     String rtnURL = (String)paramsMap.get("_rtnURL");
     StringBuffer wfRtnURL = new StringBuffer(100);
     String wId = "";
     String pId = "";
     Object rtnObj = null;
     boolean startFlag = false;
     boolean nextFlag = false;
     boolean commitFlag = false;
     boolean isSubWf = ((Boolean)(paramsMap.get("_isSubWf") == null ? Boolean.valueOf(false) : paramsMap.get("_isSubWf"))).booleanValue();
     paramsMap.put("_isSubWf", Boolean.valueOf(isSubWf));
     Util.nvl(paramsMap);
     
     String wfDefineID = (String)paramsMap.get("_processDefId");
     String curActDefID = (String)paramsMap.get("_curActDefId");
     String workItemId = (String)paramsMap.get("wid");
     String wfState = ((String)Util.nvl(paramsMap.get("_wfState"))).toLowerCase();
     String pInsId = (String)paramsMap.get("pid");
     String keyData = (String)paramsMap.get("_keyData");
     String wfBZ = (String)paramsMap.get("_comment");
     String accepter = (String)paramsMap.get("_accepterId");
     String nextActDefID = (String)paramsMap.get("_nextActDefId");
     String isWfStart = ((String)(paramsMap.get("_isWfStart") != null ? paramsMap.get("_isWfStart") : "false")).toLowerCase();
     String jar = (String)paramsMap.get("_operId");
     LOGGER.info("
     LOGGER.info("_processDefId=" + wfDefineID);
     LOGGER.info("_curActDefId=" + curActDefID);
     LOGGER.info("_nextActDefId=" + nextActDefID);
     LOGGER.info("wid=" + workItemId);
     LOGGER.info("_wfState=" + wfState);
     LOGGER.info("_isWfStart=" + isWfStart);
     LOGGER.info("pid=" + pInsId);
     LOGGER.info("_accepterId=" + accepter);
     LOGGER.info("_keyData=" + keyData);
     LOGGER.info("_comment=" + wfBZ);
     LOGGER.info("
     
     Map<String, String> respParams = new HashMap();
     
     IWorkflowService wfService = (IWorkflowService)Util.getBean("workflowService");
     if ("true".equals(isWfStart))
     {
       if ("wf".equals(wfState))
       {
         doActionCode = "001";
         actionMsg = "【001开始流程】";
         LOGGER.info(actionMsg);
         respParams = wfService.startWf(wfDefineID, paramsMap);
         pId = (String)respParams.get("pid");
         wId = (String)respParams.get("wid");
         if ((pId != null) && (pId.length() > 0)) {
           startFlag = true;
         }
         if (startFlag) {
           commitFlag = true;
         }
       }
       else if ("data".equals(wfState))
       {
         doActionCode = "002";
         actionMsg = "【002开始流程,保存数据】";
         LOGGER.info(actionMsg);
         
         respParams = wfService.startWf(wfDefineID, paramsMap);
         pId = (String)respParams.get("pid");
         wId = (String)respParams.get("wid");
         paramsMap.put("pid", pId);
         if ((pId != null) && (pId.length() > 0)) {
           startFlag = true;
         }
         rtnObj = doOperService(executeMsg, dto);
         if (startFlag) {
           commitFlag = true;
         }
       }
       else if ("all".equals(wfState))
       {
         doActionCode = "003";
         actionMsg = "【003开始流程,保存数据,提交下一环节】";
         LOGGER.info(actionMsg);
         
         respParams = wfService.startWf(wfDefineID, paramsMap);
         
         pId = (String)respParams.get("pid");
         wId = (String)respParams.get("wid");
         paramsMap.put("pid", pId);
         if ((pId != null) && (pId.length() > 0)) {
           startFlag = true;
         }
         rtnObj = doOperService(executeMsg, dto);
         
 
         nextFlag = wfService.submitWorkItem(pId, wId, paramsMap);
         if ((startFlag) && (nextFlag))
         {
           pId = (String)paramsMap.get("pid");
           wId = (String)paramsMap.get("wid");
           commitFlag = true;
         }
       }
     }
     else
     {
       pId = pInsId;
       wId = workItemId;
       if ("wf".equals(wfState))
       {
         if (("".equals(pId)) || ("".equals(wId))) {
           throw new AppException("业务流水号和环节号不能为空!");
         }
         doActionCode = "004";
         actionMsg = "【004提交到下一环节】";
         LOGGER.info(actionMsg);
         
         commitFlag = wfService.submitWorkItem(pId, wId, paramsMap);
         pId = (String)paramsMap.get("pid");
         wId = (String)paramsMap.get("wid");
       }
       else if ("all".equals(wfState))
       {
         if (("".equals(pId)) || ("".equals(wId))) {
           throw new AppException("业务流水号和环节号不能为空!");
         }
         doActionCode = "006";
         actionMsg = "【006保存数据,提交下一环节】";
         LOGGER.info(actionMsg);
         
         rtnObj = doOperService(executeMsg, dto);
         
 
 
 
 
         nextFlag = wfService.submitWorkItem(pId, wId, paramsMap);
         LOGGER.info("提交环节" + (nextFlag ? "成功" : "失败"));
         if (nextFlag)
         {
           pId = (String)paramsMap.get("pid");
           wId = (String)paramsMap.get("wid");
           commitFlag = true;
         }
       }
       else
       {
         doActionCode = "005";
         actionMsg = "【005保存数据】";
         if (!Util.isEmpty(workItemId))
         {
           WfWorkItem wfWorkItem = new WfWorkItem();
           wfWorkItem.setWORK_ITEM_ID(Long.valueOf(Long.parseLong(workItemId)));
           wfWorkItem.setMEMO((String)Util.nvl(wfBZ));
           wfService.updateWorkItem(wfWorkItem);
         }
         LOGGER.info(actionMsg);
         rtnObj = doOperService(executeMsg, dto);
         commitFlag = true;
       }
     }
     if (!commitFlag) {
       throw new AppException("系统错误，数据提交失败！");
     }
     boolean isGD = false;
     if ((!isSubWf) && (pId != null) && (pId.trim().length() > 0))
     {
       String isEnd = "";
       if (!"".equals(nextActDefID))
       {
         WfActionDef wfDef = wfService.getWfByActionDef(wfDefineID, WfService.getNextDefId(nextActDefID));
         if (wfDef == null)
         {
           isEnd = "no";
         }
         else
         {
           isEnd = wfDef.getSTART_OR_END();
           isGD = Boolean.parseBoolean((String)Util.nvl(Util.searchByKey(IWorkflow.Attr.GD.toString(), wfDef.getATTR())));
           String afterAction = (String)Util.nvl(Util.searchByKey("afterAction", wfDef.getATTR()));
           if (!Util.isEmpty(afterAction))
           {
             ExecMsg execMsg = getExecMsg(afterAction);
             rtnObj = doOperService(execMsg, dto);
             pId = (String)dto.getValue("pid");
             wId = (String)dto.getValue("wid");
           }
         }
       }
       String rightId = (String)Util.nvl(wfService.getRightIdByWfDefId(wfDefineID));
       if ((Util.isEmpty(rightId)) && (!Util.isEmpty(wId)))
       {
         Map<String, String> url = wfService.getActionForward(Long.valueOf(wId));
         rightId = (String)url.get(IWorkflow.RightMsg.ID.toString());
       }
       String menuId = (String)Util.nvl(wfService.getRightIdByPid(pId));
       if (("001".equals(doActionCode)) || 
         ("002".equals(doActionCode)) || 
         ("005".equals(doActionCode)))
       {
         Map<String, String> forward = wfService.getActionForward(curActDefID);
         String rightIdStr = (String)forward.get("RIGHTID");
         wfRtnURL.append((String)forward.get(IWorkflow.RightMsg.URL.toString())).append(IWorkflow.RightMsg.URL.toString().indexOf("?") >= 0 ? "&" : "?")
           .append("pid").append("=").append(pId)
           .append("&")
           .append("wid").append("=").append(wId)
           .append("&RightID=").append(rightIdStr)
           .append("&_menuID=").append(menuId)
           .append("&").append("funcID").append("=").append(Util.nvl(forward.get(IWorkflow.RightMsg.BUSSFUNCID.toString())));
       }
       else if (IWorkflow.ProcStartOrEnd.PSE_END.getState().equalsIgnoreCase(isEnd))
       {
         wfRtnURL.append("/jsp/framework/blank.jsp");
       }
       else if (jar.equals(accepter))
       {
         Map<String, String> forward = wfService.getActionForward(curActDefID);
         wfRtnURL.append("/Workflow.do?")
           .append("pid").append("=").append(pId)
           .append("&").append("wid").append("=").append(wId)
           .append("&RightID=").append(rightId)
           .append("&_menuID=").append(menuId)
           .append("&").append("funcID").append("=").append(Util.nvl(forward.get(IWorkflow.RightMsg.BUSSFUNCID.toString())));
       }
       else
       {
         wfRtnURL.append("/jsp/framework/blank.jsp");
       }
       if ((rtnURL.equals("")) || (rtnURL.equalsIgnoreCase("null"))) {
         rtnURL = wfRtnURL.toString();
       }
     }
     Map<String, Object> oMap = uniformResult(rtnObj);
     rtnMap.putAll(oMap);
     
     rtnMap.put("pid", pId);
     rtnMap.put("wid", wId);
     rtnMap.put("_rtnURL", rtnURL);
     rtnMap.put(IWorkflow.Attr.GD.toString(), Boolean.toString(isGD));
     return rtnMap;
   }
   
   private ExecMsg getExecMsg(String execStr)
     throws Exception
   {
     String[] exec = execStr.split("\\.");
     String prefx = exec[0];
     ExecMsg execMsg = new ExecMsg(null);
     if (Util.isEmpty(prefx)) {
       throw new AppException("_commDo不合法!");
     }
     if (prefx.lastIndexOf("Service") > 0)
     {
       execMsg.setExecType(ExecType.SERVICE);
       execMsg.setObjectName(prefx);
       execMsg.setMethodName(exec[1]);
       return execMsg;
     }
     if (Util.isMatches(prefx, "^pkg_\\w+\\.\\w+$"))
     {
       execMsg.setExecType(ExecType.PROCEDURE);
       execMsg.setExecStr(execStr);
       return execMsg;
     }
     execMsg.setExecType(ExecType.MAPSQL);
     execMsg.setExecStr(execStr);
     return execMsg;
   }
   
   private Map<String, ?> doExecute(IDTO dto, String execStr)
     throws Throwable
   {
     ExecMsg execMsg = getExecMsg(execStr);
     
     Map<String, Object> rtnMap = null;
     switch (execMsg.getExecType())
     {
     case SERVICE: 
       rtnMap = doOneService(execMsg, dto);
       break;
     case PROCEDURE: 
       rtnMap = doProcedure(execMsg.getExecStr(), dto.getData(), (String)dto.getValue("_ds"));
       break;
     default: 
       switch (getMapSQLSuffix(execMsg.getExecStr()))
       {
       case D: 
         insert(execStr, dto.getData()); break;
       case I: 
         update(execStr, dto.getData()); break;
       case NONE: 
         delete(execStr, dto.getData()); break;
       default: 
         throw new AppException("_commDo中" + execStr + "没有包含后缀_I、_U、_D");
       }
       break;
     }
     return rtnMap;
   }
   
   private MapSQLSuffix getMapSQLSuffix(String execStr)
   {
     String actType = execStr.substring(execStr.lastIndexOf("_"), execStr.lastIndexOf("_") + 2).toUpperCase();
     if (MapSQLSuffix.I.getName().equalsIgnoreCase(actType)) {
       return MapSQLSuffix.I;
     }
     if (MapSQLSuffix.D.getName().equalsIgnoreCase(actType)) {
       return MapSQLSuffix.D;
     }
     if (MapSQLSuffix.U.getName().equalsIgnoreCase(actType)) {
       return MapSQLSuffix.U;
     }
     return MapSQLSuffix.NONE;
   }
   
   public void doService(IDTO dto)
     throws Throwable
   {
     try
     {
       final String[] execStrs = ((String)dto.getData().get("_commDo")).split("\\|");
       if (execStrs.length <= 0) {
         throw new AppException("参数_commDo为指定操作");
       }
       final IDTO dto2 = new DTO();
       dto2.setUserInfo(dto.getUserInfo());
       final Map<String, Object> argsMap = Util.mapClone(dto.getData());
       final Map<String, Object> rtnsMap = new HashMap();
       String ds = (String)dto.getValue("_ds");
       if (Util.isEmpty(ds)) {
         for (String exec : execStrs)
         {
           dto2.setData(Util.mapClone(argsMap));
           
 
           Map<String, ?> rtnMap = doExecute(dto2, exec);
           if (rtnMap != null)
           {
             rtnsMap.putAll(rtnMap);
             argsMap.putAll(rtnsMap);
           }
         }
       } else {
         DataSourceCallBack.execute(ds, new IDataSourceCallBack()
         {
           public String doAction()
             throws AppException
           {
             try
             {
               for (String exec : execStrs)
               {
                 dto2.setData(Util.mapClone(argsMap));
                 
 
                 Map<String, ?> rtnMap = CommService.this.doExecute(dto2, exec);
                 if (rtnMap != null)
                 {
                   rtnsMap.putAll(rtnMap);
                   argsMap.putAll(rtnsMap);
                 }
               }
             }
             catch (Throwable e)
             {
               throw new AppException(e.getLocalizedMessage(), e);
             }
             return "";
           }
         });
       }
       dto.setData(rtnsMap);
     }
     catch (Throwable t)
     {
       throw t;
     }
   }
   
   private Object doOperService(ExecMsg executeMsg, IDTO dto)
     throws Throwable
   {
     try
     {
       Object service = Util.getBean(executeMsg.getObjectName());
       Class<?> cls = service.getClass();
       LOGGER.debug("执行service:" + executeMsg.getObjectName() + "." + executeMsg.getMethodName());
       Method method = cls.getMethod(executeMsg.getMethodName(), new Class[] { IDTO.class });
       return method.invoke(service, new Object[] { dto });
     }
     catch (InvocationTargetException e)
     {
       throw e.getTargetException();
     }
   }
   
   public void submitWf(IDTO dto)
     throws AppException
   {
     try
     {
       IWorkflowService wfService = (IWorkflowService)Util.getBean("workflowService");
       if (wfService.submitWorkItem(dto))
       {
         String execStr = (String)dto.getValue("_commDo");
         if (Util.isEmpty(execStr)) {
           return;
         }
         ExecMsg execMsg = getExecMsg(execStr);
         if (execMsg.getExecType() == ExecType.SERVICE) {
           doOperService(execMsg, dto);
         } else {
           throw new AppException("未指定要执行业务处理的Serivce对象！");
         }
       }
     }
     catch (AppException e)
     {
       throw e;
     }
     catch (Throwable e)
     {
       throw new AppException("提交任务失败!", e);
     }
   }
   
   private Map<String, Object> doProcedure(String procedureName, Map<String, ?> values, String dataSource)
     throws Exception
   {
     JdbcCallService service = (JdbcCallService)Util.getBean("jdbcCallService");
     return service.doProcedure(procedureName, values, dataSource);
   }
   
   public List<Object> doAjaxService(IDTO dto)
     throws AppException
   {
     Map[] parameters = (Map[])dto.getValue("parameters");
     Map<String, Object> shareArguments = (Map)dto.getValue("shareParameters");
     if (LOGGER.isDebugEnabled())
     {
       LOGGER.debug("处理ajax业务，入参串是：" + new JSONUtilities().parseObject(parameters));
       LOGGER.debug(">>>共享参数是：" + shareArguments);
     }
     List<Object> resultList = new ArrayList();
     for (Map<String, Object> serviceMap : parameters)
     {
       String serviceId = (String)serviceMap.get("serviceId");
       String methodName = (String)serviceMap.get("method");
       Object useShare = serviceMap.get("useShare");
       Object shareNotNull = serviceMap.get("shareNotNull");
       String transform = (String)serviceMap.get("transform");
       Map dtoData = dto.getData();
       if ((serviceId.length() == 0) || (methodName.length() == 0)) {
         throw new AppException("EFW0001", null, new Object[] { "serviceId和method" });
       }
       Map<String, Object> serviceParameters = (Map)serviceMap.get("parameters");
       
       Map<String, Object> arguments = new HashMap();
       if ((shareArguments != null) && (useShare != null))
       {
         if (Boolean.TRUE.equals(useShare)) {
           arguments.putAll(shareArguments);
         } else {
           for (Map.Entry<String, Object> entry : ((Map)useShare).entrySet())
           {
             Object value = entry.getValue();
             if ((value instanceof Collection)) {
               for (String c_value : (Collection)value) {
                 arguments.put(c_value, shareArguments.get(entry.getKey()));
               }
             } else {
               arguments.put((String)value, shareArguments.get(entry.getKey()));
             }
           }
         }
         if (shareNotNull != null) {
           if (Boolean.TRUE.equals(shareNotNull))
           {
             for (Map.Entry<String, Object> entry : arguments.entrySet())
             {
               if (entry.getValue() == null) {
                 throw new AppException("EFW0001", null, new Object[] { entry.getKey() });
               }
               if (((entry.getValue() instanceof String)) && 
                 (((String)entry.getValue()).length() == 0)) {
                 throw new AppException("EFW0001", null, new Object[] { entry.getKey() });
               }
             }
           }
           else
           {
             int arrayLength = Array.getLength(shareNotNull);
             for (int i = 0; i < arrayLength; i++)
             {
               Object one = Array.get(shareNotNull, i);
               if (one != null)
               {
                 String str = one.toString();
                 Object value = arguments.get(str);
                 if (value == null) {
                   throw new AppException("EFW0001", null, new Object[] { str });
                 }
                 if (((value instanceof String)) && 
                   (((String)value).length() == 0)) {
                   throw new AppException("EFW0001", null, new Object[] { str });
                 }
               }
             }
           }
         }
       }
       if (serviceParameters != null) {
         arguments.putAll(serviceParameters);
       }
       dto.getData().clear();
       
       dto.getData().putAll(dtoData);
       
       dto.setData(arguments);
       
       Object service = Util.getBean(serviceId);
       Object cls = service.getClass();
       LOGGER.debug("执行" + serviceId + "." + methodName);
       
       Object rst = null;
       try
       {
         Method method = ((Class)cls).getMethod(methodName, new Class[] { IDTO.class });
         rst = method.invoke(service, new Object[] { dto });
       }
       catch (SecurityException e)
       {
         throw new AppException("无访问权限", e);
       }
       catch (InvocationTargetException e)
       {
         throw new AppException(e.getTargetException().getMessage(), e.getTargetException());
       }
       catch (NoSuchMethodException e)
       {
         throw new AppException(serviceId + "中未找到方法" + methodName, e);
       }
       catch (IllegalArgumentException e)
       {
         throw new AppException("非法参数", e);
       }
       catch (IllegalAccessException e)
       {
         throw new AppException("非法访问", e);
       }
       Method method;
       if ((rst != null) && ((rst instanceof List)) && (!((List)rst).isEmpty()) && (transform != null)) {
         switch (Transform.valueOf(transform.toUpperCase()))
         {
         case BREAKDOWN: 
           rst = ((List)rst).get(0);
           break;
         default: 
           rst = breakDown((List)rst);
         }
       }
       resultList.add(rst);
       if ((rst != null) && (serviceMap.get("shareResults") != null))
       {
         Object shareResult = serviceMap.get("shareResults");
         if (!Boolean.FALSE.equals(shareResult)) {
           if (Boolean.TRUE.equals(shareResult))
           {
             Object t = uniformResult(rst);
             if (t != null) {
               shareArguments.putAll(uniformResult(rst));
             }
           }
           else
           {
             Map<String, Object> resultMap = uniformResult(rst);
             for (Map.Entry<String, Object> entry : ((Map)shareResult).entrySet())
             {
               Object value = entry.getValue();
               if ((value instanceof Collection)) {
                 for (String c_value : (Collection)value) {
                   shareArguments.put(c_value, resultMap.get(entry.getKey()));
                 }
               } else {
                 shareArguments.put((String)value, resultMap.get(entry.getKey()));
               }
             }
           }
         }
       }
     }
     return resultList;
   }
   
   public Map<String, String> startWf(IDTO dto)
     throws AppException
   {
     String SERVER_OBJECT = "object";
     
 
 
     String SERVER_METHOD = "method";
     
 
     Map<String, Object> params = dto.getData();
     String processDefId = (String)params.get("_processDefId");
     String keyData = (String)params.get("_keyData");
     String OPERID = (String)params.get("_operId");
     if (Util.isEmpty(processDefId)) {
       throw new AppException("未指定流程定义ID,key=_processDefId");
     }
     if (Util.isEmpty(OPERID))
     {
       if (dto.getUserInfo() == null) {
         throw new AppException("未指定经办人,key=_operId");
       }
       dto.setValue("_operId", dto.getUserInfo().getLoginID());
     }
     if (Util.isEmpty(keyData)) {
       LOGGER.warn("未指定关键信息,key=_keyData");
     }
     try
     {
       String commDo = (String)params.get("_commDo");
       String wfstate = (String)params.get("_wfState");
       
       String serverObject = (String)params.get("object");
       String serverMethod = (String)params.get("method");
       if (Util.isEmpty(wfstate))
       {
         wfstate = "wf";
       }
       else
       {
         if (Util.isEmpty(commDo)) {
           commDo = serverObject + "." + serverMethod;
         }
         if (Util.isEmpty(commDo)) {
           LOGGER.warn("未指定_commDo");
         }
       }
       dto.setValue("_isWfStart", params.get("_isWfStart"));
       dto.setValue("_wfState", wfstate);
       dto.setValue("_isSubWf", Boolean.valueOf(true));
       dto.setValue("_commDo", commDo);
       CommService commService = (CommService)Util.getBean("commService");
       commService.doService(dto);
       Map<String, String> respMap = new HashMap();
       respMap.put("pid", (String)dto.getValue("pid"));
       respMap.put("wid", (String)dto.getValue("wid"));
       return respMap;
     }
     catch (Throwable e)
     {
       LOGGER.error("开启流程失败，详细：" + e.getMessage(), e);
       throw new AppException("开启流程失败，详细：" + e.getMessage(), e);
     }
   }
   
   private Map<String, Object> breakDown(List<Map<String, Object>> rst)
   {
     if ((rst == null) || (rst.isEmpty())) {
       return null;
     }
     Object row = rst.get(0);
     
     Map<String, Object> rest = new HashMap();
     for (String key : ((Map)row).keySet()) {
       rest.put(key, new ArrayList(rst.size()));
     }
     for (Map.Entry<String, Object> key : ((Map)row).entrySet()) {
       ((Collection)rest.get(key.getKey())).add(key.getValue());
     }
     return rest;
   }
   
   private Map<String, Object> uniformResult(Object rst)
   {
     if ((rst instanceof Map)) {
       return (Map)rst;
     }
     if ((rst instanceof List)) {
       return Collections.singletonMap("", rst);
     }
     return Collections.singletonMap("", rst);
   }
   
   public void storeException(String url, IDTO dto, String operId, Throwable e)
   {
     try
     {
       if ("1".equals(ConfUtil.getParam("STORE_EXCEPTION")))
       {
         LOGGER.debug("保存异常信息到FW_LOG4EXCEPTION表");
         IDAO dao = (IDAO)Util.getBean("commDAO");
         
         StringBuffer ex = new StringBuffer();
         if (e != null)
         {
           ex.append(e.getLocalizedMessage());
           StackTraceElement[] st = e.getStackTrace();
           for (StackTraceElement ste : st) {
             ex.append(ste.toString());
           }
         }
         String exStr = ex.toString();
         if (exStr.getBytes().length > 1333) {
           exStr = Util.subStringByte(exStr, 1333);
         }
         String paramStr = "";
         if (dao != null)
         {
           dto.getData().remove("struts.valueStack");
           dto.getData().remove("struts.actionMapping");
           dto.getData().remove("CharacterEncodingFilter.FILTERED");
           dto.getData().remove("__cleanup_recursion_counter");
           dto.getData().remove("org.springframework.web.context.request.RequestContextListener.REQUEST_ATTRIBUTES");
           dto.getData().remove("OPERATOR");
           JSONUtilities jsonUtil = new JSONUtilities();
           paramStr = jsonUtil.parseObject(dto.getData()).toString();
         }
         if (paramStr.getBytes().length > 1333) {
           paramStr = Util.subStringByte(paramStr, 1333);
         }
         Object err = new HashMap(5);
         ((Map)err).put("EXCEPTIONSTACK", exStr);
         ((Map)err).put("URL", (String)Util.nvl(url));
         ((Map)err).put("PARAMS", paramStr);
         ((Map)err).put("OPERID", (String)Util.nvl(operId));
         ((Map)err).put("EVENTTIME", DateUtil.CurDate.YYYYMMDDHHmmss.getDate());
         dao.insert("FW_CONFIG.FW_LOG4EXCEPTION_I", (Map)err);
       }
     }
     catch (Exception ex)
     {
       LOGGER.error(ex.getLocalizedMessage(), ex);
     }
   }
   
   public <T> T autoActoin(IDataSourceCallBack<T> action)
   {
     return action.doAction();
   }
 }