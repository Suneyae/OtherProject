package cn.sinobest.framework.web.tags;

import com.opensymphony.xwork2.util.ValueStack;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ComponentTagSupport;

public class TabTag extends ComponentTagSupport {
	private static final long serialVersionUID = 1L;
	private String id;
	private String title;
	private String url;
	private String params;
	private String content;
	String disabled;

	public Component getBean(ValueStack stack, HttpServletRequest req,
			HttpServletResponse res) {
		return new TabBean(stack);
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public void setContent(String content) {
		this.content = content;
	}

	protected void populateParams() {
		super.populateParams();
		TabBean tabBean = (TabBean) this.component;
		if ((this.id != null) && (this.id.trim().length() > 0)) {
			tabBean.setId(this.id);
		}
		if ((this.title != null) && (this.title.trim().length() > 0)) {
			tabBean.setTitle(this.title);
		}
		if ((this.url != null) && (this.url.trim().length() > 0)) {
			tabBean.setUrl(this.url);
		}
		if ((this.params != null) && (this.params.trim().length() > 0)) {
			tabBean.setParams(this.params);
		}
		if ((this.disabled != null) && (this.disabled.trim().length() > 0)) {
			tabBean.setDisabled(this.disabled);
		}
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
}
