 package cn.sinobest.framework.web.tags;
 
 import com.opensymphony.xwork2.util.ValueStack;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import javax.servlet.jsp.PageContext;
 import org.apache.struts2.components.Component;
 import org.apache.struts2.views.jsp.ui.AbstractUITag;
 
 public class FieldsetTag
   extends AbstractUITag
 {
   private String id = "";
   private String width = "95%";
   private String align = "center";
   private String title = "";
   private String header = "";
   
   public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res)
   {
     return new FieldsetBean(stack, req, res);
   }
   
   protected void populateParams()
   {
     super.populateParams();
     FieldsetBean fieldsetBean = (FieldsetBean)this.component;
     fieldsetBean.setId(this.id);
     fieldsetBean.setWidth(this.width);
     fieldsetBean.setAlign(this.align);
     fieldsetBean.setHeader(this.header);
     fieldsetBean.setTitle(this.title);
     fieldsetBean.setCtx(((HttpServletRequest)this.pageContext.getRequest()).getContextPath());
   }
   
   public void setId(String id)
   {
     this.id = id;
   }
   
   public void setWidth(String width)
   {
     this.width = width;
   }
   
   public void setAlign(String align)
   {
     this.align = align;
   }
   
   public void setTitle(String title)
   {
     this.title = title;
   }
   
   public void setHeader(String header)
   {
     this.header = header;
   }
 }