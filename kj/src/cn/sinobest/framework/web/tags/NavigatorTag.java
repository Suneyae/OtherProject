 package cn.sinobest.framework.web.tags;
 
 import com.opensymphony.xwork2.util.ValueStack;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import org.apache.struts2.components.Component;
 import org.apache.struts2.views.jsp.ui.AbstractUITag;
 
 public class NavigatorTag
   extends AbstractUITag
 {
   public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res)
   {
     return new NavigatorBean(stack, req, res);
   }
   
   protected void populateParams()
   {
     super.populateParams();
     NavigatorBean navigatorBean = (NavigatorBean)this.component;
     if (!"".equals(navigatorBean.RightID)) {
       navigatorBean.setNavstr();
     }
   }
 }