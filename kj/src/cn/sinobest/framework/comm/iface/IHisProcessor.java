 package cn.sinobest.framework.comm.iface;
 
 import cn.sinobest.framework.comm.exception.AppException;
 import cn.sinobest.framework.util.ConfUtil;
 import cn.sinobest.framework.web.his.FuncModel;
 import cn.sinobest.framework.web.his.IHisInitializer;
 
 public abstract interface IHisProcessor
 {
   public static final String ISCOMPRESS = "isCompress";
   public static final String COMPRESS = "Compress";
   public static final String SESSOINID = "SESSOINID";
   public static final String EXEC_NUM = "ExecNum";
   public static final String FUNC_NO = "FuncNo";
   public static final String FUNC_ISTRAN = "isTranFn";
   public static final String HIP = "HisIP";
   public static final String HIS_LSH = "PI_HIS_LSH";
   public static final String RESP_DATA = "RESP_DATA";
   public static final String REQU_CHARACTER_ENCODING = ConfUtil.getSysParamOnly("his.request_character_encoding", "GBK");
   public static final String RESP_CHARACTER_ENCODING = ConfUtil.getSysParamOnly("his.response_character_encoding", "GBK");
   public static final String COMPRESS_CHARACTER_ENCODING = ConfUtil.getSysParamOnly("his.compress_charset", "GBK");
   public static final String HTTP_REQUEST = "http_request";
   public static final String HTTP_RESPONSE = "http_response";
   public static final String HIS_INITIALIZER = "INITIALIZER";
   public static final String FUNCMODEL_IN = "inFuncModel";
   public static final String FUNCMODEL_OUT = "outFuncModel";
   public static final String FHZ_FAILURE = "-1";
   public static final String FHZ_SUCCESS = "1";
   public static final String FHZ = "FHZ";
   public static final String MSG = "MSG";
   public static final String HELPMSG = "HELPMSG";
   public static final String HELPMSG_DEFAULT_VALUE = "发生错误！请联系系统管理员!";
   
   public abstract IHisInitializer hisInit()
     throws AppException;
   
   public abstract FuncModel processModel(IHisInitializer paramIHisInitializer, FuncModel paramFuncModel)
     throws AppException;
   
   public abstract void handle(IDTO paramIDTO)
     throws AppException;
   
   public abstract IOperator getUserInfo(String paramString1, String paramString2, String paramString3)
     throws AppException;
 }