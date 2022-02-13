package eu.cybershu.service.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

/**
 * A Create(save) DTO for the {@link eu.cybershu.domain.Promotor} entity.
 */
@Data
public class PromotorCreateDTO implements Serializable {

    @NotNull
    private String name;

    @NotNull
    private String lastName;

    private String email;

    private String phoneNumber;

    @Size(max = 500)
    private String notes;

    private Set<String> codes;
}
