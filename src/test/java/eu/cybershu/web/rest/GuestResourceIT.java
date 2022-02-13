package eu.cybershu.web.rest;

import eu.cybershu.OrganicPromoTicketsApp;
import eu.cybershu.domain.Guest;
import eu.cybershu.repository.GuestRepository;
import eu.cybershu.service.GuestService;
import eu.cybershu.service.dto.GuestDTO;
import eu.cybershu.service.mapper.GuestMapper;

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
 * Integration tests for the {@link GuestResource} REST controller.
 */
@SpringBootTest(classes = OrganicPromoTicketsApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class GuestResourceIT {

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
    private GuestRepository guestRepository;

    @Autowired
    private GuestMapper guestMapper;

    @Autowired
    private GuestService guestService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGuestMockMvc;

    private Guest guest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Guest createEntity(EntityManager em) {
        Guest guest = new Guest()
            .name(DEFAULT_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .notes(DEFAULT_NOTES)
            .createdAt(DEFAULT_CREATED_AT)
            .enabled(DEFAULT_ENABLED);
        return guest;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Guest createUpdatedEntity(EntityManager em) {
        Guest guest = new Guest()
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .notes(UPDATED_NOTES)
            .createdAt(UPDATED_CREATED_AT)
            .enabled(UPDATED_ENABLED);
        return guest;
    }

    @BeforeEach
    public void initTest() {
        guest = createEntity(em);
    }

    @Test
    @Transactional
    public void createGuest() throws Exception {
        int databaseSizeBeforeCreate = guestRepository.findAll().size();
        // Create the Guest
        GuestDTO guestDTO = guestMapper.toDto(guest);
        restGuestMockMvc.perform(post("/api/guests").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(guestDTO)))
            .andExpect(status().isCreated());

        // Validate the Guest in the database
        List<Guest> guestList = guestRepository.findAll();
        assertThat(guestList).hasSize(databaseSizeBeforeCreate + 1);
        Guest testGuest = guestList.get(guestList.size() - 1);
        assertThat(testGuest.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGuest.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testGuest.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testGuest.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testGuest.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testGuest.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testGuest.isEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    public void createGuestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = guestRepository.findAll().size();

        // Create the Guest with an existing ID
        guest.setId(1L);
        GuestDTO guestDTO = guestMapper.toDto(guest);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGuestMockMvc.perform(post("/api/guests").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(guestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Guest in the database
        List<Guest> guestList = guestRepository.findAll();
        assertThat(guestList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = guestRepository.findAll().size();
        // set the field null
        guest.setName(null);

        // Create the Guest, which fails.
        GuestDTO guestDTO = guestMapper.toDto(guest);


        restGuestMockMvc.perform(post("/api/guests").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(guestDTO)))
            .andExpect(status().isBadRequest());

        List<Guest> guestList = guestRepository.findAll();
        assertThat(guestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = guestRepository.findAll().size();
        // set the field null
        guest.setLastName(null);

        // Create the Guest, which fails.
        GuestDTO guestDTO = guestMapper.toDto(guest);


        restGuestMockMvc.perform(post("/api/guests").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(guestDTO)))
            .andExpect(status().isBadRequest());

        List<Guest> guestList = guestRepository.findAll();
        assertThat(guestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = guestRepository.findAll().size();
        // set the field null
        guest.setEmail(null);

        // Create the Guest, which fails.
        GuestDTO guestDTO = guestMapper.toDto(guest);


        restGuestMockMvc.perform(post("/api/guests").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(guestDTO)))
            .andExpect(status().isBadRequest());

        List<Guest> guestList = guestRepository.findAll();
        assertThat(guestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = guestRepository.findAll().size();
        // set the field null
        guest.setEnabled(null);

        // Create the Guest, which fails.
        GuestDTO guestDTO = guestMapper.toDto(guest);


        restGuestMockMvc.perform(post("/api/guests").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(guestDTO)))
            .andExpect(status().isBadRequest());

        List<Guest> guestList = guestRepository.findAll();
        assertThat(guestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGuests() throws Exception {
        // Initialize the database
        guestRepository.saveAndFlush(guest);

        // Get all the guestList
        restGuestMockMvc.perform(get("/api/guests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guest.getId().intValue())))
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
    public void getGuest() throws Exception {
        // Initialize the database
        guestRepository.saveAndFlush(guest);

        // Get the guest
        restGuestMockMvc.perform(get("/api/guests/{id}", guest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(guest.getId().intValue()))
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
    public void getNonExistingGuest() throws Exception {
        // Get the guest
        restGuestMockMvc.perform(get("/api/guests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGuest() throws Exception {
        // Initialize the database
        guestRepository.saveAndFlush(guest);

        int databaseSizeBeforeUpdate = guestRepository.findAll().size();

        // Update the guest
        Guest updatedGuest = guestRepository.findById(guest.getId()).get();
        // Disconnect from session so that the updates on updatedGuest are not directly saved in db
        em.detach(updatedGuest);
        updatedGuest
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .notes(UPDATED_NOTES)
            .createdAt(UPDATED_CREATED_AT)
            .enabled(UPDATED_ENABLED);
        GuestDTO guestDTO = guestMapper.toDto(updatedGuest);

        restGuestMockMvc.perform(put("/api/guests").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(guestDTO)))
            .andExpect(status().isOk());

        // Validate the Guest in the database
        List<Guest> guestList = guestRepository.findAll();
        assertThat(guestList).hasSize(databaseSizeBeforeUpdate);
        Guest testGuest = guestList.get(guestList.size() - 1);
        assertThat(testGuest.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGuest.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testGuest.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testGuest.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testGuest.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testGuest.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testGuest.isEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void updateNonExistingGuest() throws Exception {
        int databaseSizeBeforeUpdate = guestRepository.findAll().size();

        // Create the Guest
        GuestDTO guestDTO = guestMapper.toDto(guest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGuestMockMvc.perform(put("/api/guests").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(guestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Guest in the database
        List<Guest> guestList = guestRepository.findAll();
        assertThat(guestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGuest() throws Exception {
        // Initialize the database
        guestRepository.saveAndFlush(guest);

        int databaseSizeBeforeDelete = guestRepository.findAll().size();

        // Delete the guest
        restGuestMockMvc.perform(delete("/api/guests/{id}", guest.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Guest> guestList = guestRepository.findAll();
        assertThat(guestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
