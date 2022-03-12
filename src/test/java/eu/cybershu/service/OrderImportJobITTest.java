package eu.cybershu.service;

import eu.cybershu.OrganicPromoTicketsApp;
import eu.cybershu.service.dto.*;
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

        Long productId = 2L;
        var ticketType = ticketTypeService.save(
            TicketTypeDTO.builder()
                .productId(productId.toString())
                .name("Karnet 4 dniowy")
                .productUrl("http://organic/bilet-4-dni.html")
                .build()
        );

        OrderRecord orderRecord = new OrderRecord("X", "Y",
            "email@email.com", "Karnet2",
            productId, 3L, promoCode, "4.23", "Note");

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
        GuestDTO guestDTO = importResult.getGuestDTO();
        assertGuest(guestDTO, orderRecord);

        //and: ticket assertions
        TicketDTO ticketDTO = importResult.getTicketDTO();
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

        Long productId = 2L;
        OrderRecord orderRecord = new OrderRecord("X", "Y",
            "email@email.com", "Karnet2",
            productId, 3L, promoCode, "4.23", "Note");

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
        GuestDTO guestDTO = importResult.getGuestDTO();
        assertThat(guestDTO).isNull();

        //and: ticket assertions
        TicketDTO ticketDTO = importResult.getTicketDTO();
        assertThat(ticketDTO).isNull();
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

}
