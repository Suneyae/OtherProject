package cn.sinobest.framework.comm.iface;

import java.util.List;

public abstract interface IPageDAO
{
  public abstract List<String[]> getData(String paramString, int paramInt1, int paramInt2)
    throws Exception;
  
  public abstract Long getCount(String paramString)
    throws Exception;
}