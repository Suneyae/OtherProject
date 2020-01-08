 package cn.sinobest.framework.service.longtran;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.util.DateUtil.CurDate;
 import cn.sinobest.framework.util.Util;
 import java.io.PrintStream;
 import java.sql.Connection;
 import java.sql.Date;
 import java.sql.PreparedStatement;
 import java.sql.SQLException;
 
 public class LongProcessCase
 {
   public void execute(IDTO dto)
     throws InterruptedException, SQLException
   {
     Connection conn = null;
     try
     {
       long startTime = System.currentTimeMillis();
       
       Long logId = (Long)dto.getValue("_logId");
       
       String transId = (String)dto.getValue("_longTransID");
       String key = (String)dto.getValue("_longTransKey");
       
 
       int step = 1;
       
 
 
 
 
 
       JobLogger jobLogger = JobLogger.getInstance();
       
 
 
 
 
 
       JobLogDetail jobLogDetail = new JobLogDetail(logId, key, transId);
       
 
 
 
 
       jobLogDetail.setSTEP(step++);
       
       jobLogDetail.setSTEPNAME("处理1");
       
       jobLogDetail.setCLZT("1");
       
       jobLogDetail.setBZ("执行中...");
       
       jobLogDetail.setSTARTTIME(Long.valueOf(Long.parseLong(DateUtil.CurDate.YYYYMMDDHHmmss.getDate())));
       
 
       jobLogger.logDetial(JobLogger.LogIU.INSERT, jobLogDetail);
       
 
 
 
       String sql = "insert into test_job(id, name, itime)values(seq_test_job.nextval,?,?)";
       conn = Util.getConnection();
       
       PreparedStatement ps = conn.prepareStatement(sql);
       long n = 0L;
       for (long i = 0L; i < 20L; i += 1L)
       {
         ps.setString(1, "jobInsert" + i);
         ps.setDate(2, new Date(System.currentTimeMillis()));
         ps.addBatch();
         if (i % 200L == 0L)
         {
           n = i / 200L + 1L;
           System.out.println("( " + n + ")  commit");
           ps.executeBatch();
           ps.clearBatch();
           conn.commit();
         }
         Thread.sleep(1000L);
       }
       System.out.println("( " + n + 1 + ")  commit");
       ps.executeBatch();
       ps.clearBatch();
       conn.commit();
       
 
 
 
 
 
 
 
 
       jobLogDetail.setCLZT("2");
       
       jobLogDetail.setBZ("成功");
       
       jobLogDetail.setENDTIME(Long.valueOf(Long.parseLong(DateUtil.CurDate.YYYYMMDDHHmmss.getDate())));
       
       jobLogger.logDetial(JobLogger.LogIU.UPDATE, jobLogDetail);
       
 
       long endTime = System.currentTimeMillis();
       
       System.out.println("cost time :" + (endTime - startTime));
     }
     finally
     {
       if (conn != null) {
         conn.close();
       }
     }
   }
   
   public void execute1(IDTO dto)
     throws AppException
   {
     long startTime = System.currentTimeMillis();
     
     int step = 1;
     
 
 
     Long logId = (Long)dto.getValue("_logId");
     
     String transId = (String)dto.getValue("_longTransID");
     
     String key = (String)dto.getValue("_longTransKey");
     
 
 
 
 
 
 
     JobLogDetail jobLogDetail = new JobLogDetail(logId, key, transId);
     
 
 
 
 
     jobLogDetail.setSTEP(step++);
     
     jobLogDetail.setSTEPNAME("处理1");
     
     jobLogDetail.setCLZT("1");
     
     jobLogDetail.setBZ("执行中...");
     
 
     Connection conn = Util.getConnection();
     
 
 
 
     String resp1 = (String)LongProcessCallBack.execute(conn, jobLogDetail, dto, new ILongProcessCallBack()
     {
       public String doAction(Connection conn, IDTO dto)
         throws AppException, SQLException
       {
         PreparedStatement ps = null;
         String resp = "";
         try
         {
           String sql = "insert into test_job(id, name, itime)values(seq_test_job.nextval,?,?)";
           
           ps = conn.prepareStatement(sql);
           for (long i = 0L; i < 20L; i += 1L)
           {
             ps.setString(1, "jobInsert" + i);
             ps.setDate(2, new Date(System.currentTimeMillis()));
             ps.addBatch();
             if (i % 200L == 0L)
             {
               ps.executeBatch();
               ps.clearBatch();
               conn.commit();
             }
           }
           ps.executeBatch();
           ps.clearBatch();
           conn.commit();
         }
         catch (Exception ex)
         {
           conn.rollback();
           resp = "error";
           
           throw new AppException("业务处理时出现异常，详细:" + ex.getLocalizedMessage(), ex);
         }
         finally
         {
           if (resp.equalsIgnoreCase("error")) {
             Util.closeConnection(conn, ps);
           }
           Util.closeConnection(null, ps);
         }
         return resp;
       }
     });
     jobLogDetail.setSTEP(step++);
     
     jobLogDetail.setSTEPNAME("处理2");
     
     jobLogDetail.setCLZT("1");
     
     jobLogDetail.setBZ("执行中...");
     
 
 
 
 
     LongProcessCallBack.execute(conn, jobLogDetail, dto, new ILongProcessCallBack()
     {
       public String doAction(Connection conn, IDTO dto)
         throws AppException, SQLException
       {
         String sql = "insert into test_job(id, name, itime)values(seq_test_job.nextval,?,?)";
         
 
 
         PreparedStatement ps = conn.prepareStatement(sql);
         
         long n = 0L;
         for (long i = 0L; i < 20L; i += 1L)
         {
           ps.setString(1, "jobInsert" + i);
           ps.setDate(2, new Date(System.currentTimeMillis()));
           ps.addBatch();
           if (i % 200L == 0L)
           {
             n = i / 200L + 1L;
             System.out.println("( " + n + 1 + ")  commit");
             ps.executeBatch();
             ps.clearBatch();
             conn.commit();
           }
           try
           {
             Thread.sleep(1000L);
           }
           catch (InterruptedException e)
           {
             e.printStackTrace();
           }
         }
         System.out.println("( " + n + 1 + ")  commit");
         ps.executeBatch();
         ps.clearBatch();
         conn.commit();
         return "";
       }
     });
     long endTime = System.currentTimeMillis();
     
     System.out.println("cost time :" + (endTime - startTime));
   }
 }