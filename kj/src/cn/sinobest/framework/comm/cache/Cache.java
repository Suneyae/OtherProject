 package cn.sinobest.framework.comm.cache;
 
public enum Cache
 {
   CACHE_SYS("SysConfCache", "系统参数", SysConfCache.class),  CACHE_PRINT("PrintConfCache", "打印配置信息", PrintConfCache.class),  CACHE_EXCEL("ExcelConfCache", "Excel配置信息", ExcelConfCache.class),  CACHE_WORKFLOW("WfConfCache", "流程定义信息", WfConfCache.class),  CACHE_WORKFLOWTASK("WfTaskCache", "流程环节与业务处理关联信息", WfTaskCache.class),  CACHE_GENTBL("GenTblConfCache", "单记录表配置信息", GenTblConfCache.class),  CACHE_GENLIST("GenListConfCache", "多记录表配置信息", GenListConfCache.class),  CACHE_SQL("SqlConfCache", "SQL配置信息", SqlConfCache.class),  CACHE_RIGHT("RightCache", "权限配置", RightCache.class),  CACHE_TIPMSG("TipMsgCache", "信息提示", TipMsgCache.class),  CACHE_DICT("DictCache", "字典项", DictCache.class);
   
   public final String name;
   public final String desc;
   public final Class cls;
   
   private Cache(String name, String desc, Class cls)
   {
     this.name = name;
     this.desc = desc;
     this.cls = cls;
   }
   
   public String toString()
   {
     return this.name;
   }
 }