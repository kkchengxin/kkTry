package com.kk.rainbow.core.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XMLUtil
{
  private static XMLUtil xmlUtil = new XMLUtil();

  public XMLUtil getInstance()
  {
    return xmlUtil == null ? new XMLUtil() : null;
  }

  public Document getDocument(String paramString)
  {
    File localFile = new File(paramString);
    if (!localFile.exists())
      return null;
    SAXReader localSAXReader = new SAXReader();
    Document localDocument = null;
    try
    {
      localDocument = localSAXReader.read(localFile);
    }
    catch (DocumentException localDocumentException)
    {
      localDocumentException.printStackTrace();
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
      localOutputFormat.setTrimText(true);
      localOutputFormat.setNewlines(true);
      localXMLWriter = new XMLWriter(localOutputStreamWriter, localOutputFormat);
      localXMLWriter.write(paramDocument);
      boolean bool = true;
      return bool;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      localUnsupportedEncodingException.printStackTrace();
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      localFileNotFoundException.printStackTrace();
    }
    catch (IOException localIOException3)
    {
      localIOException3.printStackTrace();
    }
    finally
    {
      try
      {
        localOutputStreamWriter.close();
        localXMLWriter.close();
      }
      catch (IOException localIOException6)
      {
        localIOException6.printStackTrace();
      }
    }
    return false;
  }
}