package com.kk.rainbow.core.mybatis3;

public class MySql5Dialect extends Dialect
{
  protected static final String SQL_END_DELIMITER = ";";

  public String getLimitString(String paramString, boolean paramBoolean)
  {
    return MySql5PageHepler.getLimitString(paramString, -1, -1);
  }

  public String getLimitString(String paramString, int paramInt1, int paramInt2)
  {
    return MySql5PageHepler.getLimitString(paramString, paramInt1, paramInt2);
  }

  public boolean supportsLimit()
  {
    return true;
  }
}