package eu.cybershu.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * A Ticket.
 */
@Entity
@Table(name = "ticket")
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @NotNull
    @Column(name = "ticket_url", nullable = false)
    private String ticketUrl;

    @Lob
    @Column(name = "ticket_qr", nullable = false)
    private byte[] ticketQR;

    @Column(name = "ticket_qr_content_type", nullable = false)
    private String ticketQRContentType;

    @Lob
    @Column(name = "ticket_file")
    private byte[] ticketFile;

    @Column(name = "ticket_file_content_type")
    private String ticketFileContentType;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "disabled_at")
    private Instant disabledAt;

    @OneToOne
    @JoinColumn(unique = true)
    private TicketType ticketType;

    @Column(name="ticket_price")
    private String ticketPrice;

    @OneToOne
    @JoinColumn(unique = true, nullable=true)
    private PromoCode promoCode;

    @OneToOne
    @JoinColumn(nullable = true)
    private Promotor promotor;

    @OneToOne
    @JsonIgnore
    private Guest guest;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Ticket uuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getTicketUrl() {
        return ticketUrl;
    }

    public Ticket ticketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
        return this;
    }

    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
    }

    public byte[] getTicketQR() {
        return ticketQR;
    }

    public Ticket ticketQR(byte[] ticketQR) {
        this.ticketQR = ticketQR;
        return this;
    }

    public void setTicketQR(byte[] ticketQR) {
        this.ticketQR = ticketQR;
    }

    public String getTicketQRContentType() {
        return ticketQRContentType;
    }

    public Ticket ticketQRContentType(String ticketQRContentType) {
        this.ticketQRContentType = ticketQRContentType;
        return this;
    }

    public void setTicketQRContentType(String ticketQRContentType) {
        this.ticketQRContentType = ticketQRContentType;
    }

    public byte[] getTicketFile() {
        return ticketFile;
    }

    public Ticket ticketFile(byte[] ticketFile) {
        this.ticketFile = ticketFile;
        return this;
    }

    public void setTicketFile(byte[] ticketFile) {
        this.ticketFile = ticketFile;
    }

    public String getTicketFileContentType() {
        return ticketFileContentType;
    }

    public Ticket ticketFileContentType(String ticketFileContentType) {
        this.ticketFileContentType = ticketFileContentType;
        return this;
    }

    public void setTicketFileContentType(String ticketFileContentType) {
        this.ticketFileContentType = ticketFileContentType;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public Ticket enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Ticket createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getDisabledAt() {
        return disabledAt;
    }

    public Ticket disabledAt(Instant disabledAt) {
        this.disabledAt = disabledAt;
        return this;
    }

    public void setDisabledAt(Instant disabledAt) {
        this.disabledAt = disabledAt;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public Ticket ticketType(TicketType ticketType) {
        this.ticketType = ticketType;
        return this;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public PromoCode getPromoCode() {
        return promoCode;
    }

    public Ticket promoCode(PromoCode promoCode) {
        this.promoCode = promoCode;
        return this;
    }

    public void setPromoCode(PromoCode promoCode) {
        this.promoCode = promoCode;
    }

    public Guest getGuest() {
        return guest;
    }

    public Ticket guest(Guest guest) {
        this.guest = guest;
        return this;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }


    public Boolean getEnabled() {
        return enabled;
    }

    public String getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Promotor getPromotor() {
        return promotor;
    }

    public void setPromotor(Promotor promotor) {
        this.promotor = promotor;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here
}
