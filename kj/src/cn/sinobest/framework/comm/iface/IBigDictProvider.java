package cn.sinobest.framework.comm.iface;

import cn.sinobest.framework.service.entities.BigDictInfo;

public abstract interface IBigDictProvider
{
  public abstract BigDictInfo getDictsInfo(IDTO paramIDTO);
}