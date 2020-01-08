 package cn.sinobest.framework.service.tags;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IDAO;
 import cn.sinobest.framework.comm.iface.IPageDAO;
 import cn.sinobest.framework.util.ConfUtil;
 import cn.sinobest.framework.util.Util;
 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.Collections;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.LinkedHashMap;
 import java.util.LinkedList;
 import java.util.List;
 import java.util.Map;
 import java.util.Map.Entry;
 import java.util.Set;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
 
 @Service
 public class GltService
 {
   @Autowired
   IPageDAO jDBCPageDAO;
   @Autowired
   IDAO commDAO;
   public static final String COLSPAN = "colspan";
   public static final String ROWSPAN = "rowspan";
   public static final String COLNAME = "title";
   public static final String LASTROWINDEX = "lastRowIndex";
   private static final String CHECKBOX = "checkbox";
   private static final Pattern PATTERN_SELECT = Pattern.compile("^\\s*select\\s+(?:distinct\\s+)?(.*)\\s+from.*(?:\\s+where)?.*$", 34);
   private static final Pattern PATTERN_FIELDS = Pattern.compile("(?:\\w+\\.){0,1}(\\w+)\\s*(\\w+\\s*){0,1}", 2);
   private static final String REGEX_REPLACE2 = "[\\n\\t\\r\\f]+";
   private static final String REGEX_REPLACE = "\\([^\\)\\(]*\\)|['+%\\|\\*/\\-]+|<\\s*(\\w+)\\s*[^</]*\\s*<\\s*/\\1\\s*>|<\\w+\\s*[^/>]*\\s*/>";
   private static final String REGEX_REPLACE_WHERE = "\\([^\\)\\(]*\\)";
   private static final Pattern PATTERN_WHERE = Pattern.compile("\\s*(\\b\\d+\\b)\\s*(=)\\s*(\\b\\d+\\b)\\s*", 2);
   private static final String DEFUALT_ALIAS = "_a_";
   
   public static Map<String, Object>[][] parseCol(String source)
   {
     Map[][] rst = (Map[][])null;
     if ((source == null) || (source.trim().length() == 0)) {
       return null;
     }
     source = source.replaceAll("\n", "");
     try
     {
       String[] rows = source.split("\\|\\|");
       rst = new Map[rows.length][];
       for (int i = 0; i < rows.length; i++)
       {
         String[] cols = rows[i].split("\\|");
         Map[] row_rst = new Map[cols.length];
         for (int j = 0; j < cols.length; j++)
         {
           Map<String, Object> col_rst = new HashMap();
           String[] attrs = cols[j].split("\\*");
           col_rst.put("title", attrs[0]);
           for (int k = 1; k < attrs.length; k++)
           {
             int pos = attrs[k].indexOf('=');
             if (pos == -1) {
               col_rst.put(attrs[k].substring(0, pos).trim(), null);
             } else {
               col_rst.put(attrs[k].substring(0, pos).trim(), 
                 attrs[k].substring(pos + 1).trim());
             }
           }
           row_rst[j] = col_rst;
         }
         rst[i] = row_rst;
       }
     }
     catch (Exception e)
     {
       throw new AppException("EFW0106", e, new Object[] { source });
     }
     return rst;
   }
   
   public static Map<String, String> parseDict(String source)
   {
     HashMap<String, String> rst = null;
     if ((source == null) || (source.trim().length() == 0)) {
       return null;
     }
     String[] dicts = source.split("\\|");
     rst = new HashMap();
     for (int i = 0; i < dicts.length; i++)
     {
       String dict = dicts[i];
       String[] dictCode = dict.split("=");
       if (dictCode.length == 1) {
         rst.put(dictCode[0].trim(), dictCode[0].trim());
       } else {
         rst.put(dictCode[0].trim(), dictCode[1].trim());
       }
     }
     return rst;
   }
   
   public static int rowCount(Map[][] rows)
   {
     if ((rows == null) || (rows.length == 0) || (rows[0] == null) || 
       (rows[0].length == 0)) {
       return 0;
     }
     int rst = 0;
     for (Map map : rows[0]) {
       if (map.containsKey("colspan")) {
         rst += Integer.valueOf((String)map.get("colspan")).intValue();
       } else {
         rst++;
       }
     }
     return rst;
   }
   
   public static Collection<Integer> listCheckBoxIndices(Glt glt)
   {
     List<Integer> indices = new LinkedList();
     for (Map<String, Object> col : lastRow(glt)) {
       if (col.get("checkbox") != null) {
         indices.add((Integer)col.get("lastRowIndex"));
       }
     }
     return indices;
   }
   
   public static Map<String, Object>[] lastRow(Glt glt)
   {
     if (glt.lastRow == null)
     {
       Map[][] header = mergeHeader(glt);
       
       int[] todoIndices = new int[header.length];
       
       int[] ignoreTimes = new int[header.length];
       List<Map<String, Object>> lastRow = new LinkedList();
       do
       {
         for (int rowIndex = 0; rowIndex < header.length; rowIndex++) {
           if (ignoreTimes[rowIndex] > 0)
           {
             ignoreTimes[rowIndex] -= 1;
           }
           else if (todoIndices[rowIndex] >= 0)
           {
             Map<String, Object> theCell = header[rowIndex][todoIndices[rowIndex]];
             int rowSpan = theCell.get("rowspan") == null ? 1 : 
               Integer.parseInt((String)theCell.get("rowspan"));
             int colSpan = theCell.get("colspan") == null ? 1 : 
               Integer.parseInt((String)theCell.get("colspan"));
             if ((rowSpan < 1) || (colSpan < 1)) {
               throw new AppException("EFW0107", null, new Object[] { Integer.valueOf(rowSpan), 
                 Integer.valueOf(colSpan) });
             }
             if (rowSpan + rowIndex >= header.length)
             {
               lastRow.add(theCell);
               if (todoIndices[rowIndex] += 1 < header[rowIndex].length) {
                 break;
               }
               todoIndices[rowIndex] = -1;
               
               break;
             }
             if (colSpan > 1) {
               ignoreTimes[rowIndex] = (colSpan - 1);
             }
           }
         }
       } while (todoIndices[(header.length - 1)] >= 0);
       glt.lastRow = ((Map[])lastRow.toArray(header[0]));
     }
     return glt.lastRow;
   }
   
   public static Map<String, Object>[][] mergeHeader(Glt glt)
   {
     if (glt.getFixHead() == null) {
       return glt.getColHead();
     }
     if (glt.getColHead() == null) {
       return glt.getFixHead();
     }
     int rowNum = Math.max(glt.getColHead().length, glt.getFixHead().length);
     Map[][] rst = new HashMap[rowNum][];
     for (int i = 0; i < rowNum; i++)
     {
       int newLength = 0;
       Map[] fixRow = (Map[])null;Map[] colRow = (Map[])null;
       if (i < glt.getFixHead().length)
       {
         fixRow = glt.getFixHead()[(glt.getFixHead().length - 1 - i)];
         newLength = fixRow.length;
       }
       else
       {
         newLength++;
         
         fixRow = new Map[1];
         fixRow[0] = new HashMap();
         fixRow[0].put("rowspan", String.valueOf(1));
         fixRow[0].put("colspan", String.valueOf(glt.getFixcolumns()));
       }
       if (i < glt.getColHead().length)
       {
         colRow = glt.getColHead()[(glt.getColHead().length - 1 - i)];
         newLength += colRow.length;
       }
       else
       {
         newLength++;
         colRow = new Map[1];
         colRow[0] = new HashMap();
         colRow[0].put("rowspan", String.valueOf(1));
         colRow[0].put("colspan", 
           String.valueOf(glt.getColumns() - glt.getFixcolumns()));
       }
       Map[] newRow = new HashMap[newLength];
       int counter = 0;
       
 
       System.arraycopy(fixRow, 0, newRow, counter, fixRow.length);
       counter += fixRow.length;
       
 
       System.arraycopy(colRow, 0, newRow, counter, colRow.length);
       
       rst[(rowNum - 1 - i)] = newRow;
     }
     return rst;
   }
   
   private static void markLastRow(Glt glt)
   {
     Map[] vlastRow = lastRow(glt);
     int index = 1;
     for (Map<String, Object> col : vlastRow) {
       col.put("lastRowIndex", Integer.valueOf(index++));
     }
   }
   
   public Map<String, Object> getInitData(List<Glt> sGlt, String id, String whereCls, Map<String, String> dyndictWhereCls, int from, int pageSize, boolean isCusData, List<Map<String, Object>> data)
   {
     Glt glt = sGlt.size() > 0 ? (Glt)sGlt.remove(0) : null;
     if (glt == null)
     {
       glt = getGlt(id);
       sGlt.add(0, glt);
     }
     Map<String, Object> datas = new HashMap();
     String tmpWhereCls = whereCls;
     
     List<Map<String, String>> rst = null;
     if (isCusData)
     {
       tmpWhereCls = "1=2";
       rst = wrapData(glt, data, 
         dyndictWhereCls);
       if (rst != null) {
         datas.put("total", Integer.valueOf(rst.size()));
       } else {
         datas.put("total", Integer.valueOf(0));
       }
       datas.put(
         "headers", 
         new ArrayList(((Map)getRowData(glt, tmpWhereCls, dyndictWhereCls, 1, pageSize).remove(0)).values()));
     }
     else
     {
       rst = getRowData(glt, tmpWhereCls, 
         dyndictWhereCls, 1, pageSize);
       datas.put("total", Long.valueOf(getRowCount(glt, tmpWhereCls)));
       datas.put("headers", new ArrayList(((Map)rst.remove(0)).values()));
       
       datas.put("subs", getSubTotal(glt, tmpWhereCls));
     }
     if (((rst == null) || (rst.isEmpty())) && 
       ("preview".equals(ConfUtil.getSysParam(
       "app.runMode", null)))) {
       rst = preparePreview();
     }
     datas.put("rows", rst);
     return datas;
   }
   
   private List<Map<String, String>> preparePreview()
   {
     int rowNum = (int)ConfUtil.getSysParam2Number("glt.previewRows", 5L);
     List<Map<String, String>> list = new ArrayList(
       rowNum);
     for (int i = 0; i < rowNum; i++) {
       list.add(new HashMap());
     }
     return list;
   }
   
   public Glt getGlt(String id)
     throws AppException
   {
     if (ConfUtil.getGenListConf(id) != null) {
       return (Glt)ConfUtil.getGenListConf(id);
     }
     Map<String, String> paramsMap = new HashMap();
     paramsMap.put("ID", id);
     Glt glt = new Glt();
     try
     {
       List<Map<String, Object>> rst = this.commDAO.select(
         "FW_CONFIG.FW_GENLIST_CONF_Q", paramsMap);
       if ((rst == null) || (rst.isEmpty())) {
         throw new AppException("EFW0101", null, new Object[] { id });
       }
       Map<String, Object> glt_conf = (Map)rst.get(0);
       glt.setId((String)glt_conf.get("ID"));
       glt.setTitle((String)glt_conf.get("TITLE"));
       glt.setSelectStr((String)glt_conf.get("SELECTSTR"));
       glt.setType((String)glt_conf.get("TYPE"));
       glt.setFixHead(parseCol((String)glt_conf.get("FIXHEAD")));
       glt.setColHead(parseCol((String)glt_conf.get("COLHEAD")));
       glt.setSubTotalStr((String)glt_conf.get("SUBTOTALSTR"));
       glt.setDict(parseDict((String)glt_conf.get("DICT")));
       glt.setFixcolumns(rowCount(glt.getFixHead()));
       glt.setFields(parseSql((String)glt_conf.get("SELECTSTR")));
       glt.setColumns(glt.getFixcolumns() + rowCount(glt.getColHead()));
       markLastRow(glt);
     }
     catch (AppException e)
     {
       throw e;
     }
     catch (Exception e)
     {
       throw new AppException("EFW0102", e, new Object[] { id });
     }
     ConfUtil.setGenListConf(id, glt);
     return glt;
   }
   
   public long getRowCount(Glt glt, String whereCls)
   {
     StringBuffer sql = new StringBuffer(glt.getSelectStr());
     sql.append(" where ").append(whereCls);
     try
     {
       if (!isNeedQuery(whereCls)) {
         return 0L;
       }
       return this.jDBCPageDAO.getCount(sql.toString()).longValue();
     }
     catch (Exception e)
     {
       throw new AppException("EFW0103", e, new Object[] { sql.toString() });
     }
   }
   
   public List<Map<String, String>> getSubTotal(Glt glt, String whereCls)
   {
     if ((glt.getSubTotalStr() == null) || (glt.getSubTotalStr().length() == 0)) {
       return null;
     }
     StringBuffer sql = new StringBuffer(glt.getSubTotalStr()).append(
       " where ").append(whereCls);
     List<Map<String, String>> ret = new ArrayList();
     Map<String, String> map = new HashMap();
     ret.add(map);
     try
     {
       List<String[]> subList = this.jDBCPageDAO.getData(sql.toString(), 1, 1);
       if (subList.size() > 1)
       {
         String[] colHeads = (String[])subList.get(0);
         String[] subValues = (String[])subList.get(1);
         for (int i = 0; i < colHeads.length; i++) {
           map.put(colHeads[i], subValues[i]);
         }
       }
       return ret;
     }
     catch (Exception e)
     {
       throw new AppException("EFW0104", e, new Object[] { sql.toString() });
     }
   }
   
   public static Map<String, Map<String, Map<String, String>>> prepareDictData(Glt glt, Map<String, String> dyndictWhereCls)
     throws AppException
   {
     Map<String, Map<String, Map<String, String>>> dicts = new HashMap();
     if (dyndictWhereCls == null) {
       dyndictWhereCls = Collections.EMPTY_MAP;
     }
     if ((glt.getDict() != null) && (!glt.getDict().isEmpty()))
     {
       Map<String, List<String>> revertMap = new HashMap();
       for (Map.Entry<String, String> entry : glt.getDict().entrySet())
       {
         if (revertMap.get(entry.getValue()) == null) {
           revertMap.put((String)entry.getValue(), new ArrayList());
         }
         ((List)revertMap.get(entry.getValue())).add((String)entry.getKey());
       }
       Map<String, Map<String, String>> aliasMaps = new HashMap();
       for (Object item : revertMap.entrySet())
       {
         Map<String, Map<String, String>> rtn = null;
         if (((String)((Map.Entry)item).getKey()).startsWith("dyndict_"))
         {
           rtn = ConfUtil.getDynDict((String)((Map.Entry)item).getKey(), 
             (String)dyndictWhereCls.get(((Map.Entry)item).getKey()));
         }
         else if (((String)((Map.Entry)item).getKey()).indexOf('@') != -1)
         {
           String[] value = ((String)((Map.Entry)item).getKey()).split("\\@");
           rtn = ConfUtil.getSubDict(value[0], value[1]);
         }
         else
         {
           rtn = ConfUtil.getDict((String)((Map.Entry)item).getKey());
         }
         if (rtn != null) {
           for (String alias : (List)((Map.Entry)item).getValue()) {
             dicts.put(alias, rtn);
           }
         }
       }
     }
     return dicts;
   }
   
   public List<Map<String, String>> getRowData(Glt glt, String whereCls, Map<String, String> dyndictWhereCls, int from, int size)
     throws AppException
   {
     StringBuffer sql = new StringBuffer(glt.getSelectStr()).append(
       " where ").append(whereCls);
     List<Map<String, String>> rst = null;
     try
     {
       String[] headers = (String[])null;
       List<String[]> selectDatas = null;
       boolean isNeedQuery = isNeedQuery(whereCls);
       if (!isNeedQuery)
       {
         headers = glt.getFields();
         rst = new ArrayList(1);
       }
       else
       {
         selectDatas = selectDatas = this.jDBCPageDAO.getData(sql.toString(), from, size);
         rst = new ArrayList(selectDatas.size());
         
         headers = (String[])selectDatas.remove(0);
       }
       Map<String, String> headerMap = new LinkedHashMap();
       for (int i = 0; i < headers.length; i++) {
         headerMap.put(String.valueOf(i), headers[i]);
       }
       rst.add(headerMap);
       if (!isNeedQuery) {
         return rst;
       }
       Map<String, Map<String, Map<String, String>>> dicts = prepareDictData(
         glt, dyndictWhereCls);
       
 
       Set<Map.Entry<String, Map<String, Map<String, String>>>> dictsSet = dicts
         .entrySet();
       
       Map<String, String> colDatas = null;
       for (String[] cols : selectDatas)
       {
         colDatas = new HashMap();
         for (int i = 0; i < cols.length; i++) {
           colDatas.put(headers[i], cols[i]);
         }
         for (Map.Entry<String, Map<String, Map<String, String>>> entry : dictsSet)
         {
           String itemcode = (String)colDatas.get(entry.getKey());
           if ((itemcode != null) && 
             (((Map)entry.getValue()).containsKey(itemcode))) {
             colDatas.put("_DIC_" + (String)entry.getKey(), 
               (String)((Map)((Map)entry.getValue()).get(itemcode)).get("AAA103"));
           }
         }
         rst.add(colDatas);
       }
     }
     catch (Exception e)
     {
       throw new AppException("EFW0105", e, new Object[] { sql.toString() });
     }
     return rst;
   }
   
   public List<Map<String, String>> wrapData(Glt glt, List<Map<String, Object>> data, Map<String, String> dyndictWhereCls)
   {
     if ((data == null) || (data.isEmpty())) {
       return null;
     }
     List<Map<String, String>> newListData = new ArrayList(
       data.size());
     for (Map<String, Object> from : data)
     {
       Map<String, String> to = new HashMap(from.size());
       for (Iterator localIterator2 = from.entrySet().iterator(); localIterator2.hasNext();)
       {
         entry = (Map.Entry)localIterator2.next();
         if (entry.getValue() == null) {
           to.put((String)entry.getKey(), null);
         } else {
           to.put((String)entry.getKey(), entry.getValue().toString());
         }
       }
       newListData.add(to);
     }
     Map<String, Map<String, Map<String, String>>> dicts = prepareDictData(
       glt, dyndictWhereCls);
     
     Object dictsSet = dicts
       .entrySet();
     Iterator localIterator3;
     for (Map.Entry<String, Object> entry = newListData.iterator(); entry.hasNext(); localIterator3.hasNext())
     {
       Map<String, String> row = (Map)entry.next();
       
       localIterator3 = ((Set)dictsSet).iterator(); continue;Object entry = (Map.Entry)localIterator3.next();
       String itemcode = (String)row.get(((Map.Entry)entry).getKey());
       if ((itemcode != null) && (((Map)((Map.Entry)entry).getValue()).containsKey(itemcode))) {
         row.put("_DIC_" + (String)((Map.Entry)entry).getKey(), 
           (String)((Map)((Map)((Map.Entry)entry).getValue()).get(itemcode)).get("AAA103"));
       }
     }
     return newListData;
   }
   
   public String[] parseSql(String sql)
     throws AppException
   {
     String tmpStr = sql.replaceAll("\\([^\\)\\(]*\\)|['+%\\|\\*/\\-]+|<\\s*(\\w+)\\s*[^</]*\\s*<\\s*/\\1\\s*>|<\\w+\\s*[^/>]*\\s*/>", "_a_");
     while ((tmpStr.indexOf("(") > -1) && (tmpStr.indexOf(")") > -1)) {
       tmpStr = tmpStr.replaceAll("\\([^\\)\\(]*\\)|['+%\\|\\*/\\-]+|<\\s*(\\w+)\\s*[^</]*\\s*<\\s*/\\1\\s*>|<\\w+\\s*[^/>]*\\s*/>", "_a_");
     }
     Matcher mt = PATTERN_SELECT.matcher(tmpStr);
     if (mt.matches()) {
       tmpStr = mt.group(1);
     }
     mt = PATTERN_FIELDS.matcher(tmpStr.replaceAll("[\\n\\t\\r\\f]+", ""));
     String field = "";
     String alias = "";
     List<String> fields = new ArrayList();
     while (mt.find())
     {
       field = mt.group(1);
       alias = mt.group(2);
       if ((field.indexOf("_a_") > -1) && (Util.isEmpty(alias))) {
         throw new AppException("查询语句解析出错，请检查查询语中选择字段是否有别名!");
       }
       fields.add(Util.isEmpty(alias) ? field : alias);
     }
     String[] f = new String[fields.size()];
     int i = 0;
     for (String item : fields) {
       f[(i++)] = item;
     }
     return f;
   }
   
   public static boolean isNeedQuery(String whereCls)
   {
     if (Util.isEmpty(whereCls)) {
       return true;
     }
     String newWhereCls = whereCls;
     while ((newWhereCls.indexOf("(") > -1) && (newWhereCls.indexOf(")") > -1)) {
       newWhereCls = newWhereCls.replaceAll("\\([^\\)\\(]*\\)", "");
     }
     if (newWhereCls.toLowerCase().indexOf("or") > 0) {
       return true;
     }
     Matcher mt = PATTERN_WHERE.matcher(newWhereCls);
     while (mt.find()) {
       if (Long.valueOf(mt.group(1)).longValue() != Long.valueOf(mt.group(3)).longValue()) {
         return false;
       }
     }
     return true;
   }
 }