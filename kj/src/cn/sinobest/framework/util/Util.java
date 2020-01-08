package cn.sinobest.framework.util;

import cn.sinobest.framework.comm.Environment;
import cn.sinobest.framework.comm.exception.AppException;
import cn.sinobest.framework.comm.iface.IDTO;
import cn.sinobest.framework.service.JdbcCallService;
import cn.sinobest.framework.service.json.JSONUtilities;
import cn.sinobest.framework.web.BaseActionSupport;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class Util extends StringUtils implements ApplicationContextAware {
	private static ApplicationContext applicationContext;
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseActionSupport.class);
	static final int[] SFZ_JIAQUANYINZI = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
	static final char[] SFZ_JIAOYANGMA = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
	static final Pattern DATE_PATTERN = Pattern.compile("\\d{8}?");
	static final SimpleDateFormat CSRQ_FORMAT = new SimpleDateFormat("yyyyMMdd");
	static final Pattern NY_PATTERN = Pattern.compile("\\d{4}?(?:(?:0[1-9])|(?:1[0,1,2]))");
	static final SimpleDateFormat NY_FORMAT = new SimpleDateFormat("yyyyMM");
	static final Map<Type, Object> defaultVal;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		applicationContext = applicationContext;
	}

	public static final String checkSfz(String sfz, String[] parts) throws AppException {
		if (sfz == null) {
			throw new AppException("身份证为空");
		}
		StringBuilder t_gmsfz = new StringBuilder(sfz.trim());
		if (t_gmsfz.length() == 0) {
			throw new AppException("身份证为空");
		}
		if ((t_gmsfz.length() != 15) && (t_gmsfz.length() != 18)) {
			throw new AppException("身份证'{0}'的长度不对，只能是15位或者18位", null, new Object[] { sfz });
		}
		if (t_gmsfz.length() == 15) {
			t_gmsfz.insert(6, "19");
		}
		int li_sum = 0;
		for (int i = 0; i < 17; i++) {
			li_sum = li_sum + Integer.parseInt(t_gmsfz.substring(i, i + 1))
		}
		int li_mod = li_sum % 11;

		char jiaoyangma = SFZ_JIAOYANGMA[li_mod];
		if (t_gmsfz.length() == 17) {
			t_gmsfz.append(jiaoyangma);
		} else if (t_gmsfz.charAt(17) != jiaoyangma) {
			throw new AppException("身份证'{0}'的校验码有误！", null, new Object[] { sfz });
		}
		String csrq = t_gmsfz.substring(6, 14);

		String xb = (Integer.parseInt(t_gmsfz.substring(16, 17)) & 0x1) == 0 ? "2" : "1";
		if (!checkDate(csrq)) {
			throw new AppException("身份证'{0}'中的出生日期不合法", null, new Object[] { sfz });
		}
		if (CSRQ_FORMAT.format(new Date()).compareTo(csrq) < 0) {
			throw new AppException("身份证'{0}'中的出生日期不合法", null, new Object[] { sfz });
		}
		if (parts != null) {
			switch (parts.length) {
			case 0:
				break;
			case 1:
				parts[0] = csrq;
				break;
			default:
				parts[0] = csrq;

				parts[1] = xb;
			}
		}
		return t_gmsfz.toString();
	}

	public static boolean checkDate(String date) {
		if ((date == null) || (date.length() == 0)) {
			return false;
		}
		if (!DATE_PATTERN.matcher(date).matches()) {
			return false;
		}
		try {
			CSRQ_FORMAT.parse(date);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	public static boolean checkNy(String ny) {
		if ((ny == null) || (ny.length() == 0)) {
			return false;
		}
		if (!NY_PATTERN.matcher(ny).matches()) {
			return false;
		}
		if ("0000".equals(ny.substring(0, 4))) {
			return false;
		}
		return true;
	}

	public static double add(Double v1, Double v2) {
		BigDecimal b1 = new BigDecimal(v1.toString());
		BigDecimal b2 = new BigDecimal(v2.toString());
		return b1.add(b2).doubleValue();
	}

	public static double add(double v1, double v2, int scale) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.add(b2).setScale(scale, 4).doubleValue();
	}

	public static double sub(Double v1, Double v2) {
		BigDecimal b1 = new BigDecimal(v1.toString());
		BigDecimal b2 = new BigDecimal(v2.toString());
		return b1.subtract(b2).doubleValue();
	}

	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.multiply(b2).doubleValue();
	}

	public static double mul(double v1, double v2, int scale) {
		return BigDecimal.valueOf(v1).multiply(BigDecimal.valueOf(v2)).setScale(scale, 4).doubleValue();
	}

	public static double div(double v1, double v2) {
		return div(v1, v2, 10);
	}

	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.divide(b2, scale, 4).doubleValue();
	}

	public static Object nvl(Object value) {
		return nvl(value, "");
	}

	public static Object nvl(Object value, String repStr) {
		if (value == null) {
			return "";
		}
		if (repStr == null) {
			repStr = "";
		}
		Pattern pt = Pattern.compile("^\\s+|\\s*null\\s*$", 2);
		if ((value instanceof String)) {
			Matcher mt = pt.matcher((String) value);
			if (mt.matches()) {
				return repStr;
			}
			return value;
		}
		if ((value instanceof Map)) {
			for (Map.Entry<String, Object> entry : ((Map) value).entrySet()) {
				if (entry.getKey() == null) {
					((Map) value).remove(entry.getKey());
				} else if ((entry.getValue() instanceof String)) {
					String val = (String) entry.getValue();
					if (val == null) {
						((Map) value).put(entry.getKey(), repStr);
					} else {
						Matcher mt = pt.matcher(val);
						if (mt.matches()) {
							((Map) value).put(entry.getKey(), repStr);
						}
					}
				} else if ((entry.getValue() instanceof Map)) {
					nvl(entry.getValue(), repStr);
				} else if (entry.getValue() == null) {
					((Map) value).put(entry.getKey(), repStr);
				}
			}
		}
		return value;
	}

	public static Map<String, Object> initParams(Map<String, Object> paramsMap, String... keys) {
		return initParams(paramsMap, "", keys);
	}

	public static <T> Map<String, T> initParams(Map<String, T> paramsMap, T val, String... keys) {
		if (paramsMap == null) {
			return null;
		}
		for (String key : keys) {
			if (!paramsMap.containsKey(key)) {
				paramsMap.put(key, val);
			} else if (paramsMap.get(key) == null) {
				paramsMap.put(key, val);
			}
		}
		return paramsMap;
	}

	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new AppException("四舍五入位数必须大于0");
		}
		BigDecimal one = new BigDecimal("1");
		return new BigDecimal(v).divide(one, scale, 4).doubleValue();
	}

	public static void prepareDTO(IDTO dto, String... fields) {
		Map<String, Object> data = dto.getData();
		for (String field : fields) {
			if (data.get(field) == null) {
				data.put(field, "");
			}
		}
		nvl(data);
	}

	public static boolean isEmpty(String str) {
		return (str == null) || ("".equals(str.trim()));
	}

	public static Object getBean(String id) {
		try {
			return WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext())
					.getBean(id);
		} catch (NullPointerException e) {
			try {
				return applicationContext.getBean(id);
			} catch (NullPointerException ex) {
				if (Environment.getApplicationContext() != null) {
					tmpTernaryOp = Environment.getApplicationContext().getBean(id);
				}
			}
		}
		return null;
	}

	public static <T> T getBean(String beanName, Class<T> requiredType) {
		return applicationContext.getBean(beanName, requiredType);
	}

	public static Object getBean(ServletContext servletContext, String id) {
		try {
			return WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext).getBean(id);
		} catch (NullPointerException e) {
			try {
				return applicationContext.getBean(id);
			} catch (NullPointerException ex) {
				if (Environment.getApplicationContext() != null) {
					tmpTernaryOp = Environment.getApplicationContext().getBean(id);
				}
			}
		}
		return null;
	}

	public static Map mapClone(Map source) {
		Map target = new HashMap();
		for (Iterator itor = source.keySet().iterator(); itor.hasNext();) {
			Object key = itor.next();
			target.put(key, source.get(key));
		}
		return target;
	}

	public static <T> T objectClone(T oObj) {
		ByteArrayOutputStream bos = null;
		ByteArrayInputStream bis = null;
		ObjectOutputStream objo = null;
		ObjectInputStream obji = null;
		try {
			bos = new ByteArrayOutputStream();
			objo = new ObjectOutputStream(bos);
			objo.writeObject(oObj);
			bis = new ByteArrayInputStream(bos.toByteArray());
			obji = new ObjectInputStream(bis);
			return obji.readObject();
		} catch (IOException e) {
			throw new AppException(e.getLocalizedMessage(), e);
		} catch (ClassNotFoundException e) {
			throw new AppException("类型未找到!", e);
		} finally {
			bos = null;
			bis = null;
			if (objo != null) {
				try {
					objo.close();
				} catch (IOException localIOException3) {
				}
			}
			if (obji != null) {
				try {
					obji.close();
				} catch (IOException localIOException4) {
				}
			}
		}
	}

	public static String replaceSlant(String str) {
		if (isEmpty(str)) {
			return "";
		}
		return str.replaceAll("\\\\", "\\/");
	}

	public static boolean isMatches(String inputStr, String regex) {
		Pattern pt = Pattern.compile(regex, 2);
		Matcher mt = pt.matcher(inputStr);
		if (mt.matches()) {
			return true;
		}
		return false;
	}

	public static String searchParam(String attr, String attrs) {
		if (isEmpty(attrs)) {
			return "";
		}
		String[] attrStr = attrs.split("\\|");
		for (int i = 0; i < attrStr.length; i++) {
			String[] keyValue = attrStr[i].split("=");
			if (attr.equalsIgnoreCase(keyValue[0])) {
				return keyValue[1];
			}
		}
		return "";
	}

	public static Map<String, String> strParam2Map(String strParam, String split, String charset) {
		if (isEmpty(strParam)) {
			return null;
		}
		String[] params = strParam.split(split);
		Map<String, String> paramsMap = new HashMap();
		for (int i = 0; i < params.length; i++) {
			String[] keyValue = params[i].split("=");
			try {
				paramsMap.put(keyValue[0], isEmpty(charset) ? keyValue[1] : URLEncoder.encode(keyValue[1], charset));
			} catch (UnsupportedEncodingException e) {
				LOGGER.warn("字符集设置错误!", e);
				paramsMap.put(keyValue[0], keyValue[1]);
			}
		}
		return paramsMap;
	}

	public static Object doMethod(String objectName, String methodName, IDTO dto) throws AppException {
		Object service = getBean(objectName);
		Class<? extends Object> cls = service.getClass();
		LOGGER.debug("执行" + objectName + "." + methodName + "( " + dto.getClass().getSimpleName() + " )");
		try {
			Method method = cls.getMethod(methodName, new Class[] { IDTO.class });
			return method.invoke(service, new Object[] { dto });
		} catch (SecurityException e) {
			String err = cls.getName() + "类中方法名为" + methodName + "的方法不允许访问!";
			throw new AppException(err, e);
		} catch (IllegalArgumentException e) {
			throw new AppException("非法参数!", e);
		} catch (InvocationTargetException e) {
			throw new AppException(e.getTargetException().getLocalizedMessage(), e.getTargetException());
		} catch (IllegalAccessException e) {
			throw new AppException("非法访问!", e);
		} catch (NoSuchMethodException e) {
			String err = cls.getName() + "类中未找到方法名为" + methodName + "的方法!";
			throw new AppException(err, e);
		}
	}

	public static Object doMethod(String objectName, String methodName, Object... args) throws AppException {
		Object service = getBean(objectName);
		Class<? extends Object> cls = service.getClass();
		try {
			Class[] argsTypeObj = new Class[args.length];

			int i = 0;
			for (Object o : args) {
				argsTypeObj[(i++)] = o.getClass();
			}
			Method method = cls.getMethod(methodName, argsTypeObj);
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("执行");
			stringBuilder.append(objectName);
			stringBuilder.append(".");
			stringBuilder.append(methodName);
			stringBuilder.append("( ");
			stringBuilder.append(join(",", method.getParameterTypes()));
			stringBuilder.append(" )");
			LOGGER.debug(stringBuilder.toString());
			return method.invoke(service, args);
		} catch (SecurityException e) {
			String err = cls.getName() + "类中方法名为" + methodName + "的方法不允许访问!";
			throw new AppException(err, e);
		} catch (IllegalArgumentException e) {
			throw new AppException("非法参数!", e);
		} catch (InvocationTargetException e) {
			throw new AppException(e.getTargetException().getLocalizedMessage(), e.getTargetException());
		} catch (IllegalAccessException e) {
			throw new AppException("非法访问!", e);
		} catch (NoSuchMethodException e) {
			String err = cls.getName() + "类中未找到方法名为" + methodName + "的方法!";
			throw new AppException(err, e);
		}
	}

	public static Map<String, Object> doProcedure(String procedureName, Map<String, ?> values, String dataSource)
			throws Exception {
		JdbcCallService service = (JdbcCallService) getBean("jdbcCallService");
		return service.doProcedure(procedureName, values, dataSource);
	}

	public static String doFunction(String functionName, Map<String, ?> values, String dataSource) throws Exception {
		JdbcCallService service = (JdbcCallService) getBean("jdbcCallService");
		return (String) service.doFunction(functionName, values, dataSource);
	}

	public static <T> T[] toTypedArray(Object source, Class<T> newType) {
		if (source == null) {
			return null;
		}
		Object newArray = Array.newInstance(newType, Array.getLength(source));
		System.arraycopy(source, 0, newArray, 0, Array.getLength(source));
//		return (Object[]) newArray;
		return (T[]) newArray;
	}

	public static String join(String spliter, Object... arrays) {
		if (arrays.length == 0) {
			return null;
		}
		String t_spliter;
		String t_spliter;
		if (spliter == null) {
			t_spliter = ",";
		} else {
			t_spliter = spliter;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < arrays.length; i++) {
			if (arrays[i] != null) {
				Object value;
				if (arrays[i].getClass().isArray()) {
					Object subArray = arrays[i];
					int subArrayLength = Array.getLength(subArray);
					for (int j = 0; j < subArrayLength; j++) {
						value = Array.get(subArray, j);
						if (value != null) {
							sb.append(value);
						}
						if (j < subArrayLength - 1) {
							sb.append(t_spliter);
						}
					}
				} else if ((arrays[i] instanceof Collection)) {
					int subSize = ((Collection) arrays[i]).size();
					int subIndex = 0;
					for (Object subOne : (Collection) arrays[i]) {
						if (subOne != null) {
							sb.append(subOne);
						}
						if (subIndex < subSize - 1) {
							sb.append(t_spliter);
						}
						subIndex++;
					}
				} else if (arrays[i] != null) {
					sb.append(arrays[i]);
				}
			}
			if (i < arrays.length - 1) {
				sb.append(t_spliter);
			}
		}
		return sb.toString();
	}

	static {
		CSRQ_FORMAT.setLenient(false);

		defaultVal = new HashMap();

		defaultVal.put(Byte.TYPE, Integer.valueOf(0));
		defaultVal.put(Short.TYPE, Integer.valueOf(0));
		defaultVal.put(Integer.TYPE, Integer.valueOf(0));
		defaultVal.put(Long.TYPE, Long.valueOf(0L));
		defaultVal.put(Float.TYPE, Float.valueOf(0.0F));
		defaultVal.put(Double.TYPE, Double.valueOf(0.0D));
		defaultVal.put(Character.TYPE, Character.valueOf('\000'));
		defaultVal.put(Boolean.TYPE, Boolean.valueOf(false));
	}

	public static Map<String, Object> beanToMap(Object bean) {
		String classProp = "class";
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());

			PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
			Map<String, Object> mapBean = new HashMap();
			for (PropertyDescriptor prop : props) {
				if (!"class".equals(prop.getName())) {
					if (prop.getReadMethod() != null) {
						Object value = prop.getReadMethod().invoke(bean, new Object[0]);
						mapBean.put(prop.getName().toUpperCase(), value);
					}
				}
			}
			return mapBean;
		} catch (InvocationTargetException e) {
			throw ((AppException) e.getCause());
		} catch (IntrospectionException e) {
			throw new AppException("Bean和Map之间的转化失败", e);
		} catch (IllegalArgumentException e) {
			throw new AppException("Bean和Map之间的转化失败", e);
		} catch (IllegalAccessException e) {
			throw new AppException("Bean和Map之间的转化失败", e);
		}
	}

	public static Object mapToBean(Map<String, Object> map, Object bean) {
		String classProp = "class";
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());

			PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor prop : props) {
				if (!"class".equals(prop.getName())) {
					if (prop.getWriteMethod() != null) {
						Object value = map.get(prop.getName().toUpperCase());
						Class<?> clazz = prop.getPropertyType();
						if (value == null) {
							if (clazz.isPrimitive()) {
								prop.getWriteMethod().invoke(bean, new Object[] { defaultVal.get(clazz) });
							} else {
								prop.getWriteMethod().invoke(bean, new Object[] { null });
							}
						} else if (clazz.isAssignableFrom(value.getClass())) {
							prop.getWriteMethod().invoke(bean, new Object[] { value });
						} else if (((Double.TYPE == clazz) || (Double.class == clazz))
								&& (Number.class.isAssignableFrom(value.getClass()))) {
							prop.getWriteMethod().invoke(bean,
									new Object[] { Double.valueOf(((Number) value).doubleValue()) });
						} else if (((Integer.TYPE == clazz) || (Integer.class == clazz))
								&& (Number.class.isAssignableFrom(value.getClass()))) {
							prop.getWriteMethod().invoke(bean,
									new Object[] { Integer.valueOf(((Number) value).intValue()) });
						} else if (((Long.TYPE == clazz) || (Long.class == clazz))
								&& (Number.class.isAssignableFrom(value.getClass()))) {
							prop.getWriteMethod().invoke(bean,
									new Object[] { Long.valueOf(((Number) value).longValue()) });
						} else if (((Byte.TYPE == clazz) || (Byte.class == clazz))
								&& (Number.class.isAssignableFrom(value.getClass()))) {
							prop.getWriteMethod().invoke(bean,
									new Object[] { Byte.valueOf(((Number) value).byteValue()) });
						} else if (((Short.TYPE == clazz) || (Short.class == clazz))
								&& (Number.class.isAssignableFrom(value.getClass()))) {
							prop.getWriteMethod().invoke(bean,
									new Object[] { Short.valueOf(((Number) value).shortValue()) });
						} else if (((Float.TYPE == clazz) || (Float.class == clazz))
								&& (Number.class.isAssignableFrom(value.getClass()))) {
							prop.getWriteMethod().invoke(bean,
									new Object[] { Float.valueOf(((Number) value).floatValue()) });
						} else {
							prop.getWriteMethod().invoke(bean, new Object[] { value });
						}
					}
				}
			}
			return bean;
		} catch (InvocationTargetException e) {
			throw ((AppException) e.getCause());
		} catch (IntrospectionException e) {
			throw new AppException("Bean和Map之间的转化失败", e);
		} catch (IllegalArgumentException e) {
			throw new AppException("Bean和Map之间的转化失败", e);
		} catch (IllegalAccessException e) {
			throw new AppException("Bean和Map之间的转化失败", e);
		}
	}

	public static Connection getConnection() throws AppException {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) getBean("jdbcTemplate", JdbcTemplate.class);
		try {
			Connection conn = jdbcTemplate.getDataSource().getConnection();
			conn.setAutoCommit(false);
			return conn;
		} catch (SQLException e) {
			throw new AppException("获取数据库连接失败,详细:" + e.getLocalizedMessage(), e);
		}
	}

	public static void closeConnection(Connection conn, Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				LOGGER.warn(e.getLocalizedMessage(), e);
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				LOGGER.warn(e.getLocalizedMessage(), e);
			}
		}
	}

	public static String lpad(String str, int width, String padStr, boolean flag) {
		int strLength = str.length();
		if (strLength == width) {
			return str;
		}
		if ((strLength > width) && (flag)) {
			return str.substring(0, width);
		}
		StringBuffer newStr = new StringBuffer(width);

		long length = strLength;
		for (int i = 0; i < width - length; i++) {
			newStr.append(padStr);
		}
		newStr.append(str);
		return newStr.toString();
	}

	public static String rpad(String str, int width, String padStr, boolean flag) {
		int strLength = str.length();
		if (strLength == width) {
			return str;
		}
		if ((strLength > width) && (flag)) {
			return str.substring(strLength - width, strLength);
		}
		StringBuffer newStr = new StringBuffer(width);
		newStr.append(str);
		long length = strLength;
		for (int i = 0; i < width - length; i++) {
			newStr.append(padStr);
		}
		return newStr.toString();
	}

	public static int length(String value) {
		if ((value == null) || ("".equals(value))) {
			return 0;
		}
		if (value.length() == value.getBytes().length) {
			return value.length();
		}
		int valueLength = 0;
		String chinese = "[Α-￥]";

		int l = value.length();
		for (int i = 0; i < l; i++) {
			String temp = value.substring(i, i + 1);
			valueLength += (temp.matches(chinese) ? 2 : 1);
		}
		return valueLength;
	}

	public static String toJson(Object obj) throws AppException {
		StringBuffer jsonBf = new JSONUtilities(1).parseObject(obj);
		return jsonBf != null ? jsonBf.toString() : null;
	}

	public static Object json2Obj(String json) throws Exception {
		new JSONUtilities(1);
		Object jsonBf = JSONUtilities.parseJSON(json);
		return jsonBf;
	}

	public static String number2String(Number n) {
		return number2String(n, null);
	}

	public static String number2String(Number n, String fmt) {
		DecimalFormat df = new DecimalFormat();
		String format = fmt;
		if (isEmpty(fmt)) {
			format = "0.##";
		}
		df.applyPattern(format);
		return df.format(n);
	}

	public static String searchByKey(String key, String attrs) {
		if (isEmpty(attrs)) {
			return "";
		}
		String[] attrStr = attrs.split("\\|");
		for (int i = 0; i < attrStr.length; i++) {
			String[] keyValue = attrStr[i].split("=");
			if (key.equalsIgnoreCase(keyValue[0])) {
				return keyValue[1];
			}
		}
		return "";
	}

	public static String getMapLogString(Map<String, ?> parameters) {
		if (parameters == null) {
			return "NONE";
		}
		StringBuilder logEntry = new StringBuilder();
		for (Map.Entry entry : parameters.entrySet()) {
			logEntry.append(String.valueOf(entry.getKey()));
			logEntry.append(" => ");
			if ((entry.getValue() instanceof Object[])) {
				Object[] valueArray = (Object[]) entry.getValue();
				logEntry.append("(Object[]");
				logEntry.append("[ ");
				if (valueArray.length > 0) {
					for (int indexA = 0; indexA < valueArray.length - 1; indexA++) {
						Object valueAtIndex = valueArray[indexA];
						logEntry.append(String.valueOf(valueAtIndex));
						logEntry.append(", ");
					}
					logEntry.append(String.valueOf(valueArray[(valueArray.length - 1)]));
				}
				logEntry.append(" ] ");
			} else {
				logEntry.append(String.valueOf(entry.getValue()));
			}
		}
		return logEntry.toString();
	}

	public static String subStringByte(String str, int n) {
		int m = 0;
		String chr = "";
		String PATTERN = "^([一-龥]*)$";
		if (n <= 0) {
			return "";
		}
		for (int i = 0; i < str.length(); i++) {
			if (m < n) {
				chr = str.substring(i, i + 1);
				if (chr.matches("^([一-龥]*)$")) {
					m += 2;
				} else {
					m++;
				}
			} else {
				return chr.matches("^([一-龥]*)$") ? str.substring(0, i)
						: m - n >= 1 ? str.substring(0, i - 1) : str.substring(0, i);
			}
		}
		return "";
	}

	public static String exception2String(Throwable e, int depth) {
		StringBuffer ex = new StringBuffer();
		int i = 1;
		if (e != null) {
			ex.append(e.getLocalizedMessage());
			StackTraceElement[] st = e.getStackTrace();
			for (StackTraceElement ste : st) {
				i++;
				ex.append(ste.toString());
				if ((depth != -1) && (i > depth)) {
					break;
				}
			}
		}
		return ex.toString();
	}

	public static void validateQueryStringSafty(String queryString) throws UnsupportedEncodingException {
		if ((queryString == null) || (queryString.length() == 0)) {
			return;
		}
		queryString = URLDecoder.decode(queryString, "utf-8");
		String unsafeWord = ConfUtil.getParam("UNSAFE_QUERYSTR", "");
		String[] words = unsafeWord.split("\\|");
		for (String word : words) {
			if (-1 != queryString.indexOf(word)) {
				throw new AppException("请求包含不安全的字符串: " + word + "\nat: " + queryString);
			}
		}
	}

	public static void validateParamsDataSafty(Map<String, Object> paramsData) {
		if ((paramsData == null) || (paramsData.size() == 0)) {
			return;
		}
		StringBuilder s = new StringBuilder();
		for (Map.Entry<String, Object> entry : paramsData.entrySet()) {
			s.append(entry.getValue().toString()).append("_");
		}
		String str = s.toString();
		String unsafeWord = ConfUtil.getParam("UNSAFE_PARAMSDATA", "");
		String[] words = unsafeWord.split("\\|");
		for (String word : words) {
			if (-1 != str.indexOf(word)) {
				throw new AppException("请求包含不安全的字符串: " + word + "\nat: " + str);
			}
		}
	}

	public static void validateSQLParam(String param) {
		if (isEmpty(param)) {
			return;
		}
		String unsafeWord = ConfUtil.getParam("UNSAFE_SQL", "");
		String[] words = unsafeWord.split("\\|");
		for (String word : words) {
			if (-1 != param.indexOf(word)) {
				throw new AppException("请求包含不安全的字符串: " + word + "\nat: " + param);
			}
		}
	}
}
