package cn.sinobest.framework.comm;

import cn.sinobest.framework.comm.cache.CacheItem;
import cn.sinobest.framework.comm.dialect.Dialect;
import cn.sinobest.framework.comm.iface.ICacheProvider;
import cn.sinobest.framework.util.ConfUtil;
import cn.sinobest.framework.util.PropertiesUtil;
import cn.sinobest.framework.util.ReflectionProvider;
import cn.sinobest.framework.util.Util;
import cn.sinobest.framework.web.his.IHisInitializer;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

public class Environment implements Serializable {
	private static final Logger LOGGER = Logger.getLogger(Environment.class);
	public static IHisInitializer hisInitializer = null;
	private static Map<String, Dialect> dialect = new HashMap();
	private static Map<String, String> cacheSwitch = new HashMap();
	private static ApplicationContext applicationContext = null;
	public static String NO_ROW_BOUNDS = null;
	public static RowBounds DEFAULT_ROW_BOUNDS = null;

	public static RowBounds getRowBounds() {
		if (DEFAULT_ROW_BOUNDS != null) {
			return DEFAULT_ROW_BOUNDS;
		}
		NO_ROW_BOUNDS = ConfUtil.getSysParamOnly("db.selectRowLimit", String.valueOf(2147483647));
		DEFAULT_ROW_BOUNDS = new RowBounds(0, Integer.parseInt(NO_ROW_BOUNDS));
		return DEFAULT_ROW_BOUNDS;
	}

	public static enum SecurityLevel {
		MENU("1"), RESOURCE("2");

		private final String code;

		private SecurityLevel(String c) {
			this.code = c;
		}

		public String getCode() {
			return this.code;
		}
	}

	public static enum RightType {
		RESOURCE("99"), MENU("1"), WORKFLOW("2"), TREENODE("3");

		private final String code;

		private RightType(String c) {
			this.code = c;
		}

		public String getCode() {
			return this.code;
		}
	}

	private static final Properties GLOBAL_PROPERTIES = new Properties();
	public static final String LOGIN_SESSION_ID = "OPERATOR";
	public static final String DEFAULT_DS = "ds";
	public static final int BATCHSQL = 100;
	public static final String SECURITYLEVEL = "SECURITYLEVEL";
	private static ServletContext servletContext;
	private static ICacheProvider<CacheItem<?>> cacheManager;
	public static final String JDBC_PROPERTIES = "/sysconfig.properties";

	static {
		try {
			InputStream stream = PropertiesUtil.getResourceAsStream("/sysconfig.properties");
			try {
				GLOBAL_PROPERTIES.load(stream);
				LOGGER.info("加载/sysconfig.properties");
				loadDialectInstance(GLOBAL_PROPERTIES);
				initCacheSwitch(GLOBAL_PROPERTIES);
			} catch (IOException e) {
				LOGGER.error("加载/sysconfig.properties失败!");
				try {
					stream.close();
				} catch (IOException e) {
					LOGGER.error("无法关闭输入流/sysconfig.properties", e);
				}
			} finally {
				try {
					stream.close();
				} catch (IOException e) {
					LOGGER.error("无法关闭输入流/sysconfig.properties", e);
				}
			}
			try {
				stream.close();
			} catch (IOException e) {
				LOGGER.error("无法关闭输入流/sysconfig.properties", e);
			}
			return;
		} catch (Exception ex) {
			LOGGER.info("/sysconfig.properties未找到");
		}
	}

	public static String getSecurityLevel() {
		return ConfUtil.getParam("SECURITYLEVEL");
	}

	public static Properties getProperties() {
		Properties copy = new Properties();
		copy.putAll(GLOBAL_PROPERTIES);
		return copy;
	}

	public static Dialect getDialect(String key) {
		if ((key == null) || (key.trim().equals(""))) {
			return (Dialect) dialect.get("ds");
		}
		return (Dialect) dialect.get(key);
	}

	public static ICacheProvider<CacheItem<?>> getCacheManager() {
		return cacheManager;
	}

	public static void setCacheManager(ICacheProvider<CacheItem<?>> cacheManager) {
		cacheManager = cacheManager;
	}

	public static ServletContext getServletContext() {
		return servletContext;
	}

	public static void setServletContext(ServletContext servletContext) {
		if (servletContext != null) {
			return;
		}
		servletContext = servletContext;
	}

	public static Map<String, String> getCacheSwitch() {
		return Collections.unmodifiableMap(cacheSwitch);
	}

	private static void loadDialectInstance(Properties p)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		LOGGER.info("系统正在初始化数据库方言...");
		Set e = p.keySet();
		for (Iterator it = e.iterator(); it.hasNext();) {
			String key = (String) it.next();
			if (key.lastIndexOf(".dialect") > 0) {
				String driverName = p.getProperty(key);
				String driverKey = key.substring(0, key.lastIndexOf(".dialect"));
				LOGGER.info("初始化数据库方言【" + driverName + "】");
				Dialect d = (Dialect) ReflectionProvider.classForName(driverName).newInstance();
				dialect.put(driverKey, d);
			}
		}
	}

	private static void initCacheSwitch(Properties p) {
		LOGGER.info("系统正在初始化缓存开关...");
		Set e = p.keySet();

		Pattern pt = Pattern.compile("^cache\\.(.*)Cache$", 2);
		for (Iterator it = e.iterator(); it.hasNext();) {
			String key = (String) it.next();
			Matcher m = pt.matcher(key);
			if (m.matches()) {
				String cacheName = (m.group(1) + "Cache").toLowerCase();
				String cacheSwitchValue = p.getProperty(key);
				if (!cacheSwitchValue.trim().matches("^true|false$")) {
					LOGGER.warn("缓存" + cacheName + "开关值必须为true 或 false");
					cacheSwitch.put(cacheName, "false");
					LOGGER.info("缓存【" + cacheName + "】开关为false");
				} else {
					cacheSwitch.put(cacheName, cacheSwitchValue);
					LOGGER.info("缓存【" + cacheName + "】开关为" + cacheSwitchValue);
				}
			}
		}
	}

	public static int getBatchsql() {
		String batch = ConfUtil.getParam("BATCHSQL");
		return Util.isEmpty(batch) ? 100 : Integer.valueOf(batch).intValue();
	}

	public static boolean getExceptionCodeSwitch() {
		try {
			String s = ConfUtil.getParam("EXCEPTION_CODE");
			if ("1".equals(s)) {
				return true;
			}
			return false;
		} catch (Exception e) {
			LOGGER.warn("未获取到是否显示异常代码开关", e);
		}
		return false;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static void setApplicationContext(ApplicationContext applicationContext) {
		applicationContext = applicationContext;
	}
}