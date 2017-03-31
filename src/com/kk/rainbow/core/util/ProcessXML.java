package com.kk.rainbow.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import com.kk.rainbow.core.manager.InitDocument;
import com.kk.rainbow.core.manager.Log;

public class ProcessXML
{
  private static ProcessXML processXML = new ProcessXML();

  public static ProcessXML getInstance()
  {
    return processXML == null ? new ProcessXML() : processXML;
  }

  public Document getDocument(String paramString)
  {
    if (StringUtil.isEmpty(paramString))
    {
      Log.error("系统未找到指定文件:" + paramString);
      return null;
    }
    Document localDocument = null;
    File localFile = new File(paramString);
    if (!localFile.isFile())
      return null;
    SAXReader localSAXReader = new SAXReader();
    try
    {
      localDocument = localSAXReader.read(localFile);
      return localDocument == null ? DocumentHelper.createDocument() : localDocument;
    }
    catch (DocumentException localDocumentException)
    {
      localDocumentException.printStackTrace();
      localDocument = DocumentHelper.createDocument();
      Log.info("获取xml文件document出错,检查文件路径，及文件是否为xml文件");
    }
    return localDocument;
  }

  public boolean createXML(Document paramDocument, String paramString)
  {
    OutputStreamWriter localOutputStreamWriter = null;
    XMLWriter localXMLWriter = null;
    try
    {
      localOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(paramString), "utf-8");
      OutputFormat localOutputFormat = OutputFormat.createPrettyPrint();
      localOutputFormat.setEncoding("utf-8");
      localXMLWriter = new XMLWriter(localOutputStreamWriter, localOutputFormat);
      localXMLWriter.write(paramDocument);
      Log.info("写入文件成功:" + paramString);
      boolean bool = true;
      return bool;
    }
    catch (IOException localIOException1)
    {
      Log.error("写入文件失败:" + paramString + "\n" + localIOException1.getMessage());
      localIOException1.printStackTrace();
    }
    finally
    {
      try
      {
        localXMLWriter.close();
        localOutputStreamWriter.close();
      }
      catch (IOException localIOException4)
      {
        localIOException4.printStackTrace();
      }
    }
    return false;
  }

  public Element getElement(Document paramDocument, String paramString)
  {
    Log.info("查找element对象,xpth:" + paramString);
    if (paramDocument == null)
      return null;
    Node localNode = paramDocument.selectSingleNode(paramString);
    return localNode == null ? null : (Element)localNode;
  }

  public String getElementText(Element paramElement, String paramString)
  {
    return paramElement.elementText(paramString);
  }

  public String getElementText(String paramString1, String paramString2, String paramString3)
  {
    Document localDocument = getDocument(paramString1);
    Element localElement = getElement(localDocument, paramString2);
    return getElementText(localElement, paramString3);
  }

  public String getElementText(Document paramDocument, String paramString1, String paramString2)
  {
    Element localElement = getElement(paramDocument, paramString1);
    return getElementText(localElement, paramString2);
  }

  public String getAttributeValue(Element paramElement, String paramString)
  {
    return paramElement.attributeValue(paramString);
  }

  public String getAttributeValue(String paramString1, String paramString2, String paramString3)
  {
    Document localDocument = getDocument(paramString1);
    Element localElement = getElement(localDocument, paramString2);
    return getAttributeValue(localElement, paramString3);
  }

  public boolean isExistElement(String paramString1, String paramString2)
  {
    return getElement(getDocument(paramString1), paramString2) != null;
  }

  public boolean isExistElement(Document paramDocument, String paramString)
  {
    return getElement(paramDocument, paramString) != null;
  }

  public Document addElement(Document paramDocument, String paramString1, String paramString2, String paramString3)
  {
    if (paramDocument == null)
      return null;
    Node localNode = paramDocument.selectSingleNode(paramString1);
    if (localNode == null)
    {
      Log.error("父节点不存在：" + paramString1);
      return paramDocument;
    }
    ((Element)localNode).addElement(paramString2, paramString3);
    Log.info("modules.xml中节点[" + localNode.getName() + "]添加节点[" + paramString2 + "]成功");
    return paramDocument;
  }

  public Document addElement(Document paramDocument, Object paramObject)
  {
    if ((paramDocument == null) || (paramObject == null))
      return null;
    try
    {
      new InitDocument(paramDocument, paramObject);
      Log.info("modules.xml根中添加节点[" + paramObject.getClass().getSimpleName() + "]及所有子节点成功");
      return paramDocument;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      Log.info("modules.xml根中添加节点[" + paramObject.getClass().getSimpleName() + "]及所有子节点失败");
    }
    return null;
  }
}