 package cn.sinobest.framework.dao.mapper;
 
 import java.io.Serializable;
 
 public class SQLModel
   implements Serializable
 {
   private String sql;
   
   public String getSql()
   {
     return this.sql;
   }
   
   public void setSql(String sql)
   {
     this.sql = sql;
   }
 }