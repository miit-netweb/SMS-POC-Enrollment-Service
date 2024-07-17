package Microservices.Auth_Server.Config;

import Microservices.Auth_Server.Entity.Subscriber;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomSubscriberDetails implements UserDetails {

    private String username;
    private String password;

    public CustomSubscriberDetails(Subscriber userCredential) {
        this.username = userCredential.getEmail();
        this.password = userCredential.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}