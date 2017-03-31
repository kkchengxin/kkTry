package com.kk.rainbow.core.security;

import com.kk.rainbow.core.exception.RException;

public final class SecurityFactory
{
  public static final String AES = "AES";
  private static ISecurity aes = null;

  public static ISecurity getInstance(String paramString)
    throws RException
  {
    if ((paramString == null) || (paramString.trim().equals("")))
      throw new RException("-1", "code is required");
    if ("AES".equals(paramString))
    {
      if (aes == null)
        aes = new AESSecurity();
      return aes;
    }
    throw new RException("-1", "Not have code match '" + paramString + "'");
  }
}