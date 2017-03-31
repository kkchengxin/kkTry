package com.kk.rainbow.core.module.template;

import com.kk.rainbow.core.exception.RException;
import com.kk.rainbow.core.module.template.freemarker.Freemarker;

public class TemplateFactory
{
  public static final String FREEMARKER = "freemarker";
  private static AbstractTemplate abstractTemplate = null;

  public static AbstractTemplate getBean(String paramString)
    throws RException
  {
    if ("freemarker".equals(paramString))
    {
      if ((abstractTemplate == null) || ((abstractTemplate instanceof Freemarker)))
        abstractTemplate = new Freemarker();
      return abstractTemplate;
    }
    throw new RException("||TemplateFactory cant't provide the \"" + paramString + "\" instance.");
  }
}