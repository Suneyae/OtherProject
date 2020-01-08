 package jnlp.sample.servlet;
 
 import java.io.IOException;
 import java.net.URL;
 import java.net.URLConnection;
 import java.util.ResourceBundle;
 import javax.servlet.ServletConfig;
 import javax.servlet.ServletException;
 import javax.servlet.http.HttpServlet;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 
 public class JnlpDownloadServlet
   extends HttpServlet
 {
   private static final long serialVersionUID = 1L;
   private static ResourceBundle _resourceBundle = null;
   private static final String PARAM_JNLP_EXTENSION = "jnlp-extension";
   private static final String PARAM_JAR_EXTENSION = "jar-extension";
   private Logger _log = null;
   private JnlpFileHandler _jnlpFileHandler = null;
   private JarDiffHandler _jarDiffHandler = null;
   private ResourceCatalog _resourceCatalog = null;
   
   public void init(ServletConfig config)
     throws ServletException
   {
     super.init(config);
     
 
 
     this._log = new Logger(config, getResourceBundle());
     this._log.addDebug("Initializing");
     
 
     JnlpResource.setDefaultExtensions(
       config.getInitParameter("jnlp-extension"), 
       config.getInitParameter("jar-extension"));
     
     this._jnlpFileHandler = new JnlpFileHandler(config.getServletContext(), this._log);
     this._jarDiffHandler = new JarDiffHandler(config.getServletContext(), this._log);
     this._resourceCatalog = new ResourceCatalog(config.getServletContext(), this._log);
   }
   
   public static synchronized ResourceBundle getResourceBundle()
   {
     if (_resourceBundle == null) {
       _resourceBundle = ResourceBundle.getBundle("jnlp/sample/servlet/resources/strings");
     }
     return _resourceBundle;
   }
   
   public void doHead(HttpServletRequest request, HttpServletResponse response)
     throws ServletException, IOException
   {
     handleRequest(request, response, true);
   }
   
   public void doGet(HttpServletRequest request, HttpServletResponse response)
     throws ServletException, IOException
   {
     handleRequest(request, response, false);
   }
   
   private void handleRequest(HttpServletRequest request, HttpServletResponse response, boolean isHead)
     throws IOException
   {
     String requestStr = request.getRequestURI();
     if (request.getQueryString() != null) {
       requestStr = requestStr + "?" + request.getQueryString().trim();
     }
     DownloadRequest dreq = new DownloadRequest(getServletContext(), request);
     if (this._log.isInformationalLevel())
     {
       this._log.addInformational("servlet.log.info.request", requestStr);
       this._log.addInformational("servlet.log.info.useragent", request.getHeader("User-Agent"));
     }
     if (this._log.isDebugLevel()) {
       this._log.addDebug(dreq.toString());
     }
     long ifModifiedSince = request.getDateHeader("If-Modified-Since");
     try
     {
       validateRequest(dreq);
       
 
       JnlpResource jnlpres = locateResource(dreq);
       this._log.addDebug("JnlpResource: " + jnlpres);
       if (this._log.isInformationalLevel()) {
         this._log.addInformational("servlet.log.info.goodrequest", jnlpres.getPath());
       }
       DownloadResponse dres = null;
       if (isHead)
       {
         int cl = 
           jnlpres.getResource().openConnection().getContentLength();
         
 
         dres = DownloadResponse.getHeadRequestResponse(
           jnlpres.getMimeType(), jnlpres.getVersionId(), 
           jnlpres.getLastModified(), cl);
       }
       else
       {
         if (ifModifiedSince != -1L) {
           if (ifModifiedSince / 1000L >= jnlpres.getLastModified() / 1000L)
           {
             this._log.addDebug("return 304 Not modified");
             dres = DownloadResponse.getNotModifiedResponse();
             break label297;
           }
         }
         dres = constructResponse(jnlpres, dreq);
       }
       label297:
       dres.sendRespond(response);
     }
     catch (ErrorResponseException ere)
     {
       if (this._log.isInformationalLevel()) {
         this._log.addInformational("servlet.log.info.badrequest", requestStr);
       }
       if (this._log.isDebugLevel()) {
         this._log.addDebug("Response: " + ere.toString());
       }
       ere.getDownloadResponse().sendRespond(response);
     }
     catch (Throwable e)
     {
       this._log.addFatal("servlet.log.fatal.internalerror", e);
       response.sendError(500);
     }
   }
   
   private void validateRequest(DownloadRequest dreq)
     throws ErrorResponseException
   {
     String path = dreq.getPath();
     if ((path.endsWith("version.xml")) || 
       (path.indexOf("__") != -1)) {
       throw new ErrorResponseException(DownloadResponse.getNoContentResponse());
     }
   }
   
   private JnlpResource locateResource(DownloadRequest dreq)
     throws IOException, ErrorResponseException
   {
     if (dreq.getVersion() == null) {
       return handleBasicDownload(dreq);
     }
     return handleVersionRequest(dreq);
   }
   
   private JnlpResource handleBasicDownload(DownloadRequest dreq)
     throws ErrorResponseException, IOException
   {
     this._log.addDebug("Basic Protocol lookup");
     if ((dreq.getPath() == null) || (dreq.getPath().endsWith("/"))) {
       throw new ErrorResponseException(DownloadResponse.getNoContentResponse());
     }
     JnlpResource jnlpres = new JnlpResource(getServletContext(), dreq.getPath());
     if (!jnlpres.exists()) {
       throw new ErrorResponseException(DownloadResponse.getNoContentResponse());
     }
     return jnlpres;
   }
   
   private JnlpResource handleVersionRequest(DownloadRequest dreq)
     throws IOException, ErrorResponseException
   {
     this._log.addDebug("Version-based/Extension based lookup");
     return this._resourceCatalog.lookupResource(dreq);
   }
   
   private DownloadResponse constructResponse(JnlpResource jnlpres, DownloadRequest dreq)
     throws IOException
   {
     String path = jnlpres.getPath();
     if (jnlpres.isJnlpFile())
     {
       boolean supportQuery = JarDiffHandler.isJavawsVersion(dreq, "1.5+");
       this._log.addDebug("SupportQuery in Href: " + supportQuery);
       if (supportQuery) {
         return this._jnlpFileHandler.getJnlpFileEx(jnlpres, dreq);
       }
       return this._jnlpFileHandler.getJnlpFile(jnlpres, dreq);
     }
     if ((dreq.getCurrentVersionId() != null) && (jnlpres.isJarFile()))
     {
       DownloadResponse response = this._jarDiffHandler.getJarDiffEntry(this._resourceCatalog, dreq, jnlpres);
       if (response != null)
       {
         this._log.addInformational("servlet.log.info.jardiff.response");
         return response;
       }
     }
     JnlpResource jr = new JnlpResource(getServletContext(), 
       jnlpres.getName(), 
       jnlpres.getVersionId(), 
       jnlpres.getOSList(), 
       jnlpres.getArchList(), 
       jnlpres.getLocaleList(), 
       jnlpres.getPath(), 
       jnlpres.getReturnVersionId(), 
       dreq.getEncoding());
     
     this._log.addDebug("Real resource returned: " + jr);
     
 
     return DownloadResponse.getFileDownloadResponse(jr.getResource(), 
       jr.getMimeType(), 
       jr.getLastModified(), 
       jr.getReturnVersionId());
   }
 }