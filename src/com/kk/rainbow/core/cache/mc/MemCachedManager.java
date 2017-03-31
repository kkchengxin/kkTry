package com.kk.rainbow.core.cache.mc;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MemCachedManager extends MemCachedClient
{
  private static final MemCachedManager mcm = null;
  private static final String ENCODING = "UTF-8";
  private static final String CENTER_HOST = "the center host of cache";
  private static final String NATIVE_HOST = "the native host of cache";
  private final Set<String> keySet = new HashSet();
  public final String KEY_SEED_SPLIT = "-";
  public final long DEFAULT_EXPIRE = 3600000L;
  public final String KEY_TOP = "-";

  public static final MemCachedManager getInstance()
  {
    return mcm == null ? new MemCachedManager() : mcm;
  }

  private static String getHostSeed()
  {
    try
    {
      return InetAddress.getLocalHost().getHostAddress();
    }
    catch (UnknownHostException localUnknownHostException)
    {
      localUnknownHostException.printStackTrace();
    }
    return "DEFAULT SEED";
  }

  public Set<String> keySet()
  {
    if ((this.keySet != null) && (!this.keySet.isEmpty()))
      return this.keySet;
    Map localMap1 = statsItems();
    Map localMap2 = null;
    Map localMap3 = null;
    Iterator localIterator1 = localMap1.keySet().iterator();
    Iterator localIterator2 = null;
    Iterator localIterator3 = null;
    while (localIterator1.hasNext())
    {
      localMap2 = (Map)localMap1.get(localIterator1.next());
      localIterator2 = localMap2.keySet().iterator();
      while (localIterator2.hasNext())
      {
        String str1 = (String)localIterator2.next();
        if (str1.indexOf("number") != -1)
        {
          String[] arrayOfString = str1.split(":");
          localMap3 = statsCacheDump(Integer.parseInt(arrayOfString[1]), 0);
          localIterator3 = localMap3.values().iterator();
          while (localIterator3.hasNext())
          {
            Map localMap4 = (Map)localIterator3.next();
            Iterator localIterator4 = localMap4.keySet().iterator();
            while (localIterator4.hasNext())
            {
              String str2 = (String)localIterator4.next();
              try
              {
                this.keySet.add(URLDecoder.decode(str2, "UTF-8"));
              }
              catch (UnsupportedEncodingException localUnsupportedEncodingException)
              {
                localUnsupportedEncodingException.printStackTrace();
              }
            }
          }
        }
      }
    }
    return this.keySet;
  }

  public void addKey(String paramString)
  {
    keySet().add(paramString);
  }

  public String deleteRelactionCache(List<String> paramList)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    Set localSet = keySet();
    if ((localSet != null) && (!localSet.isEmpty()))
    {
      Iterator localIterator = localSet.iterator();
      while (localIterator.hasNext())
      {
        String str1 = (String)localIterator.next();
        String str2 = getKeySeed(str1);
        if ((paramList.contains(str2)) && (delete(str1)))
        {
          localStringBuffer.append(str2).append(";");
          localSet.remove(localSet);
        }
      }
    }
    return localStringBuffer.toString();
  }

  private String getKeySeed(String paramString)
  {
    getClass();
    return paramString.split("-")[0];
  }

  public static void main(String[] paramArrayOfString)
  {
    MemCachedManager localMemCachedManager = getInstance();
    long l1 = System.currentTimeMillis();
    localMemCachedManager.delete("22");
    localMemCachedManager.add("112", "c21");
    Set localSet = null;
    localMemCachedManager.delete("112");
    localMemCachedManager.delete("hhhhh11");
    localSet = localMemCachedManager.keySet();
    Iterator localIterator = localSet.iterator();
    while (localIterator.hasNext())
      System.out.println("keyset==" + localIterator.next());
    long l2 = System.currentTimeMillis();
    System.out.println("total time:" + (l2 - l1));
  }

  static
  {
    String str = getHostSeed();
    "the native host of cache".concat(str);
    MemCachedClient localMemCachedClient = null;
    localMemCachedClient = new MemCachedClient();
    String[] arrayOfString = { "192.168.50.53:11211", "192.168.50.53:11212" };
    Integer[] arrayOfInteger = { new Integer(5), new Integer(5) };
    int i = 10;
    int j = 5;
    int k = 50;
    long l1 = 1800000L;
    long l2 = 300000L;
    long l3 = 5000L;
    int m = 3000;
    int n = 3000;
    int i1 = 0;
    boolean bool = false;
    int i2 = 0;
    SockIOPool localSockIOPool = SockIOPool.getInstance();
    localSockIOPool.setServers(arrayOfString);
    localSockIOPool.setWeights(arrayOfInteger);
    localSockIOPool.setInitConn(i);
    localSockIOPool.setMinConn(j);
    localSockIOPool.setMaxConn(k);
    localSockIOPool.setMaxIdle(l1);
    localSockIOPool.setMaxBusyTime(l2);
    localSockIOPool.setMaintSleep(l3);
    localSockIOPool.setSocketTO(m);
    localSockIOPool.setNagle(bool);
    localSockIOPool.setHashingAlg(2);
    localSockIOPool.setAliveCheck(true);
    localSockIOPool.initialize();
    localMemCachedClient.setCompressEnable(true);
    localMemCachedClient.setCompressThreshold(65536L);
    localMemCachedClient.setDefaultEncoding("UTF-8");
  }
}