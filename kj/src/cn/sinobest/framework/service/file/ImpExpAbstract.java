 package cn.sinobest.framework.service.file;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IDAO;
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.comm.iface.IImpExp;
 import cn.sinobest.framework.comm.iface.IUploadWork;
 import cn.sinobest.framework.service.CommService;
 import cn.sinobest.framework.service.tags.GltService;
 import cn.sinobest.framework.util.ConfUtil;
 import cn.sinobest.framework.util.Util;
 import java.io.Closeable;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.FileNotFoundException;
 import java.io.IOException;
 import java.io.InputStream;
 import java.lang.reflect.Array;
 import java.util.Arrays;
 import java.util.Collections;
 import java.util.Enumeration;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.LinkedList;
 import java.util.List;
 import java.util.Map;
 import java.util.Map.Entry;
 import java.util.Set;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 import org.apache.log4j.Logger;
 import org.springframework.dao.DataAccessException;
 import org.springframework.jdbc.core.JdbcTemplate;
 import org.springframework.jdbc.object.BatchSqlUpdate;
 
 public abstract class ImpExpAbstract
   implements IImpExp
 {
   public class CommUploadWork
     implements IUploadWork
   {
     private IDAO commDAO;
     private JdbcTemplate jdbcTemplate;
     
     public CommUploadWork() {}
     
     public void close()
       throws IOException
     {
       ImpExpAbstract.LOG.debug("释放上传资源。。");
     }
     
     public Object doWork(ImpExpConfig expimpConfig, Map<String, Object> hashData, Enumeration<Map<String, Object>> enumeration)
       throws AppException
     {
       ImpExpAbstract.LOG.debug("开始上传了。。");
       long startTime = System.currentTimeMillis();
       
       String drph = null;
       try
       {
         drph = getIDAO().sequence("FW_CONFIG.SEQ_IMPEXP_DRPH");
         
         hashData.put("DRPH", drph);
         ImpExpAbstract.LOG.debug("文件的导入批号是：" + drph);
       }
       catch (AppException e)
       {
         throw e;
       }
       JdbcTemplate jdbcTemplate = getJdbcTemplate();
       if (expimpConfig.getHashSqlStr() != null) {
         try
         {
           for (String hashSql : expimpConfig.getHashSqlStr())
           {
             List<String> params = ExcelImpExp.parseSql(hashSql);
             String parsedSql = (String)params.remove(0);
             
             Object[] preparedParams = ImpExpAbstract.selectArguments(hashData, 
               params);
             int[] types = ImpExpAbstract.this.getHashTypes(params);
             jdbcTemplate.update(parsedSql, preparedParams, types);
           }
         }
         catch (DataAccessException e)
         {
           throw new AppException("EFW0221", e, new Object[] { expimpConfig.getId() });
         }
       }
       if ((expimpConfig.getSqlStr() != null) && 
         (expimpConfig.getSqlStr().length > 0)) {
         try
         {
           List<String> params = 
             ExcelImpExp.parseSql(expimpConfig.getSqlStr()[0]);
           String parsedSql = (String)params.remove(0);
           int[] types = ImpExpAbstract.this.getSqlTypes(params);
           BatchSqlUpdate batchUpdate = new BatchSqlUpdate(
             jdbcTemplate.getDataSource(), parsedSql, types, 200);
           int index = 0;
           while (enumeration.hasMoreElements())
           {
             Map<String, Object> row = new HashMap(
               hashData);
             
             row.putAll((Map)enumeration.nextElement());
             row.put("DRXH", String.valueOf(index++));
             Object[] preparedParams = ImpExpAbstract.selectArguments(row, params);
             batchUpdate.update(preparedParams);
           }
           batchUpdate.flush();
         }
         catch (DataAccessException e)
         {
           throw new AppException("EFW0222", e, new Object[] { expimpConfig.getId() });
         }
       }
       ImpExpAbstract.LOG.debug("上传数据写入数据库共花了" + (System.currentTimeMillis() - startTime) + 
         "毫秒");
       return drph;
     }
     
     protected IDAO getIDAO()
     {
       if (this.commDAO == null) {
         this.commDAO = ((IDAO)Util.getBean("commDAO"));
       }
       return this.commDAO;
     }
     
     protected JdbcTemplate getJdbcTemplate()
     {
       if (this.jdbcTemplate == null) {
         this.jdbcTemplate = ((JdbcTemplate)Util.getBean("jdbcTemplate"));
       }
       return this.jdbcTemplate;
     }
   }
   
   public static final Map<String, Object> DEFAULT_VALUE = new HashMap(5);
   public static final String KEY_ALIGN = "align";
   public static final String KEY_LABEL = "label";
   public static final String KEY_NAME = "name";
   public static final String KEY_PAD = "pad";
   public static final String KEY_TYPE = "type";
   public static final String KEY_DEC = "dec";
   public static final String KEY_WIDTH = "width";
   protected static final Logger LOG = Logger.getLogger(ImpExpAbstract.class);
   protected Map<String, Map<String, Map<String, String>>> dictMap;
   protected ImpExpConfig impExpConfig;
   
   static
   {
     DEFAULT_VALUE.put("align", ImpExpConfig.Align.LEFT);
     DEFAULT_VALUE.put("pad", Character.valueOf(' '));
     DEFAULT_VALUE.put("type", ImpExpConfig.Type.STRING);
     DEFAULT_VALUE.put("width", Integer.valueOf(0));
   }
   
   public static Map<String, Map<String, Map<String, String>>> extractDicts(Map<String, String> dictConfig, Map<String, String> dynDictWhereCls)
   {
     if ((dictConfig == null) || (dictConfig.isEmpty())) {
       return null;
     }
     Map<String, Map<String, Map<String, String>>> dictMap = new HashMap();
     for (Map.Entry<String, String> item : dictConfig.entrySet())
     {
       Map<String, Map<String, String>> rtn = null;
       if (((String)item.getValue()).startsWith("dyndict_"))
       {
         rtn = ConfUtil.getDynDict((String)item.getValue(), 
           (String)dynDictWhereCls.get(item.getValue()));
       }
       else if (((String)item.getValue()).indexOf('@') != -1)
       {
         String[] value = ((String)item.getValue()).split("\\@");
         rtn = ConfUtil.getSubDict(value[0], value[1]);
       }
       else
       {
         rtn = ConfUtil.getDict((String)item.getValue());
       }
       dictMap.put((String)item.getKey(), rtn);
     }
     return dictMap;
   }
   
   public static ImpExpConfig getExpImpConfig(String configId)
     throws AppException
   {
     Map<String, String> rst = ConfUtil.getExcelConf(configId);
     if (rst == null) {
       return null;
     }
     ImpExpConfig config = new ImpExpConfig();
     config.setId((String)rst.get("ID"));
     
 
     String sqlStr = (String)rst.get("SQLSTR");
     if ((sqlStr != null) && (sqlStr.trim().length() > 0)) {
       config.setSqlStr(sqlStr.trim().replaceAll("\n", "").split("[$]"));
     }
     String hashSql = (String)rst.get("HASHSQLSTR");
     if ((hashSql != null) && (hashSql.trim().length() > 0)) {
       config.setHashSqlStr(hashSql.trim().replaceAll("\n", "")
         .split("[$]"));
     }
     String dicts = (String)rst.get("DICTINFO");
     if ((dicts != null) && (dicts.trim().length() > 0)) {
       config.setDictInfo(GltService.parseDict(dicts));
     }
     String templateFile = (String)rst.get("TEMPLATEFILE");
     if (templateFile != null) {
       config.setTemplateFile(templateFile.trim());
     }
     String delimiter = (String)rst.get("DELIMITER");
     if ((delimiter != null) && (delimiter.length() > 0)) {
       config.setDelimiter(delimiter);
     }
     String renderstr = (String)rst.get("RENDERSTR");
     if ((renderstr != null) && (renderstr.trim().length() > 0))
     {
       config.renders = parseRenderStr(renderstr);
       normalize(config.renders);
       Map<String, Object> defV = new HashMap(
         TextImpExp.DEFAULT_VALUE);
       for (int i = 0; i < config.renders.length; i++)
       {
         Map[] row = config.renders[i];
         defV.putAll(row[0]);
         
         defV.remove("label");
         defV.remove("name");
         for (int j = 0; j < row.length; j++)
         {
           Map<String, Object> col = row[j];
           for (Map.Entry<String, Object> entry : defV.entrySet()) {
             if (col.get(entry.getKey()) == null) {
               col.put((String)entry.getKey(), entry.getValue());
             }
           }
           if (!col.containsKey("label")) {
             col.put("label", 
               col.get("name"));
           }
         }
       }
     }
     return config;
   }
   
   private static void normalize(Map<String, Object>[][] source)
   {
     Map[][] arrayOfMap = source;int j = source.length;
     for (int i = 0; i < j; i++)
     {
       Map[] row = arrayOfMap[i];
       for (Map<String, Object> col : row) {
         for (Map.Entry<String, Object> entry : col.entrySet()) {
           if (entry.getValue() != null) {
             if ("align".equals(entry.getKey())) {
               entry.setValue(ImpExpConfig.Align.valueOf(entry
                 .getValue().toString().toUpperCase()));
             } else if ("type".equals(entry.getKey())) {
               entry.setValue(ImpExpConfig.Type.valueOf(entry.getValue()
                 .toString().toUpperCase()));
             } else if ("width".equals(entry.getKey())) {
               entry.setValue(Integer.valueOf(
                 (String)entry.getValue()));
             } else if ("dec".equals(entry.getKey())) {
               entry.setValue(Integer.valueOf(
                 (String)entry.getValue()));
             } else if ("pad".equals(entry.getKey())) {
               if (entry.getValue().toString().length() == 0) {
                 entry.setValue(Character.valueOf(' '));
               } else {
                 entry.setValue(
                   Character.valueOf(entry.getValue().toString().charAt(0)));
               }
             }
           }
         }
       }
     }
   }
   
   public static Map<String, Object>[][] parseRenderStr(String source)
   {
     Map[][] rst = (Map[][])null;
     if ((source == null) || (source.trim().length() == 0)) {
       return null;
     }
     source = source.replaceAll("\n", "");
     try
     {
       String[] rows = source.split("\\|\\|");
       String name = "name";
       rst = new Map[rows.length][];
       for (int i = 0; i < rows.length; i++)
       {
         String[] cols = rows[i].split("\\|");
         Map[] row_rst = new Map[cols.length];
         for (int j = 0; j < cols.length; j++)
         {
           Map<String, Object> col_rst = new HashMap();
           String[] attrs = cols[j].split("\\*");
           col_rst.put(name, attrs[0]);
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
       throw new IllegalArgumentException("格式不合法", e);
     }
     return rst;
   }
   
   protected static List<String> parseSql(String sql)
   {
     if (sql == null) {
       return null;
     }
     if (LOG.isDebugEnabled()) {
       LOG.debug("解析前语句：" + sql);
     }
     LinkedList<String> paramList = new LinkedList();
     Matcher m = uploadSqlPattern.matcher(sql);
     while (m.find()) {
       paramList.add(m.group(1));
     }
     if (LOG.isDebugEnabled()) {
       LOG.debug("入参列表：" + paramList.toString());
     }
     String parsedSql = m.replaceAll("?");
     if (LOG.isDebugEnabled()) {
       LOG.debug("解析后语句：" + parsedSql);
     }
     paramList.addFirst(parsedSql);
     return paramList;
   }
   
   public static Object[] selectArguments(Map<String, Object> source, List<String> paramNames)
   {
     int len = paramNames.size();
     Object[] rst = new Object[len];
     Iterator<String> it = paramNames.iterator();
     for (int i = 0; i < len; i++) {
       rst[i] = source.get(it.next());
     }
     return rst;
   }
   
   public ImpExpAbstract(ImpExpConfig impExpConfig)
   {
     this.impExpConfig = impExpConfig;
   }
   
   public int[] getHashTypes(List<String> params)
   {
     int[] types = new int[params.size()];
     Arrays.fill(types, 12);
     return types;
   }
   
   protected Map<String, String>[] getLineRevertDict(String[] colAlias, Map<String, Map<String, String>> revertDict)
     throws AppException
   {
     Map[] lineRevertDict = new Map[colAlias.length];
     for (int i = 0; i < colAlias.length; i++) {
       if (revertDict.containsKey(colAlias[i]))
       {
         lineRevertDict[i] = ((Map)revertDict.get(colAlias[i]));
         if (lineRevertDict[i] == null) {
           throw new AppException("获取不到类别为 的字典数据{0}，请检查字典配置", null, new Object[] {
             colAlias[i] });
         }
       }
     }
     return lineRevertDict;
   }
   
   protected Map<String, Map<String, String>> getRevertDict()
   {
     Map<String, Map<String, String>> revertDict = new HashMap(this.dictMap.size());
     String AAA102 = "AAA102";
     String AAA103 = "AAA103";
     if ((this.dictMap != null) && (!this.dictMap.isEmpty()))
     {
       Iterator localIterator1 = this.dictMap.entrySet().iterator();
       while (localIterator1.hasNext())
       {
         Map.Entry<String, Map<String, Map<String, String>>> dictEntry = (Map.Entry)localIterator1.next();
         if ((dictEntry.getValue() == null) || 
           (((Map)dictEntry.getValue()).isEmpty()))
         {
           revertDict.put((String)dictEntry.getKey(), null);
         }
         else
         {
           Map<String, String> valueMap = new HashMap();
           for (Map<String, String> source : ((Map)dictEntry.getValue()).values()) {
             valueMap.put((String)source.get("AAA103"), (String)source.get("AAA102"));
           }
           revertDict.put((String)dictEntry.getKey(), valueMap);
         }
       }
     }
     return revertDict;
   }
   
   public int[] getSqlTypes(List<String> params)
   {
     int[] types = new int[params.size()];
     Arrays.fill(types, 12);
     return types;
   }
   
   public Object importFile(File excelFile, ImpExpParameter excelParameter)
     throws AppException
   {
     return importFile(excelFile, excelParameter, new CommUploadWork());
   }
   
   public Object importFile(File uploadFile, ImpExpParameter excelParameter, IUploadWork work)
     throws AppException
   {
     InputStream in = null;
     try
     {
       in = new FileInputStream(uploadFile);
       return importFile(in, excelParameter, work);
     }
     catch (FileNotFoundException e)
     {
       throw new AppException("上传文件读取失败", e);
     }
     finally
     {
       if (in != null)
       {
         FileService.release(new Closeable[] {in });
         in = null;
       }
     }
   }
   
   public abstract Object importFile(InputStream paramInputStream, ImpExpParameter paramImpExpParameter, IUploadWork paramIUploadWork)
     throws AppException;
   
   public void prepareDictData(Map<String, String> dyndictWhereCls)
     throws AppException
   {
     if (dyndictWhereCls == null) {
       dyndictWhereCls = Collections.EMPTY_MAP;
     }
     this.dictMap = extractDicts(this.impExpConfig.getDictInfo(), dyndictWhereCls);
   }
   
   public String replaceDictValue(StringBuilder jysm, Object row, String[] colAlias, Map<String, String>[] lineRevertDict)
   {
     String jyzt = "1";
     String JYZT_SB = "2";
     String JYSM_SB_SUF = "取值有误";
     for (int colIndex = 0; colIndex < lineRevertDict.length; colIndex++)
     {
       Map<String, String> dict = lineRevertDict[colIndex];
       if (dict != null)
       {
         Object colValue = Array.get(row, colIndex);
         if ((colValue != null) && (
           (!(colValue instanceof String)) || 
           (((String)colValue).length() != 0))) {
           if (!dict.containsKey(colValue))
           {
             jyzt = "2";
             
             jysm.append(colAlias[colIndex]).append(':').append(colValue)
               .append(',');
             
             Array.set(row, colIndex, null);
           }
           else
           {
             Array.set(row, colIndex, dict.get(colValue));
           }
         }
       }
     }
     if ("2".equals(jyzt)) {
       jysm.deleteCharAt(jysm.length() - 1).append("取值有误");
     }
     return jyzt;
   }
   
   public Map prepareData(IDTO dto)
     throws AppException
   {
     String commDo = (String)dto.getValue("_commDo");
     if (Util.isEmpty(commDo)) {
       return Collections.EMPTY_MAP;
     }
     CommService commService = (CommService)Util.getBean("commService", CommService.class);
     try
     {
       commService.doService(dto);
     }
     catch (Throwable e)
     {
       throw new AppException(e.getLocalizedMessage(), e);
     }
     return dto.getData();
   }
   
   public static class ImpExpParameter
   {
     public Map<String, Object> addData;
     public String configId;
     public Map<String, String> dyndictWhereCls;
     public String[] hashWhereCls;
     public String jbr;
     public String[] listWhereCls;
     public String xtjgdm;
   }
 }