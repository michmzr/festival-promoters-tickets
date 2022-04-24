package eu.cybershu.service.dto;

import lombok.*;

@Builder
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TicketVerificationStatus {
    VerificationStatus status;

    GuestDTO guest;
    TicketTypeDTO ticketType;
    PromotorDTO promotor;

    String message;
}
