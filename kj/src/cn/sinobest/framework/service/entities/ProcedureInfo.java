 package cn.sinobest.framework.service.entities;
 
 import java.io.Serializable;
 import java.util.List;
 
 public class ProcedureInfo
   implements Serializable
 {
   private static final long serialVersionUID = 1L;
   private String callSql;
   private String name;
   private List<Param> params;
   private int returnDataType;
   
   public static class Param
   {
     private int columnType;
     private int dataType;
     private String name;
     private String defaultValue;
     private String typeName;
     
     public int getColumnType()
     {
       return this.columnType;
     }
     
     public void setColumnType(int columnType)
     {
       this.columnType = columnType;
     }
     
     public int getDataType()
     {
       return this.dataType;
     }
     
     public void setDataType(int dataType)
     {
       this.dataType = dataType;
     }
     
     public String getName()
     {
       return this.name;
     }
     
     public void setName(String name)
     {
       this.name = name;
     }
     
     public String getDefaultValue()
     {
       return this.defaultValue;
     }
     
     public void setDefaultValue(String defaultValue)
     {
       this.defaultValue = defaultValue;
     }
     
     public String getTypeName()
     {
       return this.typeName;
     }
     
     public void setTypeName(String typeName)
     {
       this.typeName = typeName;
     }
   }
   
   public String getCallSql()
   {
     return this.callSql;
   }
   
   public void setCallSql(String callSql)
   {
     this.callSql = callSql;
   }
   
   public String getName()
   {
     return this.name;
   }
   
   public void setName(String name)
   {
     this.name = name;
   }
   
   public List<Param> getParams()
   {
     return this.params;
   }
   
   public void setParams(List<Param> params)
   {
     this.params = params;
   }
   
   public int getReturnDataType()
   {
     return this.returnDataType;
   }
   
   public void setReturnDataType(int returnDataType)
   {
     this.returnDataType = returnDataType;
   }
 }