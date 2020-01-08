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
 
 public final class TipMsgCache
   implements ICache<CacheItem<HashMap<String, String>>>
 {
   private static final Logger LOGGER = LoggerFactory.getLogger(TipMsgCache.class);
   private static final long serialVersionUID = 16519610449134315L;
   private static ICache<CacheItem<HashMap<String, String>>> oCache = new TipMsgCache();
   private static HashMap<String, CacheItem<HashMap<String, String>>> cache;
   
   private TipMsgCache()
   {
     cache = new HashMap();
   }
   
   public static synchronized ICache<CacheItem<HashMap<String, String>>> getInstance()
   {
     return oCache;
   }
   
   public String getCacheName()
   {
     return Cache.CACHE_TIPMSG.name;
   }
   
   public CacheItem<HashMap<String, String>> getItem(IDAO dao, String key)
     throws Exception
   {
     CacheItem<HashMap<String, String>> item = (CacheItem)cache.get(key);
     if (item == null) {
       return refreshByKey(dao, key);
     }
     return (CacheItem)cache.get(key);
   }
   
   public void refreshAll(IDAO dao)
     throws Exception
   {
     try
     {
       List ls = dao.select("FW_CONFIG.FW_SYSMESSAGE_Q", null);
       for (Iterator rows = ls.iterator(); rows.hasNext();)
       {
         CacheItem<HashMap<String, String>> code = new CacheItem();
         HashMap<String, String> o = (HashMap)rows.next();
         String key = (String)o.get("CODE");
         code.setItem((HashMap)Util.mapClone(o));
         code.setCode(key);
         if (ConfUtil.isCacheSwitch(getCacheName())) {
           cache.put(key, code);
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
   
   public CacheItem<HashMap<String, String>> refreshByKey(IDAO dao, String key)
     throws Exception
   {
     Map<String, String> args = new HashMap();
     args.put("CODE", key);
     boolean flag = false;
     try
     {
       List ls = dao.select("FW_CONFIG.FW_SYSMESSAGE_Q", args);
       CacheItem<HashMap<String, String>> code = new CacheItem();
       for (Iterator rows = ls.iterator(); rows.hasNext();)
       {
         flag = true;
         HashMap<String, String> o = (HashMap)rows.next();
         code.setItem((HashMap)Util.mapClone(o));
       }
       if (flag)
       {
         code.setCode(key);
         if (!ConfUtil.isCacheSwitch(getCacheName())) {
           break label128;
         }
         cache.put(key, code);
       }
       label128:
       return null;
     }
     catch (Exception e)
     {
       StringBuffer msg = new StringBuffer("查找").append(getCacheName()).append(e.getMessage());
       LOGGER.error(msg.toString(), e);
       throw new Exception(msg.toString(), e);
     }
   }
   
   public void setItem(String key, CacheItem<HashMap<String, String>> value)
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