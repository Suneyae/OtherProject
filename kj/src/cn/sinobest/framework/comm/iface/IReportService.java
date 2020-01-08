package cn.sinobest.framework.comm.iface;

import cn.sinobest.framework.comm.exception.AppException;
import cn.sinobest.framework.service.entities.ReportConfig;
import com.runqian.report4.dataset.DataSet;
import com.runqian.report4.usermodel.Context;
import com.runqian.report4.usermodel.DataSetConfig;

public abstract interface IReportService
{
  public abstract ReportConfig getConfig(String paramString)
    throws AppException;
  
  public abstract void afterPrint(IDTO paramIDTO)
    throws AppException;
  
  public abstract DataSet createDataSet(Context paramContext, DataSetConfig paramDataSetConfig, boolean paramBoolean);
}