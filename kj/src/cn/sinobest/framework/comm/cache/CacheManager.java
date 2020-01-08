package cn.sinobest.framework.comm.cache;

import cn.sinobest.framework.comm.Environment;
import cn.sinobest.framework.comm.iface.ICacheProvider;
import cn.sinobest.framework.comm.iface.IDAO;
import cn.sinobest.framework.util.ConfUtil;
import cn.sinobest.framework.util.Util;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheManager extends GeneralCacheAdministrator implements
		ICacheProvider<CacheItem<?>> {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CacheManager.class);
	private static final ThreadLocal<Cache> cacheType = new ThreadLocal();
	private static final long serialVersionUID = -9119059542544357620L;
	private IDAO dao;

	public static Cache getCacheType() {
		return (Cache) cacheType.get();
	}

	public static CacheManager setCacheType(Cache cacheType) {
		cacheType.set(cacheType);
		return (CacheManager) Environment.getCacheManager();
	}

	private CacheManager() {
	}

	public CacheManager(Properties p) {
		super(p);
	}

	private void putCache(ICache<CacheItem<?>> cache) {
		putInCache(cache.getCacheName(), cache);
	}

	public static <T> void put(String key, T value) {
		CacheManager cacheManager = (CacheManager) Environment
				.getCacheManager();
		cacheManager.putInCache(key, value);
	}

	public static Object get(String key) {
		CacheManager cacheManager = (CacheManager) Environment
				.getCacheManager();
		Object o = null;
		try {
			o = cacheManager.getFromCache(key);
		} catch (NeedsRefreshException e) {
			cacheManager.cancelUpdate(key);
		}
		return o;
	}

	public ICache<CacheItem<?>> getCache(Cache cacheType) throws Exception {
		boolean updated = false;
		ICache<CacheItem<?>> cache = null;
		try {
			cache = (ICache) getFromCache(cacheType.name);
			if (cache == null) {
				LOGGER.warn("未获取到类型为:" + cacheType + "缓存");
				throw new Exception("未获取到类型为:" + cacheType + "缓存");
			}
		} catch (NeedsRefreshException e) {
			try {
				cache = createCacheByType(cacheType);
				updated = true;
			} catch (Exception ex) {
				LOGGER.error("更新缓存:" + cacheType.desc + "出错", e);
			} finally {
				if (!updated) {
					cancelUpdate(cacheType.name);
				}
			}
		}
		return cache;
	}

	public CacheItem<?> getItem(Cache cacheType, String key) throws Exception {
		if ("".equals(Util.nvl(key))) {
			throw new Exception("key 不能为空");
		}
		if (cacheType == Cache.CACHE_DICT) {
			return (CacheItem) getCache(cacheType).getItem(this.dao, key);
		}
		return (CacheItem) getCache(cacheType).getItem(this.dao, key);
	}

	public Map<String, Object> getAllItem(Cache cacheType) throws Exception {
		return getCache(cacheType).getAllItem(this.dao);
	}

	public void setItem(Cache cacheType, String key, CacheItem<?> value)
			throws Exception {
		getCache(cacheType).setItem(key, value);
	}

	public void refreshAll() throws Exception {
		Cache[] cache = Cache.values();
		for (int i = 0; i < cache.length; i++) {
			Cache item = cache[i];
			refreshAll(item);
		}
	}

	public static Object getItem(String key) throws Exception {
		ICacheProvider cacheManager = Environment.getCacheManager();
		CacheItem cacheItem = (CacheItem) cacheManager.getItem(getCacheType(),
				key);
		if (cacheItem == null) {
			return null;
		}
		return cacheItem.getItem();
	}

	public static Object getAllItem() throws Exception {
		ICacheProvider cacheManager = Environment.getCacheManager();
		return cacheManager.getAllItem(getCacheType());
	}

	public static Map<String, String> getDictItem(String code, String itemcode)
			throws Exception {
		try {
			ICacheProvider cacheManager = Environment.getCacheManager();
			CacheItem cacheItem = (CacheItem) cacheManager.getItem(
					getCacheType(), code);
			HashMap<String, HashMap<String, String>> codeMap = (HashMap) cacheItem
					.getItem();
			return (HashMap) codeMap.get(itemcode);
		} catch (NullPointerException ex) {
		}
		return null;
	}

	public static Map<String, Map<String, String>> getDict(String code)
			throws Exception {
		try {
			ICacheProvider cacheManager = Environment.getCacheManager();
			CacheItem cacheItem = (CacheItem) cacheManager.getItem(
					getCacheType(), code);
			return (Map) cacheItem.getItem();
		} catch (NullPointerException ex) {
		}
		return null;
	}

	public static HashMap<String, CacheItem<HashMap<String, HashMap<String, String>>>> getDict()
			throws Exception {
		CacheManager cacheManager = (CacheManager) Environment
				.getCacheManager();
		return cacheManager.getAllDict();
	}

	private HashMap<String, CacheItem<HashMap<String, HashMap<String, String>>>> getAllDict()
			throws Exception {
		try {
			ICacheProvider cacheManager = Environment.getCacheManager();
			ICache cacheItem = cacheManager.getCache(Cache.CACHE_DICT);
			return (HashMap) cacheItem.getAllItem(this.dao);
		} catch (NullPointerException ex) {
		}
		return null;
	}

	public static void setItem(String key, CacheItem<?> value) throws Exception {
		ICacheProvider cacheManager = Environment.getCacheManager();
		cacheManager.setItem(getCacheType(), key, value);
	}

	public ICache createCacheByType(Cache cacheType) throws Exception {
		LOGGER.info("初始化缓存【" + cacheType.desc + "】");

		Method method = cacheType.cls.getMethod("getInstance", new Class[0]);
		ICache cache = (ICache) method.invoke(null, null);
		putCache(cache);
		return cache;
	}

	public void refreshAll(Cache cacheType) throws Exception {
		if (ConfUtil.isCacheSwitch(cacheType.name)) {
			LOGGER.info("系统正在初始化缓存【" + cacheType.desc + "】，请稍等...");
			try {
				cache = (ICache) getFromCache(cacheType.name);
			} catch (NeedsRefreshException e) {
				try {
					ICache cache;
					Method method = cacheType.cls.getMethod("getInstance",
							new Class[0]);
					ICache cache = (ICache) method.invoke(null, null);
					cache.refreshAll(this.dao);
					putCache(cache);
					return;
				} catch (Exception e1) {
					LOGGER.error("初始化缓存出错", e1);
					throw new Exception("初始化缓存出错", e1);
				}
			}
			ICache cache;
			getCache(cacheType).refreshAll(this.dao);
		}
	}

	public void refreshByKey(Cache cacheType, String key, CacheItem value)
			throws Exception {
		getCache(cacheType).refreshByKey(this.dao, key);
	}

	public IDAO getDao() {
		return this.dao;
	}

	public void setDao(IDAO dao) {
		this.dao = dao;
	}
}
