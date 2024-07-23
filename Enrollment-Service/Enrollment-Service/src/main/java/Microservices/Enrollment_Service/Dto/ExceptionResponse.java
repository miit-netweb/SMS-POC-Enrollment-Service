package Microservices.Enrollment_Service.Dto;

import org.springframework.http.HttpStatus;

public class ExceptionResponse {

	private String errorid;
	private int errorcode;
	private String message;
	private HttpStatus status;
	private String timestamp;
	
	
	public ExceptionResponse() {
		super();
	}
	public ExceptionResponse(String errorid, int errorcode, String message, HttpStatus status, String timestamp) {
		super();
		this.errorid = errorid;
		this.errorcode = errorcode;
		this.message = message;
		this.status = status;
		this.timestamp = timestamp;
	}
	public String getErrorid() {
		return errorid;
	}
	public void setErrorid(String errorid) {
		this.errorid = errorid;
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
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
