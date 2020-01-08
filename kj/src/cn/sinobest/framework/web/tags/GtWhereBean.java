 package cn.sinobest.framework.web.tags;
 
 import com.opensymphony.xwork2.util.ValueStack;
 import java.io.Writer;
 import org.apache.struts2.components.Component;
 import org.apache.struts2.views.annotations.StrutsTag;
 import org.apache.struts2.views.annotations.StrutsTagAttribute;
 
 @StrutsTag(name="gtWhere", tldTagClass="cn.sinobest.framework.web.tags.GtWhereTag", description="单记录表配置条件标签")
 public class GtWhereBean
   extends Component
 {
   String id;
   String whereCls;
   
   public GtWhereBean(ValueStack stack)
   {
     super(stack);
   }
   
   public boolean end(Writer writer, String body)
   {
     GtBean gt = (GtBean)findAncestor(GtBean.class);
     gt.addWhereCls(this.id, this.whereCls);
     return super.end(writer, body);
   }
   
   @StrutsTagAttribute(description="表单查询条件语句", type="String", defaultValue="null")
   public void setWhereCls(String whereCls)
   {
     this.whereCls = whereCls;
   }
   
   @StrutsTagAttribute(description="表单查询语句配置id", type="String", defaultValue="null")
   public void setId(String id)
   {
     this.id = id;
   }
 }