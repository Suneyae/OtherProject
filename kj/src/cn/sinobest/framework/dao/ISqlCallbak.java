package cn.sinobest.framework.dao;

public abstract interface ISqlCallbak<T>
{
  public abstract T doSql()
    throws Exception;
}