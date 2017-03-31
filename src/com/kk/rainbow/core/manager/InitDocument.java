package com.kk.rainbow.core.manager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.kk.rainbow.core.reflect.EntityReflect;


public class InitDocument
{
  private Document document;

  public InitDocument(Document paramDocument, Object paramObject)
  {
    Element localElement1 = null;
    this.document = paramDocument;
    if (this.document == null)
      this.document = DocumentHelper.createDocument();
    localElement1 = this.document.getRootElement();
    if (localElement1 == null)
      localElement1 = this.document.addElement("modules");
    Element localElement2 = localElement1.addElement(paramObject.getClass().getSimpleName());
    Class localClass = paramObject.getClass();
    Field[] arrayOfField = EntityReflect.getInstance().installPojoFields(localClass);
    for (int i = 0; i < arrayOfField.length; i++)
      if (EntityReflect.getInstance().isValid(localClass, arrayOfField[i]))
      {
        Element localElement3 = localElement2.addElement(arrayOfField[i].getName());
        Log.debug("method::::get" + arrayOfField[i].getName().substring(0, 1).toUpperCase() + arrayOfField[i].getName().substring(1));
        try
        {
          localElement3.setText((String)EntityReflect.getInstance().executeGetMethod(paramObject, arrayOfField[i].getName()));
        }
        catch (SecurityException localSecurityException)
        {
          localSecurityException.printStackTrace();
        }
        catch (NoSuchMethodException localNoSuchMethodException)
        {
          localNoSuchMethodException.printStackTrace();
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          localIllegalArgumentException.printStackTrace();
        }
        catch (IllegalAccessException localIllegalAccessException)
        {
          localIllegalAccessException.printStackTrace();
        }
        catch (InvocationTargetException localInvocationTargetException)
        {
          localInvocationTargetException.printStackTrace();
        }
        catch (NoSuchFieldException localNoSuchFieldException)
        {
          localNoSuchFieldException.printStackTrace();
        }
      }
    arrayOfField = null;
  }

  public Document getDocument()
  {
    return this.document;
  }
}