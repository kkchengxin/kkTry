package com.kk.rainbow.core.module.log;

import org.apache.log4j.Level;

public abstract interface RainbowLogLevel
{
  public static final Level LEVEL_APP = new LevelApp(50000, "APP", 128);
}