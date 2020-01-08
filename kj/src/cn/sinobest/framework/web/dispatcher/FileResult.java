 package cn.sinobest.framework.web.dispatcher;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.service.file.FileService;
 import cn.sinobest.framework.util.ConfUtil;
 import cn.sinobest.framework.util.Util;
 import cn.sinobest.framework.web.BaseActionSupport;
 import com.opensymphony.xwork2.ActionContext;
 import com.opensymphony.xwork2.ActionInvocation;
 import com.opensymphony.xwork2.util.ValueStack;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.InputStream;
 import java.io.OutputStream;
 import javax.servlet.http.HttpServletResponse;
 import org.apache.log4j.Logger;
 import org.apache.struts2.dispatcher.StrutsResultSupport;
 
 public class FileResult
   extends StrutsResultSupport
 {
   private static final long serialVersionUID = 1L;
   protected static final Logger LOG = Logger.getLogger(FileResult.class);
   private String inputName;
   private String contentType;
   protected String contentCharSet;
   protected int bufferSize = 1024;
   protected boolean allowCaching = true;
   protected boolean autoZip = true;
   protected boolean autoDelete = true;
   protected static long fileSizeToZip = ConfUtil.getSysParam2Number(
     "export.autoZipSize", 8388608L);
   
   public FileResult() {}
   
   public FileResult(String location)
   {
     super(location);
   }
   
   public FileResult(String location, boolean parse, boolean encode)
   {
     super(location, parse, encode);
   }
   
   protected final void doExecute(String finalLocation, ActionInvocation invocation)
     throws Exception
   {
     resolveParamsFromStack(invocation.getStack(), invocation);
     
     OutputStream oOutput = null;
     InputStream inputStream = null;
     File zipFile = null;
     File file = null;
     try
     {
       file = (File)invocation.getStack().findValue(
         conditionalParse(this.inputName, invocation));
       if (file == null) {
         throw new AppException("EFW0212", null, new Object[] { this.inputName });
       }
       String cusName = (String)((BaseActionSupport)invocation.getAction()).getValue("cusName");
       if (Util.isEmpty(cusName)) {
         cusName = file.getName();
       } else {
         cusName = cusName + "." + file.getName().substring(file.getName().lastIndexOf(".") + 1);
       }
       HttpServletResponse oResponse = (HttpServletResponse)invocation
         .getInvocationContext().get("com.opensymphony.xwork2.dispatcher.HttpServletResponse");
       
       String tmpContentType = null;
       if ((this.autoZip) && 
         (file.length() > fileSizeToZip) && 
         (file.getName().length() > 4) && 
         (!".zip".equals(file.getName().substring(
         file.getName().length() - 4))) && (file.getParentFile() != null))
       {
         zipFile = File.createTempFile("zipFile", ".zip");
         zipFile.deleteOnExit();
         FileService.zip(zipFile, new File[] { file });
         
         tmpContentType = ConfUtil.getSysParam("contentType.zip", 
           "content/unknown");
       }
       else if (this.contentType != null)
       {
         tmpContentType = conditionalParse(this.contentType, invocation);
         if (tmpContentType != null)
         {
           int i_start = file.getName().indexOf('.');
           String ext = file.getName().substring(i_start, 
             file.getName().length());
           tmpContentType = ConfUtil.getSysParam("contentType" + 
             ext, "content/unknown");
         }
       }
       if ((this.contentCharSet != null) && (!this.contentCharSet.equals(""))) {
         oResponse.setContentType(tmpContentType + ";charset=" + 
           this.contentCharSet);
       }
       if (zipFile != null)
       {
         if (zipFile.length() <= 2147483647L) {
           oResponse.setContentLength((int)zipFile.length());
         }
       }
       else if (file.length() <= 2147483647L) {
         oResponse.setContentLength((int)file.length());
       }
       if (zipFile != null)
       {
         if (cusName.lastIndexOf(".") > 0) {
           cusName = cusName.substring(0, 
             cusName.lastIndexOf('.') - 1);
         }
         oResponse.addHeader(
           "Content-Disposition", 
           "attachment;filename=\"" + 
           cusName + ".zip\"");
       }
       else
       {
         oResponse.addHeader("Content-Disposition", 
           "attachment;filename=\"" + cusName + '"');
       }
       if (!this.allowCaching)
       {
         oResponse.addHeader("Pragma", "no-cache");
         oResponse.addHeader("Cache-Control", "no-cache");
       }
       oOutput = oResponse.getOutputStream();
       if (LOG.isDebugEnabled()) {
         LOG.debug("Streaming result [" + this.inputName + "] type=[" + 
           this.contentType + "] charset=[" + this.contentCharSet + "]");
       }
       LOG.debug("Streaming to output buffer +++ START +++");
       byte[] oBuff = new byte[this.bufferSize];
       
       inputStream = new FileInputStream(zipFile != null ? zipFile : file);
       int iSize;
       while (-1 != (iSize = inputStream.read(oBuff)))
       {
         int iSize;
         oOutput.write(oBuff, 0, iSize);
       }
       LOG.debug("Streaming to output buffer +++ END +++");
       
 
       oOutput.flush();
     }
     finally
     {
       if (inputStream != null) {
         inputStream.close();
       }
       if (zipFile != null) {
         zipFile.delete();
       }
       if ((file != null) && (this.autoDelete)) {
         file.delete();
       }
       if (oOutput != null) {
         oOutput.close();
       }
     }
   }
   
   protected final void resolveParamsFromStack(ValueStack stack, ActionInvocation invocation)
   {
     String contentType = stack.findString("contentType");
     if (contentType != null) {
       setContentType(contentType);
     }
     String inputFile = stack.findString("inputName");
     if (inputFile != null) {
       setInputName(inputFile);
     }
     Integer bufferSize = (Integer)stack.findValue("bufferSize", 
       Integer.class);
     if (bufferSize != null) {
       setBufferSize(bufferSize.intValue());
     }
     if (this.contentCharSet != null) {
       this.contentCharSet = conditionalParse(this.contentCharSet, invocation);
     } else {
       this.contentCharSet = stack.findString("contentCharSet");
     }
   }
   
   public final void setContentType(String contentType)
   {
     this.contentType = contentType;
   }
   
   public final void setContentCharSet(String contentCharSet)
   {
     this.contentCharSet = contentCharSet;
   }
   
   public final void setBufferSize(int bufferSize)
   {
     this.bufferSize = bufferSize;
   }
   
   public final void setAllowCaching(boolean allowCaching)
   {
     this.allowCaching = allowCaching;
   }
   
   public final void setInputName(String inputName)
   {
     this.inputName = inputName;
   }
   
   public final void setAutoZip(boolean autoZip)
   {
     this.autoZip = autoZip;
   }
   
   public final void setFileSizeToZip(int fileSizeToZip)
   {
     fileSizeToZip = fileSizeToZip;
   }
   
   public String getInputName()
   {
     return this.inputName;
   }
   
   public String getContentType()
   {
     return this.contentType;
   }
   
   public String getContentCharSet()
   {
     return this.contentCharSet;
   }
   
   public int getBufferSize()
   {
     return this.bufferSize;
   }
   
   public boolean isAllowCaching()
   {
     return this.allowCaching;
   }
   
   public boolean isAutoZip()
   {
     return this.autoZip;
   }
   
   public boolean isAutoDelete()
   {
     return this.autoDelete;
   }
 }