package Microservices.Enrollment_Service.handler;

import java.time.LocalTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import Microservices.Enrollment_Service.Dto.ErrorIDGenerator;
import Microservices.Enrollment_Service.Dto.ExceptionResponse;
import Microservices.Enrollment_Service.exception.DuplicateEntryException;
import Microservices.Enrollment_Service.exception.ErrorCodes;
import Microservices.Enrollment_Service.exception.UnAuthorizedUserException;
import Microservices.Enrollment_Service.exception.ValidationException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ExceptionResponse> handleValidationException(ValidationException exception) {
		ExceptionResponse response=new ExceptionResponse();
		response.setErrorid(ErrorIDGenerator.getErrorId());
		response.setErrorcode(exception.getErrorcode());
		response.setMessage(exception.getMessage());
		response.setStatus(exception.getStatus());
		response.setTimestamp(LocalTime.now().toString());
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(DuplicateEntryException.class)
	public ResponseEntity<ExceptionResponse> handleDuplicateEntryException(DuplicateEntryException exception) {
		ExceptionResponse response=new ExceptionResponse();
		response.setErrorid(ErrorIDGenerator.getErrorId());
		response.setErrorcode(exception.getErrorcode());
		response.setMessage(exception.getMessage());
		response.setStatus(exception.getStatus());
		response.setTimestamp(LocalTime.now().toString());
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UnAuthorizedUserException.class)
	public ResponseEntity<ExceptionResponse> handleUnAUthorizedUserException(UnAuthorizedUserException exception) {
		ExceptionResponse response=new ExceptionResponse();
		response.setErrorid(ErrorIDGenerator.getErrorId());
		response.setErrorcode(exception.getErrorcode());
		response.setMessage(exception.getMessage());
		response.setStatus(exception.getStatus());
		response.setTimestamp(LocalTime.now().toString());
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<ExceptionResponse> handleRateLimitExceeded(RequestNotPermitted ex) {
		ExceptionResponse response=new ExceptionResponse();
		response.setErrorid(ErrorIDGenerator.getErrorId());
		response.setErrorcode(ErrorCodes.RATELIMIT_EXCEEDED.getErrorCode());
		response.setMessage(ErrorCodes.RATELIMIT_EXCEEDED.getErrorMessage());
		response.setStatus(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
		response.setTimestamp(LocalTime.now().toString());
		return new ResponseEntity<>(response,HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
    }
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGenericException(Exception ex){
		ExceptionResponse response=new ExceptionResponse();
		response.setErrorid(ErrorIDGenerator.getErrorId());
		response.setErrorcode(500);
		response.setMessage(ex.getMessage());
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		response.setTimestamp(LocalTime.now().toString());
		return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
