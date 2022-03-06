package eu.cybershu.service.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

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
}
