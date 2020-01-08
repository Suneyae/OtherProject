 package cn.sinobest.framework.util;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.dao.CommDAO;
 import com.runqian.report4.model.ReportDefine;
 import com.runqian.report4.usermodel.Context;
 import com.runqian.report4.usermodel.Engine;
 import com.runqian.report4.usermodel.IReport;
 import com.runqian.report4.usermodel.Param;
 import com.runqian.report4.usermodel.ParamMetaData;
 import com.runqian.report4.util.ReportUtils;
 import com.runqian.report4.view.excel.ExcelReport;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.IOException;
 import java.net.URL;
 import java.text.SimpleDateFormat;
 import java.util.ArrayList;
 import java.util.Date;
 import java.util.HashMap;
 import java.util.LinkedList;
 import java.util.List;
 import java.util.Map;
 import org.apache.commons.net.ftp.FTPClient;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 public class RepUtil
 {
   public static final String reportFilesPath;
   
   static
   {
     String path = RepUtil.class.getResource("/").getPath();
     String webPath = path.substring(1, path.indexOf("web") + "web".length());
     reportFilesPath = webPath + "/reportFiles";
   }
   
   private static final Logger LOGGER = LoggerFactory.getLogger(RepUtil.class);
   private static final CommDAO commDAO = (CommDAO)Util.getBean("commDAO");
   
   public static void fwFtpUploadExpExcel()
   {
     Map<String, Object> pMap = new HashMap();
     List<Map<String, Object>> uploadList = commDAO.select("FW_CONFIG.FW_REPORT_EXP2EXCEL_Q2", pMap);
     if (uploadList.size() == 0) {
       return;
     }
     FTPClient ftpClient = new FTPClient();
     
     boolean bFtpConnErr = false;
     String ftpConnErrMsg = null;
     try
     {
       try
       {
         ftpClient.connect(ConfUtil.getParam("EXP2XLS_FTPIP"), Integer.valueOf(ConfUtil.getParam("EXP2XLS_FTPPORT")).intValue());
         ftpClient.login(ConfUtil.getParam("EXP2XLS_FTPUSER"), ConfUtil.getParam("EXP2XLS_FTPPWD"));
       }
       catch (Exception e)
       {
         bFtpConnErr = true;
         ftpConnErrMsg = e.getClass().getName() + " " + e.getLocalizedMessage();
       }
       for (Map<String, Object> upload : uploadList)
       {
         String strDate = null;
         Long numDate = null;
         String expId = null;
         FileInputStream fis = null;
         try
         {
           strDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
           numDate = Long.valueOf(strDate);
           expId = (String)upload.get("EXPID");
           if (bFtpConnErr)
           {
             logExpFtpErr(expId, numDate, "连接FTP服务器失败。" + ftpConnErrMsg);
             if (LOGGER.isDebugEnabled()) {
               LOGGER.debug("连接FTP服务器失败。" + ftpConnErrMsg);
             }
             if (fis != null)
             {
               try
               {
                 fis.close();
               }
               catch (IOException e)
               {
                 if (!LOGGER.isDebugEnabled()) {
                   continue;
                 }
               }
               e.printStackTrace();
             }
           }
           else
           {
             String fileName = (String)upload.get("FILENAME");
             fileName = fileName.replace("\\", "/");
             if (!fileName.startsWith("/")) {
               fileName = "/" + fileName;
             }
             String xlsName = fileName.substring(fileName.lastIndexOf("/") + 1);
             String relativePath = fileName.substring(0, fileName.lastIndexOf("/") + 1);
             
 
 
             String localDir = ConfUtil.getParam("EXP2XLS_LOCALDIR", "").replace("\\", "/");
             if (localDir.endsWith("/")) {
               localDir.substring(0, localDir.length() - 1);
             }
             String ftpDir = ConfUtil.getParam("EXP2XLS_FTPDIR", "").replace("\\", "/");
             if (!ftpDir.startsWith("/")) {
               ftpDir = "/" + ftpDir;
             }
             if (ftpDir.endsWith("/")) {
               ftpDir = ftpDir.substring(0, ftpDir.length() - 1);
             }
             File localFile = new File(localDir + relativePath + xlsName);
             fis = new FileInputStream(localFile);
             ftpClient.makeDirectory(ftpDir + relativePath);
             ftpClient.changeWorkingDirectory(ftpDir + relativePath);
             ftpClient.setBufferSize(4096);
             ftpClient.setControlEncoding("GBK");
             ftpClient.setFileType(2);
             ftpClient.storeFile(xlsName, fis);
             fis.close();
             deleteLocalXlsFile(localDir + relativePath + xlsName, relativePath);
             
             Map<String, Object> updateMap = new HashMap();
             updateMap.put("EXPID", expId);
             updateMap.put("FTPSTATE", "1");
             updateMap.put("FTPMSG", "");
             updateMap.put("FTPTIME", numDate);
             commDAO.update("FW_CONFIG.FW_REPORT_EXP2EXCEL_U2", updateMap);
           }
         }
         catch (Exception e)
         {
           logExpFtpErr(expId, numDate, e.getClass().getName() + " " + e.getLocalizedMessage());
           if (LOGGER.isDebugEnabled()) {
             e.printStackTrace();
           }
           if (fis == null) {}
         }
         finally
         {
           if (fis != null) {
             try
             {
               fis.close();
             }
             catch (IOException e)
             {
               if (LOGGER.isDebugEnabled()) {
                 e.printStackTrace();
               }
             }
           }
         }
       }
     }
     catch (Exception e)
     {
       if (LOGGER.isDebugEnabled()) {
         e.printStackTrace();
       }
       try
       {
         ftpClient.disconnect();
       }
       catch (IOException e)
       {
         if (!LOGGER.isDebugEnabled()) {
           return;
         }
       }
       LOGGER.debug("关闭FTP连接异常");
     }
     finally
     {
       try
       {
         ftpClient.disconnect();
       }
       catch (IOException e)
       {
         if (LOGGER.isDebugEnabled())
         {
           LOGGER.debug("关闭FTP连接异常");
           e.printStackTrace();
         }
       }
     }
   }
   
   private static void logExpFtpErr(String expId, Long numDate, String msg)
   {
     Map<String, Object> errMap = new HashMap();
     errMap.put("EXPID", expId);
     errMap.put("FTPSTATE", "-1");
     errMap.put("FTPTIME", numDate);
     if ((msg != null) && (msg.length() > 200)) {
       msg = msg.substring(0, 200);
     }
     errMap.put("FTPMSG", msg);
     commDAO.update("FW_CONFIG.FW_REPORT_EXP2EXCEL_U2", errMap);
     if (LOGGER.isErrorEnabled()) {
       LOGGER.error(msg);
     }
   }
   
   private static void deleteLocalXlsFile(String localPath, String relativePath)
   {
     File localFile = new File(localPath);
     String[] parentDirs = relativePath.split("/");
     if (localFile.exists()) {
       localFile.delete();
     }
     File dir = localFile;
     for (String parentDir : parentDirs) {
       if (!parentDir.equals(""))
       {
         dir = dir.getParentFile();
         if ((dir.exists()) && (dir.list().length > 0)) {
           break;
         }
         if ((dir.exists()) && (dir.list().length == 0)) {
           dir.delete();
         }
       }
     }
   }
   
   public static void fwReportExp2excel()
   {
     Map<String, Object> pMap = new HashMap();
     List<Map<String, Object>> expList = commDAO.select("FW_CONFIG.FW_REPORT_EXP2EXCEL_Q", pMap);
     for (Map<String, Object> exp : expList)
     {
       String strDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
       Long numDate = Long.valueOf(strDate);
       String expId = null;
       try
       {
         expId = (String)Util.nvl(exp.get("EXPID"));
         String reportId = (String)Util.nvl(exp.get("REPORTID"));
         String pType = (String)Util.nvl(exp.get("PTYPE"));
         String params = (String)Util.nvl(exp.get("PARAMS"));
         String filename = (String)Util.nvl(exp.get("FILENAME"));
         String templateFile = (String)Util.nvl(exp.get("TEMPLATEFILE"));
         
 
 
         boolean bErr = false;
         Map<String, String> paramMap = new HashMap();
         if (!params.equals(""))
         {
           String[] arrParams = params.split("\\|");
           for (String param : arrParams)
           {
             String[] paramKeyValue = param.split("=");
             if (paramKeyValue.length != 2)
             {
               logExpErr(expId, numDate, "PARAMS配置不正确");
               bErr = true;
               break;
             }
             paramMap.put(paramKeyValue[0].trim(), paramKeyValue[1].trim());
           }
         }
         if (!bErr)
         {
           String autoFilename = null;
           if ("".equals(filename)) {
             autoFilename = reportId + "_" + expId + "_" + strDate + ".xls";
           }
           saveRaq2Xls(templateFile, paramMap, filename.equals("") ? autoFilename : filename);
           
 
 
           Map<String, Object> updateMap = new HashMap();
           updateMap.put("EXPID", expId);
           if ("".equals(filename)) {
             updateMap.put("FILENAME", autoFilename);
           }
           updateMap.put("STATE", "1");
           updateMap.put("MSG", "");
           updateMap.put("ETIME", numDate);
           commDAO.update("FW_CONFIG.FW_REPORT_EXP2EXCEL_U", updateMap);
         }
       }
       catch (Exception e)
       {
         logExpErr(expId, numDate, e.getClass().getName() + " " + e.getLocalizedMessage());
       }
     }
   }
   
   private static void logExpErr(String expId, Long numDate, String msg)
   {
     Map<String, Object> errMap = new HashMap();
     errMap.put("EXPID", expId);
     errMap.put("STATE", "-1");
     errMap.put("ETIME", numDate);
     errMap.put("MSG", msg);
     commDAO.update("FW_CONFIG.FW_REPORT_EXP2EXCEL_U", errMap);
     if (LOGGER.isErrorEnabled()) {
       LOGGER.error(msg);
     }
   }
   
   public static void saveRaq2Xls(String raqPath, Map<String, String> paramMap, String xlsName)
     throws AppException
   {
     String reportFile = null;
     
 
     raqPath = raqPath.replace("\\", "/");
     if (-1 != raqPath.indexOf("/"))
     {
       if (!raqPath.startsWith("/")) {
         raqPath = "/" + raqPath;
       }
       reportFile = reportFilesPath + raqPath;
     }
     else
     {
       List<String> fileList = findFile(new File(reportFilesPath), raqPath);
       if (fileList.size() == 0) {
         throw new AppException("没有找到报表文件: " + raqPath);
       }
       if (1 < fileList.size()) {
         throw new AppException("找到多个同名报表文件: " + fileList.toString());
       }
       reportFile = (String)fileList.get(0);
     }
     if (LOGGER.isDebugEnabled()) {
       LOGGER.debug("开始自动导出Excel,报表文件是: " + reportFile);
     }
     try
     {
       rd = (ReportDefine)ReportUtils.read(reportFile);
     }
     catch (Exception e)
     {
       ReportDefine rd;
       throw new AppException(e.getLocalizedMessage());
     }
     ReportDefine rd;
     ParamMetaData rd_pmd = rd.getParamMetaData();
     if (rd_pmd != null)
     {
       int rd_pmdNum = rd_pmd.getParamCount();
       String rd_paramName = null;
       for (int i = 0; i < rd_pmdNum; i++)
       {
         rd_paramName = rd_pmd.getParam(i).getParamName();
         rd_pmd.getParam(i).setValue((String)paramMap.get(rd_paramName));
       }
     }
     Context context = new Context();
     Engine engine = new Engine(rd, context);
     IReport iReport = engine.calc();
     
     ExcelReport er = new ExcelReport();
     
     er.export(iReport);
     
     String xlsDir = ConfUtil.getParam("EXP2XLS_LOCALDIR", "");
     if (xlsDir.equals("")) {
       throw new AppException("没有配置报表自动导出目录参数reportExp2xls.location");
     }
     xlsDir = xlsDir.replace("\\", "/");
     xlsName = xlsName.replace("\\", "/");
     if (xlsDir.endsWith("/")) {
       xlsDir = xlsDir.substring(0, xlsDir.length() - 1);
     }
     if (!xlsName.startsWith("/")) {
       xlsName = "/" + xlsName;
     }
     String xlsPath = xlsDir + xlsName;
     new File(xlsPath).getParentFile().mkdirs();
     
     er.saveTo(xlsPath);
     if (LOGGER.isDebugEnabled()) {
       LOGGER.debug("结束自动导出Excel,生成的excel是: " + xlsPath);
     }
   }
   
   private static List<String> findFile(File dir, String filename)
   {
     if (!dir.isDirectory()) {
       return null;
     }
     LinkedList<File> stack = new LinkedList();
     stack.addFirst(dir);
     List<String> list = new ArrayList(1);
     int j;
     int i;
     for (; !stack.isEmpty(); i < j)
     {
       File directory = (File)stack.removeFirst();
       File[] files = directory.listFiles();
       File[] arrayOfFile1;
       j = (arrayOfFile1 = files).length;i = 0; continue;File file = arrayOfFile1[i];
       if (file.isDirectory()) {
         stack.addFirst(file);
       } else if ((file.isFile()) && 
         (file.getName().equals(filename))) {
         list.add(file.getAbsolutePath());
       }
       i++;
     }
     return list;
   }
 }