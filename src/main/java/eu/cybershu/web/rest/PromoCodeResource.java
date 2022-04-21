package eu.cybershu.web.rest;

import eu.cybershu.service.PromoCodeService;
import eu.cybershu.service.dto.PromoCodeDTO;
import eu.cybershu.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link eu.cybershu.domain.PromoCode}.
 */
@RestController
@RequestMapping("/api")
public class PromoCodeResource {

    private final Logger log = LoggerFactory.getLogger(PromoCodeResource.class);

    private static final String ENTITY_NAME = "promoCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PromoCodeService promoCodeService;

    public PromoCodeResource(PromoCodeService promoCodeService) {
        this.promoCodeService = promoCodeService;
    }

    /**
     * {@code POST  /promo-codes} : Create a new promoCode.
     *
     * @param promoCodeDTO the promoCodeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new promoCodeDTO, or with status {@code 400 (Bad Request)} if the promoCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/promo-codes")
    public ResponseEntity<PromoCodeDTO> createPromoCode(@Valid @RequestBody PromoCodeDTO promoCodeDTO) throws URISyntaxException {
        log.debug("REST request to save PromoCode : {}", promoCodeDTO);
        if (promoCodeDTO.getId() != null) {
            throw new BadRequestAlertException("A new promoCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PromoCodeDTO result = promoCodeService.save(promoCodeDTO);
        return ResponseEntity.created(new URI("/api/promo-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /promo-codes} : Updates an existing promoCode.
     *
     * @param promoCodeDTO the promoCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated promoCodeDTO,
     * or with status {@code 400 (Bad Request)} if the promoCodeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the promoCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/promo-codes")
    public ResponseEntity<PromoCodeDTO> updatePromoCode(@Valid @RequestBody PromoCodeDTO promoCodeDTO) throws URISyntaxException {
        log.debug("REST request to update PromoCode : {}", promoCodeDTO);
        if (promoCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PromoCodeDTO result = promoCodeService.save(promoCodeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, promoCodeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /promo-codes} : get all the promoCodes.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of promoCodes in body.
     */
    @GetMapping("/promo-codes")
    public List<PromoCodeDTO> getAllPromoCodes(@RequestParam(required = false) String filter) {
        log.debug("REST request to get all PromoCodes- filter={}", filter);
        return promoCodeService.findAll();
    }

    /**
     * {@code GET  /promo-codes/:id} : get the "id" promoCode.
     *
     * @param id the id of the promoCodeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the promoCodeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/promo-codes/{id}")
    public ResponseEntity<PromoCodeDTO> getPromoCode(@PathVariable Long id) {
        log.debug("REST request to get PromoCode : {}", id);
        Optional<PromoCodeDTO> promoCodeDTO = promoCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(promoCodeDTO);
    }

    @GetMapping("/promo-codes/code/{code}")
    public ResponseEntity<PromoCodeDTO> getPromoCode(@PathVariable String code) {
        log.debug("REST request to get PromoCode : code={}", code);
        Optional<PromoCodeDTO> promoCodeDTO = promoCodeService.findOne(code.trim());

        return promoCodeDTO
            .map((response) -> ResponseEntity.ok().body(response))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * {@code DELETE  /promo-codes/:id} : delete the "id" promoCode.
     *
     * @param id the id of the promoCodeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/promo-codes/{id}")
    public ResponseEntity<Void> deletePromoCode(@PathVariable Long id) {
        log.debug("REST request to delete PromoCode : {}", id);
        promoCodeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
