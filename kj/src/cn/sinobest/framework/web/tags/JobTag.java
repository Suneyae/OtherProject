 package cn.sinobest.framework.web.tags;
 
 import com.opensymphony.xwork2.util.ValueStack;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import org.apache.struts2.components.Component;
 import org.apache.struts2.views.jsp.ui.AbstractUITag;
 
 public class JobTag
   extends AbstractUITag
 {
   private String id;
   private String refresh;
   private String width;
   private String height;
   private String key;
   private boolean isMulti;
   
   public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res)
   {
     return new JobBean(stack, req, res);
   }
   
   public String getHeight()
   {
     return this.height;
   }
   
   public void setHeight(String height)
   {
     this.height = height;
   }
   
   public String getKey()
   {
     return this.key;
   }
   
   public void setKey(String key)
   {
     this.key = key;
   }
   
   public String getWidth()
   {
     return this.width;
   }
   
   public void setWidth(String width)
   {
     this.width = width;
   }
   
   public String getId()
   {
     return this.id;
   }
   
   public void setId(String id)
   {
     this.id = id;
   }
   
   public String getRefresh()
   {
     return this.refresh;
   }
   
   public void setRefresh(String refresh)
   {
     this.refresh = refresh;
   }
   
   public boolean isMulti()
   {
     return this.isMulti;
   }
   
   public void setIsMulti(boolean isMulti)
   {
     this.isMulti = isMulti;
   }
   
   protected void populateParams()
   {
     super.populateParams();
     JobBean jobBean = (JobBean)this.component;
     jobBean.setId(this.id);
     jobBean.setRefresh(this.refresh);
     jobBean.setWidth(this.width);
     jobBean.setKey(this.key);
     jobBean.setMulti(this.isMulti);
     jobBean.setHeight(this.height);
   }
 }