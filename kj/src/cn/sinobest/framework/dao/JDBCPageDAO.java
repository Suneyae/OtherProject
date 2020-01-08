 package cn.sinobest.framework.dao;
 
 import cn.sinobest.framework.comm.Environment;
 import cn.sinobest.framework.comm.dialect.Dialect;
 import cn.sinobest.framework.comm.iface.IPageDAO;
 import java.sql.Connection;
 import java.sql.ResultSet;
 import java.sql.ResultSetMetaData;
 import java.sql.Statement;
 import java.util.ArrayList;
 import java.util.List;
 import javax.sql.DataSource;
 import org.apache.log4j.Logger;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.jdbc.core.JdbcTemplate;
 import org.springframework.stereotype.Repository;
 
 @Repository
 public class JDBCPageDAO
   implements IPageDAO
 {
   private static final Logger LOGGER = Logger.getLogger(JDBCPageDAO.class);
   @Autowired
   private JdbcTemplate jdbcTemplate;
   
   public Long getCount(String queryString)
     throws Exception
   {
     StringBuffer countQueryString = new StringBuffer("select count(1) cnt from (").append(queryString).append(") n");
     return Long.valueOf(this.jdbcTemplate.queryForLong(countQueryString.toString()));
   }
   
   public List<String[]> getData(String sql, int start, int pageSize)
     throws Exception
   {
     Dialect d = Dialect.getDialect();
     String exeSql = d.getLimitString(sql, start, pageSize);
     Connection conn = null;
     Statement stmt = null;
     ResultSet rs = null;
     List<String[]> rst = new ArrayList();
     String[] colnames = (String[])null;
     if (LOGGER.isDebugEnabled())
     {
       LOGGER.debug("分页前的语句是：" + sql);
       LOGGER.debug("分页后的句是：" + exeSql);
     }
     try
     {
       conn = this.jdbcTemplate.getDataSource().getConnection();
       stmt = conn.createStatement();
       rs = stmt.executeQuery(exeSql);
       ResultSetMetaData meta = rs.getMetaData();
       int colCount = meta.getColumnCount();
       
       colnames = new String[colCount];
       for (int i = 0; i < colCount; i++) {
         colnames[i] = meta.getColumnLabel(i + 1);
       }
       rst.add(colnames);
       String[] cols = (String[])null;
       while (rs.next())
       {
         cols = new String[colCount];
         for (int i = 0; i < colCount; i++) {
           cols[i] = rs.getString(i + 1);
         }
         rst.add(cols);
         if ((rst != null) && (rst.size() >= Integer.parseInt(Environment.NO_ROW_BOUNDS)))
         {
           String msg = "getDynDict SQL:" + exeSql + "查询结果已达到" + Environment.NO_ROW_BOUNDS + "最大行数限制!";
           LOGGER.warn(msg);
           break;
         }
       }
     }
     catch (Exception e)
     {
       throw e;
     }
     finally
     {
       if (rs != null)
       {
         rs.close();
         rs = null;
       }
       if (stmt != null)
       {
         stmt.close();
         stmt = null;
       }
       if (conn != null)
       {
         conn.close();
         conn = null;
       }
     }
     return rst;
   }
 }