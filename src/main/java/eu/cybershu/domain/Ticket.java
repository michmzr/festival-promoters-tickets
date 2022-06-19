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
@Table(name = "ticket")
@ToString(of = {"id", "uuid", "ticketType", "orderId", "artistName", "ticketPrice", "ticketDiscount", "createdAt"})
@EqualsAndHashCode(of = {"id", "uuid", "ticketUrl", "ticketType", "created_at", "artistName", "ticketPrice"})
public class Ticket implements Serializable {

    private static final long serialVersionUID = 2L;

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

    private String artistName;

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

    public Ticket uuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public Ticket ticketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
        return this;
    }

    public Ticket ticketQR(byte[] ticketQR) {
        this.ticketQR = ticketQR;
        return this;
    }

    public Ticket ticketQRContentType(String ticketQRContentType) {
        this.ticketQRContentType = ticketQRContentType;
        return this;
    }

    public Ticket ticketFile(byte[] ticketFile) {
        this.ticketFile = ticketFile;
        return this;
    }

    public Ticket ticketFileContentType(String ticketFileContentType) {
        this.ticketFileContentType = ticketFileContentType;
        return this;
    }

    public Ticket enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Ticket createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Ticket disabledAt(Instant disabledAt) {
        this.disabledAt = disabledAt;
        return this;
    }

    public Ticket ticketType(TicketType ticketType) {
        this.ticketType = ticketType;
        return this;
    }

    public Ticket promoCode(PromoCode promoCode) {
        this.promoCode = promoCode;
        return this;
    }

    public Ticket guest(Guest guest) {
        this.guest = guest;
        return this;
    }

    public Ticket ticketPrice(String ticketPrice) {
        this.ticketPrice = BigDecimal.valueOf(Double.parseDouble(ticketPrice));
        return this;
    }

    public Ticket ticketPrice(Double ticketPrice) {
        this.ticketPrice = BigDecimal.valueOf(ticketPrice);
        return this;
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

    public Ticket orderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here
}
