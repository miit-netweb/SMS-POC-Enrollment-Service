package Microservices.Auth_Server.Repository;

import Microservices.Auth_Server.Entity.PartnerDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartnerRepository extends JpaRepository<PartnerDetail,Long> {
    PartnerDetail findByPartnerNumber(long partnerNumber);
}
