package eu.cybershu.service;

import eu.cybershu.service.dto.PromotorDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link eu.cybershu.domain.Promotor}.
 */
public interface PromotorService {

    /**
     * Save a promotor.
     *
     * @param promotorDTO the entity to save.
     * @return the persisted entity.
     */
    PromotorDTO save(PromotorDTO promotorDTO);

    /**
     * Get all the promotors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PromotorDTO> findAll(Pageable pageable);


    /**
     * Get the "id" promotor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PromotorDTO> findOne(Long id);

    /**
     * Delete the "id" promotor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
