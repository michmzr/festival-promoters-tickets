package eu.cybershu.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link eu.cybershu.domain.Ticket} entity.
 */
@Data
public class TicketCreateDTO implements Serializable {
    private String ticketPrice;

    private Long ticketTypeId;

    private Long promoCodeId;

    private Long guestId;
    private Long promotorId;
}
