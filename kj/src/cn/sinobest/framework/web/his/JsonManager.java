 package cn.sinobest.framework.web.his;
 
 import java.io.PrintStream;
 import java.util.ArrayList;
 import java.util.Iterator;
 import java.util.LinkedHashMap;
 import java.util.LinkedList;
 import java.util.List;
 import java.util.Map;
 import java.util.Map.Entry;
 import java.util.Set;
 import org.codehaus.jackson.map.ObjectMapper;
 
 public class JsonManager
 {
   public FuncModel json2model(String jsonStr)
     throws Exception
   {
     ObjectMapper mapper = new ObjectMapper();
     Map<?, ?> jsonMap = (Map)mapper.readValue(jsonStr, Map.class);
     
     FuncModel funcModel = new FuncModel();
     
     LinkedHashMap<?, ?> functionParamsMap = (LinkedHashMap)jsonMap.get("FunctionParams");
     funcModel.setParamsMap(functionParamsMap);
     
 
     Map<String, List<Map<String, String>>> dataPackage = (Map)jsonMap.get("DataPackage");
     Iterator localIterator2;
     for (Iterator localIterator1 = dataPackage.entrySet().iterator(); localIterator1.hasNext(); localIterator2.hasNext())
     {
       Map.Entry<String, List<Map<String, String>>> dataSet = (Map.Entry)localIterator1.next();
       String dataSetName = (String)dataSet.getKey();
       List<Map<String, String>> rowList = (List)dataSet.getValue();
       Long rowID = Long.valueOf(1L);
       localIterator2 = rowList.iterator(); continue;Map<String, String> row = (Map)localIterator2.next();
       for (Map.Entry<String, String> field : row.entrySet()) {
         funcModel.addDataSetField(dataSetName, rowID, (String)field.getKey(), field.getValue());
       }
       rowID = Long.valueOf(rowID.longValue() + 1L);
     }
     return funcModel;
   }
   
   public String model2json(FuncModel funcModel)
     throws Exception
   {
     ObjectMapper mapper = new ObjectMapper();
     Map<String, Object> jsonMap = new LinkedHashMap();
     jsonMap.put("FunctionParams", funcModel.getParamsMap());
     
 
     Map<String, List<Map<String, String>>> dataPackageJsonObj = new LinkedHashMap();
     Map<String, Map<Long, Map<String, String>>> dataPackageModel = funcModel.getDataPackge();
     for (Map.Entry<String, Map<Long, Map<String, String>>> dataSet : dataPackageModel.entrySet())
     {
       String dataSetName = (String)dataSet.getKey();
       List<Map<String, String>> rowList = new ArrayList();
       Map<Long, Map<String, String>> rowMap = (Map)dataSet.getValue();
       for (long i = 1L; i <= rowMap.size(); i += 1L) {
         rowList.add((Map)rowMap.get(Long.valueOf(i)));
       }
       dataPackageJsonObj.put(dataSetName, rowList);
     }
     jsonMap.put("DataPackage", dataPackageJsonObj);
     return MsgModelUtil.toUnicodeString(mapper.writeValueAsString(jsonMap));
   }
   
   public String removeDataPackage(String jsonStr)
   {
     LinkedList<String> stack = new LinkedList();
     stack.addFirst("{");
     int startPos = jsonStr.indexOf("\"DataPackage\":{") + "\"DataPackage\":{".length();
     int endPos = -1;
     char lastChar = '{';
     int count = 0;
     int i = startPos;
     for (int len = jsonStr.length(); i < len; i++)
     {
       char currentChar = jsonStr.charAt(i);
       if (('"' == currentChar) && ('\\' != lastChar))
       {
         if (((String)stack.peek()).equals("\"")) {
           stack.removeFirst();
         } else {
           stack.addFirst("\"");
         }
       }
       else if (('{' == currentChar) && (!((String)stack.peek()).equals("\""))) {
         stack.addFirst("{");
       } else if (('}' == currentChar) && (!((String)stack.peek()).equals("\""))) {
         stack.removeFirst();
       }
       if (stack.isEmpty())
       {
         endPos = i;
         break;
       }
       lastChar = currentChar;
     }
     return jsonStr.substring(0, startPos) + jsonStr.substring(endPos);
   }
   
   private void test()
     throws Exception
   {
     String jsonStr = "{\"DataPackage\":{},\"FunctionParams\":{\"FHZ\":\"1\",\"MSG\":\"执行成功！\",\"SESSIONID\":\"QKSZPNrRPptTGs3ymqvJhQLZyxKJpd4XCHGJQBGFQcFQtwRYGvxS!306292812!1338878737991\"}}";
     JsonManager jm = new JsonManager();
     FuncModel funcModel = jm.json2model(jsonStr);
     System.out.println(jm.model2json(funcModel));
     System.out.println(jsonStr);
     
     String jsonStr2 = "{\"DataPackage\":{\"JZZL\":[{\"CYBZ\":\"2\",\"CYQK\":\"\",\"CYRQ\":\"20090205\",\"CYZD\":\"霍乱\",\"CYZD1\":\"\",\"CYZDGJDM\":\"A00.901\",\"JZJLH\":\"136\",\"JZLB\":\"21\",\"RYRQ\":\"20090205\",\"RYZD\":\"霍乱\",\"RYZD1\":\"\",\"RYZDGJDM\":\"A00.901\",\"RYZS\":\"\",\"SSMC\":\"\",\"YYBH\":\"1000\",\"ZYH\":\"\",\"ZYJSLB\":\"1\"},{\"CYBZ\":\"2\",\"CYQK\":\"\",\"CYRQ\":\"20090202\",\"CYZD\":\"霍乱\",\"CYZD1\":\"\",\"CYZDGJDM\":\"A00.901\",\"JZJLH\":\"142\",\"JZLB\":\"21\",\"RYRQ\":\"20090201\",\"RYZD\":\"霍乱\",\"RYZD1\":\"\",\"RYZDGJDM\":\"A00.901\",\"RYZS\":\"\",\"SSMC\":\"\",\"YYBH\":\"1000\",\"ZYH\":\"\",\"ZYJSLB\":\"1\"},{\"CYBZ\":\"2\",\"CYQK\":\"\",\"CYRQ\":\"20090304\",\"CYZD\":\"霍乱\",\"CYZD1\":\"\",\"CYZDGJDM\":\"A00.901\",\"JZJLH\":\"132\",\"JZLB\":\"21\",\"RYRQ\":\"20090303\",\"RYZD\":\"霍乱\",\"RYZD1\":\"\",\"RYZDGJDM\":\"A00.901\",\"RYZS\":\"\",\"SSMC\":\"\",\"YYBH\":\"1000\",\"ZYH\":\"\",\"ZYJSLB\":\"1\"},{\"CYBZ\":\"2\",\"CYQK\":\"\",\"CYRQ\":\"20090306\",\"CYZD\":\"nihao\",\"CYZD1\":\"\",\"CYZDGJDM\":\"\",\"JZJLH\":\"140\",\"JZLB\":\"21\",\"RYRQ\":\"20090305\",\"RYZD\":\"nihao\",\"RYZD1\":\"\",\"RYZDGJDM\":\"\",\"RYZS\":\"\",\"SSMC\":\"\",\"YYBH\":\"1000\",\"ZYH\":\"\",\"ZYJSLB\":\"1\"}]},\"FunctionParams\":{\"FHZ\":\"1\",\"GET_PAGE_TOTAL\":\"4\",\"HAS_NEXT_REQUEST\":\"false\",\"MSG\":\"执行成功！\",\"SESSIONID\":\"QKSZPNrRPptTGs3ymqvJhQLZyxKJpd4XCHGJQBGFQcFQtwRYGvxS!306292812!1338878737991\"}}";
     FuncModel funcModel2 = jm.json2model(jsonStr2);
     System.out.println(jm.model2json(funcModel2));
     System.out.println(jsonStr2);
     
     FuncModel funcModel3 = new FuncModel();
     System.out.println(model2json(funcModel3));
     
     String simplestJsonString = "{\"FunctionParams\":{},\"DataPackage\":{}}";
     System.out.println(model2json(json2model(simplestJsonString)));
   }
   
   private void testRemoveDp()
   {
     JsonManager jm = new JsonManager();
     
     String jsonStr = "{\"DataPackage\":{\"JZZL\":[{\"CY\\\"BZ\":\"2\",\"CYQK\":\"\",\"CYRQ\":\"20090205\",\"CYZD\":\"霍乱\",\"CYZD1\":\"\",\"CYZDGJDM\":\"A00.901\",\"JZJLH\":\"136\",\"JZLB\":\"21\",\"RYRQ\":\"20090205\",\"RYZD\":\"霍乱\",\"RYZD1\":\"\",\"RYZDGJDM\":\"A00.901\",\"RYZS\":\"\",\"SSMC\":\"\",\"YYBH\":\"1000\",\"ZYH\":\"\",\"ZYJSLB\":\"1\"},{\"CYBZ\":\"2\",\"CYQK\":\"\",\"CYRQ\":\"20090202\",\"CYZD\":\"霍乱\",\"CYZD1\":\"\",\"CYZDGJDM\":\"A00.901\",\"JZJLH\":\"142\",\"JZLB\":\"21\",\"RYRQ\":\"20090201\",\"RYZD\":\"霍乱\",\"RYZD1\":\"\",\"RYZDGJDM\":\"A00.901\",\"RYZS\":\"\",\"SSMC\":\"\",\"YYBH\":\"1000\",\"ZYH\":\"\",\"ZYJSLB\":\"1\"},{\"CYBZ\":\"2\",\"CYQK\":\"\",\"CYRQ\":\"20090304\",\"CYZD\":\"霍乱\",\"CYZD1\":\"\",\"CYZDGJDM\":\"A00.901\",\"JZJLH\":\"132\",\"JZLB\":\"21\",\"RYRQ\":\"20090303\",\"RYZD\":\"霍乱\",\"RYZD1\":\"\",\"RYZDGJDM\":\"A00.901\",\"RYZS\":\"\",\"SSMC\":\"\",\"YYBH\":\"1000\",\"ZYH\":\"\",\"ZYJSLB\":\"1\"},{\"CYBZ\":\"2\",\"CYQK\":\"\",\"CYRQ\":\"20090306\",\"CYZD\":\"nihao\",\"CYZD1\":\"\",\"CYZDGJDM\":\"\",\"JZJLH\":\"140\",\"JZLB\":\"21\",\"RYRQ\":\"20090305\",\"RYZD\":\"nihao\",\"RYZD1\":\"\",\"RYZDGJDM\":\"\",\"RYZS\":\"\",\"SSMC\":\"\",\"YYBH\":\"1000\",\"ZYH\":\"\",\"ZYJSLB\":\"1\"}]},\"FunctionParams\":{\"FHZ\":\"1\",\"GET_PAGE_TOTAL\":\"4\",\"HAS_NEXT_REQUEST\":\"false\",\"MSG\":\"执行成功！\",\"SESSIONID\":\"QKSZPNrRPptTGs3ymqvJhQLZyxKJpd4XCHGJQBGFQcFQtwRYGvxS!306292812!1338878737991\"}}";
     String s = jm.removeDataPackage(jsonStr);
     System.out.println(s);
   }
   
   public static void main(String[] args)
     throws Exception
   {
     new JsonManager().testRemoveDp();
   }
 }