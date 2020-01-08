 package cn.sinobest.framework.util;
 
 import cn.sinobest.framework.comm.iface.IReflectionProvider;
 import java.beans.IntrospectionException;
 import java.beans.PropertyDescriptor;
 import java.lang.reflect.Field;
 import java.lang.reflect.Method;
 import java.util.Collection;
 import java.util.Map;
 import ognl.Ognl;
 import ognl.OgnlException;
 import ognl.OgnlRuntime;
 
 public class ReflectionProvider
   implements IReflectionProvider
 {
   private OgnlUtil ognlUtil;
   
   public ReflectionProvider()
   {
     this.ognlUtil = new OgnlUtil();
   }
   
   public void setOgnlUtil(OgnlUtil ognlUtil)
   {
     this.ognlUtil = ognlUtil;
   }
   
   public Field getField(Class inClass, String name)
   {
     return OgnlRuntime.getField(inClass, name);
   }
   
   public Method getGetMethod(Class targetClass, String propertyName)
     throws IntrospectionException, Exception
   {
     try
     {
       return OgnlRuntime.getGetMethod(null, targetClass, propertyName);
     }
     catch (OgnlException e)
     {
       throw new Exception(e);
     }
   }
   
   public Method getSetMethod(Class targetClass, String propertyName)
     throws IntrospectionException, Exception
   {
     try
     {
       return OgnlRuntime.getSetMethod(null, targetClass, propertyName);
     }
     catch (OgnlException e)
     {
       throw new Exception(e);
     }
   }
   
   public void setProperties(Map<String, String> props, Object o, Map<String, Object> context)
   {
     this.ognlUtil.setProperties(props, o, context);
   }
   
   public void setProperties(Map<String, String> props, Object o, Map<String, Object> context, boolean throwPropertyExceptions)
     throws Exception
   {
     this.ognlUtil.setProperties(props, o, context, throwPropertyExceptions);
   }
   
   public void setProperties(Map<String, String> properties, Object o)
   {
     this.ognlUtil.setProperties(properties, o);
   }
   
   public void setPropertiesWithObject(Map<String, ?> properties, Object o)
   {
     this.ognlUtil.setProperties(properties, o);
   }
   
   public PropertyDescriptor getPropertyDescriptor(Class targetClass, String propertyName)
     throws IntrospectionException, Exception
   {
     try
     {
       return OgnlRuntime.getPropertyDescriptor(targetClass, propertyName);
     }
     catch (OgnlException e)
     {
       throw new Exception(e);
     }
   }
   
   public void copy(Object from, Object to, Map<String, Object> context, Collection<String> exclusions, Collection<String> inclusions)
   {
     this.ognlUtil.copy(from, to, context, exclusions, inclusions);
   }
   
   public Object getRealTarget(String property, Map<String, Object> context, Object root)
     throws Exception
   {
     try
     {
       return this.ognlUtil.getRealTarget(property, context, root);
     }
     catch (OgnlException e)
     {
       throw new Exception(e);
     }
   }
   
   public void setProperty(String name, Object value, Object o, Map<String, Object> context)
   {
     this.ognlUtil.setProperty(name, value, o, context);
   }
   
   public void setProperty(String name, Object value, Object o, Map<String, Object> context, boolean throwPropertyExceptions)
   {
     this.ognlUtil.setProperty(name, value, o, context, throwPropertyExceptions);
   }
   
   public Map getBeanMap(Object source)
     throws IntrospectionException, Exception
   {
     try
     {
       return this.ognlUtil.getBeanMap(source);
     }
     catch (OgnlException e)
     {
       throw new Exception(e);
     }
   }
   
   public Object getValue(String expression, Map<String, Object> context, Object root)
     throws Exception
   {
     try
     {
       return this.ognlUtil.getValue(expression, context, root);
     }
     catch (OgnlException e)
     {
       throw new Exception(e);
     }
   }
   
   public void setValue(String expression, Map<String, Object> context, Object root, Object value)
     throws Exception
   {
     try
     {
       Ognl.setValue(expression, context, root, value);
     }
     catch (OgnlException e)
     {
       throw new Exception(e);
     }
   }
   
   public PropertyDescriptor[] getPropertyDescriptors(Object source)
     throws IntrospectionException
   {
     return this.ognlUtil.getPropertyDescriptors(source);
   }
   
   public static Class classForName(String name)
     throws ClassNotFoundException
   {
     try
     {
       ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
       if (contextClassLoader != null) {
         return contextClassLoader.loadClass(name);
       }
     }
     catch (Throwable localThrowable) {}
     return Class.forName(name);
   }
 }