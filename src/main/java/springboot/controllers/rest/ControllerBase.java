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
import springboot.dto.response.NonModelAdditionalFields;
import springboot.enums.MapperEnum;

public abstract class ControllerBase
{
	protected static final String GODD_RESPONSE_PREFIX = "{\"status\":\"OK\", \"modelData\":";
	protected static final String GODD_RESPONSE_SUFFIX = "}";
	protected static final String EOL = System.getProperty("line.separator");
	protected static final String JSON_FIELD_SEPARATOR = ",";
	
	private String removeObjectBeginAndEnd(String objectString) {
		
		String retVar = null;
		
		if (null != objectString && objectString.length() > 1) {
			retVar = objectString.substring(1, objectString.length() - 1);
		}
		
		return retVar;
	}
	
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
	
	protected String goodResponse(Object anObject, StringBuilderContainer aContainer, NonModelAdditionalFields nonModelAdditionalFields)
	{
		String jsonString = convertToJson(anObject);
		
		// Since it is Autowired clear the buffer before you use it
		aContainer.clearStringBuffer();
		StringBuilder aBuilder = aContainer.getStringBuilder();
		
		aBuilder.append(GODD_RESPONSE_PREFIX);
		aBuilder.append(jsonString);
		if (null != nonModelAdditionalFields) {
			String tempJson = convertToJson(nonModelAdditionalFields);
			String fixedObjectJson = removeObjectBeginAndEnd(tempJson);
			if (null != fixedObjectJson) {
				aBuilder.append(JSON_FIELD_SEPARATOR); // a comma
				aBuilder.append(fixedObjectJson);
			}
			
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
