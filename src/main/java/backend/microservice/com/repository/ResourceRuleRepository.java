package backend.microservice.com.repository;

import backend.microservice.com.domain.ResourceRule;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ResourceRule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResourceRuleRepository extends JpaRepository<ResourceRule, Long> {
}
