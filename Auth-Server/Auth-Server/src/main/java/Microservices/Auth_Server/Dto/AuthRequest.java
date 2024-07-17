package Microservices.Auth_Server.Dto;

import org.springframework.stereotype.Component;

@Component
public class AuthRequest {

    private String username;
    private String password;

    public AuthRequest() {
    }

    public AuthRequest(String email, String password) {
        this.username = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String email) {
        this.username = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
