package cn.sinobest.framework.comm.dto;

import cn.sinobest.framework.comm.iface.IDTO;
import cn.sinobest.framework.comm.iface.IOperator;
import cn.sinobest.framework.comm.iface.IReflectionProvider;
import cn.sinobest.framework.service.json.JSONUtilities;
import cn.sinobest.framework.util.ReflectionProvider;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DTO implements IDTO, Cloneable, Serializable {
	private static final long serialVersionUID = 6945389340172233341L;
	private IOperator userInfo = null;
	private Map<String, Object> data = new HashMap();
	private StringBuffer jsonData = null;

	public Map<String, Object> getData() {
		return this.data;
	}

	public IOperator getUserInfo() {
		return this.userInfo;
	}

	public Object getValue(String key) {
		return this.data.get(key) != null ? this.data.get(key) : null;
	}

	public void setValue(String key, Object value) {
		this.data.put(key, value);
	}

	public void setData(Map<String, Object> data) {
		if (data == null) {
			return;
		}
		this.data.putAll(data);
	}

	public void setUserInfo(IOperator userInfo) {
		this.userInfo = userInfo;
	}

	public StringBuffer getJSONData() throws Exception {
		StringBuffer jsonString = new JSONUtilities(1).parseObject(this.data);
		return new StringBuffer(jsonString.toString().replace("\\", "\\\\").replace("'", "\\'"));
	}

	public String getStrParams4Data() {
		StringBuffer strParams = new StringBuffer("");
		for (Map.Entry<String, Object> entry : this.data.entrySet()) {
			if ((entry.getValue() instanceof Object[])) {
				for (int i = 0; i < Array.getLength(entry.getValue()); i++) {
					strParams.append((String) entry.getKey()).append("=")
							.append((String) Array.get(entry.getValue(), i)).append("|");
				}
			} else {
				strParams.append((String) entry.getKey()).append("=").append(String.valueOf(entry.getValue()))
						.append("|");
			}
		}
		return strParams.toString();
	}

	public void reflectEntity(Object entity) throws Exception {
		IReflectionProvider ref = new ReflectionProvider();
		ref.setPropertiesWithObject(this.data, entity);
	}

	public Object[] getValues(String key) {
		Object o = this.data.get(key);
		if (o == null) {
			return null;
		}
		if ((o instanceof Object[])) {
			return (Object[]) o;
		}
		return new Object[] { key, o };
	}

	public static Map<?, ?> mapClone(Map source) {
		Map target = new HashMap();
		for (Iterator itor = source.keySet().iterator(); itor.hasNext();) {
			Object key = itor.next();
			target.put(key, source.get(key));
		}
		return target;
	}

	protected Object clone() throws CloneNotSupportedException {
		super.clone();
		return super.clone();
	}
}