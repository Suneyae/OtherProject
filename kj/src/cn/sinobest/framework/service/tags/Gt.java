package cn.sinobest.framework.service.tags;

import cn.sinobest.framework.comm.exception.AppException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Gt implements Serializable {
	private String id;
	private List<List<List<Map<String, Object>>>> renders;
	private List<String> hiddens;
	private String description;
	private String bae001;
	private String bae002;
	private String bae003;

	static enum GtType {
		LABEL("01", false), INPUT("02", true), TEXTAREA("03", true), COMBOBOX("05", true), DATEPICKER("06",
				true), RADIOBOX("08", true), CHECKBOX("09", true), BUTTON("10", false), LINK("11", false), IMG("12",
						false), FILE("13", true), SFZ("18", true), NESTTABLE("26", true), CUSTOM("-1", true);

		private final String code;
		private final boolean isComposite;

		private GtType(String code, boolean isComposite) {
			this.code = code;
			this.isComposite = isComposite;
		}

		public Map<String, Object> evalExt(Map<String, Object> source) throws AppException {
			return source;
		}

		static GtType valueOfCode(String code) throws AppException {
			if ((code == null) || (code.length() == 0)) {
				throw new AppException("控件类型代码不可为空");
			}
			for (GtType t : values()) {
				if (t.code.equals(code)) {
					return t;
				}
			}
			throw new AppException("控件类型代码不可识别");
		}

		public boolean isComposite() {
			return this.isComposite;
		}
	}

	private int colNum = 1;
	private static final long serialVersionUID = 1L;

	public String getId() {
		return this.id;
	}

	public String getBae001() {
		return this.bae001;
	}

	public String getBae002() {
		return this.bae002;
	}

	public String getBae003() {
		return this.bae003;
	}

	public List<List<List<Map<String, Object>>>> getRenders() {
		return this.renders;
	}

	public void setRenders(List<List<List<Map<String, Object>>>> renders) {
		this.renders = renders;
	}

	public List<String> getHiddens() {
		return this.hiddens;
	}

	public void setHiddens(List<String> hiddens) {
		this.hiddens = hiddens;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setBae001(String bae001) {
		this.bae001 = bae001;
	}

	public void setBae002(String bae002) {
		this.bae002 = bae002;
	}

	public void setBae003(String bae003) {
		this.bae003 = bae003;
	}

	public int getColNum() {
		return this.colNum;
	}

	public void setColNum(int colNum) {
		this.colNum = colNum;
	}
}