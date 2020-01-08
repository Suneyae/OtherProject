 package cn.sinobest.framework.web.tags;
 
 import com.opensymphony.xwork2.util.ValueStack;
 import java.io.Writer;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import org.apache.struts2.components.UIBean;
 import org.apache.struts2.views.annotations.StrutsTagAttribute;
 
 public class TreeBean
   extends UIBean
 {
   private static final String TEMPLATE_TREE = "tree";
   private String root;
   private String selected;
   private String type;
   private String whereCls;
   private String options;
   private String initDepth;
   private String bussFuncId;
   
   public TreeBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response)
   {
     super(stack, request, response);
   }
   
   protected void evaluateExtraParams()
   {
     super.evaluateExtraParams();
     if ((this.root != null) && (this.root.trim().length() > 0)) {
       addParameter("root", findString(this.root));
     }
     if ((this.selected != null) && (this.selected.trim().length() > 0)) {
       addParameter("selected", findValue(this.selected));
     }
     if ((this.type != null) && (this.type.trim().length() > 0)) {
       addParameter("type", findValue(this.type, Integer.class));
     }
     if ((this.whereCls != null) && (this.whereCls.trim().length() > 0)) {
       addParameter("whereCls", findString(this.whereCls));
     }
     if ((this.initDepth != null) && (this.initDepth.trim().length() > 0)) {
       addParameter("initDepth", findValue(this.initDepth, Integer.class));
     }
     if ((this.options != null) && (this.options.trim().length() > 0)) {
       addParameter("options", this.options);
     }
     if ((this.bussFuncId != null) && (this.bussFuncId.trim().length() > 0)) {
       addParameter("bussFuncId", findString(this.bussFuncId));
     }
   }
   
   protected String getDefaultTemplate()
   {
     return "tree";
   }
   
   @StrutsTagAttribute(description="根节点", type="String", defaultValue="")
   public void setRoot(String root)
   {
     this.root = root;
   }
   
   @StrutsTagAttribute(description="已选节点的值的集合", type="List<String>", defaultValue="null")
   public void setSelected(String selected)
   {
     this.selected = selected;
   }
   
   @StrutsTagAttribute(description="树的类型。目前有两类：1，菜单树；2，机构树。默认是机构树", type="int", defaultValue="null")
   public void setType(String type)
   {
     this.type = type;
   }
   
   @StrutsTagAttribute(description="额外的sql查询条件，不包含where关键字", type="int", defaultValue="null")
   public void setWhereCls(String whereCls)
   {
     this.whereCls = whereCls;
   }
   
   public boolean usesBody()
   {
     return true;
   }
   
   public boolean end(Writer writer, String body)
   {
     this.options = body;
     return super.end(writer, "");
   }
   
   public void setOptions(String options)
   {
     this.options = options;
   }
   
   @StrutsTagAttribute(description="树形节点数据初始化的深度，默认是2。当为-1时，表示无深度限制，初始化所有树节点", type="Integer", defaultValue="2")
   public void setInitDepth(String initDepth)
   {
     this.initDepth = initDepth;
   }
   
   public void setBussFuncId(String bussFuncId)
   {
     this.bussFuncId = bussFuncId;
   }
 }