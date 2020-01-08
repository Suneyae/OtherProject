 package cn.sinobest.framework.web.his;
 
 import cn.sinobest.framework.util.ConfUtil;
 import cn.sinobest.framework.util.Util;
 import cn.sinobest.framework.util.WfUtil;
 
 public class HisUtil
 {
   public static String getLSH()
   {
     String prefix = ConfUtil.getSysParamOnly("his.lsh_prefix", "his");
     String length = ConfUtil.getSysParamOnly("his.lsh_length", "");
     
     StringBuffer ywlsStr = new StringBuffer((String)Util.nvl(prefix));
     String ywls = WfUtil.getYwlsh();
     if (!Util.isEmpty(length)) {
       ywlsStr.append(Util.lpad(String.valueOf(ywls), Integer.valueOf(length).intValue(), "0", false));
     } else {
       ywlsStr.append(String.valueOf(ywls));
     }
     return ywlsStr.toString();
   }
 }