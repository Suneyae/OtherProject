package cn.sinobest.framework.service.longtran;

import cn.sinobest.framework.comm.exception.AppException;
import cn.sinobest.framework.comm.iface.IDTO;
import java.sql.Connection;
import java.sql.SQLException;

public abstract interface ILongProcessCallBack<T>
{
  public abstract T doAction(Connection paramConnection, IDTO paramIDTO)
    throws AppException, SQLException;
}