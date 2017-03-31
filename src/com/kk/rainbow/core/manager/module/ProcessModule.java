package com.kk.rainbow.core.manager.module;

public class ProcessModule
{
  private static final ProcessModule process = new ProcessModule();
  private String isOpen = "true";

  public static final ProcessModule getInstance()
  {
    return process == null ? new ProcessModule() : process;
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