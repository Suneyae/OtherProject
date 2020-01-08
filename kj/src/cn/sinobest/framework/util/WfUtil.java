 package cn.sinobest.framework.util;
 
 import cn.sinobest.framework.comm.dto.DTO;
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.dao.workflow.WorkflowDAO;
 import cn.sinobest.framework.service.CommService;
 import cn.sinobest.framework.service.workflow.IWorkflowService;
 import cn.sinobest.framework.service.workflow.StartWfParams;
 import java.util.HashMap;
 import java.util.Map;
 
 public class WfUtil
 {
   static final String SERVER_OBJECT = "object";
   static String SERVER_METHOD = "method";
   
   public static String startRelateWf(Map<String, Object> params)
     throws AppException
   {
     String RELATEINSID = (String)params.get("rid");
     if (Util.isEmpty(RELATEINSID)) {
       throw new AppException("未指定关联流程实例号,key=rid");
     }
     params.put("_wfState", "data");
     IDTO dto = new DTO();
     dto.setData(Util.mapClone(params));
     CommService commService = (CommService)Util.getBean("commService");
     Map<String, String> resp = commService.startWf(dto);
     return (String)resp.get("pid");
   }
   
   public static String createSubWf(String pInsId, Long curWorkItemId, Boolean isReturn)
     throws AppException
   {
     if (Util.isEmpty(pInsId)) {
       throw new AppException("未指定流水号!");
     }
     if ((curWorkItemId == null) || (curWorkItemId.longValue() == 0L)) {
       throw new AppException("未指定当前环节实例号!");
     }
     if (isReturn == null) {
       throw new AppException("未指定是否回退到上一环节!");
     }
     IWorkflowService wfService = (IWorkflowService)Util.getBean("workflowService");
     try
     {
       resp = wfService.createSubWf(pInsId, curWorkItemId, isReturn);
     }
     catch (Exception e)
     {
       Map<String, String> resp;
       throw new AppException("创建子流程失败,详细：" + e.getLocalizedMessage(), e);
     }
     Map<String, String> resp;
     return resp != null ? (String)resp.get("ppid") : "";
   }
   
   public static String getYwlsh()
     throws AppException
   {
     WorkflowDAO wfDAO = (WorkflowDAO)Util.getBean("workflowDAO");
     Long pid = wfDAO.getProcessId();
     if (pid == null) {
       throw new AppException("获取流水号失败!");
     }
     return String.valueOf(pid);
   }
   
   public static boolean submitWf(IDTO dto)
     throws AppException
   {
     try
     {
       CommService commService = (CommService)Util.getBean("commService");
       commService.submitWf(dto);
       return true;
     }
     catch (Exception e)
     {
       throw new AppException("提交任务失败!", e);
     }
   }
   
   public static String startWf(StartWfParams startWfParams)
     throws AppException
   {
     if ((startWfParams == null) || (startWfParams.getWfDefName() == null)) {
       throw new AppException("未指定流程定义名称");
     }
     if (Util.isEmpty(startWfParams.getBae006())) {
       throw new AppException("未指经办机构代码");
     }
     if (Util.isEmpty(startWfParams.getKeyData())) {
       throw new AppException("未指关键信息");
     }
     try
     {
       IWorkflowService wfService = (IWorkflowService)Util.getBean("workflowService");
       String wfDefId = wfService.getWfDefId(null, startWfParams.getWfDefName(), null);
       Map<String, Object> params = new HashMap();
       params.put("_keyData", startWfParams.getKeyData());
       params.put("_aab001", startWfParams.getAab001());
       params.put("_aac001", startWfParams.getAac001());
       params.put("_archiveId", startWfParams.getArchiveId());
       params.put("_bae006", startWfParams.getBae006());
       params.put("_comment", startWfParams.getComment());
       params.put("_operId", Util.nvl(startWfParams.getOperId()));
       params.put("ppid", startWfParams.getPpid());
       params.put("rid", startWfParams.getRid());
       params.put("_commDo", startWfParams.getCommDo());
       if (startWfParams.isDoCusService()) {
         params.put("_wfState", "data");
       } else {
         params.put("_wfState", "wf");
       }
       Map<String, String> respParams = wfService.startWf(wfDefId, params);
       return (String)respParams.get("pid");
     }
     catch (Exception e)
     {
       throw new AppException("开启流程失败,详细:" + e.getLocalizedMessage(), e);
     }
   }
   
   public static Map<String, String> createWorkItem(String pid, String workItemDefId, String jbr, String acceptor, boolean yJbr, String orgCode)
     throws Exception
   {
     if (Util.isEmpty(pid)) {
       throw new AppException("未指定流水号");
     }
     if (Util.isEmpty(workItemDefId)) {
       throw new AppException("未指定要复制的环节定义ID");
     }
     if (Util.isEmpty(jbr)) {
       throw new AppException("未指定经办人ID");
     }
     IWorkflowService wfService = (IWorkflowService)Util.getBean("workflowService");
     return wfService.createWorkItemByDefId(pid, workItemDefId, jbr, acceptor == null ? "" : acceptor, yJbr, orgCode);
   }
   
   public static int wfSleep(String pid, String wid)
   {
     try
     {
       IWorkflowService wfService = (IWorkflowService)Util.getBean("workflowService");
       return wfService.wfSleep(pid, wid);
     }
     catch (Exception ex)
     {
       throw new AppException("对流程做休眠操作失败，详细:" + ex.getLocalizedMessage(), ex);
     }
   }
   
   public static int wfWake(String pid, String wid)
   {
     try
     {
       IWorkflowService wfService = (IWorkflowService)Util.getBean("workflowService");
       return wfService.wfWake(pid, wid);
     }
     catch (Exception ex)
     {
       throw new AppException("对流程做唤醒操作失败，详细:" + ex.getLocalizedMessage(), ex);
     }
   }
 }