package com.kk.rainbow.core.mybatis3;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MySql5PageHepler
{
  public static String getCountString(String paramString)
  {
    paramString = getLineSql(paramString);
    int i = getLastOrderInsertPoint(paramString);
    int j = getAfterFormInsertPoint(paramString);
    String str = paramString.substring(0, j);
    if ((str.toLowerCase().indexOf("select distinct") != -1) || (paramString.toLowerCase().indexOf("group by") != -1))
    {
      StringBuffer localStringBuffer = new StringBuffer(paramString.length()).append("select count(1) count from (");
      if (i != -1)
        return localStringBuffer.append(paramString.substring(0, i)).append(" ) t").toString();
      return localStringBuffer.append(paramString.substring(0)).append(" ) t").toString();
    }
    StringBuffer localStringBuffer = new StringBuffer(paramString.length()).append("select count(1) count ");
    if (i != -1)
      return localStringBuffer.append(paramString.substring(j, i)).toString();
    return localStringBuffer.append(paramString.substring(j)).toString();
  }

  private static int getLastOrderInsertPoint(String paramString)
  {
    int i = paramString.toLowerCase().lastIndexOf("order by");
    return i;
  }

  public static String getLimitString(String paramString, int paramInt1, int paramInt2)
  {
    paramString = getLineSql(paramString);
    String str = paramString + " limit " + paramInt1 + " ," + paramInt2;
    return str;
  }

  private static String getLineSql(String paramString)
  {
    return paramString.replaceAll("[\r\n]", " ").replaceAll("\\s{2,}", " ");
  }

  private static int getAfterFormInsertPoint(String paramString)
  {
    String str1 = "\\s+FROM\\s+";
    Pattern localPattern = Pattern.compile(str1, 2);
    Matcher localMatcher = localPattern.matcher(paramString);
    while (localMatcher.find())
    {
      int i = localMatcher.start(0);
      String str2 = paramString.substring(0, i);
      if (isBracketCanPartnership(str2))
        return i;
    }
    return 0;
  }

  private static boolean isBracketCanPartnership(String paramString)
  {
    return (paramString != null) && (getIndexOfCount(paramString, '(') == getIndexOfCount(paramString, ')'));
  }

  private static int getIndexOfCount(String paramString, char paramChar)
  {
    int i = 0;
    for (int j = 0; j < paramString.length(); j++)
      i = paramString.charAt(j) == paramChar ? i + 1 : i;
    return i;
  }
}