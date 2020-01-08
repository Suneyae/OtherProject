 package cn.sinobest.framework.service.file;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.comm.iface.IExportService;
 import cn.sinobest.framework.service.json.JSONUtilities;
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
 public class CsvService
   implements IExportService
 {
   @Autowired
   GltService gltService;
   private static final String KEY_LISTWHERECLS = "listWhereCls";
   private static final String KEY_DYNDICTSWHERECLS = "dynDictWhereCls";
   private static final String KEY_CONFIGID = "configId";
   
   public File exportFile(IDTO dto)
     throws AppException
   {
     Map<String, Object> map = dto.getData();
     String gltId = (String)map.get("configId");
     String[] listWhereCls = (String[])Util.toTypedArray(map.get("listWhereCls"), String.class);
     String dynDictWhereCls = (String)map.get("dynDictWhereCls");
     ImpExpAbstract.ImpExpParameter excelParameter = new ImpExpAbstract.ImpExpParameter();
     excelParameter.listWhereCls = listWhereCls;
     excelParameter.addData = new HashMap();
     if ((dynDictWhereCls != null) && (dynDictWhereCls.trim().length() > 0))
     {
       Object obj = null;
       try
       {
         obj = JSONUtilities.parseJSON(dynDictWhereCls);
       }
       catch (AppException e)
       {
         throw e;
       }
       catch (Exception e)
       {
         throw new AppException("EFW0209", e, new Object[] { dynDictWhereCls });
       }
       if ((obj != null) && (!Map.class.isInstance(obj))) {
         throw new AppException("EFW0209", null, new Object[] { dynDictWhereCls });
       }
       excelParameter.dyndictWhereCls = ((Map)obj);
     }
     String whereCls = excelParameter.listWhereCls != null ? excelParameter.listWhereCls[0] : 
       null;
     excelParameter.addData = dto.getData();
     Glt glt = this.gltService.getGlt(gltId);
     CSVExp exp = null;
     try
     {
       exp = new CSVExp();
       return exp.exportCSV(glt, whereCls, excelParameter.dyndictWhereCls);
     }
     catch (IOException e)
     {
       throw new AppException(e.getLocalizedMessage(), e);
     }
     finally
     {
       FileService.release(new Closeable[] {exp });
     }
   }
 }