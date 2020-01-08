 package cn.sinobest.framework.web.tags;
 
 import com.opensymphony.xwork2.util.ValueStack;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import org.apache.struts2.components.Component;
 import org.apache.struts2.views.jsp.ComponentTagSupport;
 
 public class GtWhereTag
   extends ComponentTagSupport
 {
   protected String whereCls;
   private static final long serialVersionUID = 1L;
   
   public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res)
   {
     return new GtWhereBean(stack);
   }
   
   protected void populateParams()
   {
     super.populateParams();
     GtWhereBean gtWhere = (GtWhereBean)this.component;
     gtWhere.setId(this.id);
     gtWhere.setWhereCls(this.whereCls);
   }
   
   public void setWhereCls(String whereCls)
   {
     this.whereCls = whereCls;
   }
 }