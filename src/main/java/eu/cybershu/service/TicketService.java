package eu.cybershu.service;

import com.google.zxing.WriterException;
import com.lowagie.text.DocumentException;
import eu.cybershu.service.dto.TicketCreateDTO;
import eu.cybershu.service.dto.TicketDTO;
import eu.cybershu.service.dto.TicketListingItemDTO;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    TicketDTO create(TicketCreateDTO ticketDTO)
        throws WriterException, IOException, DocumentException;

    /**
     * Get all the tickets.
     *
     * @return the list of entities.
     */
    List<TicketDTO> findAll();


    /**
     * Get all the tickets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TicketListingItemDTO> findAll(Pageable pageable);

    /**
     * Get all the TicketDTO where Guest is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<TicketDTO> findAllWhereGuestIsNull();

    /**
     * Get Ticket by Guest id, ticket type id and order id
     *
     * @param guestId      Guest id
     * @param ticketTypeId Ticket type id
     * @param orderId      Order id - string from WooComerce
     * @return Ticket Dto
     */
    Optional<TicketDTO> findByGuestIdTicketTypeAndOrderId(Long guestId, Long ticketTypeId, String orderId);

    /**
     * Get the "id" ticket.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TicketDTO> findOne(Long id);

    /**
     * Get the ticket by UUID number
     *
     * @param uuid UUID number
     * @return
     */
    Optional<TicketDTO> findByUUID(String uuid);

    /**
     * Regenerates Ticket PDF
     *
     * @param id
     * @return
     */
    Optional<TicketDTO> regenerateTicketPdf(Long id) throws DocumentException, IOException;

    /**
     * Validate ticket
     *
     * @param id Ticket id
     * @return
     */
    void validateTicket(Long id);

    /**
     * Delete the "id" ticket.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Disable the "id" ticket
     *
     * @param id the id of the ticket
     */
    void disable(Long id);

    /**
     * Enable the "id" ticket
     *
     * @param id the id of the ticket
     */
    void enable(Long id);
}
