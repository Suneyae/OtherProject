 package cn.sinobest.framework.service.workflow;
 
 import java.io.Serializable;
 
 public class SubmitWfParams
   implements Serializable
 {
   private static final long serialVersionUID = 5963936350661613693L;
   private String pid;
   private String wid;
   private IWorkflow.SubmitType submitType;
   private String nextDefId;
   private String jbr;
   private String bae006;
   private String keyData;
   private String accepter;
   private String operUnitId;
   private String operUnitType;
   private String comment;
   
   public String getOperUnitType()
   {
     return this.operUnitType;
   }
   
   public void setOperUnitType(String operUnitType)
   {
     this.operUnitType = operUnitType;
   }
   
   public String getOperUnitId()
   {
     return this.operUnitId;
   }
   
   public void setOperUnitId(String operUnitId)
   {
     this.operUnitId = operUnitId;
   }
   
   public String getPid()
   {
     return this.pid;
   }
   
   public void setPid(String pid)
   {
     this.pid = pid;
   }
   
   public String getWid()
   {
     return this.wid;
   }
   
   public void setWid(String wid)
   {
     this.wid = wid;
   }
   
   public String getNextDefId()
   {
     return this.nextDefId;
   }
   
   public void setNextDefId(String nextDefId)
   {
     this.nextDefId = nextDefId;
   }
   
   public IWorkflow.SubmitType getSubmitType()
   {
     return this.submitType;
   }
   
   public void setSubmitType(IWorkflow.SubmitType submitType)
   {
     this.submitType = submitType;
   }
   
   public String getJbr()
   {
     return this.jbr;
   }
   
   public void setJbr(String jbr)
   {
     this.jbr = jbr;
   }
   
   public String getBae006()
   {
     return this.bae006;
   }
   
   public void setBae006(String bae006)
   {
     this.bae006 = bae006;
   }
   
   public String getKeyData()
   {
     return this.keyData;
   }
   
   public void setKeyData(String keyData)
   {
     this.keyData = keyData;
   }
   
   public String getAccepter()
   {
     return this.accepter;
   }
   
   public void setAccepter(String accepter)
   {
     this.accepter = accepter;
   }
   
   public String getComment()
   {
     return this.comment;
   }
   
   public void setComment(String comment)
   {
     this.comment = comment;
   }
 }