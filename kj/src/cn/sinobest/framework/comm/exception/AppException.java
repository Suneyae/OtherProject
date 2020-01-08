 package cn.sinobest.framework.comm.exception;
 
 import cn.sinobest.framework.comm.Environment;
 import cn.sinobest.framework.util.ConfUtil;
 import cn.sinobest.framework.util.Util;
 import java.io.IOException;
 import java.lang.reflect.InvocationTargetException;
 import java.sql.SQLException;
 import java.text.MessageFormat;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.dao.DataAccessException;
 
 public class AppException
   extends RuntimeException
 {
   private static final long serialVersionUID = 4179823710504251491L;
   private static final Logger LOGGER = LoggerFactory.getLogger(AppException.class);
   protected String args;
   protected String code;
   
   public AppException(String msg)
   {
     super(getMsgByCode(msg, ""));
     this.code = msg;
     LOGGER.error(msg);
   }
   
   public AppException(String msg, Throwable cause)
   {
     super(formatMsg(msg, cause, new Object[] { "" }), cause);
     this.code = msg;
   }
   
   public AppException(String msg, Throwable cause, Object... args)
   {
     super(formatMsg(msg, cause, args), cause);
     this.code = msg;
   }
   
   private static String formatMsg(String code, Throwable cause, Object... args)
   {
     String pattern = getMsgByCode(code, "");
     if (args == null) {
       return pattern;
     }
     String msg = MessageFormat.format(pattern, args);
     
     return msg;
   }
   
   public static String formatMsg(String code, Object... args)
   {
     String pattern = getMsgByCode(code, "");
     if (args == null) {
       return pattern;
     }
     String msg = MessageFormat.format(pattern, args);
     return msg;
   }
   
   public static String getMsgByCode(String code, String errMsg)
   {
     try
     {
       if ((code == null) || ("".equals(code.trim()))) {
         return errMsg;
       }
       if ((!code.startsWith("E")) && (!code.startsWith("M"))) {
         return code;
       }
       String rtnMsg = null;
       try
       {
         rtnMsg = ConfUtil.getTipMsg(code);
       }
       catch (Exception e)
       {
         LOGGER.error(e.getMessage(), e);
       }
       rtnMsg = Util.isEmpty(rtnMsg) ? errMsg : Util.isEmpty(errMsg) ? code : rtnMsg;
       if (Environment.getExceptionCodeSwitch()) {}
       return "CODE:" + code + "， " + rtnMsg;
     }
     catch (Exception ex)
     {
       LOGGER.error("获取提示代码出错,code=" + LOGGER, ex);
     }
     return code;
   }
   
   private static String argsReplace(String msg, String args)
   {
     if (msg == null) {
       return "";
     }
     if (args == null) {
       return msg;
     }
     String[] subStr = msg.split("\\?");
     String[] argsStr = args.split("\\|");
     StringBuffer result = new StringBuffer("");
     if (subStr.length == 0) {
       return msg;
     }
     for (int i = 0; i < subStr.length; i++)
     {
       result.append(subStr[i]);
       if (i < argsStr.length) {
         result.append(argsStr[i]);
       } else if (i < subStr.length - 1) {
         result.append("?");
       }
     }
     return result.toString();
   }
   
   public static AppException wrapException(String code, Throwable t, Object args)
     throws Exception
   {
     if ((t instanceof DataAccessException)) {
       return new AppException("数据库操作失败！", t, new Object[] { args });
     }
     if ((t instanceof NullPointerException)) {
       return new AppException("调用了未经初始化的对象或者是不存在的对象！", t, new Object[] { args });
     }
     if ((t instanceof IOException)) {
       return new AppException("IO异常！", t, new Object[] { args });
     }
     if ((t instanceof ClassNotFoundException)) {
       return new AppException("指定的类不存在！", t, new Object[] { args });
     }
     if ((t instanceof ArithmeticException)) {
       return new AppException("数学运算异常！", t, new Object[] { args });
     }
     if ((t instanceof ArrayIndexOutOfBoundsException)) {
       return new AppException("数组下标越界！", t, new Object[] { args });
     }
     if ((t instanceof NoSuchMethodException)) {
       return new AppException("未找到方法！", t, new Object[] { args });
     }
     if ((t instanceof IllegalArgumentException)) {
       return new AppException("方法的参数错误！", t, new Object[] { args });
     }
     if ((t instanceof IllegalAccessException)) {
       return new AppException("非法访问错误！", t, new Object[] { args });
     }
     if ((t instanceof InvocationTargetException)) {
       return new AppException("目标调用出现异常！", t, new Object[] { args });
     }
     if ((t instanceof ClassCastException)) {
       return new AppException("类型强制转换错误！", t, new Object[] { args });
     }
     if ((t instanceof SecurityException)) {
       return new AppException("违背安全原则异常！", t, new Object[] { args });
     }
     if ((t instanceof SQLException)) {
       return new AppException("操作数据库异常！", t, new Object[] { args });
     }
     if ((t instanceof NoSuchMethodError)) {
       return new AppException("方法末找到异常！", t, new Object[] { args });
     }
     if ((t instanceof InternalError)) {
       return new AppException("Java虚拟机发生了内部错误", t, new Object[] { args });
     }
     if ((t instanceof AppMsgException)) {
       return new AppMsgException(((AppMsgException)t).getMessage(), t, new Object[] { args });
     }
     if ((t instanceof AppException)) {
       return new AppException(((AppException)t).getMessage(), t, new Object[] { args });
     }
     return new AppException("操作失败，程序内部错误!", t, new Object[] { args });
   }
   
   public String getArgs()
   {
     return this.args;
   }
   
   public void setArgs(String args)
   {
     this.args = args;
   }
   
   public String getCode()
   {
     return this.code;
   }
 }