package cn.sinobest.framework.comm.iface;

import cn.sinobest.framework.comm.exception.AppException;
import cn.sinobest.framework.dao.mapper.SQLModel;
import java.util.List;
import java.util.Map;

public abstract interface IDAO
{
  public abstract Long count(String paramString, Map paramMap)
    throws AppException;
  
  public abstract <T> T selectOneType(String paramString, Map paramMap)
    throws AppException;
  
  public abstract <T> List<T> selectListType(String paramString, Map paramMap)
    throws AppException;
  
  public abstract String sequence(String paramString)
    throws AppException;
  
  public abstract int delete(String paramString, Map paramMap)
    throws AppException;
  
  public abstract int insert(String paramString, Map paramMap)
    throws AppException;
  
  public abstract int insert(String paramString, Object paramObject)
    throws AppException;
  
  public abstract List<Map<String, Object>> select(String paramString, Map paramMap)
    throws AppException;
  
  public abstract Map<String, Object> selectOne(String paramString, Map paramMap)
    throws AppException;
  
  public abstract int update(String paramString, Map paramMap)
    throws AppException;
  
  public abstract List<Map<String, Object>> selectSQL(SQLModel paramSQLModel)
    throws AppException;
  
  public abstract List<Map<String, String>> querySQL(String paramString)
    throws AppException;
  
  public abstract int batchInsert(String paramString, List<Map<String, Object>> paramList)
    throws AppException;
}