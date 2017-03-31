package com.kk.rainbow.core.springmvc;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.annotate.JsonFilter;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.AbstractView;
import com.kk.rainbow.core.exception.RException;

public class RMappingJacksonJsonView extends AbstractView
{
  public static final String DEFAULT_CONTENT_TYPE = "application/json";
  private ObjectMapper objectMapper = new ObjectMapper();
  private JsonEncoding encoding = JsonEncoding.UTF8;
  private boolean prefixJson = false;
  private Boolean prettyPrint;
  private Set<String> modelKeys;
  private boolean extractValueFromSingleKeyModel = false;
  private boolean disableCaching = true;
  private boolean updateContentLength = false;

  public RMappingJacksonJsonView()
  {
    setContentType("application/json");
    setExposePathVariables(false);
  }

  public void setObjectMapper(ObjectMapper paramObjectMapper)
  {
    Assert.notNull(paramObjectMapper, "'objectMapper' must not be null");
    this.objectMapper = paramObjectMapper;
    configurePrettyPrint();
  }

  private void configurePrettyPrint()
  {
    if (this.prettyPrint != null)
      this.objectMapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, this.prettyPrint.booleanValue());
  }

  public void setEncoding(JsonEncoding paramJsonEncoding)
  {
    Assert.notNull(paramJsonEncoding, "'encoding' must not be null");
    this.encoding = paramJsonEncoding;
  }

  public void setPrefixJson(boolean paramBoolean)
  {
    this.prefixJson = paramBoolean;
  }

  public void setPrettyPrint(boolean paramBoolean)
  {
    this.prettyPrint = Boolean.valueOf(paramBoolean);
    configurePrettyPrint();
  }

  public void setModelKey(String paramString)
  {
    this.modelKeys = Collections.singleton(paramString);
  }

  public void setModelKeys(Set<String> paramSet)
  {
    this.modelKeys = paramSet;
  }

  public Set<String> getModelKeys()
  {
    return this.modelKeys;
  }

  @Deprecated
  public void setRenderedAttributes(Set<String> paramSet)
  {
    this.modelKeys = paramSet;
  }

  @Deprecated
  public Set<String> getRenderedAttributes()
  {
    return this.modelKeys;
  }

  public void setExtractValueFromSingleKeyModel(boolean paramBoolean)
  {
    this.extractValueFromSingleKeyModel = paramBoolean;
  }

  public void setDisableCaching(boolean paramBoolean)
  {
    this.disableCaching = paramBoolean;
  }

  public void setUpdateContentLength(boolean paramBoolean)
  {
    this.updateContentLength = paramBoolean;
  }

  protected void prepareResponse(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
  {
    setResponseContentType(paramHttpServletRequest, paramHttpServletResponse);
    paramHttpServletResponse.setCharacterEncoding(this.encoding.getJavaName());
    if (this.disableCaching)
    {
      paramHttpServletResponse.addHeader("Pragma", "no-cache");
      paramHttpServletResponse.addHeader("Cache-Control", "no-cache, no-store, max-age=0");
      paramHttpServletResponse.addDateHeader("Expires", 1L);
    }
  }

  protected void renderMergedOutputModel(Map<String, Object> paramMap, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
    throws Exception
  {
		ServletOutputStream localServletOutputStream = this.updateContentLength ? createTemporaryOutputStream() : paramHttpServletResponse.getOutputStream();
    Object localObject = filterModel(paramMap);
    JsonGenerator localJsonGenerator = this.objectMapper.getJsonFactory().createJsonGenerator(localServletOutputStream, this.encoding);
    if (this.objectMapper.getSerializationConfig().isEnabled(SerializationConfig.Feature.INDENT_OUTPUT))
      localJsonGenerator.useDefaultPrettyPrinter();
    if (this.prefixJson)
      localJsonGenerator.writeRaw("{} && ");
    if ((localObject instanceof RException))
    {
      JsonFilter localJsonFilter = (JsonFilter)AnnotationUtils.findAnnotation(localObject.getClass(), JsonFilter.class);
      if ((localJsonFilter != null) && (localJsonFilter.value().equals("RException")))
      {
        SimpleFilterProvider localSimpleFilterProvider = new SimpleFilterProvider().addFilter(localJsonFilter.value(), SimpleBeanPropertyFilter.filterOutAllExcept(new String[] { "code", "desc" }));
        this.objectMapper.setFilters(localSimpleFilterProvider);
      }
    }
    this.objectMapper.writeValue(localJsonGenerator, localObject);
    if (this.updateContentLength)
      writeToResponse(paramHttpServletResponse, (ByteArrayOutputStream)localServletOutputStream);
  }

  protected Object filterModel(Map<String, Object> paramMap)
  {
    HashMap localHashMap = new HashMap(paramMap.size());
    Set localSet = !CollectionUtils.isEmpty(this.modelKeys) ? this.modelKeys : paramMap.keySet();
    Iterator localIterator = paramMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      if ((!(localEntry.getValue() instanceof BindingResult)) && (localSet.contains(localEntry.getKey())))
        localHashMap.put(localEntry.getKey(), localEntry.getValue());
    }
    return (this.extractValueFromSingleKeyModel) && (localHashMap.size() == 1) ? localHashMap.values().iterator().next() : localHashMap;
  }
}