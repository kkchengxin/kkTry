package com.kk.rainbow.core.json;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.kk.rainbow.core.exception.RException;

public class ResponseUtils
{
  public static final void responseAsJson(String paramString1, String paramString2, Object paramObject, HttpServletResponse paramHttpServletResponse)
  {
    try
    {
      paramHttpServletResponse.setContentType("text/html; charset=UTF-8");
      paramHttpServletResponse.setCharacterEncoding("UTF-8");
      StringBuffer localStringBuffer = new StringBuffer();
      localStringBuffer.append("{");
      localStringBuffer.append("\"").append(JSONKeys.CODE.getValue()).append("\":").append("\"").append(paramString1).append("\",");
      localStringBuffer.append("\"").append(JSONKeys.DESC.getValue()).append("\":").append("\"").append(paramString2).append("\"");
      localStringBuffer.append(",\"").append(JSONKeys.DATA.getValue()).append("\":").append(JSONUtils.toJsonAsString(paramObject));
      localStringBuffer.append("}");
      PrintWriter localPrintWriter = paramHttpServletResponse.getWriter();
      localPrintWriter.write(localStringBuffer.toString());
      localPrintWriter.flush();
      localPrintWriter.close();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    finally
    {
      paramObject = null;
    }
  }

  public static final void responseAsJson(String paramString1, String paramString2, HttpServletResponse paramHttpServletResponse)
  {
    try
    {
      paramHttpServletResponse.setContentType("text/html; charset=UTF-8");
      paramHttpServletResponse.setCharacterEncoding("UTF-8");
      StringBuffer localStringBuffer = new StringBuffer();
      localStringBuffer.append("{");
      localStringBuffer.append("\"").append(JSONKeys.CODE.getValue()).append("\":").append("\"").append(paramString1).append("\",");
      localStringBuffer.append("\"").append(JSONKeys.DESC.getValue()).append("\":").append("\"").append(paramString2).append("\"");
      localStringBuffer.append("}");
      PrintWriter localPrintWriter = paramHttpServletResponse.getWriter();
      localPrintWriter.write(localStringBuffer.toString());
      localPrintWriter.flush();
      localPrintWriter.close();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
  }

  public static final void responseAsJson(RException paramRException, HttpServletResponse paramHttpServletResponse)
  {
    try
    {
      paramHttpServletResponse.setContentType("text/html; charset=UTF-8");
      paramHttpServletResponse.setCharacterEncoding("UTF-8");
      PrintWriter localPrintWriter = paramHttpServletResponse.getWriter();
      localPrintWriter.write(RException.toJson(paramRException.getMessage()));
      localPrintWriter.flush();
      localPrintWriter.close();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    finally
    {
      paramRException = null;
    }
  }

  public static final void responseAsJson(Object paramObject, HttpServletResponse paramHttpServletResponse)
  {
    try
    {
      paramHttpServletResponse.setContentType("text/html; charset=UTF-8");
      paramHttpServletResponse.setCharacterEncoding("UTF-8");
      PrintWriter localPrintWriter = paramHttpServletResponse.getWriter();
      localPrintWriter.write(JSONUtils.toJsonAsString(paramObject));
      localPrintWriter.flush();
      localPrintWriter.close();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    finally
    {
      paramObject = null;
    }
  }

  public static final void responseAsJson(HttpServletResponse paramHttpServletResponse, Object paramObject, String[] paramArrayOfString)
  {
    try
    {
      paramHttpServletResponse.setContentType("text/html; charset=UTF-8");
      paramHttpServletResponse.setCharacterEncoding("UTF-8");
      PrintWriter localPrintWriter = paramHttpServletResponse.getWriter();
      localPrintWriter.write(JSONUtils.toJsonAsString(paramObject, paramArrayOfString));
      localPrintWriter.flush();
      localPrintWriter.close();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    finally
    {
      paramObject = null;
    }
  }

  public static final void responseAsJson(HttpServletResponse paramHttpServletResponse, Object paramObject, Set<String> paramSet)
  {
    try
    {
      paramHttpServletResponse.setContentType("text/html; charset=UTF-8");
      paramHttpServletResponse.setCharacterEncoding("UTF-8");
      PrintWriter localPrintWriter = paramHttpServletResponse.getWriter();
      localPrintWriter.write(JSONUtils.toJsonAsString(paramObject, paramSet));
      localPrintWriter.flush();
      localPrintWriter.close();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    finally
    {
      paramObject = null;
    }
  }
}