package backend.microservice.com.repository;

import backend.microservice.com.domain.MachineRule;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the MachineRule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MachineRuleRepository extends JpaRepository<MachineRule, Long> {
}
