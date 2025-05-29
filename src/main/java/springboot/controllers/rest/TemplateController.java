package springboot.controllers.rest;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import springboot.autowire.helpers.StringBuilderContainer;
import springboot.autowire.helpers.ValidationErrorContainer;
import springboot.dto.request.CreateTemplate;
import springboot.dto.validation.exceptions.RequestValidationException;
import springboot.entities.TemplateEntity;
import springboot.errorHandling.helpers.ApiValidationError;
import springboot.services.interfaces.Template;
import springboot.services.interfaces.RequestValidation;

@RestController
@RequestMapping(path="/actions")
public class TemplateController
	extends ControllerBase
{
	@Autowired
	private Template templateService;
	
	@Autowired
	private RequestValidation<CreateTemplate> createTemplateValidation;
	
	@Autowired
	@Qualifier("requestValidationErrorsContainer")
	private ValidationErrorContainer requestValidationErrorsContainer;
	
	@Autowired
	@Qualifier("requestStringBuilderContainer")
	private StringBuilderContainer requestStringBuilderContainer;
	
	@RequestMapping(method = {RequestMethod.POST},
			path = "/v1/createTemplate",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Object> createTemplate(@RequestBody CreateTemplate data, HttpServletRequest request)
		throws RequestValidationException, IllegalArgumentException, AccessDeniedException
	{
		
		// single field validation
		createTemplateValidation.validateRequest(data, requestValidationErrorsContainer, null);
		List<ApiValidationError> errorList = requestValidationErrorsContainer.getValidationErrorList();
		
		if (errorList.size() > 0)
		{
//			System.out.println("Right before the throw");
			throw new RequestValidationException(errorList);
		}
		
		TemplateEntity te = templateService.buildTemplateEntity(data);
		TemplateEntity savedEntity = templateService.persistData(te);
		
		String jsonString = goodResponse(savedEntity, requestStringBuilderContainer);
		te = null;
		savedEntity = null;
		
		// support CORS
		HttpHeaders aResponseHeader = createResponseHeader();
		
		// 201 response
		return new ResponseEntity<>(jsonString, aResponseHeader, HttpStatus.CREATED);
	}
	
	@RequestMapping(method = {RequestMethod.GET},
			path = "/v1/all/templates",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Object> allTemplates(HttpServletRequest request)
		throws RequestValidationException, IllegalArgumentException, AccessDeniedException
	{
		
		List<TemplateEntity> aList = templateService.findAll();
		boolean isEmpty = true;
		if(null != aList && aList.size() > 0) {
			isEmpty = false;
		}
		
		if (isEmpty) {
			throw new IllegalArgumentException("Template Table is empty.");
		}
		
		List<Object> objectList = new ArrayList<Object>(aList);
		String jsonString = goodResponseList(objectList, requestStringBuilderContainer);
		
		// support CORS
		HttpHeaders aResponseHeader = createResponseHeader();
		
		return new ResponseEntity<>(jsonString, aResponseHeader, HttpStatus.OK);
	}

}
