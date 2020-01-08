package cn.sinobest.framework.service.workflow;

import cn.sinobest.framework.comm.exception.AppException;
import cn.sinobest.framework.comm.iface.IDTO;

public abstract interface IWorkflowProxyService
{
  public static final String TASK_SPLIT = ",";
  public static final String PROC_PATTERN = "^pkg_\\w+\\.\\w+$";
  
  public abstract Object doWork(IDTO paramIDTO)
    throws AppException;
}