package eu.cybershu.web.rest;

import eu.cybershu.OrganicPromoTicketsApp;
import eu.cybershu.domain.Promotor;
import eu.cybershu.repository.PromotorRepository;
import eu.cybershu.service.PromotorService;
import eu.cybershu.service.dto.PromotorDTO;
import eu.cybershu.service.mapper.PromotorMapper;
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
 * Integration tests for the {@link PromotorResource} REST controller.
 */
@SpringBootTest(classes = OrganicPromoTicketsApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PromotorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    @Autowired
    private PromotorRepository promotorRepository;

    @Autowired
    private PromotorMapper promotorMapper;

    @Autowired
    private PromotorService promotorService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPromotorMockMvc;

    private Promotor promotor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Promotor createEntity(EntityManager em) {
        Promotor promotor = new Promotor()
            .name(DEFAULT_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .notes(DEFAULT_NOTES)
            .createdAt(DEFAULT_CREATED_AT)
            .enabled(DEFAULT_ENABLED);
        return promotor;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Promotor createUpdatedEntity(EntityManager em) {
        return new Promotor()
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .notes(UPDATED_NOTES)
            .createdAt(UPDATED_CREATED_AT)
            .enabled(UPDATED_ENABLED);
    }

    @BeforeEach
    public void initTest() {
        promotor = createEntity(em);
    }

    @Test
    @Transactional
    public void createPromotor() throws Exception {
        int databaseSizeBeforeCreate = promotorRepository.findAll().size();
        // Create the Promotor
        PromotorDTO promotorDTO = promotorMapper.toDto(promotor);
        restPromotorMockMvc.perform(post("/api/promotors").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(promotorDTO)))
            .andExpect(status().isCreated());

        // Validate the Promotor in the database
        List<Promotor> promotorList = promotorRepository.findAll();
        assertThat(promotorList).hasSize(databaseSizeBeforeCreate + 1);
        Promotor testPromotor = promotorList.get(promotorList.size() - 1);
        assertThat(testPromotor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPromotor.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPromotor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPromotor.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testPromotor.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testPromotor.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testPromotor.isEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    public void createPromotorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = promotorRepository.findAll().size();

        // Create the Promotor with an existing ID
        promotor.setId(1L);
        PromotorDTO promotorDTO = promotorMapper.toDto(promotor);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPromotorMockMvc.perform(post("/api/promotors").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(promotorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Promotor in the database
        List<Promotor> promotorList = promotorRepository.findAll();
        assertThat(promotorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = promotorRepository.findAll().size();
        // set the field null
        promotor.setName(null);

        // Create the Promotor, which fails.
        PromotorDTO promotorDTO = promotorMapper.toDto(promotor);


        restPromotorMockMvc.perform(post("/api/promotors").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(promotorDTO)))
            .andExpect(status().isBadRequest());

        List<Promotor> promotorList = promotorRepository.findAll();
        assertThat(promotorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = promotorRepository.findAll().size();
        // set the field null
        promotor.setLastName(null);

        // Create the Promotor, which fails.
        PromotorDTO promotorDTO = promotorMapper.toDto(promotor);


        restPromotorMockMvc.perform(post("/api/promotors").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(promotorDTO)))
            .andExpect(status().isBadRequest());

        List<Promotor> promotorList = promotorRepository.findAll();
        assertThat(promotorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = promotorRepository.findAll().size();
        // set the field null
        promotor.setEnabled(null);

        // Create the Promotor, which fails.
        PromotorDTO promotorDTO = promotorMapper.toDto(promotor);


        restPromotorMockMvc.perform(post("/api/promotors").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(promotorDTO)))
            .andExpect(status().isBadRequest());

        List<Promotor> promotorList = promotorRepository.findAll();
        assertThat(promotorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPromotors() throws Exception {
        // Initialize the database
        promotorRepository.saveAndFlush(promotor);

        // Get all the promotorList
        restPromotorMockMvc.perform(get("/api/promotors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(promotor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void getPromotor() throws Exception {
        // Initialize the database
        promotorRepository.saveAndFlush(promotor);

        // Get the promotor
        restPromotorMockMvc.perform(get("/api/promotors/{id}", promotor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(promotor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingPromotor() throws Exception {
        // Get the promotor
        restPromotorMockMvc.perform(get("/api/promotors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePromotor() throws Exception {
        // Initialize the database
        promotorRepository.saveAndFlush(promotor);

        int databaseSizeBeforeUpdate = promotorRepository.findAll().size();

        // Update the promotor
        Promotor updatedPromotor = promotorRepository.findById(promotor.getId()).get();
        // Disconnect from session so that the updates on updatedPromotor are not directly saved in db
        em.detach(updatedPromotor);
        updatedPromotor
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .notes(UPDATED_NOTES)
            .createdAt(UPDATED_CREATED_AT)
            .enabled(UPDATED_ENABLED);
        PromotorDTO promotorDTO = promotorMapper.toDto(updatedPromotor);

        restPromotorMockMvc.perform(put("/api/promotors").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(promotorDTO)))
            .andExpect(status().isOk());

        // Validate the Promotor in the database
        List<Promotor> promotorList = promotorRepository.findAll();
        assertThat(promotorList).hasSize(databaseSizeBeforeUpdate);
        Promotor testPromotor = promotorList.get(promotorList.size() - 1);
        assertThat(testPromotor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPromotor.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPromotor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPromotor.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testPromotor.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testPromotor.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testPromotor.isEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void updateNonExistingPromotor() throws Exception {
        int databaseSizeBeforeUpdate = promotorRepository.findAll().size();

        // Create the Promotor
        PromotorDTO promotorDTO = promotorMapper.toDto(promotor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPromotorMockMvc.perform(put("/api/promotors").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(promotorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Promotor in the database
        List<Promotor> promotorList = promotorRepository.findAll();
        assertThat(promotorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePromotor() throws Exception {
        // Initialize the database
        promotorRepository.saveAndFlush(promotor);

        int databaseSizeBeforeDelete = promotorRepository.findAll().size();

        // Delete the promotor
        restPromotorMockMvc.perform(delete("/api/promotors/{id}", promotor.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Promotor> promotorList = promotorRepository.findAll();
        assertThat(promotorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
