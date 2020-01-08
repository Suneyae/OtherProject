 package cn.sinobest.framework.dao.workflow;
 
 import java.io.Serializable;
 
 public class WfWorkItem
   implements Serializable
 {
   private static final long serialVersionUID = -3961723825547635565L;
   private Long WORK_ITEM_ID;
   private String BAE007;
   private String ACTION_DEF_ID;
   private String ACTION_DEF_NAME;
   private String STATE;
   private Long PRE_WI_ID;
   private String WORK_TYPE;
   private String IS_RETURN;
   private String OPERID;
   private String X_OPRATOR_IDS;
   private Long START_TIME;
   private Long COMPLETE_TIME;
   private String FILTER_OPR;
   private String AAE100;
   private String MEMO;
   private String BAE006;
   private String BAE002;
   private Long BAE003;
   private String YWBAE007;
   private Long OPERUNITID;
   
   public Long getOPERUNITID()
   {
     return this.OPERUNITID;
   }
   
   public void setOPERUNITID(Long oPERUNITID)
   {
     this.OPERUNITID = oPERUNITID;
   }
   
   public String getYWBAE007()
   {
     return this.YWBAE007;
   }
   
   public void setYWBAE007(String yWBAE007)
   {
     this.YWBAE007 = yWBAE007;
   }
   
   public Long getWORK_ITEM_ID()
   {
     return this.WORK_ITEM_ID;
   }
   
   public Long getPRE_WI_ID()
   {
     return this.PRE_WI_ID;
   }
   
   public Long getSTART_TIME()
   {
     return this.START_TIME;
   }
   
   public Long getCOMPLETE_TIME()
   {
     return this.COMPLETE_TIME;
   }
   
   public Long getBAE003()
   {
     return this.BAE003;
   }
   
   public String getBAE007()
   {
     return this.BAE007;
   }
   
   public void setBAE007(String bAE007)
   {
     this.BAE007 = bAE007;
   }
   
   public String getACTION_DEF_ID()
   {
     return this.ACTION_DEF_ID;
   }
   
   public void setACTION_DEF_ID(String aCTIONDEFID)
   {
     this.ACTION_DEF_ID = aCTIONDEFID;
   }
   
   public String getACTION_DEF_NAME()
   {
     return this.ACTION_DEF_NAME;
   }
   
   public void setACTION_DEF_NAME(String aCTIONDEFNAME)
   {
     this.ACTION_DEF_NAME = aCTIONDEFNAME;
   }
   
   public String getSTATE()
   {
     return this.STATE;
   }
   
   public void setSTATE(String sTATE)
   {
     this.STATE = sTATE;
   }
   
   public String getWORK_TYPE()
   {
     return this.WORK_TYPE;
   }
   
   public void setWORK_TYPE(String wORKTYPE)
   {
     this.WORK_TYPE = wORKTYPE;
   }
   
   public String getIS_RETURN()
   {
     return this.IS_RETURN;
   }
   
   public void setIS_RETURN(String iSRETURN)
   {
     this.IS_RETURN = iSRETURN;
   }
   
   public String getOPERID()
   {
     return this.OPERID;
   }
   
   public void setOPERID(String oPERID)
   {
     this.OPERID = oPERID;
   }
   
   public String getX_OPRATOR_IDS()
   {
     return this.X_OPRATOR_IDS;
   }
   
   public void setX_OPRATOR_IDS(String xOPRATORIDS)
   {
     this.X_OPRATOR_IDS = xOPRATORIDS;
   }
   
   public String getFILTER_OPR()
   {
     return this.FILTER_OPR;
   }
   
   public void setFILTER_OPR(String fILTEROPR)
   {
     this.FILTER_OPR = fILTEROPR;
   }
   
   public String getAAE100()
   {
     return this.AAE100;
   }
   
   public void setAAE100(String aAE100)
   {
     this.AAE100 = aAE100;
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
   
   public void setWORK_ITEM_ID(Long wORKITEMID)
   {
     this.WORK_ITEM_ID = wORKITEMID;
   }
   
   public void setPRE_WI_ID(Long pREWIID)
   {
     this.PRE_WI_ID = pREWIID;
   }
   
   public void setSTART_TIME(Long sTARTTIME)
   {
     this.START_TIME = sTARTTIME;
   }
   
   public void setCOMPLETE_TIME(Long cOMPLETETIME)
   {
     this.COMPLETE_TIME = cOMPLETETIME;
   }
   
   public void setBAE003(Long bAE003)
   {
     this.BAE003 = bAE003;
   }
   
   public boolean equals(Object obj)
   {
     WfWorkItem o = (WfWorkItem)obj;
     if (this.WORK_ITEM_ID.equals(o.getWORK_ITEM_ID())) {
       return true;
     }
     return false;
   }
 }