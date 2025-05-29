package springboot.controllers.rest;

import java.nio.file.AccessDeniedException;
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

//import io.swagger.annotations.ApiImplicitParam;
import springboot.autowire.helpers.StringBuilderContainer;
import springboot.autowire.helpers.ValidationErrorContainer;
import springboot.dto.request.CreateNotification;
import springboot.dto.validation.exceptions.RequestValidationException;
import springboot.entities.NotificationEntity;
import springboot.errorHandling.helpers.ApiValidationError;
import springboot.services.interfaces.Notification;
import springboot.services.interfaces.RequestValidation;

@RestController
@RequestMapping(path="/actions")
public class NotificationController
	extends ControllerBase
{
	@Autowired
	private Notification notificationService;
	
	@Autowired
	private RequestValidation<CreateNotification> createNotificationValidation;
	
	@Autowired
	@Qualifier("requestValidationErrorsContainer")
	private ValidationErrorContainer requestValidationErrorsContainer;
	
	@Autowired
	@Qualifier("requestStringBuilderContainer")
	private StringBuilderContainer requestStringBuilderContainer;
	
	@RequestMapping(method = {RequestMethod.POST},
			path = "/v1/create",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Object> create(@RequestBody CreateNotification data, HttpServletRequest request)
		throws RequestValidationException, IllegalArgumentException, AccessDeniedException
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
		
		NotificationEntity ne = notificationService.buildNotificationEntity(data);
		NotificationEntity savedEntity = notificationService.persistData(ne);
		
		String jsonString = goodResponse(savedEntity, requestStringBuilderContainer);
		ne = null;
		savedEntity = null;
		
		// support CORS
		HttpHeaders aResponseHeader = createResponseHeader();
		
		// 201 response
		return new ResponseEntity<>(jsonString, aResponseHeader, HttpStatus.CREATED);
	}
	
	@RequestMapping(method = {RequestMethod.GET},
			path = "/v1/all",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Object> all(HttpServletRequest request)
		throws RequestValidationException, IllegalArgumentException, AccessDeniedException
	{
		
		List<NotificationEntity> aList = notificationService.findAll();
		if(null == aList) {
			throw new IllegalArgumentException("Notification Table is empty.");
		}
		
		String jsonString = goodResponse(aList, requestStringBuilderContainer);
		
		// support CORS
		HttpHeaders aResponseHeader = createResponseHeader();
		
		return new ResponseEntity<>(jsonString, aResponseHeader, HttpStatus.OK);
	}
	
}
