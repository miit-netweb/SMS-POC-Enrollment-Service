package Microservices.Auth_Server.Entity;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

@Entity
@Component
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Column(unique = true)
    private String email;
    private long partnerNumber;
    private String password;

    public Subscriber() {
    }

    public Subscriber(long id, String name, String email, long partnerNumber, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.partnerNumber = partnerNumber;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getPartnerNumber() {
        return partnerNumber;
    }

    public void setPartnerNumber(long partnerNumber) {
        this.partnerNumber = partnerNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
