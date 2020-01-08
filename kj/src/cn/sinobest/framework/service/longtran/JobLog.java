 package cn.sinobest.framework.service.longtran;
 
 public class JobLog
 {
   private Long LOGID;
   private String KEY;
   private String XTJGDM;
   private String TRANSID;
   private String PROCESS;
   private Long STARTTIME;
   private Long ENDTIME;
   private String BZ;
   
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
   
   public String getXTJGDM()
   {
     return this.XTJGDM;
   }
   
   public void setXTJGDM(String xTJGDM)
   {
     this.XTJGDM = xTJGDM;
   }
   
   public String getTRANSID()
   {
     return this.TRANSID;
   }
   
   public void setTRANSID(String tRANSID)
   {
     this.TRANSID = tRANSID;
   }
   
   public String getPROCESS()
   {
     return this.PROCESS;
   }
   
   public void setPROCESS(String pROCESS)
   {
     this.PROCESS = pROCESS;
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