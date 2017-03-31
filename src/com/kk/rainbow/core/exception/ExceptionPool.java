package com.kk.rainbow.core.exception;

import java.util.HashMap;

import com.kk.rainbow.core.exception.oo.ExceptionMsg;

public final class ExceptionPool
{
  private static final HashMap hm = new HashMap();

  protected static void add(ExceptionMsg paramExceptionMsg)
  {
    hm.put(paramExceptionMsg.getCode(), paramExceptionMsg);
  }

  public static ExceptionMsg get(String paramString)
  {
    return hm.get(paramString) == null ? null : (ExceptionMsg)hm.get(paramString);
  }
}