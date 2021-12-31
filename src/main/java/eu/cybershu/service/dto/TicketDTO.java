package eu.cybershu.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Lob;

/**
 * A DTO for the {@link eu.cybershu.domain.Ticket} entity.
 */
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


    private Long ticketTypeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getTicketUrl() {
        return ticketUrl;
    }

    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
    }

    public byte[] getTicketQR() {
        return ticketQR;
    }

    public void setTicketQR(byte[] ticketQR) {
        this.ticketQR = ticketQR;
    }

    public String getTicketQRContentType() {
        return ticketQRContentType;
    }

    public void setTicketQRContentType(String ticketQRContentType) {
        this.ticketQRContentType = ticketQRContentType;
    }

    public byte[] getTicketFile() {
        return ticketFile;
    }

    public void setTicketFile(byte[] ticketFile) {
        this.ticketFile = ticketFile;
    }

    public String getTicketFileContentType() {
        return ticketFileContentType;
    }

    public void setTicketFileContentType(String ticketFileContentType) {
        this.ticketFileContentType = ticketFileContentType;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getDisabledAt() {
        return disabledAt;
    }

    public void setDisabledAt(Instant disabledAt) {
        this.disabledAt = disabledAt;
    }

    public Long getTicketTypeId() {
        return ticketTypeId;
    }

    public void setTicketTypeId(Long ticketTypeId) {
        this.ticketTypeId = ticketTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TicketDTO)) {
            return false;
        }

        return id != null && id.equals(((TicketDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketDTO{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", ticketUrl='" + getTicketUrl() + "'" +
            ", ticketQR='" + getTicketQR() + "'" +
            ", ticketFile='" + getTicketFile() + "'" +
            ", enabled='" + isEnabled() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", disabledAt='" + getDisabledAt() + "'" +
            ", ticketTypeId=" + getTicketTypeId() +
            "}";
    }
}
