package cn.sinobest.framework.util;

import cn.sinobest.framework.comm.iface.IDTO;
import cn.sinobest.framework.comm.iface.IOperator;
import cn.sinobest.framework.service.tags.WfService;
import cn.sinobest.framework.service.workflow.IWorkflowService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DTOUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(DTOUtil.class);
	private static final String DTO = "dto";

	public static String getValue(String key) {
		try {
			ValueStack stack = ActionContext.getContext().getValueStack();
			String value = stack.findString(key);
			if (value != null) {
				return value;
			}
			IDTO dto = (IDTO) stack.findValue("dto");
			if (dto == null) {
				return "";
			}
			Map<String, Object> data = dto.getData();
			return (String) (data != null ? Util.nvl(data.get(key)) : "");
		} catch (Exception e) {
			LOGGER.warn("未找到key=" + key + "的值！", e);
		}
		return "";
	}

	public static Object getObject(String key) {
		try {
			ValueStack stack = ActionContext.getContext().getValueStack();
			IDTO dto = (IDTO) stack.findValue("dto");
			Map<String, Object> data = dto.getData();
			return data != null ? data.get(key) : "";
		} catch (Exception ex) {
			LOGGER.warn("未找到key=" + key + "的值！", ex);
		}
		return "";
	}

	public static Map<String, Object> getData() {
		try {
			ValueStack stack = ActionContext.getContext().getValueStack();
			IDTO dto = (IDTO) stack.findValue("dto");
			return dto != null ? dto.getData() : null;
		} catch (Exception ex) {
			LOGGER.warn("未找到data！", ex);
		}
		return null;
	}

	public static IOperator getUserInfo() {
		try {
			ValueStack stack = ActionContext.getContext().getValueStack();
			IDTO dto = (IDTO) stack.findValue("dto");
			return dto != null ? dto.getUserInfo() : null;
		} catch (Exception ex) {
			LOGGER.warn("未找到UserInfo！", ex);
		}
		return null;
	}

	public static void setStackValue(String key, Object o) {
		ValueStack stack = ActionContext.getContext().getValueStack();
		IDTO dto = (IDTO) stack.findValue("dto");
		stack.set(key, o);
	}

	public static Object getStackValue(String key) {
		ValueStack stack = ActionContext.getContext().getValueStack();
		return stack.findValue(key);
	}

	public static IDTO getDTO() throws Exception {
		try {
			return (IDTO) ActionContext.getContext().getValueStack().findValue("dto");
		} catch (NullPointerException ex) {
			LOGGER.warn("未找到dto！", ex);
		}
		return null;
	}

	public static String getYwlsh() {
		return getValue("pid");
	}

	public static String getCurDefName() throws Exception {
		String wid = getValue("wid");
		String pid = getValue("pid");
		if (Util.isEmpty(wid)) {
			return "";
		}
		WfService wf = (WfService) Util.getBean("wfService");
		IWorkflowService workflowService = (IWorkflowService) Util.getBean("workflowService");
		Map<String, String> actDef = wf.getActDefId(workflowService.getWfDefId(pid, null, null), Long.valueOf(wid));
		return (String) actDef.get("_curActDefName");
	}

	public static String getFuncID() {
		return getValue("funcID");
	}
}
