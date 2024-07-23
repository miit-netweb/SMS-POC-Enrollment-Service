package Microservices.Enrollment_Service.Repository;

import Microservices.Enrollment_Service.Entity.PartnerDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerRepository extends JpaRepository<PartnerDetail,Long> {
    PartnerDetail findByPartnerNumber(long partnerNumber);
}
