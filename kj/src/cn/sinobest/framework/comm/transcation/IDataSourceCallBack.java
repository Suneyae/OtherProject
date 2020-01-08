package cn.sinobest.framework.comm.transcation;

import cn.sinobest.framework.comm.exception.AppException;

public abstract interface IDataSourceCallBack<T>
{
  public abstract T doAction()
    throws AppException;
}