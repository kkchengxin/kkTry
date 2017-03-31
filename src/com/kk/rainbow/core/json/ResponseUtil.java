package com.kk.rainbow.core.json;

import org.codehaus.jackson.JsonNode;

import com.kk.rainbow.core.exception.RException;
import com.kk.rainbow.core.mybatis3.Page;

public class ResponseUtil
{
  public static final boolean isSuccess(JsonNode paramJsonNode)
    throws RException
  {
    if (paramJsonNode == null)
      return false;
    int i = getCode(paramJsonNode);
    return i > 0;
  }

  public static final int getCode(JsonNode paramJsonNode)
    throws RException
  {
    if (paramJsonNode == null)
      return -1000;
    JsonNode localJsonNode = paramJsonNode.get(JSONKeys.CODE.getValue());
    if (localJsonNode == null)
      throw new RException("-1", "缺少节点:" + JSONKeys.CODE.getValue());
    return Integer.parseInt(localJsonNode.getTextValue());
  }

  public static final String getDesc(JsonNode paramJsonNode)
    throws RException
  {
    if (paramJsonNode == null)
      return null;
    JsonNode localJsonNode = paramJsonNode.get(JSONKeys.DESC.getValue());
    if (localJsonNode == null)
      throw new RException("-1", "缺少节点:" + JSONKeys.DESC.getValue());
    return localJsonNode.getTextValue();
  }

  public static final JsonNode getData(JsonNode paramJsonNode)
    throws RException
  {
    if (paramJsonNode == null)
      return null;
    JsonNode localJsonNode = paramJsonNode.get(JSONKeys.DATA.getValue());
    if (localJsonNode == null)
      throw new RException("-1", "缺少节点:" + JSONKeys.DATA.getValue());
    return localJsonNode;
  }

  public static final Page getPage(JsonNode paramJsonNode)
    throws RException
  {
    if (paramJsonNode == null)
      return null;
    JsonNode localJsonNode1 = getData(paramJsonNode);
    if (localJsonNode1 == null)
      return null;
    JsonNode localJsonNode2 = localJsonNode1.get(JSONKeys.PAGE.getValue());
    return (Page)JSONUtils.toObject(localJsonNode2, Page.class);
  }

  public static final <T> T getArray(JsonNode paramJsonNode, Class<T> paramClass)
    throws RException
  {
    if (paramJsonNode == null)
      return null;
    JsonNode localJsonNode1 = getData(paramJsonNode);
    if (localJsonNode1 == null)
      return null;
    JsonNode localJsonNode2 = localJsonNode1.get(JSONKeys.LIST.getValue());
    return JSONUtils.toObject(localJsonNode2, paramClass);
  }

  public static final <T> T getData(JsonNode paramJsonNode, Class<T> paramClass)
    throws RException
  {
    if (paramJsonNode == null)
      return null;
    JsonNode localJsonNode = getData(paramJsonNode);
    return JSONUtils.toObject(localJsonNode, paramClass);
  }

  public static final <T> T getObject(JsonNode paramJsonNode, String paramString, Class<T> paramClass)
    throws RException
  {
    if ((paramJsonNode == null) || (paramString == null))
      return null;
    JsonNode localJsonNode = paramJsonNode.get(paramString);
    if (localJsonNode == null)
      throw new RException("-1", "缺少节点:" + paramString);
    return JSONUtils.toObject(localJsonNode, paramClass);
  }
}