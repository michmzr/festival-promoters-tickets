package eu.cybershu.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 * A Create(save) DTO for the {@link eu.cybershu.domain.Promotor} entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromotorCreateDTO implements Serializable {

    @NotNull
    private String name;

    @NotNull
    private String lastName;

    private String email;

    private String phoneNumber;

    @Size(max = 500)
    private String notes;

    private Set<String> newPromoCodes;
}
