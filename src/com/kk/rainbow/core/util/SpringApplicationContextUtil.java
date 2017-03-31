package com.kk.rainbow.core.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringApplicationContextUtil
  implements ApplicationContextAware
{
  private static ApplicationContext springApplicationContext;

  public void setApplicationContext(ApplicationContext paramApplicationContext)
    throws BeansException
  {
		springApplicationContext = paramApplicationContext;
  }

  public static Object getBean(String paramString)
    throws BeansException
  {
    return springApplicationContext.getBean(paramString);
  }

  public static boolean isContainBean(String paramString)
  {
    return springApplicationContext.containsBean(paramString);
  }
}