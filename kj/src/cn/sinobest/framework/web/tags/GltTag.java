package cn.sinobest.framework.web.tags;

import cn.sinobest.framework.service.tags.GltService;
import com.opensymphony.xwork2.util.ValueStack;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ui.AbstractUITag;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class GltTag extends AbstractUITag {
	String editable = "false";
	String showDelete = "true";
	String whereCls = "1=2";
	String width = "95%";
	String colorfun;
	String dsId;
	boolean hasTitle = true;
	String expBtns = "";
	boolean hasPage = true;
	boolean showContent = true;
	String data;
	String pageAlign = GltBean.PageAlign.BOTTOM.toString();
	String subTotalAlign = GltBean.SubTotalAlign.BOTTOM.toString();
	String pageSize = "20";
	String height = "310";
	String nupRow = "1";
	String layout = "FULL";
	private static final long serialVersionUID = 1L;

	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		return new GltBean(stack, req, res);
	}

	protected void populateParams() {
		super.populateParams();
		GltBean glt = (GltBean) this.component;
		glt.setEditable(this.editable);
		glt.setShowDelete(this.showDelete);
		glt.setWhereCls(this.whereCls);
		glt.setWidth(this.width);
		glt.setColorfun(this.colorfun);
		glt.setDsId(this.dsId);
		glt.setData(this.data);
		glt.setHasTitle(this.hasTitle);
		glt.setExpBtns(this.expBtns);
		glt.setHasPage(this.hasPage);
		if (this.nupRow != null) {
			glt.setNupRows((Integer) getStack().findValue(this.nupRow, Integer.class));
		}
		if (this.pageSize != null) {
			glt.setPageSize(((Integer) getStack().findValue(this.pageSize, Integer.class)).intValue());
		}
		glt.setHeight(this.height);
		glt.setShowContent(this.showContent);
//		glt.setSubTotalAlign(GltBean.SubTotalAlign.valueOf(this.subTotalAlign.toUpperCase()));
//	    glt.setPageAlign(GltBean.PageAlign.valueOf(this.pageAlign.toUpperCase()));
		ServletContext sc = this.pageContext.getServletContext();
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
		glt.setGltService((GltService) ac.getBean("gltService", GltService.class));
		if ((this.layout != null) && (this.layout.trim().length() > 0)) {
			glt.setLayout(findString(this.layout));
		}
	}

	public void setEditable(String editable) {
		this.editable = editable;
	}

	public void setShowDelete(String showDelete) {
		this.showDelete = showDelete;
	}

	public void setWhereCls(String whereCls) {
		this.whereCls = whereCls;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public void setColorfun(String colorfun) {
		this.colorfun = colorfun;
	}

	public void setDsId(String dsId) {
		this.dsId = dsId;
	}

	public void setHasTitle(boolean hasTitle) {
		this.hasTitle = hasTitle;
	}

	public void setHasPage(boolean hasPage) {
		this.hasPage = hasPage;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setPageAlign(String pageAlign) {
		this.pageAlign = pageAlign;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public void setShowContent(boolean showContent) {
		this.showContent = showContent;
	}

	public void setExpBtns(String expBtns) {
		this.expBtns = expBtns;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public void setSubTotalAlign(String subTotalAlign) {
		this.subTotalAlign = subTotalAlign;
	}

	public void setNupRow(String nupRow) {
		this.nupRow = nupRow;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}
}
