package com.kk.rainbow.core.module.img.oo;

import java.io.Serializable;

public final class Img
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String precise;
  private String url;
  private int width;
  private int height;

  public int getHeight()
  {
    return this.height;
  }

  public void setHeight(int paramInt)
  {
    this.height = paramInt;
  }

  public int getWidth()
  {
    return this.width;
  }

  public void setWidth(int paramInt)
  {
    this.width = paramInt;
  }

  public String getPrecise()
  {
    return this.precise;
  }

  public void setPrecise(String paramString)
  {
    this.precise = paramString;
  }

  public String getUrl()
  {
    return this.url;
  }

  public void setUrl(String paramString)
  {
    this.url = paramString;
  }
}