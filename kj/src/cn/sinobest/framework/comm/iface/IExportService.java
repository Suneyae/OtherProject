package cn.sinobest.framework.comm.iface;

import cn.sinobest.framework.comm.exception.AppException;
import java.io.File;

public abstract interface IExportService
{
  public abstract File exportFile(IDTO paramIDTO)
    throws AppException;
}