package com.kk.rainbow.core.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.kk.rainbow.core.manager.Log;

public class EntityReflect
{
  private static EntityReflect pojoReflect = null;

  public static EntityReflect getInstance()
  {
    if (pojoReflect == null)
      pojoReflect = new EntityReflect();
    return pojoReflect;
  }

  public static Field getFieldByFieldName(Class<?> paramClass, String paramString)
  {
    for (Object localObject = paramClass; localObject != Object.class; localObject = ((Class)localObject).getSuperclass())
      try
      {
        return ((Class)localObject).getDeclaredField(paramString);
      }
      catch (SecurityException localSecurityException)
      {
        localSecurityException.printStackTrace();
      }
      catch (NoSuchFieldException localNoSuchFieldException)
      {
      }
    return null;
  }

  public static Object getValueByFieldName(Object paramObject, String paramString)
    throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
  {
    Field localField = getFieldByFieldName(paramObject.getClass(), paramString);
    Object localObject = null;
    if (localField != null)
      if (localField.isAccessible())
      {
        localObject = localField.get(paramObject);
      }
      else
      {
        localField.setAccessible(true);
        localObject = localField.get(paramObject);
        localField.setAccessible(false);
      }
    return localObject;
  }

  public Field[] installPojoFields(Class<?> paramClass)
  {
    int i = 0;
    int j = 0;
    Field[] arrayOfField3 = null;
    int k = 0;
    Field[] arrayOfField2 = paramClass.getDeclaredFields();
    if (arrayOfField2 != null)
    {
      j = arrayOfField2.length;
      i += j;
    }
    if (arrayOfField2 == null)
      Log.debug("childFields is null");
    Class localClass = paramClass.getSuperclass();
    if (isSerializable(localClass))
      localClass = null;
    if (localClass != null)
      arrayOfField3 = localClass.getDeclaredFields();
    if (arrayOfField3 != null)
    {
      k = arrayOfField3.length;
      i += k;
    }
    if (arrayOfField3 == null)
      Log.debug("superFields is null");
    Field[] arrayOfField1 = new Field[i];
    if ((arrayOfField2 != null) && (arrayOfField2.length > 0))
    {
      Log.debug("childFields.length:" + arrayOfField2.length);
      System.arraycopy(arrayOfField2, 0, arrayOfField1, 0, j);
    }
    if ((arrayOfField3 != null) && (arrayOfField3.length > 0))
    {
      Log.debug("superFields.length:" + arrayOfField3.length);
      System.arraycopy(arrayOfField3, 0, arrayOfField1, j, k);
    }
    Log.debug("All fields.length:" + arrayOfField1.length);
    if ((arrayOfField1 == null) || (arrayOfField1.length <= 0))
      return null;
    return arrayOfField1;
  }

  public <T> boolean isValid(Class<T> paramClass, Field paramField)
  {
    return (!paramField.getType().getName().equals(paramClass.getSimpleName())) && (!paramField.getType().getName().equals(paramClass.getName()));
  }

  private <T> boolean isSerializable(Class<T> paramClass)
  {
    return ("Serializable".equals(paramClass.getSimpleName())) || ("java.io.Serializable".equals(paramClass.getName()));
  }

  public void executeSetMethod(Object paramObject, String paramString1, String paramString2)
    throws SecurityException, NoSuchMethodException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
  {
    String str = "set" + paramString1.substring(0, 1).toUpperCase() + paramString1.substring(1);
    Field localField = paramObject.getClass().getDeclaredField(paramString1);
    Method localMethod = paramObject.getClass().getMethod(str, new Class[] { localField.getType() });
    Log.debug("执行方法：" + localMethod.toString() + "值为：" + paramString2);
    localMethod.invoke(paramObject, new Object[] { paramString2 });
  }

  public Object executeGetMethod(Object paramObject, String paramString)
    throws SecurityException, NoSuchFieldException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
  {
    String str = "get" + paramString.substring(0, 1).toUpperCase() + paramString.substring(1);
    Method localMethod = paramObject.getClass().getMethod(str, new Class[] { paramObject.getClass() });
    Object localObject = localMethod.invoke(paramObject, new Object[] { paramObject.getClass() });
    return localObject == null ? "" : localObject;
  }
}