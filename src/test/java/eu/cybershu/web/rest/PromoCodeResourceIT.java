package eu.cybershu.web.rest;

import eu.cybershu.OrganicPromoTicketsApp;
import eu.cybershu.domain.PromoCode;
import eu.cybershu.repository.PromoCodeRepository;
import eu.cybershu.service.PromoCodeService;
import eu.cybershu.service.dto.PromoCodeDTO;
import eu.cybershu.service.mapper.PromoCodeMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PromoCodeResource} REST controller.
 */
@SpringBootTest(classes = OrganicPromoTicketsApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PromoCodeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DISABLED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DISABLED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private PromoCodeRepository promoCodeRepository;

    @Autowired
    private PromoCodeMapper promoCodeMapper;

    @Autowired
    private PromoCodeService promoCodeService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPromoCodeMockMvc;

    private PromoCode promoCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PromoCode createEntity(EntityManager em) {
        PromoCode promoCode = new PromoCode()
            .code(DEFAULT_CODE)
            .notes(DEFAULT_NOTES)
            .enabled(DEFAULT_ENABLED)
            .createdAt(DEFAULT_CREATED_AT)
            .disabledAt(DEFAULT_DISABLED_AT);
        return promoCode;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PromoCode createUpdatedEntity(EntityManager em) {
        PromoCode promoCode = new PromoCode()
            .code(UPDATED_CODE)
            .notes(UPDATED_NOTES)
            .enabled(UPDATED_ENABLED)
            .createdAt(UPDATED_CREATED_AT)
            .disabledAt(UPDATED_DISABLED_AT);
        return promoCode;
    }

    @BeforeEach
    public void initTest() {
        promoCode = createEntity(em);
    }

    @Test
    @Transactional
    public void createPromoCode() throws Exception {
        int databaseSizeBeforeCreate = promoCodeRepository.findAll().size();
        // Create the PromoCode
        PromoCodeDTO promoCodeDTO = promoCodeMapper.toDto(promoCode);
        restPromoCodeMockMvc.perform(post("/api/promo-codes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(promoCodeDTO)))
            .andExpect(status().isCreated());

        // Validate the PromoCode in the database
        List<PromoCode> promoCodeList = promoCodeRepository.findAll();
        assertThat(promoCodeList).hasSize(databaseSizeBeforeCreate + 1);
        PromoCode testPromoCode = promoCodeList.get(promoCodeList.size() - 1);
        assertThat(testPromoCode.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPromoCode.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testPromoCode.isEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testPromoCode.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testPromoCode.getDisabledAt()).isEqualTo(DEFAULT_DISABLED_AT);
    }

    @Test
    @Transactional
    public void createPromoCodeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = promoCodeRepository.findAll().size();

        // Create the PromoCode with an existing ID
        promoCode.setId(1L);
        PromoCodeDTO promoCodeDTO = promoCodeMapper.toDto(promoCode);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPromoCodeMockMvc.perform(post("/api/promo-codes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(promoCodeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PromoCode in the database
        List<PromoCode> promoCodeList = promoCodeRepository.findAll();
        assertThat(promoCodeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = promoCodeRepository.findAll().size();
        // set the field null
        promoCode.setCode(null);

        // Create the PromoCode, which fails.
        PromoCodeDTO promoCodeDTO = promoCodeMapper.toDto(promoCode);


        restPromoCodeMockMvc.perform(post("/api/promo-codes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(promoCodeDTO)))
            .andExpect(status().isBadRequest());

        List<PromoCode> promoCodeList = promoCodeRepository.findAll();
        assertThat(promoCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = promoCodeRepository.findAll().size();
        // set the field null
        promoCode.setEnabled(null);

        // Create the PromoCode, which fails.
        PromoCodeDTO promoCodeDTO = promoCodeMapper.toDto(promoCode);


        restPromoCodeMockMvc.perform(post("/api/promo-codes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(promoCodeDTO)))
            .andExpect(status().isBadRequest());

        List<PromoCode> promoCodeList = promoCodeRepository.findAll();
        assertThat(promoCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = promoCodeRepository.findAll().size();
        // set the field null
        promoCode.setCreatedAt(null);

        // Create the PromoCode, which fails.
        PromoCodeDTO promoCodeDTO = promoCodeMapper.toDto(promoCode);


        restPromoCodeMockMvc.perform(post("/api/promo-codes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(promoCodeDTO)))
            .andExpect(status().isBadRequest());

        List<PromoCode> promoCodeList = promoCodeRepository.findAll();
        assertThat(promoCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPromoCodes() throws Exception {
        // Initialize the database
        promoCodeRepository.saveAndFlush(promoCode);

        // Get all the promoCodeList
        restPromoCodeMockMvc.perform(get("/api/promo-codes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(promoCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].disabledAt").value(hasItem(DEFAULT_DISABLED_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getPromoCode() throws Exception {
        // Initialize the database
        promoCodeRepository.saveAndFlush(promoCode);

        // Get the promoCode
        restPromoCodeMockMvc.perform(get("/api/promo-codes/{id}", promoCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(promoCode.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.disabledAt").value(DEFAULT_DISABLED_AT.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingPromoCode() throws Exception {
        // Get the promoCode
        restPromoCodeMockMvc.perform(get("/api/promo-codes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePromoCode() throws Exception {
        // Initialize the database
        promoCodeRepository.saveAndFlush(promoCode);

        int databaseSizeBeforeUpdate = promoCodeRepository.findAll().size();

        // Update the promoCode
        PromoCode updatedPromoCode = promoCodeRepository.findById(promoCode.getId()).get();
        // Disconnect from session so that the updates on updatedPromoCode are not directly saved in db
        em.detach(updatedPromoCode);
        updatedPromoCode
            .code(UPDATED_CODE)
            .notes(UPDATED_NOTES)
            .enabled(UPDATED_ENABLED)
            .createdAt(UPDATED_CREATED_AT)
            .disabledAt(UPDATED_DISABLED_AT);
        PromoCodeDTO promoCodeDTO = promoCodeMapper.toDto(updatedPromoCode);

        restPromoCodeMockMvc.perform(put("/api/promo-codes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(promoCodeDTO)))
            .andExpect(status().isOk());

        // Validate the PromoCode in the database
        List<PromoCode> promoCodeList = promoCodeRepository.findAll();
        assertThat(promoCodeList).hasSize(databaseSizeBeforeUpdate);
        PromoCode testPromoCode = promoCodeList.get(promoCodeList.size() - 1);
        assertThat(testPromoCode.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPromoCode.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testPromoCode.isEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testPromoCode.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testPromoCode.getDisabledAt()).isEqualTo(UPDATED_DISABLED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingPromoCode() throws Exception {
        int databaseSizeBeforeUpdate = promoCodeRepository.findAll().size();

        // Create the PromoCode
        PromoCodeDTO promoCodeDTO = promoCodeMapper.toDto(promoCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPromoCodeMockMvc.perform(put("/api/promo-codes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(promoCodeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PromoCode in the database
        List<PromoCode> promoCodeList = promoCodeRepository.findAll();
        assertThat(promoCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePromoCode() throws Exception {
        // Initialize the database
        promoCodeRepository.saveAndFlush(promoCode);

        int databaseSizeBeforeDelete = promoCodeRepository.findAll().size();

        // Delete the promoCode
        restPromoCodeMockMvc.perform(delete("/api/promo-codes/{id}", promoCode.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PromoCode> promoCodeList = promoCodeRepository.findAll();
        assertThat(promoCodeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
