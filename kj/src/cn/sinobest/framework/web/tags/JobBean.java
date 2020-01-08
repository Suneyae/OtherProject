 package cn.sinobest.framework.web.tags;
 
 import com.opensymphony.xwork2.util.ValueStack;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import org.apache.struts2.components.UIBean;
 import org.apache.struts2.views.annotations.StrutsTag;
 import org.apache.struts2.views.annotations.StrutsTagAttribute;
 
 @StrutsTag(name="job", tldTagClass="cn.sinobest.framework.web.tags.JobTag", description="Job标签")
 public class JobBean
   extends UIBean
 {
   public static final String TEMPLATE = "job";
   private String id;
   private String refresh = "2000";
   private String width = "600";
   private String height = "400";
   private boolean isMulti;
   private String key;
   
   public String getHeight()
   {
     return this.height;
   }
   
   @StrutsTagAttribute(description="显示的窗口高度", type="String", defaultValue="400")
   public void setHeight(String height)
   {
     this.height = height;
   }
   
   public boolean isMulti()
   {
     return this.isMulti;
   }
   
   public void setMulti(boolean isMulti)
   {
     this.isMulti = isMulti;
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
   
   @StrutsTagAttribute(description="显示的窗口宽度", type="String", defaultValue="600")
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
   
   @StrutsTagAttribute(description="每隔多少秒刷新", type="", defaultValue="2000")
   public void setRefresh(String refresh)
   {
     this.refresh = refresh;
   }
   
   public JobBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response)
   {
     super(stack, request, response);
   }
   
   protected String getDefaultTemplate()
   {
     return "job";
   }
   
   protected void evaluateExtraParams()
   {
     super.evaluateExtraParams();
     addParameter("id", this.id);
     addParameter("refresh", this.refresh);
     addParameter("width", this.width);
     addParameter("key", this.key);
     addParameter("isMulti", Boolean.valueOf(this.isMulti));
     addParameter("height", this.height);
   }
 }