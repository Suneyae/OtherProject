 package cn.sinobest.framework.dao.workflow;
 
 import java.io.Serializable;
 
 public class WfActionDef2Task
   implements Serializable, Cloneable
 {
   private String PROCESS_DEF_ID;
   private String ACTION_DEF_ID;
   private String NEXT_ACTION_DEF_ID;
   private String TASK;
   
   public String getPROCESS_DEF_ID()
   {
     return this.PROCESS_DEF_ID;
   }
   
   public void setPROCESS_DEF_ID(String pROCESS_DEF_ID)
   {
     this.PROCESS_DEF_ID = pROCESS_DEF_ID;
   }
   
   public String getACTION_DEF_ID()
   {
     return this.ACTION_DEF_ID;
   }
   
   public void setACTION_DEF_ID(String aCTION_DEF_ID)
   {
     this.ACTION_DEF_ID = aCTION_DEF_ID;
   }
   
   public String getNEXT_ACTION_DEF_ID()
   {
     return this.NEXT_ACTION_DEF_ID;
   }
   
   public void setNEXT_ACTION_DEF_ID(String nEXT_ACTION_DEF_ID)
   {
     this.NEXT_ACTION_DEF_ID = nEXT_ACTION_DEF_ID;
   }
   
   public String getTASK()
   {
     return this.TASK;
   }
   
   public void setTASK(String tASK)
   {
     this.TASK = tASK;
   }
   
   public Object clone()
     throws CloneNotSupportedException
   {
     return super.clone();
   }
 }