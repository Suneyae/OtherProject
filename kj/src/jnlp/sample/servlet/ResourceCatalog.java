 package jnlp.sample.servlet;
 
 import java.io.BufferedInputStream;
 import java.io.File;
 import java.net.URL;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import javax.servlet.ServletContext;
 import javax.xml.parsers.DocumentBuilder;
 import javax.xml.parsers.DocumentBuilderFactory;
 import jnlp.sample.util.VersionID;
 import jnlp.sample.util.VersionString;
 import org.w3c.dom.Document;
 import org.w3c.dom.Element;
 import org.xml.sax.SAXParseException;
 
 public class ResourceCatalog
 {
   public static final String VERSION_XML_FILENAME = "version.xml";
   private Logger _log = null;
   private ServletContext _servletContext = null;
   private HashMap _entries;
   
   private static class PathEntries
   {
     private List _versionXmlList;
     private List _directoryList;
     private List _platformList;
     private long _lastModified;
     
     public PathEntries(List versionXmlList, List directoryList, List platformList, long lastModified)
     {
       this._versionXmlList = versionXmlList;
       this._directoryList = directoryList;
       this._platformList = platformList;
       this._lastModified = lastModified;
     }
     
     public void setDirectoryList(List dirList)
     {
       this._directoryList = dirList;
     }
     
     public List getVersionXmlList()
     {
       return this._versionXmlList;
     }
     
     public List getDirectoryList()
     {
       return this._directoryList;
     }
     
     public List getPlatformList()
     {
       return this._platformList;
     }
     
     public long getLastModified()
     {
       return this._lastModified;
     }
   }
   
   public ResourceCatalog(ServletContext servletContext, Logger log)
   {
     this._entries = new HashMap();
     this._servletContext = servletContext;
     this._log = log;
   }
   
   public JnlpResource lookupResource(DownloadRequest dreq)
     throws ErrorResponseException
   {
     String path = dreq.getPath();
     String name = null;
     String dir = null;
     int idx = path.lastIndexOf('/');
     if (idx == -1)
     {
       name = path;
     }
     else
     {
       name = path.substring(idx + 1);
       dir = path.substring(0, idx + 1);
     }
     PathEntries pentries = (PathEntries)this._entries.get(dir);
     JnlpResource xmlVersionResPath = new JnlpResource(this._servletContext, dir + "version.xml");
     if ((pentries == null) || ((xmlVersionResPath.exists()) && (xmlVersionResPath.getLastModified() > pentries.getLastModified())))
     {
       this._log.addInformational("servlet.log.scandir", dir);
       List dirList = scanDirectory(dir, dreq);
       
       List versionList = new ArrayList();
       List platformList = new ArrayList();
       parseVersionXML(versionList, platformList, dir, xmlVersionResPath);
       pentries = new PathEntries(versionList, dirList, platformList, xmlVersionResPath.getLastModified());
       this._entries.put(dir, pentries);
     }
     JnlpResource[] result = new JnlpResource[1];
     if (dreq.isPlatformRequest())
     {
       int sts = findMatch(pentries.getPlatformList(), name, dreq, result);
       if (sts != 0) {
         throw new ErrorResponseException(DownloadResponse.getJnlpErrorResponse(sts));
       }
     }
     else
     {
       int sts1 = findMatch(pentries.getVersionXmlList(), name, dreq, result);
       if (sts1 != 0)
       {
         int sts2 = findMatch(pentries.getDirectoryList(), name, dreq, result);
         if (sts2 != 0)
         {
           pentries.setDirectoryList(scanDirectory(dir, dreq));
           sts2 = findMatch(pentries.getDirectoryList(), name, dreq, result);
           if (sts2 != 0) {
             throw new ErrorResponseException(DownloadResponse.getJnlpErrorResponse(Math.max(sts1, sts2)));
           }
         }
       }
     }
     return result[0];
   }
   
   public int findMatch(List list, String name, DownloadRequest dreq, JnlpResource[] result)
   {
     if (list == null) {
       return 10;
     }
     VersionID bestVersionId = null;
     int error = 10;
     VersionString vs = new VersionString(dreq.getVersion());
     for (int i = 0; i < list.size(); i++)
     {
       JnlpResource respath = (JnlpResource)list.get(i);
       VersionID vid = new VersionID(respath.getVersionId());
       int sts = matchEntry(name, vs, dreq, respath, vid);
       if (sts == 0)
       {
         if ((result[0] == null) || (vid.isGreaterThan(bestVersionId)))
         {
           result[0] = respath;
           bestVersionId = vid;
         }
       }
       else {
         error = Math.max(error, sts);
       }
     }
     return result[0] != null ? 0 : error;
   }
   
   public int matchEntry(String name, VersionString vs, DownloadRequest dreq, JnlpResource jnlpres, VersionID vid)
   {
     if (!name.equals(jnlpres.getName())) {
       return 10;
     }
     if (!vs.contains(vid)) {
       return 11;
     }
     if (!prefixMatchLists(jnlpres.getOSList(), dreq.getOS())) {
       return 20;
     }
     if (!prefixMatchLists(jnlpres.getArchList(), dreq.getArch())) {
       return 21;
     }
     if (!prefixMatchLists(jnlpres.getLocaleList(), dreq.getLocale())) {
       return 22;
     }
     return 0;
   }
   
   private static boolean prefixMatchStringList(String[] prefixList, String target)
   {
     if (prefixList == null) {
       return true;
     }
     if (target == null) {
       return false;
     }
     for (int i = 0; i < prefixList.length; i++) {
       if (target.startsWith(prefixList[i])) {
         return true;
       }
     }
     return false;
   }
   
   public boolean prefixMatchLists(String[] prefixes, String[] keys)
   {
     if (prefixes == null) {
       return true;
     }
     if (keys == null) {
       return false;
     }
     for (int i = 0; i < keys.length; i++) {
       if (prefixMatchStringList(prefixes, keys[i])) {
         return true;
       }
     }
     return false;
   }
   
   private String jnlpGetPath(DownloadRequest dreq)
   {
     String path = dreq.getPath();
     String filename = path.substring(path.lastIndexOf("/") + 1);
     path = path.substring(0, path.lastIndexOf("/") + 1);
     String name = filename;
     String ext = null;
     if (filename.lastIndexOf(".") != -1)
     {
       ext = filename.substring(filename.lastIndexOf(".") + 1);
       
       filename = filename.substring(0, filename.lastIndexOf("."));
     }
     if (dreq.getVersion() != null) {
       filename = filename + "__V" + dreq.getVersion();
     }
     String[] temp = dreq.getOS();
     if (temp != null) {
       for (int i = 0; i < temp.length; i++) {
         filename = filename + "__O" + temp[i];
       }
     }
     temp = dreq.getArch();
     if (temp != null) {
       for (int i = 0; i < temp.length; i++) {
         filename = filename + "__A" + temp[i];
       }
     }
     temp = dreq.getLocale();
     if (temp != null) {
       for (int i = 0; i < temp.length; i++) {
         filename = filename + "__L" + temp[i];
       }
     }
     if (ext != null) {
       filename = filename + "." + ext;
     }
     path = path + filename;
     
     return path;
   }
   
   public List scanDirectory(String dirPath, DownloadRequest dreq)
   {
     ArrayList list = new ArrayList();
     if (this._servletContext.getRealPath(dirPath) == null)
     {
       String path = jnlpGetPath(dreq);
       
       String name = dreq.getPath().substring(path.lastIndexOf("/") + 1);
       
       JnlpResource jnlpres = new JnlpResource(this._servletContext, name, dreq.getVersion(), dreq.getOS(), dreq.getArch(), dreq.getLocale(), path, dreq.getVersion());
       if (jnlpres.getResource() == null) {
         return null;
       }
       list.add(jnlpres);
       return list;
     }
     File dir = new File(this._servletContext.getRealPath(dirPath));
     this._log.addDebug("File directory: " + dir);
     if ((dir.exists()) && (dir.isDirectory()))
     {
       File[] entries = dir.listFiles();
       for (int i = 0; i < entries.length; i++)
       {
         JnlpResource jnlpres = parseFileEntry(dirPath, entries[i].getName());
         if (jnlpres != null)
         {
           if (this._log.isDebugLevel()) {
             this._log.addDebug("Read file resource: " + jnlpres);
           }
           list.add(jnlpres);
         }
       }
     }
     return list;
   }
   
   private JnlpResource parseFileEntry(String dir, String filename)
   {
     int idx = filename.indexOf("__");
     if (idx == -1) {
       return null;
     }
     String name = filename.substring(0, idx);
     String rest = filename.substring(idx);
     
 
     idx = rest.lastIndexOf('.');
     String extension = "";
     if (idx != -1)
     {
       extension = rest.substring(idx);
       rest = rest.substring(0, idx);
     }
     String versionId = null;
     ArrayList osList = new ArrayList();
     ArrayList archList = new ArrayList();
     ArrayList localeList = new ArrayList();
     while (rest.length() > 0)
     {
       if (!rest.startsWith("__")) {
         return null;
       }
       rest = rest.substring(2);
       
       char option = rest.charAt(0);
       idx = rest.indexOf("__");
       String arg = null;
       if (idx == -1)
       {
         arg = rest.substring(1);
         rest = "";
       }
       else
       {
         arg = rest.substring(1, idx);
         rest = rest.substring(idx);
       }
       switch (option)
       {
       case 'V': 
         versionId = arg; break;
       case 'O': 
         osList.add(arg); break;
       case 'A': 
         archList.add(arg); break;
       case 'L': 
         localeList.add(arg); break;
       default: 
         return null;
       }
     }
     return new JnlpResource(this._servletContext, 
       name + extension, 
       versionId, 
       listToStrings(osList), 
       listToStrings(archList), 
       listToStrings(localeList), 
       dir + filename, 
       versionId);
   }
   
   private String[] listToStrings(List list)
   {
     if (list.size() == 0) {
       return null;
     }
     return (String[])list.toArray(new String[list.size()]);
   }
   
   private void parseVersionXML(final List versionList, final List platformList, final String dir, final JnlpResource versionRes)
   {
     if (!versionRes.exists()) {
       return;
     }
     XMLNode root = null;
     try
     {
       DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
       DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
       Document doc = docBuilder.parse(new BufferedInputStream(versionRes.getResource().openStream()));
       doc.getDocumentElement().normalize();
       
 
 
 
 
       root = XMLParsing.convert(doc.getDocumentElement());
     }
     catch (SAXParseException err)
     {
       this._log.addWarning("servlet.log.warning.xml.parsing", 
         versionRes.getPath(), 
         Integer.toString(err.getLineNumber()), 
         err.getMessage());
       return;
     }
     catch (Throwable t)
     {
       this._log.addWarning("servlet.log.warning.xml.reading", versionRes.getPath(), t);
       return;
     }
     if (!root.getName().equals("jnlp-versions"))
     {
       this._log.addWarning("servlet.log.warning.xml.missing-jnlp", versionRes.getPath());
       return;
     }
     XMLParsing.visitElements(root, "<resource>", new XMLParsing.ElementVisitor()
     {
       public void visitElement(XMLNode node)
       {
         XMLNode pattern = XMLParsing.findElementPath(node, "<pattern>");
         if (pattern == null)
         {
           ResourceCatalog.this._log.addWarning("servlet.log.warning.xml.missing-pattern", versionRes.getPath());
         }
         else
         {
           String name = XMLParsing.getElementContent(pattern, "<name>", "");
           String versionId = XMLParsing.getElementContent(pattern, "<version-id>");
           String[] os = XMLParsing.getMultiElementContent(pattern, "<os>");
           String[] arch = XMLParsing.getMultiElementContent(pattern, "<arch>");
           String[] locale = XMLParsing.getMultiElementContent(pattern, "<locale>");
           
           String file = XMLParsing.getElementContent(node, "<file>");
           if ((versionId == null) || (file == null))
           {
             ResourceCatalog.this._log.addWarning("servlet.log.warning.xml.missing-elems", versionRes.getPath());
           }
           else
           {
             JnlpResource res = new JnlpResource(ResourceCatalog.this._servletContext, 
               name, 
               versionId, 
               os, 
               arch, 
               locale, 
               dir + file, 
               versionId);
             if (res.exists())
             {
               versionList.add(res);
               if (ResourceCatalog.this._log.isDebugLevel()) {
                 ResourceCatalog.this._log.addDebug("Read resource: " + res);
               }
             }
             else
             {
               ResourceCatalog.this._log.addWarning("servlet.log.warning.missing-file", file, versionRes.getPath());
             }
           }
         }
       }
     });
     XMLParsing.visitElements(root, "<platform>", new XMLParsing.ElementVisitor()
     {
       public void visitElement(XMLNode node)
       {
         XMLNode pattern = XMLParsing.findElementPath(node, "<pattern>");
         if (pattern == null)
         {
           ResourceCatalog.this._log.addWarning("servlet.log.warning.xml.missing-pattern", versionRes.getPath());
         }
         else
         {
           String name = XMLParsing.getElementContent(pattern, "<name>", "");
           String versionId = XMLParsing.getElementContent(pattern, "<version-id>");
           String[] os = XMLParsing.getMultiElementContent(pattern, "<os>");
           String[] arch = XMLParsing.getMultiElementContent(pattern, "<arch>");
           String[] locale = XMLParsing.getMultiElementContent(pattern, "<locale>");
           
           String file = XMLParsing.getElementContent(node, "<file>");
           String productId = XMLParsing.getElementContent(node, "<product-version-id>");
           if ((versionId == null) || (file == null) || (productId == null))
           {
             ResourceCatalog.this._log.addWarning("servlet.log.warning.xml.missing-elems2", versionRes.getPath());
           }
           else
           {
             JnlpResource res = new JnlpResource(ResourceCatalog.this._servletContext, 
               name, 
               versionId, 
               os, 
               arch, 
               locale, 
               dir + file, 
               productId);
             if (res.exists())
             {
               platformList.add(res);
               if (ResourceCatalog.this._log.isDebugLevel()) {
                 ResourceCatalog.this._log.addDebug("Read platform resource: " + res);
               }
             }
             else
             {
               ResourceCatalog.this._log.addWarning("servlet.log.warning.missing-file", file, versionRes.getPath());
             }
           }
         }
       }
     });
   }
 }