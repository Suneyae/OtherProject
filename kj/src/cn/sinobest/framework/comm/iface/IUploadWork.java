package cn.sinobest.framework.comm.iface;

import cn.sinobest.framework.comm.exception.AppException;
import cn.sinobest.framework.service.file.ImpExpConfig;
import java.io.Closeable;
import java.util.Enumeration;
import java.util.Map;

public abstract interface IUploadWork
  extends Closeable
{
  public abstract Object doWork(ImpExpConfig paramImpExpConfig, Map<String, Object> paramMap, Enumeration<Map<String, Object>> paramEnumeration)
    throws AppException;
}