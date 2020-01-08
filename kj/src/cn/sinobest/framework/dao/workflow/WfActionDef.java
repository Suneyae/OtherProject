 package cn.sinobest.framework.dao.workflow;
 
 import java.io.Serializable;
 
 public class WfActionDef
   implements Serializable, Cloneable
 {
   private static final long serialVersionUID = -54819302401576716L;
   private String SUBSYSTYPE;
   private String PROCESS_DEF_ID;
   private String PROCESS_DEF_NAME;
   private String ACTION_DEF_ID;
   private String NEXT_ACTION_DEF_ID;
   private String PRE_ACTION_DEF_ID;
   private String START_OR_END;
   private String ACTION_DEF_NAME;
   private String SORTNO;
   private String X_ACTION_DEF_IDS;
   private String BUSSURL;
   private String AAE100;
   private String INVALIDDATE;
   private String MEMO;
   private String BAE006;
   private String BAE002;
   private String BAE003;
   private String ATTR;
   
   public String getATTR()
   {
     return this.ATTR;
   }
   
   public void setATTR(String aTTR)
   {
     this.ATTR = aTTR;
   }
   
   public String getSUBSYSTYPE()
   {
     return this.SUBSYSTYPE;
   }
   
   public void setSUBSYSTYPE(String sUBSYSTYPE)
   {
     this.SUBSYSTYPE = sUBSYSTYPE;
   }
   
   public String getPROCESS_DEF_ID()
   {
     return this.PROCESS_DEF_ID;
   }
   
   public void setPROCESS_DEF_ID(String pROCESSDEFID)
   {
     this.PROCESS_DEF_ID = pROCESSDEFID;
   }
   
   public String getPROCESS_DEF_NAME()
   {
     return this.PROCESS_DEF_NAME;
   }
   
   public void setPROCESS_DEF_NAME(String pROCESSDEFNAME)
   {
     this.PROCESS_DEF_NAME = pROCESSDEFNAME;
   }
   
   public String getACTION_DEF_ID()
   {
     return this.ACTION_DEF_ID;
   }
   
   public void setACTION_DEF_ID(String aCTIONDEFID)
   {
     this.ACTION_DEF_ID = aCTIONDEFID;
   }
   
   public String getNEXT_ACTION_DEF_ID()
   {
     return this.NEXT_ACTION_DEF_ID;
   }
   
   public void setNEXT_ACTION_DEF_ID(String nEXTACTIONDEFID)
   {
     this.NEXT_ACTION_DEF_ID = nEXTACTIONDEFID;
   }
   
   public String getPRE_ACTION_DEF_ID()
   {
     return this.PRE_ACTION_DEF_ID;
   }
   
   public void setPRE_ACTION_DEF_ID(String pREACTIONDEFID)
   {
     this.PRE_ACTION_DEF_ID = pREACTIONDEFID;
   }
   
   public String getSTART_OR_END()
   {
     return this.START_OR_END;
   }
   
   public void setSTART_OR_END(String sTART_OR_END)
   {
     this.START_OR_END = sTART_OR_END;
   }
   
   public String getACTION_DEF_NAME()
   {
     return this.ACTION_DEF_NAME;
   }
   
   public void setACTION_DEF_NAME(String aCTIONDEFNAME)
   {
     this.ACTION_DEF_NAME = aCTIONDEFNAME;
   }
   
   public String getSORTNO()
   {
     return this.SORTNO;
   }
   
   public void setSORTNO(String sORTNO)
   {
     this.SORTNO = sORTNO;
   }
   
   public String getX_ACTION_DEF_IDS()
   {
     return this.X_ACTION_DEF_IDS;
   }
   
   public void setX_ACTION_DEF_IDS(String xACTIONDEFIDS)
   {
     this.X_ACTION_DEF_IDS = xACTIONDEFIDS;
   }
   
   public String getBUSSURL()
   {
     return this.BUSSURL;
   }
   
   public void setBUSSURL(String bUSSURL)
   {
     this.BUSSURL = bUSSURL;
   }
   
   public String getAAE100()
   {
     return this.AAE100;
   }
   
   public void setAAE100(String aAE100)
   {
     this.AAE100 = aAE100;
   }
   
   public String getINVALIDDATE()
   {
     return this.INVALIDDATE;
   }
   
   public void setINVALIDDATE(String iNVALIDDATE)
   {
     this.INVALIDDATE = iNVALIDDATE;
   }
   
   public String getMEMO()
   {
     return this.MEMO;
   }
   
   public void setMEMO(String mEMO)
   {
     this.MEMO = mEMO;
   }
   
   public String getBAE006()
   {
     return this.BAE006;
   }
   
   public void setBAE006(String bAE006)
   {
     this.BAE006 = bAE006;
   }
   
   public String getBAE002()
   {
     return this.BAE002;
   }
   
   public void setBAE002(String bAE002)
   {
     this.BAE002 = bAE002;
   }
   
   public String getBAE003()
   {
     return this.BAE003;
   }
   
   public void setBAE003(String bAE003)
   {
     this.BAE003 = bAE003;
   }
   
   public Object clone()
     throws CloneNotSupportedException
   {
     return super.clone();
   }
 }