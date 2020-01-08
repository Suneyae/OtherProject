package cn.sinobest.framework.webservice;

import javax.jws.WebService;

@WebService
public abstract interface IWebService
{
  public abstract String doAction(String paramString);
}