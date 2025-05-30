package springboot.controllers.rest;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import springboot.autowire.helpers.StringBuilderContainer;
import springboot.enums.MapperEnum;

public abstract class ControllerBase
{
	protected static final String GODD_RESPONSE_PREFIX = "{\"status\":\"OK\", \"modelData\":";
	protected static final String GODD_RESPONSE_SUFFIX = "}";
	protected static final String CONTENT_FIELD_BEGIN = "\"content\":\"";
	protected static final String CONTENT_FIELD_END = "\"";
	protected static final String EOL = System.getProperty("line.separator");
	
	private String convertListToJson(List<Object> anObjectList)
	{
		String jsonString = null;
		
		try {
			if (null != anObjectList && anObjectList.size() > 0)
			{
				Gson gson = new GsonBuilder().serializeNulls().create();
				jsonString = gson.toJson(anObjectList);
			}
		}
		catch(Exception jpe)
		{
			jsonString = null;
		}
		
		return jsonString;
		
	}
	
	private String convertToJson(Object anObject)
	{
		String jsonString = null;
		
		try {
			if (null != anObject)
			{
				ObjectMapper mapper = MapperEnum.INSTANCE.getObjectMapper();				
				ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
				
				jsonString = ow.writeValueAsString(anObject);
			}
		}
		catch(JsonProcessingException jpe)
		{
			jsonString = null;
		}
		
		return jsonString;
	}
	
	protected String goodResponse(Object anObject, StringBuilderContainer aContainer, String substitutedText)
	{
		String jsonString = convertToJson(anObject);
		
		// Since it is Autowired clear the buffer before you use it
		aContainer.clearStringBuffer();
		StringBuilder aBuilder = aContainer.getStringBuilder();
		
		aBuilder.append(GODD_RESPONSE_PREFIX);
		aBuilder.append(jsonString);
		if (null != substitutedText && substitutedText.length() > 0) {
			aBuilder.append(",");
			aBuilder.append(CONTENT_FIELD_BEGIN);
			aBuilder.append(substitutedText);
			aBuilder.append(CONTENT_FIELD_END);
		}
		aBuilder.append(GODD_RESPONSE_SUFFIX);
		
		return aBuilder.toString();
	}
	
	protected String goodResponseList(List<Object> anObject, StringBuilderContainer aContainer)
	{
		String jsonString = convertListToJson(anObject);
		
		// Since it is Autowired clear the buffer before you use it
		aContainer.clearStringBuffer();
		StringBuilder aBuilder = aContainer.getStringBuilder();
		
		aBuilder.append(GODD_RESPONSE_PREFIX);
		aBuilder.append(jsonString);
		aBuilder.append(GODD_RESPONSE_SUFFIX);
		
		return aBuilder.toString();
	}

	protected Method getMethodOfClass(Class<?> aClass, String methodName)
	{
		Method retVar = null;
		
		if (null != aClass && null != methodName)
		{
			Method [] classMethods = aClass.getMethods();
			
			for (Method method : classMethods)
			{
				if (methodName.equals(method.getName()))
				{
					retVar = method;
					break;
				}
			}
		}
		
		return retVar;
		
	}
	
	protected HttpHeaders createResponseHeader()
	{
		// support CORS
		HttpHeaders aResponseHeader = new HttpHeaders();
//		aResponseHeader.add("Access-Control-Allow-Origin", request.getHeader("Access-Control-Allow-Origin"));
//		aResponseHeader.add("Access-Control-Allow-Origin", "*");
		aResponseHeader.add("Content-Type", "application/json");
		
		return aResponseHeader;
		
	}
	
}
