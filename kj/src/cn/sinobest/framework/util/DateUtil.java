 package cn.sinobest.framework.util;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IDAO;
 import java.text.DateFormat;
 import java.text.ParseException;
 import java.text.SimpleDateFormat;
 import java.util.Calendar;
 import java.util.Date;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 public class DateUtil
 {
   private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);
   
   public static enum CurDate
   {
     YYYY("yyyy"),  YYYYMM("yyyyMM"),  YYYYMMDD("yyyyMMdd"),  YYYYMMDDHH("yyyyMMddHH"),  YYYYMMDDHHmm("yyyyMMddHHmm"),  YYYYMMDDHHmmss("yyyyMMddHHmmss"),  YYYYMMDDHHmmssSSS("yyyyMMddHHmmssSSS"),  YYYYMMDD_LINE("yyyy-MM-dd"),  YYYYMMDDHH_LINE("yyyy-MM-dd HH"),  YYYYMMDDHHmm_LINE("yyyy-MM-dd HH:mm"),  YYYYMMDDHHmmss_LINE("yyyy-MM-dd HH:mm:ss"),  YYYYMMDDHHmmssSSS_LINE("yyyy-MM-dd HH:mm:ss.SSS"),  YYYYMMDD_DO("yyyy.MM.dd"),  YYYYMMDDHH_DO("yyyy.MM.dd HH"),  YYYYMMDDHHmm_DO("yyyy.MM.dd HH:mm"),  YYYYMMDDHHmmss_DO("yyyy.MM.dd HH:mm:ss"),  YYYYMMDDHHmmssSSS_DO("yyyy.MM.dd HH:mm:ss.SSS"),  YYYYMMDD_ZH("yyyy年MM月dd日"),  YYYYMMDDHH_ZH("yyyy年MM月dd日 HH时"),  YYYYMMDDHHmm_ZH("yyyy年MM月dd日 HH时mm分"),  YYYYMMDDHHmmss_ZH("yyyy年MM月dd日 HH时mm分ss秒"),  YYYYMMDDHHmmssSSS_ZH("yyyy年MM月dd日 HH时mm分ss秒SSS毫秒"),  HH("HH"),  HHmm("HH:mm"),  HHmmss("HH:mm:ss"),  HHmmssSSS("HH:mm:ss:SSS");
     
     SimpleDateFormat fm = (SimpleDateFormat)DateFormat.getDateTimeInstance();
     private String pattern;
     
     private CurDate(String pattern)
     {
       this.pattern = pattern;
     }
     
     public String getDate()
     {
       try
       {
         this.fm.applyPattern(this.pattern);
         String strD = DateUtil.getDbDate();
         Date d = DateUtil.str2Date(strD, "yyyyMMdd HH:mm:ss");
         return this.fm.format(d);
       }
       catch (Exception e)
       {
         throw new AppException("获取指定格式日期出错,详细:" + e.getMessage(), e);
       }
     }
     
     public String getPattern()
     {
       return this.pattern;
     }
   }
   
   public static enum FormatDate
   {
     YYYY("yyyy"),  YYYYMM("yyyyMM"),  YYYYMMDD("yyyyMMdd"),  YYYYMMDDHH("yyyyMMddHH"),  YYYYMMDDHHmm("yyyyMMddHHmm"),  YYYYMMDDHHmmss("yyyyMMddHHmmss"),  YYYYMMDDHHmmssSSS("yyyyMMddHHmmssSSS"),  YYYYMMDD_LINE("yyyy-MM-dd"),  YYYYMMDDHH_LINE("yyyy-MM-dd HH"),  YYYYMMDDHHmm_LINE("yyyy-MM-dd HH:mm"),  YYYYMMDDHHmmss_LINE("yyyy-MM-dd HH:mm:ss"),  YYYYMMDDHHmmssSSS_LINE("yyyy-MM-dd HH:mm:ss.SSS"),  YYYYMMDD_DO("yyyy.MM.dd"),  YYYYMMDDHH_DO("yyyy.MM.dd HH"),  YYYYMMDDHHmm_DO("yyyy.MM.dd HH:mm"),  YYYYMMDDHHmmss_DO("yyyy.MM.dd HH:mm:ss"),  YYYYMMDDHHmmssSSS_DO("yyyy.MM.dd HH:mm:ss.SSS"),  YYYYMMDD_ZH("yyyy年MM月dd日"),  YYYYMMDDHH_ZH("yyyy年MM月dd日 HH时"),  YYYYMMDDHHmm_ZH("yyyy年MM月dd日 HH时mm分"),  YYYYMMDDHHmmss_ZH("yyyy年MM月dd日 HH时mm分ss秒"),  YYYYMMDDHHmmssSSS_ZH("yyyy年MM月dd日 HH时mm分ss秒SSS毫秒"),  HH("HH"),  HHmm("HH:mm"),  HHmmss("HH:mm:ss"),  HHmmssSSS("HH:mm:ss:SSS");
     
     SimpleDateFormat fm = (SimpleDateFormat)DateFormat.getDateTimeInstance();
     private String pattern;
     
     private FormatDate(String pattern)
     {
       this.pattern = pattern;
     }
     
     public String getDate(Date d)
     {
       this.fm.applyPattern(this.pattern);
       return this.fm.format(d);
     }
     
     public String getPattern()
     {
       return this.pattern;
     }
     
     public String getDateBetween2String(Date date, int intBetween)
     {
       Date dateOld = DateUtil.getDateBetween(date, intBetween);
       return getDate(dateOld);
     }
   }
   
   public static Date str2Date(String date, String format)
   {
     if (Util.isEmpty(date)) {
       return null;
     }
     if (Util.isEmpty(format)) {
       format = "yyyy-MM-dd";
     }
     Date d = null;
     try
     {
       SimpleDateFormat sf = new SimpleDateFormat(format);
       d = sf.parse(date);
     }
     catch (ParseException e)
     {
       LOGGER.error("转换日期字符串必须与转换格式相匹配!", e);
     }
     return d;
   }
   
   public static String date2Str(Date date, String format)
   {
     if (date == null) {
       return null;
     }
     if (Util.isEmpty(format)) {
       format = "yyyy-MM-dd";
     }
     String d = null;
     SimpleDateFormat sf = new SimpleDateFormat(format);
     d = sf.format(date);
     
     return d;
   }
   
   public static int calBetweenTwoMonth(String dealMonth, String alterMonth)
   {
     int length = 0;
     if ((dealMonth.length() != 6) || (alterMonth.length() != 6))
     {
       LOGGER.debug("比较年月字符串的长度不正确");
       length = -1;
     }
     else
     {
       int dealInt = Integer.parseInt(dealMonth);
       int alterInt = Integer.parseInt(alterMonth);
       if (dealInt < alterInt)
       {
         LOGGER.debug("第一个年月变量应大于或等于第二个年月变量");
         length = -2;
       }
       else
       {
         int dealYearInt = Integer.parseInt(dealMonth.substring(0, 4));
         int dealMonthInt = Integer.parseInt(dealMonth.substring(4, 6));
         int alterYearInt = Integer.parseInt(alterMonth.substring(0, 4));
         int alterMonthInt = 
           Integer.parseInt(alterMonth.substring(4, 6));
         length = (dealYearInt - alterYearInt)
           dealMonthInt - alterMonthInt);
       }
     }
     return length;
   }
   
   public static int daysBetweenDates(Date beginDate, Date endDate)
   {
     int days = 0;
     Calendar calo = Calendar.getInstance();
     Calendar caln = Calendar.getInstance();
     calo.setTime(beginDate);
     caln.setTime(endDate);
     int oday = calo.get(6);
     int nyear = caln.get(1);
     for (int oyear = calo.get(1); nyear > oyear;)
     {
       calo.set(2, 11);
       calo.set(5, 31);
       days += calo.get(6);
       oyear++;
       calo.set(1, oyear);
     }
     int nday = caln.get(6);
     days = days + nday - oday;
     return days;
   }
   
   public static Date getDateBetween(Date date, int intBetween)
   {
     Calendar calo = Calendar.getInstance();
     calo.setTime(date);
     calo.add(5, intBetween);
     return calo.getTime();
   }
   
   public static boolean yearMonthGreatEq(String s1, String s2)
   {
     if (Long.parseLong(s1) > Long.parseLong(s2)) {
       return true;
     }
     return false;
   }
   
   public static String addYearMonth(String strDate, int intDiff)
   {
     try
     {
       if (Util.isEmpty(strDate)) {
         return strDate;
       }
       if (intDiff == 0) {
         return strDate;
       }
       if ((strDate.length() != 6) && (strDate.length() != 8)) {
         return "";
       }
       int year = new Integer(strDate.substring(0, 4)).intValue();
       int month = new Integer(strDate.substring(4, 6)).intValue();
       
       String strDay = "";
       if (strDate.length() > 6) {
         strDay = strDate.substring(6, strDate.length());
       }
       if ((intDiff > 0) || (month > Math.abs(intDiff)))
       {
         month += intDiff;
         if (month > 12)
         {
           year += month / 12;
           month %= 12;
           if (month == 0)
           {
             year--;
             month = 12;
           }
         }
       }
       else
       {
         int n = Math.abs((month + intDiff) / 12) + 1;
         month = Math.abs(month + 12 + intDiff % 12) % 12;
         year -= n;
         if (month == 0) {
           month = 12;
         }
       }
       if ((month <= 12) && (month >= 10)) {
         return year + new Integer(month).toString() + strDay;
       }
       return year + "0" + new Integer(month).toString() + strDay;
     }
     catch (Exception e) {}
     return "";
   }
   
   public static int getMonthBetween(String strDateBegin, String strDateEnd)
   {
     try
     {
       int strOut;
       int intMonthBegin;
       int intMonthEnd;
       if ((strDateBegin.equals("")) || (strDateEnd.equals("")) || 
         (strDateBegin.length() != 6) || (strDateEnd.length() != 6))
       {
         strOut = 0;
       }
       else
       {
         intMonthBegin = Integer.parseInt(strDateBegin.substring(0, 
           4))
           12 + 
           Integer.parseInt(strDateBegin.substring(4, 6));
         intMonthEnd = Integer.parseInt(strDateEnd.substring(0, 4))
           12 + Integer.parseInt(strDateEnd.substring(4, 6));
       }
       return intMonthBegin - intMonthEnd;
     }
     catch (Exception e) {}
     return 0;
   }
   
   public static String getDateBetween2String(Date date, int intBetween, String strFromat)
   {
     Date dateOld = getDateBetween(date, intBetween);
     return date2Str(dateOld, strFromat);
   }
   
   public static String getStrHaveAcross(String strDate)
   {
     try
     {
       return 
         strDate.substring(0, 4) + "-" + strDate.substring(4, 6) + "-" + strDate.substring(6, 8);
     }
     catch (Exception e) {}
     return strDate;
   }
   
   public static String getFirstDayOfNextMonth()
   {
     String strToday = CurDate.YYYYMM.getDate();
     return addYearMonth(strToday, 1) + "01";
   }
   
   public static Date getFirstDayOfMonth()
   {
     Calendar cDate = Calendar.getInstance();
     cDate.set(5, 1);
     return cDate.getTime();
   }
   
   public static Date getMaxDayOfMonth(Date d)
   {
     Calendar cDate = Calendar.getInstance();
     cDate.setTime(d);
     cDate.set(5, cDate.getActualMaximum(5));
     return cDate.getTime();
   }
   
   public static Date getMaxDayOfMonth(String d)
   {
     Calendar cDate = Calendar.getInstance();
     cDate.setTime(str2Date(d, "yyyy-MM-dd"));
     cDate.set(5, cDate.getActualMaximum(5));
     return cDate.getTime();
   }
   
   public static String getDbDate()
     throws Exception
   {
     return (String)((IDAO)Util.getBean("commDAO")).selectOneType(
       "FW_CONFIG.sysdate", null);
   }
 }