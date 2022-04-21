package eu.cybershu.repository;

import eu.cybershu.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data  repository for the Ticket entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {
    @Modifying
    @Query("update Ticket t SET validatedAt = ?2 where t.id = ?1")
    int setValidationDate(Long id, Instant validatedAt);

    Optional<Ticket> findByUuidEquals(@NotNull UUID uuid);

    Optional<Ticket> findByGuestIdAndTicketTypeIdAndOrderIdAndEnabledTrue(@NotNull Long guest_id, @NotNull Long ticketType_id, @NotNull String orderId);
}
