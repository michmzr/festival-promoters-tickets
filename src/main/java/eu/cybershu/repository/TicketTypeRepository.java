package eu.cybershu.repository;

import eu.cybershu.domain.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the TicketType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {
    Optional<TicketType> findByProductId(String productId);
}
