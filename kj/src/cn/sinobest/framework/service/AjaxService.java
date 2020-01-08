 package cn.sinobest.framework.service;
 
 import cn.sinobest.framework.comm.dto.DTO;
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.util.Util;
 import java.lang.reflect.InvocationTargetException;
 import java.lang.reflect.Method;
 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.Collections;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import java.util.Map.Entry;
 import java.util.Set;
 import org.apache.log4j.Logger;
 import org.springframework.stereotype.Service;
 
 @Service
 public class AjaxService
 {
   private static final Logger LOGGER = Logger.getLogger(AjaxService.class);
   
   static enum Transform
   {
     FIRSTROW,  BREAKDOWN;
   }
   
   public Object findService(String serviceId, Object params)
   {
     return Util.getBean(serviceId);
   }
   
   public Method locateMehod(Object service, String methodName, Map<String, ?> params)
   {
     if (service == null) {
       throw new AppException("AjaxService.locateMehod.service_required");
     }
     Class<?> kclass = service.getClass();
     try
     {
       return kclass.getMethod(methodName, new Class[] { IDTO.class });
     }
     catch (SecurityException e)
     {
       throw new AppException("AjaxService.locateMehod.method_forbid", e, new Object[] { methodName });
     }
     catch (NoSuchMethodException e)
     {
       throw new AppException("AjaxService.locateMehod.method_notfound", e, new Object[] { methodName });
     }
   }
   
   public Object invokeMethod(Object service, Method method, IDTO dto)
     throws Exception
   {
     try
     {
       if (dto == null) {
         return method.invoke(service, new Object[0]);
       }
       return method.invoke(service, new Object[] { dto });
     }
     catch (InvocationTargetException e)
     {
       throw ((Exception)e.getCause());
     }
     catch (Exception e)
     {
       throw new AppException("AjaxService.invokeMethod.method_exception", e, new Object[] { method.getName() });
     }
   }
   
   public List<Object> doService(List<Map<String, Object>> parameters, Map<String, Object> shareArguments)
     throws Exception
   {
     if (LOGGER.isDebugEnabled())
     {
       LOGGER.debug("处理ajax业务，入参串是：" + parameters);
       LOGGER.debug(">>>共享参数是：" + shareArguments);
     }
     List<Object> resultList = new ArrayList();
     for (Map<String, Object> serviceMap : parameters)
     {
       String serviceId = (String)serviceMap.get("serviceId");
       String methodName = (String)serviceMap.get("method");
       Object useShare = serviceMap.get("useShare");
       String transform = (String)serviceMap.get("transform");
       if ((serviceId.length() == 0) || (methodName.length() == 0)) {
         throw new AppException("AjaxAction.execute.serviceId&method_required");
       }
       Map<String, Object> serviceParameters = (Map)serviceMap.get("parameters");
       
       Map<String, Object> arguments = new HashMap();
       if ((shareArguments != null) && (useShare != null)) {
         if (Boolean.TRUE.equals(useShare)) {
           arguments.putAll(shareArguments);
         } else {
           for (Map.Entry<String, Object> entry : ((Map)useShare).entrySet())
           {
             Object value = entry.getValue();
             if ((value instanceof Collection)) {
               for (String c_value : (Collection)value) {
                 arguments.put(c_value, shareArguments.get(entry.getKey()));
               }
             } else {
               arguments.put((String)value, shareArguments.get(entry.getKey()));
             }
           }
         }
       }
       if (serviceParameters != null) {
         arguments.putAll(serviceParameters);
       }
       Object service = findService(serviceId, arguments);
       Method method = locateMehod(service, methodName, arguments);
       IDTO dto = new DTO();
       dto.setData(arguments);
       Object rst = invokeMethod(service, method, dto);
       if ((rst != null) && ((rst instanceof List)) && (!((List)rst).isEmpty()) && (transform != null)) {
         switch (Transform.valueOf(transform.toUpperCase()))
         {
         case BREAKDOWN: 
           rst = ((List)rst).get(0);
           break;
         default: 
           rst = breakDown((List)rst);
         }
       }
       resultList.add(rst);
       if ((rst != null) && (serviceMap.get("shareResults") != null))
       {
         Object shareResult = serviceMap.get("shareResults");
         if (!Boolean.FALSE.equals(shareResult)) {
           if (Boolean.TRUE.equals(shareResult))
           {
             Object t = uniformResult(rst);
             if (t != null) {
               shareArguments.putAll(uniformResult(rst));
             }
           }
           else
           {
             Map<String, Object> resultMap = uniformResult(rst);
             for (Map.Entry<String, Object> entry : ((Map)shareResult).entrySet())
             {
               Object value = entry.getValue();
               if ((value instanceof Collection)) {
                 for (String c_value : (Collection)value) {
                   shareArguments.put(c_value, resultMap.get(entry.getKey()));
                 }
               } else {
                 shareArguments.put((String)value, resultMap.get(entry.getKey()));
               }
             }
           }
         }
       }
     }
     return resultList;
   }
   
   private Map<String, Object> breakDown(List<Map<String, Object>> rst)
   {
     if ((rst == null) || (rst.isEmpty())) {
       return null;
     }
     Object row = rst.get(0);
     
     Map<String, Object> rest = new HashMap();
     for (String key : ((Map)row).keySet()) {
       rest.put(key, new ArrayList(rst.size()));
     }
     Iterator localIterator2;
     for (??? = rst.iterator(); ???.hasNext(); localIterator2.hasNext())
     {
       Map<String, Object> oneRow = (Map)???.next();
       localIterator2 = oneRow.entrySet().iterator(); continue;Map.Entry<String, Object> key = (Map.Entry)localIterator2.next();
       ((Collection)rest.get(key.getKey())).add(key.getValue());
     }
     return rest;
   }
   
   private Map<String, Object> uniformResult(Object rst)
   {
     if ((rst instanceof Map)) {
       return (Map)rst;
     }
     if ((rst instanceof List)) {
       return Collections.singletonMap("", rst);
     }
     return Collections.singletonMap("", rst);
   }
 }