package cn.sinobest.framework.comm.iface;

import java.util.List;
import java.util.Map;

public abstract interface IBaseService
  extends IService
{
  public abstract Long count(Map paramMap)
    throws Exception;
  
  public abstract int deleteByKey(Map paramMap)
    throws Exception;
  
  public abstract int deleteByCondition(Map paramMap)
    throws Exception;
  
  public abstract int insert(Map paramMap)
    throws Exception;
  
  public abstract int insertSelective(Map paramMap)
    throws Exception;
  
  public abstract List select(Map paramMap)
    throws Exception;
  
  public abstract List selectSelective(Map paramMap)
    throws Exception;
  
  public abstract int updateByKey(Map paramMap)
    throws Exception;
  
  public abstract int updateSelectiveByKey(Map paramMap)
    throws Exception;
  
  public abstract int updateByCondition(Map paramMap)
    throws Exception;
  
  public abstract Map selectByKey(Map paramMap)
    throws Exception;
}