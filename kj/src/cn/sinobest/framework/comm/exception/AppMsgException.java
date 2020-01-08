 package cn.sinobest.framework.comm.exception;
 
 public class AppMsgException
   extends AppException
 {
   public AppMsgException(String msg)
   {
     super(msg);
   }
   
   public AppMsgException(String msg, Throwable cause)
   {
     super(msg, cause);
   }
   
   public AppMsgException(String msg, Throwable cause, Object... args)
   {
     super(msg, cause, args);
   }
 }