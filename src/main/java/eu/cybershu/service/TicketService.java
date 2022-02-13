package eu.cybershu.service;

import com.google.zxing.WriterException;
import eu.cybershu.domain.TicketType;
import eu.cybershu.service.dto.TicketDTO;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link eu.cybershu.domain.Ticket}.
 */
public interface TicketService {

    /**
     * Save a ticket.
     *
     * @param ticketDTO the entity to save.
     * @return the persisted entity.
     */
    TicketDTO create(TicketDTO ticketDTO) throws WriterException, IOException;

    TicketDTO create(Long ticketTypeId) throws IOException, WriterException;

    /**
     * Get all the tickets.
     *
     * @return the list of entities.
     */
    List<TicketDTO> findAll();

    /**
     * Get all the TicketDTO where Guest is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<TicketDTO> findAllWhereGuestIsNull();


    /**
     * Get the "id" ticket.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TicketDTO> findOne(Long id);

    /**
     * Delete the "id" ticket.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
