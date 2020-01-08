 package cn.sinobest.framework.service.webservice.pojo;
 
 import cn.sinobest.framework.comm.iface.IHisProcessor;
 import cn.sinobest.framework.comm.iface.IOperator;
 import cn.sinobest.framework.util.ConfUtil;
 import cn.sinobest.framework.util.Util;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.UUID;
 import org.apache.axis2.context.MessageContext;
 import org.apache.axis2.context.ServiceGroupContext;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 public class LoginService
 {
   private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);
   private static final String STATE = "state";
   private static final String KEY = "key";
   private static final String ERR = "err";
   private static Class cs = null;
   
   public String login(String args)
   {
     Map<String, String> respMap = new HashMap(2);
     String respStr = "";
     try
     {
       cs = Class.forName(ConfUtil.getSysParamOnly("his.processor", ""));
       Map<String, String> argMap = (Map)Util.json2Obj(args);
       String ip = (String)argMap.get("ip");
       String username = (String)argMap.get("loginId");
       String password = (String)argMap.get("password");
       String yybh = (String)argMap.get("yybh");
       
 
       IHisProcessor hisProcessor = (IHisProcessor)cs.newInstance();
       IOperator userInfo = hisProcessor.getUserInfo(yybh, username, password);
       if (userInfo.getCheckResult())
       {
         MessageContext mc = MessageContext.getCurrentMessageContext();
         ServiceGroupContext sc = mc.getServiceGroupContext();
         LOGGER.info("成功登录");
         String key = ip + ":" + UUID.randomUUID();
         sc.setProperty(key, userInfo);
         sc.setProperty("AAZ107", userInfo.getAAB001());
         sc.setLastTouchedTime(3600000L);
         respMap.put("state", "true");
         respMap.put("key", key);
       }
       else
       {
         LOGGER.info(userInfo.getCheckMsg());
         respMap.put("state", "false");
         respMap.put("err", userInfo.getCheckMsg());
       }
     }
     catch (Exception e)
     {
       respMap.put("state", "false");
       respMap.put("err", Util.exception2String(e, 25));
     }
     respStr = Util.toJson(respMap);
     return respStr;
   }
 }