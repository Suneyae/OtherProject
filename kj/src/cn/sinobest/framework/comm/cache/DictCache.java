 package cn.sinobest.framework.comm.cache;
 
 import cn.sinobest.framework.comm.iface.IDAO;
 import cn.sinobest.framework.util.ConfUtil;
 import cn.sinobest.framework.util.Util;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 public final class DictCache
   implements ICache<CacheItem<HashMap<String, HashMap<String, String>>>>
 {
   private static final long serialVersionUID = 4892280324676072765L;
   private static final Logger LOGGER = LoggerFactory.getLogger(DictCache.class);
   private static ICache<CacheItem<HashMap<String, HashMap<String, String>>>> oCache = new DictCache();
   private static HashMap<String, CacheItem<HashMap<String, HashMap<String, String>>>> cache;
   
   private DictCache()
   {
     cache = new HashMap();
   }
   
   public static synchronized ICache<CacheItem<HashMap<String, HashMap<String, String>>>> getInstance()
   {
     return oCache;
   }
   
   public String getCacheName()
   {
     return Cache.CACHE_DICT.name;
   }
   
   public void refreshAll(IDAO dao)
     throws Exception
   {
     getAllDict(dao);
   }
   
   public CacheItem<HashMap<String, HashMap<String, String>>> refreshByKey(IDAO dao, String key)
     throws Exception
   {
     Map<String, String> args = new HashMap();
     args.put("AAA100", key);
     boolean flag = false;
     try
     {
       List ls = dao.select("FW_CONFIG.V_AA10_Q", args);
       CacheItem<HashMap<String, HashMap<String, String>>> code = new CacheItem();
       HashMap<String, HashMap<String, String>> item = new HashMap();
       for (Iterator rows = ls.iterator(); rows.hasNext();)
       {
         flag = true;
         HashMap<String, String> o = (HashMap)rows.next();
         item.put((String)o.get("AAA102"), (HashMap)Util.mapClone(o));
       }
       if (flag)
       {
         code.setItem(item);
         code.setCode(key);
         if (!ConfUtil.isCacheSwitch(getCacheName())) {
           break label155;
         }
         cache.put(key, code);
       }
       label155:
       return null;
     }
     catch (Exception e)
     {
       StringBuffer msg = new StringBuffer("查找").append(getCacheName()).append(e.getMessage());
       LOGGER.error(msg.toString(), e);
       throw new Exception(msg.toString(), e);
     }
   }
   
   public CacheItem<HashMap<String, HashMap<String, String>>> getItem(IDAO dao, String key)
     throws Exception
   {
     CacheItem<HashMap<String, HashMap<String, String>>> item = (CacheItem)cache.get(key);
     if (item == null) {
       return refreshByKey(dao, key);
     }
     return (CacheItem)cache.get(key);
   }
   
   public void setItem(String key, CacheItem<HashMap<String, HashMap<String, String>>> value)
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
   
   private Map<String, CacheItem<HashMap<String, HashMap<String, String>>>> getAllDict(IDAO dao)
     throws Exception
   {
     try
     {
       HashMap<String, CacheItem<HashMap<String, HashMap<String, String>>>> cacheTmp = new HashMap();
       boolean isCache = false;
       if (ConfUtil.isCacheSwitch(getCacheName())) {
         isCache = true;
       }
       List<Map<String, Object>> lsItem = dao.select("FW_CONFIG.V_AA10_Q", null);
       for (Map<String, Object> dItem : lsItem)
       {
         String key = (String)dItem.get("AAA100");
         String AAA102 = (String)dItem.get("AAA102");
         CacheItem<HashMap<String, HashMap<String, String>>> code = (CacheItem)cacheTmp.get(key);
         if (code == null)
         {
           code = new CacheItem();
           HashMap<String, HashMap<String, String>> codeItem = new HashMap();
           codeItem.put(AAA102, (HashMap)Util.mapClone(dItem));
           code.setItem(codeItem);
           code.setCode(key);
           
           cacheTmp.put(key, code);
           if (isCache) {
             cache.put(key, code);
           }
         }
         else
         {
           HashMap<String, HashMap<String, String>> codeItem = (HashMap)code.getItem();
           if (codeItem.get(AAA102) == null) {
             codeItem.put(AAA102, (HashMap)Util.mapClone(dItem));
           }
         }
       }
       return cacheTmp;
     }
     catch (Exception e)
     {
       StringBuffer msg = new StringBuffer("获取所有字典").append(getCacheName()).append(e.getMessage());
       LOGGER.error(msg.toString(), e);
       throw new Exception(msg.toString(), e);
     }
   }
   
   public Map<?, ?> getAllItem(IDAO dao)
     throws Exception
   {
     if (ConfUtil.isCacheSwitch(getCacheName())) {
       return cache;
     }
     return getAllDict(dao);
   }
 }