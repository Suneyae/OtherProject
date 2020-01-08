package cn.sinobest.framework.comm.iface;

import cn.sinobest.framework.comm.exception.AppException;

public abstract interface ILongProcess
{
  public abstract void execute(IDTO paramIDTO)
    throws AppException;
}