package eu.cybershu.web.rest;

import com.google.zxing.WriterException;
import eu.cybershu.service.TicketQueryService;
import eu.cybershu.service.TicketService;
import eu.cybershu.service.dto.TicketCreateDTO;
import eu.cybershu.service.dto.TicketCriteria;
import eu.cybershu.service.dto.TicketDTO;
import eu.cybershu.service.dto.TicketListingItemDTO;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link eu.cybershu.domain.Ticket}.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class TicketResource {

    private static final String ENTITY_NAME = "ticket";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TicketService ticketService;

    private final TicketQueryService ticketQueryService;

    public TicketResource(TicketService ticketService, TicketQueryService ticketQueryService) {
        this.ticketService = ticketService;
        this.ticketQueryService = ticketQueryService;
    }

    /**
     * {@code POST  /tickets} : Create a new ticket.
     *
     * @param ticketDTO the ticketDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
     * ticketDTO, or with status {@code 400 (Bad Request)} if the ticket has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tickets")
    public ResponseEntity<TicketDTO> createTicket(@Valid @RequestBody TicketCreateDTO ticketDTO)
        throws URISyntaxException, IOException, WriterException, com.lowagie.text.DocumentException {
        log.debug("REST request to save Ticket : {}", ticketDTO);

        TicketDTO result = ticketService.create(ticketDTO);
        return ResponseEntity.created(new URI("/api/tickets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME,
                result.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tickets} : get all the tickets by criteria.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tickets in
     * body.
     */
    @GetMapping("/tickets/query")
    public ResponseEntity<List<TicketListingItemDTO>> getAllTickets(TicketCriteria criteria) {
        log.debug("REST request to get Tickets by criteria: {}", criteria);
        var entityList = ticketQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /promotors} : get all the promotors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of promotors in
     * body.
     */
    @GetMapping("/tickets")
    public ResponseEntity<List<TicketListingItemDTO>> getAllTickets(Pageable pageable) {
        log.debug("REST request to get Tickets by pagination: {}", pageable);

        Page<TicketListingItemDTO> page = ticketService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
            ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tickets/count} : count all the tickets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tickets/count")
    public ResponseEntity<Long> countTickets(TicketCriteria criteria) {
        log.debug("REST request to count Tickets by criteria: {}", criteria);
        return ResponseEntity.ok().body(ticketQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tickets/:id} : get the "id" ticket.
     *
     * @param id the id of the ticketDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ticketDTO,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tickets/{id}")
    public ResponseEntity<TicketDTO> getTicket(@PathVariable Long id) {
        log.debug("REST request to get Ticket : {}", id);
        Optional<TicketDTO> ticketDTO = ticketService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ticketDTO);
    }

    /**
     * {@code GET  /tickets/:id/rebuild} : regenerated ticket pdf file
     *
     * @param id the id of the ticketDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ticketDTO,
     * or with status {@code 404 (Not Found)}.
     * @throws DocumentException
     * @throws IOException
     */
    @GetMapping("/tickets/{id}/rebuild")
    public ResponseEntity<TicketDTO> rebuildTicketPdf(@PathVariable Long id)
        throws IOException, com.lowagie.text.DocumentException {
        log.debug("REST request to rebuild Ticket : {}", id);

        Optional<TicketDTO> ticketDTO = ticketService.findOne(id);
        if (ticketDTO.isPresent()) {
            ticketDTO = ticketService.regenerateTicketPdf(id);
        }
        return ResponseUtil.wrapOrNotFound(ticketDTO);
    }

    /**
     * {@code DELETE  /tickets/:id} : delete the "id" ticket.
     *
     * @param id the id of the ticketDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tickets/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        log.debug("REST request to delete Ticket : {}", id);
        ticketService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME,
                id.toString())).build();
    }

    @PostMapping("/tickets/{id}/disable")
    public ResponseEntity<Void> disableTicket(@PathVariable Long id) {
        log.debug("REST request to disable Ticket : {}", id);
        ticketService.disable(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/tickets/{id}/enable")
    public ResponseEntity<Void> enableTicket(@PathVariable Long id) {
        log.debug("REST request to enable Ticket : {}", id);
        ticketService.enable(id);
        return ResponseEntity.ok().build();
    }
}
