package Microservices.Enrollment_Service.Repository;

import Microservices.Enrollment_Service.Entity.PersonalDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalDetailsRepository extends JpaRepository<PersonalDetails,Long> {
}
