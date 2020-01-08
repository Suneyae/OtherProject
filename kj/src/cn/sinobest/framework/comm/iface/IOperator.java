package cn.sinobest.framework.comm.iface;

import java.util.List;

public abstract interface IOperator
  extends Cloneable
{
  public abstract String getOperID();
  
  public abstract String getLoginID();
  
  public abstract String getOperUnitID();
  
  public abstract String getOperUnitName();
  
  public abstract String getOperType();
  
  public abstract String getOperType2();
  
  public abstract String getOperName();
  
  public abstract String getBAE001();
  
  public abstract String getOrgName();
  
  public abstract String getAAC001();
  
  public abstract String getAAC002();
  
  public abstract String getAAB001();
  
  public abstract String getAAB999();
  
  public abstract String getPWENCRYPT();
  
  public abstract String getPWMODIFIED();
  
  public abstract String getPOSITION();
  
  public abstract String getAAE100();
  
  public abstract boolean getCheckResult();
  
  public abstract String getCheckMsg();
  
  public abstract boolean getLoginStatus();
  
  public abstract String getLoginStatusMsg();
  
  public abstract List getMenus();
  
  public abstract List getRights();
  
  public abstract String getInterFaceStyle();
  
  public abstract List getShortCutMenus();
  
  public abstract String getLoginFail();
  
  public abstract String getLoginTime();
  
  public abstract void setOperID(String paramString);
  
  public abstract void setLoginID(String paramString);
  
  public abstract void setOperUnitID(String paramString);
  
  public abstract void setOperUnitName(String paramString);
  
  public abstract void setOperType(String paramString);
  
  public abstract void setOperType2(String paramString);
  
  public abstract void setOperName(String paramString);
  
  public abstract void setBAE001(String paramString);
  
  public abstract void setOrgName(String paramString);
  
  public abstract void setAAC001(String paramString);
  
  public abstract void setAAC002(String paramString);
  
  public abstract void setAAB001(String paramString);
  
  public abstract void setAAB999(String paramString);
  
  public abstract void setPWENCRYPT(String paramString);
  
  public abstract void setPWMODIFIED(String paramString);
  
  public abstract void setPOSITION(String paramString);
  
  public abstract void setAAE100(String paramString);
  
  public abstract void setCheckResult(boolean paramBoolean);
  
  public abstract void setCheckMsg(String paramString);
  
  public abstract void setLoginStatus(boolean paramBoolean);
  
  public abstract void setLoginStatusMsg(String paramString);
  
  public abstract void setShortCutMenus(String paramString);
  
  public abstract void setInterFaceStyle(String paramString);
  
  public abstract void setRights(List paramList);
  
  public abstract void setMenus(List paramList);
  
  public abstract void setLoginFail(String paramString);
  
  public abstract void setLoginTime(String paramString);
}