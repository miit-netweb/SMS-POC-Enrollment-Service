package Microservices.Enrollment_Service.Entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class PersonalDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long personalId;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private Long phoneNumber;
    @Column(unique = true)
    private String email;
    private String address;
    private String cardNumber;
    private String cardType;
    private String cardHolder;
    private LocalDate cardExpiry;

    public PersonalDetails() {
    }

    public PersonalDetails(String firstName, String lastName, Long phoneNumber, String email, String address, String cardNumber, String cardType, String cardHolder, LocalDate cardExpiry) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.cardHolder = cardHolder;
        this.cardExpiry = cardExpiry;
    }

    public long getPersonalId() {
        return personalId;
    }

    public void setPersonalId(long personalId) {
        this.personalId = personalId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
        return "PersonalDetails{" +
                "personalId=" + personalId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", cardType='" + cardType + '\'' +
                ", cardHolder='" + cardHolder + '\'' +
                ", cardExpiry=" + cardExpiry +
                '}';
    }
}
