package com.kk.rainbow.core.mybatis3;

public class OracleDialect extends Dialect
{
  public String getLimitString(String paramString, int paramInt1, int paramInt2)
  {
    paramString = paramString.trim();
    int i = 0;
    if (paramString.toLowerCase().endsWith(" for update"))
    {
      paramString = paramString.substring(0, paramString.length() - 11);
      i = 1;
    }
    StringBuffer localStringBuffer = new StringBuffer(paramString.length() + 100);
    localStringBuffer.append("select * from ( select row_.*, rownum rownum_ from ( ");
    localStringBuffer.append(paramString);
    localStringBuffer.append(" ) row_ ) where rownum_ > " + paramInt1 + " and rownum_ <= " + (paramInt1 + paramInt2));
    if (i != 0)
      localStringBuffer.append(" for update");
    return localStringBuffer.toString();
  }
}