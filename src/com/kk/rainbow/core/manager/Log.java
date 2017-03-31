package com.kk.rainbow.core.manager;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import sun.reflect.Reflection;

import com.kk.rainbow.core.module.log.RainbowLogLevel;

public class Log
{
  public static void debug(String paramString)
  {
    doLog("DEBUG", paramString);
  }

  public static void info(String paramString)
  {
    doLog("INFO", paramString);
  }

  public static void warn(String paramString)
  {
    doLog("WARN", paramString);
  }

  public static void error(String paramString)
  {
    doLog("ERROR", paramString);
  }

  public static void app(String paramString)
  {
    Logger.getRootLogger().log(RainbowLogLevel.LEVEL_APP, paramString);
  }

  private static void doLog(String paramString1, String paramString2)
  {
    Logger.getLogger(Reflection.getCallerClass(3)).log(Level.toPriority(paramString1), paramString2);
  }
}