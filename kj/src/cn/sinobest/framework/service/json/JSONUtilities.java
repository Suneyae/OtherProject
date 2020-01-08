package cn.sinobest.framework.service.json;

import cn.sinobest.framework.comm.exception.AppException;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleDeserializers;

public class JSONUtilities {
	public static final int BEAUTY_LOOK = 1;
	public static final int NONE = 0;
	public static final StringBuffer NULL = new StringBuffer("null");
	public static final char SEP = ',';
	public static final char ATTPAIR_SEP = ':';
	static ObjectMapper jsonParserMapper = null;
	Stack callStack;
	int beautiStyle;

	static {
		jsonParserMapper = new ObjectMapper();
		jsonParserMapper.defaultPrettyPrintingWriter();
		SimpleDeserializers sd = new SimpleDeserializers();
		sd.addDeserializer(Object.class, new CommDeserializer());
		jsonParserMapper.getDeserializerProvider().withAdditionalDeserializers(sd);
	}

	public JSONUtilities() {
		this.callStack = new Stack();
		this.beautiStyle = 0;
	}

	public JSONUtilities(int style) {
		this.callStack = new Stack();
		this.beautiStyle = style;
	}

	public static boolean isBoolean(Object obj) {
		if (((obj instanceof Boolean)) || ((obj != null) && (obj.getClass() == Boolean.TYPE))) {
			return true;
		}
		return false;
	}

	public static boolean isArray(Object obj) {
		if (((obj != null) && (obj.getClass().isArray())) || ((obj instanceof Collection))) {
			return true;
		}
		return false;
	}

	public static boolean isString(Object obj) {
		if (((obj instanceof String)) || ((obj instanceof Character)) || ((obj != null)
				&& ((obj.getClass() == Character.TYPE) || (String.class.isAssignableFrom(obj.getClass()))))) {
			return true;
		}
		return false;
	}

	public static StringBuffer quote(String string) {
		if ((string == null) || (string.length() == 0)) {
			return new StringBuffer("\"\"");
		}
		char c = '\000';

		int len = string.length();
		StringBuffer sb = new StringBuffer(len + 4);

		sb.append('"');
		for (int i = 0; i < len; i++) {
			char b = c;
			c = string.charAt(i);
			switch (c) {
			case '"':
			case '\\':
				sb.append('\\');
				sb.append(c);
				break;
			case '/':
				if (b == '<') {
					sb.append('\\');
				}
				sb.append(c);
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\r':
				sb.append("\\r");
				break;
			default:
				if (c < ' ') {
					String t = "000" + Integer.toHexString(c);
					sb.append("\\u").append(t.substring(t.length() - 4));
				} else {
					sb.append(c);
				}
				break;
			}
		}
		sb.append('"');
		return sb;
	}

	public StringBuffer parseMap(Map object) {
		Map map = object;
		if (object == null) {
			return NULL;
		}
		if (map.size() == 0) {
			return new StringBuffer("{}");
		}
		StringBuffer ret = new StringBuffer().append('{');
		Set set = map.entrySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			ret.append(quote((String) entry.getKey()));
			ret.append(':');
			ret.append(parseObject(entry.getValue()));
			if (!it.hasNext()) {
				break;
			}
			ret.append(',');
		}
		ret.append('}');
		return ret;
	}

	public StringBuffer parseArray(Object array) {
		if (array == null) {
			return NULL;
		}
		StringBuffer ret = new StringBuffer().append('[');
		if (array.getClass().isArray()) {
			Object[] arr = (Object[]) array;
			if (arr.length == 0) {
				ret.append("[]");
			} else {
				for (int i = 0; i < arr.length - 1; i++) {
					ret.append(parseObject(arr[i]));
					ret.append(',');
				}
				ret.append(parseObject(arr[(arr.length - 1)]));
			}
		} else if ((array instanceof Collection)) {
			Collection coll = (Collection) array;
			if (coll.size() != 0) {
				Iterator it = coll.iterator();
				while (it.hasNext()) {
					ret.append(parseObject(it.next()));
					if (!it.hasNext()) {
						break;
					}
					ret.append(',');
				}
			}
		}
		ret.append(']');
		return ret;
	}

	public StringBuffer parseObject(Object object) throws AppException {
		if (object == null) {
			return NULL;
		}
		StringBuffer ret = new StringBuffer();
		if ((object instanceof Boolean)) {
			return ret.append(object);
		}
		if ((object instanceof String)) {
			return quote(object.toString());
		}
		if ((object instanceof Number)) {
			return ret.append(numberToString((Number) object));
		}
		if (this.callStack.contains(object)) {
			StringBuffer sb = new StringBuffer("JSON对象循环引用").append(object.getClass().getName()).append('\n');
			throw new AppException(sb.toString());
		}
		int depth = this.callStack.size();
		if ((this.beautiStyle == 1) && (depth > 0)) {
			ret.append('\n');
			for (int d = 0; d < depth; d++) {
				ret.append('\t');
			}
		}
		try {
			this.callStack.push(object);
			if (isArray(object)) {
				ret.append(parseArray(object));
			} else if ((object instanceof Map)) {
				ret.append(parseMap((Map) object));
			} else {
				ret.append(new StringBuffer(jsonParserMapper.writeValueAsString(object)));
			}
			return ret;
		} catch (AppException e) {
			throw e;
		} catch (Exception e) {
			throw new AppException("json反序列化出错", e);
		} finally {
			this.callStack.pop();
		}
	}

	public static Object parseJSON(String json) throws JsonParseException, JsonMappingException, IOException {
		return jsonParserMapper.readValue(json, Object.class);
	}

	private String numberToString(Number number) {
		testValidity(number);
		String s = number.toString();
		if ((s.indexOf('.') > 0) && (s.indexOf('e') < 0) && (s.indexOf('E') < 0)) {
			while (s.endsWith("0")) {
				s = s.substring(0, s.length() - 1);
			}
			if (s.endsWith(".")) {
				s = s.substring(0, s.length() - 1);
			}
		}
		return s;
	}

	private void testValidity(Number number) {
		if (number != null) {
			if ((number instanceof Double)) {
				if ((((Double) number).isInfinite()) || (((Double) number).isNaN())) {
					throw new AppException("JSON does not allow non-finite numbers");
				}
			} else if ((number instanceof Float)) {
				if ((((Float) number).isInfinite()) || (((Float) number).isNaN())) {
					throw new AppException("JSON does not allow non-finite numbers.");
				}
			} else if (((number instanceof BigDecimal)) || ((number instanceof BigInteger))) {
			}
		}
	}

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		String json = "[1,[]]";
		new JSONUtilities(1);
		System.out.println(parseJSON(json));
	}
}
