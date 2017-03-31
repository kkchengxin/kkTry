package com.kk.rainbow.core.attach;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.kk.rainbow.core.exception.RException;
import com.kk.rainbow.core.util.StringUtil;
import com.oreilly.servlet.MultipartRequest;

public class UploadUtils
{
  private static int MAX_SIZE = 10485760;

  public static final File upload(HttpServletRequest paramHttpServletRequest, String paramString)
    throws RException
  {
    return uploadCore(paramHttpServletRequest, paramString, null, 100);
  }

  public static final File upload(HttpServletRequest paramHttpServletRequest, String paramString, int paramInt)
    throws RException
  {
    return uploadCore(paramHttpServletRequest, paramString, null, paramInt);
  }

  public static final File upload(HttpServletRequest paramHttpServletRequest, String paramString1, String paramString2)
    throws RException
  {
    return uploadCore(paramHttpServletRequest, paramString1, paramString2, MAX_SIZE);
  }

  public static final File upload(HttpServletRequest paramHttpServletRequest, String paramString1, String paramString2, int paramInt)
    throws RException
  {
    return uploadCore(paramHttpServletRequest, paramString1, paramString2, paramInt);
  }

  private static File uploadCore(HttpServletRequest paramHttpServletRequest, String paramString1, String paramString2, int paramInt)
    throws RException
  {
    if (paramHttpServletRequest == null)
      throw new RException("||Upload file is Null.");
    if (StringUtil.isEmpty(paramString1))
      throw new RException("||Save dir is Null.");
    if (paramInt <= 0)
      throw new RException("||maxSize is invalid.");
    File localFile1 = null;
    try
    {
      File localFile2 = new File(paramString1);
      System.out.println(localFile2.isDirectory() + "------------" + localFile2.exists());
      if (!localFile2.exists())
      {
        System.out.println("create dir");
        boolean bool = localFile2.mkdirs();
        System.out.println("create dir :" + bool);
      }
      MultipartRequest localMultipartRequest = null;
      if (StringUtil.isEmpty(paramString2))
        localMultipartRequest = new MultipartRequest(paramHttpServletRequest, paramString1, paramInt, "UTF-8");
      else
        localMultipartRequest = new MultipartRequest(paramHttpServletRequest, paramString1, paramInt, "UTF-8", new FileNamePolicy(paramString1, paramString2));
      Enumeration localEnumeration = localMultipartRequest.getFileNames();
      if (localEnumeration.hasMoreElements())
        localFile1 = localMultipartRequest.getFile((String)localEnumeration.nextElement());
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return localFile1;
  }
}