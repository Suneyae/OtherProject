package cn.sinobest.framework.web.tags;

import com.opensymphony.xwork2.util.ValueStack;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ui.AbstractUITag;

public class TabPanelTag extends AbstractUITag {
	private static final long serialVersionUID = 1L;
	private String init;

	public Component getBean(ValueStack stack, HttpServletRequest req,
			HttpServletResponse res) {
		return new TabPanelBean(stack, req, res);
	}

	public void setInit(String init) {
		this.init = init;
	}

	protected void populateParams() {
		super.populateParams();
		TabPanelBean tabPanelBean = (TabPanelBean) this.component;
		tabPanelBean.setInit(this.init);
	}
}
