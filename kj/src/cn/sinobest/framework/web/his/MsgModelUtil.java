 package cn.sinobest.framework.web.his;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import java.io.PrintStream;
 import java.util.HashMap;
 
 public class MsgModelUtil
 {
   public static final String MESSAGE_FORMAT = "MESSAGE_FORMAT";
   
   public static enum MsgFormat
   {
     XML,  JSON;
   }
   
   public static FuncModel msg2funcmodel(String str)
     throws Exception
   {
     return msg2funcmodel(str, getMsgFormat(str));
   }
   
   public static FuncModel msg2funcmodel(String str, MsgFormat format)
     throws Exception
   {
     FuncModel funcmodel = null;
     if (format.equals(MsgFormat.JSON))
     {
       JsonManager jsonManager = new JsonManager();
       String tmpStr = jsonManager.removeDataPackage(str);
       funcmodel = jsonManager.json2model(tmpStr);
     }
     else
     {
       XMLSAXParser xmlparser = new XMLSAXParser();
       String tmpStr = str.substring(str.indexOf("<FunctionParams>"), str.indexOf("</FunctionParams>") + 17);
       funcmodel = xmlparser.getInputData(tmpStr);
     }
     return funcmodel;
   }
   
   public static FuncModel msg2model(String str, MsgFormat format)
     throws Exception
   {
     FuncModel funcmodel = null;
     if (format.equals(MsgFormat.JSON))
     {
       JsonManager jsonManager = new JsonManager();
       funcmodel = jsonManager.json2model(str);
     }
     else
     {
       XMLSAXParser xmlparser = new XMLSAXParser();
       funcmodel = xmlparser.getInputData(str);
     }
     return funcmodel;
   }
   
   public static MsgFormat getMsgFormat(String str)
   {
     if ((str.indexOf("<HNBridge>") == -1) && (str.indexOf("</HNBridge>") == -1)) {
       return MsgFormat.JSON;
     }
     return MsgFormat.XML;
   }
   
   public static MsgFormat getMsgFormatByName(String str)
   {
     if ((str != null) && (str.equalsIgnoreCase("json"))) {
       return MsgFormat.JSON;
     }
     return MsgFormat.XML;
   }
   
   public static String model2Msg(FuncModel model, MsgFormat format)
     throws Exception
   {
     if (format.equals(MsgFormat.JSON)) {
       return new JsonManager().model2json(model);
     }
     return new XMLBuilder().buildXml(model);
   }
   
   public static String outputmodel2Msg(FuncModel outputFuncModel, MsgFormat format)
     throws Exception
   {
     if (format.equals(MsgFormat.JSON))
     {
       HashMap RParams = outputFuncModel.getParamsMap();
       RParams.put("SESSIONID", outputFuncModel.getSessionID());
       if ((RParams.isEmpty()) || (RParams.get("FHZ") == null) || (RParams.get("FHZ").toString().trim().equals("")))
       {
         RParams.put("FHZ", "-1");
         String errMsg = outputFuncModel.getErrMsg();
         if ((errMsg == null) || (errMsg.length() == 0)) {
           errMsg = "不能与应用服务器通讯，可能的原因是应用服务器未启动或已关闭、系统连接超时或业务逻辑出错！\r\n如果是应用服务器未启动或已关闭，需在应用服务器启动后重新启动前置服务!";
         }
         RParams.put("MSG", errMsg);
         String helpMsg = outputFuncModel.getHelpMsg();
         if ((helpMsg == null) || (helpMsg.length() == 0)) {
           helpMsg = "发生错误!请联系系统管理员";
         }
         RParams.put("HELPMSG", helpMsg);
       }
       return new JsonManager().model2json(outputFuncModel);
     }
     return new XMLBuilder().buildXml(outputFuncModel);
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
   
   public static String createJson(String fhz, String msg, String helpMsg)
     throws Exception
   {
     JsonManager jsonManager = null;
     FuncModel fm = null;
     try
     {
       fm = new FuncModel();
       fm.addParam("FHZ", fhz);
       fm.addParam("MSG", msg);
       fm.addParam("HELPMSG", helpMsg);
       jsonManager = new JsonManager();
       String jsonStr = jsonManager.model2json(fm);
       return jsonStr;
     }
     finally
     {
       fm = null;
       jsonManager = null;
     }
   }
   
   public static String createMsg(String fhz, String msg, String helpMsg, MsgFormat format)
     throws Exception
   {
     if (format.equals(MsgFormat.JSON)) {
       return createJson(fhz, msg, helpMsg);
     }
     return createXml(fhz, msg, helpMsg);
   }
   
   public static String toUnicodeString(String s)
   {
     StringBuilder strb = new StringBuilder();
     char[] c = s.toCharArray();
     int i = 0;
     for (int len = c.length; i < len; i++) {
       if (c[i] < '')
       {
         strb.append(c[i]);
       }
       else
       {
         strb.append("\\u");
         String hex = Integer.toHexString(c[i]);
         if (hex.length() < 4)
         {
           int padLen = 4 - hex.length();
           for (int k = 0; k < padLen; k++) {
             hex = "0" + hex;
           }
         }
         strb.append(hex);
       }
     }
     return strb.toString();
   }
   
   public static String parseUnicodeString(String s)
   {
     StringBuilder rtn = new StringBuilder();
     int i = 0;
     for (int len = s.length(); i < len; i++)
     {
       char c = s.charAt(i);
       if (c == '\\')
       {
         i++;
         char c1 = s.charAt(i);
         if (c1 == 'u') {
           try
           {
             char c_chinese = (char)Integer.parseInt(s.substring(i + 1, i + 1 + 4), 16);
             rtn.append(c_chinese);
             i += 4;
           }
           catch (Exception e)
           {
             throw new AppException("parseUnicodeString出错", e);
           }
         } else {
           rtn.append(c).append(c1);
         }
       }
       else
       {
         rtn.append(c);
       }
     }
     return rtn.toString();
   }
   
   public static void main(String[] args)
   {
     String s = "JSON";
     System.out.println(getMsgFormatByName(s));
     String unicodeStr = toUnicodeString("×");
     System.out.println(unicodeStr);
     System.out.println(parseUnicodeString(unicodeStr));
   }
 }