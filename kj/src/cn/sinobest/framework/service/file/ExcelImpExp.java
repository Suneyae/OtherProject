 package cn.sinobest.framework.service.file;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.service.tags.Glt;
 import cn.sinobest.framework.service.tags.GltService;
 import cn.sinobest.framework.util.ConfUtil;
 import cn.sinobest.framework.util.Util;
 import java.io.BufferedOutputStream;
 import java.io.Closeable;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.FileOutputStream;
 import java.io.IOException;
 import java.io.InputStream;
 import java.io.ObjectInputStream;
 import java.io.ObjectOutputStream;
 import java.io.OutputStream;
 import java.security.SecureRandom;
 import java.sql.ResultSet;
 import java.sql.ResultSetMetaData;
 import java.sql.SQLException;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.Collection;
 import java.util.Collections;
 import java.util.Enumeration;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import java.util.Map.Entry;
 import java.util.NoSuchElementException;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 import org.apache.commons.io.FileUtils;
 import org.apache.log4j.Logger;
 import org.apache.poi.hssf.record.formula.Ptg;
 import org.apache.poi.hssf.record.formula.RefPtgBase;
 import org.apache.poi.hssf.usermodel.HSSFCell;
 import org.apache.poi.hssf.usermodel.HSSFCellStyle;
 import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
 import org.apache.poi.hssf.usermodel.HSSFEvaluationWorkbook;
 import org.apache.poi.hssf.usermodel.HSSFFont;
 import org.apache.poi.hssf.usermodel.HSSFName;
 import org.apache.poi.hssf.usermodel.HSSFRichTextString;
 import org.apache.poi.hssf.usermodel.HSSFRow;
 import org.apache.poi.hssf.usermodel.HSSFSheet;
 import org.apache.poi.hssf.usermodel.HSSFWorkbook;
 import org.apache.poi.hssf.util.HSSFRegionUtil;
 import org.apache.poi.ss.formula.FormulaParser;
 import org.apache.poi.ss.formula.FormulaRenderer;
 import org.apache.poi.ss.usermodel.Cell;
 import org.apache.poi.ss.usermodel.DateUtil;
 import org.apache.poi.ss.usermodel.Name;
 import org.apache.poi.ss.util.AreaReference;
 import org.apache.poi.ss.util.CellRangeAddress;
 import org.apache.poi.ss.util.CellReference;
 import org.springframework.dao.DataAccessException;
 import org.springframework.jdbc.core.JdbcTemplate;
 import org.springframework.jdbc.core.ResultSetExtractor;
 
 public class ExcelImpExp
   extends ImpExpAbstract
   implements ResultSetExtractor<Object>
 {
   protected static final Logger log = Logger.getLogger(ExcelImpExp.class);
   protected static final String LIST_NAME_PREFIX = "列表";
   
   static class GltExcelExp
     extends ExcelImpExp
   {
     public static final String ALIGN = "align";
     private static final long serialVersionUID = 1L;
     private Collection<Integer> checkboxIndices;
     private Glt glt;
     
     public static void fillHeader(HSSFSheet sheet, Map<String, Object>[][] fixHeader, Glt glt)
     {
       for (int i = 0; i < fixHeader.length; i++) {
         sheet.createRow(i);
       }
       int[] startIndices = new int[fixHeader.length];
       
       int[] todoIndices = new int[fixHeader.length];
       
       HSSFCellStyle cellStyle = getHeaderStyle(sheet.getWorkbook());
       
       Pattern p_nbsp = Pattern.compile("&nbsp;", 16);
       Pattern p_br = Pattern.compile("<br/>", 16);
       do
       {
         for (int rowIndex = 0; rowIndex < fixHeader.length; rowIndex++) {
           if (todoIndices[rowIndex] >= 0)
           {
             Map<String, Object> cell = fixHeader[rowIndex][todoIndices[rowIndex]];
             HSSFRow row = sheet.getRow(rowIndex);
             
             int colspan = 1;
             if (cell.get("colspan") != null) {
               colspan = Integer.valueOf(
                 (String)cell.get("colspan")).intValue();
             }
             int rowspan = 1;
             if (cell.get("rowspan") != null) {
               rowspan = Integer.valueOf(
                 (String)cell.get("rowspan")).intValue();
             }
             if ((colspan < 1) || (rowspan < 1)) {
               throw new IllegalArgumentException(
                 "参数非法，列头colspan或者rowSpan都不能小于1");
             }
             String name = (String)cell.get("title");
             if ((name != null) && (name.length() > 0))
             {
               name = p_nbsp.matcher(name).replaceAll(" ");
               name = p_br.matcher(name).replaceAll("\n");
             }
             HSSFCell hssfCell = row.createCell(startIndices[rowIndex]);
             hssfCell.setCellValue(name);
             hssfCell.setCellStyle(cellStyle);
             if ((colspan > 1) || (rowspan > 1))
             {
               CellRangeAddress merge = new CellRangeAddress(rowIndex, 
                 rowIndex + rowspan - 1, startIndices[rowIndex], 
                 startIndices[rowIndex] + colspan - 1);
               HSSFRegionUtil.setBorderBottom(
                 2, merge, sheet, 
                 sheet.getWorkbook());
               HSSFRegionUtil.setBorderLeft(
                 2, merge, sheet, 
                 sheet.getWorkbook());
               HSSFRegionUtil.setBorderRight(
                 2, merge, sheet, 
                 sheet.getWorkbook());
               HSSFRegionUtil.setBorderTop(
                 2, merge, sheet, 
                 sheet.getWorkbook());
               sheet.addMergedRegion(merge);
             }
             for (int i = rowIndex; i < Math.min(rowIndex + rowspan, 
                   fixHeader.length); i++) {
               startIndices[i] += colspan;
             }
             if (todoIndices[rowIndex] += 1 >= fixHeader[rowIndex].length)
             {
               todoIndices[rowIndex] = -1;
               break;
             }
           }
         }
       } while (todoIndices[(fixHeader.length - 1)] >= 0);
     }
     
     public static HSSFCellStyle getHeaderStyle(HSSFWorkbook workbook)
     {
       HSSFCellStyle cellStyle = workbook.createCellStyle();
       HSSFFont font = workbook.createFont();
       font.setBoldweight((short)700);
       cellStyle.setFont(font);
       cellStyle.setBorderBottom((short)2);
       cellStyle.setAlignment((short)2);
       return cellStyle;
     }
     
     public static ImpExpConfig prepareWorkBook(Glt glt)
     {
       String[] sqlStr = new String[1];
       sqlStr[0] = glt.getSelectStr();
       ImpExpConfig excelConfig = new ImpExpConfig();
       excelConfig.setSqlStr(sqlStr);
       excelConfig.setDictInfo(glt.getDict());
       return excelConfig;
     }
     
     private static void setDataRowStyle(HSSFSheet sheet, int rowNum, Glt glt, Map<String, Object>[] lastRow)
     {
       Map<String, HSSFCellStyle> styleMap = new HashMap();
       
       styleMap.put("center", null);
       styleMap.put("left", null);
       styleMap.put("right", null);
       HSSFRow dataRow = sheet.createRow(rowNum);
       int index = -1;
       for (Map<String, Object> cell : lastRow)
       {
         index++;
         String align = "center";
         if (cell.containsKey("align"))
         {
           align = (String)cell.get("align");
           if (!styleMap.containsKey(align)) {
             throw new IllegalArgumentException(
               "参数非法，列头style只能是center,left,right");
           }
         }
         if (styleMap.get(align) == null)
         {
           HSSFCellStyle newStyle = sheet.getWorkbook()
             .createCellStyle();
           switch (align.charAt(0))
           {
           case 'c': 
             newStyle.setAlignment((short)2);
             break;
           case 'l': 
             newStyle.setAlignment((short)3);
             break;
           default: 
             newStyle.setAlignment((short)1);
           }
           styleMap.put(align, newStyle);
         }
         dataRow.createCell(index, 3).setCellStyle((HSSFCellStyle)styleMap.get(align));
         dataRow.getCell(index).setCellType(1);
       }
       Name namedCel2 = sheet.getWorkbook().createName();
       namedCel2.setNameName("列表0");
       
       CellRangeAddress addr = new CellRangeAddress(rowNum, rowNum, 0, 
         lastRow.length - 1);
       namedCel2.setRefersToFormula(sheet.getSheetName() + "!" + 
         addr.formatAsString());
     }
     
     public GltExcelExp(Glt glt)
       throws IOException, InterruptedException
     {
       super();
       this.glt = glt;
     }
     
     public void addRowData(String[] oneRow)
     {
       for (Integer j : this.checkboxIndices) {
         oneRow[(j.intValue() - 1)] = null;
       }
       super.addRowData(oneRow);
     }
     
     protected void readTemplateFile()
       throws AppException
     {
       if (this.workbook != null) {
         return;
       }
       Map[][] merged = GltService.mergeHeader(this.glt);
       Map[] lastRow = GltService.lastRow(this.glt);
       
       this.workbook = new HSSFWorkbook();
       HSSFSheet sheet = this.workbook.createSheet();
       sheet.createFreezePane(this.glt.getFixcolumns(), merged.length);
       fillHeader(sheet, merged, this.glt);
       setDataRowStyle(sheet, merged.length, this.glt, lastRow);
       
       this.checkboxIndices = GltService.listCheckBoxIndices(this.glt);
     }
   }
   
   public static final int MAX_LIST_SIZE_PER_FILE = (int)ConfUtil.getSysParam2Number("template.excel.maxListPerFile", 4000L);
   private static final SecureRandom RANDOM = new SecureRandom();
   public static final String TEMPLATE_LOCATION = ConfUtil.getSysParam(
     "template.location", ".");
   private HSSFDataFormatter _formatter;
   
   public static ExcelImpExp prepareGltExp(Glt glt)
     throws IOException, InterruptedException
   {
     return new GltExcelExp(glt);
   }
   
   private int[] colIndices = null;
   private String currentListName = null;
   private long currentListNumber = 0L;
   private long currentListNumberInFile = -1L;
   private File dataFileTmp = null;
   private int fileNumber = 0;
   private JdbcTemplate jdbcTemplate = null;
   private long listSize = -1L;
   private int namedRowIndex = -1;
   private int rowIndex = -1;
   private int sheetIndex = -1;
   private File tempDir = null;
   protected HSSFWorkbook workbook = null;
   
   public ExcelImpExp(ImpExpConfig config)
   {
     super(config);
   }
   
   protected void addRowData(String[] oneRow)
   {
     if (this.currentListNumberInFile == MAX_LIST_SIZE_PER_FILE)
     {
       writeToFile();
       
       this.workbook = null;
       InputStream in = null;
       try
       {
         in = new FileInputStream(this.dataFileTmp);
         this.workbook = new HSSFWorkbook(in);
       }
       catch (Exception e)
       {
         throw new AppException("EFW0204", null, new Object[] { e });
       }
       finally
       {
         FileService.release(new Closeable[] {in });
         in = null;
       }
       this.currentListNumberInFile = 0L;
       
       beginList(this.currentListName);
       
 
       int remainRowNum = (int)Math.min(
         this.listSize - this.currentListNumber, MAX_LIST_SIZE_PER_FILE - 
         this.currentListNumberInFile);
       this.namedRowIndex += remainRowNum;
       batchCopyRow(this.sheetIndex, this.rowIndex, remainRowNum);
     }
     if (this.currentListNumber > this.listSize) {
       return;
     }
     HSSFRow row = this.workbook.getSheetAt(this.sheetIndex).getRow(this.rowIndex);
     for (int index = 0; index < Math.min(oneRow.length, 
           this.colIndices.length); index++)
     {
       HSSFCell cell = row.getCell(this.colIndices[index]);
       String cellData = oneRow[index];
       if (cellData == null) {
         cell.setCellValue(null);
       } else {
         switch (cell.getCellType())
         {
         case 0: 
           try
           {
             cell.setCellValue(Float.parseFloat(oneRow[index]));
           }
           catch (NumberFormatException localNumberFormatException) {}
         case 1: 
           cell.setCellValue(oneRow[index]);
           break;
         case 2: 
         case 3: 
         case 4: 
         case 5: 
         default: 
           cell.setCellValue(oneRow[index]);
         }
       }
     }
     this.rowIndex += 1;
     this.currentListNumber += 1L;
     this.currentListNumberInFile += 1L;
   }
   
   protected void batchCopyRow(int sheetIndex, int rowIndex, int downOffset)
   {
     HSSFSheet sheet = this.workbook.getSheetAt(sheetIndex);
     if (sheet.getRow(rowIndex) == null) {
       sheet.createRow(rowIndex);
     }
     HSSFRow testrow = sheet.getRow(rowIndex);
     for (int colIndex : this.colIndices) {
       if (testrow.getCell(colIndex) == null) {
         testrow.createCell(colIndex);
       }
     }
     int rowFrom = rowIndex;
     
     sheet.shiftRows(rowIndex, Math.max(sheet.getLastRowNum(), rowIndex), 
       downOffset, true, false, true);
     
     HSSFEvaluationWorkbook ewb = null;
     Ptg[] ptgs = (Ptg[])null;
     int index;
     for (Object cit = sheet.getRow(rowFrom + downOffset)
           .cellIterator(); 
           ((Iterator)cit).hasNext(); index < downOffset)
     {
       HSSFCell cell = (HSSFCell)((Iterator)cit).next();
       index = 0; continue;
       HSSFRow row = sheet.getRow(rowFrom + index);
       if (row == null) {
         row = sheet.createRow(rowFrom + index);
       }
       HSSFCell newCell = row.createCell(cell.getColumnIndex());
       if (cell.getCellComment() != null) {
         newCell.setCellComment(cell.getCellComment());
       }
       newCell.setCellStyle(cell.getCellStyle());
       if (cell.getCellType() == 2)
       {
         if (ewb != null) {
           ewb = 
             HSSFEvaluationWorkbook.create(sheet.getWorkbook());
         }
         ptgs = FormulaParser.parse(cell.getCellFormula(), ewb, 
           0, sheetIndex);
         for (Ptg ptg : ptgs) {
           if (((ptg instanceof RefPtgBase)) && 
             (((RefPtgBase)ptg).isRowRelative())) {
             ((RefPtgBase)ptg).setRow(((RefPtgBase)ptg)
               .getRow() - (rowFrom + downOffset));
           }
         }
       }
       switch (cell.getCellType())
       {
       case 1: 
         newCell.setCellValue(cell.getStringCellValue());
         break;
       case 0: 
         if (DateUtil.isCellDateFormatted(cell)) {
           newCell.setCellValue(cell.getDateCellValue());
         } else {
           newCell.setCellValue(cell.getNumericCellValue());
         }
         break;
       case 4: 
         newCell.setCellValue(cell.getBooleanCellValue());
         break;
       case 2: 
         Ptg[] newPtgs = new Ptg[ptgs.length];
         System.arraycopy(ptgs, 0, newPtgs, 0, newPtgs.length);
         for (int i = 0; i < newPtgs.length; i++) {
           if ((newPtgs[i] instanceof RefPtgBase))
           {
             RefPtgBase copyPtg = (RefPtgBase)((RefPtgBase)newPtgs[i])
               .copy();
             copyPtg.setRow(copyPtg.getRow() + 
               rowFrom + index);
             newPtgs[i] = copyPtg;
           }
         }
         newCell.setCellFormula(FormulaRenderer.toFormulaString(ewb, 
           newPtgs));
         break;
       case 5: 
         newCell.setCellValue(cell.getErrorCellValue());
         break;
       case 3: 
       default: 
         newCell.setCellValue(null);
       }
       index++;
     }
   }
   
   protected void beginList(String listName)
   {
     this.currentListName = listName;
     this.currentListNumber = 0L;
     if (this.dataFileTmp == null)
     {
       OutputStream out = null;
       try
       {
         this.dataFileTmp = File.createTempFile("dlf", "xls");
         this.dataFileTmp.deleteOnExit();
         out = new FileOutputStream(this.dataFileTmp);
         this.workbook.write(out);
       }
       catch (IOException e)
       {
         throw new AppException("EFW0205", null, new Object[] { e });
       }
       finally
       {
         FileService.release(new Closeable[] {out });
         out = null;
       }
     }
     getListDataIndex(listName);
   }
   
   public void close()
   {
     if (this.workbook != null) {
       this.workbook = null;
     }
     if (this.dataFileTmp != null)
     {
       FileService.recurseDeleteFiles(this.dataFileTmp);
       this.dataFileTmp = null;
     }
     if (this.tempDir != null)
     {
       FileService.recurseDeleteFiles(this.tempDir);
       this.tempDir = null;
     }
     if (this.colIndices != null) {
       this.colIndices = null;
     }
   }
   
   protected long countListData(StringBuilder listSql, Object[] preparedParams, int[] types)
   {
     return getJdbcTemplate().queryForLong(
       "select count(1) cnt from (" + listSql + 
       ") n", preparedParams, types);
   }
   
   protected void endList()
   {
     HSSFSheet sheet = this.workbook.getSheetAt(this.sheetIndex);
     if (this.namedRowIndex + 1 < sheet.getLastRowNum()) {
       sheet.shiftRows(this.namedRowIndex + 1, sheet.getLastRowNum(), 
         this.rowIndex - this.namedRowIndex - 1);
     }
     cleanListNames(false);
     this.colIndices = null;
     this.rowIndex = -1;
     this.sheetIndex = -1;
   }
   
   protected void cleanListNames(boolean removeAll)
   {
     HSSFRow row;
     if (removeAll)
     {
       for (String name : getListNames())
       {
         HSSFName vname = this.workbook.getName(name);
         if ((vname != null) && (!vname.isDeleted()))
         {
           int[] indices = extractListDataIndex(name);
           row = this.workbook.getSheetAt(indices[0]).getRow(
             indices[1]);
           if (row != null) {
             for (Cell cell : row) {
               cell.setCellValue("");
             }
           }
         }
         this.workbook.removeName(name);
       }
     }
     else
     {
       HSSFName vname = this.workbook.getName(this.currentListName);
       if ((vname != null) && (!vname.isDeleted()))
       {
         int[] indices = extractListDataIndex(this.currentListName);
         HSSFRow row = this.workbook.getSheetAt(indices[0]).getRow(
           indices[1]);
         if (row != null) {
           for (Cell cell : row) {
             cell.setCellValue("");
           }
         }
       }
       this.workbook.removeName(this.currentListName);
     }
   }
   
   public File exportExcel(ImpExpAbstract.ImpExpParameter excelParameter)
     throws AppException, IOException, AppException
   {
     File exportFile = null;
     try
     {
       readTemplateFile();
       
       prepareDictData(excelParameter.dyndictWhereCls);
       
       Map<String, Object> hashData = setHashData(
         excelParameter.hashWhereCls, excelParameter.addData);
       
       fillListData(excelParameter.listWhereCls, hashData);
       
       writeToFile();
       
       this.workbook = null;
       
       File[] files = this.tempDir.listFiles();
       if (files.length > 1)
       {
         exportFile = File.createTempFile("excel", ".zip");
         FileService.zip(exportFile, files);
       }
       else
       {
         exportFile = File.createTempFile("excel", ".xls");
         FileUtils.copyFile(files[0], exportFile);
       }
       if (log.isDebugEnabled()) {
         log.debug("创建Excel临时导出文件：" + exportFile.getName());
       }
       files = (File[])null;
     }
     finally
     {
       close();
     }
     return exportFile;
   }
   
   public Map<String, Object> extractUploadData(InputStream excelFileStream)
     throws IOException
   {
     readTemplateFile();
     
     Map<String, Integer[]> hashIndex = getHashDataIndex();
     
     List<String> listNames = getListNames();
     if ((listNames != null) && (!listNames.isEmpty())) {
       getListDataIndex((String)listNames.get(0));
     }
     this.workbook = null;
     
     this._formatter = new HSSFDataFormatter();
     
     HSSFWorkbook uploadwb = new HSSFWorkbook(excelFileStream);
     HSSFSheet sheet = uploadwb.getSheetAt(0);
     
     Map<String, Object> hashData = new HashMap(
       hashIndex.size()
     for (Map.Entry<String, Integer[]> entry : hashIndex.entrySet())
     {
       HSSFRow row = sheet.getRow(((Integer[])entry.getValue())[0].intValue());
       if (row != null)
       {
         HSSFCell cell = row.getCell(((Integer[])entry.getValue())[1].intValue());
         if (cell != null) {
           hashData.put((String)entry.getKey(), getCellValue(cell));
         }
       }
     }
     if ((listNames != null) && (!listNames.isEmpty()))
     {
       this.dataFileTmp = File.createTempFile("excelTempData", "db");
       this.dataFileTmp.deleteOnExit();
       ObjectOutputStream dataOutput = new ObjectOutputStream(
         new BufferedOutputStream(new FileOutputStream(this.dataFileTmp)));
       
       this.listSize = 0L;
       try
       {
         for (int i = this.rowIndex; i <= sheet.getLastRowNum(); i++)
         {
           HSSFRow row = sheet.getRow(i);
           if (row == null) {
             break;
           }
           String[] rowData = new String[this.colIndices.length];
           boolean hasData = false;
           for (int j = 0; j < this.colIndices.length; j++)
           {
             rowData[j] = getCellValue(row
               .getCell(this.colIndices[j]));
             if ((rowData[j] != null) && (rowData[j].trim().length() > 0)) {
               hasData = true;
             }
           }
           if (!hasData) {
             break;
           }
           dataOutput.writeObject(rowData);
           this.listSize += 1L;
         }
       }
       finally
       {
         dataOutput.close();
         dataOutput = null;
       }
     }
     uploadwb = null;
     return hashData;
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
       if (log.isDebugEnabled()) {
         log.debug("开始向Excel写入列表数据：" + query.toString());
       }
       List<String> params = parseSql(query.toString());
       String parsedSql = (String)params.remove(0);
       
       Object[] preparedParams = ImpExpAbstract.selectArguments(hashData, 
         params);
       int[] types = new int[preparedParams.length];
       Arrays.fill(types, 12);
       long rowNum = countListData(query, preparedParams, types);
       
       beginList("列表" + String.valueOf(i));
       setListSize(rowNum);
       
       getJdbcTemplate()
         .query(parsedSql, preparedParams, types, this);
       
       endList();
     }
   }
   
   protected String getCellValue(HSSFCell cell)
   {
     if (cell == null) {
       return null;
     }
     switch (cell.getCellType())
     {
     case 3: 
       return null;
     case 1: 
       return cell.getRichStringCellValue().getString();
     case 0: 
       return this._formatter.formatCellValue(cell);
     case 4: 
       return Boolean.toString(cell.getBooleanCellValue());
     case 5: 
       return null;
     case 2: 
       switch (cell.getCachedFormulaResultType())
       {
       case 1: 
         HSSFRichTextString str = cell.getRichStringCellValue();
         if ((str != null) && (str.length() > 0)) {
           return str.toString();
         }
         return null;
       case 0: 
         HSSFCellStyle style = cell.getCellStyle();
         if (style == null) {
           return String.valueOf(cell.getNumericCellValue());
         }
         return this._formatter.formatRawCellContents(
           cell.getNumericCellValue(), style.getDataFormat(), 
           style.getDataFormatString());
       case 4: 
         return Boolean.toString(cell.getBooleanCellValue());
       case 5: 
         return null;
       }
       break;
     default: 
       throw new AppException("EFW0219", null, new Object[] { Integer.valueOf(cell.getCellType()) });
     }
     return cell.getStringCellValue();
   }
   
   protected Map<String, Integer[]> getHashDataIndex()
   {
     List<String> hashNames = getHashNames();
     Map<String, Integer[]> rst = new HashMap();
     AreaReference[] arefs;
     int i;
     for (Iterator localIterator = hashNames.iterator(); localIterator.hasNext(); i < arefs.length)
     {
       String name = (String)localIterator.next();
       HSSFName aNamedCell = this.workbook.getName(name);
       arefs = AreaReference.generateContiguous(aNamedCell
         .getRefersToFormula());
       i = 0; continue;
       CellReference cref = arefs[i].getFirstCell();
       Integer[] index = { Integer.valueOf(cref.getRow()), Integer.valueOf(cref.getCol()) };
       rst.put(name, index);i++;
     }
     return rst;
   }
   
   protected List<String> getHashNames()
   {
     int nameNum = this.workbook.getNumberOfNames();
     List<String> names = new ArrayList(nameNum);
     for (int i = 0; i < nameNum; i++)
     {
       String name = this.workbook.getNameName(i);
       if (!name.startsWith("列表")) {
         names.add(name);
       }
     }
     return names;
   }
   
   protected JdbcTemplate getJdbcTemplate()
   {
     if (this.jdbcTemplate == null) {
       this.jdbcTemplate = ((JdbcTemplate)Util.getBean("jdbcTemplate"));
     }
     return this.jdbcTemplate;
   }
   
   protected int[] extractListDataIndex(String listName)
   {
     HSSFName name = this.workbook.getName(listName);
     if ((name == null) || (name.isDeleted())) {
       throw new AppException("EFW0206", null, new Object[] { listName });
     }
     String sheetName = null;
     List<Integer> cellSize = new ArrayList();
     int sheetIndex = -1;int rowIndex = -1;
     AreaReference[] arefs = AreaReference.generateContiguous(name
       .getRefersToFormula());
     for (int i = 0; i < arefs.length; i++)
     {
       CellReference[] crefs = arefs[i].getAllReferencedCells();
       for (int j = 0; j < crefs.length; j++)
       {
         if (rowIndex == -1)
         {
           rowIndex = crefs[j].getRow();
           sheetName = crefs[j].getSheetName();
           sheetIndex = this.workbook.getSheetIndex(sheetName);
         }
         else if ((crefs[j].getRow() != rowIndex) || 
           (!sheetName.equals(crefs[j].getSheetName())))
         {
           throw new AppException("EFW0207", null, new Object[] { name.getNameName() });
         }
         cellSize.add(Integer.valueOf(crefs[j].getCol()));
       }
     }
     Collections.sort(cellSize);
     
     int[] colIndices = new int[cellSize.size() + 2];
     
     colIndices[0] = sheetIndex;
     
     colIndices[1] = rowIndex;
     for (int i = 2; i < colIndices.length; i++) {
       colIndices[i] = ((Integer)cellSize.get(i - 2)).intValue();
     }
     return colIndices;
   }
   
   protected void getListDataIndex(String listName)
   {
     int[] indices = extractListDataIndex(listName);
     this.sheetIndex = indices[0];
     this.rowIndex = indices[1];
     this.namedRowIndex = this.rowIndex;
     this.colIndices = new int[indices.length - 2];
     System.arraycopy(indices, 2, this.colIndices, 0, indices.length - 2);
   }
   
   protected List<String> getListNames()
   {
     int nameNum = this.workbook.getNumberOfNames();
     List<String> names = new ArrayList(nameNum);
     for (int i = 0; i < nameNum; i++)
     {
       String name = this.workbook.getNameName(i);
       if (name.startsWith("列表")) {
         names.add(name);
       }
     }
     return names;
   }
   
   
   public Object importFile(InputStream excelFileStream, ImpExpAbstract.ImpExpParameter impExpParameter, cn.sinobest.framework.comm.iface.IUploadWork work)
     throws AppException
   {
     // Byte code:
     //   0: aconst_null
     //   1: astore 4
     //   3: aload_0
     //   4: aload_1
     //   5: invokevirtual 859	cn/sinobest/framework/service/file/ExcelImpExp:extractUploadData	(Ljava/io/InputStream;)Ljava/util/Map;
     //   8: astore 4
     //   10: goto +18 -> 28
     //   13: astore 5
     //   15: new 145	cn/sinobest/framework/comm/exception/AppException
     //   18: dup
     //   19: ldc_w 861
     //   22: aload 5
     //   24: invokespecial 863	cn/sinobest/framework/comm/exception/AppException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
     //   27: athrow
     //   28: aload_0
     //   29: aload_2
     //   30: getfield 502	cn/sinobest/framework/service/file/ImpExpAbstract$ImpExpParameter:dyndictWhereCls	Ljava/util/Map;
     //   33: invokevirtual 508	cn/sinobest/framework/service/file/ExcelImpExp:prepareDictData	(Ljava/util/Map;)V
     //   36: aload_0
     //   37: invokevirtual 866	cn/sinobest/framework/service/file/ExcelImpExp:getRevertDict	()Ljava/util/Map;
     //   40: astore 5
     //   42: aload 5
     //   44: ifnull +372 -> 416
     //   47: aload 5
     //   49: invokeinterface 869 1 0
     //   54: ifne +362 -> 416
     //   57: aload 4
     //   59: invokeinterface 592 1 0
     //   64: anewarray 466	java/lang/String
     //   67: astore 6
     //   69: aload 4
     //   71: invokeinterface 592 1 0
     //   76: anewarray 149	java/lang/Object
     //   79: astore 7
     //   81: iconst_0
     //   82: istore 8
     //   84: aload 4
     //   86: invokeinterface 599 1 0
     //   91: invokeinterface 603 1 0
     //   96: astore 10
     //   98: goto +45 -> 143
     //   101: aload 10
     //   103: invokeinterface 252 1 0
     //   108: checkcast 606	java/util/Map$Entry
     //   111: astore 9
     //   113: aload 6
     //   115: iload 8
     //   117: aload 9
     //   119: invokeinterface 618 1 0
     //   124: checkcast 466	java/lang/String
     //   127: aastore
     //   128: aload 7
     //   130: iload 8
     //   132: aload 9
     //   134: invokeinterface 608 1 0
     //   139: aastore
     //   140: iinc 8 1
     //   143: aload 10
     //   145: invokeinterface 360 1 0
     //   150: ifne -49 -> 101
     //   153: aload_0
     //   154: aload 6
     //   156: aload 5
     //   158: invokevirtual 870	cn/sinobest/framework/service/file/ExcelImpExp:getLineRevertDict	([Ljava/lang/String;Ljava/util/Map;)[Ljava/util/Map;
     //   161: astore 9
     //   163: aload 6
     //   165: arraylength
     //   166: anewarray 466	java/lang/String
     //   169: astore 10
     //   171: iconst_0
     //   172: istore 11
     //   174: goto +147 -> 321
     //   177: aload 6
     //   179: iload 11
     //   181: aaload
     //   182: astore 12
     //   184: aload 10
     //   186: iload 11
     //   188: aload 12
     //   190: aastore
     //   191: aload_0
     //   192: getfield 666	cn/sinobest/framework/service/file/ExcelImpExp:impExpConfig	Lcn/sinobest/framework/service/file/ImpExpConfig;
     //   195: getfield 874	cn/sinobest/framework/service/file/ImpExpConfig:renders	[[Ljava/util/Map;
     //   198: ifnull +120 -> 318
     //   201: aload_0
     //   202: getfield 666	cn/sinobest/framework/service/file/ExcelImpExp:impExpConfig	Lcn/sinobest/framework/service/file/ImpExpConfig;
     //   205: getfield 874	cn/sinobest/framework/service/file/ImpExpConfig:renders	[[Ljava/util/Map;
     //   208: arraylength
     //   209: ifle +109 -> 318
     //   212: aload_0
     //   213: getfield 666	cn/sinobest/framework/service/file/ExcelImpExp:impExpConfig	Lcn/sinobest/framework/service/file/ImpExpConfig;
     //   216: getfield 874	cn/sinobest/framework/service/file/ImpExpConfig:renders	[[Ljava/util/Map;
     //   219: dup
     //   220: astore 16
     //   222: arraylength
     //   223: istore 15
     //   225: iconst_0
     //   226: istore 14
     //   228: goto +83 -> 311
     //   231: aload 16
     //   233: iload 14
     //   235: aaload
     //   236: astore 13
     //   238: aload 13
     //   240: dup
     //   241: astore 20
     //   243: arraylength
     //   244: istore 19
     //   246: iconst_0
     //   247: istore 18
     //   249: goto +52 -> 301
     //   252: aload 20
     //   254: iload 18
     //   256: aaload
     //   257: astore 17
     //   259: aload 12
     //   261: aload 17
     //   263: ldc_w 878
     //   266: invokeinterface 879 2 0
     //   271: invokevirtual 838	java/lang/String:equals	(Ljava/lang/Object;)Z
     //   274: ifeq +24 -> 298
     //   277: aload 10
     //   279: iload 11
     //   281: aload 17
     //   283: ldc_w 882
     //   286: invokeinterface 879 2 0
     //   291: checkcast 466	java/lang/String
     //   294: aastore
     //   295: goto +23 -> 318
     //   298: iinc 18 1
     //   301: iload 18
     //   303: iload 19
     //   305: if_icmplt -53 -> 252
     //   308: iinc 14 1
     //   311: iload 14
     //   313: iload 15
     //   315: if_icmplt -84 -> 231
     //   318: iinc 11 1
     //   321: iload 11
     //   323: aload 6
     //   325: arraylength
     //   326: if_icmplt -149 -> 177
     //   329: new 422	java/lang/StringBuilder
     //   332: dup
     //   333: invokespecial 884	java/lang/StringBuilder:<init>	()V
     //   336: astore 11
     //   338: aload_0
     //   339: aload 11
     //   341: aload 7
     //   343: aload 10
     //   345: aload 9
     //   347: invokevirtual 885	cn/sinobest/framework/service/file/ExcelImpExp:replaceDictValue	(Ljava/lang/StringBuilder;Ljava/lang/Object;[Ljava/lang/String;[Ljava/util/Map;)Ljava/lang/String;
     //   350: astore 12
     //   352: iconst_0
     //   353: istore 8
     //   355: goto +24 -> 379
     //   358: aload 4
     //   360: aload 6
     //   362: iload 8
     //   364: aaload
     //   365: aload 7
     //   367: iload 8
     //   369: aaload
     //   370: invokeinterface 625 3 0
     //   375: pop
     //   376: iinc 8 1
     //   379: iload 8
     //   381: aload 6
     //   383: arraylength
     //   384: if_icmplt -26 -> 358
     //   387: aload 4
     //   389: ldc_w 889
     //   392: aload 11
     //   394: invokevirtual 437	java/lang/StringBuilder:toString	()Ljava/lang/String;
     //   397: invokeinterface 625 3 0
     //   402: pop
     //   403: aload 4
     //   405: ldc_w 891
     //   408: aload 12
     //   410: invokeinterface 625 3 0
     //   415: pop
     //   416: aload_2
     //   417: getfield 515	cn/sinobest/framework/service/file/ImpExpAbstract$ImpExpParameter:addData	Ljava/util/Map;
     //   420: ifnull +124 -> 544
     //   423: aload_2
     //   424: getfield 515	cn/sinobest/framework/service/file/ImpExpAbstract$ImpExpParameter:addData	Ljava/util/Map;
     //   427: invokeinterface 599 1 0
     //   432: invokeinterface 603 1 0
     //   437: astore 7
     //   439: goto +95 -> 534
     //   442: aload 7
     //   444: invokeinterface 252 1 0
     //   449: checkcast 606	java/util/Map$Entry
     //   452: astore 6
     //   454: aload 4
     //   456: aload 6
     //   458: invokeinterface 618 1 0
     //   463: invokeinterface 893 2 0
     //   468: ifeq +6 -> 474
     //   471: goto +63 -> 534
     //   474: aload 6
     //   476: invokeinterface 608 1 0
     //   481: ifnonnull +25 -> 506
     //   484: aload 4
     //   486: aload 6
     //   488: invokeinterface 618 1 0
     //   493: checkcast 466	java/lang/String
     //   496: aconst_null
     //   497: invokeinterface 625 3 0
     //   502: pop
     //   503: goto +31 -> 534
     //   506: aload 4
     //   508: aload 6
     //   510: invokeinterface 618 1 0
     //   515: checkcast 466	java/lang/String
     //   518: aload 6
     //   520: invokeinterface 608 1 0
     //   525: invokevirtual 896	java/lang/Object:toString	()Ljava/lang/String;
     //   528: invokeinterface 625 3 0
     //   533: pop
     //   534: aload 7
     //   536: invokeinterface 360 1 0
     //   541: ifne -99 -> 442
     //   544: aconst_null
     //   545: astore 6
     //   547: aconst_null
     //   548: checkcast 897	[Ljava/lang/String;
     //   551: astore 7
     //   553: aconst_null
     //   554: checkcast 898	[Ljava/util/Map;
     //   557: astore 8
     //   559: aload_0
     //   560: getfield 108	cn/sinobest/framework/service/file/ExcelImpExp:dataFileTmp	Ljava/io/File;
     //   563: ifnull +122 -> 685
     //   566: aload_0
     //   567: getfield 98	cn/sinobest/framework/service/file/ExcelImpExp:colIndices	[I
     //   570: arraylength
     //   571: anewarray 466	java/lang/String
     //   574: astore 7
     //   576: iconst_0
     //   577: istore 9
     //   579: goto +21 -> 600
     //   582: aload 7
     //   584: iload 9
     //   586: aload_0
     //   587: getfield 98	cn/sinobest/framework/service/file/ExcelImpExp:colIndices	[I
     //   590: iload 9
     //   592: iaload
     //   593: invokestatic 900	org/apache/poi/ss/util/CellReference:convertNumToColString	(I)Ljava/lang/String;
     //   596: aastore
     //   597: iinc 9 1
     //   600: iload 9
     //   602: aload_0
     //   603: getfield 98	cn/sinobest/framework/service/file/ExcelImpExp:colIndices	[I
     //   606: arraylength
     //   607: if_icmplt -25 -> 582
     //   610: aload 5
     //   612: ifnull +23 -> 635
     //   615: aload 5
     //   617: invokeinterface 869 1 0
     //   622: ifne +13 -> 635
     //   625: aload_0
     //   626: aload 7
     //   628: aload 5
     //   630: invokevirtual 870	cn/sinobest/framework/service/file/ExcelImpExp:getLineRevertDict	([Ljava/lang/String;Ljava/util/Map;)[Ljava/util/Map;
     //   633: astore 8
     //   635: new 903	java/io/ObjectInputStream
     //   638: dup
     //   639: new 905	java/io/BufferedInputStream
     //   642: dup
     //   643: new 135	java/io/FileInputStream
     //   646: dup
     //   647: aload_0
     //   648: getfield 108	cn/sinobest/framework/service/file/ExcelImpExp:dataFileTmp	Ljava/io/File;
     //   651: invokespecial 137	java/io/FileInputStream:<init>	(Ljava/io/File;)V
     //   654: invokespecial 907	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;)V
     //   657: invokespecial 908	java/io/ObjectInputStream:<init>	(Ljava/io/InputStream;)V
     //   660: astore 6
     //   662: goto +23 -> 685
     //   665: astore 9
     //   667: iconst_1
     //   668: anewarray 154	java/io/Closeable
     //   671: dup
     //   672: iconst_0
     //   673: aload 6
     //   675: aastore
     //   676: invokestatic 156	cn/sinobest/framework/service/file/FileService:release	([Ljava/io/Closeable;)V
     //   679: aconst_null
     //   680: astore 6
     //   682: aload 9
     //   684: athrow
     //   685: aload 6
     //   687: astore 9
     //   689: aload 7
     //   691: astore 10
     //   693: aload 8
     //   695: astore 11
     //   697: aload 7
     //   699: arraylength
     //   700: anewarray 466	java/lang/String
     //   703: astore 12
     //   705: iconst_0
     //   706: istore 13
     //   708: goto +147 -> 855
     //   711: aload 10
     //   713: iload 13
     //   715: aaload
     //   716: astore 14
     //   718: aload 12
     //   720: iload 13
     //   722: aload 14
     //   724: aastore
     //   725: aload_0
     //   726: getfield 666	cn/sinobest/framework/service/file/ExcelImpExp:impExpConfig	Lcn/sinobest/framework/service/file/ImpExpConfig;
     //   729: getfield 874	cn/sinobest/framework/service/file/ImpExpConfig:renders	[[Ljava/util/Map;
     //   732: ifnull +120 -> 852
     //   735: aload_0
     //   736: getfield 666	cn/sinobest/framework/service/file/ExcelImpExp:impExpConfig	Lcn/sinobest/framework/service/file/ImpExpConfig;
     //   739: getfield 874	cn/sinobest/framework/service/file/ImpExpConfig:renders	[[Ljava/util/Map;
     //   742: arraylength
     //   743: ifle +109 -> 852
     //   746: aload_0
     //   747: getfield 666	cn/sinobest/framework/service/file/ExcelImpExp:impExpConfig	Lcn/sinobest/framework/service/file/ImpExpConfig;
     //   750: getfield 874	cn/sinobest/framework/service/file/ImpExpConfig:renders	[[Ljava/util/Map;
     //   753: dup
     //   754: astore 18
     //   756: arraylength
     //   757: istore 17
     //   759: iconst_0
     //   760: istore 16
     //   762: goto +83 -> 845
     //   765: aload 18
     //   767: iload 16
     //   769: aaload
     //   770: astore 15
     //   772: aload 15
     //   774: dup
     //   775: astore 22
     //   777: arraylength
     //   778: istore 21
     //   780: iconst_0
     //   781: istore 20
     //   783: goto +52 -> 835
     //   786: aload 22
     //   788: iload 20
     //   790: aaload
     //   791: astore 19
     //   793: aload 14
     //   795: aload 19
     //   797: ldc_w 878
     //   800: invokeinterface 879 2 0
     //   805: invokevirtual 838	java/lang/String:equals	(Ljava/lang/Object;)Z
     //   808: ifeq +24 -> 832
     //   811: aload 12
     //   813: iload 13
     //   815: aload 19
     //   817: ldc_w 882
     //   820: invokeinterface 879 2 0
     //   825: checkcast 466	java/lang/String
     //   828: aastore
     //   829: goto +23 -> 852
     //   832: iinc 20 1
     //   835: iload 20
     //   837: iload 21
     //   839: if_icmplt -53 -> 786
     //   842: iinc 16 1
     //   845: iload 16
     //   847: iload 17
     //   849: if_icmplt -84 -> 765
     //   852: iinc 13 1
     //   855: iload 13
     //   857: aload 10
     //   859: arraylength
     //   860: if_icmplt -149 -> 711
     //   863: new 909	cn/sinobest/framework/service/file/ExcelImpExp$1
     //   866: dup
     //   867: aload_0
     //   868: aload 9
     //   870: aload 11
     //   872: aload 12
     //   874: aload 10
     //   876: invokespecial 911	cn/sinobest/framework/service/file/ExcelImpExp$1:<init>	(Lcn/sinobest/framework/service/file/ExcelImpExp;Ljava/io/ObjectInputStream;[Ljava/util/Map;[Ljava/lang/String;[Ljava/lang/String;)V
     //   879: astore 13
     //   881: aload_3
     //   882: aload_0
     //   883: getfield 666	cn/sinobest/framework/service/file/ExcelImpExp:impExpConfig	Lcn/sinobest/framework/service/file/ImpExpConfig;
     //   886: aload 4
     //   888: aload 13
     //   890: invokeinterface 914 4 0
     //   895: astore 24
     //   897: iconst_1
     //   898: anewarray 154	java/io/Closeable
     //   901: dup
     //   902: iconst_0
     //   903: aload 6
     //   905: aastore
     //   906: invokestatic 156	cn/sinobest/framework/service/file/FileService:release	([Ljava/io/Closeable;)V
     //   909: aconst_null
     //   910: astore 6
     //   912: iconst_1
     //   913: anewarray 154	java/io/Closeable
     //   916: dup
     //   917: iconst_0
     //   918: aload_3
     //   919: aastore
     //   920: invokestatic 156	cn/sinobest/framework/service/file/FileService:release	([Ljava/io/Closeable;)V
     //   923: aconst_null
     //   924: astore_3
     //   925: iconst_1
     //   926: anewarray 154	java/io/Closeable
     //   929: dup
     //   930: iconst_0
     //   931: aload_0
     //   932: aastore
     //   933: invokestatic 156	cn/sinobest/framework/service/file/FileService:release	([Ljava/io/Closeable;)V
     //   936: aload 24
     //   938: areturn
     //   939: astore 7
     //   941: new 145	cn/sinobest/framework/comm/exception/AppException
     //   944: dup
     //   945: ldc_w 861
     //   948: aload 7
     //   950: invokespecial 863	cn/sinobest/framework/comm/exception/AppException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
     //   953: athrow
     //   954: astore 23
     //   956: iconst_1
     //   957: anewarray 154	java/io/Closeable
     //   960: dup
     //   961: iconst_0
     //   962: aload 6
     //   964: aastore
     //   965: invokestatic 156	cn/sinobest/framework/service/file/FileService:release	([Ljava/io/Closeable;)V
     //   968: aconst_null
     //   969: astore 6
     //   971: aload 23
     //   973: athrow
     //   974: astore 25
     //   976: iconst_1
     //   977: anewarray 154	java/io/Closeable
     //   980: dup
     //   981: iconst_0
     //   982: aload_3
     //   983: aastore
     //   984: invokestatic 156	cn/sinobest/framework/service/file/FileService:release	([Ljava/io/Closeable;)V
     //   987: aconst_null
     //   988: astore_3
     //   989: iconst_1
     //   990: anewarray 154	java/io/Closeable
     //   993: dup
     //   994: iconst_0
     //   995: aload_0
     //   996: aastore
     //   997: invokestatic 156	cn/sinobest/framework/service/file/FileService:release	([Ljava/io/Closeable;)V
     //   1000: aload 25
     //   1002: athrow
     // Line number table:
     //   Java source line #1148	-> byte code offset #0
     //   Java source line #1150	-> byte code offset #3
     //   Java source line #1151	-> byte code offset #13
     //   Java source line #1152	-> byte code offset #15
     //   Java source line #1155	-> byte code offset #28
     //   Java source line #1157	-> byte code offset #36
     //   Java source line #1159	-> byte code offset #42
     //   Java source line #1161	-> byte code offset #57
     //   Java source line #1162	-> byte code offset #69
     //   Java source line #1163	-> byte code offset #81
     //   Java source line #1164	-> byte code offset #84
     //   Java source line #1165	-> byte code offset #113
     //   Java source line #1166	-> byte code offset #128
     //   Java source line #1167	-> byte code offset #140
     //   Java source line #1164	-> byte code offset #143
     //   Java source line #1170	-> byte code offset #153
     //   Java source line #1171	-> byte code offset #154
     //   Java source line #1170	-> byte code offset #158
     //   Java source line #1172	-> byte code offset #163
     //   Java source line #1173	-> byte code offset #171
     //   Java source line #1175	-> byte code offset #177
     //   Java source line #1176	-> byte code offset #184
     //   Java source line #1177	-> byte code offset #191
     //   Java source line #1178	-> byte code offset #201
     //   Java source line #1180	-> byte code offset #212
     //   Java source line #1181	-> byte code offset #238
     //   Java source line #1182	-> byte code offset #259
     //   Java source line #1184	-> byte code offset #277
     //   Java source line #1185	-> byte code offset #295
     //   Java source line #1181	-> byte code offset #298
     //   Java source line #1180	-> byte code offset #308
     //   Java source line #1173	-> byte code offset #318
     //   Java source line #1191	-> byte code offset #329
     //   Java source line #1192	-> byte code offset #338
     //   Java source line #1193	-> byte code offset #345
     //   Java source line #1192	-> byte code offset #347
     //   Java source line #1195	-> byte code offset #352
     //   Java source line #1196	-> byte code offset #358
     //   Java source line #1195	-> byte code offset #376
     //   Java source line #1199	-> byte code offset #387
     //   Java source line #1200	-> byte code offset #403
     //   Java source line #1203	-> byte code offset #416
     //   Java source line #1204	-> byte code offset #423
     //   Java source line #1205	-> byte code offset #427
     //   Java source line #1206	-> byte code offset #454
     //   Java source line #1207	-> byte code offset #471
     //   Java source line #1208	-> byte code offset #474
     //   Java source line #1210	-> byte code offset #484
     //   Java source line #1212	-> byte code offset #506
     //   Java source line #1213	-> byte code offset #525
     //   Java source line #1212	-> byte code offset #528
     //   Java source line #1204	-> byte code offset #534
     //   Java source line #1217	-> byte code offset #544
     //   Java source line #1219	-> byte code offset #547
     //   Java source line #1220	-> byte code offset #553
     //   Java source line #1221	-> byte code offset #559
     //   Java source line #1223	-> byte code offset #566
     //   Java source line #1224	-> byte code offset #576
     //   Java source line #1225	-> byte code offset #582
     //   Java source line #1226	-> byte code offset #586
     //   Java source line #1225	-> byte code offset #596
     //   Java source line #1224	-> byte code offset #597
     //   Java source line #1229	-> byte code offset #610
     //   Java source line #1230	-> byte code offset #625
     //   Java source line #1233	-> byte code offset #635
     //   Java source line #1234	-> byte code offset #639
     //   Java source line #1235	-> byte code offset #647
     //   Java source line #1234	-> byte code offset #654
     //   Java source line #1233	-> byte code offset #657
     //   Java source line #1236	-> byte code offset #665
     //   Java source line #1237	-> byte code offset #673
     //   Java source line #1238	-> byte code offset #679
     //   Java source line #1239	-> byte code offset #682
     //   Java source line #1242	-> byte code offset #685
     //   Java source line #1243	-> byte code offset #689
     //   Java source line #1244	-> byte code offset #693
     //   Java source line #1245	-> byte code offset #697
     //   Java source line #1246	-> byte code offset #705
     //   Java source line #1248	-> byte code offset #711
     //   Java source line #1249	-> byte code offset #718
     //   Java source line #1250	-> byte code offset #725
     //   Java source line #1251	-> byte code offset #735
     //   Java source line #1253	-> byte code offset #746
     //   Java source line #1254	-> byte code offset #772
     //   Java source line #1255	-> byte code offset #793
     //   Java source line #1257	-> byte code offset #811
     //   Java source line #1258	-> byte code offset #829
     //   Java source line #1254	-> byte code offset #832
     //   Java source line #1253	-> byte code offset #842
     //   Java source line #1246	-> byte code offset #852
     //   Java source line #1264	-> byte code offset #863
     //   Java source line #1304	-> byte code offset #881
     //   Java source line #1307	-> byte code offset #897
     //   Java source line #1308	-> byte code offset #903
     //   Java source line #1309	-> byte code offset #909
     //   Java source line #1311	-> byte code offset #912
     //   Java source line #1312	-> byte code offset #918
     //   Java source line #1313	-> byte code offset #923
     //   Java source line #1314	-> byte code offset #931
     //   Java source line #1304	-> byte code offset #936
     //   Java source line #1305	-> byte code offset #939
     //   Java source line #1306	-> byte code offset #941
     //   Java source line #1307	-> byte code offset #954
     //   Java source line #1308	-> byte code offset #962
     //   Java source line #1309	-> byte code offset #968
     //   Java source line #1310	-> byte code offset #971
     //   Java source line #1311	-> byte code offset #974
     //   Java source line #1312	-> byte code offset #982
     //   Java source line #1313	-> byte code offset #987
     //   Java source line #1314	-> byte code offset #995
     //   Java source line #1315	-> byte code offset #1000
     // Local variable table:
     //   start	length	slot	name	signature
     //   0	1003	0	this	ExcelImpExp
     //   0	1003	1	excelFileStream	InputStream
     //   0	1003	2	impExpParameter	ImpExpAbstract.ImpExpParameter
     //   0	1003	3	work	cn.sinobest.framework.comm.iface.IUploadWork
     //   1	886	4	hashData	Map<String, Object>
     //   13	10	5	e1	IOException
     //   40	589	5	revertDict	Map<String, Map<String, String>>
     //   67	315	6	hashAlias	String[]
     //   452	67	6	entry	Map.Entry<String, Object>
     //   545	425	6	objectInput	ObjectInputStream
     //   79	456	7	values	Object[]
     //   551	147	7	colAlias	String[]
     //   939	10	7	e	IOException
     //   82	298	8	index	int
     //   557	137	8	lineRevertDict	Map[]
     //   111	22	9	entry	Map.Entry<String, Object>
     //   161	185	9	lineRevertDict	Map[]
     //   577	24	9	i	int
     //   665	18	9	e	IOException
     //   687	182	9	fObjectInput	ObjectInputStream
     //   96	48	10	localIterator	Iterator
     //   169	175	10	fcolLabels	String[]
     //   691	184	10	fcolAlias	String[]
     //   172	150	11	i	int
     //   336	57	11	jysm	StringBuilder
     //   695	176	11	flineRevertDict	Map[]
     //   182	78	12	name	String
     //   350	59	12	jyzt	String
     //   703	170	12	fcolLabels	String[]
     //   236	3	13	row	Map[]
     //   706	150	13	i	int
     //   879	10	13	enumE	Enumeration<Map<String, Object>>
     //   226	90	14	i	int
     //   716	78	14	name	String
     //   223	93	15	j	int
     //   770	3	15	row	Map[]
     //   220	12	16	arrayOfMap1	Map[][]
     //   760	90	16	localMap1	Map<String, Object>
     //   257	593	17	col	Map<String, Object>
     //   247	59	18	k	int
     //   754	12	18	arrayOfMap2	Map[][]
     //   244	62	19	m	int
     //   791	25	19	col	Map<String, Object>
     //   241	12	20	arrayOfMap3	Map[]
     //   781	59	20	n	int
     //   778	62	21	i1	int
     //   775	12	22	arrayOfMap4	Map[]
     //   954	18	23	localObject1	Object
     //   895	42	24	localObject2	Object
     //   974	27	25	localObject3	Object
     // Exception table:
     //   from	to	target	type
     //   3	10	13	java/io/IOException
     //   635	662	665	java/io/IOException
     //   547	897	939	java/io/IOException
     //   547	897	954	finally
     //   939	954	954	finally
     //   0	912	974	finally
     //   939	974	974	finally
   }
   
   protected void readTemplateFile()
     throws AppException
   {
     if (this.workbook != null) {
       return;
     }
     if (this.impExpConfig.getTemplateFile() != null)
     {
       InputStream in = null;
       try
       {
         in = new FileInputStream(new File(TEMPLATE_LOCATION, 
           this.impExpConfig.getTemplateFile()));
         this.workbook = new HSSFWorkbook(in);
       }
       catch (IOException e)
       {
         throw new AppException("EFW0220", e);
       }
       finally
       {
         FileService.release(new Closeable[] {in });
         in = null;
       }
     }
   }
   
   protected Map<String, Object> setHashData(String[] whereCls, Map<String, Object> data)
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
         params = parseSql(sql.toString());
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
     List<String> hashNames = getHashNames();
     AreaReference[] arefs;
     int i;
     for (List<String> params = hashNames.iterator(); params.hasNext(); i < arefs.length)
     {
       String name = (String)params.next();
       HSSFName aNamedCell = this.workbook.getName(name);
       arefs = AreaReference.generateContiguous(aNamedCell
         .getRefersToFormula());
       i = 0; continue;
       CellReference cref = arefs[i].getFirstCell();
       HSSFCell c = this.workbook.getSheet(cref.getSheetName())
         .getRow(cref.getRow()).getCell(cref.getCol());
       if (this.workbook.getSheet(cref.getSheetName())
         .getRow(cref.getRow()) == null) {
         this.workbook.getSheet(cref.getSheetName()).createRow(
           cref.getRow());
       }
       if (c == null) {
         c = 
         
           this.workbook.getSheet(cref.getSheetName()).getRow(cref.getRow()).createCell(cref.getCol());
       }
       Object v = hashData.get(name);
       if (v == null) {
         c.setCellValue(null);
       } else if ((v instanceof Number)) {
         c.setCellValue(((Number)v).doubleValue());
       } else {
         c.setCellValue(String.valueOf(v));
       }
       i++;
     }
     return hashData;
   }
   
   protected void setListSize(long rowNum)
   {
     this.listSize = rowNum;
     if (rowNum == 0L) {
       return;
     }
     int remainRowNum = (int)Math.min(this.listSize - this.currentListNumber, 
       MAX_LIST_SIZE_PER_FILE - this.currentListNumberInFile);
     this.namedRowIndex += remainRowNum;
     batchCopyRow(this.sheetIndex, this.rowIndex, remainRowNum);
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
   
   protected void writeToFile()
   {
     cleanListNames(true);
     if (this.tempDir == null)
     {
       this.tempDir = new File(System.getProperty("java.io.tmpdir"), "unzip" + 
         RANDOM.nextLong());
       if (log.isDebugEnabled()) {
         log.debug("创建Excel临时文件夹：" + this.tempDir.getName());
       }
       this.tempDir.mkdir();
     }
     File file = new File(this.tempDir, ++this.fileNumber + ".xls");
     if (log.isDebugEnabled()) {
       log.debug("创建Excel临时文件：" + file.getName());
     }
     OutputStream out = null;
     try
     {
       out = new FileOutputStream(file);
       this.workbook.write(out);
     }
     catch (AppException e)
     {
       throw e;
     }
     catch (Exception e)
     {
       throw new AppException("EFW0208", e, new Object[] { file.getAbsolutePath() });
     }
     finally
     {
       if (out != null)
       {
         try
         {
           out.close();
         }
         catch (IOException localIOException) {}
         out = null;
       }
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
         if ((v != null) && (colDict[i] != null))
         {
           Map<String, String> rst = (Map)colDict[i].get(v);
           if (rst == null) {
             v = null;
           } else {
             v = (String)((Map)colDict[i].get(v)).get("AAA103");
           }
         }
         oneRow[i] = v;
       }
       addRowData(oneRow);
     }
     return null;
   }
 }