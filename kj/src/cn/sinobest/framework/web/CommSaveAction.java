 package cn.sinobest.framework.web;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.exception.AppMsgException;
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.comm.iface.IOperator;
 import cn.sinobest.framework.comm.transcation.DataSourceCallBack;
 import cn.sinobest.framework.comm.transcation.IDataSourceCallBack;
 import cn.sinobest.framework.service.CommService;
 import cn.sinobest.framework.service.workflow.IWorkflow.RightMsg;
 import cn.sinobest.framework.service.workflow.IWorkflowService;
 import cn.sinobest.framework.util.Util;
 import java.util.Map;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import org.apache.struts2.views.util.UrlHelper;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.context.annotation.Scope;
 
 @Scope("prototype")
 public class CommSaveAction
   extends BaseActionSupport
 {
   private static final Logger LOGGER = LoggerFactory.getLogger(CommSaveAction.class);
   @Autowired
   private CommService commService;
   private String _commDo;
   private String _ds;
   private String _redirect;
   
   public String get_commDo()
   {
     return this._commDo;
   }
   
   public void set_commDo(String commDo)
   {
     this._commDo = commDo;
   }
   
   public String get_redirect()
   {
     return this._redirect;
   }
   
   public void set_redirect(String _redirect)
   {
     this._redirect = _redirect;
   }
   
   public String get_ds()
   {
     return this._ds;
   }
   
   public void set_ds(String _ds)
   {
     this._ds = _ds;
   }
   
   public String execute()
     throws Exception
   {
     if (Util.isEmpty(this._commDo)) {
       throw new AppException("_commDo未指定！");
     }
     try
     {
       if (Util.isEmpty(get_rtnURL())) {
         LOGGER.warn("未指定rtnURL");
       }
       String[] execStrs = this._commDo.split("\\|");
       if (execStrs.length <= 0) {
         throw new AppException("参数_commDo为指定操作");
       }
       try
       {
         this.dto.setValue("contextPath", this.request.getContextPath());
         DataSourceCallBack.execute(this._ds, new IDataSourceCallBack()
         {
           public String doAction()
             throws AppException
           {
             try
             {
               CommSaveAction.this.commService.doService(CommSaveAction.this.dto);
             }
             catch (Throwable e)
             {
               throw new AppException(e.getMessage(), e);
             }
             return "";
           }
         });
       }
       catch (Throwable e)
       {
         LOGGER.error("doService error!", e);
         throw new AppException(e.getMessage(), e);
       }
       if (Util.isEmpty(this._rtnURL))
       {
         String rtnURL = this.request.getContextPath();
         rtnURL = rtnURL + (String)this.dto.getValue("_rtnURL");
         
         set_rtnURL(rtnURL);
       }
     }
     catch (AppMsgException ex)
     {
       throw ex;
     }
     catch (Throwable ex)
     {
       throw new AppException(ex.getMessage(), ex);
     }
     if ((!Util.isEmpty(this._redirect)) && ("true".equalsIgnoreCase(this._redirect)))
     {
       StringBuilder tmpLocation = new StringBuilder(this._rtnURL);
       if (this.dto != null)
       {
         this.dto.getData().remove("struts.valueStack");
         this.dto.getData().remove("struts.actionMapping");
         this.dto.getData().remove("CharacterEncodingFilter.FILTERED");
         this.dto.getData().remove("__cleanup_recursion_counter");
         this.dto.getData().remove("org.springframework.web.context.request.RequestContextListener.REQUEST_ATTRIBUTES");
         this.dto.getData().remove("OPERATOR");
       }
       UrlHelper.buildParametersString((Map)Util.nvl(this.dto.getData()), tmpLocation, "&");
       String finalLocation = this.response.encodeRedirectURL(tmpLocation.toString());
       LOGGER.info("redirect to :" + tmpLocation.toString());
       this.response.sendRedirect(finalLocation);
       return "none";
     }
     return "success";
   }
   
   public void workflow()
     throws Exception
   {
     String wid = (String)getValue("wid");
     String pid = (String)getValue("pid");
     LOGGER.info("开始任务：pid=" + pid + " wid=" + wid);
     if (pid == null) {
       throw new AppException("流水号不能为空!");
     }
     if (wid == null) {
       throw new AppException("环节实例号不能为空!");
     }
     try
     {
       IWorkflowService wfService = (IWorkflowService)Util.getBean("workflowService");
       IOperator user = getUserInfo();
       wfService.startWorkItem(pid, wid, user.getOperID(), getData());
       Map<String, String> url = wfService.getActionForward(Long.valueOf(wid));
       
 
       String menuId = wfService.getRightIdByPid(pid);
       
       StringBuffer forwardname = new StringBuffer(this.request.getContextPath()).append((String)url.get(IWorkflow.RightMsg.URL.toString()))
         .append("?").append("pid").append("=").append(pid).append("&").append("wid").append("=").append(wid)
         .append("&RightID=").append((String)url.get(IWorkflow.RightMsg.ID.toString()))
         .append("&_menuID=").append(menuId)
         .append("&").append("funcID").append("=").append(Util.nvl(url.get(IWorkflow.RightMsg.BUSSFUNCID.toString())));
       
 
       LOGGER.info("开始任务：url =" + forwardname);
       this.request.setAttribute("pid", pid);
       this.request.setAttribute("wid", wid);
       this.response.sendRedirect(forwardname.toString());
     }
     catch (Exception e)
     {
       throw new AppException("开始任务失败!" + e.getMessage(), e);
     }
   }
 }