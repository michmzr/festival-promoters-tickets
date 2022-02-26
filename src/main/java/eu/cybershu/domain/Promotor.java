package eu.cybershu.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Promotor.
 */
@Entity
@Table(name = "promotor")
public class Promotor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Size(max = 500)
    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "created_at")
    private Instant createdAt;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @OneToMany(mappedBy = "promotor")
    private Set<Guest> guests = new HashSet<>();

    @OneToMany(mappedBy = "promotor", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<PromoCode> promoCodes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Promotor name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public Promotor lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public Promotor email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Promotor phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNotes() {
        return notes;
    }

    public Promotor notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Promotor createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public Promotor enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Guest> getGuests() {
        return guests;
    }

    public Promotor guests(Set<Guest> guests) {
        this.guests = guests;
        return this;
    }

    public Promotor addGuest(Guest guest) {
        this.guests.add(guest);
        guest.setPromotor(this);
        return this;
    }

    public Promotor removeGuest(Guest guest) {
        this.guests.remove(guest);
        guest.setPromotor(null);
        return this;
    }

    public void setGuests(Set<Guest> guests) {
        this.guests = guests;
    }

    public Set<PromoCode> getPromoCodes() {
        return promoCodes;
    }

    public Promotor promoCodes(Set<PromoCode> promoCodes) {
        this.promoCodes = promoCodes;
        return this;
    }

    public Promotor addPromoCode(PromoCode promoCode) {
        this.promoCodes.add(promoCode);
        promoCode.setPromotor(this);
        return this;
    }

    public Promotor removePromoCode(PromoCode promoCode) {
        this.promoCodes.remove(promoCode);
        promoCode.setPromotor(null);
        return this;
    }

    public void setPromoCodes(Set<PromoCode> promoCodes) {
        this.promoCodes = promoCodes;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Promotor)) {
            return false;
        }
        return id != null && id.equals(((Promotor) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Promotor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", notes='" + getNotes() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", enabled='" + isEnabled() + "'" +
            "}";
    }
}
