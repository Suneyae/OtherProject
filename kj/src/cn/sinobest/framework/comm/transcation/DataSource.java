package cn.sinobest.framework.comm.transcation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource
{
  public static final String DS1 = "ds1";
  public static final String DS2 = "ds2";
  public static final String DS3 = "ds3";
  public static final String DS4 = "ds4";
  public static final String DS5 = "ds5";
  
  String name();
}