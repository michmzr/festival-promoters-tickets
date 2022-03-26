package eu.cybershu.web.rest;

import eu.cybershu.OrganicPromoTicketsApp;
import eu.cybershu.domain.Ticket;
import eu.cybershu.domain.TicketType;
import eu.cybershu.repository.TicketRepository;
import eu.cybershu.service.TicketService;
import eu.cybershu.service.dto.TicketDTO;
import eu.cybershu.service.mapper.TicketMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TicketResource} REST controller.
 */
@SpringBootTest(classes = OrganicPromoTicketsApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TicketResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final String DEFAULT_TICKET_URL = "AAAAAAAAAA";
    private static final String UPDATED_TICKET_URL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_TICKET_QR = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_TICKET_QR = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_TICKET_QR_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_TICKET_QR_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_TICKET_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_TICKET_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_TICKET_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_TICKET_FILE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_ENABLED = true;
    private static final Boolean UPDATED_ENABLED = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DISABLED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DISABLED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_VALIDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VALIDATED_AT = Instant.now().minusSeconds(30).truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ORDER_ID = "3";
    private static final String UPDATED_ORDER_ID = "4";

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTicketMockMvc;

    private Ticket ticket;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ticket createEntity(EntityManager em) {
        TicketType ticketType = TicketTypeResourceIT.createEntity(em);
        em.persist(ticketType);

        Ticket ticket = Ticket.builder()
            .ticketUrl(DEFAULT_TICKET_URL)
            .uuid(DEFAULT_UUID)
            .ticketQR(DEFAULT_TICKET_QR)
            .ticketQRContentType(DEFAULT_TICKET_QR_CONTENT_TYPE)
            .ticketFile(DEFAULT_TICKET_FILE)
            .ticketFileContentType(DEFAULT_TICKET_FILE_CONTENT_TYPE)
            .orderId(DEFAULT_ORDER_ID)
            .enabled(DEFAULT_ENABLED)
            .createdAt(DEFAULT_CREATED_AT)
            .disabledAt(DEFAULT_DISABLED_AT)
            .ticketType(ticketType)
            .validatedAt(DEFAULT_VALIDATED_AT)
            .build();

        return ticket;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ticket createUpdatedEntity(EntityManager em) {
        TicketType ticketType = TicketTypeResourceIT.createEntity(em);
        em.persist(ticketType);

        Ticket ticket = Ticket.builder()
            .uuid(UPDATED_UUID)
            .ticketUrl(UPDATED_TICKET_URL)
            .ticketQR(UPDATED_TICKET_QR)
            .ticketQRContentType(UPDATED_TICKET_QR_CONTENT_TYPE)
            .ticketFile(UPDATED_TICKET_FILE)
            .ticketFileContentType(UPDATED_TICKET_FILE_CONTENT_TYPE)
            .orderId(UPDATED_ORDER_ID)
            .enabled(UPDATED_ENABLED)
            .createdAt(UPDATED_CREATED_AT)
            .validatedAt(UPDATED_VALIDATED_AT)
            .ticketType(ticketType)
            .disabledAt(UPDATED_DISABLED_AT)
            .build();
        return ticket;
    }

    @BeforeEach
    public void initTest() {
        ticket = createEntity(em);
    }

    @Test
    @Transactional
    public void createTicket() throws Exception {
        int databaseSizeBeforeCreate = ticketRepository.findAll().size();

        // Create the Ticket
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);
        restTicketMockMvc.perform(post("/api/tickets").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(ticketDTO)))
            .andExpect(status().isCreated());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeCreate + 1);
        Ticket testTicket = ticketList.get(ticketList.size() - 1);
        assertThat(testTicket.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testTicket.getTicketUrl()).isEqualTo(DEFAULT_TICKET_URL);
        assertThat(testTicket.getTicketQR()).isEqualTo(DEFAULT_TICKET_QR);
        assertThat(testTicket.getTicketQRContentType()).isEqualTo(DEFAULT_TICKET_QR_CONTENT_TYPE);
        assertThat(testTicket.getTicketFile()).isEqualTo(DEFAULT_TICKET_FILE);
        assertThat(testTicket.getTicketFileContentType()).isEqualTo(DEFAULT_TICKET_FILE_CONTENT_TYPE);
        assertThat(testTicket.getValidatedAt()).isEqualTo(DEFAULT_VALIDATED_AT);
//        assertThat(testTicket.isEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testTicket.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTicket.getDisabledAt()).isEqualTo(DEFAULT_DISABLED_AT);
    }

    @Test
    @Transactional
    public void createTicketWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ticketRepository.findAll().size();

        // Create the Ticket with an existing ID
        ticket.setId(1L);
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTicketMockMvc.perform(post("/api/tickets").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(ticketDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketRepository.findAll().size();
        // set the field null
        ticket.setUuid(null);

        // Create the Ticket, which fails.
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);


        restTicketMockMvc.perform(post("/api/tickets").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(ticketDTO)))
            .andExpect(status().isBadRequest());

        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTicketUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketRepository.findAll().size();
        // set the field null
        ticket.setTicketUrl(null);

        // Create the Ticket, which fails.
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        restTicketMockMvc.perform(post("/api/tickets").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(ticketDTO)))
            .andExpect(status().isBadRequest());

        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketRepository.findAll().size();
        // set the field null
        ticket.setEnabled(null);

        // Create the Ticket, which fails.
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);


        restTicketMockMvc.perform(post("/api/tickets").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(ticketDTO)))
            .andExpect(status().isBadRequest());

        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketRepository.findAll().size();
        // set the field null
        ticket.setCreatedAt(null);

        // Create the Ticket, which fails.
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);


        restTicketMockMvc.perform(post("/api/tickets").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(ticketDTO)))
            .andExpect(status().isBadRequest());

        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTickets() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList
        restTicketMockMvc.perform(get("/api/tickets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticket.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].ticketUrl").value(hasItem(DEFAULT_TICKET_URL)))
            .andExpect(jsonPath("$.[*].ticketQRContentType").value(hasItem(DEFAULT_TICKET_QR_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].ticketQR").value(hasItem(Base64Utils.encodeToString(DEFAULT_TICKET_QR))))
            .andExpect(jsonPath("$.[*].ticketFileContentType").value(hasItem(DEFAULT_TICKET_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].ticketFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_TICKET_FILE))))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].validatedAt").value(hasItem(DEFAULT_VALIDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].disabledAt").value(hasItem(DEFAULT_DISABLED_AT.toString())));
    }

    @Test
    @Transactional
    public void getTicket() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get the ticket
        restTicketMockMvc.perform(get("/api/tickets/{id}", ticket.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ticket.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.ticketUrl").value(DEFAULT_TICKET_URL))
            .andExpect(jsonPath("$.ticketQRContentType").value(DEFAULT_TICKET_QR_CONTENT_TYPE))
            .andExpect(jsonPath("$.ticketQR").value(Base64Utils.encodeToString(DEFAULT_TICKET_QR)))
            .andExpect(jsonPath("$.ticketFileContentType").value(DEFAULT_TICKET_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.ticketFile").value(Base64Utils.encodeToString(DEFAULT_TICKET_FILE)))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.validatedAt").value(DEFAULT_VALIDATED_AT.toString()))
            .andExpect(jsonPath("$.disabledAt").value(DEFAULT_DISABLED_AT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTicket() throws Exception {
        // Get the ticket
        restTicketMockMvc.perform(get("/api/tickets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTicket() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        int databaseSizeBeforeUpdate = ticketRepository.findAll().size();

        // Update the ticket
        Ticket updatedTicket = ticketRepository.findById(ticket.getId()).get();
        // Disconnect from session so that the updates on updatedTicket are not directly saved in db
        em.detach(updatedTicket);
        updatedTicket
            .uuid(UPDATED_UUID)
            .ticketUrl(UPDATED_TICKET_URL)
            .ticketQR(UPDATED_TICKET_QR)
            .ticketQRContentType(UPDATED_TICKET_QR_CONTENT_TYPE)
            .ticketFile(UPDATED_TICKET_FILE)
            .ticketFileContentType(UPDATED_TICKET_FILE_CONTENT_TYPE)
            .enabled(UPDATED_ENABLED)
            .createdAt(UPDATED_CREATED_AT)
            .disabledAt(UPDATED_DISABLED_AT);
        TicketDTO ticketDTO = ticketMapper.toDto(updatedTicket);

        restTicketMockMvc.perform(put("/api/tickets").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(ticketDTO)))
            .andExpect(status().isOk());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeUpdate);
        Ticket testTicket = ticketList.get(ticketList.size() - 1);
        assertThat(testTicket.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testTicket.getTicketUrl()).isEqualTo(UPDATED_TICKET_URL);
        assertThat(testTicket.getTicketQR()).isEqualTo(UPDATED_TICKET_QR);
        assertThat(testTicket.getTicketQRContentType()).isEqualTo(UPDATED_TICKET_QR_CONTENT_TYPE);
        assertThat(testTicket.getTicketFile()).isEqualTo(UPDATED_TICKET_FILE);
        assertThat(testTicket.getTicketFileContentType()).isEqualTo(UPDATED_TICKET_FILE_CONTENT_TYPE);
        assertThat(testTicket.isEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testTicket.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTicket.getValidatedAt()).isEqualTo(UPDATED_VALIDATED_AT);
        assertThat(testTicket.getDisabledAt()).isEqualTo(UPDATED_DISABLED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingTicket() throws Exception {
        int databaseSizeBeforeUpdate = ticketRepository.findAll().size();

        // Create the Ticket
        TicketDTO ticketDTO = ticketMapper.toDto(ticket);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTicketMockMvc.perform(put("/api/tickets").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(ticketDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTicket() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        int databaseSizeBeforeDelete = ticketRepository.findAll().size();

        // Delete the ticket
        restTicketMockMvc.perform(delete("/api/tickets/{id}", ticket.getId()).with(csrf())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
