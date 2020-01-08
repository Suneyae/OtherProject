 package cn.sinobest.framework.web;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.service.json.JSONUtilities;
 import cn.sinobest.framework.service.tags.TreeService;
 import java.io.ByteArrayInputStream;
 import java.io.InputStream;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.List;
 import java.util.Map;
 import org.apache.log4j.Logger;
 import org.springframework.beans.factory.annotation.Autowired;
 
 public class TreeAction
   extends BaseActionSupport
 {
   private static Logger log = Logger.getLogger(TreeAction.class);
   private static final long serialVersionUID = 1L;
   private InputStream inputStream;
   @Autowired
   TreeService treeService;
   
   public InputStream getInputStream()
   {
     return this.inputStream;
   }
   
   public String execute()
     throws Exception
   {
     String key = (String)getValue("key");
     String view = (String)getValue("view");
     String depthStr = (String)getValue("depth");
     String whereCls = (String)getValue("whereCls");
     String orderSql = (String)getValue("orderSql");
     String showRoot = (String)getValue("showRoot");
     String bussFuncId = (String)getValue("bussFuncId");
     
     Object selectedIn = getValue("selected");
     List<String> selected = null;
     if ((selectedIn instanceof String))
     {
       selected = new ArrayList(1);
       if (((String)selectedIn).length() > 0) {
         selected.add((String)selectedIn);
       }
     }
     else
     {
       selected = Arrays.asList((String[])selectedIn);
     }
     key = key.trim();
     if (key.length() % 2 != 0) {
       throw new AppException("EFW0117", null, new Object[] { key });
     }
     int depth = 1;
     if (depthStr.length() != 0) {
       try
       {
         depth = Integer.parseInt(depthStr);
       }
       catch (NumberFormatException e)
       {
         depth = 1;
       }
     }
     List<Map<String, Object>> roots = this.treeService.getTreeModel(view, key, 
       depth, whereCls, orderSql, bussFuncId, 
       Boolean.valueOf(showRoot).booleanValue());
     if (depth != -1) {
       this.treeService.enableAjax(roots, true);
     }
     if ((selected != null) && (!selected.isEmpty())) {
       this.treeService.initSelected(roots, selected);
     }
     StringBuffer rstString = new JSONUtilities(1)
       .parseObject(roots);
     this.inputStream = new ByteArrayInputStream(rstString.toString().getBytes());
     return "success";
   }
 }