package com.kk.rainbow.core.json;

import java.io.IOException;
import java.io.PrintStream;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public final class Jackson
{
  public static final String toString(Object paramObject)
  {
    if (paramObject == null)
      return null;
    try
    {
      return JacksonFactory.getInstance().getObjectMapper().writeValueAsString(paramObject);
    }
    catch (JsonGenerationException localJsonGenerationException)
    {
      localJsonGenerationException.printStackTrace();
    }
    catch (JsonMappingException localJsonMappingException)
    {
      localJsonMappingException.printStackTrace();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return null;
  }

  public static final String getNodeValueFromString(String paramString1, String paramString2)
  {
    JsonNode localJsonNode1 = null;
    try
    {
      JsonNode localJsonNode2 = JacksonFactory.getInstance().getObjectMapper().readTree(paramString1);
      localJsonNode1 = localJsonNode2.findValue(paramString2);
    }
    catch (JsonProcessingException localJsonProcessingException)
    {
      localJsonProcessingException.printStackTrace();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return localJsonNode1 == null ? null : localJsonNode1.getTextValue();
  }

  public static void main(String[] paramArrayOfString)
  {
    String str1 = "error";
    String str2 = "{\"request\":\"/statuses/update.json\",\"error_code\":\"400\",\"error\":\"40025:Error: repeated weibo text!\"}";
    String str3 = getNodeValueFromString(str2, str1);
    System.out.println("aaaa\n");
    System.out.println(str3);
  }
}