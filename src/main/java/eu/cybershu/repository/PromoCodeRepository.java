package eu.cybershu.repository;

import eu.cybershu.domain.PromoCode;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PromoCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {
}
