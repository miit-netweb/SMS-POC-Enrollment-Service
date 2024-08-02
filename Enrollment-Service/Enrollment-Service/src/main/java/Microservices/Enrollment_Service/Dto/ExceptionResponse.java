package Microservices.Enrollment_Service.Dto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ExceptionResponse {

	private String errorid;
	private int errorcode;
	private String message;
	private HttpStatus status;
	private String timestamp;
	
	
	public static ExceptionResponse fromJson(String json) throws Exception {
		System.out.println(json);

		Pattern pattern = Pattern.compile("\\{.*?\\}");
		Matcher matcher = pattern.matcher(json);
		System.out.println("Matcher Return: " + matcher);
		ExceptionResponse errorResponse = null;
		if (matcher.find()) {
			String jsonObject = matcher.group();
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(jsonObject);

			errorResponse = new ExceptionResponse();

			errorResponse.setErrorid(jsonNode.get("errorid").asText());
			errorResponse.setErrorcode(jsonNode.get("errorcode").asInt());
			errorResponse.setMessage(jsonNode.get("message").asText());
			errorResponse.setStatus(HttpStatus.valueOf(jsonNode.get("status").asText()));
			errorResponse.setTimestamp(jsonNode.get("timestamp").asText());
		} else {
			throw new RuntimeException("Failed while Parsing");
		}
		return errorResponse;
	}
	
	
	public static String parseErrorMessage(String errorMessage) {
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(errorMessage);
//        if (matcher.find()) {
//            return matcher.group(1);
//        }
        
        if (matcher.find()) {
            String[] parts = matcher.group(1).split(" ");
            if (parts.length > 2 && parts[0].equals("Duplicate") && parts[1].equals("entry")) {
                return parts[0] + " " + parts[1] + "" + parts[2] + "";
            }
        }
        
        return errorMessage;
	}
	
	
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
