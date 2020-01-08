package cn.sinobest.framework.service.workflow;

import cn.sinobest.framework.comm.exception.AppException;
import cn.sinobest.framework.comm.iface.IDTO;
import cn.sinobest.framework.dao.workflow.IWorkflowDAO;
import cn.sinobest.framework.dao.workflow.WfActionDef;
import cn.sinobest.framework.dao.workflow.WfProcessInstance;
import cn.sinobest.framework.dao.workflow.WfWorkItem;
import cn.sinobest.framework.dao.workflow.WorkItemComparator;
import cn.sinobest.framework.util.ConfUtil;
import cn.sinobest.framework.util.DateUtil;
import cn.sinobest.framework.util.DateUtil.CurDate;
import cn.sinobest.framework.util.Util;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkflowService implements IWorkflowService {
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowService.class);
	private final ReentrantLock lock = new ReentrantLock();
	@Autowired
	private IWorkflowDAO workflowDAO;

	public WfWorkItem getActionDefId(Long workItemId) throws Exception {
		return this.workflowDAO.getWorkItem(workItemId);
	}

	public Map checkWorkItemInsState(Long curWorkItemId) throws Exception {
		if (curWorkItemId == null) {
			throw new Exception("传入的环节实例ID号不能为空!");
		}
		return this.workflowDAO.checkWorkItemInsState(curWorkItemId);
	}

	public Map<String, String> getActionForward(Long workItemId) throws Exception {
		return this.workflowDAO.getUrl(workItemId);
	}

	public Map<String, String> getActionForward(String curActionDefId) throws Exception {
		return this.workflowDAO.getUrl(curActionDefId);
	}

	public WfActionDef getWfByActionDef(String processDefId, String actionDefId) throws Exception {
		return this.workflowDAO.getWfByActionDef(processDefId, actionDefId);
	}

	public Map<String, String> startWf(String processDefId, Map<String, Object> params) throws Exception {
		return this.workflowDAO.startProcess(processDefId, params);
	}

	public boolean startWorkItem(String pInsId, String curWorkItemId, String oprId, Map<String, Object> args)
			throws Exception {
		String errMsg = "";
		if ((pInsId == null) || (pInsId.trim().length() == 0)) {
			errMsg = "传入的流程实例ID号为空!";
			LOGGER.error(errMsg);
			throw new Exception(errMsg);
		}
		if ((curWorkItemId == null) || (curWorkItemId.trim().length() == 0)) {
			errMsg = "传入的环节实例ID号为空!";
			LOGGER.error(errMsg);
			throw new Exception(errMsg);
		}
		String unidId = "";
		Boolean toUnitOpr = Boolean.valueOf(Boolean.getBoolean((String) args.get("toUnitOpr")));

		unidId = (String) args.get("_unitId");

		this.lock.lock();
		try {
			WfWorkItem workItem = this.workflowDAO.getWorkItem(Long.valueOf(Long.parseLong(curWorkItemId)));
			if (((!oprId.equalsIgnoreCase(workItem.getOPERID()))
					&& (IWorkflow.WorkItemState.WIS_DOING.getState().equals(workItem.getSTATE())))
					|| (IWorkflow.WorkItemState.WIS_END.getState().equals(workItem.getSTATE()))) {
				throw new Exception("此任务可能已被其他人接收，请刷新任务列表，谢谢!");
			}
			if (!this.workflowDAO.hasActionRight(null, workItem.getACTION_DEF_ID(), pInsId,
					Long.valueOf(curWorkItemId).longValue(), oprId, unidId, toUnitOpr.booleanValue())) {
				throw new Exception("您没有权限办理" + workItem.getACTION_DEF_NAME() + "业务!");
			}
			return this.workflowDAO.startWorkItem(pInsId, workItem.getPRE_WI_ID(),
					Long.valueOf(Long.parseLong(curWorkItemId)), oprId, workItem);
		} finally {
			this.lock.unlock();
		}
	}

	public boolean submitWorkItem(String pInsId, String workItemId, Map<String, Object> reqParams) throws Exception {
		Long lWorkItemId = Long.valueOf(0L);
		String oprId = (String) reqParams.get("_operId");
		String accepter = (String) reqParams.get("_accepterId");
		String processDefId = (String) reqParams.get("_processDefId");
		String actionDefId = (String) reqParams.get("_curActDefId");
		String nextActionDefId = (String) reqParams.get("_nextActDefId");
		String keyData = (String) reqParams.get("_keyData");
		String rejectReason = (String) reqParams.get("_comment");
		WfActionDef wfDef = null;
		WfActionDef curWfDef = null;
		WfWorkItem curWorkItem = null;
		String isReturn = IWorkflow.IsReturn.N.getState();
		String processInsState = "";
		String newDefId = null;
		String workItemType = IWorkflow.WorkItemType.WIT_SINGLE.getState();
		String[] newDefIds = (String[]) null;
		String[] accepters = (String[]) null;
		Long newWorkItemId = Long.valueOf(0L);
		String errMsg = "";
		if ((pInsId == null) || (pInsId.trim().length() == 0)) {
			errMsg = "传入的流程实例ID号为空!";
			LOGGER.error(errMsg);
			throw new Exception(errMsg);
		}
		if ((workItemId == null) || (workItemId.trim().length() == 0)) {
			errMsg = "传入的环节实例ID号为空!";
			LOGGER.error(errMsg);
			throw new Exception(errMsg);
		}
		lWorkItemId = Long.valueOf(Long.parseLong(workItemId));
		curWfDef = this.workflowDAO.getWfByActionDef(processDefId, actionDefId);
		reqParams.put("_rtnURL", this.workflowDAO.getUrl(actionDefId));
		curWorkItem = this.workflowDAO.getWorkItem(lWorkItemId);

		processInsState = this.workflowDAO.checkProcessState(pInsId);
		if ((processInsState != null) && (!processInsState.equals(IWorkflow.ProcInsState.PIS_DOING.getState()))) {
			throw new Exception("流水号为:" + pInsId + "的这笔业务已" + IWorkflow.ProcInsState.getDesc(processInsState));
		}
		if (!this.workflowDAO.completeWorkItem(pInsId, lWorkItemId, oprId, rejectReason)) {
			throw new Exception("此任务可能已被其他人接收，请刷新任务列表，谢谢!");
		}
		if (nextActionDefId.indexOf("@") > 0) {
			newDefIds = nextActionDefId.split("@");
			accepters = accepter.split(",");

			workItemType = IWorkflow.WorkItemType.WIT_AND.getState();
			for (int i = 0; i < newDefIds.length; i++) {
				if (newDefIds[i].startsWith("!!")) {
					isReturn = IWorkflow.IsReturn.Y.getState();
					newDefId = newDefIds[i].substring(2);

					wfDef = this.workflowDAO.getWfByActionDef(processDefId, newDefId);
					if (IWorkflow.ProcStartOrEnd.PSE_END.getState().equalsIgnoreCase(wfDef.getSTART_OR_END())) {
						this.workflowDAO.updateProcess(pInsId, curWorkItem.getPRE_WI_ID(), lWorkItemId,
								IWorkflow.ProcInsState.PIS_PROC_BACK.getState(), keyData, rejectReason, oprId);
					} else {
						try {
							accepter = accepters[i];
						} catch (Exception ex) {
							accepter = null;
						}
						if (accepter == null) {
							accepter = this.workflowDAO.getBackOprId(lWorkItemId, newDefId);
						} else if ("-1".equals(accepter)) {
							accepter = "";
						}
						this.workflowDAO.creatWorkItem(pInsId, processDefId, newDefId, workItemType, reqParams,
								isReturn, accepter);
						this.workflowDAO.updateProcess(pInsId, curWorkItem.getPRE_WI_ID(), lWorkItemId, null, keyData,
								null, oprId);
					}
				} else {
					isReturn = IWorkflow.IsReturn.N.getState();
					newDefId = newDefIds[i];
					if (this.workflowDAO.checkBranchState(pInsId, lWorkItemId) == 0) {
						this.workflowDAO.creatWorkItem(pInsId, processDefId, newDefId, workItemType, reqParams,
								isReturn, accepters[i]);
					} else {
						this.workflowDAO.updateProcess(pInsId, curWorkItem.getPRE_WI_ID(), lWorkItemId, null, keyData,
								null, oprId);
					}
				}
			}
		} else if (nextActionDefId.startsWith("!!")) {
			isReturn = IWorkflow.IsReturn.Y.getState();
			newDefId = nextActionDefId.substring(2);
			wfDef = this.workflowDAO.getWfByActionDef(processDefId, newDefId);

			workItemType = IWorkflow.WorkItemType.WIT_SINGLE.getState();
			if (IWorkflow.ProcStartOrEnd.PSE_END.getState().equalsIgnoreCase(wfDef.getSTART_OR_END())) {
				if (IWorkflow.WorkItemType.WIT_AND.getState().equalsIgnoreCase(curWorkItem.getWORK_TYPE())) {
					this.workflowDAO.cancelBrotherWorkItem(pInsId, lWorkItemId, oprId);
				}
				this.workflowDAO.updateProcess(pInsId, curWorkItem.getPRE_WI_ID(), lWorkItemId,
						IWorkflow.ProcInsState.PIS_PROC_BACK.getState(), keyData, rejectReason, oprId);
			} else {
				if (accepter == null) {
					accepter = this.workflowDAO.getBackOprId(lWorkItemId, newDefId);
				} else if (("-1".equals(accepter)) || ("".equals(accepter.trim()))) {
					accepter = "";
					WfWorkItem wfItem = new WfWorkItem();
					wfItem.setBAE007(pInsId);
					wfItem.setACTION_DEF_ID(newDefId);
					wfItem.setSTATE(IWorkflow.WorkItemState.WIS_CANCEL.getState());
					cancelFilter(pInsId, wfItem);
				}
				newWorkItemId = this.workflowDAO.creatWorkItem(pInsId, processDefId, newDefId, workItemType, reqParams,
						isReturn, accepter);

				this.workflowDAO.cancelFilter(pInsId, actionDefId, "");
				this.workflowDAO.updateProcess(pInsId, lWorkItemId, newWorkItemId, null, keyData, null, oprId);
			}
		} else {
			isReturn = IWorkflow.IsReturn.N.getState();
			newDefId = nextActionDefId;

			wfDef = this.workflowDAO.getWfByActionDef(processDefId, newDefId);
			if (IWorkflow.ProcStartOrEnd.PSE_END.getState().equalsIgnoreCase(wfDef.getSTART_OR_END())) {
				this.workflowDAO.updateProcess(pInsId, curWorkItem.getPRE_WI_ID(), lWorkItemId,
						IWorkflow.ProcInsState.PIS_END.getState(), keyData, rejectReason, oprId);
			} else {
				workItemType = checkWorkItemType(curWfDef.getNEXT_ACTION_DEF_ID());
				newWorkItemId = this.workflowDAO.creatWorkItem(pInsId, processDefId, newDefId, workItemType, reqParams,
						isReturn, accepter);
				this.workflowDAO.updateProcess(pInsId, lWorkItemId, newWorkItemId, null, keyData, null, oprId);
			}
		}
		return true;
	}

	public void updateWorkItem(WfWorkItem wfWorkItem) throws Exception {
		this.workflowDAO.updateWorkItem(wfWorkItem);
	}

	public boolean submitWorkItem(IDTO dto) throws Exception {
		SubmitWfParams wfParams = (SubmitWfParams) dto.getValue("_submitParams");
		if (Util.isEmpty(wfParams.getPid())) {
			throw new AppException("未指定业务流水号!");
		}
		if (Util.isEmpty(wfParams.getWid())) {
			throw new AppException("未指定环节实例号!");
		}
		if ((Util.isEmpty(wfParams.getNextDefId())) && (wfParams.getSubmitType() == null)) {
			throw new AppException("未指定下一环节ID或 提交类型!");
		}
		if (Util.isEmpty(wfParams.getJbr())) {
			throw new AppException("未指定经办人ID!");
		}
		if (Util.isEmpty(wfParams.getBae006())) {
			throw new AppException("未指定经办机构!");
		}
		if (Util.isEmpty(wfParams.getKeyData())) {
			throw new AppException("未指定流程关键信息!");
		}
		WfProcessInstance wfProcIns = this.workflowDAO.getWfProcessInstanceById(wfParams.getPid());
		WfWorkItem wfItemIns = this.workflowDAO.getWorkItem(Long.valueOf(Long.parseLong(wfParams.getWid())));

		WfActionDef wfActDef = this.workflowDAO.getWfByActionDef(wfProcIns.getPROCESS_DEF_ID(),
				wfItemIns.getACTION_DEF_ID());

		String nextActionDefId = wfParams.getNextDefId();
		if (Util.isEmpty(nextActionDefId)) {
			String[] nextDefIds = wfActDef.getNEXT_ACTION_DEF_ID().split(",");
			for (String defId : nextDefIds) {
				if (wfParams.getSubmitType() == IWorkflow.SubmitType.BACK) {
					if (defId.startsWith("!!")) {
						nextActionDefId = defId;
						break;
					}
				} else if (!defId.startsWith("!!")) {
					nextActionDefId = defId;
					break;
				}
			}
		}
		dto.setValue("pid", wfParams.getPid());

		dto.setValue("wid", wfParams.getWid());

		dto.setValue("_nextActDefId", nextActionDefId);

		dto.setValue("_operId", wfParams.getJbr());

		dto.setValue("_accepterId", wfParams.getAccepter());

		dto.setValue("_keyData", wfParams.getKeyData());

		dto.setValue("_comment", wfParams.getComment());

		dto.setValue("_bae006", wfParams.getBae006());

		dto.setValue("_unitId", wfParams.getOperUnitId());

		dto.setValue("_unitType", wfParams.getOperUnitId());

		dto.setValue("_nextActDefId", nextActionDefId);
		dto.setValue("_processDefId", wfProcIns.getPROCESS_DEF_ID());

		dto.setValue("_curActDefId", wfItemIns.getACTION_DEF_ID());

		dto.setValue("_curActDefName", wfActDef.getACTION_DEF_NAME());
		wfActDef = this.workflowDAO.getWfByActionDef(wfProcIns.getPROCESS_DEF_ID(), nextActionDefId);
		dto.setValue("_nextActDefName", wfActDef.getACTION_DEF_NAME());
		return submitWorkItem(wfParams.getPid(), wfParams.getWid(), dto.getData());
	}

	private String checkWorkItemType(String nextIds) {
		int k = 0;
		if (nextIds == null) {
			return null;
		}
		String[] ids = nextIds.split(",");
		for (int i = 0; i < ids.length; i++) {
			if (!ids[i].startsWith("!!")) {
				k++;
			}
		}
		if (k < 2) {
			return IWorkflow.WorkItemType.WIT_SINGLE.getState();
		}
		return IWorkflow.WorkItemType.WIT_OR.getState();
	}

	public List<String[]> getReceivers4Ins(String processDefId, String curActDefID, String pInsId, long curWorkItemId,
			String orgCode, String nextActDefID, boolean cascade, String jbr, String unitId, boolean isRight,
			Map<String, Object> args) throws Exception {
		String toUnitOprStr = (String) args.get("_toUnitOpr");
		Boolean toUnitOpr = Boolean.valueOf(Boolean.parseBoolean(toUnitOprStr));
		return this.workflowDAO.getReceivers4Ins(processDefId, curActDefID, pInsId, curWorkItemId, orgCode,
				nextActDefID, cascade, jbr, unitId, isRight, toUnitOpr.booleanValue());
	}

	public WfActionDef getWfStartOrEnd(String processDefId, String state) throws Exception {
		return this.workflowDAO.getWfStartOrEnd(processDefId, state);
	}

	public Map<String, String> createSubWf(String pInsId, Long curWorkItemId, Boolean isReturn) throws Exception {
		WfProcessInstance wfProcessIns = this.workflowDAO.getWfProcessInstanceById(pInsId);
		List<WfWorkItem> workItems = this.workflowDAO.getWorkItems(pInsId);

		Collections.sort(workItems, new WorkItemComparator());

		String newPInsId = String.valueOf(this.workflowDAO.getProcessId());
		Long newWorkitemId = null;
		Long preWorkItemId = null;
		Map<Long, Long> workitemIdMap = new HashMap();
		Map<String, String> resp = new HashMap();

		wfProcessIns.setPARENTINSID(newPInsId);
		wfProcessIns.setSTATUS(IWorkflow.ProcInsState.PIS_DOING.getState());
		for (Iterator itor = workItems.iterator(); itor.hasNext();) {
			WfWorkItem workitem = (WfWorkItem) itor.next();
			if ((isReturn.booleanValue()) && (curWorkItemId.equals(workitem.getWORK_ITEM_ID()))) {
				preWorkItemId = workitem.getPRE_WI_ID();
			} else {
				newWorkitemId = this.workflowDAO.getWorkItemId();
				workitemIdMap.put(workitem.getWORK_ITEM_ID(), newWorkitemId);
				workitem.setBAE007(newPInsId);
				workitem.setWORK_ITEM_ID(newWorkitemId);
			}
		}
		this.workflowDAO.insertProcessIns(wfProcessIns);
		for (Iterator itor = workItems.iterator(); itor.hasNext();) {
			WfWorkItem workitem = (WfWorkItem) itor.next();
			if ((!isReturn.booleanValue()) || (!curWorkItemId.equals(workitem.getWORK_ITEM_ID()))) {
				if ((preWorkItemId != null) && (workitem.getWORK_ITEM_ID().equals(preWorkItemId))) {
					workitem.setSTATE(IWorkflow.WorkItemState.WIS_DOING.getState());
				}
				workitem.setPRE_WI_ID((Long) workitemIdMap.get(workitem.getPRE_WI_ID()));
				this.workflowDAO.insertWorkitemIns(workitem);
			}
		}
		resp.put("pid", newPInsId);
		resp.put("wid", String.valueOf(newWorkitemId));

		return resp;
	}

	public String getWfDefId(String pid, String wfName, String rightId) throws Exception {
		if (!Util.isEmpty(pid)) {
			WfProcessInstance wfIns = this.workflowDAO.getWfProcessInstanceById(pid);
			return wfIns.getPROCESS_DEF_ID();
		}
		if (!Util.isEmpty(wfName)) {
			return this.workflowDAO.getWfDefIdByName(wfName);
		}
		if (!Util.isEmpty(rightId)) {
			return this.workflowDAO.getWfDefNameByRightId(rightId);
		}
		throw new AppException("获取流程定义ID出错，详细：未获取到流程定义ID");
	}

	public String getRightIdByWfDefId(String wfDefId) throws Exception {
		return this.workflowDAO.getRightIdByWfDefId(wfDefId);
	}

	public String getRightIdByPid(String pid) throws Exception {
		return this.workflowDAO.getRightIdByPid(pid);
	}

	public WfProcessInstance getWfProcessInstanceById(String pid) throws Exception {
		return this.workflowDAO.getWfProcessInstanceById(pid);
	}

	public int wfSleep(String pid, String wid) throws Exception {
		return wfStateOpr(pid, wid, ConfUtil.getDict("WORKITEMSTATE", "6"));
	}

	public int wfWake(String pid, String wid) throws Exception {
		return wfStateOpr(pid, wid, "2");
	}

	private int wfStateOpr(String pid, String wid, String state) throws Exception {
		int i = 0;
		Long workItemId = wid != null ? Long.valueOf(Long.parseLong(wid)) : null;
		if ((!Util.isEmpty(pid)) && ((workItemId == null) || (workItemId.longValue() <= 0L))) {
			WfProcessInstance wfProcessInstance = new WfProcessInstance();
			wfProcessInstance.setBAE007(pid);
			wfProcessInstance.setSTATUS(state);
			i = this.workflowDAO.setWfState(wfProcessInstance);
			WfWorkItem workItem = new WfWorkItem();
			workItem.setBAE007(pid);
			workItem.setSTATE(state);
			i += this.workflowDAO.setWorkItemState(workItem);
		} else if ((workItemId != null) && (workItemId.longValue() > 0L)) {
			WfWorkItem workItem = new WfWorkItem();
			workItem.setBAE007(pid);
			workItem.setWORK_ITEM_ID(workItemId);
			workItem.setSTATE(state);
			i = this.workflowDAO.setWorkItemState(workItem);
		}
		return i;
	}

	public Map<String, String> getPreOpr(String pInsId, String curWorkDefId, String rightWrokDefId, String rightFlag)
			throws Exception {
		return this.workflowDAO.getPreOpr(pInsId, curWorkDefId, rightWrokDefId, rightFlag);
	}

	public void cancelFilter(String pInsId, WfWorkItem wfWorkItem) throws Exception {
		this.workflowDAO.cancelFilter(pInsId, wfWorkItem);
	}

	public Map<String, String> createWorkItemByDefId(String pid, String workItemDefId, String jbr, String acceptor,
			boolean yJbr, String orgCode) throws Exception {
		WfWorkItem wfWorkItem = this.workflowDAO.getWorkItemInsByDefId(pid, workItemDefId);
		if (wfWorkItem == null) {
			throw new Exception("未找到相关环节实例");
		}
		Long wid = this.workflowDAO.getWorkItemId();
		wfWorkItem.setPRE_WI_ID(wfWorkItem.getWORK_ITEM_ID());
		wfWorkItem.setWORK_ITEM_ID(wid);
		if (!yJbr) {
			wfWorkItem.setOPERID(acceptor);
		}
		if (!Util.isEmpty(orgCode)) {
			wfWorkItem.setBAE006(orgCode);
		}
		wfWorkItem.setSTATE(IWorkflow.WorkItemState.WIS_READY.getState());
		wfWorkItem.setBAE002(jbr);
		wfWorkItem.setBAE003(Long.valueOf(DateUtil.CurDate.YYYYMMDDHHmmss.getDate()));
		wfWorkItem.setSTART_TIME(null);
		wfWorkItem.setCOMPLETE_TIME(null);
		wfWorkItem.setWORK_TYPE(IWorkflow.WorkItemType.WIT_RESTORE.getState());
		wfWorkItem.setMEMO("唤醒环节");

		this.workflowDAO.insertWorkitemIns(wfWorkItem);
		if (!this.workflowDAO.updateProcess(pid, wfWorkItem.getPRE_WI_ID(), wid,
				IWorkflow.ProcInsState.PIS_DOING.getState(), null, null, jbr)) {
			throw new Exception("更新流程状态失败!");
		}
		Map<String, String> respMap = new HashMap(2);
		respMap.put("pid", pid);
		respMap.put("wid", String.valueOf(wid));
		return respMap;
	}
}
