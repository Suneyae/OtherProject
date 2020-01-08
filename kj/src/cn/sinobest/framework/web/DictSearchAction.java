 package cn.sinobest.framework.web;
 
 import cn.sinobest.framework.service.json.JSONUtilities;
 import cn.sinobest.framework.service.tags.GltService;
 import cn.sinobest.framework.service.tags.GtService.DictComparator;
 import cn.sinobest.framework.util.ConfUtil;
 import java.io.ByteArrayInputStream;
 import java.io.InputStream;
 import java.util.ArrayList;
 import java.util.Collections;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import java.util.Map.Entry;
 import org.apache.log4j.Logger;
 import org.springframework.beans.factory.annotation.Autowired;
 
 public class DictSearchAction
   extends BaseActionSupport
 {
   Logger log = Logger.getLogger(getClass());
   private static final long serialVersionUID = 1L;
   @Autowired
   GltService gltService;
   private InputStream inputStream;
   private static final String AAA102 = "AAA102";
   private static final String AAA103 = "AAA103";
   
   public InputStream getInputStream()
   {
     return this.inputStream;
   }
   
   public String execute()
     throws Exception
   {
     String dict = ((String)getValue("configid")).trim();
     String query = (String)getValue("query");
     Map<String, Map<String, String>> dictSearchRst = null;
     if (dict.indexOf('@') != -1)
     {
       String[] subs = query.split("\\@");
       dictSearchRst = ConfUtil.getSubDict(subs[0], subs[1]);
     }
     else
     {
       dictSearchRst = ConfUtil.getDict(dict);
     }
     int length = 0;
     List<Map<String, String>> subDict = new ArrayList();
     List<String[]> rst = null;
     if ((dictSearchRst != null) && (!dictSearchRst.isEmpty()))
     {
       rst = new ArrayList(dictSearchRst.size());
       for (Map.Entry<String, Map<String, String>> entry : dictSearchRst.entrySet()) {
         if ((((String)entry.getKey()).indexOf(query) != -1) || 
           (((String)((Map)entry.getValue()).get("AAA103")).indexOf(query) != -1))
         {
           if (length < 50) {
             subDict.add((Map)entry.getValue());
           }
           length++;
         }
       }
     }
     if (!subDict.isEmpty())
     {
       Collections.sort(subDict, new GtService.DictComparator());
       for (Map<String, String> item : subDict)
       {
         String[] pair = { (String)item.get("AAA102"), (String)item.get("AAA103") };
         rst.add(pair);
       }
     }
     Map<String, Object> jsonMap = new HashMap();
     jsonMap.put("total", Integer.valueOf(length));
     jsonMap.put("rows", rst);
     StringBuffer rstString = new JSONUtilities(1).parseObject(jsonMap);
     this.inputStream = new ByteArrayInputStream(rstString.toString().getBytes());
     return "success";
   }
 }