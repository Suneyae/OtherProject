 package cn.sinobest.framework.web.tags;
 
 import com.opensymphony.xwork2.util.ValueStack;
 import java.io.Writer;
 import org.apache.struts2.components.Component;
 import org.apache.struts2.views.annotations.StrutsTag;
 import org.apache.struts2.views.annotations.StrutsTagAttribute;
 
 @StrutsTag(name="gltWhere", tldTagClass="cn.sinobest.framework.web.tags.GltWhereTag", description="多记录表配置条件标签")
 public class GltWhereBean
   extends Component
 {
   String id;
   String whereCls;
   
   public GltWhereBean(ValueStack stack)
   {
     super(stack);
   }
   
   public boolean end(Writer writer, String body)
   {
     GltBean glt = (GltBean)findAncestor(GltBean.class);
     glt.addWhereCls(this.id, this.whereCls);
     return super.end(writer, body);
   }
   
   @StrutsTagAttribute(description="动态字典查询语句", type="String", defaultValue="null")
   public void setWhereCls(String whereCls)
   {
     this.whereCls = whereCls;
   }
   
   @StrutsTagAttribute(description="动态字典配置id", type="String", defaultValue="null")
   public void setId(String id)
   {
     this.id = id;
   }
 }
