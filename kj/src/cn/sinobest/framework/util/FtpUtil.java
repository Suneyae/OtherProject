 package cn.sinobest.framework.util;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import java.io.BufferedInputStream;
 import java.io.FileInputStream;
 import java.io.IOException;
 import org.apache.commons.net.ftp.FTPClient;
 import org.apache.commons.net.ftp.FTPReply;
 
 public class FtpUtil
 {
   public static void uploadYxzl(String webname, String localFilePath, String fileName)
   {
     FTPClient ftpClient = new FTPClient();
     String servername = ConfUtil.getParam("YXZL_FTPIP");
     String username = ConfUtil.getParam("YXZL_FTPUSER");
     String passwd = ConfUtil.getParam("YXZL_FTPPWD");
     String port = ConfUtil.getParam("YXZL_FTPPORT", "21");
     String ftpDir = ConfUtil.getParam("YXZL_FTPDIR");
     BufferedInputStream buff = null;
     try
     {
       ftpClient.connect(servername, Integer.parseInt(port));
       ftpClient.setDataTimeout(18000);
       ftpClient.login(username, passwd);
       int reply = ftpClient.getReplyCode();
       if (!FTPReply.isPositiveCompletion(reply))
       {
         ftpClient.disconnect();
         throw new AppException("连接ftp服务器失败");
       }
       createDirectory(ftpClient, ftpDir);
       
       ftpClient.changeWorkingDirectory("/" + ftpDir);
       if (createDirectory(ftpClient, webname))
       {
         boolean cd = ftpClient.changeWorkingDirectory("/" + ftpDir + "/" + 
           webname + "/");
         if (!cd) {
           throw new AppException("切换到上传文件目录失败或者目录不存在！");
         }
         buff = new BufferedInputStream(new FileInputStream(
           localFilePath));
         ftpClient.enterLocalPassiveMode();
         ftpClient.setBufferSize(4096);
         
         ftpClient.setFileType(2);
         String[] files = ftpClient.listNames();
         if (files != null) {
           for (int i = 0; i < files.length; i++) {
             if (new String(files[i].getBytes("ISO-8859-1"), "GBK").equals(fileName)) {
               ftpClient.deleteFile(files[i]);
             }
           }
         }
         ftpClient.storeFile(new String(fileName.getBytes("GBK"), "ISO-8859-1"), buff);
       }
     }
     catch (AppException e)
     {
       throw e;
     }
     catch (Exception e)
     {
       throw new AppException("上传ftp文件失败，请检查系统参数配置和ftp服务器状态!");
     }
     finally
     {
       try
       {
         if (buff != null) {
           buff.close();
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
   }
   
   public static boolean createDirectory(FTPClient ftpClient, String dirName)
   {
     try
     {
       boolean f = ftpClient.changeWorkingDirectory(dirName);
       if (!f)
       {
         boolean f2 = ftpClient.makeDirectory(dirName);
         if (!f2) {
           throw new AppException("创建ftp目录失败，请检查用户是否有创建ftp目录权限!");
         }
         return f2;
       }
     }
     catch (IOException e)
     {
       e.printStackTrace();
     }
     return true;
   }
 }