 package cn.sinobest.framework.service.file;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IDAO;
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.util.ConfUtil;
 import java.io.IOException;
 import java.util.HashMap;
 import java.util.Map;
 import org.apache.commons.net.ftp.FTPClient;
 import org.apache.commons.net.ftp.FTPReply;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
 
 @Service
 public class YxzlDeleteService
 {
   @Autowired
   IDAO commDAO;
   
   public void deleteYXFile(IDTO dto)
     throws AppException
   {
     FTPClient ftpClient = new FTPClient();
     String servername = (String)dto.getValue("FTPIP");
     String username = ConfUtil.getParam("YXZL_FTPUSER");
     String passwd = ConfUtil.getParam("YXZL_FTPPWD");
     String path = (String)dto.getValue("FILEPATH");
     String port = ConfUtil.getParam("YXZL_FTPPORT", "21");
     String filename = (String)dto.getValue("FILENAME");
     String bae007 = (String)dto.getValue("BAE007");
     try
     {
       ftpClient.connect(servername, Integer.parseInt(port));
       ftpClient.setDataTimeout(18000);
       ftpClient.login(username, passwd);
       ftpClient.enterLocalPassiveMode();
       ftpClient.setFileType(2);
       int reply = ftpClient.getReplyCode();
       if (!FTPReply.isPositiveCompletion(reply))
       {
         ftpClient.disconnect();
         throw new AppException("连接ftp服务器失败");
       }
       boolean cd = ftpClient.changeWorkingDirectory(path);
       if (!cd) {
         throw new AppException("切换到上传文件目录失败或者目录不存在！");
       }
       String[] files = ftpClient.listNames();
       if (files != null) {
         for (int i = 0; i < files.length; i++) {
           if (new String(files[i].getBytes("ISO-8859-1"), "GBK").equals(filename)) {
             ftpClient.deleteFile(files[i]);
           }
         }
       }
       Map<String, Object> yxzl = new HashMap();
       yxzl.put("BAE007", bae007);
       yxzl.put("FILENAME", filename);
       this.commDAO.delete("FW_CONFIG.FW_YXZLINFO_CONF_D", yxzl);
     }
     catch (AppException e)
     {
       throw e;
     }
     catch (Exception e)
     {
       throw new AppException("删除ftp文件失败，请检查系统参数配置和ftp服务器状态!");
     }
     finally
     {
       if (ftpClient.isConnected()) {
         try
         {
           ftpClient.disconnect();
         }
         catch (IOException e)
         {
           e.printStackTrace();
         }
       }
     }
   }
 }