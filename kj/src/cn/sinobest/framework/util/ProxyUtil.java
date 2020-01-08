 package cn.sinobest.framework.util;
 
 import cn.sinobest.framework.comm.CommHandler;
 import java.lang.reflect.Proxy;
 
 public class ProxyUtil
 {
   public static <T> T getProxy(Object obj)
   {
     CommHandler cHandler = new CommHandler(obj);
     return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), cHandler);
   }
 }