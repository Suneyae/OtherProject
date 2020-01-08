 package cn.sinobest.framework.service.file;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IDAO;
 import cn.sinobest.framework.comm.iface.IUploadWork;
 import cn.sinobest.framework.util.Util;
 import java.io.BufferedReader;
 import java.io.BufferedWriter;
 import java.io.Closeable;
 import java.io.File;
 import java.io.FileNotFoundException;
 import java.io.FileOutputStream;
 import java.io.IOException;
 import java.io.InputStream;
 import java.io.InputStreamReader;
 import java.io.OutputStreamWriter;
 import java.nio.charset.Charset;
 import java.sql.ResultSet;
 import java.sql.ResultSetMetaData;
 import java.sql.SQLException;
 import java.util.Arrays;
 import java.util.Enumeration;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import java.util.Map.Entry;
 import org.apache.log4j.Logger;
 import org.springframework.dao.DataAccessException;
 import org.springframework.jdbc.core.JdbcTemplate;
 import org.springframework.jdbc.core.ResultSetExtractor;
 
 public class TextImpExp
   extends ImpExpAbstract
   implements ResultSetExtractor<Object>, Closeable
 {
   private static final Logger LOG = Logger.getLogger(ExcelImpExp.class);
   private File txtFile;
   private IDAO commDAO;
   private JdbcTemplate jdbcTemplate;
   private BufferedWriter bw;
   
   public TextImpExp(ImpExpConfig config)
   {
     super(config);
   }
   
   public void close()
   {
     if (this.dictMap != null) {
       this.dictMap.clear();
     }
     this.impExpConfig = null;
   }
   
   private Map<String, Object> extractOneLine(ImpExpConfig config, Map<String, Object>[] lineConfig, String[] colAlias, Map<String, String>[] lineRevertDict, String line, Map<String, Object> hashData)
   {
     String[] headers = (String[])null;
     if (config.getDelimiter() != null)
     {
       headers = (line + " ").split(config.getDelimiter());
     }
     else
     {
       int startPos = 0;int colLen = 0;
       headers = new String[lineConfig.length];
       for (int i = 0; i < headers.length; i++)
       {
         Map<String, Object> col = lineConfig[i];
         colLen = ((Integer)col.get("width")).intValue();
         if (startPos >= line.length()) {
           break;
         }
         String sequence = line.substring(startPos, 
           Math.min(startPos + colLen, line.length()));
         char pad = ((Character)col.get("pad")).charValue();
         if (ImpExpConfig.Align.LEFT == col.get("align")) {
           for (int i_end = sequence.length() - 1; i_end >= 0; i_end--) {
             if (sequence.charAt(i_end) != pad)
             {
               headers[i] = sequence.substring(0, i_end + 1);
               break;
             }
           }
         } else {
           for (int i_start = 0; i_start < sequence.length(); i_start++) {
             if (sequence.charAt(i_start) != pad)
             {
               headers[i] = sequence.substring(i_start);
               break;
             }
           }
         }
         startPos += colLen;
       }
     }
     StringBuilder jysm = new StringBuilder();
     String jyzt = "1";
     if (headers.length < colAlias.length) {
       throw new AppException("数据行格式的长度不符合要求！（ID={0}）", null, new Object[] {
         this.impExpConfig.getId() });
     }
     jyzt = replaceDictValue(jysm, headers, colAlias, 
       lineRevertDict);
     
 
     Map<String, Object> lineData = new HashMap();
     for (int i = 0; i < headers.length; i++)
     {
       Map<String, Object> col = lineConfig[i];
       lineData.put((String)col.get("name"), 
         ((ImpExpConfig.Type)col.get("type")).eval(headers[i]));
     }
     lineData.put("JYSM", jysm.toString());
     lineData.put("JYZT", jyzt);
     return lineData;
   }
   
   public int[] getHashTypes(List<String> params)
   {
     int[] types = new int[params.size()];
     
     Map[] header = this.impExpConfig.renders[0];
     int index = 0;
     for (String colName : params)
     {
       boolean found = false;
       for (Map<String, Object> col : header) {
         if (colName.equals(col.get("name")))
         {
           types[index] = ((ImpExpConfig.Type)col.get("type")).sqlType;
           found = true;
           break;
         }
       }
       if (!found) {
         types[index] = 12;
       }
       index++;
     }
     return types;
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
   
   public int[] getSqlTypes(List<String> params)
   {
     int[] types = new int[params.size()];
     
     Map[] header = this.impExpConfig.renders.length > 1 ? this.impExpConfig.renders[1] : 
       this.impExpConfig.renders[0];
     int index = 0;
     for (String colName : params)
     {
       boolean found = false;
       for (Map<String, Object> col : header) {
         if (colName.equals(col.get("name")))
         {
           types[index] = ((ImpExpConfig.Type)col.get("type")).sqlType;
           found = true;
           break;
         }
       }
       if (!found) {
         types[index] = 12;
       }
       index++;
     }
     return types;
   }
   
   public Object importFile(InputStream inputStream, ImpExpAbstract.ImpExpParameter excelParameter, IUploadWork work)
     throws AppException
   {
     if ((this.impExpConfig.getRenders() == null) || 
       (this.impExpConfig.getRenders().length < 1)) {
       throw new AppException("文本导入必须先定义模板行");
     }
     if (this.impExpConfig.getRenders().length > 2) {
       throw new AppException("文本导入只能最多定义两行模板行");
     }
     prepareDictData(excelParameter.dyndictWhereCls);
     
     Map<String, Map<String, String>> revertDict = getRevertDict();
     BufferedReader reader = null;
     Map<String, Object> hashMap = new HashMap(
       excelParameter.addData);
     try
     {
       reader = new BufferedReader(new InputStreamReader(inputStream, 
         Charset.forName("GBK")));
       
       final Map<String, Object> headerMap = readHeader(this.impExpConfig, 
         reader, revertDict, excelParameter.addData);
       if ((headerMap != null) && (!headerMap.isEmpty())) {
         hashMap.putAll(headerMap);
       }
       final Map[] lineConfig = this.impExpConfig.renders.length > 1 ? this.impExpConfig.renders[1] : 
         this.impExpConfig.renders[0];
       
       String[] colAlias = new String[lineConfig.length];
       final String[] colLabels = new String[lineConfig.length];
       for (int i = 0; i < colAlias.length; i++)
       {
         colAlias[i] = ((String)lineConfig[i].get("name"));
         colLabels[i] = ((String)lineConfig[i].get("label"));
       }
       final Map[] lineRevertDict = getLineRevertDict(
         colAlias, revertDict);
       final BufferedReader bReader = reader;
       
       work.doWork(this.impExpConfig, hashMap, 
         new Enumeration()
         {
           String line;
           
           public boolean hasMoreElements()
           {
             try
             {
               this.line = bReader.readLine();
             }
             catch (IOException e)
             {
               throw new AppException("导入文件读取失败", e);
             }
             return this.line != null;
           }
           
           public Map<String, Object> nextElement()
           {
             return TextImpExp.this.extractOneLine(TextImpExp.this.impExpConfig, 
               lineConfig, colLabels, lineRevertDict, this.line, 
               headerMap);
           }
         });
     }
     catch (FileNotFoundException e)
     {
       throw new AppException("导入文件读取失败", e);
     }
     catch (IOException e)
     {
       throw new AppException("导入文件读取失败", e);
     }
     finally
     {
       if (reader != null)
       {
         FileService.release(new Closeable[] {reader });
         reader = null;
       }
     }
   }
   
   private Map<String, Object> readHeader(ImpExpConfig config, BufferedReader reader, Map<String, Map<String, String>> revertDict, Map<String, Object> outterData)
     throws IOException
   {
     if (config.renders.length < 2) {
       return null;
     }
     String line = reader.readLine();
     if (line == null) {
       throw new IllegalArgumentException("没有表头数据");
     }
     String[] colAlias = new String[config.renders[0].length];
     for (int index = 0; index < colAlias.length; index++) {
       colAlias[index] = ((String)config.renders[0][index].get("label"));
     }
     Map[] lineRevertDict = getLineRevertDict(colAlias, 
       revertDict);
     Map<String, Object> hashData = extractOneLine(config, 
       config.renders[0], colAlias, lineRevertDict, line, outterData);
     
     return hashData;
   }
   
   public File exportTxt(ImpExpAbstract.ImpExpParameter txtParameter)
     throws AppException, IOException, AppException
   {
     OutputStreamWriter os = null;
     try
     {
       prepareDictData(txtParameter.dyndictWhereCls);
       
 
       Map<String, Object> hashData = getHashData(txtParameter.hashWhereCls, txtParameter.addData);
       this.txtFile = File.createTempFile("txt", ".txt");
       LOG.info("生成临时文件:" + this.txtFile.getAbsolutePath());
       os = new OutputStreamWriter(new FileOutputStream(this.txtFile), "GBK");
       this.bw = new BufferedWriter(os);
       
 
       writeHeader(this.bw, hashData);
       
       fillListData(txtParameter.listWhereCls, hashData);
       
 
 
 
       return this.txtFile;
     }
     catch (Exception ex)
     {
       LOG.error(ex.getMessage(), ex);
       throw new AppException(ex.getLocalizedMessage(), ex);
     }
     finally
     {
       this.bw.flush();
       this.bw.close();
       os.close();
     }
   }
   
   protected void fillListData(String[] listWhereCls, Map<String, Object> hashData)
   {
     String[] sqlCls = this.impExpConfig.getSqlStr();
     if ((sqlCls == null) || (sqlCls.length == 0)) {
       return;
     }
     if ((listWhereCls == null) || (listWhereCls.length < 1)) {
       listWhereCls = new String[sqlCls.length];
     }
     for (int i = 0; i < Math.min(sqlCls.length, listWhereCls.length); i++)
     {
       StringBuilder query = new StringBuilder(sqlCls[i]);
       if ((listWhereCls[i] != null) && (listWhereCls[i].trim().length() > 0)) {
         query.append(" where ").append(listWhereCls[i]);
       } else {
         query.append(" where 1=2");
       }
       if (LOG.isDebugEnabled()) {
         LOG.debug("开始向txt写入列表数据：" + query.toString());
       }
       List<String> params = parseSql(query.toString());
       String parsedSql = (String)params.remove(0);
       
       Object[] preparedParams = ImpExpAbstract.selectArguments(hashData, 
         params);
       int[] types = new int[preparedParams.length];
       Arrays.fill(types, 12);
       
       getJdbcTemplate().query(parsedSql, preparedParams, types, this);
     }
   }
   
   public Object extractData(ResultSet rs)
     throws SQLException, DataAccessException
   {
     ResultSetMetaData rsmd = rs.getMetaData();
     int colCount = rsmd.getColumnCount();
     String[] colNames = new String[colCount];
     for (int i = 0; i < colCount; i++) {
       colNames[i] = rsmd.getColumnLabel(i + 1);
     }
     Map[] colDict = new Map[colCount];
     if ((this.dictMap != null) && (!this.dictMap.isEmpty())) {
       for (int i = 0; i < colNames.length; i++) {
         if (this.dictMap.containsKey(colNames[i])) {
           colDict[i] = ((Map)this.dictMap.get(colNames[i]));
         }
       }
     }
     Map<String, Object> rowMap = new HashMap(colCount);
     while (rs.next())
     {
       for (int i = 0; i < colCount; i++)
       {
         String v = rs.getString(i + 1);
         if (colDict[i] != null)
         {
           if (colDict[i].get(v) == null) {
             throw new AppException("第{0}列未找到代码值为{1}的字典项!", null, new Object[] { Integer.valueOf(i + 1), String.valueOf(v) });
           }
           v = (String)((Map)colDict[i].get(v)).get("AAA103");
         }
         rowMap.put(colNames[i], v);
       }
       try
       {
         addRowData(rowMap);
       }
       catch (IOException e)
       {
         SQLException e2 = new SQLException(e.getLocalizedMessage());
         e2.initCause(e);
         throw e2;
       }
     }
     return null;
   }
   
   private void addRowData(Map<String, Object> oneRow)
     throws IOException
   {
     writeRowData(this.bw, oneRow);
   }
   
   private void writeHeader(BufferedWriter bw, Map<String, Object> data)
     throws IOException
   {
     if ((data != null) && (!data.isEmpty()))
     {
       bw.write(format(this.impExpConfig.renders[0], data));
       bw.flush();
     }
   }
   
   private void writeRowData(BufferedWriter bw, Map<String, Object> data)
     throws IOException
   {
     Map[] renderMap = (Map[])null;
     if (this.impExpConfig.renders.length < 2) {
       renderMap = this.impExpConfig.renders[0];
     } else {
       renderMap = this.impExpConfig.renders[1];
     }
     bw.write("\r\n");
     bw.write(format(renderMap, data));
     bw.flush();
   }
   
   private String format(Map<String, Object>[] format, Map<String, Object> data)
   {
     StringBuffer formatRs = new StringBuffer(data.size() + 100);
     for (Map<String, Object> entry : format)
     {
       String val = (String)data.get(entry.get("name"));
       ImpExpConfig.Align align = (ImpExpConfig.Align)entry.get("align");
       Integer width = (Integer)entry.get("width");
       if (val == null) {
         val = "";
       }
       if ((width != null) && (width.intValue() > 0)) {
         if (align == ImpExpConfig.Align.RIGHT) {
           val = Util.lpad(val, width.intValue(), String.valueOf(entry.get("pad")), true);
         } else {
           val = Util.rpad(val, width.intValue(), String.valueOf(entry.get("pad")), true);
         }
       }
       formatRs.append(val).append(this.impExpConfig.getDelimiter());
     }
     return formatRs.toString().substring(0, formatRs.length() - 1);
   }
   
   public static Map<String, Object> toStringMap(Map<String, Object> source)
   {
     Map<String, Object> rst = new HashMap(
       source.size()
     for (Map.Entry<String, Object> entry : source.entrySet()) {
       if (entry.getValue() == null) {
         rst.put((String)entry.getKey(), null);
       } else {
         rst.put((String)entry.getKey(), entry.getValue().toString());
       }
     }
     return rst;
   }
   
   protected Map<String, Object> getHashData(String[] whereCls, Map<String, Object> data)
   {
     Map<String, Object> hashData = null;
     if ((data != null) && (!data.isEmpty())) {
       hashData = toStringMap(data);
     } else {
       hashData = new HashMap();
     }
     if (this.impExpConfig.getHashSqlStr() != null)
     {
       if ((whereCls == null) || (whereCls.length < 1)) {
         whereCls = new String[this.impExpConfig.getHashSqlStr().length];
       }
       for (int i = 0; i < Math.min(this.impExpConfig.getHashSqlStr().length, 
             whereCls.length); i++)
       {
         StringBuilder sql = new StringBuilder(
           this.impExpConfig.getHashSqlStr()[i]);
         if ((whereCls[i] != null) && (whereCls[i].trim().length() > 0)) {
           sql.append(" where ").append(whereCls[i]);
         } else {
           sql.append(" where 1=2");
         }
         List<String> params = parseSql(sql.toString());
         String parsedSql = (String)params.remove(0);
         
         Object[] preparedParams = selectArguments(hashData, params);
         int[] types = new int[preparedParams.length];
         Arrays.fill(types, 12);
         
         List<Map<String, Object>> list = getJdbcTemplate()
           .queryForList(parsedSql.toString(), preparedParams, 
           types);
         if (!list.isEmpty()) {
           hashData.putAll(toStringMap((Map)list.get(0)));
         }
       }
     }
     return hashData;
   }
 }