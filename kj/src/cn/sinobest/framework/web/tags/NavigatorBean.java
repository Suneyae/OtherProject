 package cn.sinobest.framework.web.tags;
 
 import cn.sinobest.framework.comm.iface.IOperator;
 import cn.sinobest.framework.util.ConfUtil;
 import com.opensymphony.xwork2.util.ValueStack;
 import java.io.Writer;
 import java.util.List;
 import java.util.Map;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import javax.servlet.http.HttpSession;
 import org.apache.struts2.components.UIBean;
 import org.apache.struts2.views.annotations.StrutsTag;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 @StrutsTag(name="navigator", tldTagClass="cn.sinobest.framework.web.tags.NavigatorTag", description="导航栏")
 public class NavigatorBean
   extends UIBean
 {
   private static final Logger logger = LoggerFactory.getLogger(NavigatorBean.class);
   public static final String TEMPLATE = "navigator";
   private String navstr = "";
   private String helpUrl = "";
   public String RightID = "";
   private String OperID = "";
   private String ShortCut = "";
   private List ListMenut;
   IOperator Operator;
   private String ContPath;
   
   public NavigatorBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response)
   {
     super(stack, request, response);
     this.ContPath = request.getContextPath();
     String tmp = request.getParameter("RightID");
     if (tmp == null) {
       tmp = (String)request.getSession().getAttribute("RightID");
     }
     if (tmp == null) {
       tmp = "";
     }
     this.RightID = tmp;
     
     this.Operator = ((IOperator)request.getSession().getAttribute("OPERATOR"));
     if (this.Operator.getShortCutMenus() != null) {
       this.ListMenut = this.Operator.getShortCutMenus();
     }
   }
   
   public void setNavstr()
   {
     String sMenuID = this.RightID;
     String menu = "";
     String helpurl = "";
     if (sMenuID == null) {
       sMenuID = "";
     }
     if ((!sMenuID.equals("")) && (!sMenuID.equals("blank")))
     {
       int iLen = sMenuID.length();
       
 
 
       Map<String, Map<String, String>> mt = ConfUtil.getNavMenus();
       for (int i = 0; i < iLen; i += 2) {
         if (i == 0)
         {
           if (mt.get(sMenuID.substring(0, i + 2)) != null) {
             menu = (String)((Map)mt.get(sMenuID.substring(0, i + 2))).get("RIGHTNAME");
           }
         }
         else if (mt.get(sMenuID.substring(0, i + 2)) != null) {
           menu = menu + "->" + (String)((Map)mt.get(sMenuID.substring(0, i + 2))).get("RIGHTNAME");
         }
       }
       try
       {
         helpurl = (String)((Map)mt.get(sMenuID)).get("HELPURL");
       }
       catch (Exception localException) {}
     }
     if (sMenuID.equals("blank")) {
       menu = "我的桌面";
     }
     if (helpurl == null) {
       helpurl = "";
     }
     this.helpUrl = helpurl;
     this.navstr = menu;
     this.ShortCut = "";
     if (this.ListMenut != null)
     {
       StringBuffer sb = new StringBuffer();
       for (int i = 0; i < this.ListMenut.size(); i++)
       {
         String url = this.ContPath + (String)((Map)ConfUtil.getMenus().get(this.ListMenut.get(i))).get("URL");
         url = url + "?RightID=" + this.ListMenut.get(i);
         menu = (String)((Map)ConfUtil.getMenus().get(this.ListMenut.get(i))).get("RIGHTNAME");
         
         sb.append("<a href=\"");
         sb.append(url);
         sb.append("\" target=workspace class=\"menu_btn\">");
         sb.append(menu);
         sb.append("</a>");
       }
       this.ShortCut = sb.toString();
       sb = null;
     }
   }
   
   protected void evaluateExtraParams()
   {
     try
     {
       super.evaluateExtraParams();
       addParameter("navstr", this.navstr);
       addParameter("helpUrl", this.helpUrl);
       addParameter("ShortCut", this.ShortCut);
     }
     catch (NumberFormatException e)
     {
       logger.error(e.getMessage(), e);
     }
     catch (Exception e)
     {
       logger.error(e.getMessage(), e);
     }
   }
   
   protected String getDefaultTemplate()
   {
     return "navigator";
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
 }