package eu.cybershu.web.rest;

import eu.cybershu.OrganicPromoTicketsApp;
import eu.cybershu.domain.TicketType;
import eu.cybershu.repository.TicketTypeRepository;
import eu.cybershu.service.TicketTypeService;
import eu.cybershu.service.dto.TicketTypeDTO;
import eu.cybershu.service.mapper.TicketTypeMapper;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TicketTypeResource} REST controller.
 */
@SpringBootTest(classes = OrganicPromoTicketsApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TicketTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    @Autowired
    private TicketTypeRepository ticketTypeRepository;

    @Autowired
    private TicketTypeMapper ticketTypeMapper;

    @Autowired
    private TicketTypeService ticketTypeService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTicketTypeMockMvc;

    private TicketType ticketType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TicketType createEntity(EntityManager em) {
        TicketType ticketType = new TicketType()
            .name(DEFAULT_NAME)
            .notes(DEFAULT_NOTES);
        return ticketType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TicketType createUpdatedEntity(EntityManager em) {
        TicketType ticketType = new TicketType()
            .name(UPDATED_NAME)
            .notes(UPDATED_NOTES);
        return ticketType;
    }

    @BeforeEach
    public void initTest() {
        ticketType = createEntity(em);
    }

    @Test
    @Transactional
    public void createTicketType() throws Exception {
        int databaseSizeBeforeCreate = ticketTypeRepository.findAll().size();
        // Create the TicketType
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);
        restTicketTypeMockMvc.perform(post("/api/ticket-types").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ticketTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the TicketType in the database
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeCreate + 1);
        TicketType testTicketType = ticketTypeList.get(ticketTypeList.size() - 1);
        assertThat(testTicketType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTicketType.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    public void createTicketTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ticketTypeRepository.findAll().size();

        // Create the TicketType with an existing ID
        ticketType.setId(1L);
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTicketTypeMockMvc.perform(post("/api/ticket-types").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ticketTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TicketType in the database
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTypeRepository.findAll().size();
        // set the field null
        ticketType.setName(null);

        // Create the TicketType, which fails.
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);


        restTicketTypeMockMvc.perform(post("/api/ticket-types").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ticketTypeDTO)))
            .andExpect(status().isBadRequest());

        List<TicketType> ticketTypeList = ticketTypeRepository.findAll();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTicketTypes() throws Exception {
        // Initialize the database
        ticketTypeRepository.saveAndFlush(ticketType);

        // Get all the ticketTypeList
        restTicketTypeMockMvc.perform(get("/api/ticket-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticketType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }
    
    @Test
    @Transactional
    public void getTicketType() throws Exception {
        // Initialize the database
        ticketTypeRepository.saveAndFlush(ticketType);

        // Get the ticketType
        restTicketTypeMockMvc.perform(get("/api/ticket-types/{id}", ticketType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ticketType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }
    @Test
    @Transactional
    public void getNonExistingTicketType() throws Exception {
        // Get the ticketType
        restTicketTypeMockMvc.perform(get("/api/ticket-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTicketType() throws Exception {
        // Initialize the database
        ticketTypeRepository.saveAndFlush(ticketType);

        int databaseSizeBeforeUpdate = ticketTypeRepository.findAll().size();

        // Update the ticketType
        TicketType updatedTicketType = ticketTypeRepository.findById(ticketType.getId()).get();
        // Disconnect from session so that the updates on updatedTicketType are not directly saved in db
        em.detach(updatedTicketType);
        updatedTicketType
            .name(UPDATED_NAME)
            .notes(UPDATED_NOTES);
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(updatedTicketType);

        restTicketTypeMockMvc.perform(put("/api/ticket-types").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ticketTypeDTO)))
            .andExpect(status().isOk());

        // Validate the TicketType in the database
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeUpdate);
        TicketType testTicketType = ticketTypeList.get(ticketTypeList.size() - 1);
        assertThat(testTicketType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTicketType.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    public void updateNonExistingTicketType() throws Exception {
        int databaseSizeBeforeUpdate = ticketTypeRepository.findAll().size();

        // Create the TicketType
        TicketTypeDTO ticketTypeDTO = ticketTypeMapper.toDto(ticketType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTicketTypeMockMvc.perform(put("/api/ticket-types").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ticketTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TicketType in the database
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTicketType() throws Exception {
        // Initialize the database
        ticketTypeRepository.saveAndFlush(ticketType);

        int databaseSizeBeforeDelete = ticketTypeRepository.findAll().size();

        // Delete the ticketType
        restTicketTypeMockMvc.perform(delete("/api/ticket-types/{id}", ticketType.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll();
        assertThat(ticketTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
