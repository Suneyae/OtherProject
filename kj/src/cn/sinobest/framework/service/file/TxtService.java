 package cn.sinobest.framework.service.file;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.comm.iface.IExportService;
 import cn.sinobest.framework.util.Util;
 import java.io.Closeable;
 import java.io.File;
 import java.io.IOException;
 import java.util.Map;
 import org.springframework.stereotype.Service;
 
 @Service
 public class TxtService
   implements IExportService
 {
   public static final String KEY_CONFIGID = "configId";
   public static final String KEY_HASHWHERECLS = "hashWhereCls";
   public static final String KEY_LISTWHERECLS = "listWhereCls";
   public static final String KEY_DYNDICTSWHERECLS = "dynDictWhereCls";
   
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
     String[] hashWhereCls = (String[])Util.toTypedArray(map.get("hashWhereCls"), String.class);
     String[] listWhereCls = (String[])Util.toTypedArray(map.get("listWhereCls"), String.class);
     String dynDictWhereCls = (String)map.get("dynDictWhereCls");
     
     TextImpExp impexp = null;
     try
     {
       impexp = new TextImpExp(getExpImpConfig(configId));
       
       impexp.prepareData(dto);
       ImpExpAbstract.ImpExpParameter txtParameter = new ImpExpAbstract.ImpExpParameter();
       txtParameter.hashWhereCls = hashWhereCls;
       txtParameter.listWhereCls = listWhereCls;
       txtParameter.addData = dto.getData();
       txtParameter.dyndictWhereCls = FileService.extractDictWhereCls(dynDictWhereCls);
       return impexp.exportTxt(txtParameter);
     }
     catch (IOException e)
     {
       new AppException("出现IO异常，" + e.getLocalizedMessage(), e);
     }
     catch (AppException ex)
     {
       throw ex;
     }
     finally
     {
       FileService.release(new Closeable[] {impexp });
     }
     return null;
   }
 }