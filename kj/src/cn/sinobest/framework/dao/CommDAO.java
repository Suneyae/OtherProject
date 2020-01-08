 package cn.sinobest.framework.dao;
 
 import cn.sinobest.framework.comm.Environment;
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IDAO;
 import cn.sinobest.framework.dao.mapper.SQLModel;
 import cn.sinobest.framework.util.Util;
 import java.sql.SQLException;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import org.apache.ibatis.executor.BatchResult;
 import org.apache.ibatis.executor.Executor;
 import org.apache.ibatis.session.Configuration;
 import org.apache.ibatis.session.ExecutorType;
 import org.apache.ibatis.session.RowBounds;
 import org.apache.ibatis.session.SqlSession;
 import org.apache.ibatis.session.defaults.DefaultSqlSession;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.orm.ibatis3.SqlSessionCallback;
 import org.springframework.orm.ibatis3.SqlSessionTemplate;
 import org.springframework.orm.ibatis3.support.SqlSessionDaoSupport;
 import org.springframework.stereotype.Repository;
 
 @Repository("commDAO")
 public class CommDAO
   extends SqlSessionDaoSupport
   implements IDAO
 {
   private static final Logger LOGGER = LoggerFactory.getLogger(CommDAO.class);
   
   public Long count(final String statement, final Map paramsMap)
     throws AppException
   {
     (Long)execute(statement, new ISqlCallbak()
     {
       public Long doSql()
         throws SQLException
       {
         return (Long)CommDAO.this.getSqlSessionTemplate().selectOne(statement, paramsMap);
       }
     });
   }
   
   public <T> List<T> selectListType(final String statement, final Map paramsMap)
     throws AppException
   {
     (List)execute(statement, new ISqlCallbak()
     {
       public List<T> doSql()
         throws SQLException
       {
         List<T> rs = CommDAO.this.getSqlSessionTemplate().selectList(statement, paramsMap, Environment.getRowBounds());
         if ((rs != null) && (rs.size() == Integer.parseInt(Environment.NO_ROW_BOUNDS)))
         {
           String msg = "SQL:" + statement + "查询结果已达到" + Environment.NO_ROW_BOUNDS + "最大行数限制!";
           CommDAO.LOGGER.warn(msg);
           throw new AppException(msg);
         }
         return rs;
       }
     });
   }
   
   public <T> T selectOneType(final String statement, final Map paramsMap)
     throws AppException
   {
     execute(statement, new ISqlCallbak()
     {
       public T doSql()
         throws SQLException
       {
         List<Map<String, Object>> rs = CommDAO.this.getSqlSessionTemplate().selectList(statement, paramsMap, new RowBounds(0, 1));
         if (rs == null) {
           return null;
         }
         if (rs.size() == 1) {
           return rs.get(0);
         }
         if (rs.size() > 1)
         {
           String msg = "使用selectOneType获取一行数据（或null)，但查出了: " + rs.size() + "行";
           CommDAO.LOGGER.warn(msg);
           throw new AppException(msg);
         }
         return null;
       }
     });

return null;   }
   
   public String sequence(final String statement)
     throws AppException
   {
     (String)execute(statement, new ISqlCallbak()
     {
       public String doSql()
         throws SQLException
       {
         Object sequence = CommDAO.this.getSqlSessionTemplate().selectOne(statement);
         String d = "";
         if (sequence != null) {
           if ((sequence instanceof Long)) {
             d = String.valueOf(sequence);
           } else {
             d = (String)sequence;
           }
         }
         if ("-1".equals(d.trim())) {
           throw new AppException("sequence nextval error!");
         }
         return d;
       }
     });
   }
   
   public List<Map<String, Object>> select(final String statement, final Map paramsMap)
     throws AppException
   {
     (List)execute(statement, new ISqlCallbak()
     {
       public List<Map<String, Object>> doSql()
         throws SQLException
       {
         List<Map<String, Object>> rs = CommDAO.this.getSqlSessionTemplate().selectList(statement, paramsMap, Environment.getRowBounds());
         if ((rs != null) && (rs.size() == Integer.parseInt(Environment.NO_ROW_BOUNDS)))
         {
           String msg = "SQL:" + statement + "查询结果已达到" + Environment.NO_ROW_BOUNDS + "最大行数限制!";
           CommDAO.LOGGER.warn(msg);
           throw new AppException(msg);
         }
         return rs;
       }
     });
   }
   
   public Map<String, Object> selectOne(final String statement, final Map paramsMap)
     throws AppException
   {
     (Map)execute(statement, new ISqlCallbak()
     {
       public Map<String, Object> doSql()
         throws SQLException
       {
         List<Map<String, Object>> rs = CommDAO.this.getSqlSessionTemplate().selectList(statement, paramsMap, new RowBounds(0, 1));
         if (rs == null) {
           return null;
         }
         if (rs.size() == 1) {
           return (Map)rs.get(0);
         }
         if (rs.size() > 1)
         {
           String msg = "使用selectOne获取一行数据（或null)，但查出了: " + rs.size() + "行";
           CommDAO.LOGGER.warn(msg);
           throw new AppException(msg);
         }
         return null;
       }
     });
   }
   
   public int insert(final String statement, final Map paramsMap)
     throws AppException
   {
     ((Integer)execute(statement, new ISqlCallbak()
     {
       public Integer doSql()
         throws SQLException
       {
         return Integer.valueOf(CommDAO.this.getSqlSessionTemplate().insert(statement, paramsMap));
       }
     })).intValue();
   }
   
   public int delete(final String statement, final Map paramsMap)
     throws AppException
   {
     ((Integer)execute(statement, new ISqlCallbak()
     {
       public Integer doSql()
         throws SQLException
       {
         return Integer.valueOf(CommDAO.this.getSqlSessionTemplate().delete(statement, paramsMap));
       }
     })).intValue();
   }
   
   public int update(final String statement, final Map paramsMap)
     throws AppException
   {
     ((Integer)execute(statement, new ISqlCallbak()
     {
       public Integer doSql()
         throws SQLException
       {
         return Integer.valueOf(CommDAO.this.getSqlSessionTemplate().update(statement, paramsMap));
       }
     })).intValue();
   }
   
   public List<Map<String, Object>> selectSQL(final SQLModel sqlModel)
     throws AppException
   {
     (List)execute(null, new ISqlCallbak()
     {
       public List<Map<String, Object>> doSql()
         throws SQLException
       {
         List<Map<String, Object>> rs = CommDAO.this.getSqlSessionTemplate().selectList("FW_CONFIG.exeSql", sqlModel);
         if ((rs != null) && (rs.size() == Integer.parseInt(Environment.NO_ROW_BOUNDS)))
         {
           String msg = "SQL:" + sqlModel.getSql() + "查询结果已达到" + Environment.NO_ROW_BOUNDS + "最大行数限制!";
           CommDAO.LOGGER.warn(msg);
           throw new AppException(msg);
         }
         return rs;
       }
     });
   }
   
   private <T> T execute(String statement, ISqlCallbak<T> action)
     throws AppException
   {
     try
     {
       LOGGER.info("执行SQL:" + statement);
       return action.doSql();
     }
     catch (Throwable e)
     {
       throw new AppException("DAO异常:" + e.getMessage(), e);
     }
   }
   
   public List<Map<String, String>> querySQL(String sql)
     throws AppException
   {
     if (Util.isEmpty(sql)) {
       return null;
     }
     SQLModel sqlModel = new SQLModel();
     sqlModel.setSql(sql);
     
     List<Map<String, String>> rs = getSqlSessionTemplate().selectList("FW_CONFIG.exeSql", sqlModel, Environment.getRowBounds());
     if ((rs != null) && (rs.size() == Integer.parseInt(Environment.NO_ROW_BOUNDS)))
     {
       String msg = "SQL:" + sql + "查询结果已达到" + Environment.NO_ROW_BOUNDS + "最大行数限制!";
       LOGGER.warn(msg);
       throw new AppException(msg);
     }
     return rs;
   }
   
   public int batchInsert(final String statement, final List<Map<String, Object>> params)
     throws AppException
   {
     ((Integer)getSqlSessionTemplate().execute(new SqlSessionCallback()
     {
       public Integer doInSqlSession(SqlSession sqlSession)
         throws SQLException
       {
         SqlSession batchSession = null;
         Executor batchExecutor = null;
         if ((sqlSession instanceof DefaultSqlSession))
         {
           batchExecutor = sqlSession.getConfiguration().newExecutor(((DefaultSqlSession)sqlSession).getTransaction(), ExecutorType.BATCH);
           batchSession = new DefaultSqlSession(sqlSession.getConfiguration(), batchExecutor, false);
         }
         else
         {
           throw new AppException("DAO异常,批量执行异常!");
         }
         int batch = 0;int allNum = 0;
         int maxBatch = Environment.getBatchsql();
         for (Map<String, Object> param : params)
         {
           int n = batchSession.insert(statement, param);
           batch++;
           if ((maxBatch != -1) && (batch >= maxBatch))
           {
             sqlSession.commit();
             batch = 0;
           }
         }
         List<BatchResult> resultList = batchExecutor.flushStatements();
         int[] rs = (int[])null;
         int i;
         for (Iterator localIterator2 = resultList.iterator(); localIterator2.hasNext(); i < rs.length)
         {
           BatchResult br = (BatchResult)localIterator2.next();
           rs = br.getUpdateCounts();
           i = 0; continue;
           allNum += rs[i];i++;
         }
         return Integer.valueOf(allNum);
       }
     }, ExecutorType.BATCH)).intValue();
   }
   
   public int insert(final String statement, final Object o)
     throws AppException
   {
     ((Integer)execute(statement, new ISqlCallbak()
     {
       public Integer doSql()
         throws SQLException
       {
         return Integer.valueOf(CommDAO.this.getSqlSessionTemplate().insert(statement, o));
       }
     })).intValue();
   }
 }