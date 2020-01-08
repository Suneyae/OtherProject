 package cn.sinobest.framework.service.tags;
 
 import cn.sinobest.framework.service.json.JSONUtilities;
 import freemarker.core.CollectionAndSequence;
 import freemarker.ext.beans.ArrayModel;
 import freemarker.ext.beans.BeanModel;
 import freemarker.template.SimpleHash;
 import freemarker.template.SimpleSequence;
 import freemarker.template.TemplateMethodModelEx;
 import freemarker.template.TemplateModelException;
 import java.util.List;
 
 public class ToJson
   implements TemplateMethodModelEx
 {
   public Object exec(List arguments)
     throws TemplateModelException
   {
     try
     {
       Object obj = arguments.get(0);
       if ((obj instanceof BeanModel)) {
         return new JSONUtilities(1).parseObject(obj == null ? null : ((BeanModel)obj).getWrappedObject());
       }
       if ((obj instanceof SimpleSequence)) {
         return new JSONUtilities(1).parseObject(obj == null ? null : ((SimpleSequence)obj).toList());
       }
       if ((obj instanceof CollectionAndSequence)) {
         return new JSONUtilities(1).parseObject(obj == null ? null : ((CollectionAndSequence)obj).iterator());
       }
       if ((obj instanceof SimpleHash)) {
         return new JSONUtilities(1).parseObject(obj == null ? null : ((SimpleHash)obj).toMap());
       }
       if ((obj instanceof ArrayModel)) {
         return new JSONUtilities(1).parseObject(obj == null ? null : ((ArrayModel)obj).getWrappedObject());
       }
       return new JSONUtilities(1).parseObject(obj);
     }
     catch (Exception e)
     {
       throw new TemplateModelException("对象无法转化为JSON", e);
     }
   }
 }