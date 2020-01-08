 package cn.sinobest.framework.service.json;
 
 import java.io.IOException;
 import java.util.List;
 import org.codehaus.jackson.JsonParser;
 import org.codehaus.jackson.JsonProcessingException;
 import org.codehaus.jackson.map.DeserializationContext;
 import org.codehaus.jackson.map.deser.UntypedObjectDeserializer;
 
 public class CommDeserializer
   extends UntypedObjectDeserializer
 {
   public Object deserialize(JsonParser jp, DeserializationContext ctxt)
     throws IOException, JsonProcessingException
   {
     switch (jp.getCurrentToken())
     {
     case NOT_AVAILABLE: 
       List<Object> result = (List)super.deserialize(jp, ctxt);
       if (result != null) {
         return result.toArray();
       }
       return result;
     }
     return super.deserialize(jp, ctxt);
   }
 }