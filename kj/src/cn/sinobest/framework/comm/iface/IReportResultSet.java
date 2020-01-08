package cn.sinobest.framework.comm.iface;

import cn.sinobest.framework.comm.exception.AppException;
import cn.sinobest.framework.service.entities.ReportConfig;
import java.util.List;
import java.util.Map;

public abstract interface IReportResultSet
{
  public abstract ReportRS getResultSet(ReportConfig paramReportConfig, String paramString, Map<String, String> paramMap)
    throws AppException;
  
  public static class ReportRS
  {
    public String[] colNames;
    public List<Object[]> datas;
  }
}