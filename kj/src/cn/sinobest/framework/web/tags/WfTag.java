package cn.sinobest.framework.web.tags;

import cn.sinobest.framework.service.tags.WfService;
import cn.sinobest.framework.util.DTOUtil;
import cn.sinobest.framework.util.Util;
import com.opensymphony.xwork2.util.ValueStack;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ui.AbstractUITag;

public class WfTag extends AbstractUITag {
	private String wfDefId;
	private String wfName;

	public static enum ShowType {
		START_END, START, MIDDLE, END, MULTI;
	}

	private String formName = "wfForm";
	private String serviceObject;
	private boolean isShowBZ;
	private boolean isShowHistory;
	private boolean isCascadeReceiver;
	private String rtnURL;
	private boolean isDialog = true;
	private boolean isUploadFile = false;
	private String width;
	private String height;
	private boolean getAab001Aac001 = false;

	public Component getBean(ValueStack stack, HttpServletRequest req,
			HttpServletResponse res) {
		return new WfBean(stack, req, res);
	}

	protected void populateParams() {
		super.populateParams();

		WfBean wfBean = (WfBean) this.component;

		wfBean.setWfDefId(this.wfDefId);
		wfBean.setWfName(this.wfName);
		wfBean.setFormName(this.formName);
		wfBean.setServiceObject(this.serviceObject);
		wfBean.setIsCascadeReceiver(this.isCascadeReceiver);
		wfBean.setIsShowBZ(this.isShowBZ);
		wfBean.setIsShowHistory(this.isShowHistory);
		wfBean.setRtnURL(this.rtnURL);
		wfBean.setRightId(DTOUtil.getValue("_menuID"));
		wfBean.setWidth(this.width);
		wfBean.setHeight(this.height);
		wfBean.setDialog(this.isDialog);
		wfBean.setIsUploadFile(this.isUploadFile);
		wfBean.setWfService((WfService) Util.getBean("wfService",
				WfService.class));
		wfBean.setCtx(((HttpServletRequest) this.pageContext.getRequest())
				.getContextPath());
		wfBean.setPid(DTOUtil.getValue("pid"));
		wfBean.setWid(DTOUtil.getValue("wid"));
		wfBean.setGetAab001Aac001(this.getAab001Aac001);
	}

	public void setWfDefId(String wfDefId) {
		this.wfDefId = wfDefId;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public void setServiceObject(String serviceObject) {
		this.serviceObject = serviceObject;
	}

	public void setIsShowBZ(boolean isShowBZ) {
		this.isShowBZ = isShowBZ;
	}

	public void setIsShowHistory(boolean isShowHistory) {
		this.isShowHistory = isShowHistory;
	}

	public void setIsCascadeReceiver(boolean isCascadeReceiver) {
		this.isCascadeReceiver = isCascadeReceiver;
	}

	public void setRtnURL(String rtnURL) {
		this.rtnURL = rtnURL;
	}

	public void setIsDialog(boolean isDialog) {
		this.isDialog = isDialog;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public void setIsUploadFile(boolean isUploadFile) {
		this.isUploadFile = isUploadFile;
	}

	public void setWfName(String wfName) {
		this.wfName = wfName;
	}

	public void setGetAab001Aac001(boolean getAab001Aac001) {
		this.getAab001Aac001 = getAab001Aac001;
	}
}
