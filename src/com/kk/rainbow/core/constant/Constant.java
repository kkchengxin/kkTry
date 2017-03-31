package com.kk.rainbow.core.constant;

import java.io.Serializable;

public final class Constant
  implements Serializable
{
  public static final int PAGESIZE = 10;

  public static final class config
  {
    public static final String MODULES_FILE_PATH = "WEB-INF/conf/";
    public static final String MODULES_FILE_NAME = "modules.xml";
    public static final String MODULES_ROOT = "modules";
  }

  public static final class Log
  {
    public static final String DEBUG = "DEBUG";
    public static final String INFO = "INFO";
    public static final String WARN = "WARN";
    public static final String ERROR = "ERROR";
  }
}