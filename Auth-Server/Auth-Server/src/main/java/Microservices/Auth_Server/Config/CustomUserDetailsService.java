package Microservices.Auth_Server.Config;

import Microservices.Auth_Server.Entity.Subscriber;
import Microservices.Auth_Server.Repository.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private SubscriberRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Subscriber> credential = repository.findByEmail(username);
        return credential.map(CustomSubscriberDetails::new).orElseThrow(() -> new UsernameNotFoundException("user not found with name :" + username));
    }
}