 package cn.sinobest.framework.web.tags;
 
 import com.opensymphony.xwork2.util.ValueStack;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import org.apache.struts2.components.Component;
 import org.apache.struts2.views.jsp.ui.AbstractUITag;
 
 public class TreeTag
   extends AbstractUITag
 {
   private static final long serialVersionUID = 1L;
   private String root;
   private String selected;
   private String type;
   private String whereCls;
   private String initDepth;
   private String bussFuncId;
   
   public void setRoot(String root)
   {
     this.root = root;
   }
   
   public void setSelected(String selected)
   {
     this.selected = selected;
   }
   
   public void setType(String type)
   {
     this.type = type;
   }
   
   public void setWhereCls(String whereCls)
   {
     this.whereCls = whereCls;
   }
   
   public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res)
   {
     return new TreeBean(stack, req, res);
   }
   
   protected void populateParams()
   {
     super.populateParams();
     TreeBean tabBean = (TreeBean)this.component;
     if ((this.id != null) && (this.id.trim().length() > 0)) {
       tabBean.setId(this.id);
     }
     if ((this.root != null) && (this.root.trim().length() > 0)) {
       tabBean.setRoot(this.root);
     }
     if ((this.selected != null) && (this.selected.trim().length() > 0)) {
       tabBean.setSelected(this.selected);
     }
     if ((this.type != null) && (this.type.trim().length() > 0)) {
       tabBean.setType(this.type);
     }
     if ((this.whereCls != null) && (this.whereCls.trim().length() > 0)) {
       tabBean.setWhereCls(this.whereCls);
     }
     if ((this.initDepth != null) && (this.initDepth.trim().length() > 0)) {
       tabBean.setInitDepth(this.initDepth);
     }
     if ((this.bussFuncId != null) && (this.bussFuncId.trim().length() > 0)) {
       tabBean.setBussFuncId(this.bussFuncId);
     }
   }
   
   public void setInitDepth(String initDepth)
   {
     this.initDepth = initDepth;
   }
   
   public void setBussFuncId(String bussFuncId)
   {
     this.bussFuncId = bussFuncId;
   }
 }