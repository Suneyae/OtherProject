 package cn.sinobest.framework.web.tags;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IOperator;
 import cn.sinobest.framework.service.tags.WfService;
 import cn.sinobest.framework.service.workflow.IWorkflow.Attr;
 import cn.sinobest.framework.util.DTOUtil;
 import cn.sinobest.framework.util.Util;
 import com.opensymphony.xwork2.util.ValueStack;
 import java.io.Writer;
 import java.util.Map;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import org.apache.struts2.components.UIBean;
 import org.apache.struts2.views.annotations.StrutsTag;
 import org.apache.struts2.views.annotations.StrutsTagAttribute;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 @StrutsTag(name="wf", tldTagClass="cn.sinobest.framework.web.tags.WfTag", description="工作流标签")
 public class WfBean
   extends UIBean
 {
   private static final Logger LOGGER = LoggerFactory.getLogger(WfBean.class);
   public static final String TEMPLATE = "wf";
   private String wfDefId;
   private String wfName;
   private String rightId;
   private String formName;
   private String serviceObject;
   private boolean isShowBZ = false;
   private boolean isShowHistory = false;
   private boolean isCascadeReceiver = false;
   private boolean isUploadFile = false;
   private String rtnURL = "";
   private WfService wfService;
   private boolean isDialog = true;
   private String width = "100%";
   private String height;
   private String pid;
   private String wid;
   private String ctx;
   private String bodyContext;
   private boolean getAab001Aac001 = false;
   
   public WfBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response)
   {
     super(stack, request, response);
   }
   
   protected void evaluateExtraParams()
   {
     try
     {
       super.evaluateExtraParams();
       
 
 
 
 
 
       addParameter("formName", this.formName);
       addParameter("isCascadeReceiver", String.valueOf(this.isCascadeReceiver));
       addParameter("isShowBZ", Boolean.valueOf(this.isShowBZ));
       addParameter("isShowHistory", Boolean.valueOf(this.isShowHistory));
       addParameter("_rtnURL", this.rtnURL);
       addParameter("serviceObject", this.serviceObject);
       addParameter("pid", this.pid);
       addParameter("wid", this.wid);
       addParameter("ctx", this.ctx);
       addParameter("width", this.width);
       
       addParameter("textareaWidth", this.width);
       addParameter("height", this.height);
       addParameter("isDialog", Boolean.valueOf(this.isDialog));
       addParameter("isUploadFile", Boolean.valueOf(this.isUploadFile));
       addParameter("_isWfStart", Util.isEmpty(this.pid) ? "true" : "false");
       String comment = "";
       String wfDefIdStr = this.wfDefId;
       
 
 
 
 
 
 
 
 
 
 
 
 
 
 
       Map<String, Object> initData = this.wfService.getInitData(wfDefIdStr, this.pid, this.wfName, this.rightId, this.wid, this.getAab001Aac001);
       
       Map<String, String> actDef = (Map)initData.get("actDef");
       wfDefIdStr = (String)initData.get("wfDefId");
       comment = (String)Util.nvl(initData.get("comment"));
       
       addParameter("_aab001", initData.get("_aab001") == null ? "" : initData.get("_aab001"));
       addParameter("_aac001", initData.get("_aac001") == null ? "" : initData.get("_aac001"));
       
 
       String isShowSaveBtn = search(IWorkflow.Attr.SAVEBTN.toString(), (String)actDef.get("_attr"));
       
 
       String isShowUploadFile = search(IWorkflow.Attr.UPLOADFILE.toString(), (String)actDef.get("_attr"));
       
       String isDownFile = search(IWorkflow.Attr.DOWNFILE.toString(), (String)actDef.get("_attr"));
       
 
       String isDisabled = search(IWorkflow.Attr.DISABLED.toString(), (String)actDef.get("_attr"));
       
 
       String toApplyOpr = search(IWorkflow.Attr.TOAPPLYOPR.toString(), (String)actDef.get("_attr"));
       
       String toUnitOpr = search(IWorkflow.Attr.TOUNITOPR.toString(), (String)actDef.get("_attr"));
       
       String getItemOpr = search(IWorkflow.Attr.GETITEMOPR.toString(), (String)actDef.get("_attr"));
       
 
       addParameter("isShowSavaBtn", Boolean.valueOf("true".equalsIgnoreCase(isShowSaveBtn)));
       
 
       addParameter("isShowUploadFile", Boolean.valueOf("true".equalsIgnoreCase(isShowUploadFile)));
       
       addParameter("isDownFile", Boolean.valueOf("true".equalsIgnoreCase(isDownFile)));
       
 
       addParameter("isDisabled", Boolean.valueOf("true".equalsIgnoreCase(isDisabled)));
       
 
       addParameter("toApplyOpr", toApplyOpr);
       addParameter("getItemOpr", Util.isEmpty(getItemOpr) ? "false" : "true");
       addParameter("toUnitOpr", toUnitOpr);
       
       addParameter("btnType", (WfTag.ShowType)initData.get("btnType"));
       
 
 
       addParameter("_curActDefId", actDef.get("_curActDefId"));
       addParameter("_nextActDefId", actDef.get("_nextActDefId"));
       addParameter("_curActDefName", actDef.get("_curActDefName"));
       addParameter("_nextActDefName", actDef.get("_nextActDefName"));
       addParameter("_processDefId", wfDefIdStr);
       this.stack.set("_curActDefName", actDef.get("_curActDefName"));
       this.stack.set("_nextActDefName", actDef.get("_nextActDefName"));
       
 
       addParameter("_operId", DTOUtil.getUserInfo().getOperID());
       addParameter("_unitId", DTOUtil.getUserInfo().getOperUnitID());
       
       addParameter("_bae006", DTOUtil.getUserInfo().getBAE001());
       addParameter("_comment", comment);
     }
     catch (Exception e)
     {
       throw new AppException(e.getLocalizedMessage(), e);
     }
   }
   
   private String search(String attr, String attrs)
   {
     if (Util.isEmpty(attrs)) {
       return "";
     }
     String[] attrStr = attrs.split("\\|");
     for (int i = 0; i < attrStr.length; i++)
     {
       String[] keyValue = attrStr[i].split("=");
       if (attr.equalsIgnoreCase(keyValue[0])) {
         return keyValue[1];
       }
     }
     return "";
   }
   
   public boolean end(Writer writer, String body)
   {
     addParameter("body", body);
     return super.end(writer, "");
   }
   
   public boolean usesBody()
   {
     return true;
   }
   
   public void setBodyContext(String bodyContext)
   {
     this.bodyContext = bodyContext;
   }
   
   public void setCtx(String ctx)
   {
     this.ctx = ctx;
   }
   
   public void setPid(String pid)
   {
     this.pid = pid;
   }
   
   public void setWid(String wid)
   {
     this.wid = wid;
   }
   
   public void setWfService(WfService wfService)
   {
     this.wfService = wfService;
   }
   
   protected String getDefaultTemplate()
   {
     return "wf";
   }
   
   @StrutsTagAttribute(description="流程定义ID", type="String", required=true)
   public void setWfDefId(String wfDefId)
   {
     this.wfDefId = wfDefId;
   }
   
   @StrutsTagAttribute(description="表单名", type="String", required=true)
   public void setFormName(String formName)
   {
     this.formName = formName;
   }
   
   @StrutsTagAttribute(description="业务处理对象名", type="String", required=true)
   public void setServiceObject(String serviceObject)
   {
     this.serviceObject = serviceObject;
   }
   
   @StrutsTagAttribute(description="是否显示环节备注录入框", type="boolean", defaultValue="false")
   public void setIsShowBZ(boolean isShowBZ)
   {
     this.isShowBZ = isShowBZ;
   }
   
   @StrutsTagAttribute(description="是否显示流程历史", type="boolean", defaultValue="false")
   public void setIsShowHistory(boolean isShowHistory)
   {
     this.isShowHistory = isShowHistory;
   }
   
   @StrutsTagAttribute(description="接收人列表是否显示所指机构的下属机构的选择人", type="boolean", defaultValue="false")
   public void setIsCascadeReceiver(boolean isCascadeReceiver)
   {
     this.isCascadeReceiver = isCascadeReceiver;
   }
   
   @StrutsTagAttribute(description="返回路径", type="String", defaultValue="")
   public void setRtnURL(String rtnURL)
   {
     this.rtnURL = rtnURL;
   }
   
   @StrutsTagAttribute(description="是否弹出对话框选择操作员，false:直接提交到岗位待办", type="boolean", defaultValue="true")
   public void setDialog(boolean isDialog)
   {
     this.isDialog = isDialog;
   }
   
   @StrutsTagAttribute(description="宽度", type="String", defaultValue="690")
   public void setWidth(String width)
   {
     if (!Util.isEmpty(width)) {
       this.width = width;
     }
   }
   
   @StrutsTagAttribute(description="高度", type="String", defaultValue="100%")
   public void setHeight(String height)
   {
     if (!Util.isEmpty(height)) {
       this.height = height;
     }
   }
   
   @StrutsTagAttribute(description="是否上传文件", type="boolean", defaultValue="false")
   public void setIsUploadFile(boolean isUploadFile)
   {
     this.isUploadFile = isUploadFile;
   }
   
   @StrutsTagAttribute(description="流程定义名称", type="String", defaultValue="wfForm")
   public void setWfName(String wfName)
   {
     this.wfName = wfName;
   }
   
   public void setRightId(String rightId)
   {
     this.rightId = rightId;
   }
   
   @StrutsTagAttribute(description="是否获取aab001和aac001,true:获取,false:不获取,默认不获取", type="boolean", defaultValue="false")
   public void setGetAab001Aac001(boolean getAab001Aac001)
   {
     this.getAab001Aac001 = getAab001Aac001;
   }
 }