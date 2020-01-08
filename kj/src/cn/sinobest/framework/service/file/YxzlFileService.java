 package cn.sinobest.framework.service.file;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.comm.iface.IExportService;
 import cn.sinobest.framework.util.ConfUtil;
 import java.io.File;
 import java.io.FileOutputStream;
 import java.io.IOException;
 import java.io.OutputStream;
 import java.net.URLEncoder;
 import org.apache.commons.net.ftp.FTPClient;
 import org.apache.commons.net.ftp.FTPReply;
 import org.springframework.stereotype.Service;
 
 @Service
 public class YxzlFileService
   implements IExportService
 {
   public File exportFile(IDTO dto)
     throws AppException
   {
     FTPClient ftpClient = new FTPClient();
     String servername = (String)dto.getValue("FTPIP");
     String username = ConfUtil.getParam("YXZL_FTPUSER");
     String passwd = ConfUtil.getParam("YXZL_FTPPWD");
     String path = (String)dto.getValue("FILEPATH");
     String port = ConfUtil.getParam("YXZL_FTPPORT", "21");
     String filename = (String)dto.getValue("FILENAME");
     OutputStream is = null;
     File localFile = null;
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
       localFile = new File(URLEncoder.encode(filename, "UTF-8"));
       is = new FileOutputStream(localFile);
       ftpClient.retrieveFile(new String(filename.getBytes("GBK"), "ISO-8859-1"), is);
       ftpClient.logout();
     }
     catch (AppException e)
     {
       throw e;
     }
     catch (Exception e)
     {
       throw new AppException("下载ftp文件失败，请检查系统参数配置和ftp服务器状态!");
     }
     finally
     {
       try
       {
         if (is != null) {
           is.close();
         }
         if (ftpClient.isConnected()) {
           ftpClient.disconnect();
         }
       }
       catch (IOException e)
       {
         e.printStackTrace();
       }
     }
     return localFile;
   }
 }