package Microservices.Auth_Server.Dto;

public class EmailMessageDto {
    private String email;
    private String fname;
    private String lname;
    private long memId;
    private int code;

    public EmailMessageDto() {
    }

    public EmailMessageDto(String email, String fname, String lname, long memId, int code) {
        this.email = email;
        this.fname = fname;
        this.lname = lname;
        this.memId = memId;
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
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
                ", memId=" + memId +
                ", code=" + code +
                '}';
    }
}
