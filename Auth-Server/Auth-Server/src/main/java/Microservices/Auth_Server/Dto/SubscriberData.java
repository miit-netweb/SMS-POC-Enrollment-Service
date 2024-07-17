package Microservices.Auth_Server.Dto;

public class SubscriberData {

    private String firstName;
    private String lastName;
    private Long phoneNumber;
    private String email;
    private BillingDetail billingDetail;

    public SubscriberData() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BillingDetail getBillingDetail() {
        return billingDetail;
    }

    public void setBillingDetail(BillingDetail billingDetail) {
        this.billingDetail = billingDetail;
    }

    @Override
    public String toString() {
        return "SubscriberData{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", billingDetail=" + billingDetail +
                '}';
    }
}
