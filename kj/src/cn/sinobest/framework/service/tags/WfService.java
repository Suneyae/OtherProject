 package cn.sinobest.framework.service.tags;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.dao.workflow.WfActionDef;
 import cn.sinobest.framework.dao.workflow.WfProcessInstance;
 import cn.sinobest.framework.dao.workflow.WfWorkItem;
 import cn.sinobest.framework.service.workflow.IWorkflow.Attr;
 import cn.sinobest.framework.service.workflow.IWorkflow.ProcStartOrEnd;
 import cn.sinobest.framework.service.workflow.IWorkflowService;
 import cn.sinobest.framework.util.ConfUtil;
 import cn.sinobest.framework.util.Util;
 import cn.sinobest.framework.web.tags.WfTag.ShowType;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
 
 @Service
 public class WfService
 {
   private static final Logger LOGGER = LoggerFactory.getLogger(WfService.class);
   @Autowired
   IWorkflowService workflowService;
   
   public WfTag.ShowType getBtnShowType(String processDefId, String actDefId)
     throws Exception
   {
     try
     {
       int k = 0;
       boolean endFlag = false;
       List<WfActionDef> wfActionDefList = ConfUtil.getAllWfConf(processDefId);
       if (Util.isEmpty(actDefId))
       {
         WfActionDef wfActionDef = getStartActDef(wfActionDefList, processDefId);
         String[] nextDefs = wfActionDef.getNEXT_ACTION_DEF_ID().split(",");
         for (int i = 0; i < nextDefs.length; i++) {
           if (!isBackAct(nextDefs[i])) {
             for (Iterator itor2 = wfActionDefList.iterator(); itor2.hasNext();)
             {
               WfActionDef wfActionDef2 = (WfActionDef)itor2.next();
               if (wfActionDef2.getACTION_DEF_ID().equalsIgnoreCase(nextDefs[i])) {
                 if (IWorkflow.ProcStartOrEnd.PSE_END.getState().equals(wfActionDef2.getSTART_OR_END())) {
                   endFlag = true;
                 } else {
                   k++;
                 }
               }
             }
           }
         }
         if (endFlag)
         {
           if (k == 0) {
             return WfTag.ShowType.START_END;
           }
           return WfTag.ShowType.MULTI;
         }
         return WfTag.ShowType.START;
       }
       for (Iterator itor = wfActionDefList.iterator(); itor.hasNext();)
       {
         WfActionDef wfActionDef = (WfActionDef)itor.next();
         if (wfActionDef.getACTION_DEF_ID().equalsIgnoreCase(actDefId))
         {
           if (IWorkflow.ProcStartOrEnd.PSE_START.getState().equals(wfActionDef.getSTART_OR_END()))
           {
             String nextDefIds = wfActionDef.getNEXT_ACTION_DEF_ID();
             String[] nextDefs = nextDefIds.split(",");
             for (int i = 0; i < nextDefs.length; i++) {
               if (!isBackAct(nextDefs[i])) {
                 for (Iterator itor2 = wfActionDefList.iterator(); itor2.hasNext();)
                 {
                   WfActionDef wfActionDef2 = (WfActionDef)itor2.next();
                   if (wfActionDef2.getACTION_DEF_ID().equalsIgnoreCase(nextDefs[i])) {
                     if (IWorkflow.ProcStartOrEnd.PSE_END.getState().equals(wfActionDef2.getSTART_OR_END())) {
                       endFlag = true;
                     } else {
                       k++;
                     }
                   }
                 }
               }
             }
             if (endFlag)
             {
               if (k == 0) {
                 return WfTag.ShowType.START_END;
               }
               return WfTag.ShowType.MULTI;
             }
             return WfTag.ShowType.START;
           }
           String nextDefIds = wfActionDef.getNEXT_ACTION_DEF_ID();
           if (!Util.isEmpty(nextDefIds))
           {
             String[] nextDefs = nextDefIds.split(",");
             for (int i = 0; i < nextDefs.length; i++) {
               if (!isBackAct(nextDefs[i])) {
                 for (Iterator itor2 = wfActionDefList.iterator(); itor2.hasNext();)
                 {
                   WfActionDef wfActionDef2 = (WfActionDef)itor2.next();
                   if (wfActionDef2.getACTION_DEF_ID().equalsIgnoreCase(nextDefs[i])) {
                     if (IWorkflow.ProcStartOrEnd.PSE_END.getState().equals(wfActionDef2.getSTART_OR_END())) {
                       endFlag = true;
                     } else {
                       k++;
                     }
                   }
                 }
               }
             }
             if (endFlag)
             {
               if (k == 0) {
                 return WfTag.ShowType.END;
               }
               return WfTag.ShowType.MULTI;
             }
           }
         }
       }
     }
     catch (Exception ex)
     {
       LOGGER.error("判断按钮显示方式时出错,详细:" + ex.getMessage(), ex);
     }
     return WfTag.ShowType.MIDDLE;
   }
   
   private WfActionDef getStartActDef(List<WfActionDef> wfActionDefList, String procDefId)
   {
     WfActionDef wfActionDef = null;
     for (Iterator itor = wfActionDefList.iterator(); itor.hasNext();)
     {
       wfActionDef = (WfActionDef)itor.next();
       if (IWorkflow.ProcStartOrEnd.PSE_START.getState().equals(wfActionDef.getSTART_OR_END())) {
         break;
       }
     }
     return wfActionDef;
   }
   
   public WfWorkItem getActDefId(Long workitemId)
     throws Exception
   {
     if ((workitemId == null) || (workitemId.longValue() == 0L)) {
       return null;
     }
     return this.workflowService.getActionDefId(workitemId);
   }
   
   public Map<String, String> getActDefId(String processDefId, Long workitemId)
     throws Exception
   {
     WfActionDef wfDef;
     WfActionDef wfDef;
     if ((workitemId == null) || (workitemId.longValue() == 0L))
     {
       wfDef = this.workflowService.getWfStartOrEnd(processDefId, IWorkflow.ProcStartOrEnd.PSE_START.getState());
     }
     else
     {
       WfWorkItem wfWorkItem = this.workflowService.getActionDefId(workitemId);
       wfDef = this.workflowService.getWfByActionDef(processDefId, wfWorkItem.getACTION_DEF_ID());
     }
     List<WfActionDef> wfActionDefList = ConfUtil.getAllWfConf(processDefId);
     
     Map<String, String> actName = new HashMap();
     for (Iterator itor = wfActionDefList.iterator(); itor.hasNext();)
     {
       WfActionDef wfActDef = (WfActionDef)itor.next();
       actName.put(wfActDef.getACTION_DEF_ID(), wfActDef.getACTION_DEF_NAME());
     }
     String nextDefIdStr = wfDef.getNEXT_ACTION_DEF_ID();
     String[] nextDefIds = nextDefIdStr.split(",");
     StringBuffer nextDefNameStr = new StringBuffer();
     for (int i = 0; i < nextDefIds.length; i++) {
       nextDefNameStr.append(i > 0 ? "," : "").append((String)actName.get(getNextDefId(nextDefIds[i])));
     }
     Map<String, String> actDef = new HashMap();
     actDef.put("_attr", wfDef.getATTR());
     actDef.put("_curActDefId", wfDef.getACTION_DEF_ID());
     actDef.put("_curActDefName", wfDef.getACTION_DEF_NAME());
     actDef.put("_nextActDefId", wfDef.getNEXT_ACTION_DEF_ID());
     actDef.put("_nextActDefName", nextDefNameStr.toString());
     
     return actDef;
   }
   
   public static String getNextDefId(String str)
   {
     if ((str != null) && (str.startsWith("!!"))) {
       return str.substring("!!".length());
     }
     return str;
   }
   
   public List<Map<String, Object>> getActs(IDTO dto)
     throws Exception
   {
     Map<String, Object> args = dto.getData();
     String processDefId = (String)args.get("_processDefId");
     String curActDefId = (String)args.get("_curActDefId");
     String pInsId = (String)args.get("pid");
     String curWorkItemId = (String)args.get("wid");
     String orgCode = (String)args.get("orgCode");
     Boolean cascade = Boolean.valueOf(Boolean.parseBoolean((String)args.get("cascade")));
     String jbr = (String)args.get("jbr");
     String actType = (String)args.get("actType");
     String unitId = (String)args.get("_unitId");
     
     boolean needGwUnitId = false;
     WfActionDef curActDef = ConfUtil.getWfConf(processDefId + "|" + curActDefId);
     
 
     List<WfActionDef> wfActionDefList = ConfUtil.getAllWfConf(processDefId);
     
     String nextDefId = curActDef.getNEXT_ACTION_DEF_ID();
     if (nextDefId == null) {
       return null;
     }
     Map<String, String> actName = new HashMap();
     for (Iterator itor = wfActionDefList.iterator(); itor.hasNext();)
     {
       WfActionDef wfActDef = (WfActionDef)itor.next();
       if (!IWorkflow.ProcStartOrEnd.PSE_END.equals(wfActDef.getSTART_OR_END())) {
         actName.put(wfActDef.getACTION_DEF_ID(), wfActDef.getACTION_DEF_NAME());
       }
     }
     String[] nextDefIds = nextDefId.split(",");
     
     String toApplyOpr = Util.searchByKey(IWorkflow.Attr.TOAPPLYOPR.toString(), curActDef.getATTR());
     String getItemOpr = Util.searchByKey(IWorkflow.Attr.GETITEMOPR.toString(), curActDef.getATTR());
     List<Map<String, Object>> resp = new ArrayList();
     for (int i = 0; i < nextDefIds.length; i++)
     {
       WfActionDef workDef = ConfUtil.getWfConf(processDefId + "|" + getNextDefId(nextDefIds[i]));
       if (!IWorkflow.ProcStartOrEnd.PSE_END.getState().equalsIgnoreCase(workDef.getSTART_OR_END()))
       {
         String gwOrg = "";
         String gwUnitId = "";
         needGwUnitId = false;
         String defvOpr = Util.searchByKey(IWorkflow.Attr.DEFAOPR.toString(), workDef.getATTR());
         if (("backAct".equalsIgnoreCase(actType)) && (isBackAct(nextDefIds[i])))
         {
           Map<String, Object> acts = new HashMap(4);
           List<String[]> receiver = this.workflowService.getReceivers4Ins(processDefId, curActDefId, pInsId, Util.isEmpty(curWorkItemId) ? 0L : Long.valueOf(curWorkItemId).longValue(), orgCode, getNextDefId(nextDefIds[i]), cascade.booleanValue(), jbr, unitId, false, args);
           String itemDefId = nextDefIds[i];
           if ("true".equals(toApplyOpr))
           {
             needGwUnitId = true;
             if (!Util.isEmpty(getItemOpr)) {
               itemDefId = getItemOpr;
             }
             Map<String, String> preOprMap = this.workflowService.getPreOpr(pInsId, getNextDefId(itemDefId), getNextDefId(nextDefIds[i]), "true");
             if (preOprMap != null)
             {
               receiver.add(0, new String[] { (String)preOprMap.get("OPERID"), (String)preOprMap.get("OPERNAME") });
               gwOrg = (String)preOprMap.get("BAE001");
               if (needGwUnitId) {
                 gwUnitId = String.valueOf(preOprMap.get("OPERUNITID"));
               }
             }
             else
             {
               preOprMap = this.workflowService.getPreOpr(pInsId, getNextDefId(itemDefId), getNextDefId(nextDefIds[i]), "false");
               if (preOprMap != null)
               {
                 gwOrg = (String)preOprMap.get("BAE001");
                 if (needGwUnitId) {
                   gwUnitId = String.valueOf(preOprMap.get("OPERUNITID"));
                 }
               }
               receiver.add(0, new String[] { "", "" });
             }
           }
           acts.put("ACTNAME", actName.get(getNextDefId(nextDefIds[i])));
           acts.put("ACTID", getNextDefId(nextDefIds[i]));
           acts.put("OACTID", nextDefIds[i]);
           acts.put("RECEIVER", receiver);
           acts.put("GWORG", gwOrg);
           acts.put("GWUNITID", gwUnitId);
           acts.put("DEFAOPR", defvOpr);
           resp.add(acts);
         }
         else if (("nextAct".equalsIgnoreCase(actType)) && (!isBackAct(nextDefIds[i])))
         {
           Map<String, Object> acts = new HashMap(4);
           List<String[]> receiver = this.workflowService.getReceivers4Ins(processDefId, curActDefId, pInsId, Util.isEmpty(curWorkItemId) ? 0L : Long.valueOf(curWorkItemId).longValue(), orgCode, getNextDefId(nextDefIds[i]), cascade.booleanValue(), jbr, unitId, false, args);
           String itemDefId = nextDefIds[i];
           if (("true".equals(toApplyOpr)) && (!Util.isEmpty(getItemOpr)))
           {
             itemDefId = getItemOpr;
             Map<String, String> preOprMap = this.workflowService.getPreOpr(pInsId, getNextDefId(itemDefId), getNextDefId(nextDefIds[i]), "true");
             needGwUnitId = true;
             if (preOprMap != null)
             {
               receiver.add(0, new String[] { (String)preOprMap.get("OPERID"), (String)preOprMap.get("OPERNAME") });
               gwOrg = (String)preOprMap.get("BAE001");
               if (needGwUnitId) {
                 gwUnitId = String.valueOf(preOprMap.get("OPERUNITID"));
               }
             }
             else
             {
               preOprMap = this.workflowService.getPreOpr(pInsId, getNextDefId(itemDefId), getNextDefId(nextDefIds[i]), "false");
               if (preOprMap != null)
               {
                 gwOrg = (String)preOprMap.get("BAE001");
                 if (needGwUnitId) {
                   gwUnitId = String.valueOf(preOprMap.get("OPERUNITID"));
                 }
               }
               receiver.add(0, new String[] { "", "" });
             }
           }
           acts.put("ACTNAME", actName.get(getNextDefId(nextDefIds[i])));
           acts.put("ACTID", nextDefIds[i]);
           acts.put("OACTID", nextDefIds[i]);
           acts.put("RECEIVER", receiver);
           acts.put("GWORG", gwOrg);
           acts.put("GWUNITID", gwUnitId);
           acts.put("DEFAOPR", defvOpr);
           resp.add(acts);
         }
       }
     }
     return resp;
   }
   
   public Map<String, Object> getRecevier(IDTO dto)
     throws Exception
   {
     Map<String, Object> args = dto.getData();
     String processDefId = (String)args.get("_processDefId");
     String curActDefId = (String)args.get("_curActDefId");
     String nextActDefId = (String)args.get("_nextActDefId");
     String pInsId = (String)args.get("pid");
     String curWorkItemId = (String)args.get("wid");
     String orgCode = (String)args.get("orgCode");
     Boolean cascade = Boolean.valueOf(Boolean.getBoolean((String)args.get("cascade")));
     String jbr = (String)args.get("jbr");
     String actType = (String)args.get("actType");
     String unitId = (String)args.get("_unitId");
     Map<String, Object> resp = new HashMap();
     
     WfActionDef curActDef = ConfUtil.getWfConf(processDefId + "|" + curActDefId);
     String toApplyOpr = Util.searchByKey(IWorkflow.Attr.TOAPPLYOPR.toString(), curActDef.getATTR());
     String getItemOpr = Util.searchByKey(IWorkflow.Attr.GETITEMOPR.toString(), curActDef.getATTR());
     
     List<WfActionDef> wfActionDefList = ConfUtil.getAllWfConf(processDefId);
     
     String nextDefId = curActDef.getNEXT_ACTION_DEF_ID();
     if (nextDefId == null) {
       return null;
     }
     Map<String, String> actName = new HashMap();
     for (Iterator itor = wfActionDefList.iterator(); itor.hasNext();)
     {
       WfActionDef wfActDef = (WfActionDef)itor.next();
       if (!IWorkflow.ProcStartOrEnd.PSE_END.equals(wfActDef.getSTART_OR_END())) {
         actName.put(wfActDef.getACTION_DEF_ID(), wfActDef.getACTION_DEF_NAME());
       }
     }
     List<String[]> receiver = this.workflowService.getReceivers4Ins(processDefId, curActDefId, pInsId, Util.isEmpty(curWorkItemId) ? 0L : Long.valueOf(curWorkItemId).longValue(), orgCode, getNextDefId(nextActDefId), cascade.booleanValue(), jbr, unitId, false, args);
     
     String gwOrg = "";
     String gwUnitId = "";
     boolean needGwUnitId = false;
     if (("backAct".equalsIgnoreCase(actType)) && (isBackAct(nextActDefId)))
     {
       String itemDefId = nextActDefId;
       if ("true".equals(toApplyOpr))
       {
         if (!Util.isEmpty(getItemOpr))
         {
           itemDefId = getItemOpr;
           needGwUnitId = true;
         }
         Map<String, String> preOprMap = this.workflowService.getPreOpr(pInsId, getNextDefId(itemDefId), getNextDefId(nextActDefId), "true");
         if (preOprMap != null)
         {
           receiver.add(0, new String[] { (String)preOprMap.get("OPERID"), (String)preOprMap.get("OPERNAME") });
         }
         else
         {
           preOprMap = this.workflowService.getPreOpr(pInsId, getNextDefId(itemDefId), getNextDefId(itemDefId), "false");
           if (preOprMap != null)
           {
             gwOrg = (String)preOprMap.get("BAE001");
             if (needGwUnitId) {
               gwUnitId = String.valueOf(preOprMap.get("OPERUNITID"));
             }
           }
           receiver.add(0, new String[] { "", "" });
         }
       }
     }
     resp.put("ACTNAME", actName.get(getNextDefId(nextActDefId)));
     resp.put("ACTID", getNextDefId(nextActDefId));
     resp.put("RECEIVER", receiver);
     resp.put("GWORG", gwOrg);
     resp.put("GWUNITID", gwUnitId);
     return resp;
   }
   
   public String getWfDefId(String pid, String wfName, String rightId)
     throws Exception
   {
     return this.workflowService.getWfDefId(pid, wfName, rightId);
   }
   
   private static boolean isBackAct(String actStr)
   {
     return actStr.startsWith("!!");
   }
   
   public WfProcessInstance getProcInstance(String pid)
     throws Exception
   {
     return this.workflowService.getWfProcessInstanceById(pid);
   }
   
   public Map<String, Object> getInitData(String wfId, String pid, String wfName, String rightId, String wId, boolean getAab001Aac001)
   {
     String wfDefId = wfId;
     String actDefId = "";
     Map<String, Object> initDataMap = new HashMap(7);
     try
     {
       if (Util.isEmpty(wfDefId)) {
         wfDefId = getWfDefId(pid, wfName, rightId);
       }
       if ((!Util.isEmpty(pid)) && (getAab001Aac001))
       {
         WfProcessInstance wfProcessInstance = getProcInstance(pid);
         initDataMap.put("_aab001", wfProcessInstance.getAAB001());
         initDataMap.put("_aac001", wfProcessInstance.getAAC001());
       }
       WfWorkItem wfWorkItem = getActDefId(Long.valueOf(Util.isEmpty(wId) ? 0L : Long.valueOf(wId).longValue()));
       if (wfWorkItem != null)
       {
         actDefId = wfWorkItem.getACTION_DEF_ID();
         String comment = wfWorkItem.getMEMO();
         initDataMap.put("comment", comment);
       }
       Map<String, String> actDef = getActDefId(wfDefId, Long.valueOf(Util.isEmpty(wId) ? 0L : Long.valueOf(wId).longValue()));
       initDataMap.put("btnType", getBtnShowType(wfDefId, actDefId));
       initDataMap.put("actDef", actDef);
       initDataMap.put("wfDefId", wfDefId);
     }
     catch (Exception e)
     {
       throw new AppException("获取流程初始化数据出错,详细:" + e.getLocalizedMessage(), e);
     }
     return initDataMap;
   }
 }