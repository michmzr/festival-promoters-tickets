package eu.cybershu.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the {@link eu.cybershu.domain.Ticket} entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketCreateDTO implements Serializable {
    private String ticketPrice;

    @NotNull
    private Long ticketTypeId;

    private Long promoCodeId;

    @NotNull
    private Long guestId;
    private Long promotorId;

    @NotNull
    private String orderId;
}
