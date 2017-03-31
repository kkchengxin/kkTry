package com.kk.rainbow.core.springmvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.kk.rainbow.core.manager.Log;

public class LogInterceptor
  implements HandlerInterceptor
{
  ThreadLocal<StopWatch> threadLocal = new ThreadLocal();

  public void afterCompletion(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, Object paramObject, Exception paramException)
    throws Exception
  {
    Log.debug("====================after====");
    StopWatch localStopWatch = (StopWatch)this.threadLocal.get();
    if (localStopWatch != null)
    {
      localStopWatch.stop();
      StringBuffer localStringBuffer = new StringBuffer();
      Log.info(localStringBuffer.append("||").append(paramHttpServletRequest.getMethod()).append(" URL:").append(paramHttpServletRequest.getRequestURI()).append(" Times:").append(localStopWatch == null ? "null" : Long.valueOf(localStopWatch.getTotalTimeMillis())).append(System.getProperty("line.separator")).append("||Query:").append(paramHttpServletRequest.getQueryString()).toString());
      localStopWatch = null;
      this.threadLocal.set(null);
    }
  }

  public void postHandle(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, Object paramObject, ModelAndView paramModelAndView)
    throws Exception
  {
    Log.debug("====================post====");
  }

  public boolean preHandle(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, Object paramObject)
    throws Exception
  {
    Log.debug("====================pre====" + paramObject.getClass());
    StopWatch localStopWatch = new StopWatch(paramObject.getClass().getName());
    localStopWatch.start();
    this.threadLocal.set(localStopWatch);
    return true;
  }
}