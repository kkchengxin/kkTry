package com.kk.rainbow.core.ceptor.self;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.kk.rainbow.core.util.StringUtil;

public class ProxyFactory
{
  public static final String JAVA = "JAVA";
  public static final String CGLIB = "CGLIB";
  private static final Map map = new HashMap();
  private static final String CACHE_CONFIG = "mobile_cache.xml";
  protected static final String MATCH_CODE = "*";
  protected static List cacheMths = null;
  protected static List<String> filterMths = null;
  protected static List expireMths = null;

  public static final Object getProxy(Object paramObject, ICeptorHandler paramICeptorHandler)
  {
    return getProxy(null, paramObject, paramICeptorHandler);
  }

  public static final Object getProxy(String paramString, Object paramObject, ICeptorHandler paramICeptorHandler)
  {
    if ((StringUtil.isEmpty(paramString)) || ("CGLIB".equals(paramString)))
      return getCglibProxy("rainbow.core.ceptor.self.CglibHandler", paramObject, paramICeptorHandler);
    return getJavaProxy("rainbow.core.ceptor.self.JavaHandler", paramObject, paramICeptorHandler);
  }

  private static final Object getJavaProxy(String paramString, Object paramObject, ICeptorHandler paramICeptorHandler)
  {
    try
    {
      JavaHandler localJavaHandler = (JavaHandler)getInstance(paramString);
      paramObject = localJavaHandler.bind(paramObject, paramICeptorHandler);
      return paramObject;
    }
    catch (InstantiationException localInstantiationException)
    {
      localInstantiationException.printStackTrace();
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      localIllegalAccessException.printStackTrace();
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      localClassNotFoundException.printStackTrace();
    }
    return null;
  }

  private static final Object getCglibProxy(String paramString, Object paramObject, ICeptorHandler paramICeptorHandler)
  {
    try
    {
      CglibHandler localCglibHandler = (CglibHandler)getInstance(paramString);
      paramObject = localCglibHandler.bind(paramObject, paramICeptorHandler);
      return paramObject;
    }
    catch (InstantiationException localInstantiationException)
    {
      localInstantiationException.printStackTrace();
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      localIllegalAccessException.printStackTrace();
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      localClassNotFoundException.printStackTrace();
    }
    return null;
  }

  private static final Object getInstance(String paramString)
    throws InstantiationException, IllegalAccessException, ClassNotFoundException
  {
    return Class.forName(paramString).newInstance();
  }

  private static boolean containProxy(String paramString)
  {
    return map.containsKey(paramString);
  }

  private static void initConfig()
  {
    Document localDocument = null;
    Element localElement1 = null;
    InputStream localInputStream = ProxyFactory.class.getClassLoader().getResourceAsStream("mobile_cache.xml");
    try
    {
      localDocument = new SAXReader().read(localInputStream);
      localElement1 = (Element)localDocument.selectSingleNode("/root/cache");
      List localList1 = localElement1.selectNodes("cache-method");
      List localList2 = localElement1.selectNodes("filter-method");
      List localList3 = localElement1.selectNodes("expire-method");
      Element localElement2;
      Iterator localIterator;
      if ((localList1 != null) && (!localList1.isEmpty()))
      {
        cacheMths = new ArrayList();
        localElement2 = null;
        localIterator = localList1.iterator();
        while (localIterator.hasNext())
        {
          localElement2 = (Element)localIterator.next();
          cacheMths.add(localElement2.getTextTrim());
        }
      }
      if ((localList2 != null) && (!localList2.isEmpty()))
      {
        filterMths = new ArrayList();
        localElement2 = null;
        localIterator = localList2.iterator();
        while (localIterator.hasNext())
        {
          localElement2 = (Element)localIterator.next();
          filterMths.add(localElement2.getTextTrim());
        }
      }
      if ((localList3 != null) && (!localList3.isEmpty()))
      {
        expireMths = new ArrayList();
        localElement2 = null;
        localIterator = localList3.iterator();
        while (localIterator.hasNext())
        {
          localElement2 = (Element)localIterator.next();
          expireMths.add(localElement2.getTextTrim());
        }
      }
    }
    catch (DocumentException localDocumentException)
    {
      localDocumentException.printStackTrace();
    }
  }

  static
  {
    initConfig();
  }
}