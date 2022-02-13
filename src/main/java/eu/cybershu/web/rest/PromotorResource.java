package eu.cybershu.web.rest;

import eu.cybershu.service.PromotorService;
import eu.cybershu.service.dto.PromotorCreateDTO;
import eu.cybershu.web.rest.errors.BadRequestAlertException;
import eu.cybershu.service.dto.PromotorDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link eu.cybershu.domain.Promotor}.
 */
@RestController
@RequestMapping("/api")
public class PromotorResource {

    private final Logger log = LoggerFactory.getLogger(PromotorResource.class);

    private static final String ENTITY_NAME = "promotor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PromotorService promotorService;

    public PromotorResource(PromotorService promotorService) {
        this.promotorService = promotorService;
    }

    /**
     * {@code POST  /promotors} : Create a new promotor.
     *
     * @param promotorDTO the promotorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new promotorDTO, or with status {@code 400 (Bad Request)} if the promotor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/promotors")
    public ResponseEntity<PromotorDTO> createPromotor(@Valid @RequestBody PromotorCreateDTO promotorDTO) throws URISyntaxException {
        log.debug("REST request to save Promotor : {}", promotorDTO);

        PromotorDTO result = promotorService.create(promotorDTO);
        return ResponseEntity.created(new URI("/api/promotors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /promotors} : Updates an existing promotor.
     *
     * @param promotorDTO the promotorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated promotorDTO,
     * or with status {@code 400 (Bad Request)} if the promotorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the promotorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/promotors")
    public ResponseEntity<PromotorDTO> updatePromotor(@Valid @RequestBody PromotorDTO promotorDTO) throws URISyntaxException {
        log.debug("REST request to update Promotor : {}", promotorDTO);
        if (promotorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PromotorDTO result = promotorService.save(promotorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, promotorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /promotors} : get all the promotors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of promotors in body.
     */
    @GetMapping("/promotors")
    public ResponseEntity<List<PromotorDTO>> getAllPromotors(Pageable pageable) {
        log.debug("REST request to get a page of Promotors");
        Page<PromotorDTO> page = promotorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /promotors/:id} : get the "id" promotor.
     *
     * @param id the id of the promotorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the promotorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/promotors/{id}")
    public ResponseEntity<PromotorDTO> getPromotor(@PathVariable Long id) {
        log.debug("REST request to get Promotor : {}", id);
        Optional<PromotorDTO> promotorDTO = promotorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(promotorDTO);
    }

    /**
     * {@code DELETE  /promotors/:id} : delete the "id" promotor.
     *
     * @param id the id of the promotorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/promotors/{id}")
    public ResponseEntity<Void> deletePromotor(@PathVariable Long id) {
        log.debug("REST request to delete Promotor : {}", id);
        promotorService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
