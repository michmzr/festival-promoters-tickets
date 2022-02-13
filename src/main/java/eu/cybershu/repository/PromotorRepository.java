package eu.cybershu.repository;

import eu.cybershu.domain.Promotor;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Promotor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PromotorRepository extends JpaRepository<Promotor, Long> {
}
