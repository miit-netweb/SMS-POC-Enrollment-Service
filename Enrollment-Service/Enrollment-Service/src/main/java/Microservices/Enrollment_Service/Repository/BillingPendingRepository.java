package Microservices.Enrollment_Service.Repository;

import Microservices.Enrollment_Service.Entity.BillingPending;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingPendingRepository extends JpaRepository<BillingPending,Long> {
}
