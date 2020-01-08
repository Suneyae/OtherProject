 package cn.sinobest.framework.service.webservice;
 
 import cn.sinobest.framework.comm.iface.IDAO;
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.util.Util;
 import java.util.HashMap;
 import java.util.Map;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
 
 @Service
 public class HisTransService
 {
   public static final String IP = "IP";
   public static final String EXEC_NUM = "EXEC_NUM";
   public static final String FUNC_NO = "FUNC_NO";
   public static final String YWLSH = "YWLSH";
   public static final String EXEC_TIME = "EXEC_TIME";
   @Autowired
   private IDAO commDAO;
   
   public long updateExecNum(IDTO dto)
   {
     String ip = (String)dto.getValue("IP");
     
     Map<String, Object> qMap = new HashMap(3);
     qMap.put("IP", ip);
     Map<String, Object> oneLog = this.commDAO.selectOne("HISAPP.FW_HIS_LOG_Q", qMap);
     
     long newExecNum = 1L;long oldExecNum = 0L;
     boolean isExists = false;
     if (dto.getValue("EXEC_NUM") != null) {
       newExecNum = Long.parseLong((String)dto.getValue("EXEC_NUM"));
     }
     if ((oneLog != null) && (oneLog.get("EXEC_NUM") != null))
     {
       isExists = true;
       oldExecNum = Long.parseLong(Util.number2String((Number)oneLog.get("EXEC_NUM")));
     }
     qMap.put("YWLSH", (String)dto.getValue("YWLSH"));
     qMap.put("FUNC_NO", (String)dto.getValue("FUNC_NO"));
     qMap.put("EXEC_NUM", (String)dto.getValue("EXEC_NUM"));
     if (newExecNum > oldExecNum)
     {
       qMap.put("EXEC_NUM", String.valueOf(newExecNum));
       if (isExists) {
         this.commDAO.update("HISAPP.FW_HIS_LOG_U", qMap);
       } else {
         this.commDAO.insert("HISAPP.FW_HIS_LOG_I", qMap);
       }
     }
     else
     {
       qMap.put("FUNC_NO", (String)oneLog.get("FUNC_NO"));
       qMap.put("YWLSH", (String)oneLog.get("YWLSH"));
       qMap.put("EXEC_NUM", oneLog.get("EXEC_NUM"));
       qMap.put("EXEC_TIME", oneLog.get("EXEC_TIME"));
       this.commDAO.insert("HISAPP.FW_HIS_LOG_MX_I", qMap);
       
       qMap.put("FUNC_NO", (String)dto.getValue("FUNC_NO"));
       qMap.put("YWLSH", (String)dto.getValue("YWLSH"));
       qMap.put("EXEC_NUM", (String)dto.getValue("EXEC_NUM"));
       
       this.commDAO.update("HISAPP.FW_HIS_LOG_U", qMap);
     }
     return newExecNum;
   }
 }