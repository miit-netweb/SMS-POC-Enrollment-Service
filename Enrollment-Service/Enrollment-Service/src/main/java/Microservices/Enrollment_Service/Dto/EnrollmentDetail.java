package Microservices.Enrollment_Service.Dto;

public class EnrollmentDetail {
    private long partnerNumber;
    private SubscriberData subscriberData;
    private SubscriptionData subscriptionData;

    public SubscriptionData getSubscriptionData() {
		return subscriptionData;
	}

	public void setSubscriptionData(SubscriptionData subscriptionData) {
		this.subscriptionData = subscriptionData;
	}

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
