 package cn.sinobest.framework.util;
 
 import java.util.HashMap;
 import java.util.LinkedHashMap;
 
 public class HTMLEncode
 {
   private static final String[] ENTITIES = {
     ">", 
     "&gt;", 
     "<", 
     "&lt;", 
     "&", 
     "&amp;", 
     "\"", 
     "&quot;", 
     "'", 
     "&#039;", 
     "\\", 
     "&#092;", 
     "©", 
     "&copy;", 
     "®", 
     "&reg;" };
   private static HashMap<String, String> entityTableEncode = null;
   
   protected static synchronized void buildEntityTables()
   {
     entityTableEncode = new LinkedHashMap();
     for (int i = 0; i < ENTITIES.length; i += 2) {
       if (!entityTableEncode.containsKey(ENTITIES[i])) {
         entityTableEncode.put(ENTITIES[i], ENTITIES[(i + 1)]);
       }
     }
   }
   
   public static final String encode(String s)
   {
     return encode(s, "\n");
   }
   
   public static final String encode(String s, String cr)
   {
     if (entityTableEncode == null) {
       buildEntityTables();
     }
     if (s == null) {
       return "";
     }
     StringBuffer sb = new StringBuffer(s.length()
     for (int i = 0; i < s.length(); i++)
     {
       char ch = s.charAt(i);
       if (((ch >= '?') && (ch <= 'Z')) || ((ch >= 'a') && (ch <= 'z')) || (ch == ' '))
       {
         sb.append(ch);
       }
       else if (ch == '\n')
       {
         sb.append(cr);
       }
       else
       {
         String chEnc = encodeSingleChar(String.valueOf(ch));
         if (chEnc != null) {
           sb.append(chEnc);
         } else {
           sb.append(ch);
         }
       }
     }
     return sb.toString();
   }
   
   private static String encodeSingleChar(String ch)
   {
     return (String)entityTableEncode.get(ch);
   }
 }