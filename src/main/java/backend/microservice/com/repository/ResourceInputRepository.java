package backend.microservice.com.repository;

import backend.microservice.com.domain.ResourceInput;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ResourceInput entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResourceInputRepository extends JpaRepository<ResourceInput, Long> {
}
