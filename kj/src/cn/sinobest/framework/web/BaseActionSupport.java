package cn.sinobest.framework.web;

import cn.sinobest.framework.comm.dto.DTO;
import cn.sinobest.framework.comm.exception.AppException;
import cn.sinobest.framework.comm.iface.IDTO;
import cn.sinobest.framework.comm.iface.IOperator;
import cn.sinobest.framework.util.Util;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public abstract class BaseActionSupport extends ActionSupport implements IBaseAction, IDTO, ModelDriven, Preparable {
	private static final long serialVersionUID = 599589399313135357L;
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseActionSupport.class);
	protected IDTO dto;
	protected String _rtnURL = "";
	protected String vals = "";
	protected final HttpServletRequest request = ServletActionContext.getRequest();
	protected final HttpServletResponse response = ServletActionContext.getResponse();

	public String get_rtnURL() {
		return this._rtnURL;
	}

	public void set_rtnURL(String rtnURL) {
		this._rtnURL = rtnURL;
	}

	public String getVals() {
		return this.vals;
	}

	public void setVals(String vals) {
		this.vals = vals;
	}
	public void setDto(IDTO dto) {
		this.dto = dto;
	}

	public BaseActionSupport() {
		this.dto = new DTO();
	}

	public final IDTO getDto() {
		return this.dto;
	}
	public final void prepare() throws Exception {
	}

	public final Object getModel() {
		try {
			prepareDto();
		} catch (Exception e) {
			throw new AppException("获取参数出错!", e);
		}
		return this.dto;
	}

	public String execute() throws Exception {
		return "i";
	}

	public final Map<String, Object> getData() {
		return this.dto.getData();
	}

	public final StringBuffer getJSONData() throws Exception {
		return this.dto.getJSONData();
	}

	public final IOperator getUserInfo() {
		return this.dto.getUserInfo();
	}

	public void reflectEntity(Object entity) throws Exception {
	}

	public final void setData(Map<String, Object> data) {
		this.dto.setData(data);
	}

	public final void setValue(String key, Object value) {
		this.dto.setValue(key, value);
	}

	public final Object getValue(String key) {
		return Util.nvl(this.dto.getValue(key));
	}

	public final Object[] getValues(String key) {
		Object o = this.dto.getValue(key);
		if (o == null) {
			return null;
		}
		if ((o instanceof Object[])) {
			return (Object[]) o;
		}
		return new Object[] { key, o };
	}

	public final void setUserInfo(IOperator userInfo) {
		this.dto.setUserInfo(userInfo);
	}

	protected final void prepareDto() throws Exception {
		HashMap<String, Object> data = new HashMap();

		HttpServletRequest rq = ServletActionContext.getRequest();
		String queryString = rq.getQueryString();
		Util.validateQueryStringSafty(queryString);
		Enumeration attrNames = rq.getAttributeNames();
		while (attrNames.hasMoreElements()) {
			String key = (String) attrNames.nextElement();
			data.put(key, rq.getAttribute(key));
		}
		Map<String, Object> sessionAttr = ServletActionContext.getContext().getSession();
		if (sessionAttr != null) {
			data.putAll(sessionAttr);
		}
		Map<String, Object> paramsData = (HashMap) formartParameter(ServletActionContext.getContext().getParameters());

		Util.validateParamsDataSafty(paramsData);
		if (paramsData != null) {
			//Copies all of the mappings
			data.putAll(paramsData);
		}
		// 这行代码就能够把findValue("dto");的形式获取到dto了，这个dto里封装了从ActionContext获取到的session，parameters等信息，
		this.dto.setData(data);

		LOGGER.info("dto :" + getParameterLogMap(this.dto.getData()));
		if (LOGGER.isDebugEnabled()) {
			for (Map.Entry<String, Object> entry : this.dto.getData().entrySet()) {
				String name = (String) entry.getKey();
				LOGGER.debug(name + " = " + String.valueOf(entry.getValue()));
			}
		}
//		this.request.setAttribute(arg0, arg1);
		this.dto.setUserInfo((IOperator) this.request.getSession().getAttribute("OPERATOR"));
	}

	private Map<String, Object> formartParameter(Map<String, Object> parameters) {
		if (parameters == null) {
			return null;
		}
		Map<String, Object> data = new HashMap(parameters);
		String multpleKey = "_multiple";
		Object value = data.get("_multiple");
		Map<String, String> paramsMap;
		if (data.get("_params") != null) {
			String params = String.valueOf(Array.get(data.get("_params"), 0));
			paramsMap = Util.strParam2Map(params.replaceAll("\\{|\\}", ""), "\\|", null);
			if (paramsMap != null) {
				data.putAll(paramsMap);
			}
		}
		if (value == null) {
			for (Map.Entry<String, Object> entry : data.entrySet()) {
				if ((entry.getValue() != null) && (entry.getValue().getClass().isArray())
						&& (Array.getLength(entry.getValue()) == 1)) {
					entry.setValue(Array.get(entry.getValue(), 0));
				}
			}
			return data;
		}
		Collection<String> multipleNames = new HashSet(10);
		String[] multipleValues = (String[]) value;
		for (String one : multipleValues) {
			for (String oneName : one.split(",")) {
				multipleNames.add(oneName);
			}
		}
		for (Map.Entry<String, Object> entry : data.entrySet()) {
			if ((entry.getValue() != null) && (entry.getValue().getClass().isArray())
					&& (Array.getLength(entry.getValue()) == 1) && (!multipleNames.contains(entry.getKey()))) {
				entry.setValue(Array.get(entry.getValue(), 0));
			}
		}
		return data;
	}

	private String getParameterLogMap(Map<String, Object> parameters) {
		if (parameters == null) {
			return "NONE";
		}
		StringBuilder logEntry = new StringBuilder();
		for (Map.Entry entry : parameters.entrySet()) {
			logEntry.append(String.valueOf(entry.getKey()));
			logEntry.append(" => ");
			if ((entry.getValue() instanceof Object[])) {
				Object[] valueArray = (Object[]) entry.getValue();
				logEntry.append("(Object[]");
				logEntry.append("[ ");
				if (valueArray.length > 0) {
					for (int indexA = 0; indexA < valueArray.length - 1; indexA++) {
						Object valueAtIndex = valueArray[indexA];
						logEntry.append(String.valueOf(valueAtIndex));
						logEntry.append(", ");
					}
					logEntry.append(String.valueOf(valueArray[(valueArray.length - 1)]));
				}
				logEntry.append(" ] ");
			} else {
				logEntry.append(String.valueOf(entry.getValue()));
			}
		}
		return logEntry.toString();
	}
}
