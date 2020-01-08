package cn.sinobest.framework.web.tags;

import cn.sinobest.framework.service.tags.Tab;
import com.opensymphony.xwork2.util.ValueStack;
import java.io.Writer;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.struts2.StrutsException;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.annotations.StrutsTagAttribute;

public class TabBean extends Component {
	private static final Pattern p = Pattern.compile("\\?[^\\?\\*]*\\*");
	private String id;
	private String title;
	private String url;
	private String params;
	String disabled;

	public TabBean(ValueStack stack) {
		super(stack);
	}

	public boolean end(Writer writer, String body) {
		TabPanelBean tabPanelBean = (TabPanelBean) findAncestor(TabPanelBean.class);
		if (tabPanelBean == null) {
			throw new StrutsException("Tab标签必须嵌套在TabPanel里面");
		}
		Tab tab = new Tab();
		if ((this.id != null) && (this.id.trim().length() > 0)) {
			tab.setId(findString(this.id));
		}
		if ((this.disabled != null) && (this.disabled.trim().length() > 0)) {
			tab.setDisabled(((Boolean) findValue(this.disabled, Boolean.class))
					.booleanValue());
		}
		tab.setTitle(findString(this.title));
		if ((this.url != null) && (this.url.trim().length() > 0)) {
			String tmpUrl = replaceParams(this.url, getStack());

			String params = appendParams();
			if ((params != null) && (params.length() > 0)) {
				if (this.url.contains("?")) {
					tab.setUrl(this.url + '&' + params);
				} else {
					tab.setUrl(this.url + '?' + params);
				}
			} else {
				tab.setUrl(this.url);
			}
		} else {
			tab.setContent(body);
		}
		tabPanelBean.addTab(tab);
		return super.end(writer, "");
	}

	@StrutsTagAttribute(description = "Tab分页标签ID", type = "String", defaultValue = "null")
	public void setId(String id) {
		this.id = id;
	}

	@StrutsTagAttribute(description = "分页标签的标签头", type = "String", defaultValue = "null")
	public void setTitle(String title) {
		this.title = title;
	}

	@StrutsTagAttribute(description = "请求参数", type = "String", defaultValue = "null")
	public void setUrl(String url) {
		this.url = url;
	}

	@StrutsTagAttribute(description = "外部数据源", type = "String", defaultValue = "null")
	public void setParams(String params) {
		this.params = params;
	}

	public static String replaceParams(String input, ValueStack valueStack) {
		Matcher m = p.matcher(input);
		StringBuffer sb = new StringBuffer();
		String group = null;
		String value = null;
		while (m.find()) {
			group = m.group();
			group = group.substring(1, group.length() - 1);
			value = valueStack.findString(group);
			if (value == null) {
				value = "";
			}
			m.appendReplacement(sb, value);
		}
		m.appendTail(sb);
		return sb.toString();
	}

	protected String appendParams() {
		if ((this.params == null) || (this.params.trim().length() == 0)) {
			return null;
		}
		List<String> paramNames = (List) getStack().findValue(this.params,
				List.class);
		StringBuilder sb = new StringBuilder();
		String value = null;
		for (String attr : paramNames) {
			if ((attr != null) && (attr.length() != 0)) {
				value = getStack().findString(attr);
				if (value != null) {
					sb.append(attr).append('=').append(value).append('&');
				}
			}
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	public boolean usesBody() {
		return true;
	}

	@StrutsTagAttribute(description = "本标签页是否无效", type = "boolean", defaultValue = "false")
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
}
