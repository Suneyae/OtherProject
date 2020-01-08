 package cn.sinobest.framework.service.longtran;
 
 import java.io.Serializable;
 
 public class LongTran
   implements Serializable
 {
   private static final long serialVersionUID = -5984692058878771233L;
   private String TRANSID;
   private String SUBSYS_TYPE;
   private String CTYPE;
   private String PROCESS;
   private String CRON_EXPR;
   private String MEMO;
   private String TRIGGER_TYPE;
   private String KEY;
   
   public String getKEY()
   {
     return this.KEY;
   }
   
   public void setKEY(String kEY)
   {
     this.KEY = kEY;
   }
   
   public String getTRIGGER_TYPE()
   {
     return this.TRIGGER_TYPE;
   }
   
   public void setTRIGGER_TYPE(String tRIGGER_TYPE)
   {
     this.TRIGGER_TYPE = tRIGGER_TYPE;
   }
   
   public String getTRANSID()
   {
     return this.TRANSID;
   }
   
   public void setTRANSID(String tRANSID)
   {
     this.TRANSID = tRANSID;
   }
   
   public String getSUBSYS_TYPE()
   {
     return this.SUBSYS_TYPE;
   }
   
   public void setSUBSYS_TYPE(String sUBSYS_TYPE)
   {
     this.SUBSYS_TYPE = sUBSYS_TYPE;
   }
   
   public String getCTYPE()
   {
     return this.CTYPE;
   }
   
   public void setCTYPE(String cTYPE)
   {
     this.CTYPE = cTYPE;
   }
   
   public String getPROCESS()
   {
     return this.PROCESS;
   }
   
   public void setPROCESS(String pROCESS)
   {
     this.PROCESS = pROCESS;
   }
   
   public String getCRON_EXPR()
   {
     return this.CRON_EXPR;
   }
   
   public void setCRON_EXPR(String cRON_EXPR)
   {
     this.CRON_EXPR = cRON_EXPR;
   }
   
   public String getMEMO()
   {
     return this.MEMO;
   }
   
   public void setMEMO(String mEMO)
   {
     this.MEMO = mEMO;
   }
 }