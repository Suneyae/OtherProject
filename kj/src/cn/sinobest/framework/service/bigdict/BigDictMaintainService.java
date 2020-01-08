 package cn.sinobest.framework.service.bigdict;
 
 import cn.sinobest.framework.comm.cache.CacheItem;
 import cn.sinobest.framework.comm.cache.CacheManager;
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.comm.iface.IDTO;
 import cn.sinobest.framework.comm.iface.IExportService;
 import cn.sinobest.framework.service.entities.BigDictInfo;
 import cn.sinobest.framework.service.file.FileService;
 import cn.sinobest.framework.service.tags.GltService;
 import cn.sinobest.framework.service.tags.GtService;
 import cn.sinobest.framework.util.ConfUtil;
 import cn.sinobest.framework.util.Util;
 import java.io.BufferedOutputStream;
 import java.io.Closeable;
 import java.io.File;
 import java.io.FileOutputStream;
 import java.io.IOException;
 import java.io.ObjectOutputStream;
 import java.io.OutputStream;
 import java.security.SecureRandom;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.LinkedHashMap;
 import java.util.List;
 import java.util.Map;
 import java.util.Map.Entry;
 import java.util.Set;
 import org.apache.log4j.Logger;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.dao.DataAccessException;
 import org.springframework.jdbc.core.JdbcTemplate;
 import org.springframework.jdbc.core.RowCallbackHandler;
 import org.springframework.stereotype.Service;
 
 @Service
 public class BigDictMaintainService
   implements IExportService
 {
   private static final SecureRandom RANDOM = new SecureRandom();
   private static Logger LOG = Logger.getLogger(BigDictMaintainService.class);
   @Autowired
   GltService gltService;
   static final char SEP_FIELD = '\003';
   static final char SEP_RECORED = '\002';
   static final char SEP_SECTION = '\001';
   
   public File exportFile(IDTO dto)
     throws AppException
   {
     return doWork(dto);
   }
   
   
   public File doWork(IDTO dto)
     throws AppException
   {
     // Byte code:
     //   0: aload_1
     //   1: ldc 56
     //   3: invokeinterface 58 2 0
     //   8: checkcast 64	java/lang/String
     //   11: astore_2
     //   12: lconst_0
     //   13: lstore_3
     //   14: aload_2
     //   15: invokestatic 66	cn/sinobest/framework/util/Util:isEmpty	(Ljava/lang/String;)Z
     //   18: ifne +20 -> 38
     //   21: aload_2
     //   22: invokestatic 72	java/lang/Long:parseLong	(Ljava/lang/String;)J
     //   25: lstore_3
     //   26: aload_1
     //   27: ldc 56
     //   29: lload_3
     //   30: invokestatic 78	java/lang/Long:valueOf	(J)Ljava/lang/Long;
     //   33: invokeinterface 82 3 0
     //   38: ldc 86
     //   40: ldc 88
     //   42: invokestatic 90	cn/sinobest/framework/util/ConfUtil:getSysParam	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
     //   45: astore 5
     //   47: aconst_null
     //   48: astore 6
     //   50: aconst_null
     //   51: astore 7
     //   53: new 96	java/util/HashSet
     //   56: dup
     //   57: invokespecial 98	java/util/HashSet:<init>	()V
     //   60: astore 8
     //   62: new 99	java/io/File
     //   65: dup
     //   66: ldc 101
     //   68: invokestatic 103	java/lang/System:getProperty	(Ljava/lang/String;)Ljava/lang/String;
     //   71: new 109	java/lang/StringBuilder
     //   74: dup
     //   75: ldc 111
     //   77: invokespecial 113	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
     //   80: getstatic 31	cn/sinobest/framework/service/bigdict/BigDictMaintainService:RANDOM	Ljava/security/SecureRandom;
     //   83: invokevirtual 116	java/security/SecureRandom:nextLong	()J
     //   86: invokevirtual 120	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
     //   89: invokevirtual 124	java/lang/StringBuilder:toString	()Ljava/lang/String;
     //   92: invokespecial 128	java/io/File:<init>	(Ljava/lang/String;Ljava/lang/String;)V
     //   95: astore 6
     //   97: aload 6
     //   99: invokevirtual 131	java/io/File:mkdir	()Z
     //   102: pop
     //   103: aload 5
     //   105: invokestatic 66	cn/sinobest/framework/util/Util:isEmpty	(Ljava/lang/String;)Z
     //   108: ifne +277 -> 385
     //   111: new 135	java/util/HashMap
     //   114: dup
     //   115: invokespecial 137	java/util/HashMap:<init>	()V
     //   118: astore 9
     //   120: aload 5
     //   122: ldc 138
     //   124: invokevirtual 140	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
     //   127: dup
     //   128: astore 13
     //   130: arraylength
     //   131: istore 12
     //   133: iconst_0
     //   134: istore 11
     //   136: goto +201 -> 337
     //   139: aload 13
     //   141: iload 11
     //   143: aaload
     //   144: astore 10
     //   146: new 109	java/lang/StringBuilder
     //   149: dup
     //   150: ldc 144
     //   152: invokespecial 113	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
     //   155: aload 10
     //   157: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
     //   160: invokevirtual 124	java/lang/StringBuilder:toString	()Ljava/lang/String;
     //   163: ldc 88
     //   165: invokestatic 90	cn/sinobest/framework/util/ConfUtil:getSysParam	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
     //   168: astore 14
     //   170: aload 14
     //   172: invokestatic 66	cn/sinobest/framework/util/Util:isEmpty	(Ljava/lang/String;)Z
     //   175: ifeq +6 -> 181
     //   178: goto +156 -> 334
     //   181: aload 14
     //   183: ldc 149
     //   185: invokestatic 151	cn/sinobest/framework/util/Util:getBean	(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;
     //   188: checkcast 149	cn/sinobest/framework/comm/iface/IBigDictProvider
     //   191: astore 15
     //   193: aload_1
     //   194: ldc 155
     //   196: aload 10
     //   198: invokeinterface 82 3 0
     //   203: aload 15
     //   205: aload_1
     //   206: invokeinterface 157 2 0
     //   211: astore 16
     //   213: aload 16
     //   215: ifnonnull +6 -> 221
     //   218: goto +116 -> 334
     //   221: aload 16
     //   223: aload 10
     //   225: putfield 161	cn/sinobest/framework/service/entities/BigDictInfo:dictName	Ljava/lang/String;
     //   228: aload_0
     //   229: getfield 166	cn/sinobest/framework/service/bigdict/BigDictMaintainService:gltService	Lcn/sinobest/framework/service/tags/GltService;
     //   232: aload 10
     //   234: invokevirtual 168	cn/sinobest/framework/service/tags/GltService:getGlt	(Ljava/lang/String;)Lcn/sinobest/framework/service/tags/Glt;
     //   237: astore 17
     //   239: aload 16
     //   241: aload 17
     //   243: invokevirtual 174	cn/sinobest/framework/service/tags/Glt:getDict	()Ljava/util/Map;
     //   246: putfield 180	cn/sinobest/framework/service/entities/BigDictInfo:colDict	Ljava/util/Map;
     //   249: aload 16
     //   251: getfield 180	cn/sinobest/framework/service/entities/BigDictInfo:colDict	Ljava/util/Map;
     //   254: ifnull +21 -> 275
     //   257: aload 8
     //   259: aload 16
     //   261: getfield 180	cn/sinobest/framework/service/entities/BigDictInfo:colDict	Ljava/util/Map;
     //   264: invokeinterface 184 1 0
     //   269: invokeinterface 190 2 0
     //   274: pop
     //   275: aload 9
     //   277: aload 10
     //   279: aload_0
     //   280: aload 16
     //   282: invokespecial 196	cn/sinobest/framework/service/bigdict/BigDictMaintainService:getOneDict	(Lcn/sinobest/framework/service/entities/BigDictInfo;)Lcn/sinobest/framework/service/bigdict/DictEntry;
     //   285: invokeinterface 200 3 0
     //   290: pop
     //   291: goto +43 -> 334
     //   294: astore 14
     //   296: getstatic 39	cn/sinobest/framework/service/bigdict/BigDictMaintainService:LOG	Lorg/apache/log4j/Logger;
     //   299: new 109	java/lang/StringBuilder
     //   302: dup
     //   303: ldc 204
     //   305: invokespecial 113	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
     //   308: aload 10
     //   310: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
     //   313: ldc 206
     //   315: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
     //   318: aload 14
     //   320: invokevirtual 208	java/lang/Exception:getLocalizedMessage	()Ljava/lang/String;
     //   323: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
     //   326: invokevirtual 124	java/lang/StringBuilder:toString	()Ljava/lang/String;
     //   329: aload 14
     //   331: invokevirtual 213	org/apache/log4j/Logger:error	(Ljava/lang/Object;Ljava/lang/Throwable;)V
     //   334: iinc 11 1
     //   337: iload 11
     //   339: iload 12
     //   341: if_icmplt -202 -> 139
     //   344: aload_0
     //   345: aload 6
     //   347: aload 9
     //   349: invokevirtual 217	cn/sinobest/framework/service/bigdict/BigDictMaintainService:genBigDict	(Ljava/io/File;Ljava/util/Map;)V
     //   352: goto +33 -> 385
     //   355: astore 10
     //   357: getstatic 39	cn/sinobest/framework/service/bigdict/BigDictMaintainService:LOG	Lorg/apache/log4j/Logger;
     //   360: new 109	java/lang/StringBuilder
     //   363: dup
     //   364: ldc 221
     //   366: invokespecial 113	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
     //   369: aload 10
     //   371: invokevirtual 208	java/lang/Exception:getLocalizedMessage	()Ljava/lang/String;
     //   374: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
     //   377: invokevirtual 124	java/lang/StringBuilder:toString	()Ljava/lang/String;
     //   380: aload 10
     //   382: invokevirtual 213	org/apache/log4j/Logger:error	(Ljava/lang/Object;Ljava/lang/Throwable;)V
     //   385: aload_0
     //   386: aload 6
     //   388: aload 8
     //   390: invokevirtual 223	cn/sinobest/framework/service/bigdict/BigDictMaintainService:genAA10	(Ljava/io/File;Ljava/util/Set;)V
     //   393: ldc 227
     //   395: ldc 229
     //   397: invokestatic 231	java/io/File:createTempFile	(Ljava/lang/String;Ljava/lang/String;)Ljava/io/File;
     //   400: astore 7
     //   402: aload 7
     //   404: aload 6
     //   406: invokevirtual 235	java/io/File:listFiles	()[Ljava/io/File;
     //   409: invokestatic 239	cn/sinobest/framework/service/file/FileService:zip	(Ljava/io/File;[Ljava/io/File;)V
     //   412: getstatic 39	cn/sinobest/framework/service/bigdict/BigDictMaintainService:LOG	Lorg/apache/log4j/Logger;
     //   415: invokevirtual 245	org/apache/log4j/Logger:isDebugEnabled	()Z
     //   418: ifeq +29 -> 447
     //   421: getstatic 39	cn/sinobest/framework/service/bigdict/BigDictMaintainService:LOG	Lorg/apache/log4j/Logger;
     //   424: new 109	java/lang/StringBuilder
     //   427: dup
     //   428: ldc 248
     //   430: invokespecial 113	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
     //   433: aload 7
     //   435: invokevirtual 250	java/io/File:getAbsolutePath	()Ljava/lang/String;
     //   438: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
     //   441: invokevirtual 124	java/lang/StringBuilder:toString	()Ljava/lang/String;
     //   444: invokevirtual 253	org/apache/log4j/Logger:debug	(Ljava/lang/Object;)V
     //   447: aload 7
     //   449: astore 19
     //   451: aload 6
     //   453: invokestatic 257	cn/sinobest/framework/service/file/FileService:recurseDeleteFiles	(Ljava/io/File;)V
     //   456: aload 19
     //   458: areturn
     //   459: astore 9
     //   461: aload 7
     //   463: invokestatic 257	cn/sinobest/framework/service/file/FileService:recurseDeleteFiles	(Ljava/io/File;)V
     //   466: new 49	cn/sinobest/framework/comm/exception/AppException
     //   469: dup
     //   470: ldc_w 261
     //   473: aload 9
     //   475: invokespecial 263	cn/sinobest/framework/comm/exception/AppException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
     //   478: athrow
     //   479: astore 18
     //   481: aload 6
     //   483: invokestatic 257	cn/sinobest/framework/service/file/FileService:recurseDeleteFiles	(Ljava/io/File;)V
     //   486: aload 18
     //   488: athrow
     // Line number table:
     //   Java source line #84	-> byte code offset #0
     //   Java source line #85	-> byte code offset #12
     //   Java source line #87	-> byte code offset #14
     //   Java source line #89	-> byte code offset #21
     //   Java source line #91	-> byte code offset #26
     //   Java source line #94	-> byte code offset #38
     //   Java source line #97	-> byte code offset #47
     //   Java source line #99	-> byte code offset #50
     //   Java source line #101	-> byte code offset #53
     //   Java source line #102	-> byte code offset #62
     //   Java source line #103	-> byte code offset #80
     //   Java source line #102	-> byte code offset #92
     //   Java source line #104	-> byte code offset #97
     //   Java source line #105	-> byte code offset #103
     //   Java source line #106	-> byte code offset #111
     //   Java source line #108	-> byte code offset #120
     //   Java source line #112	-> byte code offset #146
     //   Java source line #111	-> byte code offset #165
     //   Java source line #113	-> byte code offset #170
     //   Java source line #115	-> byte code offset #178
     //   Java source line #118	-> byte code offset #181
     //   Java source line #119	-> byte code offset #183
     //   Java source line #118	-> byte code offset #185
     //   Java source line #121	-> byte code offset #193
     //   Java source line #122	-> byte code offset #203
     //   Java source line #123	-> byte code offset #213
     //   Java source line #125	-> byte code offset #218
     //   Java source line #127	-> byte code offset #221
     //   Java source line #129	-> byte code offset #228
     //   Java source line #131	-> byte code offset #239
     //   Java source line #132	-> byte code offset #249
     //   Java source line #134	-> byte code offset #257
     //   Java source line #137	-> byte code offset #275
     //   Java source line #138	-> byte code offset #294
     //   Java source line #139	-> byte code offset #296
     //   Java source line #108	-> byte code offset #334
     //   Java source line #145	-> byte code offset #344
     //   Java source line #146	-> byte code offset #355
     //   Java source line #147	-> byte code offset #357
     //   Java source line #153	-> byte code offset #385
     //   Java source line #155	-> byte code offset #393
     //   Java source line #156	-> byte code offset #402
     //   Java source line #157	-> byte code offset #412
     //   Java source line #158	-> byte code offset #421
     //   Java source line #159	-> byte code offset #447
     //   Java source line #165	-> byte code offset #451
     //   Java source line #159	-> byte code offset #456
     //   Java source line #160	-> byte code offset #459
     //   Java source line #161	-> byte code offset #461
     //   Java source line #162	-> byte code offset #466
     //   Java source line #164	-> byte code offset #479
     //   Java source line #165	-> byte code offset #481
     //   Java source line #166	-> byte code offset #486
     // Local variable table:
     //   start	length	slot	name	signature
     //   0	489	0	this	BigDictMaintainService
     //   0	489	1	dto	IDTO
     //   11	11	2	lastSynTimeStr	String
     //   13	17	3	lastSynTime	long
     //   45	76	5	dicts	String
     //   48	434	6	dir	File
     //   51	411	7	zipFile	File
     //   60	329	8	dictsSet	Set<String>
     //   118	230	9	bigDictMap	Map<String, DictEntry>
     //   459	15	9	e	IOException
     //   144	165	10	oneDict	String
     //   355	26	10	ex	Exception
     //   134	208	11	i	int
     //   131	211	12	j	int
     //   128	12	13	arrayOfString	String[]
     //   168	14	14	serviceName	String
     //   294	36	14	ex	Exception
     //   191	13	15	provider	cn.sinobest.framework.comm.iface.IBigDictProvider
     //   211	70	16	info	BigDictInfo
     //   237	5	17	glt	cn.sinobest.framework.service.tags.Glt
     //   479	8	18	localObject	Object
     //   449	8	19	localFile1	File
     // Exception table:
     //   from	to	target	type
     //   146	178	294	java/lang/Exception
     //   181	218	294	java/lang/Exception
     //   221	291	294	java/lang/Exception
     //   344	352	355	java/lang/Exception
     //   385	451	459	java/io/IOException
     //   53	451	479	finally
     //   459	479	479	finally
   }
   
   public void genBigDict(File tmpDir, Map<String, DictEntry> bigDictMap)
     throws AppException
   {
     ObjectOutputStream oss = null;
     try
     {
       File file = new File(tmpDir, "bigDict");
       OutputStream out = new BufferedOutputStream(new FileOutputStream(file), 1024);
       oss = new ObjectOutputStream(out);
       oss.writeObject(bigDictMap);
     }
     catch (IOException e)
     {
       throw new AppException("生成大字典文件失败", e);
     }
     finally
     {
       FileService.release(new Closeable[] {oss });
     }
   }
   
   public void genAA10(File dir, Set<String> dynDictSet)
     throws AppException
   {
     try
     {
       dicts = CacheManager.getDict();
     }
     catch (Exception e)
     {
       HashMap<String, CacheItem<HashMap<String, HashMap<String, String>>>> dicts;
       throw new AppException(e.getLocalizedMessage(), e);
     }
     HashMap<String, CacheItem<HashMap<String, HashMap<String, String>>>> dicts;
     Map<String, Map<String, String[]>> extractedDictMap = new HashMap(
       dicts.size());
     for (Map.Entry<String, CacheItem<HashMap<String, HashMap<String, String>>>> item : dicts.entrySet())
     {
       HashMap<String, Map<String, String>> dict = (HashMap)((CacheItem)item.getValue()).getItem();
       extractedDictMap.put((String)item.getKey(), extractDictMap(dict));
     }
     for (String oneDict : dynDictSet) {
       if (oneDict.startsWith("dyndict_")) {
         extractedDictMap.put(oneDict, 
           extractDictMap(ConfUtil.getDynDict(oneDict, "1=1")));
       }
     }
     ObjectOutputStream oos = null;
     try
     {
       File file = new File(dir, "aa10");
       OutputStream out = new BufferedOutputStream(new FileOutputStream(file), 512);
       oos = new ObjectOutputStream(out);
       oos.writeObject(extractedDictMap);
     }
     catch (IOException e)
     {
       throw new AppException("生成综合参数文件失败", e);
     }
     finally
     {
       FileService.release(new Closeable[] {oos });
     }
   }
   
   public void genAA10(Set<String> dictsSet, File dir)
     throws AppException
   {
     Map<String, Map<String, String[]>> extractedDictMap = new HashMap(
       dictsSet.size()
     for (String oneDict : dictsSet) {
       if (oneDict.startsWith("dyndict_"))
       {
         extractedDictMap.put(oneDict, 
           extractDictMap(ConfUtil.getDynDict(oneDict, "1=1")));
       }
       else if (oneDict.indexOf('@') != -1)
       {
         String[] value = oneDict.split("\\@");
         if (extractedDictMap.get(value[0]) == null) {
           extractedDictMap.put(value[0], extractDictMap(
             ConfUtil.getSubDict(value[0], value[1])));
         } else {
           ((Map)extractedDictMap.get(value[0])).putAll(
             extractDictMap(ConfUtil.getSubDict(value[0], 
             value[1])));
         }
       }
       else
       {
         extractedDictMap.put(oneDict, 
           extractDictMap(ConfUtil.getDict(oneDict)));
       }
     }
     File file = null;
     Object out = null;
     byte[] B_RECORED = { 2 };
     byte[] B_FIELD = { 3 };
     byte[] B_SECTION = { 1 };
     try
     {
       file = new File(dir, "_");
       out = new BufferedOutputStream(new FileOutputStream(file), 1048576);
       
       Iterator localIterator2 = extractedDictMap.entrySet().iterator();
       while (localIterator2.hasNext())
       {
         Map.Entry<String, Map<String, String[]>> entry = (Map.Entry)localIterator2.next();
         
         ((OutputStream)out).write(((String)entry.getKey()).getBytes());
         ((OutputStream)out).write(B_RECORED);
         if (entry.getValue() != null)
         {
           Iterator localIterator3 = ((Map)entry.getValue()).entrySet().iterator();
           while (localIterator3.hasNext())
           {
             Map.Entry<String, String[]> valueEntry = (Map.Entry)localIterator3.next();
             
             ((OutputStream)out).write(((String)valueEntry.getKey()).getBytes());
             ((OutputStream)out).write(B_FIELD);
             ((OutputStream)out).write(((String[])valueEntry.getValue())[0].getBytes());
             ((OutputStream)out).write(B_FIELD);
             ((OutputStream)out).write(((String[])valueEntry.getValue())[1].getBytes());
             ((OutputStream)out).write(B_RECORED);
           }
         }
         ((OutputStream)out).write(B_SECTION);
       }
     }
     catch (IOException e)
     {
       throw new AppException("生成综合参数文件失败", e);
     }
     finally
     {
       FileService.release(new Closeable[] {out });
     }
   }
   
   private Map<String, String[]> extractDictMap(Map<String, Map<String, String>> dict)
   {
     if (dict == null) {
       return null;
     }
     Map<String, Map<String, String>> dictSortMap = GtService.dictSort(dict);
     
 
     Map<String, String[]> liteMap = new LinkedHashMap(6);
     String AAA103 = "AAA103";
     String ITEMCODE = "ITEMCODE";
     String SUBCODE = "SUBCODE";
     for (Map.Entry<String, Map<String, String>> entry : dictSortMap.entrySet())
     {
       String[] values = { (String)Util.nvl(((Map)entry.getValue()).get("ITEMCODE")), (String)Util.nvl(((Map)entry.getValue()).get("AAA103")), 
         (String)Util.nvl(((Map)entry.getValue()).get("SUBCODE")) };
       liteMap.put((String)entry.getKey(), values);
     }
     return liteMap;
   }
   
   private DictEntry getOneDict(BigDictInfo bigDictInfo)
     throws AppException
   {
     DictEntry dictEntry = new DictEntry();
     dictEntry.keyColIndex = bigDictInfo.keyColIndex;
     dictEntry.colDict = genColDicts(bigDictInfo);
     dictEntry.columns = mergeColnums(bigDictInfo, dictEntry.colDict);
     
 
 
 
 
 
 
 
     dictEntry.datas = genInsertValues(bigDictInfo);
     return dictEntry;
   }
   
   private String[] mergeColnums(BigDictInfo bigDictInfo, String[] colDict)
   {
     int colLen = bigDictInfo.columns.length;
     String[] cols = new String[colLen + bigDictInfo.colDict.size()];
     System.arraycopy(bigDictInfo.columns, 0, cols, 0, colLen);
     int j = colLen;
     for (int i = 0; i < colLen; i++) {
       if (colDict[i] != null) {
         cols[(j++)] = ("_DIC_" + bigDictInfo.columns[i]);
       }
     }
     return cols;
   }
   
   private String[] genColDicts(BigDictInfo bigDictInfo)
   {
     String[] colDicts = new String[bigDictInfo.columns.length];
     if ((bigDictInfo.colDict != null) && (!bigDictInfo.colDict.isEmpty()))
     {
       String col = null;
       for (int index = 0; index < bigDictInfo.columns.length; index++)
       {
         col = bigDictInfo.columns[index];
         String dictValue = (String)bigDictInfo.colDict.get(col);
         if ((dictValue != null) && (dictValue.indexOf('@') != -1)) {
           colDicts[index] = dictValue.split("\\@")[0];
         } else {
           colDicts[index] = ((String)bigDictInfo.colDict.get(col));
         }
       }
     }
     return colDicts;
   }
   
   private String[][] genInsertValues(final BigDictInfo bigDictInfo)
     throws AppException
   {
     JdbcTemplate jdbcTemplate = (JdbcTemplate)Util.getBean("jdbcTemplate", 
       JdbcTemplate.class);
     final List<String[]> datas = new ArrayList();
     try
     {
       jdbcTemplate.query(bigDictInfo.selectSql4I, 
         new RowCallbackHandler()
         {
           public void processRow(ResultSet rs)
             throws SQLException
           {
             String[] row = new String[bigDictInfo.columns.length + bigDictInfo.colDict.size()];
             for (int i = 0; i < bigDictInfo.columns.length; i++) {
               row[i] = rs.getString(i + 1);
             }
             datas.add(row);
           }
         });
       String[][] dataStr = new String[datas.size()][];
       int i = 0;
       for (String[] item : datas) {
         dataStr[(i++)] = item;
       }
       return dataStr;
     }
     catch (DataAccessException e)
     {
       throw new AppException("大字典{0}获取新增字典项时出错，请检查新增查询语句", e, new Object[] {
         bigDictInfo.dictName });
     }
   }
   
   private void genDeleteKeys(final BigDictInfo bigDictInfo, final OutputStream out)
     throws AppException, IOException
   {
     StringBuilder deleteHeader = new StringBuilder("DELETE:");
     out.write(deleteHeader.toString().getBytes());
     JdbcTemplate jdbcTemplate = (JdbcTemplate)Util.getBean("jdbcTemplate", 
       JdbcTemplate.class);
     try
     {
       jdbcTemplate.query(bigDictInfo.selectSql4D, 
         new RowCallbackHandler()
         {
           public void processRow(ResultSet rs)
             throws SQLException
           {
             StringBuilder sb = new StringBuilder(rs
               .getString(1)).append('\003');
             byte[] bytes = sb.toString().getBytes();
             try
             {
               out.write(bytes, 0, bytes.length);
             }
             catch (IOException e)
             {
               throw new AppException(
                 "无法写数据到字典临时文件，所在的字典是{0}。", e, new Object[] {
                 bigDictInfo.dictName });
             }
           }
         });
     }
     catch (DataAccessException e)
     {
       throw new AppException("大字典{0}获取新增字典项时出错，请检查新增查询语句", e, new Object[] {
         bigDictInfo.dictName });
     }
   }
   
   private void genColumns(BigDictInfo bigDictInfo, OutputStream out)
     throws IOException
   {
     StringBuilder sb = new StringBuilder(256).append("COLS:");
     merge(sb, bigDictInfo.columns);
     out.write(sb.toString().getBytes());
   }
   
   private void genKeyIndex(BigDictInfo bigDictInfo, OutputStream out)
     throws IOException
   {
     StringBuilder sb = new StringBuilder("KEYINDEX:")
       .append((byte)bigDictInfo.keyColIndex);
     
     out.write(sb.toString().getBytes());
   }
   
   private StringBuilder merge(StringBuilder dest, String[] arrays)
   {
     StringBuilder sb = dest;
     if (sb == null) {
       sb = new StringBuilder();
     }
     for (int i = 0; i < arrays.length; i++)
     {
       if (arrays[i] != null) {
         sb.append(arrays[i]);
       }
       if (arrays.length > i + 1) {
         sb.append('\003');
       }
     }
     return sb;
   }
 }