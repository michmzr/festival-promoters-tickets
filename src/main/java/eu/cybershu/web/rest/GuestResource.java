package eu.cybershu.web.rest;

import com.google.zxing.WriterException;
import eu.cybershu.service.GuestService;
import eu.cybershu.service.dto.GuestCreateDTO;
import eu.cybershu.service.dto.GuestDTO;
import eu.cybershu.service.dto.GuestUpdateDTO;
import eu.cybershu.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link eu.cybershu.domain.Guest}.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class GuestResource {
    private static final String ENTITY_NAME = "guest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GuestService guestService;

    public GuestResource(GuestService guestService) {
        this.guestService = guestService;
    }

    /**
     * {@code POST  /guests} : Create a new guest.
     *
     * @param guestDTO the guestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new guestDTO, or with status {@code 400 (Bad Request)} if the guest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/guests")
    public ResponseEntity<GuestDTO> createGuest(@Valid @RequestBody GuestCreateDTO guestDTO)
        throws URISyntaxException, IOException, WriterException {
        log.debug("REST request to save Guest : {}", guestDTO);

        GuestDTO result = guestService.save(guestDTO);

        return ResponseEntity.created(new URI("/api/guests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false,
                ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /guests} : Updates an existing guest.
     *
     * @param guestUpdateTO the guestUpdateTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated guestUpdateTO,
     * or with status {@code 400 (Bad Request)} if the guestUpdateTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the guestUpdateTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/guests")
    public ResponseEntity<GuestDTO> updateGuest(
        @Valid @RequestBody GuestUpdateDTO guestUpdateTO) throws URISyntaxException {
        log.debug("REST request to update Guest : {}", guestUpdateTO);

        if (guestUpdateTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        GuestDTO result = guestService.save(guestUpdateTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false,
                ENTITY_NAME, guestUpdateTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /guests} : get all the guests.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of guests in body.
     */
    @GetMapping("/guests")
    public ResponseEntity<List<GuestDTO>> getAllGuests(Pageable pageable) {
        log.debug("REST request to get a page of Guests");
        Page<GuestDTO> page = guestService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /guests/:id} : get the "id" guest.
     *
     * @param id the id of the guestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the guestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/guests/{id}")
    public ResponseEntity<GuestDTO> getGuest(@PathVariable Long id) {
        log.debug("REST request to get Guest : {}", id);
        Optional<GuestDTO> guestDTO = guestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(guestDTO);
    }

    /**
     * {@code DELETE  /guests/:id} : delete the "id" guest.
     *
     * @param id the id of the guestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/guests/{id}")
    public ResponseEntity<Void> deleteGuest(@PathVariable Long id) {
        log.debug("REST request to delete Guest : {}", id);
        guestService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
