package Microservices.Auth_Server.Dto;

public class BillingDetail {

    private String address;
    private CardDetail cardDetail;

    public BillingDetail() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public CardDetail getCardDetail() {
        return cardDetail;
    }

    public void setCardDetail(CardDetail cardDetail) {
        this.cardDetail = cardDetail;
    }

    @Override
    public String toString() {
        return "BillingDetail{" +
                "address='" + address + '\'' +
                ", cardDetail=" + cardDetail +
                '}';
    }
}
