package eu.cybershu.service.dto;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;

import java.math.BigDecimal;

public class TicketDTOTest {

    @RequiredArgsConstructor(staticName = "assertThat")
    public static class TicketDTOAssert {
        private final TicketDTO actual;

        public TicketDTOAssert uuidIdNotNull() {
            Assertions.assertThat(actual.getUuid()).isNotNull();
            return this;
        }

        public TicketDTOAssert hasOrderIdEqualTo(String orderID) {
            Assertions.assertThat(actual.getOrderId()).isEqualTo(orderID);
            return this;
        }

        public TicketDTOAssert hasPromoCodeEqualTo(String promoCode) {
            Assertions.assertThat(actual.getPromoCodeId()).isEqualTo(promoCode);
            return this;
        }


        public TicketDTOAssert ticketQRISNotEmpty() {
            Assertions.assertThat(actual.getTicketQR()).isNotEmpty();
            return this;
        }

        public TicketDTOAssert ticketFileIsNotEmpty() {
            Assertions.assertThat(actual.getTicketFile()).isNotEmpty();
            return this;
        }

        public TicketDTOAssert hasPriceEqualTo(BigDecimal ticketPrice) {
            Assertions.assertThat(actual.getTicketPrice()).isEqualTo(ticketPrice);
            return this;
        }

        public TicketDTOAssert hasPriceEqualTo(String ticketPrice) {
            BigDecimal bd = BigDecimal.valueOf(Double.parseDouble(ticketPrice));
            return hasPriceEqualTo(bd);
        }

        public TicketDTOAssert hasDiscountEqualTo(BigDecimal ticketDiscountInPln) {
            Assertions.assertThat(actual.getTicketDiscount()).isEqualTo(ticketDiscountInPln);
            return this;
        }

        public TicketDTOAssert hasDiscountEqualTo(String ticketDiscountInPln) {
            BigDecimal bd = BigDecimal.valueOf(Double.parseDouble(ticketDiscountInPln));
            return hasPriceEqualTo(bd);
        }

        public TicketDTOAssert isGuestIdEqualTo(Long guestId) {
            Assertions.assertThat(actual.getGuestId()).isEqualTo(guestId);
            return this;
        }
    }
}
