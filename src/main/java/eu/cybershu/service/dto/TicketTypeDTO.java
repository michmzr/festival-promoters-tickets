package eu.cybershu.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A DTO for the {@link eu.cybershu.domain.TicketType} entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketTypeDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 30)
    private String name;

    @Size(max = 500)
    private String notes;

    @Size(max = 50)
    private String productId;

    @Size(max=250)
    private String productUrl;
}
