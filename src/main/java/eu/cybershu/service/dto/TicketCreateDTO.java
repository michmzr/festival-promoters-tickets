package eu.cybershu.service.dto;

import eu.cybershu.domain.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link Ticket} entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketCreateDTO implements Serializable {
    @NotNull
    private Long ticketTypeId;

    private Long promotorId;
    private Long promoCodeId;

    private String artistName;

    @NotNull
    private Long guestId;

    @NotNull
    private String orderId;

    @NotNull
    private BigDecimal ticketPrice;

    @NotNull
    private BigDecimal ticketDiscount;
}
