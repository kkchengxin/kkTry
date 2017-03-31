package com.kk.rainbow.core.module.template.freemarker;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import javax.servlet.ServletContext;

public final class ConfigUtil extends AbstractConfigUtil
{
  private Configuration cfg;
  private static final ConfigUtil configUtil = new ConfigUtil();

  public static final ConfigUtil getInstance()
  {
    return configUtil;
  }

  protected Configuration getConfiguration(String paramString)
    throws IOException
  {
    if (paramString == null)
      throw new FileNotFoundException();
    if (this.cfg != null)
    {
      this.cfg.setDirectoryForTemplateLoading(new File(paramString));
      return this.cfg;
    }
    this.cfg = new Configuration();
    init();
    this.cfg.setDirectoryForTemplateLoading(new File(paramString));
    return this.cfg;
  }

  protected Configuration getConfiguration(Class paramClass, String paramString)
    throws ClassNotFoundException
  {
    if (paramClass == null)
      throw new ClassNotFoundException();
    if (this.cfg != null)
    {
      this.cfg.setClassForTemplateLoading(paramClass, paramString);
      return this.cfg;
    }
    this.cfg = new Configuration();
    init();
    this.cfg.setClassForTemplateLoading(paramClass, paramString);
    return this.cfg;
  }

  public Configuration getConfiguration(ServletContext paramServletContext, String paramString)
    throws Exception
  {
    if (paramServletContext == null)
      throw new Exception();
    if (this.cfg != null)
    {
      this.cfg.setServletContextForTemplateLoading(paramServletContext, paramString);
      return this.cfg;
    }
    this.cfg = new Configuration();
    init();
    this.cfg.setServletContextForTemplateLoading(paramServletContext, paramString);
    return this.cfg;
  }

  private void init()
  {
    this.cfg.setEncoding(Locale.getDefault(), "UTF-8");
    try
    {
      this.cfg.setObjectWrapper(new DefaultObjectWrapper());
      this.cfg.setSetting("cache_storage", "strong:25,soft:300");
    }
    catch (TemplateException localTemplateException)
    {
      localTemplateException.printStackTrace();
    }
  }
}