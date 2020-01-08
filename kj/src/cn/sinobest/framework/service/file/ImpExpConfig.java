 package cn.sinobest.framework.service.file;
 
 import java.io.Serializable;
 import java.util.HashMap;
 import java.util.Map;
 
 public class ImpExpConfig
   implements Serializable
 {
   public static enum Align
   {
     LEFT,  RIGHT;
   }
   
   public static enum Type
   {
     DOUBLE(8),  INTEGER(4),  STRING(12);
     
     public final int sqlType;
     
     private Type(int sqlType)
     {
       this.sqlType = sqlType;
     }
     
     public Object eval(String source)
     {
       return source;
     }
   }
   
   public static final Map<Type, Character> TYPE2DBF = new HashMap(5);
   private static final long serialVersionUID = 1L;
   String id;
   String[] sqlStr;
   Map<String, String> dictInfo;
   String[] hashSqlStr;
   String templateFile;
   Map<String, Object>[][] renders;
   String delimiter;
   
   static
   {
     TYPE2DBF.put(Type.STRING, Character.valueOf('C'));
     TYPE2DBF.put(Type.INTEGER, Character.valueOf('N'));
     TYPE2DBF.put(Type.DOUBLE, Character.valueOf('N'));
   }
   
   public String getId()
   {
     return this.id;
   }
   
   public void setId(String id)
   {
     this.id = id;
   }
   
   public String[] getSqlStr()
   {
     return this.sqlStr;
   }
   
   public void setSqlStr(String[] sqlStr)
   {
     this.sqlStr = sqlStr;
   }
   
   public Map<String, String> getDictInfo()
   {
     return this.dictInfo;
   }
   
   public void setDictInfo(Map<String, String> dictInfo)
   {
     this.dictInfo = dictInfo;
   }
   
   public String[] getHashSqlStr()
   {
     return this.hashSqlStr;
   }
   
   public void setHashSqlStr(String[] hashSqlStr)
   {
     this.hashSqlStr = hashSqlStr;
   }
   
   public String getTemplateFile()
   {
     return this.templateFile;
   }
   
   public void setTemplateFile(String templateFile)
   {
     this.templateFile = templateFile;
   }
   
   public Map<String, Object>[][] getRenders()
   {
     return this.renders;
   }
   
   public void setRenders(Map<String, Object>[][] renders)
   {
     this.renders = renders;
   }
   
   public void setDelimiter(String delimiter)
   {
     this.delimiter = delimiter;
   }
   
   public String getDelimiter()
   {
     return this.delimiter;
   }
 }