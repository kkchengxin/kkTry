package com.kk.rainbow.core.exception;

import org.codehaus.jackson.map.annotate.JsonFilter;
@JsonFilter("RException")
public class RException extends Exception
{
  private String code;
  private String desc;
  private static final long serialVersionUID = 1L;
  public static final String SPLIT = "\\|";

  public RException(String paramString1, String paramString2)
  {
    super(paramString1 + "|" + paramString2);
    this.code = paramString1;
    this.desc = paramString2;
  }

  public RException()
  {
  }

  public RException(String paramString)
  {
    super(paramString);
  }

  public RException(Throwable paramThrowable)
  {
    super(paramThrowable);
  }

  public RException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }

  public static final String toJson(String paramString)
  {
    if (paramString == null)
      return null;
    String[] arrayOfString = paramString.split("\\|");
    if ((arrayOfString == null) || (arrayOfString.length == 0))
      return null;
    String str1 = arrayOfString[0];
    String str2 = arrayOfString.length == 2 ? arrayOfString[1] : null;
    return toJson(str1, str2);
  }

  public static final String toJson(String paramString1, String paramString2)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append("code");
    localStringBuilder.append(":\"");
    localStringBuilder.append(paramString1);
    localStringBuilder.append("\",");
    localStringBuilder.append("desc");
    localStringBuilder.append(":\"");
    localStringBuilder.append(paramString2);
    localStringBuilder.append("\"");
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }

  public String getCode()
  {
    return this.code;
  }

  public String getDesc()
  {
    return this.desc;
  }
}