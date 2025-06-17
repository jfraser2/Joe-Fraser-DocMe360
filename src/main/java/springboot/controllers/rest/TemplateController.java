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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import springboot.autowire.helpers.StringBuilderContainer;
import springboot.autowire.helpers.ValidationErrorContainer;
import springboot.dto.request.CreateTemplate;
import springboot.dto.request.GetById;
import springboot.dto.request.UpdateTemplate;
import springboot.dto.validation.exceptions.DatabaseRowNotFoundException;
import springboot.dto.validation.exceptions.RequestValidationException;
import springboot.entities.TemplateEntity;
import springboot.errorHandling.helpers.ApiValidationError;
import springboot.services.interfaces.Template;
import springboot.services.interfaces.RequestValidation;

@RestController
@RequestMapping(path="/rest/api")
public class TemplateController
	extends ControllerBase
{
	@Autowired
	private Template templateService;
	
	@Autowired
	private RequestValidation<CreateTemplate> createTemplateValidation;
	
	@Autowired
	private RequestValidation<UpdateTemplate> updateTemplateValidation;
	
	@Autowired
	private RequestValidation<GetById> getByIdValidation;
	
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
		
		String jsonString = goodResponse(savedEntity, requestStringBuilderContainer, null);
		te = null;
		savedEntity = null;
		
		// support CORS
		HttpHeaders aResponseHeader = createResponseHeader(request);
		
		// 201 response
		return new ResponseEntity<>(jsonString, aResponseHeader, HttpStatus.CREATED);
	}
	
	@RequestMapping(method = {RequestMethod.GET},
			path = "/v1/all/templates",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Object> allTemplates(HttpServletRequest request)
		throws DatabaseRowNotFoundException, AccessDeniedException
	{
		
		List<TemplateEntity> aList = templateService.findAll();
		boolean isEmpty = true;
		if(null != aList && aList.size() > 0) {
			isEmpty = false;
		}
		
		if (isEmpty) {
			throw new DatabaseRowNotFoundException("Template Table is empty.");
		}
		
		List<Object> objectList = new ArrayList<Object>(aList);
		String jsonString = goodResponseList(objectList, requestStringBuilderContainer);
		
		// support CORS
		HttpHeaders aResponseHeader = createResponseHeader(request);
		
		return new ResponseEntity<>(jsonString, aResponseHeader, HttpStatus.OK);
	}
	
	@RequestMapping(method = {RequestMethod.GET},
			path = "/v1/findByTemplateId",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Object> findByTemplateId(@RequestParam(required = true) String templateId, HttpServletRequest request)
		throws RequestValidationException, DatabaseRowNotFoundException, AccessDeniedException
	{
		
		GetById data = new GetById(templateId);
		getByIdValidation.validateRequest(data, requestValidationErrorsContainer, null);
		List<ApiValidationError> errorList = requestValidationErrorsContainer.getValidationErrorList();
		
		if (errorList.size() > 0)
		{
//			System.out.println("Right before the throw");
			throw new RequestValidationException(errorList);
		}
		
		Long tempId = Long.valueOf(templateId);
		TemplateEntity record = templateService.findById(tempId);
		if(null == record) {
			throw new DatabaseRowNotFoundException("The Template for Id: " + templateId + " does not exist.");
		}
		
		String jsonString = goodResponse(record, requestStringBuilderContainer, null);
		record = null;
		
		// support CORS
		HttpHeaders aResponseHeader = createResponseHeader(request);
		
		return new ResponseEntity<>(jsonString, aResponseHeader, HttpStatus.OK);
	}
	
	@RequestMapping(method = {RequestMethod.PATCH},
			path = "/v1/updateTemplate",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Object> updateTemplate(@RequestBody UpdateTemplate data, HttpServletRequest request)
		throws RequestValidationException, DatabaseRowNotFoundException, AccessDeniedException
	{
		
		// single field validation
		updateTemplateValidation.validateRequest(data, requestValidationErrorsContainer, null);
		List<ApiValidationError> errorList = requestValidationErrorsContainer.getValidationErrorList();
		
		if (errorList.size() > 0)
		{
//			System.out.println("Right before the throw");
			throw new RequestValidationException(errorList);
		}
		
		Long tempId = Long.valueOf(data.getTemplateId());
		TemplateEntity record = templateService.findById(tempId);
		if(null == record) {
			throw new DatabaseRowNotFoundException("The Template for Id: " + tempId.toString() + " does not exist.");
		}
		
		record.setBody(data.getNewTemplateText());
		TemplateEntity updatedEntity = templateService.persistData(record);
		
		
		String jsonString = goodResponse(updatedEntity, requestStringBuilderContainer, null);
		record = null;
		updatedEntity = null;
		
		// support CORS
		HttpHeaders aResponseHeader = createResponseHeader(request);
		
		return new ResponseEntity<>(jsonString, aResponseHeader, HttpStatus.OK);
	}

}
