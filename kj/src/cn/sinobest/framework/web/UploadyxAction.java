 package cn.sinobest.framework.web;
 
 import org.apache.struts2.convention.annotation.Results;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 @Results({@org.apache.struts2.convention.annotation.Result(name="toUploadYX", location="/jsp/framework/uploadYX_i.jsp")})
 public class UploadyxAction
   extends BaseActionSupport
 {
   private static final long serialVersionUID = 1L;
   private static final Logger LOG = LoggerFactory.getLogger(UploadyxAction.class);
   
   public String execute()
     throws Exception
   {
     return "toUploadYX";
   }
 }