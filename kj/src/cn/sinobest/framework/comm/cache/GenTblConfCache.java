 package cn.sinobest.framework.comm.cache;
 
 import cn.sinobest.framework.comm.iface.IDAO;
 import cn.sinobest.framework.util.ConfUtil;
 import cn.sinobest.framework.util.Util;
 import java.util.HashMap;
 import java.util.Map;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 public final class GenTblConfCache
   implements ICache<CacheItem<Object>>
 {
   private static final Logger LOGGER = LoggerFactory.getLogger(GenTblConfCache.class);
   private static final long serialVersionUID = 8804200504728908536L;
   private static ICache<CacheItem<Object>> oCache = new GenTblConfCache();
   private static HashMap<String, CacheItem<Object>> cache;
   
   private GenTblConfCache()
   {
     cache = new HashMap();
   }
   
   public static synchronized ICache<CacheItem<Object>> getInstance()
   {
     return oCache;
   }
   
   public String getCacheName()
   {
     return Cache.CACHE_GENTBL.name;
   }
   
   public CacheItem<Object> getItem(IDAO dao, String key)
   {
     return (CacheItem)cache.get(key);
   }
   
   public void refreshAll(IDAO dao)
   {
     cache.clear();
   }
   
   public CacheItem<Object> refreshByKey(IDAO dao, String key)
   {
     cache.remove(key);
     return null;
   }
   
   public void setItem(String key, CacheItem<Object> value)
   {
     if ("".equals(Util.nvl(key)))
     {
       LOGGER.error("key 值为空");
       return;
     }
     if (ConfUtil.isCacheSwitch(getCacheName())) {
       cache.put(key, value);
     }
   }
   
   public Map getAllItem(IDAO dao)
   {
     return null;
   }
 }