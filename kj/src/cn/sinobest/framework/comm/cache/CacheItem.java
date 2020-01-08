 package cn.sinobest.framework.comm.cache;
 
 import java.io.Serializable;
 
 public class CacheItem<T>
   implements Serializable
 {
   private String code;
   private String subCode;
   private T Item;
   
   public T getItem()
   {
     return this.Item;
   }
   
   public void setItem(T item)
   {
     this.Item = item;
   }
   
   public String getCode()
   {
     return this.code;
   }
   
   public void setCode(String code)
   {
     this.code = code;
   }
   
   public String getSubCode()
   {
     return this.subCode;
   }
   
   public void setSubCode(String subCode)
   {
     this.subCode = subCode;
   }
 }