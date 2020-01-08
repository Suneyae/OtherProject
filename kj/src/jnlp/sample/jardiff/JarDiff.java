 package jnlp.sample.jardiff;
 
 import java.io.File;
 import java.io.FileOutputStream;
 import java.io.IOException;
 import java.io.InputStream;
 import java.io.OutputStream;
 import java.io.PrintStream;
 import java.io.StringWriter;
 import java.io.Writer;
 import java.util.ArrayList;
 import java.util.Enumeration;
 import java.util.HashMap;
 import java.util.HashSet;
 import java.util.Iterator;
 import java.util.LinkedList;
 import java.util.List;
 import java.util.ListIterator;
 import java.util.Map;
 import java.util.MissingResourceException;
 import java.util.ResourceBundle;
 import java.util.Set;
 import java.util.jar.JarEntry;
 import java.util.jar.JarFile;
 import java.util.jar.JarOutputStream;
 
 public class JarDiff
   implements JarDiffConstants
 {
   private static final int DEFAULT_READ_SIZE = 2048;
   private static byte[] newBytes = new byte[2048];
   private static byte[] oldBytes = new byte[2048];
   private static ResourceBundle _resources = null;
   private static boolean _debug;
   
   public static ResourceBundle getResources()
   {
     if (_resources == null) {
       _resources = ResourceBundle.getBundle("jnlp/sample/jardiff/resources/strings");
     }
     return _resources;
   }
   
   public static void createPatch(String oldPath, String newPath, OutputStream os, boolean minimal)
     throws IOException
   {
     JarFile2 oldJar = new JarFile2(oldPath);
     JarFile2 newJar = new JarFile2(newPath);
     try
     {
       HashMap moved = new HashMap();
       HashSet visited = new HashSet();
       HashSet implicit = new HashSet();
       HashSet moveSrc = new HashSet();
       HashSet newEntries = new HashSet();
       
 
 
 
 
 
 
 
 
       Iterator entries = newJar.getJarEntries();
       if (entries != null) {
         while (entries.hasNext())
         {
           JarEntry newEntry = (JarEntry)entries.next();
           String newname = newEntry.getName();
           
 
           String oldname = oldJar.getBestMatch(newJar, newEntry);
           if (oldname == null)
           {
             if (_debug) {
               System.out.println("NEW: " + newname);
             }
             newEntries.add(newname);
           }
           else if ((oldname.equals(newname)) && (!moveSrc.contains(oldname)))
           {
             if (_debug) {
               System.out.println(newname + " added to implicit set!");
             }
             implicit.add(newname);
           }
           else
           {
             if ((!minimal) && ((implicit.contains(oldname)) || 
               (moveSrc.contains(oldname))))
             {
               if (_debug) {
                 System.out.println("NEW: " + newname);
               }
               newEntries.add(newname);
             }
             else
             {
               if (_debug) {
                 System.err.println("moved.put " + newname + " " + oldname);
               }
               moved.put(newname, oldname);
               moveSrc.add(oldname);
             }
             if ((implicit.contains(oldname)) && (minimal))
             {
               if (_debug)
               {
                 System.err.println("implicit.remove " + oldname);
                 
                 System.err.println("moved.put " + oldname + " " + oldname);
               }
               implicit.remove(oldname);
               moved.put(oldname, oldname);
               moveSrc.add(oldname);
             }
           }
         }
       }
       ArrayList deleted = new ArrayList();
       entries = oldJar.getJarEntries();
       if (entries != null) {
         while (entries.hasNext())
         {
           JarEntry oldEntry = (JarEntry)entries.next();
           String oldName = oldEntry.getName();
           if ((!implicit.contains(oldName)) && (!moveSrc.contains(oldName)) && 
             (!newEntries.contains(oldName)))
           {
             if (_debug) {
               System.err.println("deleted.add " + oldName);
             }
             deleted.add(oldName);
           }
         }
       }
       if (_debug)
       {
         entries = moved.keySet().iterator();
         if (entries != null)
         {
           System.out.println("MOVED MAP!!!");
           while (entries.hasNext())
           {
             String newName = (String)entries.next();
             String oldName = (String)moved.get(newName);
             System.out.println("key is " + newName + " value is " + oldName);
           }
         }
         entries = implicit.iterator();
         if (entries != null)
         {
           System.out.println("IMOVE MAP!!!");
           while (entries.hasNext())
           {
             String newName = (String)entries.next();
             System.out.println("key is " + newName);
           }
         }
       }
       JarOutputStream jos = new JarOutputStream(os);
       
 
       createIndex(jos, deleted, moved);
       
 
       entries = newEntries.iterator();
       if (entries != null) {
         while (entries.hasNext())
         {
           String newName = (String)entries.next();
           if (_debug) {
             System.out.println("New File: " + newName);
           }
           writeEntry(jos, newJar.getEntryByName(newName), newJar);
         }
       }
       jos.finish();
       jos.close();
     }
     catch (IOException ioE)
     {
       throw ioE;
     }
     finally
     {
       try
       {
         oldJar.getJarFile().close();
       }
       catch (IOException localIOException1) {}
       try
       {
         newJar.getJarFile().close();
       }
       catch (IOException localIOException2) {}
     }
   }
   
   private static void createIndex(JarOutputStream jos, List oldEntries, Map movedMap)
     throws IOException
   {
     StringWriter writer = new StringWriter();
     
     writer.write("version 1.0");
     writer.write("\r\n");
     for (int counter = 0; counter < oldEntries.size(); counter++)
     {
       String name = (String)oldEntries.get(counter);
       
       writer.write("remove");
       writer.write(" ");
       writeEscapedString(writer, name);
       writer.write("\r\n");
     }
     Iterator names = movedMap.keySet().iterator();
     if (names != null) {
       while (names.hasNext())
       {
         String newName = (String)names.next();
         String oldName = (String)movedMap.get(newName);
         
         writer.write("move");
         writer.write(" ");
         writeEscapedString(writer, oldName);
         writer.write(" ");
         writeEscapedString(writer, newName);
         writer.write("\r\n");
       }
     }
     JarEntry je = new JarEntry("META-INF/INDEX.JD");
     byte[] bytes = writer.toString().getBytes("UTF-8");
     
     writer.close();
     jos.putNextEntry(je);
     jos.write(bytes, 0, bytes.length);
   }
   
   private static void writeEscapedString(Writer writer, String string)
     throws IOException
   {
     int index = 0;
     int last = 0;
     char[] chars = (char[])null;
     while ((index = string.indexOf(' ', index)) != -1)
     {
       if (last != index)
       {
         if (chars == null) {
           chars = string.toCharArray();
         }
         writer.write(chars, last, index - last);
       }
       last = index;
       index++;
       writer.write(92);
     }
     if (last != 0) {
       writer.write(chars, last, chars.length - last);
     } else {
       writer.write(string);
     }
   }
   
   private static void writeEntry(JarOutputStream jos, JarEntry entry, JarFile2 file)
     throws IOException
   {
     writeEntry(jos, entry, file.getJarFile().getInputStream(entry));
   }
   
   private static void writeEntry(JarOutputStream jos, JarEntry entry, InputStream data)
     throws IOException
   {
     jos.putNextEntry(entry);
     try
     {
       int size = data.read(newBytes);
       while (size != -1)
       {
         jos.write(newBytes, 0, size);
         size = data.read(newBytes);
       }
     }
     catch (IOException ioE)
     {
       throw ioE;
     }
     finally
     {
       try
       {
         data.close();
       }
       catch (IOException localIOException1) {}
     }
   }
   
   private static class JarFile2
   {
     private JarFile _jar;
     private List _entries;
     private HashMap _nameToEntryMap;
     private HashMap _crcToEntryMap;
     
     public JarFile2(String path)
       throws IOException
     {
       this._jar = new JarFile(new File(path));
       index();
     }
     
     public JarFile getJarFile()
     {
       return this._jar;
     }
     
     public Iterator getJarEntries()
     {
       return this._entries.iterator();
     }
     
     public JarEntry getEntryByName(String name)
     {
       return (JarEntry)this._nameToEntryMap.get(name);
     }
     
     private static boolean differs(InputStream oldIS, InputStream newIS)
       throws IOException
     {
       int newSize = 0;
       
       int total = 0;
       boolean retVal = false;
       try
       {
         while (newSize != -1)
         {
           newSize = newIS.read(JarDiff.newBytes);
           int oldSize = oldIS.read(JarDiff.oldBytes);
           if (newSize != oldSize)
           {
             if (JarDiff._debug) {
               System.out.println("\tread sizes differ: " + newSize + 
                 " " + oldSize + " total " + total);
             }
             retVal = true;
             break;
           }
           if (newSize > 0) {
             do
             {
               total++;
               if (JarDiff.newBytes[newSize] != JarDiff.oldBytes[newSize])
               {
                 if (JarDiff._debug) {
                   System.out.println("\tbytes differ at " + 
                     total);
                 }
                 retVal = true;
                 break;
               }
               if (retVal) {
                 break;
               }
               newSize = 0;newSize--;
             } while (newSize >= 0);
           }
         }
       }
       catch (IOException ioE)
       {
         throw ioE;
       }
       finally
       {
         try
         {
           oldIS.close();
         }
         catch (IOException localIOException1) {}
         try
         {
           newIS.close();
         }
         catch (IOException localIOException2) {}
       }
       return retVal;
     }
     
     public String getBestMatch(JarFile2 file, JarEntry entry)
       throws IOException
     {
       if (contains(file, entry)) {
         return entry.getName();
       }
       return hasSameContent(file, entry);
     }
     
     public boolean contains(JarFile2 f, JarEntry e)
       throws IOException
     {
       JarEntry thisEntry = getEntryByName(e.getName());
       if (thisEntry == null) {
         return false;
       }
       if (thisEntry.getCrc() != e.getCrc()) {
         return false;
       }
       InputStream oldIS = getJarFile().getInputStream(thisEntry);
       InputStream newIS = f.getJarFile().getInputStream(e);
       boolean retValue = differs(oldIS, newIS);
       
       return !retValue;
     }
     
     public String hasSameContent(JarFile2 file, JarEntry entry)
       throws IOException
     {
       String thisName = null;
       
       Long crcL = new Long(entry.getCrc());
       if (this._crcToEntryMap.containsKey(crcL))
       {
         LinkedList ll = (LinkedList)this._crcToEntryMap.get(crcL);
         
         ListIterator li = ll.listIterator(0);
         if (li != null) {
           while (li.hasNext())
           {
             JarEntry thisEntry = (JarEntry)li.next();
             
 
             InputStream oldIS = getJarFile().getInputStream(thisEntry);
             InputStream newIS = file.getJarFile().getInputStream(entry);
             if (!differs(oldIS, newIS))
             {
               thisName = thisEntry.getName();
               return thisName;
             }
           }
         }
       }
       return thisName;
     }
     
     private void index()
       throws IOException
     {
       Enumeration entries = this._jar.entries();
       
       this._nameToEntryMap = new HashMap();
       this._crcToEntryMap = new HashMap();
       
       this._entries = new ArrayList();
       if (JarDiff._debug) {
         System.out.println("indexing: " + this._jar.getName());
       }
       if (entries != null) {
         while (entries.hasMoreElements())
         {
           JarEntry entry = (JarEntry)entries.nextElement();
           
           long crc = entry.getCrc();
           
           Long crcL = new Long(crc);
           if (JarDiff._debug) {
             System.out.println("\t" + entry.getName() + " CRC " + 
               crc);
           }
           this._nameToEntryMap.put(entry.getName(), entry);
           this._entries.add(entry);
           if (this._crcToEntryMap.containsKey(crcL))
           {
             LinkedList ll = (LinkedList)this._crcToEntryMap.get(crcL);
             
 
             ll.add(entry);
             
 
             this._crcToEntryMap.put(crcL, ll);
           }
           else
           {
             LinkedList ll = new LinkedList();
             ll.add(entry);
             
 
             this._crcToEntryMap.put(crcL, ll);
           }
         }
       }
     }
   }
   
   private static void showHelp()
   {
     System.out.println("JarDiff: [-nonminimal (for backward compatibility with 1.0.1/1.0] [-creatediff | -applydiff] [-output file] old.jar new.jar");
   }
   
   public static void main(String[] args)
     throws IOException
   {
     boolean diff = true;
     boolean minimal = true;
     String outputFile = "out.jardiff";
     for (int counter = 0; counter < args.length; counter++) {
       if ((args[counter].equals("-nonminimal")) || 
         (args[counter].equals("-n")))
       {
         minimal = false;
       }
       else if ((args[counter].equals("-creatediff")) || 
         (args[counter].equals("-c")))
       {
         diff = true;
       }
       else if ((args[counter].equals("-applydiff")) || 
         (args[counter].equals("-a")))
       {
         diff = false;
       }
       else if ((args[counter].equals("-debug")) || 
         (args[counter].equals("-d")))
       {
         _debug = true;
       }
       else if ((args[counter].equals("-output")) || 
         (args[counter].equals("-o")))
       {
         counter++;
         if (counter < args.length) {
           outputFile = args[counter];
         }
       }
       else if ((args[counter].equals("-applydiff")) || 
         (args[counter].equals("-a")))
       {
         diff = false;
       }
       else
       {
         if (counter + 2 != args.length)
         {
           showHelp();
           System.exit(0);
         }
         if (diff) {
           try
           {
             OutputStream os = new FileOutputStream(outputFile);
             
             createPatch(args[counter], 
               args[(counter + 1)], os, minimal);
             os.close();
           }
           catch (IOException ioe)
           {
             try
             {
               System.out.println(getResources().getString("jardiff.error.create") + " " + ioe);
             }
             catch (MissingResourceException localMissingResourceException) {}
           }
         } else {
           try
           {
             OutputStream os = new FileOutputStream(outputFile);
             
             new JarDiffPatcher().applyPatch(
               null, 
               args[counter], 
               args[(counter + 1)], 
               os);
             os.close();
           }
           catch (IOException ioe)
           {
             try
             {
               System.out.println(getResources().getString("jardiff.error.apply") + " " + ioe);
             }
             catch (MissingResourceException localMissingResourceException1) {}
           }
         }
         System.exit(0);
       }
     }
     showHelp();
   }
 }