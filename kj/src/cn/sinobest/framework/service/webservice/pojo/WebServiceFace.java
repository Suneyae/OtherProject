 package cn.sinobest.framework.service.webservice.pojo;
 
 import cn.sinobest.framework.comm.Environment;
 import cn.sinobest.framework.comm.dto.DTO;
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.comm.iface.IHisProcessor;
 import cn.sinobest.framework.comm.iface.IOperator;
 import cn.sinobest.framework.service.webservice.HisTransService;
 import cn.sinobest.framework.service.webservice.WebService;
 import cn.sinobest.framework.service.webservice.WebServiceContext;
 import cn.sinobest.framework.util.CompressUtil;
 import cn.sinobest.framework.util.Util;
 import cn.sinobest.framework.web.his.FuncModel;
 import cn.sinobest.framework.web.his.HisUtil;
 import cn.sinobest.framework.web.his.MsgModelUtil;
 import cn.sinobest.framework.web.his.MsgModelUtil.MsgFormat;
 import cn.sinobest.framework.web.his.XMLBuilder;
 import java.util.HashMap;
 import java.util.Map;
 import org.apache.axis2.context.MessageContext;
 import org.apache.axis2.context.ServiceGroupContext;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 public class WebServiceFace
 {
   private static final Logger LOGGER = LoggerFactory.getLogger(WebServiceFace.class);
   
   public String doAction(String options, String args)
     throws Exception
   {
     String ouData = "";
     String isCompress = "false";
     FuncModel reqFuncModel = null;FuncModel respFuncModel = null;
     long backExecNum = 0L;
     MsgModelUtil.MsgFormat format = MsgModelUtil.MsgFormat.XML;
     try
     {
       if (LOGGER.isDebugEnabled()) {
         LOGGER.debug("接收数据:" + args);
       }
       Map<String, String> optMap = (Map)Util.json2Obj(options);
       LOGGER.debug("LGC DEBUG optsMap: " + optMap);
       LOGGER.debug("LGC DEBUG options: " + options);
       format = MsgModelUtil.getMsgFormatByName((String)optMap.get("MESSAGE_FORMAT"));
       LOGGER.debug("LGC DEBUG format: " + (String)optMap.get("MESSAGE_FORMAT"));
       LOGGER.debug("LGC DEBUG format: " + format.equals(MsgModelUtil.MsgFormat.JSON));
       String sessionId = (String)optMap.get("SESSOINID");
       isCompress = (String)optMap.get("Compress");
       String execNum = (String)optMap.get("ExecNum");
       String hisIp = (String)optMap.get("HisIP");
       String funcNo = (String)optMap.get("FuncNo");
       Boolean isTranFun = Boolean.valueOf(Boolean.parseBoolean((String)optMap.get("isTranFn")));
       String hisLsh = HisUtil.getLSH();
       IDTO dto2 = new DTO();
       dto2.setValue("YWLSH", hisLsh);
       if (isTranFun.booleanValue())
       {
         dto2.setValue("FUNC_NO", funcNo);
         dto2.setValue("IP", hisIp);
         dto2.setValue("EXEC_NUM", execNum);
         HisTransService hisTransService = (HisTransService)Util.getBean("hisTransService", HisTransService.class);
         backExecNum = hisTransService.updateExecNum(dto2);
       }
       if (!checkLogin(sessionId))
       {
         LOGGER.info("未登录用户,请先登录,谢谢!");
         String str1 = MsgModelUtil.createMsg("-1", "未登录用户,请先登录,谢谢!", "", format);return str1;
       }
       IDTO dto2;
       String hisLsh;
       Boolean isTranFun;
       String funcNo;
       String hisIp;
       String execNum;
       String sessionId;
       Map<String, String> optMap;
       WebService webService = (WebService)Util.getBean("webService", WebService.class);
       IDTO dto = new DTO();
       dto.setValue("PI_HIS_LSH", hisLsh);
       IOperator userInfo = getUserInfo(sessionId);
       String inData = args;
       if ("true".equals(isCompress))
       {
         inData = CompressUtil.decompress(args);
         if (LOGGER.isDebugEnabled()) {
           LOGGER.debug("解压数据:" + inData);
         }
       }
       reqFuncModel = MsgModelUtil.msg2model(inData, format);
       reqFuncModel.addParam("OPERATOR", userInfo);
       reqFuncModel.addParam("AAZ107", WebServiceContext.getContextProperty("AAZ107"));
       dto.setUserInfo(userInfo);
       dto.setValue("INITIALIZER", Environment.hisInitializer);
       dto.setValue("inFuncModel", reqFuncModel);
       
 
       IDTO outDto = webService.doProcess(dto);
       respFuncModel = (FuncModel)outDto.getValue("outFuncModel");
       
 
 
 
 
 
 
 
 
 
 
       ouData = MsgModelUtil.outputmodel2Msg(respFuncModel, format);
       if (LOGGER.isDebugEnabled()) {
         LOGGER.debug("返回数据:" + ouData);
       }
       if ("true".equals(isCompress)) {
         ouData = CompressUtil.compress(ouData.getBytes(IHisProcessor.COMPRESS_CHARACTER_ENCODING));
       }
       Map<String, String> respMap = new HashMap(2);
       respMap.put("isCompress", isCompress);
       respMap.put("ExecNum", String.valueOf(backExecNum));
       respMap.put("RESP_DATA", ouData);
       ouData = Util.toJson(respMap);
     }
     catch (Exception ex)
     {
       LOGGER.error("业务处理出错," + ex.getMessage(), ex);
       try
       {
         ouData = MsgModelUtil.createMsg("-1", "业务处理出错，" + ex.getLocalizedMessage(), Util.exception2String(ex, -1), format);
         if ("true".equals(isCompress)) {
           ouData = CompressUtil.compress(ouData.getBytes());
         }
         Map<String, String> respMap = new HashMap(2);
         respMap.put("isCompress", isCompress);
         respMap.put("RESP_DATA", ouData);
         respMap.put("ExecNum", String.valueOf(backExecNum));
         ouData = Util.toJson(respMap);
       }
       catch (Exception e)
       {
         LOGGER.error("构造" + format + "出错," + ex.getMessage(), ex);
         throw e;
       }
     }
     finally
     {
       reqFuncModel = null;
       respFuncModel = null;
     }
     return ouData;
   }
   
   private boolean checkLogin(String sessionId)
   {
     MessageContext mc = MessageContext.getCurrentMessageContext();
     ServiceGroupContext sc = mc.getServiceGroupContext();
     if (sc.getProperty(sessionId) != null) {
       return true;
     }
     return false;
   }
   
   private IOperator getUserInfo(String sessionId)
   {
     MessageContext mc = MessageContext.getCurrentMessageContext();
     ServiceGroupContext sc = mc.getServiceGroupContext();
     return (IOperator)sc.getProperty(sessionId);
   }
   
   public static String createXml(String fhz, String msg, String helpMsg)
     throws Exception
   {
     XMLBuilder xmlBuilder = null;
     FuncModel fm = null;
     try
     {
       fm = new FuncModel();
       fm.addParam("FHZ", fhz);
       fm.addParam("MSG", msg);
       fm.addParam("HELPMSG", helpMsg);
       xmlBuilder = new XMLBuilder();
       String xmlStr = xmlBuilder.buildXml(fm);
       return xmlStr;
     }
     finally
     {
       fm = null;
       xmlBuilder = null;
     }
   }
 }