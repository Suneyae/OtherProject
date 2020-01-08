package cn.sinobest.framework.web.tags;

import cn.sinobest.framework.service.tags.GtService;
import com.opensymphony.xwork2.util.ValueStack;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ui.AbstractUITag;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class GtTag extends AbstractUITag {
	String title;
	String hasTitle = "true";
	String hasBorder = "true";
	String readOnly = "false";
	String data;
	String getFromReq = "true";
	String showContent = "true";
	String defV;
	private static final long serialVersionUID = 1L;

	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		return new GtBean(stack, req, res);
	}

	protected void populateParams() {
		super.populateParams();
		GtBean gt = (GtBean) this.component;
		if ((this.title != null) && (this.title.length() > 0)) {
			gt.setTitle(findString(this.title));
		}
		if ((this.hasTitle != null) && (this.hasTitle.length() > 0)) {
			gt.setHasTitle(((Boolean) findValue(this.hasTitle, Boolean.class)).booleanValue());
		}
		if ((this.hasTitle != null) && (this.hasTitle.length() > 0)) {
			gt.setHasTitle(((Boolean) findValue(this.hasTitle, Boolean.class)).booleanValue());
		}
		if ((this.hasBorder != null) && (this.hasBorder.length() > 0)) {
			gt.setHasBorder(((Boolean) findValue(this.hasBorder, Boolean.class)).booleanValue());
		}
		if ((this.readOnly != null) && (this.readOnly.length() > 0)) {
			gt.setReadOnly(((Boolean) findValue(this.readOnly, Boolean.class)).booleanValue());
		}
		if ((this.data != null) && (this.data.length() > 0)) {
			gt.setData((Map) findValue(this.data, Map.class));
		}
		if ((this.getFromReq != null) && (this.getFromReq.length() > 0)) {
			gt.setGetFromReq(((Boolean) findValue(this.getFromReq, Boolean.class)).booleanValue());
		}
		if ((this.showContent != null) && (this.showContent.length() > 0)) {
			gt.setShowContent(((Boolean) findValue(this.showContent, Boolean.class)).booleanValue());
		}
		if ((this.defV != null) && (this.defV.length() > 0)) {
			gt.setDefV((Map) findValue(this.defV, Map.class));
		}
		ServletContext sc = this.pageContext.getServletContext();
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
		gt.setGtService((GtService) ac.getBean("gtService", GtService.class));
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setHasTitle(String hasTitle) {
		this.hasTitle = hasTitle;
	}

	public void setHasBorder(String hasBorder) {
		this.hasBorder = hasBorder;
	}

	public void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setGetFromReq(String getFromReq) {
		this.getFromReq = getFromReq;
	}

	public void setShowContent(String showContent) {
		this.showContent = showContent;
	}

	public void setDefV(String defV) {
		this.defV = defV;
	}
}
