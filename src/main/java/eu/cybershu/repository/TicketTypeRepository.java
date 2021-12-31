package eu.cybershu.repository;

import eu.cybershu.domain.TicketType;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TicketType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {
}
