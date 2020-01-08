 package cn.sinobest.framework.service.file;
 
 import cn.sinobest.framework.service.file.csv.CsvWriter;
 import cn.sinobest.framework.service.tags.Glt;
 import cn.sinobest.framework.service.tags.GltService;
 import cn.sinobest.framework.util.Util;
 import java.io.BufferedOutputStream;
 import java.io.Closeable;
 import java.io.File;
 import java.io.FileOutputStream;
 import java.io.IOException;
 import java.sql.ResultSet;
 import java.sql.ResultSetMetaData;
 import java.sql.SQLException;
 import java.util.Collection;
 import java.util.Map;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 import org.apache.log4j.Logger;
 import org.springframework.dao.DataAccessException;
 import org.springframework.jdbc.core.JdbcTemplate;
 import org.springframework.jdbc.core.ResultSetExtractor;
 
 public class CSVExp
   implements ResultSetExtractor<Object>, Closeable
 {
   static Logger log = Logger.getLogger(CSVExp.class);
   private File csvFile;
   private CsvWriter csvWriter;
   private Map<String, Map<String, Map<String, String>>> dictMap;
   private Collection<Integer> checkboxIndices;
   
   public File exportCSV(Glt glt, String whereCls, Map<String, String> dyndictWhereCls)
     throws IOException
   {
     this.dictMap = GltService.prepareDictData(glt, dyndictWhereCls);
     try
     {
       this.csvFile = File.createTempFile("csv", ".csv");
       this.csvWriter = new CsvWriter(new BufferedOutputStream(
         new FileOutputStream(this.csvFile)));
       
       this.checkboxIndices = GltService.listCheckBoxIndices(glt);
       
       writeHeader(glt);
       
       JdbcTemplate template = getJdbcTemplate();
       StringBuilder query = new StringBuilder(glt.getSelectStr());
       if ((whereCls != null) && (whereCls.trim().length() > 0)) {
         query.append(" where ").append(whereCls);
       }
       if (log.isDebugEnabled()) {
         log.debug("开始向CSV写入列表数据：" + query.toString());
       }
       template.query(query.toString(), this);
     }
     finally
     {
       close();
     }
     return this.csvFile;
   }
   
   private void writeHeader(Glt glt)
     throws IOException
   {
     Map[] lastRow = GltService.lastRow(glt);
     String[] header = new String[lastRow.length];
     
     Pattern p_nbsp = Pattern.compile("&nbsp;", 16);
     Pattern p_br = Pattern.compile("<br/>", 16);
     for (int i = 0; i < lastRow.length; i++)
     {
       String name = (String)lastRow[i].get("title");
       if ((name != null) && (name.length() > 0))
       {
         name = p_nbsp.matcher(name).replaceAll(" ");
         name = p_br.matcher(name).replaceAll("\n");
       }
       header[i] = name;
     }
     this.csvWriter.writeRecord(header);
   }
   
   protected static JdbcTemplate getJdbcTemplate()
   {
     return (JdbcTemplate)Util.getBean("jdbcTemplate");
   }
   
   public void processRow(ResultSet rs)
     throws SQLException
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
     String[] oneRow = new String[colCount];
     while (rs.next())
     {
       for (int i = 0; i < colCount; i++)
       {
         String v = rs.getString(i + 1);
         if (colDict[i] != null) {
           v = (String)((Map)colDict[i].get(v)).get("AAA103");
         }
         oneRow[i] = v;
       }
       try
       {
         addRowData(oneRow);
       }
       catch (IOException e)
       {
         SQLException e2 = new SQLException(e.getLocalizedMessage());
         e2.initCause(e);
         throw e2;
       }
     }
   }
   
   private void addRowData(String[] oneRow)
     throws IOException
   {
     for (Integer j : this.checkboxIndices) {
       oneRow[(j.intValue() - 1)] = null;
     }
     this.csvWriter.writeRecord(oneRow);
   }
   
   public void close()
     throws IOException
   {
     if (this.csvWriter != null)
     {
       this.csvWriter.close();
       this.csvWriter = null;
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
     String[] oneRow = new String[colCount];
     while (rs.next())
     {
       for (int i = 0; i < colCount; i++)
       {
         String v = rs.getString(i + 1);
         if ((colDict[i] != null) && (v != null) && (colDict[i].get(v) != null)) {
           v = (String)((Map)colDict[i].get(v)).get("AAA103");
         }
         oneRow[i] = v;
       }
       try
       {
         addRowData(oneRow);
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
 }