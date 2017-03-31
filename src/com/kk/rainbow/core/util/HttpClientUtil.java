package com.kk.rainbow.core.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.kk.rainbow.core.exception.RException;
import com.kk.rainbow.core.manager.Log;

public class HttpClientUtil
{
  private static String CONTENT_CHAR = "UTF-8";
  private OutputStream out;
  HttpResponse response = null;
  private static final String POST = "post";
  private static final String GET = "get";

  public HttpClientUtil()
  {
  }

  public HttpClientUtil(OutputStream paramOutputStream)
  {
    this.out = paramOutputStream;
  }

  /** @deprecated */
  public InputStream executeGetAsStream(String paramString)
  {
    return executeGetAsStream(paramString, CONTENT_CHAR);
  }

  public OutputStream executePostAsStream(String paramString, Map<String, String> paramMap)
    throws RException
  {
    return executePostAsStream(paramString, paramMap, CONTENT_CHAR);
  }

  /** @deprecated */
  public InputStream executeGetAsStream(String paramString1, String paramString2)
  {
    System.out.println("URL-Get==" + paramString1);
    if ((paramString1 == null) || (paramString1.isEmpty()))
      throw new NullPointerException("url  is must be required.");
    if ((paramString2 == null) || (paramString2.isEmpty()))
      CONTENT_CHAR = paramString2;
    InputStream localInputStream = null;
    HttpGet localHttpGet = new HttpGet(paramString1);
    DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
    try
    {
      HttpResponse localHttpResponse = localDefaultHttpClient.execute(localHttpGet);
      HttpEntity localHttpEntity = localHttpResponse.getEntity();
      if (localHttpEntity != null)
        localInputStream = localHttpEntity.getContent();
      localDefaultHttpClient.getConnectionManager().shutdown();
    }
    catch (ClientProtocolException localClientProtocolException)
    {
      localClientProtocolException.printStackTrace();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    catch (IllegalStateException localIllegalStateException)
    {
      localIllegalStateException.printStackTrace();
    }
    return localInputStream;
  }

  public boolean decompressZIP(String paramString1, Map<String, String> paramMap, String paramString2, String paramString3)
    throws RException
  {
    System.out.println("URL-Post-Stream==>" + paramString1);
    if ((paramString1 == null) || (paramString1.isEmpty()))
      throw new NullPointerException("url  is must be required.");
    if (paramString3 == null)
      throw new RException("-1", "Please appoint the target dir");
    boolean bool = false;
    Object localObject1 = null;
    HttpEntity localHttpEntity = null;
    try
    {
      localObject1 = new DefaultHttpClient();
      localObject1 = initParas((HttpClient)localObject1);
      HttpPost localHttpPost = new HttpPost(paramString1);
      if ((paramMap != null) && (!paramMap.isEmpty()))
        localHttpPost.setEntity(new UrlEncodedFormEntity(assemParameter(paramMap), "UTF-8"));
      this.response = ((HttpClient)localObject1).execute(localHttpPost);
      System.out.println("status====>" + this.response.getStatusLine() + ":" + this.response.getStatusLine().getStatusCode());
      if (this.response.getStatusLine().getStatusCode() != 200)
        throw new RException(String.valueOf(this.response.getStatusLine().getStatusCode()), "The request resource is not exist. or The remoting service is Not Available");
      localHttpEntity = this.response.getEntity();
      if (localHttpEntity != null)
      {
        System.out.println("HttpResponse content-length==" + localHttpEntity.getContentLength());
        bool = doDecompressZIP(localHttpEntity.getContent(), paramString2, paramString3);
      }
      localHttpEntity.consumeContent();
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      localUnsupportedEncodingException.printStackTrace();
      throw new RException("-1", localUnsupportedEncodingException.getLocalizedMessage());
    }
    catch (ClientProtocolException localClientProtocolException)
    {
      localClientProtocolException.printStackTrace();
      throw new RException("-1", localClientProtocolException.getLocalizedMessage());
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
      throw new RException("-100", localIOException.getLocalizedMessage());
    }
    catch (IllegalStateException localIllegalStateException)
    {
      localIllegalStateException.printStackTrace();
      throw new RException("-1", localIllegalStateException.getLocalizedMessage());
    }
    finally
    {
      ((HttpClient)localObject1).getConnectionManager().shutdown();
    }
    return bool;
  }

  private boolean doDecompressZIP(InputStream paramInputStream, String paramString1, String paramString2)
    throws RException
  {
    boolean bool = false;
    if (paramString1 == null)
      paramString1 = "zip";
    try
    {
      ArchiveInputStream localArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(paramString1, new GZIPInputStream(paramInputStream));
      ArchiveEntry localArchiveEntry;
      while ((localArchiveEntry = localArchiveInputStream.getNextEntry()) != null)
      {
        File localFile = new File(paramString2, localArchiveEntry.getName());
        if (localArchiveEntry.isDirectory())
        {
          if (!localFile.exists())
            localFile.mkdirs();
        }
        else
        {
          System.out.println("localhost file len=>" + localFile.length());
          System.out.println("remote file len=>" + localArchiveEntry.getSize());
          if ((!localFile.exists()) || (localArchiveEntry.getSize() != localFile.length()))
          {
            FileOutputStream localFileOutputStream = new FileOutputStream(localFile);
            IOUtils.copy(localArchiveInputStream, localFileOutputStream);
            localFileOutputStream.flush();
            localFileOutputStream.close();
          }
        }
      }
      if (localArchiveInputStream != null)
        localArchiveInputStream.close();
      bool = true;
    }
    catch (ArchiveException localArchiveException)
    {
      localArchiveException.printStackTrace();
      throw new RException("-1", "The source resource is not ZIP file");
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
      throw new RException("-1", "Thre source stream is not varaiable");
    }
    return bool;
  }

  public OutputStream executePostAsStream(String paramString1, Map<String, String> paramMap, String paramString2)
    throws RException
  {
    System.out.println("URL-Post-Stream==>" + paramString1);
    if ((paramString1 == null) || (paramString1.isEmpty()))
      throw new NullPointerException("url  is must be required.");
    if ((paramString2 == null) || (paramString2.isEmpty()))
      CONTENT_CHAR = paramString2;
    Object localObject1 = null;
    HttpEntity localHttpEntity = null;
    try
    {
      localObject1 = new DefaultHttpClient();
      localObject1 = initParas((HttpClient)localObject1);
      HttpPost localHttpPost = new HttpPost(paramString1);
      if ((paramMap != null) && (!paramMap.isEmpty()))
        localHttpPost.setEntity(new UrlEncodedFormEntity(assemParameter(paramMap), "UTF-8"));
      this.response = ((HttpClient)localObject1).execute(localHttpPost);
      System.out.println("status====>" + this.response.getStatusLine() + ":" + this.response.getStatusLine().getStatusCode());
      if (this.response.getStatusLine().getStatusCode() != 200)
      {
        if (this.response.getStatusLine().getStatusCode() == 500)
          throw new RException(String.valueOf(this.response.getStatusLine().getStatusCode()), "The remoting service is Not Available. URL:" + paramString1);
        throw new RException(String.valueOf(this.response.getStatusLine().getStatusCode()), "The request resource is Not exist. URL:" + paramString1);
      }
      localHttpEntity = this.response.getEntity();
      if (localHttpEntity != null)
      {
        System.out.println("HttpResponse content-length==" + localHttpEntity.getContentLength());
        this.out = writeToOutputStream(localHttpEntity.getContent());
      }
      localHttpEntity.consumeContent();
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      localUnsupportedEncodingException.printStackTrace();
      throw new RException("-1", localUnsupportedEncodingException.getLocalizedMessage());
    }
    catch (ClientProtocolException localClientProtocolException)
    {
      localClientProtocolException.printStackTrace();
      throw new RException("-1", localClientProtocolException.getLocalizedMessage());
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
      throw new RException("-100", localIOException.getLocalizedMessage());
    }
    catch (IllegalStateException localIllegalStateException)
    {
      localIllegalStateException.printStackTrace();
      throw new RException("-1", localIllegalStateException.getLocalizedMessage());
    }
    finally
    {
      ((HttpClient)localObject1).getConnectionManager().shutdown();
    }
    return this.out;
  }

  public Document executePostAsDocument(String paramString1, Map<String, String> paramMap, String paramString2)
    throws RException
  {
    System.out.println("URL-Post-Stream==>" + paramString1);
    if ((paramString1 == null) || (paramString1.isEmpty()))
      throw new NullPointerException("url  is must be required.");
    if ((paramString2 == null) || (paramString2.isEmpty()))
      CONTENT_CHAR = paramString2;
    SAXReader localSAXReader = null;
    Document localDocument = null;
    Object localObject1 = null;
    HttpEntity localHttpEntity = null;
    try
    {
      localObject1 = new DefaultHttpClient();
      localObject1 = initParas((HttpClient)localObject1);
      HttpPost localHttpPost = new HttpPost(paramString1);
      if ((paramMap != null) && (!paramMap.isEmpty()))
        localHttpPost.setEntity(new UrlEncodedFormEntity(assemParameter(paramMap), "UTF-8"));
      this.response = ((HttpClient)localObject1).execute(localHttpPost);
      System.out.println("status====>" + this.response.getStatusLine() + ":" + this.response.getStatusLine().getStatusCode());
      if (this.response.getStatusLine().getStatusCode() != 200)
      {
        if (this.response.getStatusLine().getStatusCode() == 500)
          throw new RException(String.valueOf(this.response.getStatusLine().getStatusCode()), "The remoting service is Not Available. URL:" + paramString1);
        throw new RException(String.valueOf(this.response.getStatusLine().getStatusCode()), "The request resource is Not exist. URL:" + paramString1);
      }
      localHttpEntity = this.response.getEntity();
      if (localHttpEntity != null)
      {
        System.out.println("HttpResponse content-length==" + localHttpEntity.getContentLength());
        localSAXReader = new SAXReader();
        localDocument = localSAXReader.read(localHttpEntity.getContent());
      }
      localHttpEntity.consumeContent();
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      localUnsupportedEncodingException.printStackTrace();
      throw new RException("-1", localUnsupportedEncodingException.getLocalizedMessage());
    }
    catch (ClientProtocolException localClientProtocolException)
    {
      localClientProtocolException.printStackTrace();
      throw new RException("-1", localClientProtocolException.getLocalizedMessage());
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
      throw new RException("-100", localIOException.getLocalizedMessage());
    }
    catch (IllegalStateException localIllegalStateException)
    {
      localIllegalStateException.printStackTrace();
      throw new RException("-1", localIllegalStateException.getLocalizedMessage());
    }
    catch (DocumentException localDocumentException)
    {
      localDocumentException.printStackTrace();
      throw new RException("-1", localDocumentException.getLocalizedMessage());
    }
    finally
    {
      ((HttpClient)localObject1).getConnectionManager().shutdown();
    }
    return localDocument;
  }

  private List<BasicNameValuePair> assemParameter(Map<String, String> paramMap)
  {
    if ((paramMap == null) || (paramMap.isEmpty()))
      return null;
    ArrayList localArrayList = null;
    if ((paramMap != null) && (!paramMap.isEmpty()))
    {
      localArrayList = new ArrayList();
      Iterator localIterator = paramMap.keySet().iterator();
      StringBuffer localStringBuffer = new StringBuffer();
      while (localIterator.hasNext())
      {
        String str1 = (String)localIterator.next();
        String str2 = (String)paramMap.get(str1);
        localArrayList.add(new BasicNameValuePair(str1, str2));
        localStringBuffer.append(str1).append("=").append(str2).append(";");
      }
      Log.debug("Post Paras==>" + localStringBuffer.toString());
    }
    return localArrayList;
  }

  private HttpClient initParas(HttpClient paramHttpClient)
  {
    paramHttpClient.getParams().setParameter("http.protocol.content-charset", "UTF-8");
    HttpParams localHttpParams = paramHttpClient.getParams();
    HttpProtocolParams.setContentCharset(localHttpParams, "UTF-8");
    HttpProtocolParams.setHttpElementCharset(localHttpParams, "UTF-8");
    HttpConnectionParams.setConnectionTimeout(localHttpParams, 20000);
    HttpConnectionParams.setSocketBufferSize(localHttpParams, 102400);
    HttpConnectionParams.setSoTimeout(localHttpParams, 20000);
    return paramHttpClient;
  }

  private OutputStream writeToOutputStream(InputStream paramInputStream)
    throws RException
  {
    if (paramInputStream == null)
      throw new RException("-1", "输入流为空");
    if (this.out == null)
      throw new RException("-1", "输出流为空");
    BufferedInputStream localBufferedInputStream = new BufferedInputStream(paramInputStream);
    byte[] arrayOfByte = new byte[2048];
    try
    {
      int i;
      while ((i = localBufferedInputStream.read(arrayOfByte, 0, arrayOfByte.length)) > 0)
        this.out.write(arrayOfByte, 0, i);
      paramInputStream.close();
    }
    catch (IOException localIOException)
    {
      System.out.println("已取消连接");
    }
    return this.out;
  }

  /** @deprecated */
  public String executeGetAsString(String paramString)
  {
    return executeRequestAsString(paramString, null, "get", CONTENT_CHAR);
  }

  public String executePostAsString(String paramString, Map<String, String> paramMap)
  {
    return executeRequestAsString(paramString, paramMap, "post", CONTENT_CHAR);
  }

  /** @deprecated */
  public String executeGetAsString(String paramString1, String paramString2)
  {
    return executeRequestAsString(paramString1, null, "get", paramString2);
  }

  public String executePostAsString(String paramString1, Map<String, String> paramMap, String paramString2)
  {
    return executeRequestAsString(paramString1, paramMap, "post", paramString2);
  }

  private String executeRequestAsString(String paramString1, Map<String, String> paramMap, String paramString2, String paramString3)
  {
    System.out.println("URL-" + paramString2 + "==" + paramString1);
    if ((paramString1 == null) || (paramString1.isEmpty()))
      throw new NullPointerException("url  is must be required.");
    if ((paramString3 == null) || (paramString3.isEmpty()))
      CONTENT_CHAR = paramString3;
    if ((paramString2 == null) || (paramString2.isEmpty()))
      paramString2 = "post";
    String str1 = null;
    HttpGet localHttpGet = null;
    HttpPost localHttpPost = null;
    int i = 0;
    try
    {
      if ("get".equalsIgnoreCase(paramString2.trim()))
      {
        localHttpGet = new HttpGet(paramString1);
        i = 1;
      }
      else if ("post".equalsIgnoreCase(paramString2.trim()))
      {
        localHttpPost = new HttpPost(paramString1);
        List localObject1 = null;
        if ((paramMap != null) && (!paramMap.isEmpty()))
        {
          localObject1 = new ArrayList();
          Iterator<String> localObject2 = paramMap.keySet().iterator();
          StringBuffer localStringBuffer = new StringBuffer();
          while (((Iterator)localObject2).hasNext())
          {
            String str2 = (String)((Iterator)localObject2).next();
            String str3 = (String)paramMap.get(str2);
            ((List)localObject1).add(new BasicNameValuePair(str2, str3));
            localStringBuffer.append(str2).append("=").append(str3).append(";");
          }
          Log.debug(paramString2 + " Paras==>" + localStringBuffer.toString());
          localHttpPost.setEntity(new UrlEncodedFormEntity((List)localObject1, "UTF-8"));
        }
        i = 0;
      }
      Object localObject1 = new DefaultHttpClient();
      Object localObject2 = null;
      if (i != 0)
        this.response = ((HttpClient)localObject1).execute(localHttpGet);
      else
        this.response = ((HttpClient)localObject1).execute(localHttpPost);
      System.out.println(this.response.getStatusLine());
      localObject2 = this.response.getEntity();
      if (localObject2 != null)
      {
        str1 = EntityUtils.toString((HttpEntity)localObject2);
        ((HttpEntity)localObject2).consumeContent();
        ((HttpClient)localObject1).getConnectionManager().shutdown();
      }
    }
    catch (ClientProtocolException localClientProtocolException)
    {
      localClientProtocolException.printStackTrace();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return str1;
  }

  public String getFirstHeader(String paramString)
    throws RException
  {
    if ((paramString == null) || (paramString.isEmpty()))
      throw new RException("-1", "请指定头信息");
    return this.response == null ? null : this.response.getFirstHeader(paramString).getValue();
  }
}