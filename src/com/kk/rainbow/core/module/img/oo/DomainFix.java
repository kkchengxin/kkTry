package com.kk.rainbow.core.module.img.oo;

public enum DomainFix
{
  NET(".net"), COM(".com"), CN(".cn"), ORG(".org");

  private String fix;

  private DomainFix(String paramString)
  {
    this.fix = paramString;
  }

  public String getDomainFix()
  {
    return this.fix;
  }
}