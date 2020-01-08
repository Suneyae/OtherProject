package cn.sinobest.framework.comm.cache;

import cn.sinobest.framework.comm.iface.IDAO;
import java.io.Serializable;
import java.util.Map;

public abstract interface ICache<T>
  extends Serializable
{
  public abstract String getCacheName();
  
  public abstract void refreshAll(IDAO paramIDAO)
    throws Exception;
  
  public abstract T getItem(IDAO paramIDAO, String paramString)
    throws Exception;
  
  public abstract Map getAllItem(IDAO paramIDAO)
    throws Exception;
  
  public abstract void setItem(String paramString, T paramT);
  
  public abstract T refreshByKey(IDAO paramIDAO, String paramString)
    throws Exception;
}