 package cn.sinobest.framework.web.tags;
 
 import com.opensymphony.xwork2.util.ValueStack;
 import java.io.Writer;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import org.apache.struts2.components.UIBean;
 import org.apache.struts2.views.annotations.StrutsTag;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 @StrutsTag(name="ft", tldTagClass="cn.sinobest.framework.web.tags.FieldsetTag", description="Fieldset标签")
 public class FieldsetBean
   extends UIBean
 {
   private static final Logger logger = LoggerFactory.getLogger(FieldsetBean.class);
   public static final String TEMPLATE = "ft";
   private String id = "";
   private String width = "95%";
   private String align = "center";
   private String title = "";
   private String header = "";
   private String ctx = "";
   
   public FieldsetBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response)
   {
     super(stack, request, response);
   }
   
   protected void evaluateExtraParams()
   {
     try
     {
       super.evaluateExtraParams();
       addParameter("id", this.id);
       addParameter("width", this.width);
       addParameter("align", this.align);
       addParameter("header", this.header);
       addParameter("title", this.title);
       addParameter("ctx", this.ctx);
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
     return "ft";
   }
   
   public boolean usesBody()
   {
     return true;
   }
   
   public boolean end(Writer writer, String body)
   {
     addParameter("body", body);
     return super.end(writer, "");
   }
   
   public void setId(String id)
   {
     this.id = id;
   }
   
   public void setWidth(String width)
   {
     this.width = width;
   }
   
   public void setCtx(String ctx)
   {
     this.ctx = ctx;
   }
   
   public void setAlign(String align)
   {
     this.align = align;
   }
   
   public void setHeader(String header)
   {
     this.header = header;
   }
   
   public void setTitle(String title)
   {
     this.title = title;
   }
 }
