package cn.sinobest.framework.web.interceptor;

public abstract interface Cleanable
{
  public abstract void cleanDo()
    throws Exception;
}