 package cn.sinobest.framework.util;
 
 import com.opensymphony.xwork2.util.CompoundRoot;
 import com.opensymphony.xwork2.util.logging.Logger;
 import com.opensymphony.xwork2.util.logging.LoggerFactory;
 import com.opensymphony.xwork2.util.reflection.ReflectionException;
 import java.beans.BeanInfo;
 import java.beans.IntrospectionException;
 import java.beans.Introspector;
 import java.beans.PropertyDescriptor;
 import java.lang.reflect.Method;
 import java.util.Collection;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.Map.Entry;
 import java.util.concurrent.ConcurrentHashMap;
 import ognl.Ognl;
 import ognl.OgnlContext;
 import ognl.OgnlException;
 import ognl.OgnlRuntime;
 
 public class OgnlUtil
 {
   private static final Logger LOG = LoggerFactory.getLogger(OgnlUtil.class);
   private ConcurrentHashMap<String, Object> expressions = new ConcurrentHashMap();
   private final ConcurrentHashMap<Class, BeanInfo> beanInfoCache = new ConcurrentHashMap();
   static boolean devMode = false;
   static boolean enableExpressionCache = true;
   
   public void setProperties(Map<String, ?> props, Object o, Map<String, Object> context)
   {
     setProperties(props, o, context, false);
   }
   
   public void setProperties(Map<String, ?> props, Object o, Map<String, Object> context, boolean throwPropertyExceptions)
     throws ReflectionException
   {
     if (props == null) {
       return;
     }
     Object oldRoot = Ognl.getRoot(context);
     Ognl.setRoot(context, o);
     for (Map.Entry<String, ?> entry : props.entrySet())
     {
       String expression = (String)entry.getKey();
       internalSetProperty(expression, entry.getValue(), o, context, throwPropertyExceptions);
     }
     Ognl.setRoot(context, oldRoot);
   }
   
   public void setProperties(Map<String, ?> properties, Object o)
   {
     setProperties(properties, o, false);
   }
   
   public void setProperties(Map<String, ?> properties, Object o, boolean throwPropertyExceptions)
   {
     Map context = Ognl.createDefaultContext(o);
     setProperties(properties, o, context, throwPropertyExceptions);
   }
   
   public void setProperty(String name, Object value, Object o, Map<String, Object> context)
   {
     setProperty(name, value, o, context, false);
   }
   
   public void setProperty(String name, Object value, Object o, Map<String, Object> context, boolean throwPropertyExceptions)
   {
     Object oldRoot = Ognl.getRoot(context);
     Ognl.setRoot(context, o);
     
     internalSetProperty(name, value, o, context, throwPropertyExceptions);
     
     Ognl.setRoot(context, oldRoot);
   }
   
   public Object getRealTarget(String property, Map<String, Object> context, Object root)
     throws OgnlException
   {
     if ("top".equals(property)) {
       return root;
     }
     if ((root instanceof CompoundRoot))
     {
       CompoundRoot cr = (CompoundRoot)root;
       try
       {
         for (Object target : cr) {
           if ((OgnlRuntime.hasSetProperty((OgnlContext)context, target, property)) || 
           
             (OgnlRuntime.hasGetProperty((OgnlContext)context, target, property)) || 
             
             (OgnlRuntime.getIndexedPropertyType((OgnlContext)context, target.getClass(), property) != OgnlRuntime.INDEXED_PROPERTY_NONE)) {
             return target;
           }
         }
       }
       catch (IntrospectionException ex)
       {
         throw new ReflectionException("Cannot figure out real target class", ex);
       }
       return null;
     }
     return root;
   }
   
   public void setValue(String name, Map<String, Object> context, Object root, Object value)
     throws OgnlException
   {
     Ognl.setValue(compile(name), context, root, value);
   }
   
   public Object getValue(String name, Map<String, Object> context, Object root)
     throws OgnlException
   {
     return Ognl.getValue(compile(name), context, root);
   }
   
   public Object getValue(String name, Map<String, Object> context, Object root, Class resultType)
     throws OgnlException
   {
     return Ognl.getValue(compile(name), context, root, resultType);
   }
   
   public Object compile(String expression)
     throws OgnlException
   {
     if (enableExpressionCache)
     {
       Object o = this.expressions.get(expression);
       if (o == null)
       {
         o = Ognl.parseExpression(expression);
         this.expressions.put(expression, o);
       }
       return o;
     }
     return Ognl.parseExpression(expression);
   }
   
   public void copy(Object from, Object to, Map<String, Object> context, Collection<String> exclusions, Collection<String> inclusions)
   {
     if ((from == null) || (to == null))
     {
       LOG.warn("Attempting to copy from or to a null source. This is illegal and is bein skipped. This may be due to an error in an OGNL expression, action chaining, or some other event.", new String[0]);
       
       return;
     }
     Map contextFrom = Ognl.createDefaultContext(from);
     
     Map contextTo = Ognl.createDefaultContext(to);
     try
     {
       PropertyDescriptor[] fromPds = getPropertyDescriptors(from);
       toPds = getPropertyDescriptors(to);
     }
     catch (IntrospectionException e)
     {
       PropertyDescriptor[] toPds;
       LOG.error("An error occured", e, new String[0]); return;
     }
     PropertyDescriptor[] toPds;
     PropertyDescriptor[] fromPds;
     Map<String, PropertyDescriptor> toPdHash = new HashMap();
     for (PropertyDescriptor toPd : toPds) {
       toPdHash.put(toPd.getName(), toPd);
     }
     for (PropertyDescriptor fromPd : fromPds) {
       if (fromPd.getReadMethod() != null)
       {
         boolean copy = true;
         if ((exclusions != null) && (exclusions.contains(fromPd.getName()))) {
           copy = false;
         } else if ((inclusions != null) && (!inclusions.contains(fromPd.getName()))) {
           copy = false;
         }
         if (copy)
         {
           PropertyDescriptor toPd = (PropertyDescriptor)toPdHash.get(fromPd.getName());
           if ((toPd != null) && (toPd.getWriteMethod() != null)) {
             try
             {
               Object expr = compile(fromPd.getName());
               Object value = Ognl.getValue(expr, contextFrom, from);
               Ognl.setValue(expr, contextTo, to, value);
             }
             catch (OgnlException localOgnlException) {}
           }
         }
       }
     }
   }
   
   public void copy(Object from, Object to, Map<String, Object> context)
   {
     copy(from, to, context, null, null);
   }
   
   public PropertyDescriptor[] getPropertyDescriptors(Object source)
     throws IntrospectionException
   {
     BeanInfo beanInfo = getBeanInfo(source);
     return beanInfo.getPropertyDescriptors();
   }
   
   public PropertyDescriptor[] getPropertyDescriptors(Class clazz)
     throws IntrospectionException
   {
     BeanInfo beanInfo = getBeanInfo(clazz);
     return beanInfo.getPropertyDescriptors();
   }
   
   public Map getBeanMap(Object source)
     throws IntrospectionException, OgnlException
   {
     Map beanMap = new HashMap();
     Map sourceMap = Ognl.createDefaultContext(source);
     PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(source);
     for (PropertyDescriptor propertyDescriptor : propertyDescriptors)
     {
       String propertyName = propertyDescriptor.getDisplayName();
       Method readMethod = propertyDescriptor.getReadMethod();
       if (readMethod != null)
       {
         Object expr = compile(propertyName);
         Object value = Ognl.getValue(expr, sourceMap, source);
         beanMap.put(propertyName, value);
       }
       else
       {
         beanMap.put(propertyName, "There is no read method for " + propertyName);
       }
     }
     return beanMap;
   }
   
   public BeanInfo getBeanInfo(Object from)
     throws IntrospectionException
   {
     return getBeanInfo(from.getClass());
   }
   
   public BeanInfo getBeanInfo(Class clazz)
     throws IntrospectionException
   {
     synchronized (this.beanInfoCache)
     {
       BeanInfo beanInfo = (BeanInfo)this.beanInfoCache.get(clazz);
       if (beanInfo == null)
       {
         beanInfo = Introspector.getBeanInfo(clazz, Object.class);
         this.beanInfoCache.put(clazz, beanInfo);
       }
       return beanInfo;
     }
   }
   
   void internalSetProperty(String name, Object value, Object o, Map<String, Object> context, boolean throwPropertyExceptions)
     throws ReflectionException
   {
     try
     {
       setValue(name, context, o, value);
     }
     catch (OgnlException e)
     {
       Throwable reason = e.getReason();
       String msg = "Caught OgnlException while setting property '" + name + "' on type '" + o.getClass().getName() + "'.";
       Throwable exception = reason == null ? e : reason;
       if (throwPropertyExceptions) {
         throw new ReflectionException(msg, exception);
       }
       if (devMode) {
         LOG.warn(msg, exception, new String[0]);
       }
     }
   }
 }