package eu.cybershu.service.dto;

import lombok.Data;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link eu.cybershu.domain.Guest} entity.
 */
@Data
public class GuestDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String lastName;

    @NotNull
    private String email;

    private String phoneNumber;

    @Size(max = 500)
    private String notes;

    private Instant createdAt;

    @NotNull
    private Boolean enabled;

    private Long ticketId;

    private Long promotorId;

    // prettier-ignore
    @Override
    public String toString() {
        return "GuestDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", notes='" + getNotes() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", enabled='" + enabled + "'" +
            ", ticketId=" + getTicketId() +
            ", promotorId=" + getPromotorId() +
            "}";
    }
}
