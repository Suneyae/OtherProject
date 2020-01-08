 package cn.sinobest.framework.util;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.FileOutputStream;
 import javax.crypto.Cipher;
 import javax.crypto.SecretKey;
 import javax.crypto.SecretKeyFactory;
 import javax.crypto.spec.DESKeySpec;
 
 public class SecurityUtil
 {
   private static String encoding = "UTF-8";
   
   public String getEncoding()
   {
     return encoding;
   }
   
   public void setEncoding(String Encoding)
   {
     encoding = Encoding;
   }
   
   public static String bytes2HexStr(byte[] b)
   {
     StringBuffer sb = new StringBuffer();
     String str = "";
     for (int i = 0; i < b.length; i++) {
       sb.append(Integer.toHexString(0xFF & b[i] | 0xFFFFFF00).substring(6));
     }
     str = sb.toString();
     return str;
   }
   
   public static byte[] hexStr2Bytes(String str)
     throws AppException
   {
     int count = str.length() / 2;
     byte[] bytes = new byte[count];
     String tmpStr = null;
     int ch = -1;
     for (int i = 0; i < count; i++)
     {
       tmpStr = str.substring(i
       ch = twoHex2Dec(tmpStr);
       bytes[i] = ((byte)ch);
     }
     return bytes;
   }
   
   private static int twoHex2Dec(String hex)
     throws AppException
   {
     char high = hex.charAt(0);
     char low = hex.charAt(1);
     int i = hex2Dec(high)
     if (i > 127)
     {
       i = (i ^ 0xFFFFFFFF) & 0xFF;
       i++;
       i = Integer.parseInt("-" + i);
     }
     return i;
   }
   
   private static int hex2Dec(char hex)
     throws AppException
   {
     if ((hex >= '0') && (hex < ':')) {
       return hex - '0';
     }
     if ((hex == 'a') || (hex == 'A')) {
       return 10;
     }
     if ((hex == 'b') || (hex == 'B')) {
       return 11;
     }
     if ((hex == 'c') || (hex == 'C')) {
       return 12;
     }
     if ((hex == 'd') || (hex == 'D')) {
       return 13;
     }
     if ((hex == 'e') || (hex == 'E')) {
       return 14;
     }
     if ((hex == 'f') || (hex == 'F')) {
       return 15;
     }
     throw new AppException("EFW0301", null, new Object[] { Character.valueOf(hex) });
   }
   
   public static String EncryptStr(String keyStr, String dataStr)
     throws Exception
   {
     byte[] encryptedPwd1 = encrypt("rational", keyStr, "DES");
     byte[] encryptedPwd = new byte[8];
     System.arraycopy(encryptedPwd1, 0, encryptedPwd, 0, 8);
     byte[] data = dataStr.getBytes(encoding);
     byte[] encryptedData = encrypt(data, encryptedPwd, "DES");
     String hexStr = bytes2HexStr(encryptedData);
     return hexStr;
   }
   
   public static byte[] encrypt(String dataStr, String keyStr, String alg)
     throws Exception
   {
     return encrypt(dataStr.getBytes(encoding), keyStr.getBytes(encoding), alg);
   }
   
   public static byte[] encrypt(byte[] data, byte[] rawKeyData, String alg)
     throws Exception
   {
     DESKeySpec dks = new DESKeySpec(rawKeyData);
     SecretKeyFactory keyFactory = null;
     if (alg.equalsIgnoreCase("DES")) {
       keyFactory = SecretKeyFactory.getInstance("DES");
     } else {
       throw new AppException("EFW0302", null, new Object[] { alg });
     }
     SecretKey key = keyFactory.generateSecret(dks);
     
     Cipher cipher = null;
     if (alg.equalsIgnoreCase("DES")) {
       cipher = Cipher.getInstance("DES");
     } else {
       throw new AppException("EFW0302", null, new Object[] { alg });
     }
     cipher.init(1, key);
     byte[] encryptedData = cipher.doFinal(data);
     return encryptedData;
   }
   
   public static String DecryptStr(String keyStr, String dataStr)
     throws Exception
   {
     byte[] encryptedPwd1 = encrypt("rational", keyStr, "DES");
     byte[] encryptedPwd = new byte[8];
     System.arraycopy(encryptedPwd1, 0, encryptedPwd, 0, 8);
     byte[] encdata = hexStr2Bytes(dataStr);
     byte[] encryptedData = decrypt(encdata, encryptedPwd, "DES");
     return new String(encryptedData);
   }
   
   public static byte[] decrypt(byte[] encryptedData, byte[] rawKeyData, String alg)
     throws Exception
   {
     DESKeySpec dks = new DESKeySpec(rawKeyData);
     SecretKeyFactory keyFactory = null;
     if (alg.equalsIgnoreCase("DES")) {
       keyFactory = SecretKeyFactory.getInstance("DES");
     } else {
       throw new AppException("EFW0302", null, new Object[] { alg });
     }
     SecretKey key = keyFactory.generateSecret(dks);
     
     Cipher cipher = null;
     if (alg.equalsIgnoreCase("DES")) {
       cipher = Cipher.getInstance("DES");
     } else {
       throw new AppException("EFW0302", null, new Object[] { alg });
     }
     cipher.init(2, key);
     
 
     byte[] decryptedData = cipher.doFinal(encryptedData);
     
     return decryptedData;
   }
   
   public static void EncryptFile(String fileInPath, String sKey)
     throws Exception
   {
     try
     {
       File fileIn = new File(fileInPath);
       if (sKey.length() == 48)
       {
         byte[] bytK1 = getKeyByStr(sKey.substring(0, 16));
         byte[] bytK2 = getKeyByStr(sKey.substring(16, 32));
         byte[] bytK3 = getKeyByStr(sKey.substring(32, 48));
         
         FileInputStream fis = new FileInputStream(fileIn);
         byte[] bytIn = new byte[(int)fileIn.length()];
         for (int i = 0; i < fileIn.length(); i++) {
           bytIn[i] = ((byte)fis.read());
         }
         byte[] bytOut = encryptByDES(encryptByDES(encryptByDES(bytIn, bytK1), bytK2), bytK3);
         String fileOut = fileIn.getPath() + ".tdes";
         FileOutputStream fos = new FileOutputStream(fileOut);
         for (int i = 0; i < bytOut.length; i++) {
           fos.write(bytOut[i]);
         }
         fos.close();
       }
       else
       {
         throw new AppException("EFW0303");
       }
     }
     catch (Exception e)
     {
       e.printStackTrace();
     }
   }
   
   public static void DecryptFile(String fileInPath, String fileOutPath, String sKey)
   {
     try
     {
       if (sKey.length() == 48)
       {
         String strPath = fileInPath;
         if (strPath.substring(strPath.length() - 5).toLowerCase().equals(".tdes")) {
           strPath = strPath.substring(0, strPath.length() - 5);
         } else {
           throw new AppException("EFW0304");
         }
         byte[] bytK1 = getKeyByStr(sKey.substring(0, 16));
         byte[] bytK2 = getKeyByStr(sKey.substring(16, 32));
         byte[] bytK3 = getKeyByStr(sKey.substring(32, 48));
         
         File fileIn = new File(fileInPath);
         
         FileInputStream fis = new FileInputStream(fileIn);
         byte[] bytIn = new byte[(int)fileIn.length()];
         for (int i = 0; i < fileIn.length(); i++) {
           bytIn[i] = ((byte)fis.read());
         }
         byte[] bytOut = decryptByDES(decryptByDES(decryptByDES(bytIn, bytK3), bytK2), bytK1);
         
         FileOutputStream fos = new FileOutputStream(fileOutPath);
         for (int i = 0; i < bytOut.length; i++) {
           fos.write(bytOut[i]);
         }
         fos.close();
       }
       else
       {
         throw new AppException("EFW0304");
       }
     }
     catch (Exception e)
     {
       throw new AppException("EFW0305");
     }
   }
   
   private static byte[] encryptByDES(byte[] bytP, byte[] bytKey)
     throws Exception
   {
     DESKeySpec desKS = new DESKeySpec(bytKey);
     SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
     SecretKey sk = skf.generateSecret(desKS);
     Cipher cip = Cipher.getInstance("DES");
     cip.init(1, sk);
     return cip.doFinal(bytP);
   }
   
   private static byte[] decryptByDES(byte[] bytE, byte[] bytKey)
     throws Exception
   {
     DESKeySpec desKS = new DESKeySpec(bytKey);
     SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
     SecretKey sk = skf.generateSecret(desKS);
     Cipher cip = Cipher.getInstance("DES");
     cip.init(2, sk);
     return cip.doFinal(bytE);
   }
   
   private static byte[] getKeyByStr(String str)
   {
     byte[] bRet = new byte[str.length() / 2];
     for (int i = 0; i < str.length() / 2; i++)
     {
       Integer itg = new Integer(16
       bRet[i] = itg.byteValue();
     }
     return bRet;
   }
   
   private static int getChrInt(char chr)
   {
     int iRet = 0;
     if (chr == "0".charAt(0)) {
       iRet = 0;
     }
     if (chr == "1".charAt(0)) {
       iRet = 1;
     }
     if (chr == "2".charAt(0)) {
       iRet = 2;
     }
     if (chr == "3".charAt(0)) {
       iRet = 3;
     }
     if (chr == "4".charAt(0)) {
       iRet = 4;
     }
     if (chr == "5".charAt(0)) {
       iRet = 5;
     }
     if (chr == "6".charAt(0)) {
       iRet = 6;
     }
     if (chr == "7".charAt(0)) {
       iRet = 7;
     }
     if (chr == "8".charAt(0)) {
       iRet = 8;
     }
     if (chr == "9".charAt(0)) {
       iRet = 9;
     }
     if (chr == "A".charAt(0)) {
       iRet = 10;
     }
     if (chr == "B".charAt(0)) {
       iRet = 11;
     }
     if (chr == "C".charAt(0)) {
       iRet = 12;
     }
     if (chr == "D".charAt(0)) {
       iRet = 13;
     }
     if (chr == "E".charAt(0)) {
       iRet = 14;
     }
     if (chr == "F".charAt(0)) {
       iRet = 15;
     }
     return iRet;
   }
 }