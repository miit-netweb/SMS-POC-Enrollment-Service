package Microservices.Auth_Server.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class PartnerTokenValidation {

    @Id
    private long partnerNumber;
    private LocalDateTime expiry;

    private String token;

    public PartnerTokenValidation() {
    }

    public PartnerTokenValidation(long partnerNumber, LocalDateTime expiry, String token) {
        this.partnerNumber = partnerNumber;
        this.expiry = expiry;
        this.token = token;
    }

    public long getPartnerNumber() {
        return partnerNumber;
    }

    public void setPartnerNumber(long partnerNumber) {
        this.partnerNumber = partnerNumber;
    }

    public LocalDateTime getExpiry() {
        return expiry;
    }

    public void setExpiry(LocalDateTime expiry) {
        this.expiry = expiry;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
