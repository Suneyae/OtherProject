package cn.sinobest.framework.web.tags;

import cn.sinobest.framework.service.tags.Tab;
import cn.sinobest.framework.service.tags.TabPanel;
import com.opensymphony.xwork2.util.ValueStack;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.components.UIBean;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;

@StrutsTag(name = "tabPanel", tldTagClass = "cn.sinobest.framework.web.tags.TabPanelTag", description = "分页标签面板")
public class TabPanelBean extends UIBean {
	private static final String TEMPLATE_TABPANEL = "tabpanel";
	private TabPanel tabPanel;
	private String init;

	public TabPanelBean(ValueStack stack, HttpServletRequest request,
			HttpServletResponse response) {
		super(stack, request, response);
		this.tabPanel = new TabPanel();
	}

	public TabPanel getTabPanel() {
		return this.tabPanel;
	}

	public void setTabPanel(TabPanel tabPanel) {
		this.tabPanel = tabPanel;
	}

	protected String getDefaultTemplate() {
		return "tabpanel";
	}

	@StrutsTagAttribute(description = "标签初始化函数", type = "String", defaultValue = "null")
	public void setInit(String init) {
		this.init = init;
	}

	protected void evaluateExtraParams() {
		super.evaluateExtraParams();
		if ((this.init != null) && (this.init.trim().length() > 0)) {
			this.tabPanel.setInit(findString(this.init));
		}
		addParameter("tabPanel", this.tabPanel);
	}

	public void addTab(Tab tab) {
		if (this.tabPanel.getTabs() == null) {
			this.tabPanel.setTabs(new ArrayList());
		}
		this.tabPanel.getTabs().add(tab);
	}
}
