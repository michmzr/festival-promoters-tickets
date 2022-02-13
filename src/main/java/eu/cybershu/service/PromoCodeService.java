package eu.cybershu.service;

import eu.cybershu.service.dto.PromoCodeDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link eu.cybershu.domain.PromoCode}.
 */
public interface PromoCodeService {

    /**
     * Save a promoCode.
     *
     * @param promoCodeDTO the entity to save.
     * @return the persisted entity.
     */
    PromoCodeDTO save(PromoCodeDTO promoCodeDTO);

    /**
     * Get all the promoCodes.
     *
     * @return the list of entities.
     */
    List<PromoCodeDTO> findAll();

    /**
     * Get the "id" promoCode.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PromoCodeDTO> findOne(Long id);

    /**
     * Delete the "id" promoCode.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
