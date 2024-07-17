package Microservices.Auth_Server.Repository;

import Microservices.Auth_Server.Entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {

    public Optional<Subscriber> findByEmail(String email);
}
