package Microservices.Enrollment_Service.Dto;

import java.time.LocalDate;

public class CardDetail {

    private String cardNumber;
    private String cardType;
    private String cardHolder;
    private LocalDate cardExpiry;

    public CardDetail() {
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public LocalDate getCardExpiry() {
        return cardExpiry;
    }

    public void setCardExpiry(LocalDate cardExpiry) {
        this.cardExpiry = cardExpiry;
    }

    @Override
    public String toString() {
        return "CardDetail{" +
                "cardNumber=" + cardNumber +
                ", cardType='" + cardType + '\'' +
                ", cardHolder='" + cardHolder + '\'' +
                ", cardExpiry=" + cardExpiry +
                '}';
    }
}
