 package cn.sinobest.framework.dao.workflow;
 
 import java.io.Serializable;
 
 public class WfProcessInstance
   implements Serializable
 {
   private static final long serialVersionUID = -514317977701546124L;
   private String BAE007;
   private String PROCESS_DEF_ID;
   private String PROCESS_DEF_NAME;
   private String ARCHIVEID;
   private String STATUS;
   private String PROCESS_KEY_INFO;
   private Long AAB001;
   private Long AAC001;
   private String REJECT_REASON;
   private String PARENTINSID;
   private String RELATEINSID;
   private Long PREVIOUS_WI_ID;
   private Long CURRENT_WI_ID;
   private Long COMPLETE_TIME;
   private String BAE006;
   private String BAE002;
   private Long BAE003;
   private String YWBAE007;
   
   public String getYWBAE007()
   {
     return this.YWBAE007;
   }
   
   public void setYWBAE007(String yWBAE007)
   {
     this.YWBAE007 = yWBAE007;
   }
   
   public String getBAE007()
   {
     return this.BAE007;
   }
   
   public void setBAE007(String bAE007)
   {
     this.BAE007 = bAE007;
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
   
   public String getARCHIVEID()
   {
     return this.ARCHIVEID;
   }
   
   public void setARCHIVEID(String aRCHIVEID)
   {
     this.ARCHIVEID = aRCHIVEID;
   }
   
   public String getSTATUS()
   {
     return this.STATUS;
   }
   
   public void setSTATUS(String sTATUS)
   {
     this.STATUS = sTATUS;
   }
   
   public String getPROCESS_KEY_INFO()
   {
     return this.PROCESS_KEY_INFO;
   }
   
   public void setPROCESS_KEY_INFO(String pROCESSKEYINFO)
   {
     this.PROCESS_KEY_INFO = pROCESSKEYINFO;
   }
   
   public Long getAAB001()
   {
     return this.AAB001;
   }
   
   public void setAAB001(Long aAB001)
   {
     this.AAB001 = aAB001;
   }
   
   public Long getAAC001()
   {
     return this.AAC001;
   }
   
   public void setAAC001(Long aAC001)
   {
     this.AAC001 = aAC001;
   }
   
   public String getREJECT_REASON()
   {
     return this.REJECT_REASON;
   }
   
   public void setREJECT_REASON(String rEJECTREASON)
   {
     this.REJECT_REASON = rEJECTREASON;
   }
   
   public String getPARENTINSID()
   {
     return this.PARENTINSID;
   }
   
   public void setPARENTINSID(String pARENTINSID)
   {
     this.PARENTINSID = pARENTINSID;
   }
   
   public String getRELATEINSID()
   {
     return this.RELATEINSID;
   }
   
   public void setRELATEINSID(String rELATEINSID)
   {
     this.RELATEINSID = rELATEINSID;
   }
   
   public Long getPREVIOUS_WI_ID()
   {
     return this.PREVIOUS_WI_ID;
   }
   
   public void setPREVIOUS_WI_ID(Long pREVIOUSWIID)
   {
     this.PREVIOUS_WI_ID = pREVIOUSWIID;
   }
   
   public Long getCURRENT_WI_ID()
   {
     return this.CURRENT_WI_ID;
   }
   
   public void setCURRENT_WI_ID(Long cURRENTWIID)
   {
     this.CURRENT_WI_ID = cURRENTWIID;
   }
   
   public Long getCOMPLETE_TIME()
   {
     return this.COMPLETE_TIME;
   }
   
   public void setCOMPLETE_TIME(Long cOMPLETETIME)
   {
     this.COMPLETE_TIME = cOMPLETETIME;
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
   
   public Long getBAE003()
   {
     return this.BAE003;
   }
   
   public void setBAE003(Long bAE003)
   {
     this.BAE003 = bAE003;
   }
 }