package eu.cybershu.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.cybershu.OrganicPromoTicketsApp;
import eu.cybershu.domain.Guest;
import eu.cybershu.domain.Ticket;
import eu.cybershu.domain.TicketType;
import eu.cybershu.repository.GuestRepository;
import eu.cybershu.repository.TicketRepository;
import eu.cybershu.repository.TicketTypeRepository;
import eu.cybershu.service.TicketTypeService;
import eu.cybershu.service.dto.*;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Rollback
@AutoConfigureMockMvc
@WithMockUser
@SpringBootTest(classes = OrganicPromoTicketsApp.class)
public class TicketVerificationResourceIT {
    @Autowired
    private TicketTypeRepository ticketTypeRepository;

    @Autowired
    private TicketTypeService ticketTypeService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private MockMvc mockMVC;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void initTest() {
    }

    @Test
    @Transactional
    public void given_not_existing_ticket_expect_UNPROCESSABLE_ENTITY() throws Exception {
        //given
        UUID uuid = UUID.randomUUID();

        // expect
        mockMVC
            .perform(get("/api/ticket/verify/" + uuid)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()));
    }

    @Test
    @Transactional
    public void given_existing_enabled_ticket_expect_ticket_details() throws Exception {
        //given
        Guest guest = guest();
        TicketType ticketType = createPass4days();

        UUID uuid = UUID.randomUUID();
        Ticket ticket = createTicket(uuid, guest, ticketType, true);

        // when
        var callResult = mockMVC
            .perform(get("/api/ticket/verify/" + uuid)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        //then
        TicketVerificationStatus status = asStatus(callResult);
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(status.getStatus()).isEqualTo(VerificationStatus.OK);

        softAssertions.assertThat(status.getTicketType()).isNotNull();
        softAssertions.assertThat(status.getTicketType().getId()).isEqualTo(ticketType.getId());

        softAssertions.assertThat(status.getGuest()).isNotNull();
        softAssertions.assertThat(status.getGuest().getId()).isEqualTo(guest.getId());

        //when
        ticket = ticketRepository.getOne(ticket.getId());

        //then
        assertThat(ticket.getValidatedAt()).isBeforeOrEqualTo(Instant.now());
    }

    @Test
    @Transactional
    public void given_existing_enabled_ticket_checked_before_expect_200_and_status_already_checked() throws Exception {
        //given
        Guest guest = guest();
        TicketType ticketType = createPass4days();

        UUID uuid = UUID.randomUUID();
        Ticket ticket = createTicket(uuid, guest, ticketType, true);

        // when
        ticket.setValidatedAt(Instant.now().minusSeconds(3600));
        ticketRepository.save(ticket);

        //when: verify again
        mockMVC
            .perform(get("/api/ticket/verify/" + uuid)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(VerificationStatus.ALREADY_VALIDATED.name()));
    }

    @Test
    @Transactional
    public void given_existing_disabled_ticket_expect_ticket_details_with_status_deactivated() throws Exception {
        //given
        Guest guest = guest();
        TicketType ticketType = createPass4days();

        UUID uuid = UUID.randomUUID();
        createTicket(uuid, guest, ticketType, false);

        // when
        var callResult = mockMVC
            .perform(get("/api/ticket/verify/" + uuid)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        //then
        TicketVerificationStatus status = asStatus(callResult);
        SoftAssertions soft = new SoftAssertions();

        soft.assertThat(status.getStatus()).isEqualTo(VerificationStatus.DEACTIVATED);

        soft.assertThat(status.getTicketType()).isNotNull();
        soft.assertThat(status.getTicketType().getId()).isEqualTo(ticketType.getId());

        soft.assertThat(status.getGuest()).isNotNull();
        soft.assertThat(status.getGuest().getId()).isEqualTo(guest.getId());
    }

    @SneakyThrows
    private TicketVerificationStatus asStatus(MvcResult mvcResult) {
        String responseJSON = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(responseJSON, TicketVerificationStatus.class);
    }

    private GuestCreateDTO createGuest(String name, String lastName, String email) {
        return GuestCreateDTO
            .builder()
            .name(name)
            .lastName(lastName)
            .email(email)
            .build();
    }

    private TicketCreateDTO ticketCreateDTO(Long guestId, Long ticketTypeId, String orderId) {
        return TicketCreateDTO
            .builder()
            .guestId(guestId)
            .ticketTypeId(ticketTypeId)
            .orderId(orderId)
            .build();
    }

    private TicketTypeDTO ticketTypeDTO(Long id, String name, String productUrl, String productId) {
        return TicketTypeDTO
            .builder()
            .id(id)
            .name(name)
            .productUrl(productUrl)
            .productId(productId)
            .build();
    }

    private TicketType createPass4days() {
        TicketType ticketType = new TicketType();
        ticketType.setName("Karnet 4 dniowy");
        ticketType.setProductId("345");
        ticketType.setProductUrl("http://organic/bilet-4-dni.html");

        return ticketTypeRepository.save(ticketType);
    }

    private Guest guest() {
        Guest guest = new Guest()
            .name("Nme")
            .lastName("SurName")
            .email("email@email.com")
            .createdAt(Instant.now())
            .enabled(true);

        return guestRepository.save(guest);
    }

    private Ticket createTicket(UUID uuid, Guest guest, TicketType ticketType, Boolean enabled) {
        Ticket ticket = new Ticket();
        ticket.setUuid(uuid);
        ticket.setGuest(guest);
        ticket.setPromotor(null);
        ticket.setPromoCode(null);
        ticket.setOrderId("123");
        ticket.setTicketType(ticketType);

        ticket.enabled(enabled);
        if (!enabled)
            ticket.disabledAt(Instant.now().minusSeconds(90));

        ticket.setTicketQR(RandomUtils.nextBytes(1));
        ticket.setTicketQRContentType("png");
        ticket.setTicketFile(RandomUtils.nextBytes(1));
        ticket.setTicketFileContentType("png");
        ticket.setTicketUrl("http://localhost/api/ticket/verification/" + uuid);
        ticket.setCreatedAt(Instant.now());

        return ticketRepository.save(ticket);
    }

}
