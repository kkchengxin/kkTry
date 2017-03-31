package com.kk.rainbow.core.json;

public enum JSONKeys
{
  CODE("code"), DESC("desc"), DATA("data"), PAGE("page"), LIST("list");

  String key = null;

  private JSONKeys(String paramString)
  {
    this.key = paramString;
  }

  public String getValue()
  {
    return this.key;
  }
}