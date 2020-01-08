 package cn.sinobest.framework.service;
 
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.dao.CommDAO;
 import cn.sinobest.framework.util.Util;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
 
 @Service
 public class MutlSelectService
 {
   @Autowired
   private CommDAO commDAO;
   
   public Map<String, Object> addOne(IDTO dto)
   {
     Map<String, Object> rtn = new HashMap();
     String tableName = (String)Util.nvl(dto.getValue("TABLE_NAME"));
     String id = (String)Util.nvl(dto.getValue("ID"));
     String value = (String)Util.nvl(dto.getValue("VALUE"));
     Map<String, Object> paramsMap = new HashMap();
     paramsMap.put("TABLE_NAME", tableName);
     paramsMap.put("ID", id);
     paramsMap.put("VALUE", value);
     this.commDAO.insert("FW_CONFIG.FW_MUTL_I", paramsMap);
     return rtn;
   }
   
   public Map<String, Object> delOne(IDTO dto)
   {
     Map<String, Object> rtn = new HashMap();
     String tableName = (String)Util.nvl(dto.getValue("TABLE_NAME"));
     String id = (String)Util.nvl(dto.getValue("ID"));
     String value = (String)Util.nvl(dto.getValue("VALUE"));
     Map<String, Object> paramsMap = new HashMap();
     paramsMap.put("TABLE_NAME", tableName);
     paramsMap.put("ID", id);
     paramsMap.put("VALUE", value);
     this.commDAO.delete("FW_CONFIG.FW_MUTL_D", paramsMap);
     return rtn;
   }
   
   public Map<String, Object> addBatch(IDTO dto)
   {
     Map<String, Object> rtn = new HashMap();
     String tableName = (String)Util.nvl(dto.getValue("TABLE_NAME"));
     String id = (String)Util.nvl(dto.getValue("ID"));
     String[] arrValues = ((String)Util.nvl(dto.getValue("VALUES"))).split(",");
     List<Map<String, Object>> pList = new ArrayList();
     for (String value : arrValues)
     {
       Map<String, Object> pMap = new HashMap();
       pMap.put("TABLE_NAME", tableName);
       pMap.put("ID", id);
       pMap.put("VALUE", value);
       pList.add(pMap);
     }
     this.commDAO.batchInsert("FW_CONFIG.FW_MUTL_I2", pList);
     return rtn;
   }
   
   public Map<String, Object> delBatch(IDTO dto)
   {
     Map<String, Object> rtn = new HashMap();
     String tableName = (String)Util.nvl(dto.getValue("TABLE_NAME"));
     String id = (String)Util.nvl(dto.getValue("ID"));
     String[] arrValues = ((String)Util.nvl(dto.getValue("VALUES"))).split(",");
     List<Map<String, Object>> pList = new ArrayList();
     for (String value : arrValues)
     {
       Map<String, Object> pMap = new HashMap();
       pMap.put("TABLE_NAME", tableName);
       pMap.put("ID", id);
       pMap.put("VALUE", value);
       pList.add(pMap);
     }
     this.commDAO.batchInsert("FW_CONFIG.FW_MUTL_D", pList);
     return rtn;
   }
   
   public Map<String, Object> addAll(IDTO dto)
   {
     Map<String, Object> rtn = new HashMap();
     String tableName = (String)Util.nvl(dto.getValue("TABLE_NAME"));
     String id = (String)Util.nvl(dto.getValue("ID"));
     String querysqlWhereCls = (String)dto.getValue("QUERYSQL_WHERECLS");
     
 
     String querysqlId = (String)Util.nvl(dto.getValue("QUERYSQL_ID"));
     Map<String, Object> pMap1 = new HashMap(1, 1.0F);
     pMap1.put("ID", querysqlId);
     Map<String, Object> selectStrMap = this.commDAO.selectOne("FW_CONFIG.FW_QUERYSQL_Q", pMap1);
     String selectStr = (String)selectStrMap.get("SELECTSTR");
     String columnName = getColumnName(selectStr);
     
     Map<String, Object> pMap = new HashMap();
     pMap.put("TABLE_NAME", tableName);
     pMap.put("SELECTSTR", selectStr);
     pMap.put("QUERYSQL_WHERECLS", querysqlWhereCls);
     pMap.put("COLUMN_NAME", columnName);
     pMap.put("ID", id);
     
     this.commDAO.insert("FW_CONFIG.FW_MUTL_I3", pMap);
     return rtn;
   }
   
   public Map<String, Object> delAll(IDTO dto)
   {
     Map<String, Object> rtn = new HashMap();
     String tableName = (String)Util.nvl(dto.getValue("TABLE_NAME"));
     String id = (String)Util.nvl(dto.getValue("ID"));
     Map<String, Object> pMap = new HashMap();
     pMap.put("TABLE_NAME", tableName);
     pMap.put("ID", id);
     this.commDAO.insert("FW_CONFIG.FW_MUTL_D2", pMap);
     return rtn;
   }
   
   private String getColumnName(String selectStr)
   {
     selectStr = selectStr.toLowerCase();
     int pos1 = selectStr.indexOf("select") + "select".length();
     int pos2 = selectStr.indexOf("from");
     return selectStr.substring(pos1, pos2).trim();
   }
 }