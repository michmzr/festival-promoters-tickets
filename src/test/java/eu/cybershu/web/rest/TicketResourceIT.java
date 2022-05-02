package eu.cybershu.web.rest;

import eu.cybershu.OrganicPromoTicketsApp;
import eu.cybershu.domain.Guest;
import eu.cybershu.domain.PromoCode;
import eu.cybershu.domain.Ticket;
import eu.cybershu.domain.TicketType;
import eu.cybershu.repository.TicketRepository;
import eu.cybershu.service.TicketQueryService;
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

    private static final String DEFAULT_TICKET_ORDER_ID = "Xj3Ssi";
    private static final String UPDATED_TICKET_ORDER_ID = "Xj3Ssi8";

    private static final String DEFAULT_TICKET_PRICE = "199.99";
    private static final String UPDATED_TICKET_PRICE = "11.45";

    private static final String DEFAULT_TICKET_DISCOUNT = "199.99";
    private static final String UPDATED_TICKET_DISCOUNT = "11.45";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DISABLED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DISABLED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);


    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketQueryService ticketQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTicketMockMvc;

    private Ticket ticket;

    private TicketType ticketType;
    private Guest guest;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ticket createEntity(EntityManager em) {
        TicketType ticketType = TicketTypeResourceIT.createEntity(em);
        em.persist(ticketType);

        Guest guest = GuestResourceIT.createEntity(em);
        em.persist(guest);

        return new Ticket()
            .uuid(DEFAULT_UUID)
            .ticketUrl(DEFAULT_TICKET_URL)
            .ticketQR(DEFAULT_TICKET_QR)
            .ticketQRContentType(DEFAULT_TICKET_QR_CONTENT_TYPE)
            .ticketFile(DEFAULT_TICKET_FILE)
            .ticketFileContentType(DEFAULT_TICKET_FILE_CONTENT_TYPE)
            .ticketPrice(DEFAULT_TICKET_PRICE)
            .ticketDiscount(DEFAULT_TICKET_DISCOUNT)
            .ticketType(ticketType)
            .orderId(DEFAULT_TICKET_ORDER_ID)
            .enabled(DEFAULT_ENABLED)
            .createdAt(DEFAULT_CREATED_AT)
            .guest(guest)
            .disabledAt(DEFAULT_DISABLED_AT);
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ticket createUpdatedEntity(EntityManager em) {
        TicketType ticketType = TicketTypeResourceIT.createEntity(em);
        em.persist(ticketType);

        Guest guest = GuestResourceIT.createEntity(em);
        em.persist(guest);

        return new Ticket()
            .uuid(UPDATED_UUID)
            .ticketUrl(UPDATED_TICKET_URL)
            .ticketQR(UPDATED_TICKET_QR)
            .ticketQRContentType(UPDATED_TICKET_QR_CONTENT_TYPE)
            .ticketFile(UPDATED_TICKET_FILE)
            .ticketFileContentType(UPDATED_TICKET_FILE_CONTENT_TYPE)
            .ticketPrice(UPDATED_TICKET_PRICE)
            .ticketDiscount(UPDATED_TICKET_DISCOUNT)
            .orderId(UPDATED_TICKET_ORDER_ID)
            .enabled(UPDATED_ENABLED)
            .ticketType(ticketType)
            .guest(guest)
            .createdAt(UPDATED_CREATED_AT)
            .disabledAt(UPDATED_DISABLED_AT);
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
        assertThat(testTicket.isEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testTicket.getOrderId()).isEqualTo(DEFAULT_TICKET_ORDER_ID);
        assertThat(testTicket.getTicketPrice()).isEqualTo(DEFAULT_TICKET_PRICE);
        assertThat(testTicket.getTicketDiscount()).isEqualTo(DEFAULT_TICKET_DISCOUNT);
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
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_TICKET_ORDER_ID)))
            .andExpect(jsonPath("$.[*].ticketPrice").value(hasItem(DEFAULT_TICKET_PRICE)))
            .andExpect(jsonPath("$.[*].ticketDiscount").value(hasItem(DEFAULT_TICKET_DISCOUNT)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
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
            .andExpect(jsonPath("$.orderId").value(hasItem(DEFAULT_TICKET_ORDER_ID)))
            .andExpect(jsonPath("$.ticketPrice").value(hasItem(DEFAULT_TICKET_PRICE)))
            .andExpect(jsonPath("$.ticketDiscount").value(hasItem(DEFAULT_TICKET_DISCOUNT)))
            .andExpect(jsonPath("$.ticketQRContentType").value(DEFAULT_TICKET_QR_CONTENT_TYPE))
            .andExpect(jsonPath("$.ticketQR").value(Base64Utils.encodeToString(DEFAULT_TICKET_QR)))
            .andExpect(jsonPath("$.ticketFileContentType").value(DEFAULT_TICKET_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.ticketFile").value(Base64Utils.encodeToString(DEFAULT_TICKET_FILE)))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.disabledAt").value(DEFAULT_DISABLED_AT.toString()));
    }


    @Test
    @Transactional
    public void getTicketsByIdFiltering() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        Long id = ticket.getId();

        defaultTicketShouldBeFound("id.equals=" + id);
        defaultTicketShouldNotBeFound("id.notEquals=" + id);

        defaultTicketShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTicketShouldNotBeFound("id.greaterThan=" + id);

        defaultTicketShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTicketShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllTicketsByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultTicketShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the ticketList where uuid equals to UPDATED_UUID
        defaultTicketShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    public void getAllTicketsByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where uuid is not null
        defaultTicketShouldBeFound("uuid.specified=true");

        // Get all the ticketList where uuid is null
        defaultTicketShouldNotBeFound("uuid.specified=false");
    }

    @Test
    @Transactional
    public void getAllTicketsByTicketUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where ticketUrl equals to DEFAULT_TICKET_URL
        defaultTicketShouldBeFound("ticketUrl.equals=" + DEFAULT_TICKET_URL);

        // Get all the ticketList where ticketUrl equals to UPDATED_TICKET_URL
        defaultTicketShouldNotBeFound("ticketUrl.equals=" + UPDATED_TICKET_URL);
    }

    @Test
    @Transactional
    public void getAllTicketsByTicketUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where ticketUrl not equals to DEFAULT_TICKET_URL
        defaultTicketShouldNotBeFound("ticketUrl.notEquals=" + DEFAULT_TICKET_URL);

        // Get all the ticketList where ticketUrl not equals to UPDATED_TICKET_URL
        defaultTicketShouldBeFound("ticketUrl.notEquals=" + UPDATED_TICKET_URL);
    }

    @Test
    @Transactional
    public void getAllTicketsByTicketUrlIsInShouldWork() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where ticketUrl in DEFAULT_TICKET_URL or UPDATED_TICKET_URL
        defaultTicketShouldBeFound("ticketUrl.in=" + DEFAULT_TICKET_URL + "," + UPDATED_TICKET_URL);

        // Get all the ticketList where ticketUrl equals to UPDATED_TICKET_URL
        defaultTicketShouldNotBeFound("ticketUrl.in=" + UPDATED_TICKET_URL);
    }

    @Test
    @Transactional
    public void getAllTicketsByTicketUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where ticketUrl is not null
        defaultTicketShouldBeFound("ticketUrl.specified=true");

        // Get all the ticketList where ticketUrl is null
        defaultTicketShouldNotBeFound("ticketUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllTicketsByTicketUrlContainsSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where ticketUrl contains DEFAULT_TICKET_URL
        defaultTicketShouldBeFound("ticketUrl.contains=" + DEFAULT_TICKET_URL);

        // Get all the ticketList where ticketUrl contains UPDATED_TICKET_URL
        defaultTicketShouldNotBeFound("ticketUrl.contains=" + UPDATED_TICKET_URL);
    }

    @Test
    @Transactional
    public void getAllTicketsByTicketUrlNotContainsSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where ticketUrl does not contain DEFAULT_TICKET_URL
        defaultTicketShouldNotBeFound("ticketUrl.doesNotContain=" + DEFAULT_TICKET_URL);

        // Get all the ticketList where ticketUrl does not contain UPDATED_TICKET_URL
        defaultTicketShouldBeFound("ticketUrl.doesNotContain=" + UPDATED_TICKET_URL);
    }


    @Test
    @Transactional
    public void getAllTicketsByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where enabled equals to DEFAULT_ENABLED
        defaultTicketShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the ticketList where enabled equals to UPDATED_ENABLED
        defaultTicketShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllTicketsByEnabledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where enabled not equals to DEFAULT_ENABLED
        defaultTicketShouldNotBeFound("enabled.notEquals=" + DEFAULT_ENABLED);

        // Get all the ticketList where enabled not equals to UPDATED_ENABLED
        defaultTicketShouldBeFound("enabled.notEquals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllTicketsByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultTicketShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the ticketList where enabled equals to UPDATED_ENABLED
        defaultTicketShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllTicketsByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where enabled is not null
        defaultTicketShouldBeFound("enabled.specified=true");

        // Get all the ticketList where enabled is null
        defaultTicketShouldNotBeFound("enabled.specified=false");
    }

    @Test
    @Transactional
    public void getAllTicketsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where createdAt equals to DEFAULT_CREATED_AT
        defaultTicketShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the ticketList where createdAt equals to UPDATED_CREATED_AT
        defaultTicketShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllTicketsByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where createdAt not equals to DEFAULT_CREATED_AT
        defaultTicketShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the ticketList where createdAt not equals to UPDATED_CREATED_AT
        defaultTicketShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllTicketsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultTicketShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the ticketList where createdAt equals to UPDATED_CREATED_AT
        defaultTicketShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllTicketsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where createdAt is not null
        defaultTicketShouldBeFound("createdAt.specified=true");

        // Get all the ticketList where createdAt is null
        defaultTicketShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllTicketsByDisabledAtIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where disabledAt equals to DEFAULT_DISABLED_AT
        defaultTicketShouldBeFound("disabledAt.equals=" + DEFAULT_DISABLED_AT);

        // Get all the ticketList where disabledAt equals to UPDATED_DISABLED_AT
        defaultTicketShouldNotBeFound("disabledAt.equals=" + UPDATED_DISABLED_AT);
    }

    @Test
    @Transactional
    public void getAllTicketsByDisabledAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where disabledAt not equals to DEFAULT_DISABLED_AT
        defaultTicketShouldNotBeFound("disabledAt.notEquals=" + DEFAULT_DISABLED_AT);

        // Get all the ticketList where disabledAt not equals to UPDATED_DISABLED_AT
        defaultTicketShouldBeFound("disabledAt.notEquals=" + UPDATED_DISABLED_AT);
    }

    @Test
    @Transactional
    public void getAllTicketsByDisabledAtIsInShouldWork() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where disabledAt in DEFAULT_DISABLED_AT or UPDATED_DISABLED_AT
        defaultTicketShouldBeFound("disabledAt.in=" + DEFAULT_DISABLED_AT + "," + UPDATED_DISABLED_AT);

        // Get all the ticketList where disabledAt equals to UPDATED_DISABLED_AT
        defaultTicketShouldNotBeFound("disabledAt.in=" + UPDATED_DISABLED_AT);
    }

    @Test
    @Transactional
    public void getAllTicketsByDisabledAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where disabledAt is not null
        defaultTicketShouldBeFound("disabledAt.specified=true");

        // Get all the ticketList where disabledAt is null
        defaultTicketShouldNotBeFound("disabledAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllTicketsByTicketTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);
        TicketType ticketType = TicketTypeResourceIT.createEntity(em);
        em.persist(ticketType);
        em.flush();
        ticket.setTicketType(ticketType);
        ticketRepository.saveAndFlush(ticket);
        Long ticketTypeId = ticketType.getId();

        // Get all the ticketList where ticketType equals to ticketTypeId
        defaultTicketShouldBeFound("ticketTypeId.equals=" + ticketTypeId);

        // Get all the ticketList where ticketType equals to ticketTypeId + 1
        defaultTicketShouldNotBeFound("ticketTypeId.equals=" + (ticketTypeId + 1));
    }


    @Test
    @Transactional
    public void getAllTicketsByPromoCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);
        PromoCode promoCode = PromoCodeResourceIT.createEntity(em);
        em.persist(promoCode);
        em.flush();
        ticket.setPromoCode(promoCode);
        ticketRepository.saveAndFlush(ticket);
        Long promoCodeId = promoCode.getId();

        // Get all the ticketList where promoCode equals to promoCodeId
        defaultTicketShouldBeFound("promoCodeId.equals=" + promoCodeId);

        // Get all the ticketList where promoCode equals to promoCodeId + 1
        defaultTicketShouldNotBeFound("promoCodeId.equals=" + (promoCodeId + 1));
    }


    @Test
    @Transactional
    public void getAllTicketsByGuestIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);
        Guest guest = GuestResourceIT.createEntity(em);
        em.persist(guest);
        em.flush();
        ticket.setGuest(guest);
        ticketRepository.saveAndFlush(ticket);
        Long guestId = guest.getId();

        // Get all the ticketList where guest equals to guestId
        defaultTicketShouldBeFound("guestId.equals=" + guestId);

        // Get all the ticketList where guest equals to guestId + 1
        defaultTicketShouldNotBeFound("guestId.equals=" + (guestId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTicketShouldBeFound(String filter) throws Exception {
        restTicketMockMvc.perform(get("/api/tickets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticket.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].ticketUrl").value(hasItem(DEFAULT_TICKET_URL)))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_TICKET_ORDER_ID)))
            .andExpect(jsonPath("$.[*].ticketPrice").value(hasItem(DEFAULT_TICKET_PRICE)))
            .andExpect(jsonPath("$.[*].ticketDiscount").value(hasItem(DEFAULT_TICKET_DISCOUNT)))
            .andExpect(jsonPath("$.[*].ticketQRContentType").value(hasItem(DEFAULT_TICKET_QR_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].ticketQR").value(hasItem(Base64Utils.encodeToString(DEFAULT_TICKET_QR))))
            .andExpect(jsonPath("$.[*].ticketFileContentType").value(hasItem(DEFAULT_TICKET_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].ticketFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_TICKET_FILE))))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].disabledAt").value(hasItem(DEFAULT_DISABLED_AT.toString())));

        // Check, that the count call also returns 1
        restTicketMockMvc.perform(get("/api/tickets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTicketShouldNotBeFound(String filter) throws Exception {
        restTicketMockMvc.perform(get("/api/tickets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTicketMockMvc.perform(get("/api/tickets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
