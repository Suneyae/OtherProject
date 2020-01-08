 package cn.sinobest.framework.service.webservice;
 
 import cn.sinobest.framework.comm.dto.DTO;
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.comm.iface.IHisProcessor;
 import cn.sinobest.framework.util.ConfUtil;
 import cn.sinobest.framework.util.Util;
 import cn.sinobest.framework.web.his.FuncModel;
 import cn.sinobest.framework.web.his.IHisInitializer;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.stereotype.Service;
 
 @Service
 public class WebService
 {
   private static final Logger LOGGER = LoggerFactory.getLogger(WebService.class);
   private static Class<?> cs = null;
   
   static
   {
     try
     {
       cs = Class.forName(ConfUtil.getSysParamOnly("his.processor", ""));
     }
     catch (Exception ex)
     {
       LOGGER.warn("加载系统参数his.processor出错," + ex.getLocalizedMessage(), ex);
     }
   }
   
   public IDTO doProcess(IDTO dto)
     throws Exception
   {
     LOGGER.info("dto=>" + Util.getMapLogString(dto.getData()));
     IHisProcessor hisProcessor = (IHisProcessor)cs.newInstance();
     IHisInitializer hisInitializer = (IHisInitializer)dto.getValue("INITIALIZER");
     FuncModel reqFuncModel = (FuncModel)dto.getValue("inFuncModel");
     FuncModel respFuncModel = hisProcessor.processModel(hisInitializer, reqFuncModel);
     IDTO outDto = new DTO();
     outDto.setValue("outFuncModel", respFuncModel);
     return outDto;
   }
 }