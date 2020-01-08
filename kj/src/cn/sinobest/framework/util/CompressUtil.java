 package cn.sinobest.framework.util;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import java.io.ByteArrayInputStream;
 import java.io.ByteArrayOutputStream;
 import java.io.IOException;
 import java.io.PrintStream;
 import java.io.UnsupportedEncodingException;
 import java.util.zip.GZIPInputStream;
 import java.util.zip.GZIPOutputStream;
 import org.apache.log4j.Logger;
 
 public class CompressUtil
 {
   private static final Logger LOGGER = Logger.getLogger(CompressUtil.class);
   private static final String SPLIT_DE = "\\|";
   private static final String SPLIT_EN = "|";
   private static String compressCharset = ConfUtil.getSysParamOnly("his.compress_charset", "UTF-8");
   
   public static String compress(byte[] obj)
     throws Exception
   {
     return compressHex(obj);
   }
   
   public static String decompress(String compressStr)
     throws Exception
   {
     return decompressHex(compressStr);
   }
   
   public static String compressDeci(byte[] obj)
     throws Exception
   {
     GZIPOutputStream gzipOs = null;
     ByteArrayOutputStream baos = null;
     try
     {
       long s = System.currentTimeMillis();
       long dataSize = new String(obj).getBytes(compressCharset).length;
       baos = new ByteArrayOutputStream();
       gzipOs = new GZIPOutputStream(baos);
       gzipOs.write(obj);
       gzipOs.flush();
       gzipOs.close();
       byte[] zbyte = baos.toByteArray();
       StringBuffer zSb = new StringBuffer();
       for (int i = 0; i < zbyte.length; i++) {
         zSb.append(zbyte[i]).append("|");
       }
       String zStr = zSb.toString();
       zSb = null;
       int eDataSize = zStr.getBytes(compressCharset).length;
       LOGGER.debug("压缩前后大小(byte):" + dataSize + "," + eDataSize + "\n压缩耗时(ms):" + (System.currentTimeMillis() - s) + "\n压缩比:" + Math.round((float)(eDataSize
       
       return zStr;
     }
     catch (IOException e)
     {
       e.printStackTrace();
       throw new Exception("压缩失败," + e.getLocalizedMessage(), e);
     }
     finally
     {
       if (baos != null) {
         try
         {
           baos.close();
           baos = null;
         }
         catch (IOException localIOException3) {}
       }
       if (gzipOs != null) {
         try
         {
           gzipOs.close();
           gzipOs = null;
         }
         catch (IOException localIOException4) {}
       }
     }
   }
   
   public static String decompressDeci(String compressStr)
     throws Exception
   {
     ByteArrayInputStream bais = null;
     GZIPInputStream gzipIs = null;
     try
     {
       long s = System.currentTimeMillis();
       long dataSize = compressStr.getBytes(compressCharset).length;
       String[] dSb = compressStr.split("\\|");
       byte[] dbyte = new byte[dSb.length];
       for (int i = 0; i < dbyte.length; i++) {
         if (dSb[i] != null) {
           dbyte[i] = Byte.parseByte(dSb[i]);
         }
       }
       bais = new ByteArrayInputStream(dbyte);
       gzipIs = new GZIPInputStream(bais);
       
       byte[] b = new byte[1024];
       int count = 0;
       StringBuffer sf = new StringBuffer();
       while ((count = gzipIs.read(b)) != -1) {
         sf.append(new String(b, 0, count));
       }
       String dStr = sf.toString();
       sf = null;
       
       int eDataSize = dStr.getBytes(compressCharset).length;
       LOGGER.debug("解压前后大小(byte):" + dataSize + "," + eDataSize + "\n解压耗时(ms):" + (System.currentTimeMillis() - s));
       
       return dStr;
     }
     catch (UnsupportedEncodingException e)
     {
       throw new AppException("解压失败，" + e.getLocalizedMessage(), e);
     }
     catch (IOException e)
     {
       throw new AppException("解压失败，" + e.getLocalizedMessage(), e);
     }
     finally
     {
       if (bais != null) {
         try
         {
           bais.close();
         }
         catch (IOException localIOException3) {}
       }
       if (gzipIs != null) {
         try
         {
           gzipIs.close();
         }
         catch (IOException localIOException4) {}
       }
     }
   }
   
   public static String compressHex(byte[] obj)
     throws Exception
   {
     GZIPOutputStream gzipOs = null;
     ByteArrayOutputStream baos = null;
     try
     {
       long s = System.currentTimeMillis();
       long dataSize = obj.length;
       baos = new ByteArrayOutputStream();
       gzipOs = new GZIPOutputStream(baos);
       gzipOs.write(obj);
       gzipOs.flush();
       gzipOs.close();
       byte[] zbyte = baos.toByteArray();
       
       String zStr = byteArr2HexStr(zbyte);
       int eDataSize = zStr.length();
       if (LOGGER.isDebugEnabled()) {
         LOGGER.debug("压缩前大小(byte):\t" + dataSize + "\n压缩后大小(byte):\t" + eDataSize + "\n压缩耗时(ms):\t" + (System.currentTimeMillis() - s) + "\n压缩比:\t" + 
           Math.round((float)(eDataSize
       }
       return zStr;
     }
     catch (IOException e)
     {
       throw new AppException("压缩失败," + e.getLocalizedMessage(), e);
     }
     finally
     {
       if (baos != null) {
         try
         {
           baos.close();
           baos = null;
         }
         catch (IOException localIOException3) {}
       }
       if (gzipOs != null) {
         try
         {
           gzipOs.close();
           gzipOs = null;
         }
         catch (IOException localIOException4) {}
       }
     }
   }
   
   public static String decompressHex(String compressStr)
     throws Exception
   {
     ByteArrayInputStream bais = null;
     GZIPInputStream gzipIs = null;
     try
     {
       long s = System.currentTimeMillis();
       long dataSize = compressStr.getBytes(compressCharset).length;
       byte[] dbyte = hexStr2ByteArr(compressStr);
       bais = new ByteArrayInputStream(dbyte);
       gzipIs = new GZIPInputStream(bais);
       
       byte[] b = new byte[1024];
       int count = 0;
       StringBuffer sf = new StringBuffer();
       while ((count = gzipIs.read(b)) != -1) {
         sf.append(new String(b, 0, count));
       }
       String dStr = sf.toString();
       sf = null;
       
       int eDataSize = dStr.getBytes(compressCharset).length;
       if (LOGGER.isDebugEnabled()) {
         LOGGER.debug(dStr + "\n解压前大小(byte):\t" + dataSize + "\n解压后大小(byte):\t" + eDataSize + "\n解压耗时(ms):\t" + (System.currentTimeMillis() - s));
       }
       return dStr;
     }
     catch (UnsupportedEncodingException e)
     {
       throw new AppException("解压失败，" + e.getLocalizedMessage(), e);
     }
     catch (IOException e)
     {
       throw new AppException("解压失败，" + e.getLocalizedMessage(), e);
     }
     finally
     {
       if (bais != null) {
         try
         {
           bais.close();
         }
         catch (IOException localIOException3) {}
       }
       if (gzipIs != null) {
         try
         {
           gzipIs.close();
         }
         catch (IOException localIOException4) {}
       }
     }
   }
   
   public static String byteArr2HexStr(byte[] b)
   {
     int bLen = b.length;
     int cLen = bLen << 1;
     char[] c = new char[cLen];
     int i = 0;
     for (int j = 0; i < bLen; i++)
     {
       c[(j++)] = Character.forDigit((b[i] & 0xF0) >>> 4, 16);
       c[(j++)] = Character.forDigit(b[i] & 0xF, 16);
     }
     return String.valueOf(c);
   }
   
   public static byte[] hexStr2ByteArr(String h)
   {
     char[] c = h.toCharArray();
     int cLen = c.length;
     int blen = cLen >> 1;
     byte[] b = new byte[blen];
     int i = 0;
     for (int j = 0; i < blen; i++)
     {
       int _b = Character.digit(c[(j++)], 16) << 4;
       _b |= Character.digit(c[(j++)], 16);
       b[i] = ((byte)(_b & 0xFF));
     }
     return b;
   }
   
   public static void main(String[] args)
     throws Exception
   {
     if ("h".equals(args[0]))
     {
       if ("c".equals(args[1])) {
         System.out.println(compress(args[2].getBytes()));
       } else {
         System.out.println(decompress(args[2]));
       }
     }
     else if ("d".equals(args[0])) {
       if ("c".equals(args[1])) {
         System.out.println(compressDeci(args[2].getBytes()));
       } else {
         System.out.println(decompressDeci(args[2]));
       }
     }
   }
 }