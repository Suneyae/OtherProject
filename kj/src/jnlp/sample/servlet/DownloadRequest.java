 package jnlp.sample.servlet;
 
 import java.io.File;
 import java.util.ArrayList;
 import java.util.Arrays;
 import javax.servlet.ServletContext;
 import javax.servlet.http.HttpServletRequest;
 
 public class DownloadRequest
 {
   private static final String ARG_ARCH = "arch";
   private static final String ARG_OS = "os";
   private static final String ARG_LOCALE = "locale";
   private static final String ARG_VERSION_ID = "version-id";
   private static final String ARG_CURRENT_VERSION_ID = "current-version-id";
   private static final String ARG_PLATFORM_VERSION_ID = "platform-version-id";
   private static final String ARG_KNOWN_PLATFORMS = "known-platforms";
   private static final String TEST_JRE = "TestJRE";
   private String _path = null;
   private String _version = null;
   private String _currentVersionId = null;
   private String[] _os = null;
   private String[] _arch = null;
   private String[] _locale = null;
   private String[] _knownPlatforms = null;
   private String _query = null;
   private String _testJRE = null;
   private boolean _isPlatformRequest = false;
   private ServletContext _context = null;
   private String _encoding = null;
   private HttpServletRequest _httpRequest = null;
   public static final String ACCEPT_ENCODING = "accept-encoding";
   
   public DownloadRequest(HttpServletRequest request)
   {
     this(null, request);
   }
   
   public DownloadRequest(ServletContext context, HttpServletRequest request)
   {
     this._context = context;
     this._httpRequest = request;
     this._path = request.getRequestURI();
     this._encoding = request.getHeader("accept-encoding");
     String context_path = request.getContextPath();
     if (context_path != null) {
       this._path = this._path.substring(context_path.length());
     }
     if (this._path == null) {
       this._path = request.getServletPath();
     }
     if (this._path == null) {
       this._path = "/";
     }
     this._path = this._path.trim();
     if ((this._context != null) && (!this._path.endsWith("/")))
     {
       String realPath = this._context.getRealPath(this._path);
       if (realPath != null)
       {
         File f = new File(realPath);
         if ((f != null) && (f.exists()) && (f.isDirectory())) {
           this._path += "/";
         }
       }
     }
     if (this._path.endsWith("/")) {
       this._path += "launch.jnlp";
     }
     this._version = getParameter(request, "version-id");
     this._currentVersionId = getParameter(request, "current-version-id");
     this._os = getParameterList(request, "os");
     this._arch = getParameterList(request, "arch");
     this._locale = getParameterList(request, "locale");
     this._knownPlatforms = getParameterList(request, "known-platforms");
     String platformVersion = getParameter(request, "platform-version-id");
     this._isPlatformRequest = (platformVersion != null);
     if (this._isPlatformRequest) {
       this._version = platformVersion;
     }
     this._query = request.getQueryString();
     this._testJRE = getParameter(request, "TestJRE");
   }
   
   private DownloadRequest(DownloadRequest dreq)
   {
     this._encoding = dreq._encoding;
     this._context = dreq._context;
     this._httpRequest = dreq._httpRequest;
     this._path = dreq._path;
     this._version = dreq._currentVersionId;
     this._currentVersionId = null;
     this._os = dreq._os;
     this._arch = dreq._arch;
     this._locale = dreq._locale;
     this._knownPlatforms = dreq._knownPlatforms;
     this._isPlatformRequest = dreq._isPlatformRequest;
     this._query = dreq._query;
     this._testJRE = dreq._testJRE;
   }
   
   private String getParameter(HttpServletRequest req, String key)
   {
     String res = req.getParameter(key);
     return res == null ? null : res.trim();
   }
   
   private static String[] getStringList(String str)
   {
     if (str == null) {
       return null;
     }
     ArrayList list = new ArrayList();
     int i = 0;
     int length = str.length();
     StringBuffer sb = null;
     while (i < length)
     {
       char ch = str.charAt(i);
       if (ch == ' ')
       {
         if (sb != null)
         {
           list.add(sb.toString());
           sb = null;
         }
       }
       else if (ch == '\\')
       {
         if (i + 1 < length)
         {
           ch = str.charAt(++i);
           if (sb == null) {
             sb = new StringBuffer();
           }
           sb.append(ch);
         }
       }
       else
       {
         if (sb == null) {
           sb = new StringBuffer();
         }
         sb.append(ch);
       }
       i++;
     }
     if (sb != null) {
       list.add(sb.toString());
     }
     if (list.size() == 0) {
       return null;
     }
     String[] results = new String[list.size()];
     return (String[])list.toArray(results);
   }
   
   private String[] getParameterList(HttpServletRequest req, String key)
   {
     String res = req.getParameter(key);
     return res == null ? null : getStringList(res.trim());
   }
   
   public String getPath()
   {
     return this._path;
   }
   
   public String getVersion()
   {
     return this._version;
   }
   
   public String getCurrentVersionId()
   {
     return this._currentVersionId;
   }
   
   public String getQuery()
   {
     return this._query;
   }
   
   public String getTestJRE()
   {
     return this._testJRE;
   }
   
   public String getEncoding()
   {
     return this._encoding;
   }
   
   public String[] getOS()
   {
     return this._os;
   }
   
   public String[] getArch()
   {
     return this._arch;
   }
   
   public String[] getLocale()
   {
     return this._locale;
   }
   
   public String[] getKnownPlatforms()
   {
     return this._knownPlatforms;
   }
   
   public boolean isPlatformRequest()
   {
     return this._isPlatformRequest;
   }
   
   public HttpServletRequest getHttpRequest()
   {
     return this._httpRequest;
   }
   
   DownloadRequest getFromDownloadRequest()
   {
     return new DownloadRequest(this);
   }
   
   public String toString()
   {
     return 
     
 
 
 
 
 
 
 
 
       "DownloadRequest[path=" + this._path + showEntry(" encoding=", this._encoding) + showEntry(" query=", this._query) + showEntry(" TestJRE=", this._testJRE) + showEntry(" version=", this._version) + showEntry(" currentVersionId=", this._currentVersionId) + showEntry(" os=", this._os) + showEntry(" arch=", this._arch) + showEntry(" locale=", this._locale) + showEntry(" knownPlatforms=", this._knownPlatforms) + " isPlatformRequest=" + this._isPlatformRequest + "]";
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