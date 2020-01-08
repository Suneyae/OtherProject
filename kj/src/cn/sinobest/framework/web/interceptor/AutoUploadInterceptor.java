 package cn.sinobest.framework.web.interceptor;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.comm.transcation.DataSourceCallBack;
 import cn.sinobest.framework.comm.transcation.IDataSourceCallBack;
 import cn.sinobest.framework.service.file.FileService;
 import cn.sinobest.framework.util.Util;
 import cn.sinobest.framework.web.BaseActionSupport;
 import com.opensymphony.xwork2.ActionContext;
 import com.opensymphony.xwork2.ActionInvocation;
 import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
 import java.io.File;
 import java.lang.reflect.Array;
 import java.util.Map;
 import java.util.Map.Entry;
 import javax.servlet.http.HttpServletRequest;
 import org.apache.log4j.Logger;
 import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
 
 public class AutoUploadInterceptor
   extends AbstractInterceptor
 {
   private static final long serialVersionUID = 1L;
   static Logger log = Logger.getLogger(AutoUploadInterceptor.class);
   private static final String KEY_CONFIGID_DEFAULT = "configId";
   private static final String KEY_DSID = "_ds";
   private static final String KEY_DYNDICTSWHERECLS_DEFAULT = "dynDictWhereCls";
   private static final String KEY_UPlOADFILE_DEFAULT = "_uploadFile";
   static final String EXCEL_SUFFIX = ".xls";
   static final String TEXT_SUFFIX = ".txt";
   static final String DBF_SUFFIX = ".dbf";
   protected String configId = "configId";
   protected String dynDictWhereCls = "dynDictWhereCls";
   
   public String intercept(ActionInvocation invocation)
     throws Exception
   {
     ActionContext ac = invocation.getInvocationContext();
     
     HttpServletRequest request = (HttpServletRequest)ac
       .get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
     if (!(request instanceof MultiPartRequestWrapper)) {
       return invocation.invoke();
     }
     if (!(invocation.getAction() instanceof BaseActionSupport)) {
       return invocation.invoke();
     }
     int fileType = -1;
     
     File[] files = (File[])null;
     String fileName = null;
     String[] filenames = (String[])null;
     
     Map<String, Object> params = ac.getParameters();
     
     String uploadFile = (String)Util.nvl(((BaseActionSupport)invocation.getAction())
       .getDto().getData().get("_uploadFile"));
     for (Map.Entry<String, Object> entry : params.entrySet()) {
       if ((Array.get(entry.getValue(), 0) instanceof File))
       {
         Object obj = params.get((String)entry.getKey() + "FileName");
         if (obj != null)
         {
           fileName = 
             ((String)Array.get(obj, 0)).toLowerCase();
           filenames = (String[])obj;
           if (uploadFile.equals("true")) {
             fileType = 3;
           } else if (fileName.endsWith(".xls")) {
             fileType = 0;
           } else if (fileName.endsWith(".txt")) {
             fileType = 1;
           } else if (fileName.endsWith(".dbf")) {
             fileType = 2;
           }
         }
         files = (File[])entry.getValue();
         break;
       }
     }
     if (fileType == -1) {
       return invocation.invoke();
     }
     final File[] fFiles = files;
     final Object action = invocation.getAction();
     String ds = 
       (String)((BaseActionSupport)invocation.getAction()).getDto().getData().get("_ds");
     
     ((BaseActionSupport)invocation.getAction())
       .getDto().getData().put("FILENAMES", filenames);
     
     final FileService fileService = (FileService)
       Util.getBean("fileService");
     ActionInvocation fInvocation = invocation;
     ((BaseActionSupport)invocation.getAction()).getDto().getData()
       .put("fileType", Integer.valueOf(fileType));
     DataSourceCallBack.execute(ds, new IDataSourceCallBack()
     {
       public String doAction()
         throws AppException
       {
         try
         {
           Object rtn = fileService.importFile(fFiles, 
             ((BaseActionSupport)action).getDto());
           ((BaseActionSupport)action).getDto().getData()
             .put("DRPH", rtn);
           return null;
         }
         catch (AppException e)
         {
           throw e;
         }
         catch (Exception e)
         {
           throw new AppException("EFW0201", e);
         }
       }
     });
     return fInvocation.invoke();
   }
 }