package Microservices.Enrollment_Service.Repository;

import Microservices.Enrollment_Service.Entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriberRepository extends JpaRepository<Subscriber,Long> {
}
