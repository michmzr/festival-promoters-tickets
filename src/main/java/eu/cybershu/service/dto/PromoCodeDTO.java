package eu.cybershu.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link eu.cybershu.domain.PromoCode} entity.
 */
public class PromoCodeDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 30)
    private String code;

    @Size(max = 500)
    private String notes;

    @NotNull
    private Boolean enabled;

    @NotNull
    private Instant createdAt;

    private Instant disabledAt;

    private Long promotorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public Long getPromotorId() {
        return promotorId;
    }

    public void setPromotorId(Long promotorId) {
        this.promotorId = promotorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PromoCodeDTO)) {
            return false;
        }

        return id != null && id.equals(((PromoCodeDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PromoCodeDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", notes='" + getNotes() + "'" +
            ", enabled='" + isEnabled() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", disabledAt='" + getDisabledAt() + "'" +
            ", promotorId=" + getPromotorId() +
            "}";
    }
}
