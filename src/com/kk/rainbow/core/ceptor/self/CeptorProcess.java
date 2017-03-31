package com.kk.rainbow.core.ceptor.self;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.kk.rainbow.core.cache.mc.MemCachedManager;
import com.kk.rainbow.core.util.StringUtil;

public class CeptorProcess
{
  MemCachedManager mcm = null;
  private String key = null;
  private boolean isCache = false;

  public Object beforeProcess(Object paramObject, Method paramMethod, Object[] paramArrayOfObject, String paramString)
  {
    this.mcm = MemCachedManager.getInstance();
    this.isCache = ((isCache(paramMethod.getName())) && (!isFilter(paramMethod.getName())));
    this.key = getKey(paramString, paramMethod, paramArrayOfObject);
    System.out.println("key=" + this.key);
    if ((this.isCache) && (this.mcm.keyExists(this.key)))
    {
      Object localObject = this.mcm.get(this.key);
      if (localObject != null)
        System.out.println("||The method:" + paramMethod.getName() + " 's result returned from the cache  !");
      return localObject == null ? null : localObject;
    }
    return null;
  }

  public Object afterProcess(Object paramObject1, Object paramObject2, Method paramMethod, Object[] paramArrayOfObject, String paramString1, String paramString2)
  {
    boolean bool = false;
    if (this.isCache)
    {
      if (paramObject1 == null)
      {
        System.out.println("||The method:" + paramMethod.getName() + " 's result was NULL, so not be cached  !");
        return null;
      }
      this.mcm.getClass();
      bool = this.mcm.add(this.key, paramObject1, new Date(System.currentTimeMillis() + 3600000L));
      if (bool)
      {
        this.mcm.addKey(this.key);
        System.out.println("||The method:" + paramMethod.getName() + " 's result cached successfully!");
      }
      else
      {
        System.out.println("||The method:" + paramMethod.getName() + " 's result cached faild !");
      }
    }
    if (isExpire(paramMethod.getName()))
    {
      String str = this.mcm.deleteRelactionCache(installKeySeed(paramString1, paramString2));
      if (StringUtil.isEmpty(str))
        System.out.println("||The method:" + paramMethod.getName() + " touch  seeds were not exist in the cache! so that was in need of delete!");
      else
        System.out.println("||The method:" + paramMethod.getName() + " has deleted the interrelated data from the cache successfully! \n The keySeeds of deleted :" + str);
      return Boolean.valueOf(true);
    }
    return Boolean.valueOf(bool);
  }

  private boolean isCache(String paramString)
  {
    return isMatchRule(paramString, ProxyFactory.cacheMths);
  }

  private boolean isFilter(String paramString)
  {
    return isMatchRule(paramString, ProxyFactory.filterMths);
  }

  private boolean isExpire(String paramString)
  {
    return isMatchRule(paramString, ProxyFactory.expireMths);
  }

  private boolean isMatchRule(String paramString, List paramList)
  {
    if ((paramList != null) && (!paramList.isEmpty()))
    {
      Iterator localIterator = paramList.iterator();
      String str = null;
      if (localIterator.hasNext())
      {
        str = (String)localIterator.next();
        if ("*".equals(str))
          return true;
        if (paramString.equalsIgnoreCase(str))
          return true;
        if ((str.endsWith("*")) && (paramString.startsWith(getMthSeed(str))))
          return true;
        return (str.startsWith("*")) && (paramString.endsWith(getMthSeed(str)));
      }
    }
    return false;
  }

  private String getMthSeed(String paramString)
  {
    if (StringUtil.isEmpty(paramString))
      return null;
    int i = paramString.indexOf("*");
    switch (i)
    {
    case -1:
      return paramString;
    case 0:
      return paramString.substring(1);
    }
    return paramString.substring(0, i);
  }

  private String getKey(String paramString, Method paramMethod, Object[] paramArrayOfObject)
  {
    this.mcm.getClass();
    this.mcm.getClass();
    String str1 = "-" + paramString + "-" + paramMethod.getName();
    StringBuffer localStringBuffer = new StringBuffer();
    if (paramArrayOfObject.length > 0)
    {
      Class[] arrayOfClass = paramMethod.getParameterTypes();
      for (int i = 0; i < paramArrayOfObject.length; i++)
      {
        localStringBuffer.append(arrayOfClass[i].toString());
        String str2 = null;
        if (paramArrayOfObject[i] != null)
        {
          str2 = paramArrayOfObject[i].toString();
          localStringBuffer.append(str2);
        }
      }
    }
    return str1 + StringUtil.initMD5(localStringBuffer.toString());
  }

  private List installKeySeed(String paramString1, String paramString2)
  {
    if (StringUtil.isEmpty(paramString1))
      System.out.println("||:::Please change the keySeed!");
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(paramString1);
    if (!StringUtil.isEmpty(paramString2))
    {
      String[] arrayOfString = paramString2.split(";");
      localArrayList.addAll(Arrays.asList(arrayOfString));
    }
    return localArrayList;
  }
}