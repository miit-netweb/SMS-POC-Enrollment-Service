package Microservices.Auth_Server.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends RuntimeException {

	private int errorcode;
	private String message;
	private HttpStatus status;

	public ValidationException(int errorcode, String message, HttpStatus status) {
		super();
		this.errorcode = errorcode;
		this.message = message;
		this.status = status;
	}

	public int getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
}
