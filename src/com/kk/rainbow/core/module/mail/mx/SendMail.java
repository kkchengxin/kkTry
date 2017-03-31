package com.kk.rainbow.core.module.mail.mx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;

import com.kk.rainbow.core.module.mail.ISendMail;
import com.kk.rainbow.core.util.StringUtil;

public final class SendMail
  implements ISendMail
{
  public static final boolean SUCCESSFUL = true;
  public static final boolean FAILED = false;
  private static final int PORT = 25;
  private static final int RETRY = 3;
  private static final int INTERVAL = 1000;
  private String smtp;
  private String user;
  private String password;
  private static final int TIMEOUT = 10000;
  private static final String BOUNDARY = "Boundary-=_hMbeqwnGNoWeLsRMeKTIPeofyStu";
  private static final String CHARSET = Charset.defaultCharset().displayName();
  private static final Pattern PATTERN = Pattern.compile(".+@[^.@]+(\\.[^.@]+)+$");
  private static InitialDirContext dirContext;
  private final ArrayList<LogManager> logManager;
  private boolean isEsmtp;
  private String sender;
  private String senderAddress;
  private int ATTBUFFER = 60;

  private SendMail(String paramString)
  {
    if (paramString == null)
      throw new IllegalArgumentException("参数from不能为null。");
    int i = (paramString = paramString.trim()).charAt(paramString.length() - 1) == '>' ? paramString.lastIndexOf(60) : -1;
    this.senderAddress = (i > -1 ? paramString.substring(i + 1, paramString.length() - 1).trim() : paramString);
    if (!PATTERN.matcher(this.senderAddress).find())
      throw new IllegalArgumentException("参数from不正确。");
    this.sender = (i > -1 ? paramString.substring(0, i).trim() : null);
    this.logManager = new ArrayList();
    this.isEsmtp = false;
    if (this.sender != null)
      if (this.sender.length() == 0)
        this.sender = null;
      else if ((this.sender.charAt(0) == '"') && (this.sender.charAt(this.sender.length() - 1) == '"'))
        this.sender = this.sender.substring(1, this.sender.length() - 1).trim();
  }

  private SendMail(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    this(paramString2);
    this.isEsmtp = true;
    this.smtp = paramString1;
    this.user = StringUtil.Base64.encode(paramString3.getBytes());
    this.password = StringUtil.Base64.encode(paramString4.getBytes());
  }

  public static SendMail getInstance(String paramString)
    throws IllegalArgumentException
  {
    return new SendMail(paramString);
  }

  public static SendMail getInstance(String paramString1, String paramString2, String paramString3, String paramString4)
    throws IllegalArgumentException
  {
    return new SendMail(paramString1, paramString2, paramString3, paramString4);
  }

  public boolean sendMail(String paramString1, String paramString2, String paramString3, String[] paramArrayOfString1, String[] paramArrayOfString2, Object paramObject, boolean paramBoolean1, boolean paramBoolean2)
    throws IllegalArgumentException
  {
    if (paramString3 == null)
      throw new IllegalArgumentException("参数to不能为null。");
    if (!PATTERN.matcher(paramString3).find())
      throw new IllegalArgumentException("参数to不正确。");
    int i = paramObject == null ? 0 : 1;
    Socket localSocket = null;
    InputStream localInputStream = null;
    OutputStream localOutputStream = null;
    try
    {
      if (this.isEsmtp)
      {
        int j = 1;
        while (true)
          try
          {
            log(new StringBuilder().append("连接: 主机:\"").append(this.smtp).append("\" 端口:\"").append(25).append("\"").toString());
            localSocket = new Socket(this.smtp, 25);
          }
          catch (IOException localIOException2)
          {
            log(new StringBuilder().append("错误: 连接失败").append(j).append("次").toString());
            if (j == 3)
            {
              boolean bool3 = false;
              return bool3;
            }
            try
            {
              Thread.sleep(1000L);
            }
            catch (InterruptedException localInterruptedException)
            {
            }
            j++;
          }
        localInputStream = localSocket.getInputStream();
        localOutputStream = localSocket.getOutputStream();
        if (response(localInputStream) != 220)
        {
          j = 0;
          return j;
        }
      }
      else
      {
        log("状态: 创建邮件接收服务器列表");
        String[] arrayOfString = parseDomain(parseUrl(paramString3));
        if (arrayOfString == null)
        {
          n = 0;
          return n;
        }
        int n = 0;
        while (n < arrayOfString.length)
          try
          {
            log(new StringBuilder().append("连接: 主机:\"").append(arrayOfString[n]).append("\" 端口:\"").append(25).append("\"").toString());
            localSocket = new Socket(arrayOfString[n], 25);
            localInputStream = localSocket.getInputStream();
            localOutputStream = localSocket.getOutputStream();
            if (response(localInputStream) != 220)
            {
              boolean bool4 = false;
              return bool4;
            }
          }
          catch (IOException localIOException42)
          {
            log("错误: 连接失败");
            n++;
          }
      }
      if ((localInputStream == null) || (localOutputStream == null))
      {
        k = 0;
        return k;
      }
      localSocket.setSoTimeout(10000);
      sendString(new StringBuilder().append("HELO ").append(parseUrl(this.senderAddress)).toString(), localOutputStream);
      sendNewline(localOutputStream);
      if (response(localInputStream) != 250)
      {
        k = 0;
        return k;
      }
      if (this.isEsmtp)
      {
        sendString("AUTH LOGIN", localOutputStream);
        sendNewline(localOutputStream);
        if (response(localInputStream) != 334)
        {
          k = 0;
          return k;
        }
        sendString(this.user, localOutputStream);
        sendNewline(localOutputStream);
        if (response(localInputStream) != 334)
        {
          k = 0;
          return k;
        }
        sendString(this.password, localOutputStream);
        sendNewline(localOutputStream);
        if (response(localInputStream) != 235)
        {
          k = 0;
          return k;
        }
      }
      sendString(new StringBuilder().append("MAIL FROM: <").append(this.senderAddress).append(">").toString(), localOutputStream);
      sendNewline(localOutputStream);
      if (response(localInputStream) != 250)
      {
        k = 0;
        return k;
      }
      sendString(new StringBuilder().append("RCPT TO: <").append(paramString3).append(">").toString(), localOutputStream);
      sendNewline(localOutputStream);
      if (response(localInputStream) != 250)
      {
        k = 0;
        return k;
      }
      sendString("DATA", localOutputStream);
      sendNewline(localOutputStream);
      if (response(localInputStream) != 354)
      {
        k = 0;
        return k;
      }
      sendString(new StringBuilder().append("From: ").append(this.sender == null ? this.senderAddress : new StringBuilder().append(getBase64String(this.sender)).append(" <").append(this.senderAddress).append(">").toString()).toString(), localOutputStream);
      sendNewline(localOutputStream);
      sendString(new StringBuilder().append("To: <").append(paramString3).append(">").toString(), localOutputStream);
      sendNewline(localOutputStream);
      sendString(new StringBuilder().append("Subject: ").append(getBase64String(paramString1)).toString(), localOutputStream);
      sendNewline(localOutputStream);
      sendString(new StringBuilder().append("Date: ").append(new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z (z)", Locale.US).format(new Date())).toString(), localOutputStream);
      sendNewline(localOutputStream);
      sendString("MIME-Version: 1.0", localOutputStream);
      sendNewline(localOutputStream);
      if (i != 0)
      {
        sendString(new StringBuilder().append("Content-Type: multipart/mixed; BOUNDARY=\"").append(BOUNDARY).append("\"").toString(), localOutputStream);
        sendNewline(localOutputStream);
      }
      else if (paramBoolean1)
      {
        sendString(new StringBuilder().append("Content-Type: text/html; charset=\"").append(CHARSET).append("\"").toString(), localOutputStream);
        sendNewline(localOutputStream);
      }
      else
      {
        sendString(new StringBuilder().append("Content-Type: text/plain; charset=\"").append(CHARSET).append("\"").toString(), localOutputStream);
        sendNewline(localOutputStream);
      }
      sendString("Content-Transfer-Encoding: base64", localOutputStream);
      sendNewline(localOutputStream);
      if (paramBoolean2)
      {
        sendString("X-Priority: 1", localOutputStream);
        sendNewline(localOutputStream);
      }
      else
      {
        sendString("X-Priority: 3", localOutputStream);
        sendNewline(localOutputStream);
      }
      sendString("X-Mailer: BlackFox Mail[Copyright(C) 2010 Sol]", localOutputStream);
      sendNewline(localOutputStream);
      log("发送: ");
      sendNewline(localOutputStream);
      if (i != 0)
      {
        sendString(new StringBuilder().append("--").append(BOUNDARY).toString(), localOutputStream);
        sendNewline(localOutputStream);
        if (paramBoolean1)
        {
          sendString(new StringBuilder().append("Content-Type: text/html; charset=\"").append(CHARSET).append("\"").toString(), localOutputStream);
          sendNewline(localOutputStream);
        }
        else
        {
          sendString(new StringBuilder().append("Content-Type: text/plain; charset=\"").append(CHARSET).append("\"").toString(), localOutputStream);
          sendNewline(localOutputStream);
        }
        sendString("Content-Transfer-Encoding: base64", localOutputStream);
        sendNewline(localOutputStream);
        log("发送: ");
        sendNewline(localOutputStream);
      }
      byte[] arrayOfByte = (paramString2 != null ? paramString2 : "").getBytes();
      for (int k = 0; k < arrayOfByte.length; k += 54)
      {
        log(String.valueOf(k));
        sendString(StringUtil.Base64.encode(arrayOfByte, k, Math.min(arrayOfByte.length - k, 54)), localOutputStream);
        log(String.valueOf(new StringBuilder().append("-----").append(k).toString()));
        sendNewline(localOutputStream);
      }
      int m;
      if (i != 0)
      {
        m = 0;
        File[] arrayOfFile = null;
        File localFile = null;
        Map localMap = null;
        RandomAccessFile localRandomAccessFile = null;
        try
        {
          if ((paramObject instanceof File[]))
          {
            arrayOfFile = (File[])paramObject;
            while (m < arrayOfFile.length)
            {
              localFile = arrayOfFile[m];
              m++;
            }
          }
          if ((paramObject instanceof Map))
          {
            localMap = (Map)paramObject;
            Iterator localIterator = localMap.keySet().iterator();
            while (localIterator.hasNext())
              localFile = (File)localIterator.next();
          }
          localRandomAccessFile = new RandomAccessFile(localFile, "r");
          sendAttach(localOutputStream, localRandomAccessFile, localFile.getName(), arrayOfByte);
        }
        catch (FileNotFoundException localFileNotFoundException)
        {
          log(new StringBuilder().append("错误: 附件\"").append(localFile.getAbsolutePath()).append("\"不存在").toString());
          bool5 = false;
          return bool5;
        }
        catch (IOException localIOException59)
        {
          log(new StringBuilder().append("错误: 无法读取附件\"").append(localFile.getAbsolutePath()).append("\"").toString());
          boolean bool5 = false;
          return bool5;
        }
        finally
        {
          if (localRandomAccessFile != null)
            try
            {
              localRandomAccessFile.close();
            }
            catch (IOException localIOException68)
            {
            }
        }
        sendString(new StringBuilder().append("--").append(BOUNDARY).append("--").toString(), localOutputStream);
        sendNewline(localOutputStream);
      }
      sendString(".", localOutputStream);
      sendNewline(localOutputStream);
      if (response(localInputStream) != 250)
      {
        m = 0;
        return m;
      }
      sendString("QUIT", localOutputStream);
      sendNewline(localOutputStream);
      if (response(localInputStream) != 221)
      {
        bool1 = false;
        return bool1;
      }
      boolean bool1 = true;
      return bool1;
    }
    catch (SocketTimeoutException localSocketTimeoutException)
    {
      log("错误: 连接超时");
      bool2 = false;
      return bool2;
    }
    catch (IOException localIOException1)
    {
      log("错误: 连接出错");
      bool2 = false;
      return bool2;
    }
    catch (Exception localException)
    {
      log(new StringBuilder().append("错误: ").append(localException.toString()).toString());
      boolean bool2 = false;
      return bool2;
    }
    finally
    {
      if (localInputStream != null)
        try
        {
          localInputStream.close();
        }
        catch (IOException localIOException69)
        {
        }
      if (localOutputStream != null)
        try
        {
          localOutputStream.close();
        }
        catch (IOException localIOException70)
        {
        }
      if (localSocket != null)
        try
        {
          localSocket.close();
        }
        catch (IOException localIOException71)
        {
        }
    }
  }

  private void sendAttach(OutputStream paramOutputStream, RandomAccessFile paramRandomAccessFile, String paramString, byte[] paramArrayOfByte)
    throws IOException
  {
    paramArrayOfByte = new byte[this.ATTBUFFER];
    sendString(new StringBuilder().append("--").append(BOUNDARY).toString(), paramOutputStream);
    sendNewline(paramOutputStream);
    sendString(new StringBuilder().append("Content-Type: ").append(MimeTypes.getMimeType(paramString.indexOf(".") == -1 ? "*" : paramString.substring(paramString.lastIndexOf(".") + 1))).append("; name=\"").append(paramString = getBase64String(paramString)).append("\"").toString(), paramOutputStream);
    sendNewline(paramOutputStream);
    sendString("Content-Transfer-Encoding: base64", paramOutputStream);
    sendNewline(paramOutputStream);
    sendString(new StringBuilder().append("Content-Disposition: attachment; filename=\"").append(paramString).append("\"").toString(), paramOutputStream);
    sendNewline(paramOutputStream);
    log("发送: ");
    sendNewline(paramOutputStream);
    int i;
    do
    {
      i = paramRandomAccessFile.read(paramArrayOfByte, 0, this.ATTBUFFER);
      if (i == -1)
        break;
      sendString(StringUtil.Base64.encode(paramArrayOfByte, 0, i), paramOutputStream);
      sendNewline(paramOutputStream);
    }
    while (i == 54);
  }

  public boolean[] sendMail(String paramString1, String paramString2, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, Object paramObject, boolean paramBoolean1, boolean paramBoolean2)
    throws IllegalArgumentException
  {
    boolean[] arrayOfBoolean = new boolean[paramArrayOfString1.length];
    for (int i = 0; i < arrayOfBoolean.length; i++)
      arrayOfBoolean[i] = sendMail(paramString1, paramString2, paramArrayOfString1[i], paramArrayOfString2, paramArrayOfString3, paramObject, paramBoolean1, paramBoolean2);
    return arrayOfBoolean;
  }

  public boolean sendText(String paramString1, String paramString2, String paramString3, String[] paramArrayOfString1, String[] paramArrayOfString2, File[] paramArrayOfFile)
    throws IllegalArgumentException
  {
    return sendMail(paramString1, paramString2, paramString3, null, null, paramArrayOfFile, false, false);
  }

  public boolean sendText(String paramString1, String paramString2, String paramString3, String[] paramArrayOfString1, String[] paramArrayOfString2, Map paramMap)
    throws IllegalArgumentException
  {
    return sendMail(paramString1, paramString2, paramString3, null, null, paramMap, false, false);
  }

  public boolean[] sendText(String paramString1, String paramString2, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, File[] paramArrayOfFile)
    throws IllegalArgumentException
  {
    return sendMail(paramString1, paramString2, paramArrayOfString1, paramArrayOfString2, paramArrayOfString3, paramArrayOfFile, false, false);
  }

  public boolean[] sendText(String paramString1, String paramString2, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, Map paramMap)
    throws IllegalArgumentException
  {
    return sendMail(paramString1, paramString2, paramArrayOfString1, paramArrayOfString2, paramArrayOfString3, paramMap, false, false);
  }

  public boolean sendHtml(String paramString1, String paramString2, String paramString3, String[] paramArrayOfString1, String[] paramArrayOfString2, File[] paramArrayOfFile)
    throws IllegalArgumentException
  {
    return sendMail(paramString1, paramString2, paramString3, paramArrayOfString1, paramArrayOfString2, paramArrayOfFile, true, false);
  }

  public boolean sendHtml(String paramString1, String paramString2, String paramString3, String[] paramArrayOfString1, String[] paramArrayOfString2, Map paramMap)
    throws IllegalArgumentException
  {
    return sendMail(paramString1, paramString2, paramString3, paramArrayOfString1, paramArrayOfString2, paramMap, true, false);
  }

  public boolean[] sendHtml(String paramString1, String paramString2, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, File[] paramArrayOfFile)
    throws IllegalArgumentException
  {
    return sendMail(paramString1, paramString2, paramArrayOfString1, paramArrayOfString2, paramArrayOfString3, paramArrayOfFile, true, false);
  }

  public boolean[] sendHtml(String paramString1, String paramString2, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, Map paramMap)
    throws IllegalArgumentException
  {
    return sendMail(paramString1, paramString2, paramArrayOfString1, paramArrayOfString2, paramArrayOfString3, paramMap, true, false);
  }

  public void addLogManager(LogManager paramLogManager)
  {
    this.logManager.add(paramLogManager);
  }

  public void removeLogManager(LogManager paramLogManager)
  {
    this.logManager.remove(paramLogManager);
  }

  private String[] parseDomain(String paramString)
  {
    try
    {
      NamingEnumeration localNamingEnumeration = dirContext.getAttributes(paramString, new String[] { "mx" }).getAll();
      String[] arrayOfString1;
      if (localNamingEnumeration.hasMore())
      {
        paramString = localNamingEnumeration.next().toString();
        paramString = paramString.substring(paramString.indexOf(": ") + 2);
        arrayOfString1 = paramString.split(",");
        MX[] arrayOfMX = new MX[arrayOfString1.length];
        for (int i = 0; i < arrayOfString1.length; i++)
        {
          String[] arrayOfString2 = arrayOfString1[i].trim().split(" ");
          arrayOfMX[i] = new MX(Integer.parseInt(arrayOfString2[0]), arrayOfString2[1]);
        }
        for (i = 1; i < arrayOfMX.length; i++)
          for (int j = i; j > 0; j--)
            if (arrayOfMX[(j - 1)].pri > arrayOfMX[j].pri)
            {
              MX localMX = arrayOfMX[(j - 1)];
              arrayOfMX[(j - 1)] = arrayOfMX[j];
              arrayOfMX[j] = localMX;
            }
        for (i = 0; i < arrayOfMX.length; i++)
          arrayOfString1[i] = arrayOfMX[i].address;
        return arrayOfString1;
      }
      localNamingEnumeration = dirContext.getAttributes(paramString, new String[] { "a" }).getAll();
      if (localNamingEnumeration.hasMore())
      {
        paramString = localNamingEnumeration.next().toString();
        paramString = paramString.substring(paramString.indexOf(": ") + 2).replace(" ", "");
        arrayOfString1 = paramString.split(",");
        return arrayOfString1;
      }
      return new String[] { paramString };
    }
    catch (NamingException localNamingException)
    {
      log(new StringBuilder().append("错误: 域名\"").append(paramString).append("\"无法解析").toString());
    }
    return null;
  }

  private int response(InputStream paramInputStream)
    throws IOException
  {
    byte[] arrayOfByte = new byte[1024];
    int i = paramInputStream.read(arrayOfByte);
    if (i == -1)
      return -1;
    String str = new String(arrayOfByte, 0, i).trim();
    log(new StringBuilder().append("响应: ").append(str).toString());
    return Integer.parseInt(str.substring(0, 3));
  }

  private void sendString(String paramString, OutputStream paramOutputStream)
    throws IOException
  {
    log(new StringBuilder().append("发送: ").append(paramString).toString());
    if (paramString == null)
      paramString = "";
    paramOutputStream.write(paramString.getBytes());
    paramOutputStream.flush();
  }

  private void log(String paramString)
  {
    int i = 0;
    int j = this.logManager.size();
    while (i < j)
    {
      ((LogManager)this.logManager.get(i)).output(paramString);
      i++;
    }
  }

  private static void sendNewline(OutputStream paramOutputStream)
    throws IOException
  {
    paramOutputStream.write(13);
    paramOutputStream.write(10);
    paramOutputStream.flush();
  }

  private static String getBase64String(String paramString)
  {
    if ((paramString == null) || (paramString.length() == 0))
      return "";
    StringBuffer localStringBuffer = new StringBuffer();
    byte[] arrayOfByte = paramString.getBytes();
    int i = 0;
    while (i < arrayOfByte.length)
    {
      if (i != 0)
        localStringBuffer.append(' ');
      localStringBuffer.append("=?");
      localStringBuffer.append(CHARSET);
      localStringBuffer.append("?B?");
      localStringBuffer.append(StringUtil.Base64.encode(arrayOfByte, i, Math.min(arrayOfByte.length - i, 30)));
      localStringBuffer.append("?=");
      i += 30;
      if (i < arrayOfByte.length)
      {
        localStringBuffer.append('\r');
        localStringBuffer.append('\n');
      }
    }
    return localStringBuffer.toString();
  }

  private static String parseUrl(String paramString)
  {
    return paramString.substring(paramString.lastIndexOf(64) + 1);
  }

  static
  {
    Hashtable localHashtable = new Hashtable();
    localHashtable.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
    try
    {
      dirContext = new InitialDirContext(localHashtable);
    }
    catch (NamingException localNamingException)
    {
    }
  }

  private class MX
  {
    final int pri;
    final String address;

    MX(int paramString, String arg3)
    {
      this.pri = paramString;
      Object localObject;
      this.address = localObject;
    }
  }
}