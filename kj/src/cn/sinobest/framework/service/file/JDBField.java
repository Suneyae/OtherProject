 package cn.sinobest.framework.service.file;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import java.text.DecimalFormat;
 import java.text.ParseException;
 import java.text.SimpleDateFormat;
 import java.util.Date;
 
 public class JDBField
 {
   private String name;
   private char type;
   private int length;
   private int decimalCount;
   
   public JDBField(String fieldName, char fType, int fLen, int decLen)
     throws AppException
   {
     this.name = fieldName;
     this.type = fType;
     this.length = fLen;
     this.decimalCount = decLen;
   }
   
   public String getName()
   {
     return this.name;
   }
   
   public char getType()
   {
     return this.type;
   }
   
   public int getLength()
   {
     return this.length;
   }
   
   public int getDecimalCount()
   {
     return this.decimalCount;
   }
   
   public String format(Object obj)
     throws AppException
   {
     if ((this.type == 'N') || (this.type == 'F'))
     {
       if (obj == null) {
         obj = new Double(0.0D);
       }
       if ((obj instanceof Number))
       {
         Number number = (Number)obj;
         StringBuffer stringbuffer = new StringBuffer(getLength());
         for (int i = 0; i < getLength(); i++) {
           stringbuffer.append("#");
         }
         if (getDecimalCount() > 0) {
           stringbuffer.setCharAt(getLength() - getDecimalCount() - 1, 
             '.');
         }
         DecimalFormat decimalformat = new DecimalFormat(
           stringbuffer.toString());
         String s1 = decimalformat.format(number);
         int k = getLength() - s1.length();
         if (k < 0) {
           throw new AppException("Value " + number + 
             " cannot fit in pattern: '" + stringbuffer + "'.");
         }
         StringBuffer stringbuffer2 = new StringBuffer(k);
         for (int l = 0; l < k; l++) {
           stringbuffer2.append(" ");
         }
         return stringbuffer2 + s1;
       }
       throw new AppException("Expected a Number, got " + 
         obj.getClass() + ".");
     }
     if (this.type == 'C')
     {
       if (obj == null) {
         obj = "";
       }
       if ((obj instanceof String))
       {
         String s = (String)obj;
         if (s.length() > getLength()) {
           throw new AppException("'" + obj + "' is longer than " + 
             getLength() + " characters.");
         }
         StringBuffer stringbuffer1 = new StringBuffer(getLength() - 
           s.length());
         for (int j = 0; j < getLength() - s.length(); j++) {
           stringbuffer1.append(' ');
         }
         return s + stringbuffer1;
       }
       throw new AppException("Expected a String, got " + 
         obj.getClass() + ".");
     }
     if (this.type == 'L')
     {
       if (obj == null) {
         obj = new Boolean(false);
       }
       if ((obj instanceof Boolean))
       {
         Boolean boolean1 = (Boolean)obj;
         return boolean1.booleanValue() ? "Y" : "N";
       }
       throw new AppException("Expected a Boolean, got " + 
         obj.getClass() + ".");
     }
     if (this.type == 'D')
     {
       if (obj == null) {
         obj = new Date();
       }
       if ((obj instanceof Date))
       {
         Date date = (Date)obj;
         SimpleDateFormat simpledateformat = new SimpleDateFormat(
           "yyyy-MM-dd HH:mm:ss");
         return simpledateformat.format(date);
       }
       throw new AppException("Expected a Date, got " + obj.getClass() + 
         ".");
     }
     throw new AppException("Unrecognized JDBFField type: " + this.type);
   }
   
   public Object parse(String s)
     throws AppException
   {
     s = s.trim();
     if ((this.type == 'N') || (this.type == 'F'))
     {
       if (s.equals("")) {
         s = "0";
       }
       try
       {
         if (getDecimalCount() == 0) {
           try
           {
             return new Long(s);
           }
           catch (NumberFormatException e)
           {
             return new Double(s);
           }
         }
         return new Double(s);
       }
       catch (NumberFormatException numberformatexception)
       {
         throw new AppException("字段值无法格式化为数值类型", numberformatexception);
       }
     }
     if (this.type == 'C') {
       return s;
     }
     if (this.type == 'L')
     {
       if ((s.equals("Y")) || (s.equals("y")) || (s.equals("T")) || 
         (s.equals("t"))) {
         return new Boolean(true);
       }
       if ((s.equals("N")) || (s.equals("n")) || (s.equals("F")) || 
         (s.equals("f"))) {
         return new Boolean(false);
       }
       throw new AppException("Unrecognized value for logical field: " + 
         s);
     }
     if (this.type == 'D')
     {
       SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMdd");
       try
       {
         if ("".equals(s)) {
           return null;
         }
         return simpledateformat.parse(s);
       }
       catch (ParseException parseexception)
       {
         throw new AppException("数据类型解析失败", parseexception);
       }
     }
     throw new AppException("Unrecognized JDBFField type: " + this.type);
   }
   
   public String toString()
   {
     return this.name;
   }
 }