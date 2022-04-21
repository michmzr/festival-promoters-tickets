package eu.cybershu.service;

import eu.cybershu.service.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class TicketVerificationServiceTest {
    private final GuestService guestService = mock(GuestService.class);
    private final TicketService ticketService = mock(TicketService.class);
    private final TicketTypeService ticketTypeService = mock(TicketTypeService.class);

    private TicketVerificationService ticketVerificationService;


    @BeforeEach
    void init() {
        ticketVerificationService = new TicketVerificationService(
            guestService, ticketService,
            ticketTypeService);
    }

    @Test
    void when_not_found_ticket_expect_status_not_found() {
        //given
        UUID uuid = UUID.randomUUID();

        given(ticketService.findByUUID(uuid.toString())).willReturn(Optional.empty());

        //then
        TicketVerificationStatus status = ticketVerificationService.verify(uuid.toString());

        //then
        assertThat(status.getStatus()).isEqualTo(VerificationStatus.NOT_FOUND);
    }

    @Test
    void when_ticket_disabled_expect_status_deactivated() {
        //given
        UUID uuid = UUID.randomUUID();

        GuestDTO guestDTO = GuestDTO
            .builder()
            .id(17L)
            .build();

        TicketTypeDTO ticketTypeDTO = createPass4days();

        TicketDTO ticketDTO = TicketDTO
            .builder()
            .enabled(false)
            .guestId(guestDTO.getId())
            .disabledAt(Instant.now().minusSeconds(30))
            .ticketTypeId(ticketTypeDTO.getId())
            .build();

        given(guestService.findOne(guestDTO.getId())).willReturn(Optional.of(guestDTO));
        given(ticketTypeService.findOne(ticketDTO.getTicketTypeId())).willReturn(Optional.of(ticketTypeDTO));
        given(ticketService.findByUUID(uuid.toString())).willReturn(Optional.of(ticketDTO));

        //then
        TicketVerificationStatus status = ticketVerificationService.verify(uuid.toString());

        //then
        assertThat(status.getStatus()).isEqualTo(VerificationStatus.DEACTIVATED);
        assertThat(status.getGuest()).isEqualTo(guestDTO);
        assertThat(status.getTicketType()).isEqualTo(ticketTypeDTO);
        assertThat(status.getMessage()).isNotBlank();
    }

    @Test
    void when_ticket_valid_expect_status_ok() {
        //given
        UUID uuid = UUID.randomUUID();

        GuestDTO guestDTO = GuestDTO
            .builder()
            .id(17L)
            .build();

        TicketTypeDTO ticketTypeDTO = createPass4days();

        TicketDTO ticketDTO = TicketDTO
            .builder()
            .enabled(true)
            .guestId(guestDTO.getId())
            .disabledAt(Instant.now().minusSeconds(30))
            .ticketTypeId(ticketTypeDTO.getId())
            .build();

        given(guestService.findOne(guestDTO.getId())).willReturn(Optional.of(guestDTO));
        given(ticketTypeService.findOne(ticketDTO.getTicketTypeId())).willReturn(Optional.of(ticketTypeDTO));
        given(ticketService.findByUUID(uuid.toString())).willReturn(Optional.of(ticketDTO));

        //then
        TicketVerificationStatus status = ticketVerificationService.verify(uuid.toString());

        //then
        assertThat(status.getStatus()).isEqualTo(VerificationStatus.OK);
        assertThat(status.getGuest()).isEqualTo(guestDTO);
        assertThat(status.getTicketType()).isEqualTo(ticketTypeDTO);
        assertThat(status.getMessage()).isNotBlank();
    }

    private TicketTypeDTO createPass4days() {
        TicketTypeDTO ticketType = new TicketTypeDTO();
        ticketType.setId(2L);
        ticketType.setName("Karnet 4 dniowy");
        ticketType.setProductId("345");
        ticketType.setProductUrl("http://organic/bilet-4-dni.html");
        return ticketType;
    }
}
