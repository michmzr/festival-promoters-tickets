package eu.cybershu.repository;

import eu.cybershu.domain.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the Guest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
    Optional<Guest> findGuestByEmail(String email);
}
