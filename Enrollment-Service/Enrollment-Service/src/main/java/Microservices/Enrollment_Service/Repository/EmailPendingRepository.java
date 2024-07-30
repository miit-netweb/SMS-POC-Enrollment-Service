package Microservices.Enrollment_Service.Repository;

import Microservices.Enrollment_Service.Entity.EmailPending;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailPendingRepository extends JpaRepository<EmailPending,Long> {
}
