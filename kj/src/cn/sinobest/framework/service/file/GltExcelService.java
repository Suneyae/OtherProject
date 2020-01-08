 package cn.sinobest.framework.service.file;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IDTO;
 import java.io.File;
 import org.springframework.stereotype.Service;
 
 @Service
 public class GltExcelService
   extends ExcelService
 {
   public File exportFile(IDTO dto)
     throws AppException
   {
     return super.exportGlt(dto);
   }
 }