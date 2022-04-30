package eu.cybershu.service.dto;

import lombok.*;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * A DTO for the {@link eu.cybershu.domain.Ticket} entity.
 */
@Data
@Builder
@ToString(exclude = {"ticketQR", "ticketQRContentType", "ticketFile", "ticketFileContentType"})
@AllArgsConstructor
@NoArgsConstructor
public class TicketDTO implements Serializable {
    private Long id;

    @NotNull
    private UUID uuid;

    @NotNull
    private String ticketUrl;

    @Lob
    private byte[] ticketQR;

    private String ticketQRContentType;
    @Lob
    private byte[] ticketFile;

    private String ticketFileContentType;
    @NotNull
    private Boolean enabled;

    private Instant createdAt;

    private Instant disabledAt;

    private Instant validatedAt;

    private BigDecimal ticketPrice;
    private BigDecimal ticketDiscount;

    @NotNull
    private Long ticketTypeId;

    private Long promoCodeId;

    @NotNull
    private String orderId;

    @NotNull
    private Long guestId;
}
