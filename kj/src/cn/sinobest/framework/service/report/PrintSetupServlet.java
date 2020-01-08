 package cn.sinobest.framework.service.report;
 
 import cn.sinobest.framework.dao.CommDAO;
 import cn.sinobest.framework.util.Util;
 import java.io.PrintWriter;
 import java.util.HashMap;
 import java.util.Map;
 import javax.servlet.http.HttpServlet;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 public class PrintSetupServlet
   extends HttpServlet
 {
   private static final long serialVersionUID = 1L;
   private CommDAO commDAO = (CommDAO)Util.getBean("commDAO");
   private Logger LOGGER = LoggerFactory.getLogger(PrintSetupServlet.class);
   private final String N_CONFIGS = "FW_CONFIG.";
   
   public void service(HttpServletRequest request, HttpServletResponse response)
   {
     PrintWriter pw = null;
     label1215:
     try
     {
       request.setCharacterEncoding("GBK");
       response.setContentType("text/html; charset=GBK");
       pw = response.getWriter();
       String action = request.getParameter("action");
       String key = request.getParameter("key");
       if (this.LOGGER.isDebugEnabled()) {
         this.LOGGER.debug("处理报表向数据库读写打印设置请求: \naction=" + 
           action + 
           "\nkey=" + key);
       }
       if ("write".equalsIgnoreCase(action))
       {
         String width = request.getParameter("width");
         String height = request.getParameter("height");
         String x = request.getParameter("x");
         String y = request.getParameter("y");
         String w = request.getParameter("w");
         String h = request.getParameter("h");
         String orientation = request.getParameter("orientation");
         String printerName = request.getParameter("printerName");
         String a = request.getParameter("a");
         String scale = request.getParameter("scale");
         
         printerName = printerName.replaceAll("\\\\", "\\\\\\\\");
         if (this.LOGGER.isDebugEnabled()) {
           this.LOGGER.debug("从浏览器请求读取到的打印配置\nwidth=" + width + "\nheight=" + height + "\nx=" + x + "\ny=" + y + 
             "\nw=" + w + "\nh=" + h + 
             "\norientation=" + orientation + "\nprinterName=" + printerName + 
             "\na=" + a + "\nscale=" + scale);
         }
         Map<String, Object> pMap = new HashMap();
         pMap.put("KEYID", key);
         pMap.put("WIDTH", width);
         pMap.put("HEIGHT", height);
         pMap.put("X", x);
         pMap.put("Y", y);
         pMap.put("W", w);
         pMap.put("H", h);
         pMap.put("ORIENTATION", orientation);
         pMap.put("PRINTERNAME", printerName);
         pMap.put("A", a);
         pMap.put("SCALE", scale);
         Map<String, Object> countMap = this.commDAO.selectOne("FW_CONFIG.FW_PRINT_CLIENTCONFIG_CNT", pMap);
         long count = ((Number)countMap.get("COUNT")).longValue();
         if (this.LOGGER.isDebugEnabled()) {
           this.LOGGER.debug("数据库已存在记录数量(" + key + "): " + count);
         }
         if (1L <= count) {
           this.commDAO.update("FW_CONFIG.FW_PRINT_CLIENTCONFIG_U", pMap);
         } else {
           this.commDAO.insert("FW_CONFIG.FW_PRINT_CLIENTCONFIG_I", pMap);
         }
       }
       else
       {
         Map<String, Object> pMap2 = new HashMap(1, 1.0F);
         pMap2.put("KEYID", key);
         Map<String, Object> row = this.commDAO.selectOne("FW_CONFIG.FW_PRINT_CLIENTCONFIG_Q", pMap2);
         if (row != null)
         {
           String width = (String)row.get("WIDTH");
           String height = (String)row.get("HEIGHT");
           String x = (String)row.get("X");
           String y = (String)row.get("Y");
           String w = (String)row.get("W");
           String h = (String)row.get("H");
           String orientation = (String)row.get("ORIENTATION");
           String printerName = (String)row.get("PRINTERNAME");
           String a = (String)row.get("A");
           String scale = (String)row.get("SCALE");
           pw.println("width=" + width);
           pw.println("height=" + height);
           pw.println("x=" + x);
           pw.println("y=" + y);
           pw.println("w=" + w);
           pw.println("h=" + h);
           pw.println("orientation=" + orientation);
           pw.println("printerName=" + printerName);
           pw.println("a=" + a);
           pw.println("scale=" + scale);
           pw.println("setup=yes");
           pw.flush();
           if (this.LOGGER.isDebugEnabled())
           {
             this.LOGGER.debug("从数据库读取到的打印配置\nwidth=" + width + "\nheight=" + height + "\nx=" + x + "\ny=" + y + 
               "\nw=" + w + "\nh=" + h + 
               "\norientation=" + orientation + "\nprinterName=" + printerName + 
               "\na=" + a + "\nscale=" + scale);
             break label1215;
           }
         }
         else
         {
           if (this.LOGGER.isDebugEnabled()) {
             this.LOGGER.debug("没有读取到打印配置");
           }
           pw.println("setup=no");
           pw.flush();
         }
       }
     }
     catch (Exception e)
     {
       if (pw != null)
       {
         pw.println("setup=no");
         pw.flush();
       }
       e.printStackTrace();
     }
     finally
     {
       if (pw != null) {
         pw.close();
       }
     }
   }
 }