package cn.sinobest.framework.comm.iface;

import java.io.Serializable;
import java.util.Map;

public abstract interface IDTO
  extends Serializable
{
  public static final String DTO = "dto";
  public static final String FUNCID = "funcID";
  
  public abstract IOperator getUserInfo();
  
  public abstract void setUserInfo(IOperator paramIOperator);
  
  public abstract Map<String, Object> getData();
  
  public abstract void setData(Map<String, Object> paramMap);
  
  public abstract StringBuffer getJSONData()
    throws Exception;
  
  public abstract void reflectEntity(Object paramObject)
    throws Exception;
  
  public abstract void setValue(String paramString, Object paramObject);
  
  public abstract Object getValue(String paramString);
  
  public abstract Object[] getValues(String paramString);
}

