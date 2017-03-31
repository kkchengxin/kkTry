package com.kk.rainbow.core.module.img;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import com.kk.rainbow.core.module.img.exception.ConvertException;
import com.kk.rainbow.core.module.img.exception.NullElementException;
import com.kk.rainbow.core.module.img.oo.Img;
import com.kk.rainbow.core.util.FileUtil;

public final class ImageUtil
{
  private static final String IMAGERULE = "img.cfg";
  private static final String DRIVE_XPATH = "/root/drive";
  private static String drive = "C:/tt/";
  public final List<Img> imgRules = new ArrayList();

  public ImageUtil()
  {
    InputStream localInputStream = ImageUtil.class.getClassLoader().getResourceAsStream("img.cfg");
    SAXReader localSAXReader = new SAXReader();
    try
    {
      Document localDocument = localSAXReader.read(localInputStream);
      Element localElement = (Element)localDocument.selectSingleNode("/root/drive");
      if (localElement == null)
        throw new NullElementException("||File img.cfg has not drive element");
      drive = localElement.getTextTrim();
      if (drive == null)
        throw new NullPointerException("||Element of drive was not a value int the file img.cfg !");
      localDocument.clearContent();
    }
    catch (DocumentException localDocumentException)
    {
      localDocumentException.printStackTrace();
      System.out.println("||ParseXML error! Please check img.cfg!");
    }
    System.out.println("static dive=" + drive);
  }

  public byte[] convertImage(Img paramImg, boolean paramBoolean)
    throws IOException, ConvertException
  {
    if (paramImg == null)
      throw new ConvertException("请求参数错误");
    if (isImgUrl(paramImg.getUrl()))
      return convertRemoteImage(paramImg, null, paramBoolean);
    throw new ConvertException("url 不符合规则!");
  }

  public byte[] convertImage(Img paramImg, String paramString, boolean paramBoolean)
    throws IOException, ConvertException
  {
    if (paramImg == null)
      throw new ConvertException("请求参数错误");
    if (isImgUrl(paramImg.getUrl()))
      return convertRemoteImage(paramImg, paramString, paramBoolean);
    throw new ConvertException("url 不符合规则!");
  }

  private byte[] convertRemoteImage(Img paramImg, String paramString, boolean paramBoolean)
    throws ConvertException
  {
    long l = System.currentTimeMillis();
    String str = paramImg.getUrl();
    byte[] arrayOfByte = null;
    try
    {
      URL localURL = new URL(paramImg.getUrl());
      BufferedImage localBufferedImage1 = ImageIO.read(localURL);
      int i = localBufferedImage1.getWidth();
      int j = localBufferedImage1.getHeight();
      paramString = initFolder(str, paramString);
      System.out.println("||TimgPath = " + paramString);
      Img localImg = toImgRule(paramImg, i, j);
      System.out.println("new=" + localImg.getWidth() + ":::" + localImg.getHeight());
      File localFile = FileUtil.isFileExist(paramString);
      if (localFile != null)
      {
        BufferedImage localBufferedImage2 = ImageIO.read(localFile);
        System.out.println("old=" + localBufferedImage2.getWidth(null) + ":" + localBufferedImage2.getHeight(null));
        if ((localBufferedImage2.getWidth(null) == localImg.getWidth()) && (localBufferedImage2.getHeight(null) == localImg.getHeight()))
        {
          if (paramBoolean)
          {
            System.out.println("||the img is exist,and two images' rules are equals, and had appoint a imgRule !  return bytes of the img ");
            return FileUtil.getBytes(paramString);
          }
          System.out.println("||the img is exist!  but isReturn is false, so return null");
          return null;
        }
        System.out.println("||the img is exist! but two images' rules are diffrient!");
      }
      BufferedImage localBufferedImage2 = null;
      localBufferedImage2 = new BufferedImage(localImg.getWidth(), localImg.getHeight(), 1);
      localBufferedImage2.getGraphics().drawImage(localBufferedImage1, 0, 0, localImg.getWidth(), localImg.getHeight(), null);
      FileOutputStream localFileOutputStream = new FileOutputStream(paramString);
      JPEGImageEncoder localJPEGImageEncoder = JPEGCodec.createJPEGEncoder(localFileOutputStream);
      localJPEGImageEncoder.encode(localBufferedImage2);
      localFileOutputStream.close();
      if (paramBoolean)
      {
        arrayOfByte = FileUtil.getBytes(paramString);
        System.out.println("||convert the img Success!  And return the bytes");
      }
      else
      {
        System.out.println("||convert the img Success!  But does not appoint imgRule, does not return the bytes");
      }
    }
    catch (MalformedURLException localMalformedURLException)
    {
      System.out.println("||Can't get srcImgurl! Please check the network!");
      throw new ConvertException("请检查网络连接!");
    }
    catch (IOException localIOException)
    {
      System.out.println("||Read the file of srcImgurl or targetImgurl error!");
      throw new ConvertException("读取源图片或本地图片出错!");
    }
    System.out.println(System.currentTimeMillis() - l);
    return arrayOfByte;
  }

  private Img toImgRule(Img paramImg, int paramInt1, int paramInt2)
  {
    if ((paramImg.getWidth() == 0) && (paramImg.getHeight() == 0))
    {
      paramImg.setWidth(paramInt1);
      paramImg.setHeight(paramInt2);
      return paramImg;
    }
    if (paramImg.getPrecise().equals("true"))
    {
      if (paramImg.getWidth() == 0)
        paramImg.setWidth(paramInt1);
      if (paramImg.getHeight() == 0)
        paramImg.setHeight(paramInt2);
    }
    else
    {
      double d1 = paramImg.getWidth() / paramInt1;
      double d2 = paramImg.getHeight() / paramInt2;
      double d3 = 1.0D;
      d1 = d1 == 0.0D ? 1.0D : d1;
      d2 = d2 == 0.0D ? 1.0D : d2;
      d3 = d1 < d2 ? d1 : d2;
      System.out.println("||rate ==" + d3);
      paramImg.setWidth((int)(d3 * paramInt1));
      paramImg.setHeight((int)(d3 * paramInt2));
    }
    return paramImg;
  }

  private String initFolder(String paramString1, String paramString2)
    throws MalformedURLException
  {
    System.out.println("create remote image");
    String str1 = null;
    String str2 = null;
    String str3 = null;
    URL localURL = new URL(paramString1);
    if (paramString2 == null)
    {
      str1 = getDomain(localURL);
      str3 = getFileName(localURL);
      str2 = getFilePath(localURL, str3);
      drive += str1;
      if ((str2 != null) && (!str2.isEmpty()))
        drive = drive + File.separator + str2;
      paramString2 = drive + File.separator + str3;
      System.out.println("timgPath is null=" + drive);
    }
    else
    {
      str3 = getFileName(paramString2);
      str2 = getFilePath(null, str3, paramString2);
      if ((str2 != null) && (!str2.isEmpty()))
        drive = drive + File.separator + str2;
      paramString2 = drive + File.separator + str3;
      System.out.println("timgPath is not null=" + drive);
    }
    FileUtil.mkdir(drive);
    return paramString2;
  }

  private String getDomain(URL paramURL)
  {
    String str = paramURL.getHost();
    int i = str.indexOf(":");
    if (i > 0)
      str = str.substring(0, i + 1);
    return str;
  }

  private String getFileName(URL paramURL)
  {
    String str = paramURL.getPath();
    return str.substring(str.lastIndexOf("/") + 1);
  }

  private String getFilePath(URL paramURL, String paramString)
  {
    String str = paramURL.getPath();
    return paramString.equals(str.substring(1)) ? "" : str.substring(1, str.indexOf(paramString) - 1);
  }

  public boolean isImgUrl(String paramString)
  {
    return paramString == null ? false : paramString.matches("^http(s)?://([a-zA-Z0-9/&=?.%-_])+.(jpg|gif|jpeg|png|bmp)$");
  }

  private String getFileName(String paramString)
  {
    int i = paramString.lastIndexOf("/");
    if (i > 0)
      return paramString.substring(i + 1);
    i = paramString.lastIndexOf("\\");
    return paramString.substring(i + 1);
  }

  private String getFilePath(String paramString1, String paramString2, String paramString3)
  {
    System.out.println(paramString1 + ":" + paramString2 + ":" + paramString3);
    if ((paramString1 == null) && (paramString2 != null))
      return paramString3.substring(0, paramString3.indexOf(paramString2));
    if ((paramString1 != null) && (paramString2 != null))
    {
      int i = paramString3.indexOf(paramString1) + paramString1.length() + 1;
      int j = paramString3.indexOf(paramString2);
      return paramString3.substring(i, j - 1);
    }
    return null;
  }

  public static void main(String[] paramArrayOfString)
    throws IOException, ConvertException
  {
    ImageUtil localImageUtil = new ImageUtil();
    Img localImg = new Img();
    localImg.setUrl("http://www.google.com.hk/intl/zh-CN/images/logo_cn.png");
    localImg.setWidth(100);
    localImg.setHeight(200);
    localImg.setPrecise("true");
    System.out.println(localImageUtil.convertImage(localImg, true));
  }
}