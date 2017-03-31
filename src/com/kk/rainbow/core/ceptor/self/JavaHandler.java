package com.kk.rainbow.core.ceptor.self;

import java.io.PrintStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JavaHandler
  implements InvocationHandler
{
  private Object obj = null;
  private String keySeed = null;
  private String rKeySeed = null;
  private ICeptorHandler handler;

  public Object bind(Object paramObject, ICeptorHandler paramICeptorHandler)
  {
    this.obj = paramObject;
    this.handler = paramICeptorHandler;
    return Proxy.newProxyInstance(paramObject.getClass().getClassLoader(), paramObject.getClass().getInterfaces(), this);
  }

  public Object invoke(Object paramObject, Method paramMethod, Object[] paramArrayOfObject)
    throws Throwable
  {
    System.out.println("method before...");
    Object localObject1 = this.handler.beforeProcess(paramObject, paramMethod, paramArrayOfObject);
    if (localObject1 != null)
      return localObject1;
    Object localObject2 = paramMethod.invoke(this.obj, paramArrayOfObject);
    Object localObject3 = this.handler.afterProcess(localObject2, paramObject, paramMethod, paramArrayOfObject);
    System.out.println("method after...");
    return localObject2;
  }
}