package cn.sinobest.framework.util;

import cn.sinobest.framework.comm.Environment;
import cn.sinobest.framework.comm.Environment.RightType;
import cn.sinobest.framework.comm.cache.Cache;
import cn.sinobest.framework.comm.cache.CacheItem;
import cn.sinobest.framework.comm.cache.CacheManager;
import cn.sinobest.framework.comm.exception.AppException;
import cn.sinobest.framework.dao.DynDictDAO;
import cn.sinobest.framework.dao.workflow.WfActionDef;
import cn.sinobest.framework.dao.workflow.WfActionDef2Task;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

public class ConfUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfUtil.class);

	public static <T> void put(String key, T value) {
		CacheManager.put(key, value);
	}

	public static Object get(String key) {
		return CacheManager.get(key);
	}

	public static String getDict(String code, String itemcode) throws AppException {
		String err = "获取字典值出错，code=" + code + " ，itemcode=" + itemcode;
		try {
			CacheManager.setCacheType(Cache.CACHE_DICT);

			Map<String, Map<String, String>> dict = getDict(code);
			String AAA102 = "";
			for (Map.Entry<String, Map<String, String>> rowEntry : dict.entrySet()) {
				if (itemcode.equals(((Map) rowEntry.getValue()).get("ITEMCODE"))) {
					AAA102 = (String) ((Map) rowEntry.getValue()).get("AAA102");
					if (AAA102 != null) {
						break;
					}
					AAA102 = "";

					break;
				}
			}
			return AAA102;
		} catch (Exception ex) {
			throw new AppException(err, ex);
		}
	}

	public static String getDict(String code, String itemcode, boolean isNotEmpty) throws AppException {
		String err = "获取字典值出错，code=" + code + " ，itemcode=" + itemcode;
		try {
			CacheManager.setCacheType(Cache.CACHE_DICT);

			Map<String, Map<String, String>> dict = getDict(code);
			String AAA102 = "";
			for (Map.Entry<String, Map<String, String>> rowEntry : dict.entrySet()) {
				if (itemcode.equals(((Map) rowEntry.getValue()).get("ITEMCODE"))) {
					AAA102 = (String) ((Map) rowEntry.getValue()).get("AAA102");
					if (AAA102 != null) {
						break;
					}
					AAA102 = "";

					break;
				}
			}
			if ((isNotEmpty) && (Util.isEmpty(AAA102))) {
				throw new AppException("未设置字典code=" + code + " ，itemcode=" + itemcode + "值");
			}
			return AAA102;
		} catch (AppException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new AppException(err, ex);
		}
	}

	public static Map<String, Map<String, String>> getDictKeyItemCode(String code) throws AppException {
		String err = "获取字典值出错，code=" + code;
		try {
			CacheManager.setCacheType(Cache.CACHE_DICT);

			Map<String, Map<String, String>> dict = getDict(code);

			Map<String, Map<String, String>> reDic = new HashMap(dict.size());
			for (Map.Entry<String, Map<String, String>> rowEntry : dict.entrySet()) {
				reDic.put((String) ((Map) rowEntry.getValue()).get("ITEMCODE"), (Map) rowEntry.getValue());
			}
			return reDic;
		} catch (Exception ex) {
			throw new AppException(err, ex);
		}
	}

	public static String getDictZh(String code, String AAA102) throws AppException {
		String err = "获取字典项中文名称出错，code=" + code + " ，AAA102=" + AAA102;
		try {
			CacheManager.setCacheType(Cache.CACHE_DICT);
			Map<String, String> codeMap = CacheManager.getDictItem(code, AAA102);
			if (codeMap != null) {
				return codeMap.get("AAA103") == null ? "" : (String) codeMap.get("AAA103");
			}
			LOGGER.error(err);

			return "";
		} catch (Exception ex) {
			throw new AppException(err, ex);
		}
	}

	public static String getDictCodeByName(String code, String name) throws AppException {
		String err = "获取字典值出错，code=" + code;
		try {
			if (Util.isEmpty(name)) {
				return "";
			}
			CacheManager.setCacheType(Cache.CACHE_DICT);
			Map<String, Map<String, String>> codeMap = CacheManager.getDict(code);
			if (codeMap != null) {
				for (Map.Entry<String, Map<String, String>> entry : codeMap.entrySet()) {
					Map<String, String> value = (Map) entry.getValue();
					if (name.equals(value.get("AAA103"))) {
						return value.get("AAA102") == null ? "" : (String) value.get("AAA102");
					}
				}
			} else {
				LOGGER.error(err);
			}
			return "";
		} catch (Exception ex) {
			throw new AppException(err, ex);
		}
	}

	public static Map<String, Map<String, String>> getDict(String code) throws AppException {
		try {
			CacheManager.setCacheType(Cache.CACHE_DICT);
			return unmodifiableMap(CacheManager.getDict(code));
		} catch (Exception ex) {
			String err = "获取字典出错，code=" + code;

			throw new AppException(err, ex);
		}
	}

	public static Map<String, Map<String, String>> getDynDict(String code, String where)
    throws AppException
  {
    try
    {
      if ("".equals(Util.nvl(code))) {
        return null;
      }
      String sqlStr = getSqlConf(code);
      String codeStr = "AAA102";
      String nameStr = "AAA103";
      if (Util.isEmpty(sqlStr))
      {
        LOGGER.warn("未获取动态字典sql");
        return null;
      }
      StringBuffer sql = new StringBuffer(sqlStr);
      if (!"".equals(Util.nvl(where))) {
        sql.append(" where ").append(where);
      }
      String dynSql = sql.toString();
      LOGGER.info("getDynDict SQL:" + dynSql);
      JdbcTemplate jdbcTemplate = (JdbcTemplate)Util.getBean("jdbcTemplate");
      (Map)jdbcTemplate.query(dynSql, new ResultSetExtractor()
      {
        public Map<String, Map<String, String>> extractData(ResultSet rs)
          throws SQLException, DataAccessException
        {
          Map<String, Map<String, String>> resp = new HashMap();
          while (rs.next())
          {
            Map<String, String> item = new HashMap();
            item.put("AAA102", rs.getString(1));
            item.put("AAA103", rs.getString(2));
            resp.put(rs.getString(1), item);
            if ((resp != null) && (resp.size() >= Integer.parseInt(Environment.NO_ROW_BOUNDS)))
            {
              String msg = "getDynDict SQL:" + ConfUtil.this + "查询结果已达到" + Environment.NO_ROW_BOUNDS + "最大行数限制!";
              ConfUtil.LOGGER.warn(msg);
              break;
            }
          }
          return resp;
        }
      });
    }
    catch (Exception ex)
    {
      String err = "获取动态字典出错，key=" + code + ",where = " + where;
      
      throw new AppException(err, ex);
    }
  }

	public static List<Map<String, String>> getQuerySQLResult(String code, String where) throws AppException {
		try {
			if ("".equals(Util.nvl(code))) {
				return null;
			}
			DynDictDAO dynDictDAO = (DynDictDAO) Util.getBean("dynDictDAO");
			return dynDictDAO.getDynDict(code, where);
		} catch (Exception ex) {
			String err = "找指定SQL结果集出错，ID=" + code + " ，where = " + where;

			throw new AppException(err, ex);
		}
	}

	public static Map<String, Map<String, String>> getSubDict(String code, String subCode) throws AppException {
		try {
			CacheManager.setCacheType(Cache.CACHE_DICT);
			Map<String, Map<String, String>> codeMap = CacheManager.getDict(code);
			Map<String, Map<String, String>> subMap = new HashMap();
			for (Map.Entry<String, Map<String, String>> entry : codeMap.entrySet()) {
				if ((entry.getValue() != null) && (subCode.equals(((Map) entry.getValue()).get("SUBCODE")))) {
					subMap.put((String) entry.getKey(), unmodifiableMap((Map) entry.getValue()));
				}
			}
			return unmodifiableMap(subMap);
		} catch (Exception ex) {
			String err = "获取字典 " + code + " 的子字典 " + subCode + " 出错";

			throw new AppException(err, ex);
		}
	}

	public static String getParam(String key) throws AppException {
		try {
			CacheManager.setCacheType(Cache.CACHE_SYS);
			Map<String, String> params = (Map) CacheManager.getItem(key);
			if (params == null) {
				return "";
			}
			return (String) Util.nvl(params.get("AAA005"));
		} catch (Exception ex) {
			String err = "获取系统参数值出错,key=" + key;

			throw new AppException(err, ex);
		}
	}

	public static String getParam(String key, String defV) throws AppException {
		try {
			CacheManager.setCacheType(Cache.CACHE_SYS);
			Map<String, String> params = (Map) CacheManager.getItem(key);
			if (params == null) {
				return defV;
			}
			return (String) Util.nvl(params.get("AAA005"), defV);
		} catch (Exception ex) {
			String err = "获取系统参数值出错,key=" + key;

			throw new AppException(err, ex);
		}
	}

	public static Object getGenListConf(String key) throws AppException {
		try {
			CacheManager.setCacheType(Cache.CACHE_GENLIST);

			return CacheManager.getItem(key);
		} catch (Exception ex) {
			String err = "获取多记录表配置出错,key=" + key;

			throw new AppException(err, ex);
		}
	}

	public static void setGenListConf(String key, Object conf) throws AppException {
		CacheManager.setCacheType(Cache.CACHE_GENLIST);
		CacheItem<Object> cacheItem = new CacheItem();

		cacheItem.setItem(conf);
		try {
			CacheManager.setItem(key, cacheItem);
		} catch (Exception ex) {
			String err = "缓存多记录表配置出错,key=" + key;

			throw new AppException(err, ex);
		}
	}

	public static Object getGenTblConf(String key) throws AppException {
		try {
			CacheManager.setCacheType(Cache.CACHE_GENTBL);
			return CacheManager.getItem(key);
		} catch (Exception ex) {
			String err = "获取单记录表配置出错,key=" + key;

			throw new AppException(err, ex);
		}
	}

	public static void setGenTblConf(String key, Object conf) throws AppException {
		CacheManager.setCacheType(Cache.CACHE_GENTBL);
		CacheItem<Object> cacheItem = new CacheItem();

		cacheItem.setItem(conf);
		try {
			CacheManager.setItem(key, cacheItem);
		} catch (Exception ex) {
			String err = "缓存多记录表配置出错,key=" + key;

			throw new AppException(err, ex);
		}
	}

	public static Object getPrintConf(String key) throws AppException {
		try {
			CacheManager.setCacheType(Cache.CACHE_PRINT);
			return CacheManager.getItem(key);
		} catch (Exception ex) {
			String err = "获取打印配置信息出错,key=" + key;

			throw new AppException(err, ex);
		}
	}

	public static void setPrintConf(String key, Object conf) throws AppException {
		CacheManager.setCacheType(Cache.CACHE_PRINT);
		CacheItem<Object> cacheItem = new CacheItem();

		cacheItem.setItem(conf);
		try {
			CacheManager.setItem(key, cacheItem);
		} catch (Exception ex) {
			String err = "缓存打印配置信息出错,key=" + key;

			throw new AppException(err, ex);
		}
	}

	public static Map<String, String> getExcelConf(String key) throws AppException {
		try {
			CacheManager.setCacheType(Cache.CACHE_EXCEL);
			return unmodifiableMap((Map) CacheManager.getItem(key));
		} catch (Exception ex) {
			String err = "获取Excel配置信息出错,key=" + key;
			throw new AppException(err, ex);
		}
	}

	public static Map<String, CacheItem<Map<String, String>>> getRight() throws AppException {
		try {
			CacheManager.setCacheType(Cache.CACHE_RIGHT);
			return unmodifiableMap((Map) CacheManager.getAllItem());
		} catch (Exception ex) {
			String err = "获取权限配置出错";
			throw new AppException(err, ex);
		}
	}

	public static Map<String, Map<String, String>> getMenus() throws AppException {
		try {
			CacheManager.setCacheType(Cache.CACHE_RIGHT);
			Map<String, CacheItem<Map<String, String>>> right = (Map) CacheManager.getAllItem();
			if (right != null) {
				Map<String, Map<String, String>> menu = new HashMap();
				for (Map.Entry<String, CacheItem<Map<String, String>>> entry : right.entrySet()) {
					CacheItem<Map<String, String>> item = (CacheItem) entry.getValue();
					Map<String, String> reco = (Map) item.getItem();
					if ((!Environment.RightType.RESOURCE.getCode().equals(reco.get("RIGHTTYPE")))
							&& (!Environment.RightType.WORKFLOW.getCode().equals(reco.get("RIGHTTYPE")))) {
						menu.put(item.getSubCode(), (Map) item.getItem());
					}
				}
				return unmodifiableMap(menu);
			}
			return null;
		} catch (Exception ex) {
			String err = "获取菜单配置出错";
			throw new AppException(err, ex);
		}
	}

	public static Map<String, Map<String, String>> getNavMenus() throws AppException {
		try {
			CacheManager.setCacheType(Cache.CACHE_RIGHT);
			Map<String, CacheItem<Map<String, String>>> right = (Map) CacheManager.getAllItem();
			if (right != null) {
				Map<String, Map<String, String>> menu = new HashMap();
				for (Map.Entry<String, CacheItem<Map<String, String>>> entry : right.entrySet()) {
					CacheItem<Map<String, String>> item = (CacheItem) entry.getValue();
					Map<String, String> reco = (Map) item.getItem();
					if (!Environment.RightType.RESOURCE.getCode().equals(reco.get("RIGHTTYPE"))) {
						menu.put(item.getSubCode(), (Map) item.getItem());
					}
				}
				return unmodifiableMap(menu);
			}
			return null;
		} catch (Exception ex) {
			String err = "获取导航菜单配置出错";
			throw new AppException(err, ex);
		}
	}

	public static WfActionDef getWfConf(String key) throws AppException {
		try {
			CacheManager.setCacheType(Cache.CACHE_WORKFLOW);
			return (WfActionDef) CacheManager.getItem(key);
		} catch (Exception ex) {
			String err = "获取流程定义信息出错,key=" + key;
			throw new AppException(err, ex);
		}
	}

	public static WfActionDef2Task getWfTask(String key) throws AppException {
		try {
			CacheManager.setCacheType(Cache.CACHE_WORKFLOWTASK);
			return (WfActionDef2Task) CacheManager.getItem(key);
		} catch (Exception ex) {
			String err = "获取流程环节与业务处理关联信息出错,key=" + key;
			throw new AppException(err, ex);
		}
	}

	public static List<WfActionDef> getAllWfConf(String processDefId) throws AppException {
		CacheManager.setCacheType(Cache.CACHE_WORKFLOW);
		try {
			Map<String, CacheItem<WfActionDef>> temp = unmodifiableMap((Map) CacheManager.getAllItem());
			if (temp == null) {
				return null;
			}
			List<WfActionDef> temp2 = new ArrayList();
			for (Map.Entry<String, CacheItem<WfActionDef>> entry : temp.entrySet()) {
				CacheItem<WfActionDef> item = (CacheItem) entry.getValue();
				if (item.getCode().equalsIgnoreCase(processDefId)) {
					temp2.add((WfActionDef) item.getItem());
				}
			}
			temp = null;

			return temp2;
		} catch (Exception ex) {
			String err = "根据流程定义ID获取此流程环节出错,ID=" + processDefId;
			throw new AppException(err, ex);
		}
	}

	public static String getTipMsg(String key) throws Exception {
		try {
			CacheManager.setCacheType(Cache.CACHE_TIPMSG);
			Map<String, String> params = (Map) CacheManager.getItem(key);
			if (params == null) {
				return "";
			}
			return (String) Util.nvl(params.get("DESCRIPTION"));
		} catch (Exception ex) {
			String err = "获取提示信息值出错,key=" + key;
			throw new Exception(err, ex);
		}
	}

	public static String getSqlConf(String key) throws AppException {
		try {
			CacheManager.setCacheType(Cache.CACHE_SQL);
			Map<String, String> params = (Map) CacheManager.getItem(key);
			if (params == null) {
				return "";
			}
			return (String) Util.nvl(params.get("SELECTSTR"));
		} catch (Exception ex) {
			String err = "获取SQL配置信息出错,key=" + key;
			throw new AppException(err, ex);
		}
	}

	public static boolean isCacheSwitch(String cacheName) {
		if ("true".equalsIgnoreCase((String) Environment.getCacheSwitch().get(cacheName.toLowerCase()))) {
			return true;
		}
		return false;
	}

	public static String getSysParam(String key, String defVal) {
		String val = Environment.getProperties().getProperty(key);
		if (Util.isEmpty(val)) {
			val = getParam(key);
		}
		return Util.isEmpty(val) ? defVal : val;
	}

	public static String getSysParamOnly(String key, String defVal) {
		String val = Environment.getProperties().getProperty(key);
		return Util.isEmpty(val) ? defVal : val;
	}

	public static long getSysParam2Number(String key, long defVal) {
		try {
			String val = getSysParam(key, "");
			return Util.isEmpty(val) ? defVal : Long.valueOf(val).longValue();
		} catch (NumberFormatException ex) {
			String err = "获取sysconfig.properties文件中参数出错,key=" + key + " defVal=" + defVal;
			throw new AppException(err, ex);
		}
	}

	private static Map unmodifiableMap(Map arg) {
		return arg == null ? null : Collections.unmodifiableMap(arg);
	}
}
