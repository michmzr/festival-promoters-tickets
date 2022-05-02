package eu.cybershu.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * A Ticket.
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "uuid", "ticketUrl", "ticketType", "created_at"})
@Table(name = "ticket")
@ToString(of = {"id", "uuid", "ticketType", "orderId", "createdAt"})
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
    @ManyToOne
    private TicketType ticketType;

    /**
     * Ticket price paid by guest in PLN
     */
    @Column(name = "ticket_price", precision = 6, scale = 2)
    private BigDecimal ticketPrice;

    /**
     * Discount value in PLN
     */
    @Column(name = "ticket_discount", precision = 6, scale = 2)
    private BigDecimal ticketDiscount = new BigDecimal(0);

    @NotNull
    @Column(name = "order_id")
    private String orderId;

    @OneToOne
    @JoinColumn
    private PromoCode promoCode;

    @OneToOne
    @JoinColumn
    private Promotor promotor;

    @OneToOne
    @JsonIgnore
    private Guest guest;

    @Column(name = "validated_at")
    private Instant validatedAt;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "disabled_at")
    private Instant disabledAt;

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

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        this.ticketPrice = new BigDecimal(ticketPrice);
    }

    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Ticket ticketPrice(String ticketPrice) {
        this.ticketPrice = BigDecimal.valueOf(Double.parseDouble(ticketPrice));
        return this;
    }

    public Ticket ticketPrice(Double ticketPrice) {
        this.ticketPrice = BigDecimal.valueOf(ticketPrice);
        return this;
    }

    public Promotor getPromotor() {
        return promotor;
    }

    public void setPromotor(Promotor promotor) {
        this.promotor = promotor;
    }


    public BigDecimal getTicketDiscount() {
        return ticketDiscount;
    }

    public void setTicketDiscount(BigDecimal ticketDiscount) {
        this.ticketDiscount = ticketDiscount;
    }

    public Ticket ticketDiscount(BigDecimal ticketDiscount) {
        this.ticketDiscount = ticketDiscount;
        return this;
    }

    public Ticket ticketDiscount(String ticketDiscount) {
        this.ticketDiscount = BigDecimal.valueOf(Double.parseDouble(ticketDiscount));
        return this;
    }

    public Ticket ticketDiscount(Double ticketDiscount) {
        this.ticketDiscount = BigDecimal.valueOf(ticketDiscount);
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Ticket orderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public Instant getValidatedAt() {
        return validatedAt;
    }

    public void setValidatedAt(Instant validatedAt) {
        this.validatedAt = validatedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here
}
