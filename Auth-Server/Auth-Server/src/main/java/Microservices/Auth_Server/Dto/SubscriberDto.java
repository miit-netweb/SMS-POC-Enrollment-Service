package Microservices.Auth_Server.Dto;

import jakarta.validation.constraints.NotNull;

public class SubscriberDto {

    @NotNull
    private PartnerCredential partnerCredential;
    @NotNull
    private EnrollmentDetail enrollmentDetail;

    public SubscriberDto() {
    }

    public PartnerCredential getPartnerCredential() {
        return partnerCredential;
    }

    public void setPartnerCredential(PartnerCredential partnerCredential) {
        this.partnerCredential = partnerCredential;
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
                "partnerCredential=" + partnerCredential +
                ", enrollmentDetail=" + enrollmentDetail +
                '}';
    }
}
