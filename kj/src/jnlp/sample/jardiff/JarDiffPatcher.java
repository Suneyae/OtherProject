 package jnlp.sample.jardiff;
 
 import java.io.File;
 import java.io.IOException;
 import java.io.InputStream;
 import java.io.InputStreamReader;
 import java.io.LineNumberReader;
 import java.io.OutputStream;
 import java.io.PrintStream;
 import java.util.ArrayList;
 import java.util.Enumeration;
 import java.util.HashMap;
 import java.util.HashSet;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import java.util.MissingResourceException;
 import java.util.ResourceBundle;
 import java.util.Set;
 import java.util.jar.JarEntry;
 import java.util.jar.JarFile;
 import java.util.jar.JarOutputStream;
 import java.util.zip.ZipEntry;
 
 public class JarDiffPatcher
   implements JarDiffConstants, Patcher
 {
   private static final int DEFAULT_READ_SIZE = 2048;
   private static byte[] newBytes = new byte[2048];
   private static byte[] oldBytes = new byte[2048];
   private static ResourceBundle _resources = JarDiff.getResources();
   
   public static ResourceBundle getResources()
   {
     return JarDiff.getResources();
   }
   
   public void applyPatch(Patcher.PatchDelegate delegate, String oldJarPath, String jarDiffPath, OutputStream result)
     throws IOException
   {
     File oldFile = new File(oldJarPath);
     File diffFile = new File(jarDiffPath);
     JarOutputStream jos = new JarOutputStream(result);
     JarFile oldJar = new JarFile(oldFile);
     JarFile jarDiff = new JarFile(diffFile);
     Set ignoreSet = new HashSet();
     Map renameMap = new HashMap();
     
 
     determineNameMapping(jarDiff, ignoreSet, renameMap);
     
 
     Object[] keys = renameMap.keySet().toArray();
     
 
 
     Set oldjarNames = new HashSet();
     
     Enumeration oldEntries = oldJar.entries();
     if (oldEntries != null) {
       while (oldEntries.hasMoreElements()) {
         oldjarNames.add(((JarEntry)oldEntries.nextElement()).getName());
       }
     }
     double size = oldjarNames.size() + keys.length + jarDiff.size();
     double currentEntry = 0.0D;
     
 
     oldjarNames.removeAll(ignoreSet);
     size -= ignoreSet.size();
     
 
 
     Enumeration entries = jarDiff.entries();
     if (entries != null) {
       while (entries.hasMoreElements())
       {
         JarEntry entry = (JarEntry)entries.nextElement();
         if (!"META-INF/INDEX.JD".equals(entry.getName()))
         {
           updateDelegate(delegate, currentEntry, size);
           currentEntry += 1.0D;
           
           writeEntry(jos, entry, jarDiff);
           
 
 
           boolean wasInOld = oldjarNames.remove(entry.getName());
           if (wasInOld) {
             size -= 1.0D;
           }
         }
         else
         {
           size -= 1.0D;
         }
       }
     }
     for (int j = 0; j < keys.length; j++)
     {
       String newName = (String)keys[j];
       String oldName = (String)renameMap.get(newName);
       
 
       JarEntry oldEntry = oldJar.getJarEntry(oldName);
       if (oldEntry == null)
       {
         String moveCmd = "move" + oldName + " " + newName;
         handleException("jardiff.error.badmove", moveCmd);
       }
       JarEntry newEntry = new JarEntry(newName);
       newEntry.setTime(oldEntry.getTime());
       newEntry.setSize(oldEntry.getSize());
       newEntry.setCompressedSize(oldEntry.getCompressedSize());
       newEntry.setCrc(oldEntry.getCrc());
       newEntry.setMethod(oldEntry.getMethod());
       newEntry.setExtra(oldEntry.getExtra());
       newEntry.setComment(oldEntry.getComment());
       
 
       updateDelegate(delegate, currentEntry, size);
       currentEntry += 1.0D;
       
       writeEntry(jos, newEntry, oldJar.getInputStream(oldEntry));
       
 
 
       boolean wasInOld = oldjarNames.remove(oldName);
       if (wasInOld) {
         size -= 1.0D;
       }
     }
     Iterator iEntries = oldjarNames.iterator();
     if (iEntries != null) {
       while (iEntries.hasNext())
       {
         String name = (String)iEntries.next();
         JarEntry entry = oldJar.getJarEntry(name);
         
         updateDelegate(delegate, currentEntry, size);
         currentEntry += 1.0D;
         
         writeEntry(jos, entry, oldJar);
       }
     }
     updateDelegate(delegate, currentEntry, size);
     
     jos.finish();
   }
   
   private void updateDelegate(Patcher.PatchDelegate delegate, double currentSize, double size)
   {
     if (delegate != null) {
       delegate.patching((int)(currentSize / size));
     }
   }
   
   private void determineNameMapping(JarFile jarDiff, Set ignoreSet, Map renameMap)
     throws IOException
   {
     InputStream is = jarDiff.getInputStream(jarDiff.getEntry("META-INF/INDEX.JD"));
     if (is == null) {
       handleException("jardiff.error.noindex", null);
     }
     LineNumberReader indexReader = new LineNumberReader(
       new InputStreamReader(is, "UTF-8"));
     String line = indexReader.readLine();
     if ((line == null) || (!line.equals("version 1.0"))) {
       handleException("jardiff.error.badheader", line);
     }
     while ((line = indexReader.readLine()) != null) {
       if (line.startsWith("remove"))
       {
         List sub = getSubpaths(line.substring("remove"
           .length()));
         if (sub.size() != 1) {
           handleException("jardiff.error.badremove", line);
         }
         ignoreSet.add(sub.get(0));
       }
       else if (line.startsWith("move"))
       {
         List sub = getSubpaths(line.substring("move".length()));
         if (sub.size() != 2) {
           handleException("jardiff.error.badmove", line);
         }
         if (renameMap.put(sub.get(1), sub.get(0)) != null) {
           handleException("jardiff.error.badmove", line);
         }
       }
       else if (line.length() > 0)
       {
         handleException("jardiff.error.badcommand", line);
       }
     }
   }
   
   private void handleException(String errorMsg, String line)
     throws IOException
   {
     try
     {
       throw new IOException(getResources().getString(errorMsg) + " " + line);
     }
     catch (MissingResourceException mre)
     {
       System.err.println("Fatal error: " + errorMsg);
       new Throwable().printStackTrace(System.err);
       System.exit(-1);
     }
   }
   
   private List getSubpaths(String path)
   {
     int index = 0;
     int length = path.length();
     ArrayList sub = new ArrayList();
     while (index < length)
     {
       while ((index < length) && (Character.isWhitespace(
         path.charAt(index)))) {
         index++;
       }
       if (index < length)
       {
         int start = index;
         int last = start;
         String subString = null;
         while (index < length)
         {
           char aChar = path.charAt(index);
           if ((aChar == '\\') && (index + 1 < length) && 
             (path.charAt(index + 1) == ' '))
           {
             if (subString == null) {
               subString = path.substring(last, index);
             } else {
               subString = subString + path.substring(last, index);
             }
             index++;last = index;
           }
           else
           {
             if (Character.isWhitespace(aChar)) {
               break;
             }
           }
           index++;
         }
         if (last != index) {
           if (subString == null) {
             subString = path.substring(last, index);
           } else {
             subString = subString + path.substring(last, index);
           }
         }
         sub.add(subString);
       }
     }
     return sub;
   }
   
   private void writeEntry(JarOutputStream jos, JarEntry entry, JarFile file)
     throws IOException
   {
     writeEntry(jos, entry, file.getInputStream(entry));
   }
   
   private void writeEntry(JarOutputStream jos, JarEntry entry, InputStream data)
     throws IOException
   {
     jos.putNextEntry(new ZipEntry(entry.getName()));
     
 
     int size = data.read(newBytes);
     while (size != -1)
     {
       jos.write(newBytes, 0, size);
       size = data.read(newBytes);
     }
     data.close();
   }
 }