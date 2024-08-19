package Microservices.Enrollment_Service.Dto;

import jakarta.validation.constraints.NotNull;

public class SubscriberDto {

    @NotNull
    private EnrollmentDetail enrollmentDetail;

    public SubscriberDto() {
    }
    public EnrollmentDetail getEnrollmentDetail() {
        return enrollmentDetail;
    }

    public void setEnrollmentDetail(EnrollmentDetail enrollmentDetail) {
        this.enrollmentDetail = enrollmentDetail;
    }

    @Override
    public String toString() {
        return "SubscriberDto{" +
                ", enrollmentDetail=" + enrollmentDetail +
                '}';
    }
}
