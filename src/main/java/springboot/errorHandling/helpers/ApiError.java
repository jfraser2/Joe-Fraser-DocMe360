package springboot.errorHandling.helpers;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

//import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ApiError
{
	private HttpStatus status;
	   
	@JsonSerialize(using = ZonedDateTimeConverter.class)
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private ZonedDateTime timestamp;
	   
	private String message;
	private String debugMessage;
	@JsonSerialize(using = ListApiValidationErrorConverter.class)
	private List<ApiValidationError> subErrors;

	public ApiError() {
	    Instant instant = Instant.now(); // Current instant from London(Greenwich)
	    ZoneId zoneId = ZoneId.of("America/Chicago");
	    ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);
		setTimestamp(zonedDateTime);
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDebugMessage() {
		return debugMessage;
	}

	public void setDebugMessage(String debugMessage) {
		this.debugMessage = debugMessage;
	}

	public List<ApiValidationError> getSubErrors() {
		return subErrors;
	}

	public void setSubErrors(List<ApiValidationError> subErrors) {
		this.subErrors = subErrors;
	}

	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(ZonedDateTime timestamp) {
		this.timestamp = timestamp;
	}
	   
}
