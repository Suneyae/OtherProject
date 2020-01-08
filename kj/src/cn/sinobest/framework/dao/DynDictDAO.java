 package cn.sinobest.framework.dao;
 
 import cn.sinobest.framework.comm.Environment;
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.dao.mapper.SQLModel;
 import cn.sinobest.framework.util.ConfUtil;
 import cn.sinobest.framework.util.Util;
 import java.util.List;
 import java.util.Map;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.orm.ibatis3.SqlSessionTemplate;
 import org.springframework.orm.ibatis3.support.SqlSessionDaoSupport;
 import org.springframework.stereotype.Repository;
 
 @Repository
 public class DynDictDAO
   extends SqlSessionDaoSupport
 {
   private static final Logger LOGGER = LoggerFactory.getLogger(DynDictDAO.class);
   
   public List getDynDict(String code, String where)
     throws AppException
   {
     if (code == null) {
       throw new AppException("动态字典不能为空");
     }
     String sqlStr = ConfUtil.getSqlConf(code);
     if (Util.isEmpty(sqlStr)) {
       throw new AppException("未获取动态字典" + code + "的配置SQL");
     }
     StringBuffer sql = new StringBuffer(sqlStr);
     if (!"".equals(Util.nvl(where))) {
       sql.append(" where ").append(where);
     }
     SQLModel sqlModel = new SQLModel();
     sqlModel.setSql(sql.toString());
     
     List<Map<String, Object>> rs = getSqlSessionTemplate().selectList("FW_CONFIG.exeSql", sqlModel, Environment.getRowBounds());
     if ((rs != null) && (rs.size() == Integer.parseInt(Environment.NO_ROW_BOUNDS)))
     {
       String msg = "DynDict SQL:" + sql.toString() + "查询结果已达到" + Environment.NO_ROW_BOUNDS + "最大行数限制!";
       LOGGER.warn(msg);
     }
     return rs;
   }
 }