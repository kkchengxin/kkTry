package com.kk.rainbow.core.springmvc;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.util.WebUtils;

public class RExceptionResolver extends AbstractHandlerExceptionResolver
{
  public static final String DEFAULT_EXCEPTION_ATTRIBUTE = "exception";
  private Properties exceptionMappings;
  private Class<?>[] excludedExceptions;
  private String defaultErrorView;
  private Integer defaultStatusCode;
  private Map<String, Integer> statusCodes = new HashMap();
  private String exceptionAttribute = "exception";

  public void setExceptionMappings(Properties paramProperties)
  {
    this.exceptionMappings = paramProperties;
  }

  public void setExcludedExceptions(Class<?>[] paramArrayOfClass)
  {
    this.excludedExceptions = paramArrayOfClass;
  }

  public void setDefaultErrorView(String paramString)
  {
    this.defaultErrorView = paramString;
  }

  public void setStatusCodes(Properties paramProperties)
  {
    Enumeration localEnumeration = paramProperties.propertyNames();
    while (localEnumeration.hasMoreElements())
    {
      String str = (String)localEnumeration.nextElement();
      Integer localInteger = new Integer(paramProperties.getProperty(str));
      this.statusCodes.put(str, localInteger);
    }
  }

  public void addStatusCode(String paramString, int paramInt)
  {
    this.statusCodes.put(paramString, Integer.valueOf(paramInt));
  }

  public Map<String, Integer> getStatusCodesAsMap()
  {
    return Collections.unmodifiableMap(this.statusCodes);
  }

  public void setDefaultStatusCode(int paramInt)
  {
    this.defaultStatusCode = Integer.valueOf(paramInt);
  }

  public void setExceptionAttribute(String paramString)
  {
    this.exceptionAttribute = paramString;
  }

  protected ModelAndView doResolveException(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, Object paramObject, Exception paramException)
  {
    String str = determineViewName(paramException, paramHttpServletRequest);
    if (str != null)
    {
      Integer localInteger = determineStatusCode(paramHttpServletRequest, str);
      if (localInteger != null)
        applyStatusCodeIfPossible(paramHttpServletRequest, paramHttpServletResponse, localInteger.intValue());
      return getModelAndView(str, paramException, paramHttpServletRequest);
    }
    return null;
  }

  protected String determineViewName(Exception paramException, HttpServletRequest paramHttpServletRequest)
  {
    String str = null;
    if (this.excludedExceptions != null)
      for (Class localClass : this.excludedExceptions)
        if (localClass.equals(paramException.getClass()))
          return null;
    if (this.exceptionMappings != null)
      str = findMatchingViewName(this.exceptionMappings, paramException);
    if ((str == null) && (this.defaultErrorView != null))
    {
      if (this.logger.isDebugEnabled())
        this.logger.debug("Resolving to default view '" + this.defaultErrorView + "' for exception of type [" + paramException.getClass().getName() + "]");
      str = this.defaultErrorView;
    }
    return str;
  }

  protected String findMatchingViewName(Properties paramProperties, Exception paramException)
  {
    String str1 = null;
    Object localObject = null;
    int i = 2147483647;
    Enumeration localEnumeration = paramProperties.propertyNames();
    while (localEnumeration.hasMoreElements())
    {
      String str2 = (String)localEnumeration.nextElement();
      int j = getDepth(str2, paramException);
      if ((j >= 0) && (j < i))
      {
        i = j;
        localObject = str2;
        str1 = paramProperties.getProperty(str2);
      }
    }
    if ((str1 != null) && (this.logger.isDebugEnabled()))
      this.logger.debug("Resolving to view '" + str1 + "' for exception of type [" + paramException.getClass().getName() + "], based on exception mapping [" + localObject + "]");
    return str1;
  }

  protected int getDepth(String paramString, Exception paramException)
  {
    return getDepth(paramString, paramException.getClass(), 0);
  }

  private int getDepth(String paramString, Class<?> paramClass, int paramInt)
  {
    if (paramClass.getName().contains(paramString))
      return paramInt;
    if (paramClass.equals(Throwable.class))
      return -1;
    return getDepth(paramString, paramClass.getSuperclass(), paramInt + 1);
  }

  protected Integer determineStatusCode(HttpServletRequest paramHttpServletRequest, String paramString)
  {
    if (this.statusCodes.containsKey(paramString))
      return (Integer)this.statusCodes.get(paramString);
    return this.defaultStatusCode;
  }

  protected void applyStatusCodeIfPossible(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, int paramInt)
  {
    if (!WebUtils.isIncludeRequest(paramHttpServletRequest))
    {
      if (this.logger.isDebugEnabled())
        this.logger.debug("Applying HTTP status code " + paramInt);
      paramHttpServletResponse.setStatus(paramInt);
      paramHttpServletRequest.setAttribute("javax.servlet.error.status_code", Integer.valueOf(paramInt));
    }
  }

  protected ModelAndView getModelAndView(String paramString, Exception paramException, HttpServletRequest paramHttpServletRequest)
  {
    return getModelAndView(paramString, paramException);
  }

  protected ModelAndView getModelAndView(String paramString, Exception paramException)
  {
    ModelAndView localModelAndView = new ModelAndView(paramString);
    if (this.exceptionAttribute != null)
    {
      if (this.logger.isDebugEnabled())
        this.logger.debug("Exposing Exception as model attribute '" + this.exceptionAttribute + "'");
      localModelAndView.addObject(this.exceptionAttribute, paramException);
    }
    return localModelAndView;
  }
}