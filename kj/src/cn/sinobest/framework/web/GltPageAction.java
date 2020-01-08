 package cn.sinobest.framework.web;
 
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.comm.transcation.AppContextHolder;
 import cn.sinobest.framework.service.json.JSONUtilities;
 import cn.sinobest.framework.service.tags.Glt;
 import cn.sinobest.framework.service.tags.GltService;
 import cn.sinobest.framework.service.tags.GtService;
 import java.io.ByteArrayInputStream;
 import java.io.InputStream;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import org.apache.log4j.Logger;
 import org.springframework.beans.factory.annotation.Autowired;
 
 public class GltPageAction
   extends BaseActionSupport
 {
   Logger log = Logger.getLogger(getClass());
   private static final long serialVersionUID = 1L;
   @Autowired
   GltService gltService;
   private InputStream inputStream;
   
   public InputStream getInputStream()
   {
     return this.inputStream;
   }
   
   public String execute()
     throws Exception
   {
     String confid = ((String)getValue("confid")).trim();
     String whereCls = ((String)getValue("whereCls")).trim();
     String dsId = ((String)getValue("dsId")).trim();
     String query = (String)getValue("query");
     String dyndictWhereCls = (String)getValue("dynDictWhereCls");
     int rowstart = Integer.parseInt((String)this.dto.getValue("rowstart"));
     int pagesize = Integer.parseInt((String)this.dto.getValue("pageSize"));
     Map<String, String> dyndictWhereClsMap = null;
     if (query.length() > 0)
     {
       this.log.debug("分页查询条件，替换前是：" + whereCls);
       whereCls = GtService.parseWhereCls(whereCls, query);
       this.log.debug("分页查询条件，替换后是：" + whereCls);
     }
     if ((dsId != null) && (dsId.length() == 0)) {
       AppContextHolder.setCustomerType(dsId);
     }
     if (dyndictWhereCls.length() > 0) {
       dyndictWhereClsMap = (Map)JSONUtilities.parseJSON(dyndictWhereCls);
     }
     Glt glt = this.gltService.getGlt(confid);
     List<Map<String, String>> rows = this.gltService.getRowData(glt, whereCls, 
       dyndictWhereClsMap, rowstart, pagesize);
     long rowCount = this.gltService.getRowCount(glt, whereCls);
     Map<String, Object> jsonMap = new HashMap();
     
     jsonMap.put("headers", ((Map)rows.remove(0)).values());
     jsonMap.put("subs", this.gltService.getSubTotal(glt, whereCls));
     jsonMap.put("total", Long.valueOf(rowCount));
     jsonMap.put("rows", rows);
     StringBuffer rst = new JSONUtilities(1).parseObject(jsonMap);
     if ((dsId != null) && (dsId.length() == 0)) {
       AppContextHolder.clearCustomerType();
     }
     this.inputStream = new ByteArrayInputStream(rst.toString().getBytes());
     return "success";
   }
 }