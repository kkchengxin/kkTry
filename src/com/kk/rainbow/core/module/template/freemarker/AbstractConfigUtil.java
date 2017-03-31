package com.kk.rainbow.core.module.template.freemarker;

import freemarker.template.Configuration;
import java.io.IOException;
import javax.servlet.ServletContext;

public abstract class AbstractConfigUtil
{
  protected abstract Configuration getConfiguration(String paramString)
    throws IOException;

  protected abstract Configuration getConfiguration(Class paramClass, String paramString)
    throws Exception;

  protected abstract Configuration getConfiguration(ServletContext paramServletContext, String paramString)
    throws Exception;
}