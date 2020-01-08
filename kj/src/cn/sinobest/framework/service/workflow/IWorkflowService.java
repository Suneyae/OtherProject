package cn.sinobest.framework.service.workflow;

import cn.sinobest.framework.comm.iface.IDTO;
import cn.sinobest.framework.dao.workflow.WfActionDef;
import cn.sinobest.framework.dao.workflow.WfProcessInstance;
import cn.sinobest.framework.dao.workflow.WfWorkItem;
import java.util.List;
import java.util.Map;

public abstract interface IWorkflowService
{
  public abstract void cancelFilter(String paramString, WfWorkItem paramWfWorkItem)
    throws Exception;
  
  public abstract WfActionDef getWfByActionDef(String paramString1, String paramString2)
    throws Exception;
  
  public abstract Map<String, String> startWf(String paramString, Map<String, Object> paramMap)
    throws Exception;
  
  public abstract boolean startWorkItem(String paramString1, String paramString2, String paramString3, Map<String, Object> paramMap)
    throws Exception;
  
  public abstract boolean submitWorkItem(String paramString1, String paramString2, Map<String, Object> paramMap)
    throws Exception;
  
  public abstract Map<String, String> getActionForward(String paramString)
    throws Exception;
  
  public abstract Map<String, String> getActionForward(Long paramLong)
    throws Exception;
  
  public abstract WfWorkItem getActionDefId(Long paramLong)
    throws Exception;
  
  public abstract Map checkWorkItemInsState(Long paramLong)
    throws Exception;
  
  public abstract List<String[]> getReceivers4Ins(String paramString1, String paramString2, String paramString3, long paramLong, String paramString4, String paramString5, boolean paramBoolean1, String paramString6, String paramString7, boolean paramBoolean2, Map<String, Object> paramMap)
    throws Exception;
  
  public abstract WfActionDef getWfStartOrEnd(String paramString1, String paramString2)
    throws Exception;
  
  public abstract Map<String, String> createSubWf(String paramString, Long paramLong, Boolean paramBoolean)
    throws Exception;
  
  public abstract String getWfDefId(String paramString1, String paramString2, String paramString3)
    throws Exception;
  
  public abstract boolean submitWorkItem(IDTO paramIDTO)
    throws Exception;
  
  public abstract String getRightIdByWfDefId(String paramString)
    throws Exception;
  
  public abstract String getRightIdByPid(String paramString)
    throws Exception;
  
  public abstract void updateWorkItem(WfWorkItem paramWfWorkItem)
    throws Exception;
  
  public abstract WfProcessInstance getWfProcessInstanceById(String paramString)
    throws Exception;
  
  public abstract int wfSleep(String paramString1, String paramString2)
    throws Exception;
  
  public abstract int wfWake(String paramString1, String paramString2)
    throws Exception;
  
  public abstract Map<String, String> getPreOpr(String paramString1, String paramString2, String paramString3, String paramString4)
    throws Exception;
  
  public abstract Map<String, String> createWorkItemByDefId(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean, String paramString5)
    throws Exception;
}