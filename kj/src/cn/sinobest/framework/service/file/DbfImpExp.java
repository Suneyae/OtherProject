 package cn.sinobest.framework.service.file;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IUploadWork;
 import cn.sinobest.framework.util.Util;
 import java.io.Closeable;
 import java.io.DataInputStream;
 import java.io.EOFException;
 import java.io.File;
 import java.io.IOException;
 import java.io.InputStream;
 import java.io.UnsupportedEncodingException;
 import java.sql.ResultSet;
 import java.sql.ResultSetMetaData;
 import java.sql.SQLException;
 import java.util.Arrays;
 import java.util.Enumeration;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import org.apache.log4j.Logger;
 import org.springframework.dao.DataAccessException;
 import org.springframework.jdbc.core.JdbcTemplate;
 import org.springframework.jdbc.core.ResultSetExtractor;
 
 public class DbfImpExp
   extends ImpExpAbstract
   implements ResultSetExtractor<Object>
 {
   private DBFWriter dBFWriter;
   private JdbcTemplate jdbcTemplate;
   private JDBField[] fields;
   private byte[] nextRecord;
   private DataInputStream stream;
   
   public DbfImpExp(ImpExpConfig config)
   {
     super(config);
     this.stream = null;
     this.fields = null;
     this.nextRecord = null;
   }
   
   public void close()
   {
     this.nextRecord = null;
     this.dBFWriter.streamClose();
     if (this.dictMap != null) {
       this.dictMap.clear();
     }
     FileService.release(new Closeable[] {this.stream });
   }
   
   public JDBField getField(int i)
   {
     return this.fields[i];
   }
   
   public int getFieldCount()
   {
     return this.fields.length;
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
           types[index] = 
             ((ImpExpConfig.Type)col.get("type")).sqlType;
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
   
   public int[] getSqlTypes(List<String> params)
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
           types[index] = 
             ((ImpExpConfig.Type)col.get("type")).sqlType;
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
   
   public boolean hasNextRecord()
   {
     return this.nextRecord != null;
   }
   
   public Object importFile(InputStream fileStream, ImpExpAbstract.ImpExpParameter excelParameter, IUploadWork work)
     throws AppException
   {
     Map<String, Object> hashMap = new HashMap(
       excelParameter.addData);
     init(fileStream);
     
     Enumeration<Map<String, Object>> numE = new Enumeration()
     {
       public boolean hasMoreElements()
       {
         return DbfImpExp.this.hasNextRecord();
       }
       
       public Map<String, Object> nextElement()
       {
         Map<String, Object> rowMap = new HashMap();
         
         Object[] row = DbfImpExp.this.nextRecord();
         for (int index = 0; index < row.length; index++) {
           rowMap.put(DbfImpExp.this.getField(index).getName(), 
             row[index]);
         }
         return rowMap;
       }
     };
     return work.doWork(this.impExpConfig, hashMap, numE);
   }
   
   private void init(InputStream inputstream)
     throws AppException
   {
     try
     {
       this.stream = new DataInputStream(inputstream);
       int i = readHeader();
       if (i < this.impExpConfig.renders.length) {
         throw new AppException("数据行格式的长度不符合要求！（ID={0}）", null, new Object[] {
           this.impExpConfig.getId() });
       }
       this.fields = new JDBField[i];
       int j = 1;
       for (int k = 0; k < i; k++)
       {
         this.fields[k] = readFieldHeader();
         j += this.fields[k].getLength();
       }
       if (this.stream.read() < 1) {
         throw new AppException("文件异常结束");
       }
       this.nextRecord = new byte[j];
       try
       {
         this.stream.readFully(this.nextRecord);
       }
       catch (EOFException eofexception)
       {
         this.nextRecord = null;
         this.stream.close();
       }
       return;
     }
     catch (IOException ioexception)
     {
       throw new AppException("文件读取失败", ioexception);
     }
   }
   
   public Object[] nextRecord()
     throws AppException
   {
     if (!hasNextRecord()) {
       throw new AppException("无有更多的数据可读！");
     }
     Object[] aobj = new Object[this.fields.length];
     int i = 1;
     for (int j = 0; j < aobj.length; j++)
     {
       int k = this.fields[j].getLength();
       StringBuffer stringbuffer = new StringBuffer(k);
       byte[] b = new byte[k];
       System.arraycopy(this.nextRecord, i, b, 0, k);
       try
       {
         stringbuffer.append(new String(b, "GBK"));
       }
       catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
       aobj[j] = this.fields[j].parse(nvl(stringbuffer).toString());
       i += this.fields[j].getLength();
     }
     try
     {
       this.stream.readFully(this.nextRecord);
     }
     catch (EOFException eofexception)
     {
       this.nextRecord = null;
     }
     catch (IOException ioexception)
     {
       throw new AppException("文件读取失败", ioexception);
     }
     return aobj;
   }
   
   public StringBuffer nvl(StringBuffer stringbuffer)
   {
     if (stringbuffer == null) {
       stringbuffer = new StringBuffer();
     }
     return stringbuffer;
   }
   
   private JDBField readFieldHeader()
     throws AppException
   {
     byte[] abyte0 = new byte[16];
     try
     {
       this.stream.readFully(abyte0);
     }
     catch (IOException eofexception)
     {
       throw new AppException("文件异常结束");
     }
     StringBuffer stringbuffer = new StringBuffer(10);
     for (int i = 0; i < 10; i++)
     {
       if (abyte0[i] == 0) {
         break;
       }
       stringbuffer.append((char)abyte0[i]);
     }
     char c = (char)abyte0[11];
     try
     {
       this.stream.readFully(abyte0);
     }
     catch (IOException eofexception1)
     {
       throw new AppException("文件异常结束");
     }
     int j = abyte0[0];
     int k = abyte0[1];
     if (j < 0) {
       j += 256;
     }
     if (k < 0) {
       k += 256;
     }
     return new JDBField(stringbuffer.toString(), c, j, k);
   }
   
   private int readHeader()
     throws AppException
   {
     byte[] abyte0 = new byte[16];
     try
     {
       this.stream.readFully(abyte0);
     }
     catch (IOException eofexception)
     {
       throw new AppException("文件异常结束");
     }
     int i = abyte0[8];
     if (i < 0) {
       i += 256;
     }
     i += 256
     i--;i /= 32;
     i--;
     try
     {
       this.stream.readFully(abyte0);
     }
     catch (IOException eofexception1)
     {
       throw new AppException("文件异常结束");
     }
     return i;
   }
   
   public File exportDbf(ImpExpAbstract.ImpExpParameter dbfParameter)
     throws AppException, IOException
   {
     File dbfFile = null;
     try
     {
       prepareDictData(dbfParameter.dyndictWhereCls);
       
       dbfFile = File.createTempFile("dbf", ".dbf");
       LOG.info("生成临时文件:" + dbfFile.getAbsolutePath());
       
       JDBField[] jDBField = prepareDbfHeaderData();
       this.dBFWriter = new DBFWriter(dbfFile, jDBField, "GBK");
       
 
       fillListData(dbfParameter.listWhereCls, dbfParameter.addData);
       this.dBFWriter.close();
       
 
 
 
       return dbfFile;
     }
     catch (Exception ex)
     {
       LOG.error(ex.getMessage(), ex);
       throw new AppException(ex.getLocalizedMessage(), ex);
     }
     finally
     {
       close();
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
         LOG.debug("开始向dbf写入列表数据：" + query.toString());
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
   
   private JDBField[] prepareDbfHeaderData()
     throws AppException
   {
     Map[] render = this.impExpConfig.renders[0];
     JDBField[] fields = new JDBField[render.length];
     int i = 0;
     for (Map<String, Object> entry : render)
     {
       Integer width = (Integer)entry.get("width");
       Integer dec = (Integer)entry.get("dec");
       dec = Integer.valueOf(dec == null ? 0 : dec.intValue());
       ImpExpConfig.Type type = (ImpExpConfig.Type)entry.get("type");
       if (width == null) {
         throw new AppException(entry.get("name") + "未指定width属性");
       }
       fields[(i++)] = new JDBField((String)entry.get("name"), ((Character)ImpExpConfig.TYPE2DBF.get(type)).charValue(), width.intValue(), dec.intValue());
     }
     return fields;
   }
   
   protected JdbcTemplate getJdbcTemplate()
   {
     if (this.jdbcTemplate == null) {
       this.jdbcTemplate = ((JdbcTemplate)Util.getBean("jdbcTemplate"));
     }
     return this.jdbcTemplate;
   }
   
   public Object extractData(ResultSet rs)
     throws SQLException, DataAccessException
   {
     ResultSetMetaData rsmd = rs.getMetaData();
     int colCount = rsmd.getColumnCount();
     String[] colNames = new String[colCount];
     for (int i = 0; i < colCount; i++)
     {
       int j = i + 1;
       colNames[i] = rsmd.getColumnLabel(j);
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
         Object v = rs.getObject(i + 1);
         if (colDict[i] != null)
         {
           if (colDict[i].get(v) == null) {
             throw new AppException("第{0}列未找到代码值为{1}的字典项!", null, new Object[] { Integer.valueOf(i + 1), String.valueOf(v) });
           }
           v = ((Map)colDict[i].get(v)).get("AAA103");
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
     Object[] record = format(this.impExpConfig.renders[0], oneRow);
     this.dBFWriter.addRecord(record);
   }
   
   private Object[] format(Map<String, Object>[] format, Map<String, Object> data)
   {
     Object[] record = new Object[format.length];
     int i = 0;
     for (Map<String, Object> entry : format) {
       record[(i++)] = data.get(entry.get("name"));
     }
     return record;
   }
 }