package cn.sinobest.framework.service.entities;

import java.util.Map;

public class BigDictInfo
{
  public String[] columns;
  public int keyColIndex;
  public String selectSql4D;
  public String selectSql4I;
  public long lastSynTime;
  public long currentSynTime;
  public String dictName;
  public Map<String, String> colDict;
}