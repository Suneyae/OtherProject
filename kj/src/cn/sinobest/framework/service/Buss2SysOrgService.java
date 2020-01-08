 package cn.sinobest.framework.service;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IDAO;
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.util.Util;
 import java.io.PrintStream;
 import java.util.List;
 import java.util.Map;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
 
 @Service
 public class Buss2SysOrgService
 {
   @Autowired
   IDAO commDAO;
   public static final String KEY_BUSSFUNCID = "BUSSFUNCID";
   public static final String KEY_BAE001 = "BAE001";
   public static final String KEY_BAE006 = "BAE006";
   public static final String KEY_BUSSFUNCID_REGEX = "BUSSFUNCID_REGEX";
   public static final String KEY_BAE006_REGEX = "BAE006_REGEX";
   public static final String KEY_BAE001_REGEX = "BAE001_REGEX";
   
   public String getBuss2SysOrg(String bae006, String bussfuncId, String columnName)
   {
     if (columnName == null) {
       columnName = "BAE001";
     }
     List<Map<String, Object>> config = getConfig();
     StringBuilder query = new StringBuilder("(").append(columnName).append(
       " like '" + bae006 + "%' ");
     for (Map<String, Object> record : config) {
       if (((Pattern)record.get("BUSSFUNCID_REGEX")).matcher(bussfuncId).matches()) {
         if (((Pattern)record.get("BAE006_REGEX")).matcher(bae006).matches()) {
           query.append("or ").append(columnName).append(" like '").append(record.get("BAE001")).append("' ");
         }
       }
     }
     query.append(")");
     return query.toString();
   }
   
   public static String getBuss2SysOrgS(String bae006, String bussfuncId, String columnName)
   {
     return 
       ((Buss2SysOrgService)Util.getBean("buss2SysOrgService", Buss2SysOrgService.class)).getBuss2SysOrg(bae006, bussfuncId, null);
   }
   
   public String getBuss2SysOrg(String bae006, String columnName)
   {
     if (columnName == null) {
       columnName = "BAE001";
     }
     List<Map<String, Object>> config = getConfig();
     StringBuilder query = new StringBuilder(" (").append(columnName)
       .append(" like '" + bae006 + "%' ");
     for (Map<String, Object> record : config) {
       if (((Pattern)record.get("BAE006_REGEX")).matcher(bae006).matches()) {
         query.append("or (").append(columnName).append(" like '").append(record.get("BAE001")).append("' and bussfuncid like '").append(record.get("BUSSFUNCID")).append("' escape '\\' )");
       }
     }
     query.append(")");
     return query.toString();
   }
   
   public static String getBuss2SysOrgS(String bae006, String columnName)
   {
     return 
       ((Buss2SysOrgService)Util.getBean("buss2SysOrgService", Buss2SysOrgService.class)).getBuss2SysOrg(bae006, columnName);
   }
   
   public boolean matchBuss2SysOrg(String bae006, String bussfuncid, String bae001)
   {
     if (bae001.indexOf(bae006) == 0) {
       return true;
     }
     List<Map<String, Object>> config = getConfig();
     for (Map<String, Object> record : config) {
       if (((Pattern)record.get("BUSSFUNCID_REGEX")).matcher(bussfuncid).matches()) {
         if (((Pattern)record.get("BAE006_REGEX")).matcher(bae006).matches()) {
           if (((Pattern)record.get("BAE001_REGEX")).matcher(bae001).matches()) {
             return true;
           }
         }
       }
     }
     return false;
   }
   
   public boolean matchBuss2SysOrg(IDTO dto)
   {
     String bae001 = (String)dto.getValue("BAE001");
     String bae006 = (String)dto.getValue("BAE006");
     String bussfuncid = (String)dto.getValue("BUSSFUNCID");
     if ((Util.isEmpty(bae001)) || (Util.isEmpty(bae006)) || 
       (Util.isEmpty(bussfuncid))) {
       throw new AppException("BAE001、BAE006和BUSSIFUNCID不可为空！");
     }
     return matchBuss2SysOrg(bae006, bussfuncid, bae001);
   }
   
   public static boolean matchBuss2SysOrgS(String bae006, String bussfuncid, String bae001)
   {
     return 
       ((Buss2SysOrgService)Util.getBean("buss2SysOrgService", Buss2SysOrgService.class)).matchBuss2SysOrg(bae006, bussfuncid, bae001);
   }
   
   public List<Map<String, Object>> getConfig()
   {
     List<Map<String, Object>> config = this.commDAO.select(
       "FW_CONFIG.FW_BUSS2SYSORG_Q", null);
     for (Map<String, Object> record : config) {
       parse(record);
     }
     return config;
   }
   
   private static void parse(Map<String, Object> record)
   {
     String bussfuncid = doReplace((String)record.get("BUSSFUNCID"));
     Pattern p = Pattern.compile(bussfuncid);
     record.put("BUSSFUNCID_REGEX", p);
     
     String bae006 = doReplace((String)record.get("BAE006"));
     Pattern p2 = Pattern.compile(bae006);
     record.put("BAE006_REGEX", p2);
     
     String bae001 = doReplace((String)record.get("BAE001"));
     Pattern p3 = Pattern.compile(bae001);
     record.put("BAE001_REGEX", p3);
   }
   
   private static String doReplace(String source)
   {
     char[] _replasement2 = { '.', '*', '?' };
     StringBuilder sb = new StringBuilder(source);
     for (int index = 0; index < sb.length(); index++) {
       switch (sb.charAt(index))
       {
       case '_': 
         if ((index == 0) || (sb.charAt(index - 1) != '\\')) {
           sb.setCharAt(index, '.');
         }
         break;
       case '%': 
         if ((index == 0) || (sb.charAt(index - 1) != '\\'))
         {
           sb.deleteCharAt(index).insert(index, _replasement2);
           index += 2;
         }
         break;
       }
     }
     return sb.toString();
   }
   
   public static void main(String[] args)
   {
     System.out.println("escape '\\' ");
   }
 }