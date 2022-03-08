package eu.cybershu.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private Long ticketTypeId;

    private Long promoCodeId;

    private Long guestId;
    private Long promotorId;
}
