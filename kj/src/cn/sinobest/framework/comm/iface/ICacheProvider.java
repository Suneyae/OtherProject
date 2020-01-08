package cn.sinobest.framework.comm.iface;

import cn.sinobest.framework.comm.cache.Cache;
import cn.sinobest.framework.comm.cache.CacheItem;
import cn.sinobest.framework.comm.cache.ICache;
import java.util.Map;

public abstract interface ICacheProvider<T>
{
  public abstract void refreshAll(Cache paramCache)
    throws Exception;
  
  public abstract ICache<CacheItem<?>> getCache(Cache paramCache)
    throws Exception;
  
  public abstract T getItem(Cache paramCache, String paramString)
    throws Exception;
  
  public abstract void setItem(Cache paramCache, String paramString, T paramT)
    throws Exception;
  
  public abstract void refreshByKey(Cache paramCache, String paramString, T paramT)
    throws Exception;
  
  public abstract Map<String, ?> getAllItem(Cache paramCache)
    throws Exception;
  
  public abstract void refreshAll()
    throws Exception;
}