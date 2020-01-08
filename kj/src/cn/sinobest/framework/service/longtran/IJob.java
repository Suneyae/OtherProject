package cn.sinobest.framework.service.longtran;

import org.quartz.Job;

public abstract interface IJob
  extends Job
{
  public static final String LONG_TRANS_PREFIX = "_longTrans";
  public static final String CTYPE_JAVA = "JAVA";
  public static final String CTYPE_PROCEDURE = "PROC";
  public static final String FIRES_FORE = "FORE";
  public static final String FIRES_BACK = "BACK";
  public static final String PROCESS_CLASS = "processClass";
  public static final String MEMO = "memo";
  public static final String JOB_DATA_TRANCONF = "JOB_DATA_TRANCONF";
  public static final String CTYPE = "CTYPE";
  public static final String KEY = "_longTransKey";
  public static final String TRIGGER = "_longTransTrigger";
  public static final String LONG_TRANS_ID = "_longTransID";
}