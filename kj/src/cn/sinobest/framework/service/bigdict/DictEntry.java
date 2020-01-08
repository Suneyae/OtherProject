 package cn.sinobest.framework.service.bigdict;
 
 import java.io.Serializable;
 
 public class DictEntry
   implements Serializable
 {
   private static final long serialVersionUID = 1L;
   public String[] columns;
   public int keyColIndex;
   public long lastSynTime;
   public long currentSynTime;
   public String dictName;
   public String[] colDict;
   public String[][] datas;
   public transient boolean isNew = false;
 }