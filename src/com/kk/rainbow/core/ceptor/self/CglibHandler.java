package com.kk.rainbow.core.ceptor.self;

import java.io.PrintStream;
import java.lang.reflect.Method;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CglibHandler
  implements MethodInterceptor
{
  private Object obj = null;
  private ICeptorHandler handler;

  public Object bind(Object paramObject, ICeptorHandler paramICeptorHandler)
  {
    this.obj = paramObject;
    this.handler = paramICeptorHandler;
    Enhancer localEnhancer = new Enhancer();
    localEnhancer.setSuperclass(paramObject.getClass());
    localEnhancer.setCallback(this);
    return localEnhancer.create();
  }

  public Object intercept(Object paramObject, Method paramMethod, Object[] paramArrayOfObject, MethodProxy paramMethodProxy)
    throws Throwable
  {
    System.out.println("cglib mth before");
    Object localObject1 = this.handler.beforeProcess(paramObject, paramMethod, paramArrayOfObject);
    if (localObject1 != null)
      return localObject1;
    Object localObject2 = paramMethod.invoke(this.obj, paramArrayOfObject);
    Object localObject3 = this.handler.afterProcess(localObject2, paramObject, paramMethod, paramArrayOfObject);
    System.out.println("cglib mth after");
    return localObject2;
  }
}