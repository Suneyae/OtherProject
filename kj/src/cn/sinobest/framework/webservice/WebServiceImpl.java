 package cn.sinobest.framework.webservice;
 
 import java.io.PrintStream;
 import javax.jws.WebService;
 
 @WebService(endpointInterface="cn.sinobest.framework.webservice.IWebService")
 public class WebServiceImpl
   implements IWebService
 {
   public String doAction(String args)
   {
     System.out.println("WebService 入参:" + args);
     String returnStr = "WebService 返回:" + args;
     System.out.println(returnStr);
     return returnStr;
   }
 }