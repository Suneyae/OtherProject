 package cn.sinobest.framework.service.file.csv;
 
 import java.io.FileOutputStream;
 import java.io.IOException;
 import java.io.OutputStream;
 import java.io.OutputStreamWriter;
 import java.io.PrintWriter;
 import java.nio.charset.Charset;
 
 public class CsvWriter
 {
   private PrintWriter outputStream = null;
   private String fileName = null;
   private boolean firstColumn = true;
   private boolean useCustomRecordDelimiter = false;
   private Charset charset = null;
   private UserSettings userSettings = new UserSettings();
   private boolean initialized = false;
   private boolean closed = false;
   public static final int ESCAPE_MODE_DOUBLED = 1;
   public static final int ESCAPE_MODE_BACKSLASH = 2;
   
   public CsvWriter(String fileName, char delimiter, Charset charset)
   {
     if (fileName == null) {
       throw new IllegalArgumentException("Parameter fileName can not be null.");
     }
     if (charset == null) {
       throw new IllegalArgumentException(
         "Parameter charset can not be null.");
     }
     this.fileName = fileName;
     this.userSettings.Delimiter = delimiter;
     this.charset = charset;
   }
   
   public CsvWriter(String fileName)
   {
     this(fileName, ',', Charset.defaultCharset());
   }
   
   public CsvWriter(OutputStream outputStream)
     throws IOException
   {
     this(outputStream, ',', Charset.forName("GBK"));
   }
   
   public CsvWriter(OutputStream outputStream, char delimiter, Charset charset)
     throws IOException
   {
     this.charset = charset;
     writeBom(outputStream);
     this.outputStream = new PrintWriter(new OutputStreamWriter(
       outputStream, charset));
     this.userSettings.Delimiter = delimiter;
     this.initialized = true;
   }
   
   private void writeBom(OutputStream outputStream)
     throws IOException
   {
     if ("UTF-8".equalsIgnoreCase(this.charset.name()))
     {
       byte[] bom = { -17, -69, -65 };
       outputStream.write(bom);
     }
   }
   
   public char getDelimiter()
   {
     return this.userSettings.Delimiter;
   }
   
   public void setDelimiter(char delimiter)
   {
     this.userSettings.Delimiter = delimiter;
   }
   
   public char getRecordDelimiter()
   {
     return this.userSettings.RecordDelimiter;
   }
   
   public void setRecordDelimiter(char recordDelimiter)
   {
     this.useCustomRecordDelimiter = true;
     this.userSettings.RecordDelimiter = recordDelimiter;
   }
   
   public char getTextQualifier()
   {
     return this.userSettings.TextQualifier;
   }
   
   public void setTextQualifier(char textQualifier)
   {
     this.userSettings.TextQualifier = textQualifier;
   }
   
   public boolean getUseTextQualifier()
   {
     return this.userSettings.UseTextQualifier;
   }
   
   public void setUseTextQualifier(boolean useTextQualifier)
   {
     this.userSettings.UseTextQualifier = useTextQualifier;
   }
   
   public int getEscapeMode()
   {
     return this.userSettings.EscapeMode;
   }
   
   public void setEscapeMode(int escapeMode)
   {
     this.userSettings.EscapeMode = escapeMode;
   }
   
   public void setComment(char comment)
   {
     this.userSettings.Comment = comment;
   }
   
   public char getComment()
   {
     return this.userSettings.Comment;
   }
   
   public boolean getForceQualifier()
   {
     return this.userSettings.ForceQualifier;
   }
   
   public void setForceQualifier(boolean forceQualifier)
   {
     this.userSettings.ForceQualifier = forceQualifier;
   }
   
   public void write(String content, boolean preserveSpaces)
     throws IOException
   {
     checkClosed();
     
     checkInit();
     if (content == null) {
       content = "";
     }
     if (!this.firstColumn) {
       this.outputStream.write(this.userSettings.Delimiter);
     }
     boolean textQualify = this.userSettings.ForceQualifier;
     if ((!preserveSpaces) && (content.length() > 0)) {
       content = content.trim();
     }
     if ((!textQualify) && 
       (this.userSettings.UseTextQualifier) && (
       (content.indexOf(this.userSettings.TextQualifier) > -1) || 
       (content.indexOf(this.userSettings.Delimiter) > -1) || 
       ((!this.useCustomRecordDelimiter) && (
       (content.indexOf('\n') > -1) || 
       (content.indexOf('\r') > -1))) || 
       ((this.useCustomRecordDelimiter) && 
       (content.indexOf(this.userSettings.RecordDelimiter) > -1)) || 
       ((this.firstColumn) && (content.length() > 0) && 
       (content.charAt(0) == this.userSettings.Comment)) || (
       
 
       (this.firstColumn) && (content.length() == 0)))) {
       textQualify = true;
     }
     if ((this.userSettings.UseTextQualifier) && (!textQualify) && 
       (content.length() > 0) && (preserveSpaces))
     {
       char firstLetter = content.charAt(0);
       if ((firstLetter == ' ') || (firstLetter == '\t')) {
         textQualify = true;
       }
       if ((!textQualify) && (content.length() > 1))
       {
         char lastLetter = content.charAt(content.length() - 1);
         if ((lastLetter == ' ') || (lastLetter == '\t')) {
           textQualify = true;
         }
       }
     }
     if (textQualify)
     {
       this.outputStream.write(this.userSettings.TextQualifier);
       if (this.userSettings.EscapeMode == 2)
       {
         content = replace(content, "\\", "\\\\");
         
         content = replace(content, this.userSettings.TextQualifier, "\\" + 
           this.userSettings.TextQualifier);
       }
       else
       {
         content = replace(content, this.userSettings.TextQualifier, 
           this.userSettings.TextQualifier + 
           this.userSettings.TextQualifier);
       }
     }
     else if (this.userSettings.EscapeMode == 2)
     {
       content = replace(content, "\\", "\\\\");
       
       content = replace(content, this.userSettings.Delimiter, "\\" + 
         this.userSettings.Delimiter);
       if (this.useCustomRecordDelimiter)
       {
         content = replace(content, this.userSettings.RecordDelimiter, 
           "\\" + this.userSettings.RecordDelimiter);
       }
       else
       {
         content = replace(content, "\r", "\\\r");
         
         content = replace(content, "\n", "\\\n");
       }
       if ((this.firstColumn) && (content.length() > 0) && 
         (content.charAt(0) == this.userSettings.Comment)) {
         if (content.length() > 1) {
           content = 
             "\\" + this.userSettings.Comment + content.substring(1);
         } else {
           content = "\\" + this.userSettings.Comment;
         }
       }
     }
     this.outputStream.write(content);
     if (textQualify) {
       this.outputStream.write(this.userSettings.TextQualifier);
     }
     this.firstColumn = false;
   }
   
   public void write(String content)
     throws IOException
   {
     write(content, false);
   }
   
   public void writeComment(String commentText)
     throws IOException
   {
     checkClosed();
     
     checkInit();
     
     this.outputStream.write(this.userSettings.Comment);
     
     this.outputStream.write(commentText);
     if (this.useCustomRecordDelimiter) {
       this.outputStream.write(this.userSettings.RecordDelimiter);
     } else {
       this.outputStream.println();
     }
     this.firstColumn = true;
   }
   
   public void writeRecord(String[] values, boolean preserveSpaces)
     throws IOException
   {
     if ((values != null) && (values.length > 0))
     {
       for (int i = 0; i < values.length; i++) {
         write(values[i], preserveSpaces);
       }
       endRecord();
     }
   }
   
   public void writeRecord(String[] values)
     throws IOException
   {
     writeRecord(values, false);
   }
   
   public void endRecord()
     throws IOException
   {
     checkClosed();
     
     checkInit();
     if (this.useCustomRecordDelimiter) {
       this.outputStream.write(this.userSettings.RecordDelimiter);
     } else {
       this.outputStream.println();
     }
     this.firstColumn = true;
   }
   
   private void checkInit()
     throws IOException
   {
     if (!this.initialized)
     {
       if (this.fileName != null)
       {
         OutputStream output = new FileOutputStream(this.fileName);
         writeBom(output);
         this.outputStream = new PrintWriter(new OutputStreamWriter(output, 
           this.charset));
       }
       this.initialized = true;
     }
   }
   
   public void flush()
   {
     this.outputStream.flush();
   }
   
   public void close()
   {
     if (!this.closed)
     {
       close(true);
       
       this.closed = true;
     }
   }
   
   private void close(boolean closing)
   {
     if (!this.closed)
     {
       if (closing) {
         this.charset = null;
       }
       try
       {
         if (this.initialized) {
           this.outputStream.close();
         }
       }
       catch (Exception localException) {}
       this.outputStream = null;
       
       this.closed = true;
     }
   }
   
   private void checkClosed()
     throws IOException
   {
     if (this.closed) {
       throw new IOException(
         "This instance of the CsvWriter class has already been closed.");
     }
   }
   
   protected void finalize()
   {
     close(false);
   }
   
   private class Letters
   {
     public static final char LF = '\n';
     public static final char CR = '\r';
     public static final char QUOTE = '"';
     public static final char COMMA = ',';
     public static final char SPACE = ' ';
     public static final char TAB = '\t';
     public static final char POUND = '#';
     public static final char BACKSLASH = '\\';
     public static final char NULL = '\000';
     
     private Letters() {}
   }
   
   private class UserSettings
   {
     public char TextQualifier = '"';
     public boolean UseTextQualifier = true;
     public char Delimiter = ',';
     public char RecordDelimiter = '\000';
     public char Comment = '#';
     public int EscapeMode = 1;
     public boolean ForceQualifier = false;
     
     public UserSettings() {}
   }
   
   public static String replace(String original, String pattern, String replace)
   {
     int len = pattern.length();
     int found = original.indexOf(pattern);
     if (found > -1)
     {
       StringBuffer sb = new StringBuffer();
       int start = 0;
       while (found != -1)
       {
         sb.append(original.substring(start, found));
         sb.append(replace);
         start = found + len;
         found = original.indexOf(pattern, start);
       }
       sb.append(original.substring(start));
       
       return sb.toString();
     }
     return original;
   }
 }