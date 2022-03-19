package eu.cybershu.service;

import eu.cybershu.OrganicPromoTicketsApp;
import eu.cybershu.service.dto.*;
import lombok.SneakyThrows;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Rollback
@Transactional
@SpringBootTest(classes = OrganicPromoTicketsApp.class)
class OrderImportJobITTest {

    @Autowired
    private TicketTypeService ticketTypeService;

    @Autowired
    private PromotorService promotorService;

    @Autowired
    private GuestService guestService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private OrderImportJob orderImportJob = null;

    @BeforeEach
    void setUp() {
    }

    @Test
    void given_new_guest_expect_created_guest_and_ticket() {
        //given
        String promoCode = "promo";

        promotorService.create(PromotorCreateDTO.builder()
            .email("promo@test.pl")
            .name("Mikołaj")
            .lastName("Kopernik")
            .newPromoCodes(Set.of(promoCode, "kodzik90", "Miko120"))
            .build());

        String productId = "2";
        var ticketType = ticketTypeService.save(
            TicketTypeDTO.builder()
                .productId(productId)
                .name("Karnet 4 dniowy")
                .productUrl("http://organic/bilet-4-dni.html")
                .build()
        );

        OrderRecord orderRecord = new OrderRecord("X", "Y",
            "email@email.com", "Karnet2",
            productId, "3", promoCode, "4.23", "Note");

        //when
        OrderImportResult importResult = orderImportJob.processRecord(orderRecord);

        //then
        assertThat(importResult.getOrderRecord()).isEqualTo(orderRecord);
        assertThat(importResult.success()).isTrue();
        assertThat(importResult.getMessages()).isEmpty();

        //and: validation assertions
        assertThat(importResult.getValidation().valid).isTrue();
        assertThat(importResult.getValidation().messages).isEmpty();
        assertThat(importResult.getValidation().fieldNames).isEmpty();

        //and: guest assertions
        GuestDTO guestDTO = importResult.getGuest();
        assertGuest(guestDTO, orderRecord);

        //and: ticket assertions
        TicketDTO ticketDTO = importResult.getTicket();
        assertTicketDTO(ticketDTO, orderRecord, ticketType);
    }

    @Test
    void given_new_guest_and_not_found_productId_expect_failed_import() {
        //given
        String promoCode = "promo";

        promotorService.create(PromotorCreateDTO.builder()
            .email("promo@test.pl")
            .name("Mikołaj")
            .lastName("Kopernik")
            .newPromoCodes(Set.of(promoCode, "kodzik90", "Miko120"))
            .build());

        String productId = "2";
        OrderRecord orderRecord = new OrderRecord("X", "Y",
            "email@email.com", "Karnet2",
            productId, "3", promoCode, "4.23", "Note");

        //when
        OrderImportResult importResult = orderImportJob.processRecord(orderRecord);

        //then
        assertThat(importResult.getOrderRecord()).isEqualTo(orderRecord);
        assertThat(importResult.success()).isFalse();
        assertThat(importResult.getMessages().toString())
            .containsIgnoringCase("Not recognised ticket type with defined product id");

        //and: validation assertions
        assertThat(importResult.getValidation().valid).isTrue();
        assertThat(importResult.getValidation().messages).isEmpty();
        assertThat(importResult.getValidation().fieldNames).isEmpty();

        //and: guest assertions
        GuestDTO guestDTO = importResult.getGuest();
        assertThat(guestDTO).isNull();

        //and: ticket assertions
        TicketDTO ticketDTO = importResult.getTicket();
        assertThat(ticketDTO).isNull();
    }

    @Test
    @Transactional
    @SneakyThrows
    void given_already_imported_order_expected_return_registerd_ticket() {
        //given
        String productId = "22";
        TicketTypeDTO ticketType = ticketTypeService.save(
            ticketTypeDTO(10L, "Bilet 4 dni", "ticket-url", productId));

        String orderId = "3";
        GuestDTO guest = guestService.save(createGuest("X", "Y", "email@email.com"));
        TicketDTO ticket = ticketService.create(ticketCreateDTO(guest.getId(), ticketType.getId(), orderId));

        OrderRecord orderRecord = new OrderRecord("X", "Y",
            "email@email.com", "Karnet2", productId, orderId, "s", "4.23", "Note");

        //when
        OrderImportResult importResult = orderImportJob.processRecord(orderRecord);

        //then
        assertThat(importResult.success()).isTrue();
        assertThat(importResult.getMessages().toString())
            .containsIgnoringCase("Found already registered ticket");
        assertThat(importResult.getTicket()).isEqualTo(ticket);
        assertThat(importResult.getGuest()).isEqualTo(guest);
    }

    private void assertGuest(GuestDTO guestDTO, OrderRecord orderRecord) {
        assertThat(guestDTO).isNotNull();
        assertThat(guestDTO.getId()).isNotNull();

        assertThat(guestDTO.getName()).isEqualTo(orderRecord.getGuestName());
        assertThat(guestDTO.getLastName()).isEqualTo(orderRecord.getGuestLastName());
        assertThat(guestDTO.getEmail()).isEqualTo(orderRecord.getGuestEmail());
        assertThat(guestDTO.getCreatedAt()).isNotNull();
        assertThat(guestDTO.getEnabled()).isTrue();
    }

    private void assertTicketDTO(TicketDTO ticketDTO, OrderRecord orderRecord, TicketTypeDTO ticketType) {
        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(ticketDTO).isNotNull();
        softly.assertThat(ticketDTO.getId()).isNotNull();

        softly.assertThat(ticketDTO.getUuid()).isNotNull();
        softly.assertThat(ticketDTO.getTicketUrl()).isNotBlank();
        softly.assertThat(ticketDTO.getTicketQR()).isNotEmpty();
        softly.assertThat(ticketDTO.getTicketQRContentType()).isNotBlank();
        softly.assertThat(ticketDTO.getTicketFile()).isNotEmpty();
        softly.assertThat(ticketDTO.getTicketFileContentType()).isNotBlank();
        softly.assertThat(ticketDTO.getEnabled()).isTrue();
        softly.assertThat(ticketDTO.getCreatedAt()).isNotNull();
        softly.assertThat(ticketDTO.getTicketPrice()).isEqualTo(orderRecord.getPrice());
        softly.assertThat(ticketDTO.getTicketTypeId()).isEqualTo(ticketDTO.getId());
        softly.assertThat(ticketDTO.getPromoCodeId()).isNotNull();
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

}
