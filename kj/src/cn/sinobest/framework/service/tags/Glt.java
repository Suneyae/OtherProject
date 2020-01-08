 package cn.sinobest.framework.service.tags;
 
 import java.io.Serializable;
 import java.util.Map;
 
 public class Glt
   implements Serializable
 {
   String id;
   String title;
   String type;
   String selectStr;
   String subTotalStr;
   Map<String, String> dict;
   int fixcolumns;
   int columns;
   Map<String, Object>[][] fixHead;
   Map<String, Object>[][] colHead;
   Map<String, Object>[] lastRow;
   String[] fields;
   
   public String[] getFields()
   {
     return this.fields;
   }
   
   public void setFields(String[] fields)
   {
     this.fields = fields;
   }
   
   public void setId(String id)
   {
     this.id = id;
   }
   
   public void setTitle(String title)
   {
     this.title = title;
   }
   
   public void setType(String type)
   {
     this.type = type;
   }
   
   public void setSelectStr(String selectStr)
   {
     this.selectStr = selectStr;
   }
   
   public void setSubTotalStr(String subTotalStr)
   {
     this.subTotalStr = subTotalStr;
   }
   
   public void setDict(Map<String, String> dict)
   {
     this.dict = dict;
   }
   
   public void setFixcolumns(int fixcolumns)
   {
     this.fixcolumns = fixcolumns;
   }
   
   public void setFixHead(Map<String, Object>[][] fixHead)
   {
     this.fixHead = fixHead;
   }
   
   public void setColHead(Map<String, Object>[][] colHead)
   {
     this.colHead = colHead;
   }
   
   public String getId()
   {
     return this.id;
   }
   
   public String getTitle()
   {
     return this.title;
   }
   
   public String getType()
   {
     return this.type;
   }
   
   public String getSelectStr()
   {
     return this.selectStr;
   }
   
   public String getSubTotalStr()
   {
     return this.subTotalStr;
   }
   
   public Map<String, String> getDict()
   {
     return this.dict;
   }
   
   public int getFixcolumns()
   {
     return this.fixcolumns;
   }
   
   public Map<String, Object>[][] getFixHead()
   {
     return this.fixHead;
   }
   
   public Map<String, Object>[][] getColHead()
   {
     return this.colHead;
   }
   
   public int getColumns()
   {
     return this.columns;
   }
   
   public void setColumns(int columns)
   {
     this.columns = columns;
   }
 }