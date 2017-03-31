package com.kk.rainbow.core.attach;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.kk.rainbow.core.exception.RException;

public final class Attachment
{
  private static String end = "\r\n";
  private static String twoHyphens = "--";
  private static String boundary = "*****";

  public static boolean uploadMulti(String[] paramArrayOfString, String paramString1, String paramString2)
    throws RException
  {
    try
    {
      URL localURL = new URL(paramString1);
      HttpURLConnection localHttpURLConnection = (HttpURLConnection)localURL.openConnection();
      localHttpURLConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0)");
      localHttpURLConnection.setDoInput(true);
      localHttpURLConnection.setDoOutput(true);
      localHttpURLConnection.setUseCaches(false);
      localHttpURLConnection.setRequestMethod("POST");
      localHttpURLConnection.setRequestProperty("Connection", "Keep-Alive");
      localHttpURLConnection.setRequestProperty("Charset", "UTF-8");
      localHttpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
      localHttpURLConnection.connect();
      DataOutputStream localDataOutputStream = new DataOutputStream(localHttpURLConnection.getOutputStream());
      addParam("upath", paramString2, localDataOutputStream);
      for (int i = 0; i < paramArrayOfString.length; i++)
        if (!isExist(paramArrayOfString[i]))
          System.out.println("*****File is not exist." + paramArrayOfString[i]);
        else
          addAttachment(paramArrayOfString[i], localDataOutputStream);
      localDataOutputStream.writeBytes(twoHyphens + boundary + end);
      localDataOutputStream.flush();
      localDataOutputStream.close();
      InputStream localInputStream = localHttpURLConnection.getInputStream();
      BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream));
      for (String str = localBufferedReader.readLine(); (str != null) && (str != ""); str = localBufferedReader.readLine());
      localBufferedReader.close();
      localInputStream.close();
      System.out.println("success");
      return true;
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      localFileNotFoundException.printStackTrace();
    }
    catch (MalformedURLException localMalformedURLException)
    {
      localMalformedURLException.printStackTrace();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return false;
  }

  private static void addAttachment(String paramString, DataOutputStream paramDataOutputStream)
    throws IOException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    paramDataOutputStream.writeBytes(twoHyphens + boundary + end);
    localStringBuffer.append("Content-Disposition: form-data; ");
    localStringBuffer.append("name=\"file_" + Math.random() * 10.0D + "\"; filename=\"");
    localStringBuffer.append(paramString.substring(paramString.lastIndexOf("\\") + 1) + "\"");
    localStringBuffer.append(end);
    localStringBuffer.append("Content-Type: application/octet-stream");
    localStringBuffer.append(end);
    localStringBuffer.append(end);
    paramDataOutputStream.writeBytes(localStringBuffer.toString());
    FileInputStream localFileInputStream = new FileInputStream(paramString);
    int i = 1024;
    byte[] arrayOfByte = new byte[i];
    int j = -1;
    while ((j = localFileInputStream.read(arrayOfByte)) != -1)
      paramDataOutputStream.write(arrayOfByte, 0, j);
    localFileInputStream.close();
    paramDataOutputStream.writeBytes(end);
  }

  private static void addParam(String paramString1, String paramString2, DataOutputStream paramDataOutputStream)
    throws IOException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    paramDataOutputStream.writeBytes(twoHyphens + boundary + end);
    localStringBuffer.append("Content-Disposition: form-data; ");
    localStringBuffer.append("name=\"" + paramString1 + "\"");
    localStringBuffer.append(end);
    localStringBuffer.append(end);
    localStringBuffer.append(paramString2);
    paramDataOutputStream.writeBytes(localStringBuffer.toString());
    paramDataOutputStream.writeBytes(end);
  }

  private static boolean isExist(String paramString)
  {
    if (paramString == null)
      return false;
    File localFile = new File(paramString);
    return (localFile.isFile()) && (localFile.exists());
  }

  public static void main(String[] paramArrayOfString)
    throws RException
  {
    String[] arrayOfString = { "D:/a.xls", "d:/a.sql" };
    uploadMulti(arrayOfString, null, null);
  }
}