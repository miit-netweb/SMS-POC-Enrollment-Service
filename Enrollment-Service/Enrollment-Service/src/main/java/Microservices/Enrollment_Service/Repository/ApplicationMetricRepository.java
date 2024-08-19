package Microservices.Enrollment_Service.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Microservices.Enrollment_Service.Entity.ApplicationMetric;

public interface ApplicationMetricRepository extends JpaRepository<ApplicationMetric, Long> {
}
