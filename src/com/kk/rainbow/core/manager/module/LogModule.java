package com.kk.rainbow.core.manager.module;

public class LogModule
{
  private static final LogModule logEle = new LogModule();
  private String isOpen = "true";

  public static final LogModule getInstance()
  {
    return logEle == null ? new LogModule() : logEle;
  }

  public String getIsOpen()
  {
    return this.isOpen;
  }

  public void setIsOpen(String paramString)
  {
    this.isOpen = paramString;
  }
}