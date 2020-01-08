package com.test.javaAPI.wechat;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.io.SegmentedStringWriter;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

/**
 * json工具类,用于对json相关的一些操作,
 * 
 * @author Wei
 * @time 2016年10月2日 下午4:25:25
 */
public class UtilJackson {
	public static String jsonStr = "{\"parameters\":{\"PI_CAE574\":\"20160908\",\"PI_JSRQ\":\"20160908\",\"Pi_CAE920\":\"301\",\"Pi_YAE601\":\"1\",\"PI_BAE001\":\"511502\",\"Pi_JFDX\":\"1\",\"\":\"pkg_weiyl.getMatchResult\"},\"serviceId\":\"directJdbcService\",\"method\":\"savePointProcedure\"}";
	/**
	 * 比较复杂的json格式字符串
	 */
	public static String jsonStr_HN = "{\"DataPackage\":{\"JZZL\":[{\"CY\\\"BZ\":\"2\",\"CYQK\":\"\",\"CYRQ\":\"20090205\",\"CYZD\":\"霍乱\",\"CYZD1\":\"\",\"CYZDGJDM\":\"A00.901\",\"JZJLH\":\"136\",\"JZLB\":\"21\",\"RYRQ\":\"20090205\",\"RYZD\":\"霍乱\",\"RYZD1\":\"\",\"RYZDGJDM\":\"A00.901\",\"RYZS\":\"\",\"SSMC\":\"\",\"YYBH\":\"1000\",\"ZYH\":\"\",\"ZYJSLB\":\"1\"},{\"CYBZ\":\"2\",\"CYQK\":\"\",\"CYRQ\":\"20090202\",\"CYZD\":\"霍乱\",\"CYZD1\":\"\",\"CYZDGJDM\":\"A00.901\",\"JZJLH\":\"142\",\"JZLB\":\"21\",\"RYRQ\":\"20090201\",\"RYZD\":\"霍乱\",\"RYZD1\":\"\",\"RYZDGJDM\":\"A00.901\",\"RYZS\":\"\",\"SSMC\":\"\",\"YYBH\":\"1000\",\"ZYH\":\"\",\"ZYJSLB\":\"1\"},{\"CYBZ\":\"2\",\"CYQK\":\"\",\"CYRQ\":\"20090304\",\"CYZD\":\"霍乱\",\"CYZD1\":\"\",\"CYZDGJDM\":\"A00.901\",\"JZJLH\":\"132\",\"JZLB\":\"21\",\"RYRQ\":\"20090303\",\"RYZD\":\"霍乱\",\"RYZD1\":\"\",\"RYZDGJDM\":\"A00.901\",\"RYZS\":\"\",\"SSMC\":\"\",\"YYBH\":\"1000\",\"ZYH\":\"\",\"ZYJSLB\":\"1\"},{\"CYBZ\":\"2\",\"CYQK\":\"\",\"CYRQ\":\"20090306\",\"CYZD\":\"nihao\",\"CYZD1\":\"\",\"CYZDGJDM\":\"\",\"JZJLH\":\"140\",\"JZLB\":\"21\",\"RYRQ\":\"20090305\",\"RYZD\":\"nihao\",\"RYZD1\":\"\",\"RYZDGJDM\":\"\",\"RYZS\":\"\",\"SSMC\":\"\",\"YYBH\":\"1000\",\"ZYH\":\"\",\"ZYJSLB\":\"1\"}]},\"FunctionParams\":{\"FHZ\":\"1\",\"GET_PAGE_TOTAL\":\"4\",\"HAS_NEXT_REQUEST\":\"false\",\"MSG\":\"执行成功！\",\"SESSIONID\":\"QKSZPNrRPptTGs3ymqvJhQLZyxKJpd4XCHGJQBGFQcFQtwRYGvxS!306292812!1338878737991\"}}";
	public static String jsonStr_KEY_DataPackage = "DataPackage";
	public static String jsonStr_KEY_FunctionParams = "FunctionParams";
	public static String jsonStr4 = "{\"verified\":false,\"name\":{\"last\":\"Hankcs\",\"first\":\"Joe\"},\"userImage\":\"Rm9vYmFyIQ==\",\"gender\":\"MALE\"}";
	public static String jsonStr4_KEY1 = "verified";

	public static ObjectMapper objMap;

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		// ObjectMapper objMap = new ObjectMapper();
		// // Map map = objMap.readValue(UtilJackson.jsonStr, Map.class);
		// Map map = objMap.readValue(UtilJackson.jsonStr_HN, Map.class);
		// System.out.println(map.toString());
		// Set set = map.keySet();
		// Iterator it = set.iterator();
		// while (it.hasNext()) {
		// Object key = (Object) it.next();
		// Object value = map.get(key);
		// System.out.println("key:" + key + ",value:" + value);
		// }
//		System.out.println("jsonStr_HN:"+jsonStr_HN);
		// new JacksonTest().removeDataPackage("DataPackage");
		System.out.println("UtilJackson.jsonStr4:"+UtilJackson.jsonStr4);
		String str = removeJsonObjByKey(UtilJackson.jsonStr4, UtilJackson.jsonStr4_KEY1);

		System.out.println(str);

		Map map = new HashMap<>();
		map.put("xx", "abc");
		map.put("xiao", "大大的");
		String json = mapToJson(map);
		System.out.println(json);
	}

	/**
	 * 根据键除去json格式字符串中的某一个键值对
	 * 
	 * @param jsonStr
	 * @param key
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public static String removeJsonObjByKey(String jsonStr, String key)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objMap = getObjectMapper();
		// 1 把json格式字符串转换为 java.util.Map
		Map map = objMap.readValue(jsonStr, Map.class);
		// 2 删除map中的对应key的项目
		map.remove(key);
		// 准备字节流，接收ObjectMapper中写出的输出流
		ByteOutputStream bops = new ByteOutputStream();
		// 3 把map重新转换为json格式字符串
		objMap.writeValue(bops, map);
		if (!"".equals(bops)) {
			return bops.toString();
		}
		return "";
	}

	/**
	 * 方法的作用：去除一个json格式字符串的某一个key 删除 这个json字符串里的这个key对应的对象 该方法与框架中的 String
	 * cn.sinobest.framework.web.his.JsonManager.removeDataPackage(String
	 * jsonStr) 这个方法的功能一致
	 * 
	 * @param jsonKey
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Test
	public static String removeDataPackage(String jsonKey)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objMap = getObjectMapper();
		Map map = objMap.readValue(UtilJackson.jsonStr_HN, Map.class);
		// map.remove("DataPackage");
		map.remove(jsonKey);
		ByteOutputStream bops = new ByteOutputStream();
		objMap.writeValue(bops, map);
		System.out.println(bops.toString());
		return null;
	}

	/**
	 * map转换为json格式字符串
	 * 
	 * @param map
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	@Test
	public static String mapToJson(Map<Object, Object> map)
			throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper objMap = getObjectMapper();
		return objMap.writeValueAsString(map);
	}
	
	@Test
	public static String mapToJsonstr(Map<String, String> map)
			throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper objMap = getObjectMapper();
		return objMap.writeValueAsString(map);
	}
	
	/**
	 * 把list转换为json格式字符串
	 * @param list
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String ListToJson(List list)
			throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper objMap = getObjectMapper();
		return objMap.writeValueAsString(list);
	}
	
	/**
	 * 一下这段代码是源码
	 */
	/*public String writeValueAsString(Object value) throws IOException, JsonGenerationException, JsonMappingException {
		// alas, we have to pull the recycler directly here...
		SegmentedStringWriter sw = new SegmentedStringWriter(_jsonFactory._getBufferRecycler());
		_configAndWriteValue(_jsonFactory.createJsonGenerator(sw), value);
		return sw.getAndClear();
	}*/

	/**
	 * 传入map或者list对象，转换为字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String getJsonString(Object obj) {
		ObjectMapper om = getObjectMapper();
		StringWriter sw = new StringWriter();
		try {
			JsonGenerator jg = new JsonFactory().createJsonGenerator(sw);
			om.writeValue(jg, obj);
			jg.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sw.toString();
	}

	/**
	 * 获取ObjectMapper
	 * 
	 * @return
	 */
	public static ObjectMapper getObjectMapper() {
		if (objMap == null) {
			objMap = new ObjectMapper();
		}
		return objMap;
	}

}
