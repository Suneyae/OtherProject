 package cn.sinobest.framework.comm.cache;
 
 import cn.sinobest.framework.comm.iface.IDAO;
 import cn.sinobest.framework.dao.workflow.WfActionDef;
 import cn.sinobest.framework.util.ConfUtil;
 import cn.sinobest.framework.util.Util;
 import java.util.Collections;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 public final class WfConfCache
   implements ICache<CacheItem<WfActionDef>>
 {
   private static final Logger LOGGER = LoggerFactory.getLogger(WfConfCache.class);
   private static final long serialVersionUID = -7466721132273228061L;
   private static ICache<CacheItem<WfActionDef>> oCache = new WfConfCache();
   private static HashMap<String, CacheItem<WfActionDef>> cache;
   
   private WfConfCache()
   {
     cache = new HashMap();
   }
   
   public static synchronized ICache<CacheItem<WfActionDef>> getInstance()
   {
     return oCache;
   }
   
   public String getCacheName()
   {
     return Cache.CACHE_WORKFLOW.name;
   }
   
   public CacheItem<WfActionDef> getItem(IDAO dao, String key)
     throws Exception
   {
     CacheItem<WfActionDef> item = (CacheItem)cache.get(key);
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
       List ls = dao.select("FW_CONFIG.WF_ACTION_DEF_Q", null);
       for (Iterator rows = ls.iterator(); rows.hasNext();)
       {
         CacheItem<WfActionDef> code = new CacheItem();
         WfActionDef o = (WfActionDef)rows.next();
         String key = o.getPROCESS_DEF_ID() + "|" + o.getACTION_DEF_ID();
         code.setItem((WfActionDef)o.clone());
         code.setCode(o.getPROCESS_DEF_ID());
         code.setSubCode(o.getACTION_DEF_ID());
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
   
   public CacheItem<WfActionDef> refreshByKey(IDAO dao, String key)
     throws Exception
   {
     String[] params = key.split("\\|");
     if (params.length < 2)
     {
       LOGGER.error("传入的key必须是:PROCESS_DEF_ID|ACTION_DEF_ID");
       return null;
     }
     Map<String, String> args = new HashMap();
     args.put("PROCESS_DEF_ID", params[0]);
     args.put("ACTION_DEF_ID", params[1]);
     
     boolean flag = false;
     try
     {
       List ls = dao.select("FW_CONFIG.WF_ACTION_DEF_Q", args);
       CacheItem<WfActionDef> code = new CacheItem();
       for (Iterator rows = ls.iterator(); rows.hasNext();)
       {
         flag = true;
         WfActionDef o = (WfActionDef)rows.next();
         code.setItem((WfActionDef)o.clone());
       }
       if (flag)
       {
         code.setCode(params[0]);
         code.setSubCode(params[1]);
         if (!ConfUtil.isCacheSwitch(getCacheName())) {
           break label181;
         }
         cache.put(key, code);
       }
       label181:
       return null;
     }
     catch (Exception e)
     {
       StringBuffer msg = new StringBuffer("查找").append(getCacheName()).append(e.getMessage());
       LOGGER.error(msg.toString(), e);
       throw new Exception(msg.toString(), e);
     }
   }
   
   public void setItem(String key, CacheItem<WfActionDef> value)
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
   
   public Map<String, CacheItem<WfActionDef>> getAll4DB(IDAO dao)
     throws Exception
   {
     try
     {
       List ls = dao.select("FW_CONFIG.WF_ACTION_DEF_Q", null);
       Map<String, CacheItem<WfActionDef>> cacheTmp = new HashMap();
       for (Iterator rows = ls.iterator(); rows.hasNext();)
       {
         CacheItem<WfActionDef> code = new CacheItem();
         WfActionDef o = (WfActionDef)rows.next();
         String key = o.getPROCESS_DEF_ID() + "|" + o.getACTION_DEF_ID();
         code.setItem((WfActionDef)o.clone());
         code.setCode(o.getPROCESS_DEF_ID());
         code.setSubCode(o.getACTION_DEF_ID());
         cacheTmp.put(key, code);
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
   
   public Map getAllItem(IDAO dao)
     throws Exception
   {
     if (ConfUtil.isCacheSwitch(getCacheName())) {
       return Collections.unmodifiableMap(cache);
     }
     return getAll4DB(dao);
   }
 }