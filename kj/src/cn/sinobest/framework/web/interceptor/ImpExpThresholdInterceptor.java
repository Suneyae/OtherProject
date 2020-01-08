 package cn.sinobest.framework.web.interceptor;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.util.ConfUtil;
 import com.opensymphony.xwork2.ActionContext;
 import com.opensymphony.xwork2.ActionInvocation;
 import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
 import java.util.concurrent.atomic.AtomicInteger;
 import javax.servlet.http.HttpServletRequest;
 import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
 
 public class ImpExpThresholdInterceptor
   extends AbstractInterceptor
 {
   private static final long serialVersionUID = 1L;
   static final int MAX_UPLOAD = getIntConfig("export.maxThreasholdInaTime");
   static final int MAX_DOWNLOAD = getIntConfig("export.maxThreasholdInaTime");
   private static AtomicInteger currentUpload = new AtomicInteger();
   private static AtomicInteger currentDownload = new AtomicInteger();
   static final String KEY_ACTION_INIT = "__" + 
     ImpExpThresholdInterceptor.class.getName() + "_init";
   boolean download;
   
   public static int getIntConfig(String key)
     throws AppException
   {
     return (int)ConfUtil.getSysParam2Number(key, 40L);
   }
   
   public String intercept(ActionInvocation invocation)
     throws Exception
   {
     ActionContext ac = invocation.getInvocationContext();
     HttpServletRequest request = (HttpServletRequest)ac
       .get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
     
     boolean init = false;
     try
     {
       if (request.getAttribute(KEY_ACTION_INIT) == null)
       {
         if (((request instanceof MultiPartRequestWrapper)) && 
           (currentUpload.incrementAndGet() > MAX_UPLOAD))
         {
           currentUpload.decrementAndGet();
           throw new AppException("EFW0202", null, new Object[] { Integer.valueOf(MAX_UPLOAD) });
         }
         if ((this.download) && 
           (currentDownload.incrementAndGet() > MAX_DOWNLOAD))
         {
           currentDownload.decrementAndGet();
           throw new AppException("EFW0203", null, new Object[] { Integer.valueOf(MAX_DOWNLOAD) });
         }
         init = true;
       }
       return invocation.invoke();
     }
     finally
     {
       if (init)
       {
         if ((request instanceof MultiPartRequestWrapper)) {
           currentUpload.decrementAndGet();
         }
         if (this.download) {
           currentDownload.decrementAndGet();
         }
       }
     }
   }
   
   public void setDownload(boolean download)
   {
     this.download = download;
   }
 }