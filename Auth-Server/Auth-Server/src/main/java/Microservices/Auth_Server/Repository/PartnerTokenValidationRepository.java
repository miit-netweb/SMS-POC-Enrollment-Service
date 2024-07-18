package Microservices.Auth_Server.Repository;


import Microservices.Auth_Server.Entity.PartnerTokenValidation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerTokenValidationRepository extends JpaRepository<PartnerTokenValidation,Long> {
}
