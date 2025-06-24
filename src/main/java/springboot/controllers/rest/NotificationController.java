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

//import io.swagger.annotations.ApiImplicitParam;
import springboot.autowire.helpers.StringBuilderContainer;
import springboot.autowire.helpers.ValidationErrorContainer;
import springboot.dto.request.CreateNotification;
import springboot.dto.request.GetById;
import springboot.dto.response.NonModelAdditionalFields;
import springboot.dto.validation.exceptions.BuildNotificationException;
import springboot.dto.validation.exceptions.DatabaseRowNotFoundException;
import springboot.dto.validation.exceptions.RequestValidationException;
import springboot.entities.NotificationEntity;
import springboot.errorHandling.helpers.ApiValidationError;
import springboot.services.interfaces.Notification;
import springboot.services.validation.request.RequestValidationService;

@RestController
@RequestMapping(path="/rest/api")
public class NotificationController
	extends ControllerBase
{
	@Autowired
	private Notification notificationService;
	
	@Autowired
	@Qualifier("requestValidationErrorsContainer")
	private ValidationErrorContainer requestValidationErrorsContainer;
	
	@Autowired
	@Qualifier("requestStringBuilderContainer")
	private StringBuilderContainer requestStringBuilderContainer;
	
	@RequestMapping(method = {RequestMethod.POST},
			path = "/v1/createNotification",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Object> createNotification(@RequestBody CreateNotification data,
		HttpServletRequest request,  @Autowired RequestValidationService<CreateNotification> createNotificationValidation)
		throws RequestValidationException, DatabaseRowNotFoundException, AccessDeniedException
	{
		
		// single field validation
		createNotificationValidation.validateRequest(data, requestValidationErrorsContainer, null);
		List<ApiValidationError> errorList = requestValidationErrorsContainer.getValidationErrorList();
		
		if (errorList.size() > 0)
		{
//			System.out.println("Right before the throw");
			throw new RequestValidationException(errorList);
		}
		
		// multiple field validation
		if(!notificationService.validateTemplateFields(data)) {
			List<ApiValidationError> templateFieldsError = notificationService.generateTemplateFieldsError(data);
			throw new RequestValidationException(templateFieldsError);
		}
		
		NotificationEntity ne = null;
		try {
			ne = notificationService.buildNotificationEntity(data);
		} catch (BuildNotificationException bne) {
			throw new DatabaseRowNotFoundException(bne.getMessage());
		}
		
		NotificationEntity savedEntity = notificationService.persistData(ne);
		
		String jsonString = goodResponse(savedEntity, requestStringBuilderContainer, null);
		ne = null;
		savedEntity = null;
		
		// support CORS
		HttpHeaders aResponseHeader = createResponseHeader(request);
		
		// 201 response
		return new ResponseEntity<>(jsonString, aResponseHeader, HttpStatus.CREATED);
	}
	
	@RequestMapping(method = {RequestMethod.GET},
			path = "/v1/all/notifications",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Object> allNotifications(HttpServletRequest request)
		throws DatabaseRowNotFoundException, AccessDeniedException
	{
		
		List<NotificationEntity> aList = notificationService.findAll();
		boolean isEmpty = true;
		if(null != aList && aList.size() > 0) {
			isEmpty = false;
		}
		
		if(isEmpty) {
			throw new DatabaseRowNotFoundException("Notification Table is empty.");
		}
		
		List<Object> objectList = new ArrayList<Object>(aList);
		String jsonString = goodResponseList(objectList, requestStringBuilderContainer);
		
		// support CORS
		HttpHeaders aResponseHeader = createResponseHeader(request);
		
		return new ResponseEntity<>(jsonString, aResponseHeader, HttpStatus.OK);
	}
	
	@RequestMapping(method = {RequestMethod.GET},
			path = "/v1/findByNotificationId",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Object> findByNotificationId(@RequestParam(required = true) String notificationId,
		HttpServletRequest request, @Autowired RequestValidationService<GetById> getByIdValidation)
		throws RequestValidationException, DatabaseRowNotFoundException, AccessDeniedException
	{
		
		GetById data = new GetById(notificationId);
		getByIdValidation.validateRequest(data, requestValidationErrorsContainer, null);
		List<ApiValidationError> errorList = requestValidationErrorsContainer.getValidationErrorList();
		
		if (errorList.size() > 0)
		{
//			System.out.println("Right before the throw");
			throw new RequestValidationException(errorList);
		}
		
		Long tempId = Long.valueOf(notificationId);
		NotificationEntity record = notificationService.findById(tempId);
		if(null == record) {
			throw new DatabaseRowNotFoundException("The Notification for Id: " + notificationId + " does not exist.");
		}
		
		String jsonString = null;
		String substitutedText = notificationService.generatePersonalization(record);
		if (null != substitutedText && substitutedText.length() > 0) {
			NonModelAdditionalFields nonModelAdditionalFields = new NonModelAdditionalFields();
			nonModelAdditionalFields.setContent(substitutedText);
			jsonString = goodResponse(record, requestStringBuilderContainer, nonModelAdditionalFields);			
		} else {
			jsonString = goodResponse(record, requestStringBuilderContainer, null);			
		}
		
		record = null;
		
		// support CORS
		HttpHeaders aResponseHeader = createResponseHeader(request);
		
		return new ResponseEntity<>(jsonString, aResponseHeader, HttpStatus.OK);
	}
	
	
}
