 package jnlp.sample.servlet;
 
 import java.io.BufferedReader;
 import java.io.ByteArrayInputStream;
 import java.io.IOException;
 import java.io.InputStreamReader;
 import java.io.StringWriter;
 import java.net.URL;
 import java.net.URLConnection;
 import java.util.Calendar;
 import java.util.Date;
 import java.util.HashMap;
 import java.util.TimeZone;
 import javax.servlet.ServletContext;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpUtils;
 import javax.xml.parsers.DocumentBuilder;
 import javax.xml.parsers.DocumentBuilderFactory;
 import javax.xml.transform.Transformer;
 import javax.xml.transform.TransformerFactory;
 import javax.xml.transform.dom.DOMSource;
 import javax.xml.transform.stream.StreamResult;
 import org.w3c.dom.Document;
 import org.w3c.dom.Element;
 import org.w3c.dom.NodeList;
 
 public class JnlpFileHandler
 {
   private static final String JNLP_MIME_TYPE = "application/x-java-jnlp-file";
   private static final String HEADER_LASTMOD = "Last-Modified";
   private ServletContext _servletContext;
   private Logger _log = null;
   private HashMap _jnlpFiles = null;
   
   public JnlpFileHandler(ServletContext servletContext, Logger log)
   {
     this._servletContext = servletContext;
     this._log = log;
     this._jnlpFiles = new HashMap();
   }
   
   private static class JnlpFileEntry
   {
     DownloadResponse _response;
     private long _lastModified;
     
     JnlpFileEntry(DownloadResponse response, long lastmodfied)
     {
       this._response = response;
       this._lastModified = lastmodfied;
     }
     
     public DownloadResponse getResponse()
     {
       return this._response;
     }
     
     long getLastModified()
     {
       return this._lastModified;
     }
   }
   
   public synchronized DownloadResponse getJnlpFile(JnlpResource jnlpres, DownloadRequest dreq)
     throws IOException
   {
     String path = jnlpres.getPath();
     URL resource = jnlpres.getResource();
     long lastModified = jnlpres.getLastModified();
     
 
     this._log.addDebug("lastModified: " + lastModified + " " + new Date(lastModified));
     if (lastModified == 0L) {
       this._log.addWarning("servlet.log.warning.nolastmodified", path);
     }
     String reqUrl = HttpUtils.getRequestURL(dreq.getHttpRequest()).toString();
     
 
     JnlpFileEntry jnlpFile = (JnlpFileEntry)this._jnlpFiles.get(reqUrl);
     if ((jnlpFile != null) && (jnlpFile.getLastModified() == lastModified)) {
       return jnlpFile.getResponse();
     }
     long timeStamp = lastModified;
     String mimeType = this._servletContext.getMimeType(path);
     if (mimeType == null) {
       mimeType = "application/x-java-jnlp-file";
     }
     StringBuffer jnlpFileTemplate = new StringBuffer();
     URLConnection conn = resource.openConnection();
     BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
     String line = br.readLine();
     if ((line != null) && (line.startsWith("TS:")))
     {
       timeStamp = parseTimeStamp(line.substring(3));
       this._log.addDebug("Timestamp: " + timeStamp + " " + new Date(timeStamp));
       if (timeStamp == 0L)
       {
         this._log.addWarning("servlet.log.warning.notimestamp", path);
         timeStamp = lastModified;
       }
       line = br.readLine();
     }
     while (line != null)
     {
       jnlpFileTemplate.append(line);
       line = br.readLine();
     }
     String jnlpFileContent = specializeJnlpTemplate(dreq.getHttpRequest(), path, jnlpFileTemplate.toString());
     
 
     byte[] byteContent = jnlpFileContent.getBytes("UTF-8");
     
 
     DownloadResponse resp = DownloadResponse.getFileDownloadResponse(byteContent, 
       mimeType, 
       timeStamp, 
       jnlpres.getReturnVersionId());
     jnlpFile = new JnlpFileEntry(resp, lastModified);
     this._jnlpFiles.put(reqUrl, jnlpFile);
     
     return resp;
   }
   
   public synchronized DownloadResponse getJnlpFileEx(JnlpResource jnlpres, DownloadRequest dreq)
     throws IOException
   {
     String path = jnlpres.getPath();
     URL resource = jnlpres.getResource();
     long lastModified = jnlpres.getLastModified();
     
 
     this._log.addDebug("lastModified: " + lastModified + " " + new Date(lastModified));
     if (lastModified == 0L) {
       this._log.addWarning("servlet.log.warning.nolastmodified", path);
     }
     String reqUrl = HttpUtils.getRequestURL(dreq.getHttpRequest()).toString();
     if (dreq.getQuery() != null) {
       reqUrl = reqUrl + dreq.getQuery();
     }
     JnlpFileEntry jnlpFile = (JnlpFileEntry)this._jnlpFiles.get(reqUrl);
     if ((jnlpFile != null) && (jnlpFile.getLastModified() == lastModified)) {
       return jnlpFile.getResponse();
     }
     long timeStamp = lastModified;
     String mimeType = this._servletContext.getMimeType(path);
     if (mimeType == null) {
       mimeType = "application/x-java-jnlp-file";
     }
     StringBuffer jnlpFileTemplate = new StringBuffer();
     URLConnection conn = resource.openConnection();
     BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
     String line = br.readLine();
     if ((line != null) && (line.startsWith("TS:")))
     {
       timeStamp = parseTimeStamp(line.substring(3));
       this._log.addDebug("Timestamp: " + timeStamp + " " + new Date(timeStamp));
       if (timeStamp == 0L)
       {
         this._log.addWarning("servlet.log.warning.notimestamp", path);
         timeStamp = lastModified;
       }
       line = br.readLine();
     }
     while (line != null)
     {
       jnlpFileTemplate.append(line);
       line = br.readLine();
     }
     String jnlpFileContent = specializeJnlpTemplate(dreq.getHttpRequest(), path, jnlpFileTemplate.toString());
     
 
 
 
     String query = dreq.getQuery();
     String testJRE = dreq.getTestJRE();
     this._log.addDebug("Double check query string: " + query);
     if (query != null)
     {
       byte[] cb = jnlpFileContent.getBytes("UTF-8");
       ByteArrayInputStream bis = new ByteArrayInputStream(cb);
       try
       {
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         DocumentBuilder builder = factory.newDocumentBuilder();
         Document document = builder.parse(bis);
         if ((document != null) && (document.getNodeType() == 9))
         {
           boolean modified = false;
           Element root = document.getDocumentElement();
           if ((root.hasAttribute("href")) && (query != null))
           {
             String href = root.getAttribute("href");
             root.setAttribute("href", href + "?" + query);
             modified = true;
           }
           if (testJRE != null)
           {
             NodeList j2seNL = root.getElementsByTagName("j2se");
             if (j2seNL != null)
             {
               Element j2se = (Element)j2seNL.item(0);
               String ver = j2se.getAttribute("version");
               if (ver.length() > 0)
               {
                 j2se.setAttribute("version", testJRE);
                 modified = true;
               }
             }
           }
           TransformerFactory tFactory = TransformerFactory.newInstance();
           Transformer transformer = tFactory.newTransformer();
           DOMSource source = new DOMSource(document);
           StringWriter sw = new StringWriter();
           StreamResult result = new StreamResult(sw);
           transformer.transform(source, result);
           jnlpFileContent = sw.toString();
           this._log.addDebug("Converted jnlpFileContent: " + jnlpFileContent);
           if (modified)
           {
             timeStamp = new Date().getTime();
             this._log.addDebug("Last modified on the fly:  " + timeStamp);
           }
         }
       }
       catch (Exception e)
       {
         this._log.addDebug(e.toString(), e);
       }
     }
     byte[] byteContent = jnlpFileContent.getBytes("UTF-8");
     
 
     DownloadResponse resp = DownloadResponse.getFileDownloadResponse(byteContent, 
       mimeType, 
       timeStamp, 
       jnlpres.getReturnVersionId());
     jnlpFile = new JnlpFileEntry(resp, lastModified);
     this._jnlpFiles.put(reqUrl, jnlpFile);
     
     return resp;
   }
   
   private String specializeJnlpTemplate(HttpServletRequest request, String respath, String jnlpTemplate)
   {
     String urlprefix = getUrlPrefix(request);
     int idx = respath.lastIndexOf('/');
     String name = respath.substring(idx + 1);
     String codebase = respath.substring(0, idx + 1);
     jnlpTemplate = substitute(jnlpTemplate, "$$name", name);
     
     jnlpTemplate = substitute(jnlpTemplate, "$$hostname", 
       request.getServerName());
     jnlpTemplate = substitute(jnlpTemplate, "$$codebase", urlprefix + request.getContextPath() + codebase);
     jnlpTemplate = substitute(jnlpTemplate, "$$context", urlprefix + request.getContextPath());
     
     jnlpTemplate = substitute(jnlpTemplate, "$$site", urlprefix);
     return jnlpTemplate;
   }
   
   private String getUrlPrefix(HttpServletRequest req)
   {
     StringBuffer url = new StringBuffer();
     String scheme = req.getScheme();
     int port = req.getServerPort();
     url.append(scheme);
     url.append("://");
     url.append(req.getServerName());
     if (((scheme.equals("http")) && (port != 80)) || (
       (scheme.equals("https")) && (port != 443)))
     {
       url.append(':');
       url.append(req.getServerPort());
     }
     return url.toString();
   }
   
   private String substitute(String target, String key, String value)
   {
     int start = 0;
     for (;;)
     {
       int idx = target.indexOf(key, start);
       if (idx == -1) {
         return target;
       }
       target = target.substring(0, idx) + value + target.substring(idx + key.length());
       start = idx + value.length();
     }
   }
   
   private long parseTimeStamp(String timestamp)
   {
     int YYYY = 0;
     int MM = 0;
     int DD = 0;
     int hh = 0;
     int mm = 0;
     int ss = 0;
     
     timestamp = timestamp.trim();
     try
     {
       if (matchPattern("####-##-## ##:##", timestamp))
       {
         YYYY = getIntValue(timestamp, 0, 4);
         MM = getIntValue(timestamp, 5, 7);
         DD = getIntValue(timestamp, 8, 10);
         hh = getIntValue(timestamp, 11, 13);
         mm = getIntValue(timestamp, 14, 16);
         timestamp = timestamp.substring(16);
         if (matchPattern(":##", timestamp))
         {
           ss = getIntValue(timestamp, 1, 3);
           timestamp = timestamp.substring(3);
         }
       }
       else if (matchPattern("############", timestamp))
       {
         YYYY = getIntValue(timestamp, 0, 4);
         MM = getIntValue(timestamp, 4, 6);
         DD = getIntValue(timestamp, 6, 8);
         hh = getIntValue(timestamp, 8, 10);
         mm = getIntValue(timestamp, 10, 12);
         timestamp = timestamp.substring(12);
         if (matchPattern("##", timestamp))
         {
           ss = getIntValue(timestamp, 0, 2);
           timestamp = timestamp.substring(2);
         }
       }
       else
       {
         return 0L;
       }
     }
     catch (NumberFormatException e)
     {
       return 0L;
     }
     String timezone = null;
     
     timestamp = timestamp.trim();
     if (timestamp.equalsIgnoreCase("Z")) {
       timezone = "GMT";
     } else if ((timestamp.startsWith("+")) || (timestamp.startsWith("-"))) {
       timezone = "GMT" + timestamp;
     }
     if (timezone == null)
     {
       Calendar cal = Calendar.getInstance();
       cal.set(YYYY, MM - 1, DD, hh, mm, ss);
       return cal.getTime().getTime();
     }
     Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(timezone));
     cal.set(YYYY, MM - 1, DD, hh, mm, ss);
     return cal.getTime().getTime();
   }
   
   private int getIntValue(String key, int start, int end)
   {
     return Integer.parseInt(key.substring(start, end));
   }
   
   private boolean matchPattern(String pattern, String key)
   {
     if (key.length() < pattern.length()) {
       return false;
     }
     for (int i = 0; i < pattern.length(); i++)
     {
       char format = pattern.charAt(i);
       char ch = key.charAt(i);
       if (((format != '#') || (!Character.isDigit(ch))) && (format != ch)) {
         return false;
       }
     }
     return true;
   }
 }