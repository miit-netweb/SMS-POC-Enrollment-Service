package Microservices.Enrollment_Service.Entity;

import jakarta.persistence.*;

@Entity
public class BillingPending {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String subscriberNumber;
    private Long partnerNumber;
    private Long subtypeNumber;
    private String pricingRoutine;
    private String status;



    public BillingPending() {
    }

    public BillingPending(String subscriberNumber, Long partnerNumber, Long subtypeNumber, String pricingRoutine, String status) {
        this.subscriberNumber = subscriberNumber;
        this.partnerNumber = partnerNumber;
        this.subtypeNumber = subtypeNumber;
        this.pricingRoutine = pricingRoutine;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubscriberNumber() {
        return subscriberNumber;
    }

    public void setSubscriberNumber(String subscriberNumber) {
        this.subscriberNumber = subscriberNumber;
    }

    public Long getPartnerNumber() {
        return partnerNumber;
    }

    public void setPartnerNumber(Long partnerNumber) {
        this.partnerNumber = partnerNumber;
    }

    public Long getSubtypeNumber() {
        return subtypeNumber;
    }

    public void setSubtypeNumber(Long subtypeNumber) {
        this.subtypeNumber = subtypeNumber;
    }

    public String getPricingRoutine() {
        return pricingRoutine;
    }

    public void setPricingRoutine(String pricingRoutine) {
        this.pricingRoutine = pricingRoutine;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
