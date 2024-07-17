package Microservices.Auth_Server.Dto;

public class EnrollmentDetail {
    private long partnerNumber;
    private SubscriberData subscriberData;

    public EnrollmentDetail() {
    }

    public long getPartnerNumber() {
        return partnerNumber;
    }

    public void setPartnerNumber(long partnerNumber) {
        this.partnerNumber = partnerNumber;
    }

    public SubscriberData getSubscriberData() {
        return subscriberData;
    }

    public void setSubscriberData(SubscriberData subscriberData) {
        this.subscriberData = subscriberData;
    }

    @Override
    public String toString() {
        return "EnrollmentDetail{" +
                "partnerNumber=" + partnerNumber +
                ", subscriberData=" + subscriberData +
                '}';
    }
}
