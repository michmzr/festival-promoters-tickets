package eu.cybershu.service;

import eu.cybershu.service.dto.TicketTypeDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link eu.cybershu.domain.TicketType}.
 */
public interface TicketTypeService {

    /**
     * Save a ticketType.
     *
     * @param ticketTypeDTO the entity to save.
     * @return the persisted entity.
     */
    TicketTypeDTO save(TicketTypeDTO ticketTypeDTO);

    /**
     * Get all the ticketTypes.
     *
     * @return the list of entities.
     */
    List<TicketTypeDTO> findAll();

    /**
     * Get the "id" ticketType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TicketTypeDTO> findOne(Long id);

    /**
     * Delete the "id" ticketType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
