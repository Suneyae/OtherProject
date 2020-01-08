package cn.sinobest.framework.web.tags;

import com.opensymphony.xwork2.util.ValueStack;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ui.AbstractUITag;

public class ButtonTag extends AbstractUITag {
	private String id = "";
	private String name = "";
	private String type = "a";
	private String value = "";
	private String delay = "";
	private String onclick = "";
	private String title = "";
	private String keycomb = "";
	private String href = "javascript:void(0)";
	private String readonly = "false";

	public Component getBean(ValueStack stack, HttpServletRequest req,
			HttpServletResponse res) {
		return new ButtonBean(stack, req, res);
	}

	protected void populateParams() {
		super.populateParams();
		ButtonBean buttonBean = (ButtonBean) this.component;
		buttonBean.setId(this.id);
		buttonBean.setName(this.name);
		buttonBean.setOnclick(this.onclick);
		buttonBean.setType(this.type);
		buttonBean.setValue(this.value);
		buttonBean.setTitle(this.title);
		buttonBean.setKeycomb(this.keycomb);
		buttonBean.setHref(this.href);
		buttonBean.setReadonly(this.readonly);
		buttonBean.setDelay(this.delay);
	}

	public void setDelay(String delay) {
		this.delay = delay;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setKeycomb(String keycomb) {
		this.keycomb = keycomb;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}
}
