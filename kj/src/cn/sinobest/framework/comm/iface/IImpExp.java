 package cn.sinobest.framework.comm.iface;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.service.file.ImpExpAbstract.ImpExpParameter;
 import java.io.Closeable;
 import java.io.File;
 import java.util.Map;
 import java.util.regex.Pattern;
 
 public abstract interface IImpExp
   extends Closeable
 {
   public static final Pattern uploadSqlPattern = Pattern.compile("#\\{\\s*(\\w+?)\\s*\\}");
   
   public abstract void close();
   
   public abstract Object importFile(File paramFile, ImpExpAbstract.ImpExpParameter paramImpExpParameter, IUploadWork paramIUploadWork)
     throws Exception;
   
   public abstract void prepareDictData(Map<String, String> paramMap)
     throws AppException;
   
   public abstract Object importFile(File paramFile, ImpExpAbstract.ImpExpParameter paramImpExpParameter)
     throws AppException;
 }