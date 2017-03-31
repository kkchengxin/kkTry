package com.kk.rainbow.core.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.annotate.JsonFilter;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.springframework.core.annotation.AnnotationUtils;

import com.kk.rainbow.core.exception.RException;
import com.kk.rainbow.core.mybatis3.Page;
import com.kk.rainbow.core.util.StringUtil;

public class JSONUtils {
	public static final JsonNode toStringAsJson(String paramString)
			throws RException {
		try {
			JsonNode localJsonNode = JacksonFactory.getInstance()
					.getObjectMapper().readTree(paramString);
			return localJsonNode;
		} catch (JsonProcessingException localJsonProcessingException) {
			throw new RException("-1",
					localJsonProcessingException.getMessage());
		} catch (IOException localIOException) {
			throw new RException("-1", localIOException.getMessage());
		} finally {
			paramString = null;
		}
	}

	public static final String toJsonAsString(Object paramObject) {
		try {
			String str = JacksonFactory.getInstance().getObjectMapper()
					.writeValueAsString(paramObject);
			return str;
		} catch (JsonGenerationException localJsonGenerationException) {
			localJsonGenerationException.printStackTrace();
		} catch (JsonMappingException localJsonMappingException) {
			localJsonMappingException.printStackTrace();
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		} finally {
			paramObject = null;
		}
		return null;
	}

	public static final String toJsonAsString(Object paramObject,
			String[] paramArrayOfString) {
		try {
			SimpleFilterProvider localSimpleFilterProvider = new SimpleFilterProvider()
					.addFilter(((JsonFilter) AnnotationUtils.findAnnotation(
							paramObject.getClass(), JsonFilter.class)).value(),
							SimpleBeanPropertyFilter
									.filterOutAllExcept(paramArrayOfString));
			if (localSimpleFilterProvider != null)
				JacksonFactory.getInstance().getObjectMapper()
						.setFilters(localSimpleFilterProvider);
			String str = JacksonFactory.getInstance().getObjectMapper()
					.writeValueAsString(paramObject);
			return str;
		} catch (JsonGenerationException localJsonGenerationException) {
			localJsonGenerationException.printStackTrace();
		} catch (JsonMappingException localJsonMappingException) {
			localJsonMappingException.printStackTrace();
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		} finally {
			paramObject = null;
		}
		return null;
	}

	public static final String toJsonAsString(Object paramObject,
			Set<String> paramSet) {
		try {
			SimpleFilterProvider localSimpleFilterProvider = new SimpleFilterProvider()
					.addFilter(((JsonFilter) AnnotationUtils.findAnnotation(
							paramObject.getClass(), JsonFilter.class)).value(),
							SimpleBeanPropertyFilter
									.filterOutAllExcept(paramSet));
			if (localSimpleFilterProvider != null)
				JacksonFactory.getInstance().getObjectMapper()
						.setFilters(localSimpleFilterProvider);
			String str = JacksonFactory.getInstance().getObjectMapper()
					.writeValueAsString(paramObject);
			return str;
		} catch (JsonGenerationException localJsonGenerationException) {
			localJsonGenerationException.printStackTrace();
		} catch (JsonMappingException localJsonMappingException) {
			localJsonMappingException.printStackTrace();
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		} finally {
			paramObject = null;
		}
		return null;
	}

	public static final <T> T toObject(String paramString, Class<T> paramClass) {
		if (StringUtil.isEmpty(paramString))
			return null;
		try {
			return JacksonFactory.getInstance().getObjectMapper()
					.readValue(paramString, paramClass);
		} catch (JsonParseException localJsonParseException) {
			localJsonParseException.printStackTrace();
		} catch (JsonMappingException localJsonMappingException) {
			localJsonMappingException.printStackTrace();
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		}
		return null;
	}

	public static final <T> T toObject(JsonNode paramJsonNode,
			Class<T> paramClass) {
		if (paramJsonNode == null)
			return null;
		try {
			return JacksonFactory.getInstance().getObjectMapper()
					.readValue(paramJsonNode, paramClass);
		} catch (JsonParseException localJsonParseException) {
			localJsonParseException.printStackTrace();
		} catch (JsonMappingException localJsonMappingException) {
			localJsonMappingException.printStackTrace();
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		}
		return null;
	}

	public static final boolean isSuccess(JsonNode paramJsonNode)
			throws RException {
		if (paramJsonNode == null)
			return false;
		int i = getCode(paramJsonNode);
		return i > 0;
	}

	public static final int getCode(JsonNode paramJsonNode) throws RException {
		if (paramJsonNode == null)
			return -1000;
		JsonNode localJsonNode = paramJsonNode.get(JSONKeys.CODE.getValue());
		if (localJsonNode == null)
			throw new RException("-1", new StringBuilder().append("缺少节点:")
					.append(JSONKeys.CODE.getValue()).toString());
		return Integer.parseInt(localJsonNode.getTextValue());
	}

	public static final String getDesc(JsonNode paramJsonNode)
			throws RException {
		if (paramJsonNode == null)
			return null;
		JsonNode localJsonNode = paramJsonNode.get(JSONKeys.DESC.getValue());
		if (localJsonNode == null)
			throw new RException("-1", new StringBuilder().append("缺少节点:")
					.append(JSONKeys.DESC.getValue()).toString());
		return localJsonNode.getTextValue();
	}

	public static final JsonNode getData(JsonNode paramJsonNode)
			throws RException {
		if (paramJsonNode == null)
			return null;
		JsonNode localJsonNode = paramJsonNode.get(JSONKeys.DATA.getValue());
		if (localJsonNode == null)
			throw new RException("-1", new StringBuilder().append("缺少节点:")
					.append(JSONKeys.DATA.getValue()).toString());
		return localJsonNode;
	}

	public static final Page getPage(JsonNode paramJsonNode) throws RException {
		if (paramJsonNode == null)
			return null;
		JsonNode localJsonNode1 = getData(paramJsonNode);
		if (localJsonNode1 == null)
			return null;
		JsonNode localJsonNode2 = localJsonNode1.get(JSONKeys.PAGE.getValue());
		return (Page) toObject(localJsonNode2, Page.class);
	}

	public static final <T> T getArray(JsonNode paramJsonNode,
			Class<T> paramClass) throws RException {
		if (paramJsonNode == null)
			return null;
		JsonNode localJsonNode1 = getData(paramJsonNode);
		if (localJsonNode1 == null)
			return null;
		JsonNode localJsonNode2 = localJsonNode1.get(JSONKeys.LIST.getValue());
		return toObject(localJsonNode2, paramClass);
	}

	public static final <T> T getData(JsonNode paramJsonNode,
			Class<T> paramClass) throws RException {
		if (paramJsonNode == null)
			return null;
		JsonNode localJsonNode = getData(paramJsonNode);
		return toObject(localJsonNode, paramClass);
	}

	public static final <T> T getObject(JsonNode paramJsonNode,
			String paramString, Class<T> paramClass) throws RException {
		if ((paramJsonNode == null) || (paramString == null))
			return null;
		JsonNode localJsonNode = paramJsonNode.get(paramString);
		if (localJsonNode == null)
			throw new RException("-1", new StringBuilder().append("缺少节点:")
					.append(paramString).toString());
		return toObject(localJsonNode, paramClass);
	}

	public static String format(String paramString1, String paramString2) {
		if ((paramString1 == null) || (paramString1.trim().length() == 0))
			return null;
		int i = 0;
		ArrayList localArrayList = new ArrayList();
		String str1 = paramString1;
		String str2;
		while (str1.length() > 0) {
			str2 = getToken(str1);
			str1 = str1.substring(str2.length());
			str2 = str2.trim();
			localArrayList.add(str2);
		}
		for (int j = 0; j < localArrayList.size(); j++) {
			str2 = (String) localArrayList.get(j);
			int m = str2.getBytes().length;
			if ((m > i) && (j < localArrayList.size() - 1)
					&& (((String) localArrayList.get(j + 1)).equals(":")))
				i = m;
		}
		StringBuilder localStringBuilder = new StringBuilder();
		int k = 0;
		for (int m = 0; m < localArrayList.size(); m++) {
			String str3 = (String) localArrayList.get(m);
			if (str3.equals(",")) {
				localStringBuilder.append(str3);
				doFill(localStringBuilder, k, paramString2);
			} else if (str3.equals(":")) {
				localStringBuilder.append(" ").append(str3).append(" ");
			} else {
				String str4;
				if (str3.equals("{")) {
					str4 = (String) localArrayList.get(m + 1);
					if (str4.equals("}")) {
						m++;
						localStringBuilder.append("{ }");
					} else {
						k++;
						localStringBuilder.append(str3);
						doFill(localStringBuilder, k, paramString2);
					}
				} else if (str3.equals("}")) {
					k--;
					doFill(localStringBuilder, k, paramString2);
					localStringBuilder.append(str3);
				} else if (str3.equals("[")) {
					str4 = (String) localArrayList.get(m + 1);
					if (str4.equals("]")) {
						m++;
						localStringBuilder.append("[ ]");
					} else {
						k++;
						localStringBuilder.append(str3);
						doFill(localStringBuilder, k, paramString2);
					}
				} else if (str3.equals("]")) {
					k--;
					doFill(localStringBuilder, k, paramString2);
					localStringBuilder.append(str3);
				} else {
					localStringBuilder.append(str3);
					if ((m < localArrayList.size() - 1)
							&& (((String) localArrayList.get(m + 1))
									.equals(":"))) {
						int n = i - str3.getBytes().length;
						if (n > 0)
							for (int i1 = 0; i1 < n; i1++)
								localStringBuilder.append(" ");
					}
				}
			}
		}
		return localStringBuilder.toString();
	}

	private static String getToken(String paramString) {
		StringBuilder localStringBuilder = new StringBuilder();
		int i = 0;
		while (paramString.length() > 0) {
			String str = paramString.substring(0, 1);
			paramString = paramString.substring(1);
			if ((i == 0)
					&& ((str.equals(":")) || (str.equals("{"))
							|| (str.equals("}")) || (str.equals("["))
							|| (str.equals("]")) || (str.equals(",")))) {
				if (localStringBuilder.toString().trim().length() != 0)
					break;
				localStringBuilder.append(str);
				break;
			}
			if (str.equals("\\")) {
				localStringBuilder.append(str);
				localStringBuilder.append(paramString.substring(0, 1));
				paramString = paramString.substring(1);
			} else if (str.equals("\"")) {
				localStringBuilder.append(str);
				if (i != 0)
					break;
				i = 1;
			} else {
				localStringBuilder.append(str);
			}
		}
		return localStringBuilder.toString();
	}

	private static void doFill(StringBuilder paramStringBuilder, int paramInt,
			String paramString) {
		paramStringBuilder.append("\n");
		for (int i = 0; i < paramInt; i++)
			paramStringBuilder.append(paramString);
	}
}