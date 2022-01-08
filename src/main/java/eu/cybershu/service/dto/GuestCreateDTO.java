package eu.cybershu.service.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

@Data
public class GuestCreateDTO implements Serializable {

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

    private Long ticketTypeId; //todo co z tym

    private Long promotorId;
}
