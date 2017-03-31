package com.kk.rainbow.core.json;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;

public class JacksonFactory
{
  private static JacksonFactory jksonFactory = new JacksonFactory();
  private static ObjectMapper mapper = new ObjectMapper();

  public static JacksonFactory getInstance()
  {
    return jksonFactory;
  }

  public ObjectMapper getObjectMapper()
  {
    return mapper;
  }

  public JsonGenerator getJsonGenerator(OutputStream paramOutputStream)
    throws IOException
  {
    return mapper.getJsonFactory().createJsonGenerator(paramOutputStream);
  }

  public JsonGenerator getJsonGenerator(Writer paramWriter)
    throws IOException
  {
    return mapper.getJsonFactory().createJsonGenerator(paramWriter);
  }

  public JsonGenerator getJsonGenerator(File paramFile, Encoding paramEncoding)
    throws IOException
  {
    return mapper.getJsonFactory().createJsonGenerator(paramFile, toJsonEncoding(paramEncoding));
  }

  public JsonGenerator getJsonGenerator(OutputStream paramOutputStream, Encoding paramEncoding)
    throws IOException
  {
    return mapper.getJsonFactory().createJsonGenerator(paramOutputStream, toJsonEncoding(paramEncoding));
  }

  private JsonEncoding toJsonEncoding(Encoding paramEncoding)
  {
    JsonEncoding localJsonEncoding = null;
//    switch (1.$SwitchMap$rainbow$core$json$Encoding[paramEncoding.ordinal()])
    switch (paramEncoding.ordinal())
    {
    case 1:
      localJsonEncoding = JsonEncoding.UTF8;
      break;
    case 2:
      localJsonEncoding = JsonEncoding.UTF16_BE;
      break;
    case 3:
      localJsonEncoding = JsonEncoding.UTF16_LE;
      break;
    case 4:
      localJsonEncoding = JsonEncoding.UTF32_BE;
      break;
    case 5:
      localJsonEncoding = JsonEncoding.UTF32_LE;
      break;
    default:
      localJsonEncoding = JsonEncoding.UTF8;
    }
    return localJsonEncoding;
  }
}