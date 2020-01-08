 package cn.sinobest.framework.service.file;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.comm.iface.IExportService;
 import cn.sinobest.framework.service.tags.Glt;
 import cn.sinobest.framework.service.tags.GltService;
 import cn.sinobest.framework.util.Util;
 import java.io.Closeable;
 import java.io.File;
 import java.io.IOException;
 import java.util.HashMap;
 import java.util.Map;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
 
 @Service
 public class ExcelService
   implements IExportService
 {
   public static final String KEY_CONFIGID = "configId";
   public static final String KEY_HASHWHERECLS = "hashWhereCls";
   public static final String KEY_LISTWHERECLS = "listWhereCls";
   public static final String KEY_DYNDICTSWHERECLS = "dynDictWhereCls";
   @Autowired
   private GltService gltService;
   @Autowired
   private FileService fileService;
   
   public ImpExpConfig getExpImpConfig(String configId)
     throws AppException
   {
     return ImpExpAbstract.getExpImpConfig(configId);
   }
   
   public File exportFile(IDTO dto)
     throws AppException
   {
     Map<String, Object> map = dto.getData();
     String configId = (String)map.get("configId");
     String[] hashWhereCls = (String[])Util.toTypedArray(
       map.get("hashWhereCls"), String.class);
     String[] listWhereCls = (String[])Util.toTypedArray(
       map.get("listWhereCls"), String.class);
     String dynDictWhereCls = (String)map.get("dynDictWhereCls");
     ImpExpAbstract.ImpExpParameter excelParameter = new ImpExpAbstract.ImpExpParameter();
     excelParameter.hashWhereCls = hashWhereCls;
     excelParameter.listWhereCls = listWhereCls;
     excelParameter.addData = map;
     excelParameter.dyndictWhereCls = 
       FileService.extractDictWhereCls(dynDictWhereCls);
     ExcelImpExp impexp = null;
     try
     {
       impexp = new ExcelImpExp(getExpImpConfig(configId));
       return impexp.exportExcel(excelParameter);
     }
     catch (IOException e)
     {
       throw new AppException(e.getLocalizedMessage(), e);
     }
     finally
     {
       FileService.release(new Closeable[] {impexp });
     }
   }
   
   public File exportGlt(IDTO dto)
     throws AppException
   {
     Map<String, Object> map = dto.getData();
     String gltId = (String)map.get("configId");
     String[] hashWhereCls = (String[])Util.toTypedArray(
       map.get("hashWhereCls"), String.class);
     String[] listWhereCls = (String[])Util.toTypedArray(
       map.get("listWhereCls"), String.class);
     String dynDictWhereCls = (String)map.get("dynDictWhereCls");
     ImpExpAbstract.ImpExpParameter excelParameter = new ImpExpAbstract.ImpExpParameter();
     excelParameter.hashWhereCls = hashWhereCls;
     excelParameter.listWhereCls = listWhereCls;
     excelParameter.addData = new HashMap();
     excelParameter.dyndictWhereCls = 
       FileService.extractDictWhereCls(dynDictWhereCls);
     Glt glt = this.gltService.getGlt(gltId);
     ExcelImpExp impexp = null;
     try
     {
       impexp = ExcelImpExp.prepareGltExp(glt);
       return impexp.exportExcel(excelParameter);
     }
     catch (Exception e)
     {
       throw new AppException(e.getLocalizedMessage(), e);
     }
     finally
     {
       FileService.release(new Closeable[] {impexp });
     }
   }
   
   public Object importFile(File[] files, IDTO dto)
     throws Exception
   {
     return this.fileService.importFile(files, dto);
   }
 }