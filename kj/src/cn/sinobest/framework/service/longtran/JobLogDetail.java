 package cn.sinobest.framework.service.longtran;
 
 public class JobLogDetail
 {
   private Long LOGID;
   private String KEY;
   private int STEP;
   private String STEPNAME;
   private String CLZT;
   private Long STARTTIME;
   private Long ENDTIME;
   private String BZ;
   private String TRANSID;
   
   public JobLogDetail() {}
   
   public JobLogDetail(Long LogId, String key, String transId)
   {
     this.LOGID = LogId;
     this.KEY = key;
     this.TRANSID = transId;
   }
   
   public String getTRANSID()
   {
     return this.TRANSID;
   }
   
   public void setTRANSID(String tRANSID)
   {
     this.TRANSID = tRANSID;
   }
   
   public Long getLOGID()
   {
     return this.LOGID;
   }
   
   public void setLOGID(Long lOGID)
   {
     this.LOGID = lOGID;
   }
   
   public String getKEY()
   {
     return this.KEY;
   }
   
   public void setKEY(String kEY)
   {
     this.KEY = kEY;
   }
   
   public int getSTEP()
   {
     return this.STEP;
   }
   
   public void setSTEP(int sTEP)
   {
     this.STEP = sTEP;
   }
   
   public String getSTEPNAME()
   {
     return this.STEPNAME;
   }
   
   public void setSTEPNAME(String sTEPNAME)
   {
     this.STEPNAME = sTEPNAME;
   }
   
   public String getCLZT()
   {
     return this.CLZT;
   }
   
   public void setCLZT(String cLZT)
   {
     this.CLZT = cLZT;
   }
   
   public Long getSTARTTIME()
   {
     return this.STARTTIME;
   }
   
   public void setSTARTTIME(Long sTARTTIME)
   {
     this.STARTTIME = sTARTTIME;
   }
   
   public Long getENDTIME()
   {
     return this.ENDTIME;
   }
   
   public void setENDTIME(Long eNDTIME)
   {
     this.ENDTIME = eNDTIME;
   }
   
   public String getBZ()
   {
     return this.BZ;
   }
   
   public void setBZ(String bZ)
   {
     this.BZ = bZ;
   }
 }