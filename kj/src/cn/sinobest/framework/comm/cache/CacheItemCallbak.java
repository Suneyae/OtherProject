package cn.sinobest.framework.comm.cache;

public abstract interface CacheItemCallbak<T>
{
  public abstract T getItem();
}