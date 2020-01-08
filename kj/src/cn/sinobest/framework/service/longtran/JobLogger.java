 package cn.sinobest.framework.service.longtran;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.util.Util;
 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.sql.Statement;
 import javax.sql.DataSource;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.jdbc.core.JdbcTemplate;
 
 public class JobLogger
 {
   private static final Logger LOGGER = LoggerFactory.getLogger(JobLogger.class);
   private JdbcTemplate jdbcTemplate = (JdbcTemplate)Util.getBean("jdbcTemplate", JdbcTemplate.class);
   private final String LOG_SQL_I = "insert into FW_LONGTRANS_LOG (LOGID, KEY, XTJGDM, TRANSID, PROCESS, STARTTIME, BZ) values  (?, ?, ?, ?, ?, ?, ?)";
   private final String LOG_SQL_D = "delete from FW_LONGTRANS_LOG where TRANSID = ? and KEY = ?";
   private final String LOG_SQL_U = "update FW_LONGTRANS_LOG set ENDTIME = ? , BZ = ? where LOGID = ?";
   private final String LOG_DTL_SQL_I = "insert into FW_LONGTRANS_LOG_MX(LOGID, KEY,STEP, STEPNAME, CLZT, STARTTIME, BZ, TRANSID)  values(?, ?, ?, ?,  ?,  ?,  ?, ?)";
   private final String LOG_DTL_SQL_U = "update FW_LONGTRANS_LOG_MX set ENDTIME = ? ,BZ = ?,CLZT = ? where LOGID = ? and STEP = ?";
   private final String LOGID_SEQ = "SEQ_FW_LOGID";
   public static final String LOGID = "_logId";
   public static final String CLZT_INIT = "0";
   public static final String CLZT_DOING = "1";
   public static final String CLZT_SUCCESS = "2";
   public static final String CLZT_FAILURE = "4";
   
   public static enum LogIU
   {
     INSERT,  UPDATE;
   }
   
   public static JobLogger getInstance()
   {
     return new JobLogger();
   }
   
   public long log(LogIU state, JobLog jobLog)
   {
     Connection conn = null;
     PreparedStatement ps = null;
     long logId = 0L;
     try
     {
       conn = this.jdbcTemplate.getDataSource().getConnection();
       conn.setAutoCommit(false);
       int i = 1;
       if (state == LogIU.INSERT)
       {
         ps = conn.prepareStatement("delete from FW_LONGTRANS_LOG where TRANSID = ? and KEY = ?");
         ps.setString(i++, jobLog.getTRANSID());
         ps.setString(i++, jobLog.getKEY());
         ps.execute();
         ps = conn.prepareStatement("insert into FW_LONGTRANS_LOG (LOGID, KEY, XTJGDM, TRANSID, PROCESS, STARTTIME, BZ) values  (?, ?, ?, ?, ?, ?, ?)");
         logId = Long.parseLong(getSequence(conn, "SEQ_FW_LOGID"));
         i = 1;
         ps.setLong(i++, logId);
         ps.setString(i++, jobLog.getKEY());
         ps.setString(i++, jobLog.getXTJGDM());
         ps.setString(i++, jobLog.getTRANSID());
         ps.setString(i++, jobLog.getPROCESS());
         ps.setLong(i++, jobLog.getSTARTTIME().longValue());
         ps.setString(i++, jobLog.getBZ());
       }
       else
       {
         logId = jobLog.getLOGID().longValue();
         ps = conn.prepareStatement("update FW_LONGTRANS_LOG set ENDTIME = ? , BZ = ? where LOGID = ?");
         ps.setLong(i++, jobLog.getENDTIME().longValue());
         ps.setString(i++, jobLog.getBZ());
         ps.setLong(i++, logId);
       }
       ps.execute();
       conn.commit();
       
       return logId;
     }
     catch (SQLException e)
     {
       throw new AppException("生成日志记录出错，详细:" + e.getLocalizedMessage(), e);
     }
     finally
     {
       if (ps != null) {
         try
         {
           ps.close();
         }
         catch (SQLException e)
         {
           LOGGER.warn(e.getLocalizedMessage(), e);
         }
       }
       if (conn != null) {
         try
         {
           conn.close();
         }
         catch (SQLException e)
         {
           LOGGER.warn(e.getLocalizedMessage(), e);
         }
       }
     }
   }
   
   public void logDetial(LogIU state, JobLogDetail jobLogDetail)
   {
     Connection conn = null;
     PreparedStatement ps = null;
     try
     {
       conn = this.jdbcTemplate.getDataSource().getConnection();
       conn.setAutoCommit(false);
       int i = 1;
       if (state == LogIU.INSERT)
       {
         ps = conn.prepareStatement("insert into FW_LONGTRANS_LOG_MX(LOGID, KEY,STEP, STEPNAME, CLZT, STARTTIME, BZ, TRANSID)  values(?, ?, ?, ?,  ?,  ?,  ?, ?)");
         ps.setLong(i++, jobLogDetail.getLOGID().longValue());
         ps.setString(i++, jobLogDetail.getKEY());
         ps.setInt(i++, jobLogDetail.getSTEP());
         ps.setString(i++, jobLogDetail.getSTEPNAME());
         ps.setString(i++, jobLogDetail.getCLZT());
         ps.setLong(i++, jobLogDetail.getSTARTTIME().longValue());
         ps.setString(i++, jobLogDetail.getBZ());
         ps.setString(i++, jobLogDetail.getTRANSID());
       }
       else
       {
         ps = conn.prepareStatement("update FW_LONGTRANS_LOG_MX set ENDTIME = ? ,BZ = ?,CLZT = ? where LOGID = ? and STEP = ?");
         ps.setLong(i++, jobLogDetail.getENDTIME() == null ? 0L : jobLogDetail.getENDTIME().longValue());
         ps.setString(i++, (String)Util.nvl(jobLogDetail.getBZ()));
         ps.setString(i++, jobLogDetail.getCLZT());
         ps.setLong(i++, jobLogDetail.getLOGID().longValue());
         ps.setInt(i++, jobLogDetail.getSTEP());
       }
       ps.execute();
       conn.commit();
     }
     catch (SQLException e)
     {
       throw new AppException("生成日志记录出错，详细:" + e.getLocalizedMessage(), e);
     }
     finally
     {
       if (ps != null) {
         try
         {
           ps.close();
         }
         catch (SQLException e)
         {
           LOGGER.warn(e.getLocalizedMessage(), e);
         }
       }
       if (conn != null) {
         try
         {
           conn.close();
         }
         catch (SQLException e)
         {
           LOGGER.warn(e.getLocalizedMessage(), e);
         }
       }
     }
   }
   
   private String getSequence(Connection conn, String seqName)
     throws AppException
   {
     Statement ps = null;
     try
     {
       ps = conn.createStatement();
       ResultSet rs = ps.executeQuery("select " + seqName + ".nextval from dual");
       if (rs.next()) {
         return rs.getString(1);
       }
       ResultSet rs;
       throw new AppException("未获取到日志序列");
     }
     catch (SQLException e)
     {
       throw new AppException("获取日志序列出错，详细:" + e.getLocalizedMessage(), e);
     }
     finally
     {
       if (ps != null) {
         try
         {
           ps.close();
         }
         catch (SQLException e)
         {
           LOGGER.warn(e.getLocalizedMessage(), e);
         }
       }
     }
   }
 }