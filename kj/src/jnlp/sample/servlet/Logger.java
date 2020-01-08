 package jnlp.sample.servlet;
 
 import java.io.FileWriter;
 import java.io.IOException;
 import java.io.PrintWriter;
 import java.text.MessageFormat;
 import java.util.MissingResourceException;
 import java.util.ResourceBundle;
 import javax.servlet.ServletConfig;
 import javax.servlet.ServletContext;
 
 public class Logger
 {
   public static final int NONE = 0;
   public static final String NONE_KEY = "NONE";
   public static final int FATAL = 1;
   public static final String FATAL_KEY = "FATAL";
   public static final int WARNING = 2;
   public static final String WARNING_KEY = "WARNING";
   public static final int INFORMATIONAL = 3;
   public static final String INFORMATIONAL_KEY = "INFORMATIONAL";
   public static final int DEBUG = 4;
   public static final String DEBUG_KEY = "DEBUG";
   private static final String LOG_LEVEL = "logLevel";
   private static final String LOG_PATH = "logPath";
   private int _loggingLevel = 1;
   private ServletContext _servletContext = null;
   private String _logFile = null;
   private String _servletName = null;
   ResourceBundle _resources = null;
   
   public Logger(ServletConfig config, ResourceBundle resources)
   {
     this._resources = resources;
     this._servletContext = config.getServletContext();
     this._servletName = config.getServletName();
     this._logFile = config.getInitParameter("logPath");
     if (this._logFile != null)
     {
       this._logFile = this._logFile.trim();
       if (this._logFile.length() == 0) {
         this._logFile = null;
       }
     }
     String level = config.getInitParameter("logLevel");
     if (level != null)
     {
       level = level.trim().toUpperCase();
       if (level.equals("NONE")) {
         this._loggingLevel = 0;
       }
       if (level.equals("FATAL")) {
         this._loggingLevel = 1;
       }
       if (level.equals("WARNING")) {
         this._loggingLevel = 2;
       }
       if (level.equals("INFORMATIONAL")) {
         this._loggingLevel = 3;
       }
       if (level.equals("DEBUG")) {
         this._loggingLevel = 4;
       }
     }
   }
   
   public void addFatal(String key, Throwable throwable)
   {
     logEvent(1, getString(key), throwable);
   }
   
   public void addWarning(String key, String arg)
   {
     logL10N(2, key, arg, null);
   }
   
   public void addWarning(String key, String arg, Throwable t)
   {
     logL10N(2, key, arg, t);
   }
   
   public void addWarning(String key, String arg1, String arg2)
   {
     logL10N(2, key, arg1, arg2);
   }
   
   public void addWarning(String key, String arg1, String arg2, String arg3)
   {
     logL10N(2, key, arg1, arg2, arg3);
   }
   
   public void addInformational(String key)
   {
     logEvent(3, getString(key), null);
   }
   
   public void addInformational(String key, String arg)
   {
     logL10N(3, key, arg, null);
   }
   
   public void addInformational(String key, String arg1, String arg2, String arg3)
   {
     logL10N(3, key, arg1, arg2, arg3);
   }
   
   public void addDebug(String msg)
   {
     logEvent(4, msg, null);
   }
   
   public void addDebug(String msg, Throwable throwable)
   {
     logEvent(4, msg, throwable);
   }
   
   boolean isNoneLevel()
   {
     return this._loggingLevel >= 0;
   }
   
   boolean isFatalevel()
   {
     return this._loggingLevel >= 1;
   }
   
   boolean isWarningLevel()
   {
     return this._loggingLevel >= 2;
   }
   
   boolean isInformationalLevel()
   {
     return this._loggingLevel >= 3;
   }
   
   boolean isDebugLevel()
   {
     return this._loggingLevel >= 4;
   }
   
   private String getString(String key)
   {
     try
     {
       return this._resources.getString(key);
     }
     catch (MissingResourceException mre) {}
     return "Missing resource for: " + key;
   }
   
   private void logL10N(int level, String key, String arg, Throwable e)
   {
     Object[] messageArguments = { arg };
     logEvent(level, applyPattern(key, messageArguments), e);
   }
   
   private void logL10N(int level, String key, String arg1, String arg2)
   {
     Object[] messageArguments = { arg1, arg2 };
     logEvent(level, applyPattern(key, messageArguments), null);
   }
   
   private void logL10N(int level, String key, String arg1, String arg2, String arg3)
   {
     Object[] messageArguments = { arg1, arg2, arg3 };
     logEvent(level, applyPattern(key, messageArguments), null);
   }
   
   private String applyPattern(String key, Object[] messageArguments)
   {
     String message = getString(key);
     MessageFormat formatter = new MessageFormat(message);
     String output = MessageFormat.format(message, messageArguments);
     return output;
   }
   
   private synchronized void logEvent(int level, String string, Throwable throwable)
   {
     if (level > this._loggingLevel) {
       return;
     }
     if (this._logFile != null)
     {
       PrintWriter pw = null;
       try
       {
         pw = new PrintWriter(new FileWriter(this._logFile, true));
         pw.println(this._servletName + "(" + level + "): " + string);
         if (throwable != null) {
           throwable.printStackTrace(pw);
         }
         pw.close();
         
 
         return;
       }
       catch (IOException localIOException) {}
     }
     if (throwable == null) {
       this._servletContext.log(string);
     } else {
       this._servletContext.log(string, throwable);
     }
   }
 }