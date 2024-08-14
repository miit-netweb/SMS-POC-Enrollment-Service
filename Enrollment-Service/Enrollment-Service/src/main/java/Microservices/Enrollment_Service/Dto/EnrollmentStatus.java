package Microservices.Enrollment_Service.Dto;

import java.time.LocalDate;

public class EnrollmentStatus {
    private String timeStamp;
    private String status;

    public EnrollmentStatus() {
    }

    public EnrollmentStatus(String timeStamp, String status) {
        this.timeStamp = timeStamp;
        this.status = status;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return "EmailStatus{" +
                "timeStamp=" + timeStamp +
                ", status='" + status + '\'' +
                '}';
    }
}
