package eu.cybershu.repository;

import eu.cybershu.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * Spring Data  repository for the Ticket entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByGuestIdAndTicketTypeIdAndOrderIdAndEnabledTrue(@NotNull Long guest_id, @NotNull Long ticketType_id, @NotNull String orderId);
}
