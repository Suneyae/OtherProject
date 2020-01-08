 package cn.sinobest.framework.service.file;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IDAO;
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.comm.iface.IImpExp;
 import cn.sinobest.framework.comm.iface.IOperator;
 import cn.sinobest.framework.service.json.JSONUtilities;
 import cn.sinobest.framework.util.ConfUtil;
 import cn.sinobest.framework.util.DateUtil.CurDate;
 import cn.sinobest.framework.util.FtpUtil;
 import cn.sinobest.framework.util.Util;
 import java.io.Closeable;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.FileOutputStream;
 import java.io.IOException;
 import java.net.URI;
 import java.nio.channels.Channels;
 import java.nio.channels.FileChannel;
 import java.nio.channels.ReadableByteChannel;
 import java.nio.channels.WritableByteChannel;
 import java.security.SecureRandom;
 import java.util.ArrayList;
 import java.util.Enumeration;
 import java.util.HashMap;
 import java.util.LinkedList;
 import java.util.List;
 import java.util.Map;
 import java.util.Queue;
 import java.util.zip.ZipEntry;
 import java.util.zip.ZipFile;
 import java.util.zip.ZipOutputStream;
 import javax.servlet.ServletContext;
 import org.apache.commons.io.FileUtils;
 import org.apache.log4j.Logger;
 import org.apache.struts2.ServletActionContext;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
 
 @Service
 public class FileService
 {
   @Autowired
   IDAO commDAO;
   private static Logger log = Logger.getLogger(FileService.class);
   private static final SecureRandom random = new SecureRandom();
   public static final String KEY_CONFIGID = "configId";
   public static final String KEY_DYNDICTSWHERECLS = "dynDictWhereCls";
   public static final String KEY_FILETYPE = "fileType";
   
   public static void zip(File zipFile, File... sourceFiles)
   {
     long startTime = System.currentTimeMillis();
     ZipOutputStream zos = null;
     WritableByteChannel destination = null;
     try
     {
       zos = new ZipOutputStream(new FileOutputStream(zipFile));
       destination = Channels.newChannel(zos);
       FileChannel source = null;
       Queue<File> queue = new LinkedList();
       for (File file : sourceFiles)
       {
         URI base = file.getParentFile().toURI();
         queue.add(file);
         while (!queue.isEmpty())
         {
           File queueFile = (File)queue.remove();
           String name = base.relativize(queueFile.toURI()).getPath();
           if (queueFile.isDirectory())
           {
             name = name + "/";
             zos.putNextEntry(new ZipEntry(name));
             for (File subFile : queueFile.listFiles()) {
               queue.add(subFile);
             }
           }
           else
           {
             ZipEntry entry = new ZipEntry(name);
             entry.setSize(file.length());
             zos.putNextEntry(entry);
             
             source = new FileInputStream(queueFile).getChannel();
             long count = 0L;
             long size;
             while (count += source.transferTo(count, size - count, destination) < size) {}
             zos.closeEntry();
             source.close();
             source = null;
           }
         }
       }
     }
     catch (Exception e)
     {
       throw new AppException("EFW0210", null, new Object[] { e });
     }
     finally
     {
       if (destination != null) {
         try
         {
           destination.close();
           destination = null;
         }
         catch (IOException e)
         {
           log.error(e);
         }
       }
     }
     if (log.isDebugEnabled()) {
       log.debug("压缩总用时" + (System.currentTimeMillis() - startTime));
     }
   }
   
   public static void unzip(File zipFile, IUnZipWork unzipWork)
   {
     long startTime = System.currentTimeMillis();
     
 
 
     File tempDir = new File(System.getProperty("java.io.tmpdir"), "unzip" + 
       random.nextLong());
     tempDir.deleteOnExit();
     if (log.isDebugEnabled()) {
       log.debug("解压缩的临时文件夹地址是：" + tempDir.getAbsolutePath());
     }
     try
     {
       tempDir.mkdir();
       
       FileChannel destination = null;
       ReadableByteChannel source = null;
       try
       {
         ZipFile zip = new ZipFile(zipFile);
         for (Enumeration<ZipEntry> e = zip
               .entries(); 
               e.hasMoreElements();)
         {
           ZipEntry entry = (ZipEntry)e.nextElement();
           if (entry.isDirectory())
           {
             new File(tempDir, entry.getName()).mkdirs();
           }
           else
           {
             File file = new File(tempDir, entry.getName());
             file.deleteOnExit();
             
 
             File parent = file.getParentFile();
             if ((parent != null) && (!parent.exists())) {
               parent.mkdirs();
             }
             source = Channels.newChannel(zip.getInputStream(entry));
             destination = new FileOutputStream(file).getChannel();
             long count = 0L;
             long size = file.length();
             while (count += destination.transferFrom(source, count, size - count) < size) {}
             destination.close();
             destination = null;
             source.close();
             source = null;
           }
         }
       }
       catch (Exception e)
       {
         throw new AppException("EFW0211", e, new Object[] { zipFile.getName() });
       }
       finally
       {
         if (destination != null)
         {
           try
           {
             destination.close();
           }
           catch (IOException e)
           {
             log.error(e);
           }
           destination = null;
         }
         if (source != null)
         {
           try
           {
             source.close();
           }
           catch (IOException e)
           {
             log.error(e);
           }
           source = null;
         }
       }
       unzipWork.doWork(tempDir.listFiles());
     }
     catch (AppException e)
     {
       throw e;
     }
     catch (Exception e)
     {
       String name = null;
       if (zipFile != null) {
         name = zipFile.getName();
       }
       throw new AppException("文件解压缩出错", e, new Object[] { name });
     }
     finally
     {
       recurseDeleteFiles(tempDir);
       if (log.isDebugEnabled()) {
         log.debug("解压缩总用时" + (System.currentTimeMillis() - startTime));
       }
     }
   }
   
   public static void recurseDeleteFiles(File file)
   {
     if (file == null) {
       return;
     }
     try
     {
       FileUtils.forceDelete(file);
     }
     catch (Exception e)
     {
       log.error("删除临时文件失败", e);
     }
   }
   
   public static void release(Closeable... closeables)
   {
     Closeable[] arrayOfCloseable = closeables;int j = closeables.length;
     for (int i = 0; i < j; i++)
     {
       Closeable closeable = arrayOfCloseable[i];
       if (closeable != null) {
         try
         {
           closeable.close();
         }
         catch (IOException e)
         {
           log.warn("关闭IO时出错", e);
         }
       }
     }
   }
   
   public ImpExpConfig getConfig(String id)
   {
     return ImpExpAbstract.getExpImpConfig(id);
   }
   
   public Object importFile(File[] files, IDTO dto)
     throws AppException
   {
     Map<String, Object> map = dto.getData();
     
     String configId = (String)map.get("configId");
     String dynDictWhereCls = (String)map.get("dynDictWhereCls");
     Integer fileType = (Integer)map.get("fileType");
     if ((Util.isEmpty(configId)) && (fileType.intValue() == 3))
     {
       String[] filenames = (String[])dto.getValue("FILENAMES");
       String path = ServletActionContext.getServletContext().getRealPath(
         "/uploadyxFile");
       for (int i = 0; i < files.length; i++)
       {
         String filename = filenames[i];
         File file = files[i];
         File savefile = new File(new File(path), 
           filename);
         if (!savefile.getParentFile().exists()) {
           savefile.getParentFile().mkdirs();
         }
         try
         {
           if (file.length() > 0L) {
             FileUtils.copyFile(file, savefile);
           } else {
             savefile.createNewFile();
           }
           FtpUtil.uploadYxzl((String)dto.getValue("ywlsh"), savefile.getAbsolutePath(), 
             filename);
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
           savefile.delete();
         }
       }
       return null;
     }
     ImpExpConfig config = getConfig(configId);
     
     IImpExp impexp = null;
     if ((fileType == null) || (fileType.intValue() == 0)) {
       impexp = new ExcelImpExp(config);
     } else if (fileType.intValue() == 1) {
       impexp = new TextImpExp(config);
     } else if (fileType.intValue() == 2) {
       impexp = new DbfImpExp(config);
     } else {
       throw new AppException("不可识别的文件类型，类型代码为{0}", null, new Object[] { fileType });
     }
     ImpExpAbstract.ImpExpParameter textParameter = new ImpExpAbstract.ImpExpParameter();
     
     textParameter.dyndictWhereCls = extractDictWhereCls(dynDictWhereCls);
     textParameter.addData = map;
     return impexp.importFile(files[0], textParameter);
   }
   
   public static Map<String, String> extractDictWhereCls(String dynDictWhereCls)
     throws AppException
   {
     Map<String, String> dynDictWhereClsValue = null;
     if ((dynDictWhereCls != null) && (dynDictWhereCls.trim().length() > 0))
     {
       Object obj = null;
       try
       {
         obj = JSONUtilities.parseJSON(dynDictWhereCls);
       }
       catch (AppException e)
       {
         throw e;
       }
       catch (Exception e)
       {
         throw new AppException("EFW0209", e, new Object[] { dynDictWhereCls });
       }
       if ((obj != null) && (!Map.class.isInstance(obj))) {
         throw new AppException("EFW0209", null, new Object[] { obj.toString() });
       }
       dynDictWhereClsValue = (Map)obj;
     }
     return dynDictWhereClsValue;
   }
   
   public void saveFileInfo(IDTO dto)
     throws AppException
   {
     String[] fileNames = (String[])dto.getValue("FILENAMES");
     List<Map<String, Object>> insertList = new ArrayList();
     for (String fileName : fileNames)
     {
       Map<String, Object> fileInfo = new HashMap();
       String ywlsh = (String)dto.getValue("ywlsh");
       fileInfo.put("BAE007", ywlsh);
       fileInfo.put("FILENAME", fileName);
       long i = this.commDAO.count("FW_CONFIG.FW_YXZLINFO_CONF_CNT", fileInfo).longValue();
       fileInfo.put("FILEPATH", "/" + ConfUtil.getParam("YXZL_FTPDIR") + "/" + ywlsh + "/");
       fileInfo.put("FILETYPE", fileName.substring(fileName.lastIndexOf(".")));
       fileInfo.put("FTOSERVER", ConfUtil.getParam("YXZL_FTPIP"));
       fileInfo.put("AAE011", dto.getUserInfo().getOperID());
       fileInfo.put("AAE036", Long.valueOf(Long.parseLong(DateUtil.CurDate.YYYYMMDDHHmmss.getDate())));
       fileInfo.put("FTOSERVER", ConfUtil.getParam("YXZL_FTPIP"));
       fileInfo.put("MEMO", dto.getValue("MEMO"));
       if (i == 0L) {
         insertList.add(fileInfo);
       } else {
         this.commDAO.update("FW_CONFIG.FW_YXZLINFO_CONF_US", fileInfo);
       }
     }
     this.commDAO.batchInsert("FW_CONFIG.FW_YXZLINFO_CONF_IS", insertList);
   }
   
   public static abstract interface IUnZipWork
   {
     public abstract void doWork(File[] paramArrayOfFile);
   }
 }