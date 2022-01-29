package eu.cybershu.service.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;

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

    @NotNull
    private Long ticketTypeId;

    @NotNull
    private Long promotorId;
}
