 package cn.sinobest.framework.web.his;
 
 import cn.sinobest.framework.comm.Environment;
 import cn.sinobest.framework.comm.dto.DTO;
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.comm.iface.IHisProcessor;
 import cn.sinobest.framework.util.ConfUtil;
 import cn.sinobest.framework.util.Util;
 import java.io.IOException;
 import javax.servlet.ServletException;
 import javax.servlet.ServletOutputStream;
 import javax.servlet.http.HttpServlet;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 public class MainServlet
   extends HttpServlet
 {
   private static final long serialVersionUID = 28454416916187676L;
   private static final Logger LOGGER = LoggerFactory.getLogger(MainServlet.class);
   private static Class cs = null;
   
   public void doGet(HttpServletRequest request, HttpServletResponse response)
     throws IOException, ServletException
   {
     doProcess(request, response);
   }
   
   public void doPost(HttpServletRequest request, HttpServletResponse response)
     throws IOException, ServletException
   {
     doProcess(request, response);
   }
   
   private void doProcess(HttpServletRequest request, HttpServletResponse response)
   {
     MsgModelUtil.MsgFormat format = MsgModelUtil.getMsgFormatByName(request.getHeader("MESSAGE_FORMAT"));
     
     String respMsg = "系统错误";
     ServletOutputStream out = null;
     try
     {
       if (cs == null) {
         init();
       }
       IHisProcessor hisProcessor = (IHisProcessor)cs.newInstance();
       IDTO dto = new DTO();
       dto.setValue("http_request", request);
       dto.setValue("http_response", response);
       dto.setValue("INITIALIZER", Environment.hisInitializer);
       hisProcessor.handle(dto);
     }
     catch (Exception e)
     {
       e.printStackTrace();
       String msg = Util.exception2String(e, 20);
       try
       {
         respMsg = MsgModelUtil.createMsg("-1", "系统错误,详细：doProcess调用出错，" + e.getLocalizedMessage(), msg, format);
       }
       catch (Exception e1)
       {
         LOGGER.error(e1.getLocalizedMessage(), e1);
       }
       try
       {
         response.setContentType("text/html;charset=" + IHisProcessor.RESP_CHARACTER_ENCODING);
         response.setCharacterEncoding(IHisProcessor.RESP_CHARACTER_ENCODING);
         response.setHeader("Content-Length", String.valueOf(respMsg.getBytes(IHisProcessor.RESP_CHARACTER_ENCODING).length));
         out = response.getOutputStream();
         out.println(respMsg);
         out.flush();
       }
       catch (IOException e1)
       {
         LOGGER.error(e1.getLocalizedMessage(), e1);
       }
     }
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
   
   public static String createMsg(String fhz, String msg, String helpMsg, MsgModelUtil.MsgFormat format)
     throws Exception
   {
     if (format.equals(MsgModelUtil.MsgFormat.JSON)) {
       return createJson(fhz, msg, helpMsg);
     }
     return createXml(fhz, msg, helpMsg);
   }
   
   public void init()
   {
     try
     {
       cs = Class.forName(ConfUtil.getSysParamOnly("his.processor", ""));
       
       IHisProcessor hisProcessor = (IHisProcessor)cs.newInstance();
       Environment.hisInitializer = hisProcessor.hisInit();
       Environment.hisInitializer.init();
     }
     catch (Exception ex)
     {
       LOGGER.warn("加载系统参数his.processor出错," + ex.getLocalizedMessage(), ex);
     }
     if (LOGGER.isInfoEnabled()) {
       LOGGER.info("MainServlet initialized.");
     }
   }
   
   public void destroy()
   {
     if (Environment.hisInitializer != null)
     {
       Environment.hisInitializer.clear();
       Environment.hisInitializer = null;
     }
     if (LOGGER.isInfoEnabled()) {
       LOGGER.info("MainServlet initialized.");
     }
   }
 }