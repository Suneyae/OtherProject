package cn.sinobest.framework.comm.iface;

import cn.sinobest.framework.comm.exception.AppException;

public abstract interface IReportFeedback
{
  public static final String KEY_REPORTCONFIG = "ReportConfig";
  
  public abstract void afterPrint(IDTO paramIDTO)
    throws AppException;
}