package com.kk.rainbow.core.module.template;

import freemarker.template.TemplateException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractTemplate
{
  protected String templateRootPath;

  public String getTemplateRootPath()
  {
    return this.templateRootPath;
  }

  public void setTemplateRootPath(String paramString)
  {
    this.templateRootPath = paramString;
  }

  public abstract String getTargetCode(String paramString, Object paramObject);

  public abstract boolean privew(String paramString, Object paramObject, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse);

  public abstract boolean privew(String paramString1, Object paramObject, String paramString2, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
    throws IOException, TemplateException, ClassNotFoundException;

  public abstract String process(String paramString1, Object paramObject, String paramString2, String paramString3);

  public abstract String process(String paramString1, Object paramObject, String paramString2, String paramString3, String paramString4);
}