 package cn.sinobest.framework.web.tags;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.service.tags.GltService;
 import cn.sinobest.framework.util.ConfUtil;
 import com.opensymphony.xwork2.util.ValueStack;
 import java.io.Writer;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import java.util.Set;
 import org.apache.struts2.components.Component;
 import org.apache.struts2.views.annotations.StrutsTag;
 import org.apache.struts2.views.annotations.StrutsTagAttribute;
 
 @StrutsTag(name="gtData", tldTagClass="cn.sinobest.framework.web.tags.GtDataTag", description="单记录表数据源配置标签")
 public class GtDataBean
   extends Component
 {
   String id;
   protected String whereCls;
   protected String breakDown;
   
   public GtDataBean(ValueStack stack)
   {
     super(stack);
   }
   
   @StrutsTagAttribute(description="表单数据源条件语句", type="String", defaultValue="null")
   public void setWhereCls(String whereCls)
   {
     this.whereCls = whereCls;
   }
   
   @StrutsTagAttribute(description="结果集分列标识（分列指：行列转换）", type="Boolean", defaultValue="false")
   public void setBreakDown(String breakDown)
   {
     this.breakDown = breakDown;
   }
   
   public boolean end(Writer writer, String body)
   {
     String wCls = findString(this.whereCls);
     if (!GltService.isNeedQuery(wCls)) {
       return super.end(writer, body);
     }
     List<Map<String, String>> list = ConfUtil.getQuerySQLResult(this.id, 
       wCls);
     if ((list == null) || (list.isEmpty())) {
       return super.end(writer, body);
     }
     Map<String, Object> columns = new HashMap();
     boolean breakDown = false;
     if (this.breakDown != null) {
       breakDown = Boolean.TRUE.equals(findValue(this.breakDown, 
         Boolean.class));
     }
     if (breakDown)
     {
       columns = new HashMap();
       
       Map<String, String> firstColumn = (Map)list.get(0);
       int rowNums = list.size();
       for (String key : firstColumn.keySet()) {
         columns.put(key, new ArrayList(rowNums));
       }
       Iterator localIterator2;
       for (??? = list.iterator(); ???.hasNext(); localIterator2.hasNext())
       {
         Map<String, String> row = (Map)???.next();
         localIterator2 = columns.keySet().iterator(); continue;String key = (String)localIterator2.next();
         ((List)columns.get(key)).add((String)row.get(key));
       }
     }
     else
     {
       columns.putAll((Map)list.get(0));
     }
     GtBean gt = (GtBean)findAncestor(GtBean.class);
     if (gt == null) {
       throw new AppException("EFW0120", null);
     }
     Map<String, Object> data = gt.getData();
     if (data != null) {
       columns.putAll(data);
     }
     gt.setData(columns);
     return super.end(writer, body);
   }
   
   @StrutsTagAttribute(description="表单数据源语句配置id", type="String", defaultValue="null")
   public void setId(String id)
   {
     this.id = id;
   }
 }
