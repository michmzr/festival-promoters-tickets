package eu.cybershu.service;

import com.google.zxing.WriterException;
import eu.cybershu.service.dto.GuestCreateDTO;
import eu.cybershu.service.dto.GuestDTO;

import eu.cybershu.service.dto.GuestUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.Optional;

/**
 * Service Interface for managing {@link eu.cybershu.domain.Guest}.
 */
public interface GuestService {

    /**
     * Save a guest.
     *
     * @param guestDTO the entity to save.
     * @return the persisted entity.
     */
    GuestDTO save(GuestCreateDTO guestDTO) throws IOException, WriterException;

    /**
     * Update a guest.
     *
     * @param guestDTO the entity to save.
     * @return the persisted entity.
     */
    GuestDTO save(GuestUpdateDTO guestDTO);

    /**
     * Get all the guests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GuestDTO> findAll(Pageable pageable);


    /**
     * Get the "id" guest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GuestDTO> findOne(Long id);

    /**
     * Delete the "id" guest.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
