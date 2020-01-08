 package cn.sinobest.framework.service.longtran;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.util.DateUtil.CurDate;
 import java.sql.Connection;
 
 public class LongProcessCallBack
 {
   public static <T> T execute(Connection conn, JobLogDetail jobLogDetail, IDTO dto, ILongProcessCallBack<T> action)
     throws AppException
   {
     JobLogger jobLogger = JobLogger.getInstance();
     String clzt = "";
     String bz = "";
     T resp = null;
     try
     {
       jobLogDetail.setSTARTTIME(Long.valueOf(Long.parseLong(DateUtil.CurDate.YYYYMMDDHHmmss.getDate())));
       jobLogDetail.setENDTIME(null);
       
       jobLogger.logDetial(JobLogger.LogIU.INSERT, jobLogDetail);
       
 
       resp = action.doAction(conn, dto);
       
       clzt = "2";
       bz = "成功";
     }
     catch (Exception e)
     {
       clzt = "4";
       bz = "失败";
       throw new AppException("执行业务处理失败,详细：" + e.getLocalizedMessage());
     }
     finally
     {
       jobLogDetail.setCLZT(clzt);
       
       jobLogDetail.setBZ(bz);
       
       jobLogDetail.setENDTIME(Long.valueOf(Long.parseLong(DateUtil.CurDate.YYYYMMDDHHmmss.getDate())));
       
       jobLogger.logDetial(JobLogger.LogIU.UPDATE, jobLogDetail);
     }
     return resp;
   }
 }