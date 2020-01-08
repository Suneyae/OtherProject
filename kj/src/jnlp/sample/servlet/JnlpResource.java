 package jnlp.sample.servlet;
 
 import java.io.File;
 import java.io.IOException;
 import java.net.URL;
 import java.net.URLConnection;
 import java.util.Arrays;
 import java.util.Date;
 import javax.servlet.ServletContext;
 
 public class JnlpResource
 {
   private static final String JNLP_MIME_TYPE = "application/x-java-jnlp-file";
   private static final String JAR_MIME_TYPE = "application/x-java-archive";
   private static final String JAR_MIME_TYPE_NEW = "application/java-archive";
   private static final String JNLP_EXTENSION = ".jnlp";
   private static final String JAR_EXTENSION = ".jar";
   private static String _jnlpExtension = ".jnlp";
   private static String _jarExtension = ".jar";
   private String _name;
   private String _versionId;
   private String[] _osList;
   private String[] _archList;
   private String[] _localeList;
   private String _path;
   private URL _resource;
   private long _lastModified;
   private String _mimeType;
   private String _returnVersionId;
   private String _encoding;
   
   public static void setDefaultExtensions(String jnlpExtension, String jarExtension)
   {
     if ((jnlpExtension != null) && (jnlpExtension.length() > 0))
     {
       if (!jnlpExtension.startsWith(".")) {
         jnlpExtension = "." + jnlpExtension;
       }
       _jnlpExtension = jnlpExtension;
     }
     if ((jarExtension != null) && (jarExtension.length() > 0))
     {
       if (!jarExtension.startsWith(".")) {
         jarExtension = "." + jarExtension;
       }
       _jarExtension = jarExtension;
     }
   }
   
   public JnlpResource(ServletContext context, String path)
   {
     this(context, null, null, null, null, null, path, null);
   }
   
   public JnlpResource(ServletContext context, String name, String versionId, String[] osList, String[] archList, String[] localeList, String path, String returnVersionId)
   {
     this(context, name, versionId, osList, archList, localeList, path, returnVersionId, null);
   }
   
   public JnlpResource(ServletContext context, String name, String versionId, String[] osList, String[] archList, String[] localeList, String path, String returnVersionId, String encoding)
   {
     this._encoding = encoding;
     this._name = name;
     this._versionId = versionId;
     this._osList = osList;
     this._archList = archList;
     this._localeList = localeList;
     
     this._returnVersionId = returnVersionId;
     try
     {
       String orig_path = path.trim();
       String search_path = orig_path;
       this._resource = context.getResource(orig_path);
       this._mimeType = getMimeType(context, orig_path);
       if (this._resource != null)
       {
         boolean found = false;
         if ((encoding != null) && (this._mimeType != null) && 
           ((this._mimeType.compareTo("application/x-java-archive") == 0) || (this._mimeType.compareTo("application/java-archive") == 0)) && 
           (encoding.toLowerCase().indexOf("pack200-gzip") > -1))
         {
           search_path = orig_path + ".pack.gz";
           this._resource = context.getResource(search_path);
           if (this._resource != null)
           {
             this._lastModified = getLastModified(context, this._resource, search_path);
             if (this._lastModified != 0L)
             {
               this._path = search_path;
               found = true;
             }
             else
             {
               this._resource = null;
             }
           }
         }
         if ((!found) && (encoding != null) && 
           (encoding.toLowerCase().indexOf("gzip") > -1))
         {
           search_path = orig_path + ".gz";
           this._resource = context.getResource(search_path);
           if (this._resource != null)
           {
             this._lastModified = getLastModified(context, this._resource, search_path);
             if (this._lastModified != 0L)
             {
               this._path = search_path;
               found = true;
             }
             else
             {
               this._resource = null;
             }
           }
         }
         if (!found)
         {
           search_path = orig_path;
           this._resource = context.getResource(search_path);
           if (this._resource != null)
           {
             this._lastModified = getLastModified(context, this._resource, search_path);
             if (this._lastModified != 0L)
             {
               this._path = search_path;
               found = true;
             }
             else
             {
               this._resource = null;
             }
           }
         }
       }
     }
     catch (IOException ioe)
     {
       this._resource = null;
     }
   }
   
   long getLastModified(ServletContext context, URL resource, String path)
   {
     long lastModified = 0L;
     try
     {
       URLConnection conn = resource.openConnection();
       lastModified = conn.getLastModified();
     }
     catch (Exception localException) {}
     if (lastModified == 0L)
     {
       String filepath = context.getRealPath(path);
       if (filepath != null)
       {
         File f = new File(filepath);
         if (f.exists()) {
           lastModified = f.lastModified();
         }
       }
     }
     return lastModified;
   }
   
   public String getPath()
   {
     return this._path;
   }
   
   public URL getResource()
   {
     return this._resource;
   }
   
   public String getMimeType()
   {
     return this._mimeType;
   }
   
   public long getLastModified()
   {
     return this._lastModified;
   }
   
   public boolean exists()
   {
     return this._resource != null;
   }
   
   public boolean isJnlpFile()
   {
     return this._path.endsWith(_jnlpExtension);
   }
   
   public boolean isJarFile()
   {
     return this._path.endsWith(_jarExtension);
   }
   
   public String getName()
   {
     return this._name;
   }
   
   public String getVersionId()
   {
     return this._versionId;
   }
   
   public String[] getOSList()
   {
     return this._osList;
   }
   
   public String[] getArchList()
   {
     return this._archList;
   }
   
   public String[] getLocaleList()
   {
     return this._localeList;
   }
   
   public String getReturnVersionId()
   {
     return this._returnVersionId;
   }
   
   private String getMimeType(ServletContext context, String path)
   {
     String mimeType = context.getMimeType(path);
     if (mimeType != null) {
       return mimeType;
     }
     if (path.endsWith(_jnlpExtension)) {
       return "application/x-java-jnlp-file";
     }
     if (path.endsWith(_jarExtension)) {
       return "application/x-java-archive";
     }
     return "application/unknown";
   }
   
   public String toString()
   {
     return 
     
 
 
 
 
 
       "JnlpResource[WAR Path: " + this._path + showEntry(" versionId=", this._versionId) + showEntry(" name=", this._name) + " lastModified=" + new Date(this._lastModified) + showEntry(" osList=", this._osList) + showEntry(" archList=", this._archList) + showEntry(" localeList=", this._localeList) + "]" + showEntry(" returnVersionId=", this._returnVersionId) + "]";
   }
   
   private String showEntry(String msg, String value)
   {
     if (value == null) {
       return "";
     }
     return msg + value;
   }
   
   private String showEntry(String msg, String[] value)
   {
     if (value == null) {
       return "";
     }
     return msg + Arrays.asList(value).toString();
   }
 }