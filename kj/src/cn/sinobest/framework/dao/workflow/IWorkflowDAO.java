package cn.sinobest.framework.dao.workflow;

import java.util.List;
import java.util.Map;

public abstract interface IWorkflowDAO
{
  public abstract WfActionDef getWfStartOrEnd(String paramString1, String paramString2)
    throws Exception;
  
  public abstract WfActionDef getWfByActionDef(String paramString1, String paramString2)
    throws Exception;
  
  public abstract List<WfActionDef> getAllProcessDef(String paramString)
    throws Exception;
  
  public abstract WfWorkItem getWorkItem(Long paramLong)
    throws Exception;
  
  public abstract List<WfWorkItem> getWorkItems(String paramString)
    throws Exception;
  
  public abstract WfProcessInstance getWfProcessInstanceById(String paramString)
    throws Exception;
  
  public abstract Map<String, String> startProcess(String paramString, Map<String, Object> paramMap)
    throws Exception;
  
  public abstract Long creatWorkItem(String paramString1, String paramString2, String paramString3, String paramString4, Map<String, Object> paramMap, String paramString5, String paramString6)
    throws Exception;
  
  public abstract boolean stopProcess(String paramString, Map<String, Object> paramMap)
    throws Exception;
  
  public abstract boolean completeWorkItem(String paramString1, Long paramLong, String paramString2, String paramString3)
    throws Exception;
  
  public abstract String checkProcessState(String paramString)
    throws Exception;
  
  public abstract Map<String, String> checkWorkItemInsState(Long paramLong)
    throws Exception;
  
  public abstract boolean cancelBrotherWorkItem(String paramString1, Long paramLong, String paramString2)
    throws Exception;
  
  public abstract boolean updateWorkItem(String paramString1, Long paramLong, String paramString2)
    throws Exception;
  
  public abstract void cancelFilter(String paramString, WfWorkItem paramWfWorkItem)
    throws Exception;
  
  public abstract boolean updateWorkItem(WfWorkItem paramWfWorkItem)
    throws Exception;
  
  public abstract boolean startWorkItem(String paramString1, Long paramLong1, Long paramLong2, String paramString2, WfWorkItem paramWfWorkItem)
    throws Exception;
  
  public abstract String getBackOprId(Long paramLong, String paramString)
    throws Exception;
  
  public abstract boolean updateProcess(String paramString1, Long paramLong1, Long paramLong2, String paramString2, String paramString3, String paramString4, String paramString5)
    throws Exception;
  
  public abstract String getXOpr(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, boolean paramBoolean1, boolean paramBoolean2, String paramString6)
    throws Exception;
  
  public abstract List<List<String>> getBackOprs(String paramString1, String paramString2, Long paramLong)
    throws Exception;
  
  public abstract List<String[]> getReceivers4Ins(String paramString1, String paramString2, String paramString3, long paramLong, String paramString4, String paramString5, boolean paramBoolean1, String paramString6, String paramString7, boolean paramBoolean2, boolean paramBoolean3)
    throws Exception;
  
  public abstract boolean hasActionRight(String paramString1, String paramString2, String paramString3, long paramLong, String paramString4, String paramString5, boolean paramBoolean)
    throws Exception;
  
  public abstract int checkBranchState(String paramString, Long paramLong)
    throws Exception;
  
  public abstract void insertProcessIns(WfProcessInstance paramWfProcessInstance)
    throws Exception;
  
  public abstract void insertWorkitemIns(WfWorkItem paramWfWorkItem)
    throws Exception;
  
  public abstract Map<String, String> getUrl(Long paramLong)
    throws Exception;
  
  public abstract Map<String, String> getUrl(String paramString)
    throws Exception;
  
  public abstract Long getWorkItemId();
  
  public abstract Long getProcessId();
  
  public abstract String getWfDefIdByName(String paramString)
    throws Exception;
  
  public abstract String getWfDefNameByRightId(String paramString)
    throws Exception;
  
  public abstract String getRightIdByWfDefId(String paramString)
    throws Exception;
  
  public abstract String getRightIdByPid(String paramString)
    throws Exception;
  
  public abstract int setWorkItemState(WfWorkItem paramWfWorkItem)
    throws Exception;
  
  public abstract int setWfState(WfProcessInstance paramWfProcessInstance)
    throws Exception;
  
  public abstract Map<String, String> getPreOpr(String paramString1, String paramString2, String paramString3, String paramString4)
    throws Exception;
  
  public abstract WfWorkItem getWorkItemInsByDefId(String paramString1, String paramString2)
    throws Exception;
  
  public abstract boolean cancelFilter(String paramString1, String paramString2, String paramString3)
    throws Exception;
}