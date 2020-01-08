 package jnlp.sample.servlet;
 
 import java.io.BufferedInputStream;
 import java.io.ByteArrayInputStream;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.IOException;
 import java.io.InputStream;
 import java.io.OutputStream;
 import java.io.PrintWriter;
 import java.net.URL;
 import java.net.URLConnection;
 import java.util.Date;
 import java.util.MissingResourceException;
 import java.util.ResourceBundle;
 import javax.servlet.http.HttpServletResponse;
 
 public abstract class DownloadResponse
 {
   private static final String HEADER_LASTMOD = "Last-Modified";
   private static final String HEADER_JNLP_VERSION = "x-java-jnlp-version-id";
   private static final String JNLP_ERROR_MIMETYPE = "application/x-java-jnlp-error";
   public static final int STS_00_OK = 0;
   public static final int ERR_10_NO_RESOURCE = 10;
   public static final int ERR_11_NO_VERSION = 11;
   public static final int ERR_20_UNSUP_OS = 20;
   public static final int ERR_21_UNSUP_ARCH = 21;
   public static final int ERR_22_UNSUP_LOCALE = 22;
   public static final int ERR_23_UNSUP_JRE = 23;
   public static final int ERR_99_UNKNOWN = 99;
   public static final String CONTENT_ENCODING = "content-encoding";
   public static final String GZIP_ENCODING = "gzip";
   public static final String PACK200_GZIP_ENCODING = "pack200-gzip";
   
   public String toString()
   {
     return getClass().getName();
   }
   
   abstract void sendRespond(HttpServletResponse paramHttpServletResponse)
     throws IOException;
   
   static DownloadResponse getNotFoundResponse()
   {
     return new NotFoundResponse(null);
   }
   
   static DownloadResponse getNoContentResponse()
   {
     return new NotFoundResponse(null);
   }
   
   static DownloadResponse getJnlpErrorResponse(int jnlpErrorCode)
   {
     return new JnlpErrorResponse(jnlpErrorCode);
   }
   
   static DownloadResponse getNotModifiedResponse()
   {
     return new NotModifiedResponse(null);
   }
   
   static DownloadResponse getHeadRequestResponse(String mimeType, String versionId, long lastModified, int contentLength)
   {
     return new HeadRequestResponse(mimeType, versionId, lastModified, 
       contentLength);
   }
   
   static DownloadResponse getFileDownloadResponse(byte[] content, String mimeType, long timestamp, String versionId)
   {
     return new ByteArrayFileDownloadResponse(content, mimeType, versionId, timestamp);
   }
   
   static DownloadResponse getFileDownloadResponse(URL resource, String mimeType, long timestamp, String versionId)
   {
     return new ResourceFileDownloadResponse(resource, mimeType, versionId, timestamp);
   }
   
   static DownloadResponse getFileDownloadResponse(File file, String mimeType, long timestamp, String versionId)
   {
     return new DiskFileDownloadResponse(file, mimeType, versionId, timestamp);
   }
   
   private static class NotModifiedResponse
     extends DownloadResponse
   {
     public void sendRespond(HttpServletResponse response)
       throws IOException
     {
       response.sendError(304);
     }
   }
   
   private static class NotFoundResponse
     extends DownloadResponse
   {
     public void sendRespond(HttpServletResponse response)
       throws IOException
     {
       response.sendError(404);
     }
   }
   
   private static class NoContentResponse
     extends DownloadResponse
   {
     public void sendRespond(HttpServletResponse response)
       throws IOException
     {
       response.sendError(204);
     }
   }
   
   private static class HeadRequestResponse
     extends DownloadResponse
   {
     private String _mimeType;
     private String _versionId;
     private long _lastModified;
     private int _contentLength;
     
     HeadRequestResponse(String mimeType, String versionId, long lastModified, int contentLength)
     {
       this._mimeType = mimeType;
       this._versionId = versionId;
       this._lastModified = lastModified;
       this._contentLength = contentLength;
     }
     
     public void sendRespond(HttpServletResponse response)
       throws IOException
     {
       response.setContentType(this._mimeType);
       response.setContentLength(this._contentLength);
       if (this._versionId != null) {
         response.setHeader("x-java-jnlp-version-id", this._versionId);
       }
       if (this._lastModified != 0L) {
         response.setDateHeader("Last-Modified", this._lastModified);
       }
       response.sendError(200);
     }
   }
   
   public static class JnlpErrorResponse
     extends DownloadResponse
   {
     private String _message;
     
     public JnlpErrorResponse(int jnlpErrorCode)
     {
       String msg = Integer.toString(jnlpErrorCode);
       String dsc = "No description";
       try
       {
         dsc = JnlpDownloadServlet.getResourceBundle().getString("servlet.jnlp.err." + msg);
       }
       catch (MissingResourceException localMissingResourceException) {}
       this._message = (msg + " " + dsc);
     }
     
     public void sendRespond(HttpServletResponse response)
       throws IOException
     {
       response.setContentType("application/x-java-jnlp-error");
       PrintWriter pw = response.getWriter();
       pw.println(this._message);
     }
     
     public String toString()
     {
       return super.toString() + "[" + this._message + "]";
     }
   }
   
   private static abstract class FileDownloadResponse
     extends DownloadResponse
   {
     private String _mimeType;
     private String _versionId;
     private long _lastModified;
     private String _fileName;
     
     FileDownloadResponse(String mimeType, String versionId, long lastModified)
     {
       this._mimeType = mimeType;
       this._versionId = versionId;
       this._lastModified = lastModified;
       this._fileName = null;
     }
     
     FileDownloadResponse(String mimeType, String versionId, long lastModified, String fileName)
     {
       this._mimeType = mimeType;
       this._versionId = versionId;
       this._lastModified = lastModified;
       this._fileName = fileName;
     }
     
     String getMimeType()
     {
       return this._mimeType;
     }
     
     String getVersionId()
     {
       return this._versionId;
     }
     
     long getLastModified()
     {
       return this._lastModified;
     }
     
     abstract int getContentLength()
       throws IOException;
     
     abstract InputStream getContent()
       throws IOException;
     
     public void sendRespond(HttpServletResponse response)
       throws IOException
     {
       response.setContentType(getMimeType());
       response.setContentLength(getContentLength());
       if (getVersionId() != null) {
         response.setHeader("x-java-jnlp-version-id", getVersionId());
       }
       if (getLastModified() != 0L) {
         response.setDateHeader("Last-Modified", getLastModified());
       }
       if (this._fileName != null) {
         if (this._fileName.endsWith(".pack.gz")) {
           response.setHeader("content-encoding", "pack200-gzip");
         } else if (this._fileName.endsWith(".gz")) {
           response.setHeader("content-encoding", "gzip");
         } else {
           response.setHeader("content-encoding", null);
         }
       }
       InputStream in = getContent();
       OutputStream out = response.getOutputStream();
       try
       {
         byte[] bytes = new byte[32768];
         int read;
         while ((read = in.read(bytes)) != -1)
         {
           int read;
           out.write(bytes, 0, read);
         }
       }
       finally
       {
         if (in != null) {
           in.close();
         }
       }
     }
     
     protected String getArgString()
     {
       long length = 0L;
       try
       {
         length = getContentLength();
       }
       catch (IOException localIOException) {}
       return 
       
 
         "Mimetype=" + getMimeType() + " VersionId=" + getVersionId() + " Timestamp=" + new Date(getLastModified()) + " Length=" + length;
     }
   }
   
   private static class ByteArrayFileDownloadResponse
     extends DownloadResponse.FileDownloadResponse
   {
     private byte[] _content;
     
     ByteArrayFileDownloadResponse(byte[] content, String mimeType, String versionId, long lastModified)
     {
       super(versionId, lastModified);
       this._content = content;
     }
     
     int getContentLength()
     {
       return this._content.length;
     }
     
     InputStream getContent()
     {
       return new ByteArrayInputStream(this._content);
     }
     
     public String toString()
     {
       return super.toString() + "[ " + getArgString() + "]";
     }
   }
   
   private static class ResourceFileDownloadResponse
     extends DownloadResponse.FileDownloadResponse
   {
     URL _url;
     
     ResourceFileDownloadResponse(URL url, String mimeType, String versionId, long lastModified)
     {
       super(versionId, lastModified, url.toString());
       this._url = url;
     }
     
     int getContentLength()
       throws IOException
     {
       return this._url.openConnection().getContentLength();
     }
     
     InputStream getContent()
       throws IOException
     {
       return this._url.openConnection().getInputStream();
     }
     
     public String toString()
     {
       return super.toString() + "[ " + getArgString() + "]";
     }
   }
   
   private static class DiskFileDownloadResponse
     extends DownloadResponse.FileDownloadResponse
   {
     private File _file;
     
     DiskFileDownloadResponse(File file, String mimeType, String versionId, long lastModified)
     {
       super(versionId, lastModified, file.getName());
       this._file = file;
     }
     
     int getContentLength()
       throws IOException
     {
       return (int)this._file.length();
     }
     
     InputStream getContent()
       throws IOException
     {
       return new BufferedInputStream(new FileInputStream(this._file));
     }
     
     public String toString()
     {
       return super.toString() + "[ " + getArgString() + "]";
     }
   }
 }