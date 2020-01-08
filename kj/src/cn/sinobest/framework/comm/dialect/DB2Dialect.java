 package cn.sinobest.framework.comm.dialect;
 
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 
 public class DB2Dialect
   extends Dialect
 {
   public boolean supportsLimit()
   {
     return true;
   }
   
   public boolean supportsLimitOffset()
   {
     return true;
   }
   
   private static String getRowNumber(String sql)
   {
     StringBuffer rownumber = new StringBuffer(50)
       .append("rownumber() over(");
     
     int orderByIndex = sql.toLowerCase().indexOf("order by");
     if ((orderByIndex > 0) && (!hasDistinct(sql))) {
       rownumber.append(sql.substring(orderByIndex));
     }
     rownumber.append(") as rownumber_,");
     
     return rownumber.toString();
   }
   
   private static boolean hasDistinct(String sql)
   {
     return sql.toLowerCase().indexOf("select distinct") >= 0;
   }
   
   public String getLimitString(String sql, int offset, String offsetPlaceholder, int limit, String limitPlaceholder)
   {
     sql = sql.trim();
     boolean isForUpdate = false;
     if (sql.toLowerCase().endsWith(" for update"))
     {
       sql = sql.substring(0, sql.length() - 11);
       isForUpdate = true;
     }
     StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
     if (offset > 0) {
       pagingSelect.append("select
     } else {
       pagingSelect.append("select
     }
     pagingSelect.append(sql);
     if (offset > 0)
     {
       String endString = offsetPlaceholder + "+" + limitPlaceholder;
       pagingSelect.append(" ) row_ where rownumber() over() < " + endString + ") where rownum_ >= " + offsetPlaceholder);
     }
     else
     {
       pagingSelect.append(" ) where rownum <= " + limitPlaceholder);
     }
     if (isForUpdate) {
       pagingSelect.append(" for update");
     }
     return pagingSelect.toString();
   }
   
   private String getAlias(String sql)
   {
     Pattern pt = Pattern.compile("^select.*\\)\\s+(\\w+)\\s+(where).*$", 2);
     Matcher mt = pt.matcher(sql);
     if ((mt.matches()) && (mt.groupCount() > 0)) {
       return mt.group(1);
     }
     return "";
   }
 }