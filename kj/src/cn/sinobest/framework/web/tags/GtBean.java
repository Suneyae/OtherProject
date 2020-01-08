package cn.sinobest.framework.web.tags;

import cn.sinobest.framework.service.tags.GtService;
import com.opensymphony.xwork2.util.ValueStack;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.components.UIBean;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;

@StrutsTag(name = "gt", tldTagClass = "cn.sinobest.framework.web.tags.GtTag", description = "单记录表标签")
public class GtBean extends UIBean {
	String title;
	boolean hasTitle = true;
	boolean hasBorder = true;
	boolean readOnly = false;
	Map<String, Object> data;
	boolean getFromReq = true;
	boolean showContent = true;
	Map<String, Object> defV;
	Map<String, String> whereClsMap;
	GtService gtService;
	public static final String TEMPLATE_GLT = "gt";

	public GtBean(ValueStack stack, HttpServletRequest request,
			HttpServletResponse response) {
		super(stack, request, response);
	}

	protected String getDefaultTemplate() {
		return "gt";
	}

	protected void evaluateExtraParams() {
		super.evaluateExtraParams();
		addParameter("title", this.title);
		addParameter("hasTitle", Boolean.valueOf(this.hasTitle));
		addParameter("hasBorder", Boolean.valueOf(this.hasBorder));
		addParameter("readOnly", Boolean.valueOf(this.readOnly));
		addParameter("showContent", Boolean.valueOf(this.showContent));
		Map<String, Object> args =

		this.gtService.getInitData(this.id, this.data, this.whereClsMap, null,
				this.getFromReq ? this.stack : null, this.defV);
		addParameter("gt", args.get("gt"));
		addParameter("gtDicts", args.get("dicts"));
		addParameter("gtValues", args.get("data"));
		addParameter("whereClsMap", this.whereClsMap);
		addParameter("gltMap", args.get("gtlMap"));
		addParameter("permitArchive",
				Boolean.valueOf(this.gtService.isSysPermitArchive()));
		addParameter("archiveUrl", this.gtService.getArchiveUrl());
		addParameter("hasArchive",
				Boolean.valueOf(this.gtService.getHasArchive(this.id)));
	}

	@StrutsTagAttribute(description = "标题", type = "String", defaultValue = "null")
	public void setTitle(String title) {
		this.title = title;
	}

	@StrutsTagAttribute(description = "是否显示标题", type = "boolean", defaultValue = "true")
	public void setHasTitle(boolean hasTitle) {
		this.hasTitle = hasTitle;
	}

	@StrutsTagAttribute(description = "是否显示单记录边框", type = "boolean", defaultValue = "true")
	public void setHasBorder(boolean hasBorder) {
		this.hasBorder = hasBorder;
	}

	@StrutsTagAttribute(description = "是否只读", type = "boolean", defaultValue = "false")
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	@StrutsTagAttribute(description = "外部数据源", type = "Map<String, Object>", defaultValue = "null")
	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	@StrutsTagAttribute(description = "是否从request中获取数据", type = "boolean", defaultValue = "true")
	public void setGetFromReq(boolean getFromReq) {
		this.getFromReq = getFromReq;
	}

	@StrutsTagAttribute(description = "是否显示表单的内容", type = "boolean", defaultValue = "true")
	public void setShowContent(boolean showContent) {
		this.showContent = showContent;
	}

	@StrutsTagAttribute(description = "设置表单控件的默认值，格式：name1=value1|name2=value2", type = "Map", defaultValue = "null")
	public void setDefV(Map<String, Object> defV) {
		this.defV = defV;
	}

	public void setGtService(GtService gtService) {
		this.gtService = gtService;
	}

	public void addWhereCls(String id, String whereCls) {
		if (this.whereClsMap == null) {
			this.whereClsMap = new HashMap();
		}
		this.whereClsMap.put(id, whereCls);
	}

	public Map<String, Object> getData() {
		return this.data;
	}
}
