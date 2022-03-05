package eu.cybershu.service;

import eu.cybershu.service.dto.PromotorCreateDTO;
import eu.cybershu.service.dto.PromotorDTO;
import eu.cybershu.service.dto.PromotorUpdateDTO;
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
    PromotorDTO save(PromotorUpdateDTO promotorDTO);

    /**
     * Create  a promotor.
     *
     * @param promotorDTO the entity to create.
     * @return the persisted entity.
     */
    PromotorDTO create(PromotorCreateDTO promotorDTO);

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
