package Microservices.Auth_Server.Dto;

public class EmailMessageDto {
    private String email;
    private String message;
    private long memId;
    private int code;

    public EmailMessageDto() {
    }

    public EmailMessageDto(String email, String message, long memId, int code) {
        this.email = email;
        this.message = message;
        this.memId = memId;
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getMemId() {
        return memId;
    }

    public void setMemId(long memId) {
        this.memId = memId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "MessageDto{" +
                "email='" + email + '\'' +
                ", message='" + message + '\'' +
                ", memId=" + memId +
                ", code=" + code +
                '}';
    }
}
