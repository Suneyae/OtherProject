 package cn.sinobest.framework.dao.workflow;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.service.workflow.IWorkflow.IsReturn;
 import cn.sinobest.framework.service.workflow.IWorkflow.ProcInsState;
 import cn.sinobest.framework.service.workflow.IWorkflow.ProcStartOrEnd;
 import cn.sinobest.framework.service.workflow.IWorkflow.RightMsg;
 import cn.sinobest.framework.service.workflow.IWorkflow.WorkItemState;
 import cn.sinobest.framework.service.workflow.IWorkflow.WorkItemType;
 import cn.sinobest.framework.util.ConfUtil;
 import cn.sinobest.framework.util.DateUtil.CurDate;
 import cn.sinobest.framework.util.Util;
 import java.sql.SQLException;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import org.apache.commons.logging.Log;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.orm.ibatis3.SqlSessionTemplate;
 import org.springframework.orm.ibatis3.support.SqlSessionDaoSupport;
 import org.springframework.stereotype.Repository;
 
 @Repository
 public class WorkflowDAO
   extends SqlSessionDaoSupport
   implements IWorkflowDAO
 {
   private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowDAO.class);
   private SqlSessionTemplate session = getSqlSessionTemplate();
   
   public boolean cancelBrotherWorkItem(String pInsId, Long curWorkItemId, String oprId)
     throws Exception
   {
     try
     {
       WfWorkItem workitem = new WfWorkItem();
       workitem.setSTATE(IWorkflow.WorkItemState.WIS_CANCEL.getState());
       workitem.setCOMPLETE_TIME(Long.valueOf(DateUtil.CurDate.YYYYMMDDHHmmss.getDate()));
       workitem.setBAE007(pInsId);
       workitem.setWORK_ITEM_ID(curWorkItemId);
       
       updateWorkItem(pInsId, curWorkItemId, "");
       
       int resp = this.session.update("FW_CONFIG.WF_WORK_ITEM_U", workitem);
       if (resp == 0) {
         return false;
       }
       return true;
     }
     catch (Exception ex)
     {
       throw new Exception("取消所有兄弟环节出错,详细:" + ex.getMessage(), ex);
     }
   }
   
   public int checkBranchState(String pInsId, Long curWorkItemId)
     throws Exception
   {
     try
     {
       WfWorkItem workitem = new WfWorkItem();
       workitem.setBAE007(pInsId);
       workitem.setWORK_ITEM_ID(curWorkItemId);
       workitem.setSTATE(IWorkflow.WorkItemState.WIS_END.getState());
       
       Integer cnt = (Integer)this.session.selectOne("FW_CONFIG.WF_WORK_ITEM_CNT", workitem);
       
       return cnt.intValue();
     }
     catch (Exception ex)
     {
       throw new Exception("检查分支环节的兄弟环节是否已完成出错", ex);
     }
   }
   
   public final String checkProcessState(String pInsId)
     throws Exception
   {
     try
     {
       WfProcessInstance process = getWfProcessInstanceById(pInsId);
       return process.getSTATUS();
     }
     catch (Exception ex)
     {
       ex.printStackTrace();
       throw new Exception("检查流程状态出错,流程ID:" + pInsId + ",详细:" + ex.getMessage(), ex);
     }
   }
   
   public Map<String, String> checkWorkItemInsState(Long curWorkItemId)
     throws Exception
   {
     HashMap<String, String> resp = null;
     try
     {
       Map<String, String> url = getUrl(curWorkItemId);
       WfWorkItem workitem = (WfWorkItem)this.session.selectOne("FW_CONFIG.WF_WORK_ITEM_Q", curWorkItemId);
       resp = new HashMap();
       resp.put("state", workitem.getSTATE());
       resp.put("_rtnURL", (String)url.get(IWorkflow.RightMsg.URL.toString()));
       return resp;
     }
     catch (Exception ex)
     {
       throw new Exception("检查环节状态出错,环节ID:" + curWorkItemId + ",详细:" + ex.getMessage(), ex);
     }
   }
   
   public Map<String, String> getUrl(Long curWorkItemId)
     throws Exception
   {
     Map<String, Object> params = new HashMap();
     params.put("RIGHTTYPE", ConfUtil.getDictCodeByName("RIGHTTYPE", "流程权限"));
     params.put("WORK_ITEM_ID", curWorkItemId);
     return (Map)this.session.selectOne("FW_CONFIG.FW_RIGHT_Q2", params);
   }
   
   public Map<String, String> getUrl(String actionDefId)
     throws Exception
   {
     Map<String, Object> params = new HashMap();
     params.put("RIGHTTYPE", ConfUtil.getDictCodeByName("RIGHTTYPE", "流程权限"));
     params.put("ACTION_DEF_ID", actionDefId);
     return (Map)this.session.selectOne("FW_CONFIG.FW_RIGHT_Q3", params);
   }
   
   public boolean completeWorkItem(String pInsId, Long curWorkItemId, String oprId, String BZ)
     throws Exception
   {
     try
     {
       WfWorkItem workitem = new WfWorkItem();
       workitem.setSTATE(IWorkflow.WorkItemState.WIS_END.getState());
       workitem.setCOMPLETE_TIME(Long.valueOf(DateUtil.CurDate.YYYYMMDDHHmmss.getDate()));
       workitem.setMEMO(BZ);
       workitem.setOPERID(oprId);
       workitem.setWORK_ITEM_ID(curWorkItemId);
       int resp = this.session.update("FW_CONFIG.WF_WORK_ITEM_U", workitem);
       
       return resp > 0;
     }
     catch (Exception ex)
     {
       throw new Exception("结束环节时出错,详情:" + ex.getMessage(), ex);
     }
   }
   
   public final Long creatWorkItem(String pInsId, String processDefId, String actionDefId, String workItemType, Map<String, Object> params, String isReturn, String oprId)
     throws Exception
   {
     try
     {
       if (pInsId == null) {
         throw new Exception("流程实例ID不能为空!");
       }
       WfActionDef wfActDef = getWfByActionDef(processDefId, actionDefId);
       
       Long wid = getWorkItemId();
       String bae006 = (String)params.get("_bae006");
       String unitIdStr = (String)params.get("_unitId");
       Long unitId = Util.isEmpty(unitIdStr) ? null : Long.valueOf(Long.parseLong(unitIdStr));
       if (!Util.isEmpty(oprId))
       {
         Map<String, Object> oprMsgMap = getOprMsgByOprId(oprId);
         bae006 = (String)oprMsgMap.get("BAE001");
         unitId = oprMsgMap.get("OPERUNITID") != null ? Long.valueOf(((Number)oprMsgMap.get("OPERUNITID")).longValue()) : null;
       }
       String preId = (String)params.get("wid");
       WfWorkItem workitem = new WfWorkItem();
       workitem.setACTION_DEF_ID(wfActDef.getACTION_DEF_ID());
       workitem.setWORK_ITEM_ID(wid);
       workitem.setBAE007(pInsId);
       workitem.setACTION_DEF_NAME(wfActDef.getACTION_DEF_NAME());
       workitem.setSTATE(IWorkflow.WorkItemState.WIS_READY.getState());
       workitem.setPRE_WI_ID(Util.isEmpty(preId) ? null : Long.valueOf(Long.parseLong(preId)));
       workitem.setWORK_TYPE(workItemType);
       workitem.setIS_RETURN(isReturn);
       workitem.setOPERID(oprId);
       workitem.setX_OPRATOR_IDS(getXOpr(pInsId, processDefId, wfActDef.getACTION_DEF_ID(), wfActDef.getACTION_DEF_ID(), (String)params.get("_bae006"), false, false, null));
       workitem.setSTART_TIME(Long.valueOf(DateUtil.CurDate.YYYYMMDDHHmmss.getDate()));
       workitem.setCOMPLETE_TIME(null);
       workitem.setFILTER_OPR(oprId);
       workitem.setAAE100(ConfUtil.getDict("YESORNO", "YES"));
       workitem.setBAE002((String)params.get("_operId"));
       workitem.setBAE003(Long.valueOf(DateUtil.CurDate.YYYYMMDDHHmmss.getDate()));
       workitem.setBAE006(bae006);
       workitem.setMEMO(null);
       workitem.setYWBAE007(null);
       workitem.setOPERUNITID(unitId);
       
       insertWorkitemIns(workitem);
       
       params.put("pid", pInsId.toString());
       params.put("wid", String.valueOf(wid));
       
       return wid;
     }
     catch (Exception ex)
     {
       throw new Exception("创建环节出错,详情：" + ex.getMessage(), ex);
     }
   }
   
   public List<WfActionDef> getAllProcessDef(String processDefId)
     throws Exception
   {
     try
     {
       return ConfUtil.getAllWfConf(processDefId);
     }
     catch (Exception ex)
     {
       ex.printStackTrace();
       throw new Exception("获取环节定义信息出错，详细：" + ex.getMessage(), ex);
     }
   }
   
   public final String getBackOprId(Long curWorkItemId, String nextActionDefId)
     throws Exception
   {
     try
     {
       WfWorkItem workitem = new WfWorkItem();
       workitem.setACTION_DEF_ID(nextActionDefId);
       workitem.setWORK_ITEM_ID(curWorkItemId);
       return (String)this.session.selectOne("FW_CONFIG.WF_WORK_ITEM_Q_OPR", workitem);
     }
     catch (Exception ex)
     {
       throw new Exception("获取上一环节的操作人出错,详细:" + ex.getMessage(), ex);
     }
   }
   
   public List<List<String>> getBackOprs(String nextActDefID, String pInsId, Long curWorkItemId)
     throws Exception
   {
     try
     {
       if (nextActDefID.startsWith("!!"))
       {
         WfWorkItem workitem = new WfWorkItem();
         workitem.setBAE007(pInsId);
         workitem.setACTION_DEF_ID(nextActDefID);
         return this.session.selectList("FW_CONFIG.WF_WORK_ITEM_Q_OPRNAME", workitem);
       }
       return null;
     }
     catch (Exception ex)
     {
       throw new Exception("找出原办理人时出错!" + ex.getMessage(), ex);
     }
   }
   
   public List<String[]> getReceivers4Ins(String processDefId, String curActDefID, String pInsId, long curWorkItemId, String orgCode, String nextActDefID, boolean cascade, String jbr, String unitId, boolean isRight, boolean toUnitOpr)
     throws Exception
   {
     String xOprIds = "0";
     List<String[]> oprIds = null;
     List<Map<String, String>> oprIdsMap = null;
     
 
     StringBuffer orgWhere = new StringBuffer(" and A.BAE001 ");
     boolean orgWhereFlag = false;
     try
     {
       if ((orgCode != null) && (orgCode.trim().length() > 0))
       {
         orgWhereFlag = true;
         if (cascade) {
           orgWhere.append(" LIKE '%" + orgCode + "%'");
         } else if (orgCode.endsWith("00")) {
           orgWhere.append(" IN ('" + orgCode + "','" + orgCode.substring(0, orgCode.length() - 2) + "')");
         } else {
           orgWhere.append(" = '" + orgCode + "'");
         }
       }
       if ((nextActDefID != null) && (nextActDefID.trim().length() > 0))
       {
         xOprIds = getXOpr(pInsId, processDefId, curActDefID, nextActDefID, orgCode, cascade, isRight, jbr);
         xOprIds = xOprIds.length() > 1 ? xOprIds.substring(1, xOprIds.length() - 1) : xOprIds;
       }
       if ((xOprIds == null) || ("".equals(xOprIds))) {
         xOprIds = "0";
       }
       Map<String, String> params = new HashMap(3);
       params.put("XOPERID", xOprIds);
       params.put("ACTION_DEF_ID", nextActDefID.startsWith("!!") ? nextActDefID.substring(2) : nextActDefID);
       if (orgWhereFlag) {
         params.put("ORGWHERE", orgWhere.toString());
       }
       if ((toUnitOpr) && (!Util.isEmpty(unitId))) {
         params.put("OPERUNITID", unitId);
       }
       oprIdsMap = this.session.selectList("FW_CONFIG.FW_OPERATOR_Q", params);
       
       oprIds = new ArrayList();
       for (Iterator itor = oprIdsMap.iterator(); itor.hasNext();)
       {
         Map<String, String> tmpItem = (Map)itor.next();
         String[] oprIdName = { (String)tmpItem.get("OPERID"), (String)tmpItem.get("OPERNAME") };
         oprIds.add(oprIdName);
       }
       return oprIds;
     }
     catch (Exception ex)
     {
       throw new Exception("在获取操作员时出错!详情:" + ex.getMessage(), ex);
     }
   }
   
   private Map<String, Object> getOprMsgByOprId(String OprId)
   {
     return (Map)this.session.selectOne("FW_CONFIG.FW_OPERATOR_Q2", OprId);
   }
   
   public WfActionDef getWfByActionDef(String processDefId, String actionDefId)
     throws Exception
   {
     try
     {
       return ConfUtil.getWfConf(processDefId + "|" + actionDefId);
     }
     catch (Exception ex)
     {
       throw new Exception("获取环节定义信息出错，详细：" + ex.getMessage(), ex);
     }
   }
   
   public WfActionDef getWfStartOrEnd(String processDefId, String state)
     throws Exception
   {
     try
     {
       List<WfActionDef> wfDefs = getAllProcessDef(processDefId);
       for (WfActionDef wfDef : wfDefs) {
         if (state.equalsIgnoreCase(wfDef.getSTART_OR_END())) {
           return wfDef;
         }
       }
       WfActionDef wfActDef = new WfActionDef();
       wfActDef.setPROCESS_DEF_ID(processDefId);
       wfActDef.setSTART_OR_END(state);
       return (WfActionDef)this.session.selectOne("FW_CONFIG.WF_ACTION_DEF_Q2", wfActDef);
     }
     catch (Exception ex)
     {
       throw new Exception("获取流程定义的开始坏节定义出错,详细:" + ex.getMessage(), ex);
     }
   }
   
   public WfWorkItem getWorkItem(Long curWorkItemId)
     throws Exception
   {
     try
     {
       return (WfWorkItem)this.session.selectOne("FW_CONFIG.WF_WORK_ITEM_Q", curWorkItemId);
     }
     catch (Exception ex)
     {
       throw new Exception("获取环节实例" + curWorkItemId + "信息出错，详细：" + ex.getMessage(), ex);
     }
   }
   
   public Map<String, String> getPreOpr(String pInsId, String curWorkDefId, String rightWrokDefId, String rightFlag)
     throws Exception
   {
     try
     {
       Map<String, String> args = new HashMap(2);
       args.put("C_ACTION_DEF_ID", curWorkDefId);
       args.put("R_ACTION_DEF_ID", rightWrokDefId);
       args.put("W_BAE007", pInsId);
       args.put("RIGHTFLAG", rightFlag);
       return (Map)this.session.selectOne("FW_CONFIG.WF_WORK_ITEM_Q_PRE_OPERID", args);
     }
     catch (Exception ex)
     {
       throw new Exception("回退时获取上一环节经办人出错，详细：" + ex.getMessage(), ex);
     }
   }
   
   public List<WfWorkItem> getWorkItems(String pInsId)
     throws Exception
   {
     return this.session.selectList("FW_CONFIG.WF_WORK_ITEM_Q2", pInsId);
   }
   
   public WfProcessInstance getWfProcessInstanceById(String pid)
     throws Exception
   {
     try
     {
       return (WfProcessInstance)this.session.selectOne("FW_CONFIG.WF_PROCESS_INSTANCE_Q", pid);
     }
     catch (Exception ex)
     {
       throw new Exception("获取流程实例" + pid + "信息出错，详细：" + ex.getMessage(), ex);
     }
   }
   
   public final String getXOpr(String pInsId, String processDefId, String curActionDefId, String nextActionDefIDs, String orgCode, boolean cascade, boolean isRight, String jbr)
     throws Exception
   {
     StringBuffer resOprs = new StringBuffer(",");
     String[] xActDefIds = (String[])null;
     WfActionDef wfActionDef = null;
     String xAcDefIds = "";
     
     StringBuffer inWhere = new StringBuffer("'-1'");
     try
     {
       if (Util.isEmpty(pInsId))
       {
         wfActionDef = getWfByActionDef(processDefId, curActionDefId);
         xAcDefIds = wfActionDef.getX_ACTION_DEF_IDS();
         if ((xAcDefIds == null) || (xAcDefIds.trim().length() == 0)) {
           return "";
         }
         xActDefIds = xAcDefIds.split(",");
         for (int i = 0; i < xActDefIds.length; i++) {
           if (nextActionDefIDs.indexOf(xActDefIds[i]) > -1) {
             return "," + jbr + ",";
           }
         }
         return "";
       }
       wfActionDef = getWfByActionDef(processDefId, nextActionDefIDs);
       xAcDefIds = wfActionDef.getX_ACTION_DEF_IDS();
       if ((xAcDefIds == null) || (xAcDefIds.trim().length() == 0)) {
         return "";
       }
       xActDefIds = xAcDefIds.split(",");
       for (int i = 0; i < xActDefIds.length; i++) {
         inWhere.append(",").append("'").append(xActDefIds[i]).append("'");
       }
       Map<String, String> params = new HashMap();
       params.put("BAE007", pInsId);
       params.put("STATE", IWorkflow.WorkItemState.WIS_CANCEL.getState());
       params.put("WORK_ITEM_ID", inWhere.toString());
       
       List opr = this.session.selectList("FW_CONFIG.WF_WORK_ITEM_Q_XOPR", params);
       for (Iterator itor = opr.iterator(); itor.hasNext();)
       {
         String oprId = (String)itor.next();
         if ((!isRight) || (oprId == null) || (!oprId.equals(jbr))) {
           resOprs.append(oprId).append(",");
         }
       }
       return resOprs.length() == 1 ? "" : resOprs.toString();
     }
     catch (Exception ex)
     {
       throw new Exception("在获取互斥操作员时出错!详情:" + ex.getMessage(), ex);
     }
   }
   
   public boolean hasActionRight(String processDefId, String curActDefID, String pInsId, long curWorkItemId, String jbr, String unitId, boolean toUnitOpr)
     throws Exception
   {
     try
     {
       List<String[]> receJbr = null;
       String proceDefId = processDefId;
       if (Util.isEmpty(processDefId))
       {
         WfProcessInstance wfProcess = getWfProcessInstanceById(pInsId);
         proceDefId = wfProcess.getPROCESS_DEF_ID();
       }
       receJbr = getReceivers4Ins(proceDefId, curActDefID, pInsId, curWorkItemId, null, curActDefID, false, jbr, unitId, false, toUnitOpr);
       for (int i = 0; i < receJbr.size(); i++) {
         if (((String[])receJbr.get(i))[0].equals(jbr)) {
           return true;
         }
       }
       return false;
     }
     catch (Exception ex)
     {
       ex.printStackTrace();
       throw new Exception("判断操作员是否具备相关环节的执行权限时错,详细:" + ex.getMessage(), ex);
     }
   }
   
   public Map<String, String> startProcess(String processDefId, Map<String, Object> params)
     throws Exception
   {
     String msg = "";
     try
     {
       WfActionDef wfDef = getWfStartOrEnd(processDefId, IWorkflow.ProcStartOrEnd.PSE_START.getState());
       if (wfDef == null)
       {
         msg = "未找到定义为'" + processDefId + "'的流程定义!";
         this.logger.error(msg);
         throw new Exception(msg);
       }
       Long wid = getWorkItemId();
       String pid = getYwlsStr(wfDef.getATTR(), (String)params.get("pid"), params);
       String jbr = (String)params.get("_operId");
       String bae006 = (String)params.get("_bae006");
       
       String unitIdStr = (String)params.get("_unitId");
       Long unitId = Util.isEmpty(unitIdStr) ? null : Long.valueOf(Long.parseLong(unitIdStr));
       String itemState = IWorkflow.WorkItemState.WIS_READY.getState();
       String procState = IWorkflow.ProcInsState.PIS_DOING.getState();
       if (!Util.isEmpty(jbr))
       {
         itemState = IWorkflow.WorkItemState.WIS_DOING.getState();
         
         Map<String, Object> oprMsgMap = getOprMsgByOprId(jbr);
         bae006 = (String)oprMsgMap.get("BAE001");
         unitId = oprMsgMap.get("OPERUNITID") != null ? Long.valueOf(((Number)oprMsgMap.get("OPERUNITID")).longValue()) : null;
       }
       WfProcessInstance process = new WfProcessInstance();
       
       process.setBAE007(pid);
       process.setPROCESS_DEF_ID(wfDef.getPROCESS_DEF_ID());
       process.setPROCESS_DEF_NAME(wfDef.getPROCESS_DEF_NAME());
       process.setSTATUS(procState);
       process.setPROCESS_KEY_INFO((String)params.get("_keyData"));
       
       process.setAAB001(Util.isEmpty((String)params.get("_aab001")) ? null : Long.valueOf(Long.parseLong((String)params.get("_aab001"))));
       process.setAAC001(Util.isEmpty((String)params.get("_aac001")) ? null : Long.valueOf(Long.parseLong((String)params.get("_aac001"))));
       process.setARCHIVEID((String)params.get("_archiveId"));
       process.setREJECT_REASON((String)params.get("_comment"));
       process.setPARENTINSID((String)params.get("ppid"));
       
       process.setRELATEINSID((String)params.get("rid"));
       process.setPREVIOUS_WI_ID(null);
       process.setCURRENT_WI_ID(wid);
       process.setCOMPLETE_TIME(null);
       process.setBAE002(jbr);
       
       process.setBAE003(Long.valueOf(DateUtil.CurDate.YYYYMMDDHHmmss.getDate()));
       process.setBAE006((String)params.get("_bae006"));
       process.setYWBAE007(null);
       
 
 
       insertProcessIns(process);
       
       WfWorkItem wfWorkItem = new WfWorkItem();
       wfWorkItem.setWORK_ITEM_ID(wid);
       wfWorkItem.setBAE007(pid);
       wfWorkItem.setACTION_DEF_ID(wfDef.getACTION_DEF_ID());
       wfWorkItem.setACTION_DEF_NAME(wfDef.getACTION_DEF_NAME());
       wfWorkItem.setSTATE(itemState);
       
       wfWorkItem.setWORK_TYPE(IWorkflow.WorkItemType.WIT_SINGLE.getState());
       wfWorkItem.setIS_RETURN(IWorkflow.IsReturn.N.getState());
       wfWorkItem.setOPERID(jbr);
       wfWorkItem.setPRE_WI_ID(null);
       
       wfWorkItem.setSTART_TIME(Long.valueOf(DateUtil.CurDate.YYYYMMDDHHmmss.getDate()));
       wfWorkItem.setX_OPRATOR_IDS("," + jbr + ",");
       wfWorkItem.setCOMPLETE_TIME(null);
       wfWorkItem.setFILTER_OPR(jbr);
       
       wfWorkItem.setAAE100(ConfUtil.getDict("YESORNO", "YES"));
       wfWorkItem.setBAE002((String)params.get("_operId"));
       wfWorkItem.setBAE006(bae006);
       wfWorkItem.setBAE003(Long.valueOf(DateUtil.CurDate.YYYYMMDDHHmmss.getDate()));
       wfWorkItem.setMEMO((String)params.get("_comment"));
       wfWorkItem.setYWBAE007(null);
       wfWorkItem.setOPERUNITID(unitId);
       
 
       insertWorkitemIns(wfWorkItem);
       
 
       Map<String, String> respMap = new HashMap();
       Map<String, String> url = getUrl(wfDef.getACTION_DEF_ID());
       params.put("_rtnURL", url.get(IWorkflow.RightMsg.URL.toString()));
       respMap.put("pid", pid);
       respMap.put("wid", String.valueOf(wid));
       return respMap;
     }
     catch (Exception ex)
     {
       throw new Exception("启动流程失败,详情:" + ex.getMessage(), ex);
     }
   }
   
   public boolean startWorkItem(String pInsId, Long preWorkItemId, Long curWorkItemId, String oprId, WfWorkItem workItemArg)
     throws Exception
   {
     try
     {
       WfWorkItem workitem = new WfWorkItem();
       workitem.setSTATE(IWorkflow.WorkItemState.WIS_DOING.getState());
       workitem.setSTART_TIME(Long.valueOf(DateUtil.CurDate.YYYYMMDDHHmmss.getDate()));
       workitem.setOPERID(oprId);
       workitem.setWORK_ITEM_ID(curWorkItemId);
       workitem.setPRE_WI_ID(preWorkItemId);
       if ((this.session.update("FW_CONFIG.WF_WORK_ITEM_U", workitem) <= 0) && (Util.isEmpty(workItemArg.getOPERID()))) {
         throw new Exception("更改任务状态失败，可能原因：此任务可能已被其他人接收，请刷新任务列表，谢谢!");
       }
       updateProcess(pInsId, preWorkItemId, curWorkItemId, null, null, null, oprId);
       updateWorkItem(pInsId, curWorkItemId, oprId);
       return true;
     }
     catch (SQLException ex)
     {
       throw new Exception("开始环节时出错,详情:" + ex.getMessage());
     }
   }
   
   public boolean stopProcess(String pInsId, Map<String, Object> params)
     throws Exception
   {
     try
     {
       WfProcessInstance process = new WfProcessInstance();
       process.setSTATUS(IWorkflow.ProcInsState.PIS_END.getState());
       process.setPROCESS_KEY_INFO((String)params.get("_keyData"));
       process.setREJECT_REASON((String)params.get("_comment"));
       process.setCURRENT_WI_ID(params.get("pid") == null ? null : (Long)params.get("pid"));
       process.setCOMPLETE_TIME(Long.valueOf(DateUtil.CurDate.YYYYMMDDHHmmss.getDate()));
       process.setBAE007(pInsId);
       
       this.session.update("FW_CONFIG.WF_PROCESS_INSTANCE_U", process);
       return true;
     }
     catch (Exception ex)
     {
       throw new Exception("结束流程时出错，详细：" + ex.getMessage(), ex);
     }
   }
   
   public boolean updateProcess(String pInsId, Long preWorkItemId, Long curWorkItemId, String processState, String keyData, String rejectReason, String oprId)
     throws Exception
   {
     try
     {
       String compleateTime = null;
       if ((IWorkflow.ProcInsState.PIS_END.getState().equals(processState)) || 
         (IWorkflow.ProcInsState.PIS_PROC_BACK.getState().equals(processState))) {
         compleateTime = DateUtil.CurDate.YYYYMMDDHHmmss.getDate();
       }
       WfProcessInstance process = new WfProcessInstance();
       process.setBAE007(pInsId);
       if ((preWorkItemId != null) && (preWorkItemId.longValue() > 0L)) {
         process.setPREVIOUS_WI_ID(preWorkItemId);
       }
       if ((curWorkItemId != null) && (curWorkItemId.longValue() > 0L)) {
         process.setCURRENT_WI_ID(curWorkItemId);
       }
       if ((processState != null) && (processState.trim().length() > 0)) {
         process.setSTATUS(processState);
       }
       if ((keyData != null) && (keyData.trim().length() > 0)) {
         process.setPROCESS_KEY_INFO(keyData);
       }
       if ((compleateTime != null) && (compleateTime.trim().length() > 0)) {
         process.setCOMPLETE_TIME(Long.valueOf(compleateTime));
       }
       if ((rejectReason != null) && (rejectReason.trim().length() > 0)) {
         process.setREJECT_REASON(rejectReason);
       }
       this.session.update("FW_CONFIG.WF_PROCESS_INSTANCE_U", process);
       return true;
     }
     catch (Exception ex)
     {
       throw new Exception("更新流程状态时出错,详细:" + ex.getMessage(), ex);
     }
   }
   
   public boolean updateWorkItem(String pInsId, Long curWorkItemId, String oprId)
     throws Exception
   {
     try
     {
       WfWorkItem workitem = new WfWorkItem();
       workitem.setFILTER_OPR(oprId);
       workitem.setBAE007(pInsId);
       workitem.setWORK_ITEM_ID(curWorkItemId);
       
       int resp = this.session.update("FW_CONFIG.WF_WORK_ITEM_U", workitem);
       if (resp > 0) {
         return true;
       }
       return false;
     }
     catch (Exception ex)
     {
       throw new Exception("更新过滤操作员出错,详细:" + ex.getMessage(), ex);
     }
   }
   
   public void cancelFilter(String pInsId, WfWorkItem wfWorkItem)
     throws Exception
   {
     this.session.update("FW_CONFIG.WF_WORK_ITEM_U4", wfWorkItem);
   }
   
   public boolean updateWorkItem(WfWorkItem wfWorkItem)
     throws Exception
   {
     try
     {
       int resp = this.session.update("FW_CONFIG.WF_WORK_ITEM_U", wfWorkItem);
       if (resp > 0) {
         return true;
       }
       return false;
     }
     catch (Exception ex)
     {
       throw new Exception("更新流程实例信息,详细:" + ex.getMessage(), ex);
     }
   }
   
   public Long getWorkItemId()
   {
     return (Long)this.session.selectOne("FW_CONFIG.SEQ_WF_WORK_ITEM");
   }
   
   public Long getProcessId()
   {
     return (Long)this.session.selectOne("FW_CONFIG.SEQ_WF_PROCESS_INSTANCE");
   }
   
   public Long getYwls(String seq)
   {
     Map<String, String> arg = new HashMap(1);
     arg.put("SEQ_YWLS", seq);
     return (Long)this.session.selectOne("FW_CONFIG.SEQ_WF_YWLS", arg);
   }
   
   public String getYwlsStr(String attrs, String pid, Map<String, Object> args)
   {
     String cusYwlsh = (String)Util.nvl(Util.searchByKey("useCusYwlsh", attrs));
     if (Boolean.parseBoolean(cusYwlsh)) {
       return pid;
     }
     String prefix = (String)args.get("_prefix");
     String length = Util.searchByKey("length", attrs);
     String seq = Util.searchByKey("seq", attrs);
     Long ywls = null;
     if (!Util.isEmpty(seq)) {
       ywls = getYwls(seq);
     } else {
       ywls = getProcessId();
     }
     if (ywls == null) {
       throw new AppException("未获取到业务关联流水序列值，参数值：" + attrs);
     }
     if (Util.isEmpty(prefix)) {
       prefix = (String)Util.nvl(Util.searchByKey("prefix", attrs));
     }
     StringBuffer ywlsStr = new StringBuffer((String)Util.nvl(prefix));
     if (!Util.isEmpty(length)) {
       ywlsStr.append(Util.lpad(String.valueOf(ywls), Integer.valueOf(length).intValue(), "0", false));
     } else {
       ywlsStr.append(String.valueOf(ywls));
     }
     return ywlsStr.toString();
   }
   
   public void insertProcessIns(WfProcessInstance wfProcessIns)
     throws Exception
   {
     int n = 0;
     this.logger.info("生成流程实例....");
     n = this.session.insert("FW_CONFIG.WF_PROCESS_INSTANCE_I", wfProcessIns);
     if (n > 0) {
       this.logger.info("生成流程实例成功,流水号(pid)为:" + wfProcessIns.getBAE007());
     } else {
       throw new AppException("生成流程实例失败");
     }
   }
   
   public void insertWorkitemIns(WfWorkItem wfWorkItem)
     throws Exception
   {
     int n = 0;
     this.logger.info("生成环节实例....");
     n = this.session.insert("FW_CONFIG.WF_WORK_ITEM_I", wfWorkItem);
     if (n > 0) {
       this.logger.info("生成环节实例成功,环节号(wid)为:" + wfWorkItem.getWORK_ITEM_ID());
     } else {
       throw new AppException("生成环节实例失败");
     }
   }
   
   public String getWfDefIdByName(String wfName)
     throws Exception
   {
     String wfDefId = (String)this.session.selectOne("FW_CONFIG.WF_ACTION_DEF_Q3", wfName);
     return wfDefId;
   }
   
   public String getWfDefNameByRightId(String rightId)
     throws Exception
   {
     String wfDefName = (String)this.session.selectOne("FW_CONFIG.FW_RIGHT_Q4", rightId);
     return wfDefName;
   }
   
   public String getRightIdByWfDefId(String wfDefId)
     throws Exception
   {
     return (String)this.session.selectOne("FW_CONFIG.FW_RIGHT_Q5", wfDefId);
   }
   
   public String getRightIdByPid(String pid)
     throws Exception
   {
     return (String)this.session.selectOne("FW_CONFIG.FW_RIGHT_Q6", pid);
   }
   
   public int setWorkItemState(WfWorkItem wfWorkItem)
     throws Exception
   {
     return this.session.update("FW_CONFIG.WF_WORK_ITEM_U2", wfWorkItem);
   }
   
   public int setWfState(WfProcessInstance wfProcessInstance)
     throws Exception
   {
     return this.session.update("FW_CONFIG.WF_PROCESS_INSTANCE_U2", wfProcessInstance);
   }
   
   public WfWorkItem getWorkItemInsByDefId(String pid, String workItemDefId)
     throws Exception
   {
     Map<String, Object> args = new HashMap();
     args.put("W_BAE007", pid);
     args.put("W_ACTION_DEF_ID", workItemDefId);
     return (WfWorkItem)this.session.selectOne("FW_CONFIG.WF_WORK_ITEM_Q6", args);
   }
   
   public boolean cancelFilter(String pInsId, String wfItemDefId, String oprId)
     throws Exception
   {
     try
     {
       WfWorkItem workitem = new WfWorkItem();
       workitem.setFILTER_OPR(oprId);
       workitem.setBAE007(pInsId);
       workitem.setACTION_DEF_ID(wfItemDefId);
       workitem.setSTATE(IWorkflow.WorkItemState.WIS_END.getState());
       int resp = this.session.update("FW_CONFIG.WF_WORK_ITEM_U5", workitem);
       if (resp > 0) {
         return true;
       }
       return false;
     }
     catch (Exception ex)
     {
       throw new Exception("更新过滤操作员出错,详细:" + ex.getMessage(), ex);
     }
   }
 }