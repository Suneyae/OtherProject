 package cn.sinobest.framework.service.workflow;
 
 public abstract interface IWorkflow
 {
   public static final String WI_RETURN_PRE = "!!";
   public static final String NEXT_DEF_ID_SPLIT = ",";
   public static final String BRANCH_SPLIT = "@";
   public static final String GWDB = "-1";
   public static final String PARENTINS_ID = "ppid";
   public static final String RELATEINS_ID = "rid";
   public static final String PROCESS_ID = "pid";
   public static final String WORKITEM_ID = "wid";
   public static final String IS_SUB_WF = "_isSubWf";
   public static final String UNITID = "_unitId";
   public static final String UNITTYPE = "_unitType";
   public static final String TO_UNIT_OPR = "_toUnitOpr";
   public static final String YWLS_PREFIX = "prefix";
   public static final String YWLS_LENGTH = "length";
   public static final String YWLS_SEQ = "seq";
   public static final String ATTR_CUS_YWLSH = "useCusYwlsh";
   public static final String ATTR_AFTER_ACT = "afterAction";
   public static final String WF_STATE = "_wfState";
   public static final String ISWFSTART = "_isWfStart";
   public static final String URL = "_rtnURL";
   public static final String STATE = "state";
   public static final String PROCESS_DEF_NAME = "_processDefName";
   public static final String PROCESS_DEF_ID = "_processDefId";
   public static final String CUR_ACT_DEF_ID = "_curActDefId";
   public static final String CUR_ACT_ATTR = "_attr";
   public static final String CUR_ACT_DEF_NAME = "_curActDefName";
   public static final String NEXT_ACT_DEF_ID = "_nextActDefId";
   public static final String NEXT_ACT_DEF_NAME = "_nextActDefName";
   public static final String KEY_DATA = "_keyData";
   public static final String AAB001 = "_aab001";
   public static final String AAC001 = "_aac001";
   public static final String REJECT_REASON = "_rejectReason";
   public static final String ARCHIVEID = "_archiveId";
   public static final String OPERID = "_operId";
   public static final String ACCEPTER = "_accepterId";
   public static final String BAE006 = "_bae006";
   public static final String COMMENT = "_comment";
   public static final String SUBMIT_TYPE = "_submitType";
   public static final String SUBMIT_PARAMS = "_submitParams";
   
   public static enum Attr
   {
     SAVEBTN("savebtn", "是否显示保存按钮true/false,默认为false"),  DISABLED("disabled", "是否屏蔽输入true/false,默认为false"),  TOAPPLYOPR("toApplyOpr", "回退时是否回退给原提交人"),  TOUNITOPR("toUnitOpr", "是否是提交给单位"),  GETITEMOPR("getItemOpr", "获取指定环节经办人"),  DEFAOPR("defvOpr", "默认选择接收人"),  UNITTYPE("unitType", "接收人范围"),  UPLOADFILE("uploadFile", "是否显示上传影像资料按钮true/false,默认为false"),  DOWNFILE("downfile", "是否显示下载影像资料多记录表true/false,默认为false"),  GD("gd", "归档true/false,默认为false");
     
     private String name;
     private String desc;
     
     private Attr(String name, String desc)
     {
       this.name = name;
       this.desc = desc;
     }
     
     public String toString()
     {
       return this.name;
     }
   }
   
   public static enum ProcStartOrEnd
   {
     PSE_START("start", "开始环节"),  PSE_END("end", "结始环节");
     
     private String desc;
     private String state;
     
     private ProcStartOrEnd(String state, String desc)
     {
       this.state = state;
       this.desc = desc;
     }
     
     public String getDesc()
     {
       return this.desc;
     }
     
     public String getState()
     {
       return this.state;
     }
     
     public int getOrdinal()
     {
       return ordinal();
     }
   }
   
   public static enum RightMsg
   {
     ID("RIGHTID", "权限ID"),  URL("URL", "导航路径"),  BUSSFUNCID("BUSSFUNCID", "业务功能ID");
     
     private String id;
     private String desc;
     
     private RightMsg(String id, String desc)
     {
       this.id = id;
       this.desc = desc;
     }
     
     public String toString()
     {
       return this.id;
     }
   }
   
   public static enum IsReturn
   {
     N("0", "否"),  Y("1", "是");
     
     private String desc;
     private String state;
     
     private IsReturn(String state, String desc)
     {
       this.state = state;
       this.desc = desc;
     }
     
     public String getDesc()
     {
       return this.desc;
     }
     
     public String getState()
     {
       return this.state;
     }
     
     public int getOrdinal()
     {
       return ordinal();
     }
   }
   
   public static enum ProcInsState
   {
     PIS_READY("1", "未开始"),  PIS_DOING("2", "进行中"),  PIS_CANCEL("3", "取消"),  PIS_END("5", "结束"),  PIS_SUB_DOING("6", "子流程进行中"),  PIS_PROC_BACK("99", "退单");
     
     private String desc;
     private String state;
     
     private ProcInsState(String state, String desc)
     {
       this.state = state;
       this.desc = desc;
     }
     
     public String getDesc()
     {
       return this.desc;
     }
     
     public String getState()
     {
       return this.state;
     }
     
     public int getOrdinal()
     {
       return ordinal();
     }
     
     public static String getDesc(String state)
     {
       if (state == null) {
         return "";
       }
       for (ProcInsState subState : values()) {
         if (state.equals(subState.state)) {
           return subState.desc;
         }
       }
       return "";
     }
   }
   
   public static enum WorkItemState
   {
     WIS_READY("0", "未开始"),  WIS_DOING("2", "进行中"),  WIS_CANCEL("3", "已取消"),  WIS_END("5", "结束");
     
     private String desc;
     private String state;
     
     private WorkItemState(String state, String desc)
     {
       this.state = state;
       this.desc = desc;
     }
     
     public String getDesc()
     {
       return this.desc;
     }
     
     public String getState()
     {
       return this.state;
     }
     
     public int getOrdinal()
     {
       return ordinal();
     }
     
     public static String getDesc(String state)
     {
       if (state == null) {
         return "";
       }
       for (WorkItemState subState : values()) {
         if (state.equals(subState.state)) {
           return subState.desc;
         }
       }
       return "";
     }
   }
   
   public static enum WorkItemType
   {
     WIT_SINGLE("1", "单线环节"),  WIT_AND("2", "与分支环节"),  WIT_OR("3", "或分支环节"),  WIT_RESTORE("8", "结束之后换醒环节");
     
     private String desc;
     private String state;
     
     private WorkItemType(String state, String desc)
     {
       this.state = state;
       this.desc = desc;
     }
     
     public String getDesc()
     {
       return this.desc;
     }
     
     public String getState()
     {
       return this.state;
     }
     
     public int getOrdinal()
     {
       return ordinal();
     }
   }
   
   public static enum SubmitType
   {
     NEXT("1", "前进"),  BACK("2", "回退");
     
     public final String desc;
     public final String type;
     
     private SubmitType(String type, String desc)
     {
       this.type = type;
       this.desc = desc;
     }
     
     public String getDesc()
     {
       return this.desc;
     }
     
     public int getOrdinal()
     {
       return ordinal();
     }
   }
 }