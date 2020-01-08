 package cn.sinobest.framework.comm.cache;
 
 import cn.sinobest.framework.comm.Environment;
 import cn.sinobest.framework.comm.Environment.RightType;
 import cn.sinobest.framework.comm.Environment.SecurityLevel;
 import cn.sinobest.framework.comm.iface.IDAO;
 import cn.sinobest.framework.util.ConfUtil;
 import cn.sinobest.framework.util.Util;
 import java.util.Collections;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import org.apache.log4j.Logger;
 
 public final class RightCache
   implements ICache<CacheItem<Map<String, String>>>
 {
   private static final long serialVersionUID = -3962332542409417110L;
   private static final Logger LOGGER = Logger.getLogger(RightCache.class);
   private static ICache<CacheItem<Map<String, String>>> oCache = new RightCache();
   private static Map<String, CacheItem<Map<String, String>>> cache;
   
   private RightCache()
   {
     cache = new HashMap();
   }
   
   public static synchronized ICache<CacheItem<Map<String, String>>> getInstance()
   {
     return oCache;
   }
   
   public String getCacheName()
   {
     return Cache.CACHE_RIGHT.name;
   }
   
   public CacheItem<Map<String, String>> getItem(IDAO dao, String key)
   {
     CacheItem<Map<String, String>> item = (CacheItem)cache.get(key);
     if (item == null) {
       refreshByKey(dao, key);
     }
     return (CacheItem)cache.get(key);
   }
   
   public void refreshAll(IDAO dao)
     throws Exception
   {
     try
     {
       List ls = dao.select("FW_CONFIG.FW_RIGHT_Q", null);
       
       String RightLevel = Environment.getSecurityLevel();
       boolean cacheResource = false;
       
       cacheResource = (!Util.isEmpty(RightLevel)) && (RightLevel.equals(Environment.SecurityLevel.RESOURCE.getCode()));
       for (Iterator rows = ls.iterator(); rows.hasNext();)
       {
         HashMap<String, String> o = (HashMap)rows.next();
         String key = (String)o.get("RIGHTID") + (String)o.get("URL");
         if ((cacheResource) || (!Environment.RightType.RESOURCE.getCode().equals(o.get("SUBSYSTYPE"))))
         {
           CacheItem<Map<String, String>> code = new CacheItem();
           code.setItem((HashMap)Util.mapClone(o));
           code.setCode(key);
           code.setSubCode((String)o.get("RIGHTID"));
           if (ConfUtil.isCacheSwitch(getCacheName())) {
             cache.put(key, code);
           }
         }
       }
     }
     catch (Exception e)
     {
       StringBuffer msg = new StringBuffer("刷新全部").append(getCacheName()).append(e.getMessage());
       LOGGER.error(msg.toString(), e);
       throw new Exception(msg.toString(), e);
     }
   }
   
   public Map<String, CacheItem<Map<String, String>>> getAll4DB(IDAO dao)
     throws Exception
   {
     try
     {
       List ls = dao.select("FW_CONFIG.FW_RIGHT_Q", null);
       Map<String, CacheItem<Map<String, String>>> cacheTmp = new HashMap();
       
       String RightLevel = Environment.getSecurityLevel();
       boolean cacheResource = false;
       
       cacheResource = (!Util.isEmpty(RightLevel)) && (RightLevel.equals(Environment.SecurityLevel.RESOURCE.getCode()));
       for (Iterator rows = ls.iterator(); rows.hasNext();)
       {
         Map<String, String> o = (HashMap)rows.next();
         String key = (String)o.get("RIGHTID") + (String)o.get("URL");
         if ((cacheResource) || (!Environment.RightType.RESOURCE.getCode().equals(o.get("SUBSYSTYPE"))))
         {
           CacheItem<Map<String, String>> code = new CacheItem();
           code.setItem((HashMap)Util.mapClone(o));
           code.setCode(key);
           code.setSubCode((String)o.get("RIGHTID"));
           cacheTmp.put(key, code);
         }
       }
       return cacheTmp;
     }
     catch (Exception e)
     {
       StringBuffer msg = new StringBuffer("刷新全部").append(getCacheName()).append(e.getMessage());
       LOGGER.error(msg.toString(), e);
       throw new Exception(msg.toString(), e);
     }
   }
   
   public CacheItem<Map<String, String>> refreshByKey(IDAO dao, String key)
   {
     return null;
   }
   
   public void setItem(String key, CacheItem<Map<String, String>> value)
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
   
   public Map<String, CacheItem<Map<String, String>>> getAllItem(IDAO dao)
     throws Exception
   {
     if (ConfUtil.isCacheSwitch(getCacheName())) {
       return Collections.unmodifiableMap(cache);
     }
     return getAll4DB(dao);
   }
 }