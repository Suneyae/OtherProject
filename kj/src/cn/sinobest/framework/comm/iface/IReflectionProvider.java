package cn.sinobest.framework.comm.iface;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

public abstract interface IReflectionProvider
{
  public abstract Method getGetMethod(Class paramClass, String paramString)
    throws IntrospectionException, Exception;
  
  public abstract Method getSetMethod(Class paramClass, String paramString)
    throws IntrospectionException, Exception;
  
  public abstract Field getField(Class paramClass, String paramString);
  
  public abstract void setProperties(Map<String, String> paramMap, Object paramObject, Map<String, Object> paramMap1);
  
  public abstract void setProperties(Map<String, String> paramMap, Object paramObject, Map<String, Object> paramMap1, boolean paramBoolean)
    throws Exception;
  
  public abstract void setProperties(Map<String, String> paramMap, Object paramObject);
  
  public abstract void setPropertiesWithObject(Map<String, ?> paramMap, Object paramObject);
  
  public abstract PropertyDescriptor getPropertyDescriptor(Class paramClass, String paramString)
    throws IntrospectionException, Exception;
  
  public abstract void copy(Object paramObject1, Object paramObject2, Map<String, Object> paramMap, Collection<String> paramCollection1, Collection<String> paramCollection2);
  
  public abstract Object getRealTarget(String paramString, Map<String, Object> paramMap, Object paramObject)
    throws Exception;
  
  public abstract void setProperty(String paramString, Object paramObject1, Object paramObject2, Map<String, Object> paramMap, boolean paramBoolean);
  
  public abstract void setProperty(String paramString, Object paramObject1, Object paramObject2, Map<String, Object> paramMap);
  
  public abstract Map<String, Object> getBeanMap(Object paramObject)
    throws IntrospectionException, Exception;
  
  public abstract Object getValue(String paramString, Map<String, Object> paramMap, Object paramObject)
    throws Exception;
  
  public abstract void setValue(String paramString, Map<String, Object> paramMap, Object paramObject1, Object paramObject2)
    throws Exception;
  
  public abstract PropertyDescriptor[] getPropertyDescriptors(Object paramObject)
    throws IntrospectionException;
}