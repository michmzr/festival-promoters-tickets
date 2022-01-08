package eu.cybershu.service.dto;

import lombok.Data;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Lob;

/**
 * A DTO for the {@link eu.cybershu.domain.Ticket} entity.
 */
@Data
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

    @NotNull
    private Instant createdAt;

    private Instant disabledAt;

    public Long getTicketTypeId() {
        return ticketTypeId;
    }

    private Long ticketTypeId;

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketDTO{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", ticketUrl='" + getTicketUrl() + "'" +
            ", enabled='" + enabled + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", disabledAt='" + getDisabledAt() + "'" +
            ", ticketTypeId=" + getTicketTypeId() +
            "}";
    }
}
