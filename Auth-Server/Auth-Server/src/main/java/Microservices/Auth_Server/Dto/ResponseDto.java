package Microservices.Auth_Server.Dto;

public class ResponseDto {
    private int code;
    private String message;

    public ResponseDto() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
