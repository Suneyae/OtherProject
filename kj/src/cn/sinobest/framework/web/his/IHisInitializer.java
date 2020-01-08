package cn.sinobest.framework.web.his;

import java.util.HashMap;

public abstract interface IHisInitializer
{
  public abstract HashMap getFMMap();
  
  public abstract HashMap getObjCache();
  
  public abstract void init();
  
  public abstract void clear();
}