package eu.cybershu.web.rest;

import eu.cybershu.service.TicketTypeService;
import eu.cybershu.web.rest.errors.BadRequestAlertException;
import eu.cybershu.service.dto.TicketTypeDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link eu.cybershu.domain.TicketType}.
 */
@RestController
@RequestMapping("/api")
public class TicketTypeResource {

    private final Logger log = LoggerFactory.getLogger(TicketTypeResource.class);

    private static final String ENTITY_NAME = "ticketType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TicketTypeService ticketTypeService;

    public TicketTypeResource(TicketTypeService ticketTypeService) {
        this.ticketTypeService = ticketTypeService;
    }

    /**
     * {@code POST  /ticket-types} : Create a new ticketType.
     *
     * @param ticketTypeDTO the ticketTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ticketTypeDTO, or with status {@code 400 (Bad Request)} if the ticketType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ticket-types")
    public ResponseEntity<TicketTypeDTO> createTicketType(@Valid @RequestBody TicketTypeDTO ticketTypeDTO) throws URISyntaxException {
        log.debug("REST request to save TicketType : {}", ticketTypeDTO);
        if (ticketTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new ticketType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TicketTypeDTO result = ticketTypeService.save(ticketTypeDTO);
        return ResponseEntity.created(new URI("/api/ticket-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ticket-types} : Updates an existing ticketType.
     *
     * @param ticketTypeDTO the ticketTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ticketTypeDTO,
     * or with status {@code 400 (Bad Request)} if the ticketTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ticketTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ticket-types")
    public ResponseEntity<TicketTypeDTO> updateTicketType(@Valid @RequestBody TicketTypeDTO ticketTypeDTO) throws URISyntaxException {
        log.debug("REST request to update TicketType : {}", ticketTypeDTO);
        if (ticketTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TicketTypeDTO result = ticketTypeService.save(ticketTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ticketTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ticket-types} : get all the ticketTypes.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ticketTypes in body.
     */
    @GetMapping("/ticket-types")
    public List<TicketTypeDTO> getAllTicketTypes(@RequestParam(required = false) String filter) {
        log.debug("REST request to get all TicketTypes");
        return ticketTypeService.findAll();
    }

    /**
     * {@code GET  /ticket-types/:id} : get the "id" ticketType.
     *
     * @param id the id of the ticketTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ticketTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ticket-types/{id}")
    public ResponseEntity<TicketTypeDTO> getTicketType(@PathVariable Long id) {
        log.debug("REST request to get TicketType : {}", id);
        Optional<TicketTypeDTO> ticketTypeDTO = ticketTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ticketTypeDTO);
    }

    /**
     * {@code DELETE  /ticket-types/:id} : delete the "id" ticketType.
     *
     * @param id the id of the ticketTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ticket-types/{id}")
    public ResponseEntity<Void> deleteTicketType(@PathVariable Long id) {
        log.debug("REST request to delete TicketType : {}", id);
        ticketTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
