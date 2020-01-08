 package cn.sinobest.framework.service.workflow;
 
 import java.io.Serializable;
 
 public class StartWfParams
   implements Serializable
 {
   private String wfDefName;
   private String keyData;
   private String aab001;
   private String aac001;
   private String archiveId;
   private String comment;
   private String ppid;
   private String rid;
   private String operId;
   private String bae006;
   private String commDo;
   private boolean doCusService;
   
   public String getWfDefName()
   {
     return this.wfDefName;
   }
   
   public void setWfDefName(String wfDefName)
   {
     this.wfDefName = wfDefName;
   }
   
   public String getKeyData()
   {
     return this.keyData;
   }
   
   public void setKeyData(String keyData)
   {
     this.keyData = keyData;
   }
   
   public String getAab001()
   {
     return this.aab001;
   }
   
   public void setAab001(String aab001)
   {
     this.aab001 = aab001;
   }
   
   public String getAac001()
   {
     return this.aac001;
   }
   
   public void setAac001(String aac001)
   {
     this.aac001 = aac001;
   }
   
   public String getArchiveId()
   {
     return this.archiveId;
   }
   
   public void setArchiveId(String archiveId)
   {
     this.archiveId = archiveId;
   }
   
   public String getComment()
   {
     return this.comment;
   }
   
   public void setComment(String comment)
   {
     this.comment = comment;
   }
   
   public String getPpid()
   {
     return this.ppid;
   }
   
   public void setPpid(String ppid)
   {
     this.ppid = ppid;
   }
   
   public String getRid()
   {
     return this.rid;
   }
   
   public void setRid(String rid)
   {
     this.rid = rid;
   }
   
   public String getOperId()
   {
     return this.operId;
   }
   
   public void setOperId(String operId)
   {
     this.operId = operId;
   }
   
   public String getBae006()
   {
     return this.bae006;
   }
   
   public void setBae006(String bae006)
   {
     this.bae006 = bae006;
   }
   
   public String getCommDo()
   {
     return this.commDo;
   }
   
   public void setCommDo(String commDo)
   {
     this.commDo = commDo;
   }
   
   public boolean isDoCusService()
   {
     return this.doCusService;
   }
   
   public void setDoCusService(boolean doCusService)
   {
     this.doCusService = doCusService;
   }
 }