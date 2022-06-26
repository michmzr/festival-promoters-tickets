package eu.cybershu.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A DTO for the {@link eu.cybershu.domain.Ticket} entity.
 */
@Data
@Builder
@ToString(exclude = {"ticketQRContentType", "ticketFileContentType"})
@AllArgsConstructor
@NoArgsConstructor
public class TicketListingItemDTO implements Serializable {
    private Long id;

    @NotNull
    private UUID uuid;

    @NotNull
    private String ticketUrl;

    private String ticketQRContentType;

    private String ticketFileContentType;

    @NotNull
    private Boolean enabled;

    private Instant createdAt;
    private Instant disabledAt;

    /**
     * Ticket price paid by guest
     */
    private BigDecimal ticketPrice;

    /**
     * Ticket discount in PLN
     */
    private BigDecimal ticketDiscount;

    @NotNull
    private Long ticketTypeId;

    /**
     * Promo code used for getting discount - it identifies promotor
     */
    private Long promoCodeId;

    /**
     * Order id - any string which can represent order number
     */
    @NotNull
    private String orderId;

    @NotNull
    private Long guestId;

    private String artistName;
}
