 package cn.sinobest.framework.web.tags;
 
 import com.opensymphony.xwork2.util.ValueStack;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import org.apache.struts2.components.Component;
 import org.apache.struts2.views.jsp.ComponentTagSupport;
 
 public class GtDataTag
   extends ComponentTagSupport
 {
   protected String whereCls;
   protected String breakDown;
   private static final long serialVersionUID = 1L;
   
   public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res)
   {
     return new GtDataBean(stack);
   }
   
   public void setWhereCls(String whereCls)
   {
     this.whereCls = whereCls;
   }
   
   public void setBreakDown(String breakDown)
   {
     this.breakDown = breakDown;
   }
   
   protected void populateParams()
   {
     super.populateParams();
     GtDataBean bean = (GtDataBean)this.component;
     bean.setId(this.id);
     if (this.whereCls != null) {
       bean.setWhereCls(this.whereCls);
     }
     if (this.breakDown != null) {
       bean.setBreakDown(this.breakDown);
     }
   }
 }