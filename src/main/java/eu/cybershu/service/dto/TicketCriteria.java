package eu.cybershu.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link eu.cybershu.domain.Ticket} entity. This class is used
 * in {@link eu.cybershu.web.rest.TicketResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tickets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TicketCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter uuid;

    private StringFilter ticketUrl;

    private BooleanFilter enabled;

    private InstantFilter createdAt;

    private InstantFilter disabledAt;

    private LongFilter ticketTypeId;

    private LongFilter promoCodeId;


    private LongFilter promotorId;

    private LongFilter guestId;

    public TicketCriteria() {
    }

    public TicketCriteria(TicketCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uuid = other.uuid == null ? null : other.uuid.copy();
        this.ticketUrl = other.ticketUrl == null ? null : other.ticketUrl.copy();
        this.enabled = other.enabled == null ? null : other.enabled.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.disabledAt = other.disabledAt == null ? null : other.disabledAt.copy();
        this.ticketTypeId = other.ticketTypeId == null ? null : other.ticketTypeId.copy();
        this.promoCodeId = other.promoCodeId == null ? null : other.promoCodeId.copy();
        this.promotorId = other.promotorId == null ? null : other.promotorId.copy();
        this.guestId = other.guestId == null ? null : other.guestId.copy();
    }

    @Override
    public TicketCriteria copy() {
        return new TicketCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public UUIDFilter getUuid() {
        return uuid;
    }

    public void setUuid(UUIDFilter uuid) {
        this.uuid = uuid;
    }

    public StringFilter getTicketUrl() {
        return ticketUrl;
    }

    public void setTicketUrl(StringFilter ticketUrl) {
        this.ticketUrl = ticketUrl;
    }

    public BooleanFilter getEnabled() {
        return enabled;
    }

    public void setEnabled(BooleanFilter enabled) {
        this.enabled = enabled;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public InstantFilter getDisabledAt() {
        return disabledAt;
    }

    public void setDisabledAt(InstantFilter disabledAt) {
        this.disabledAt = disabledAt;
    }

    public LongFilter getTicketTypeId() {
        return ticketTypeId;
    }

    public void setTicketTypeId(LongFilter ticketTypeId) {
        this.ticketTypeId = ticketTypeId;
    }

    public LongFilter getPromoCodeId() {
        return promoCodeId;
    }

    public void setPromoCodeId(LongFilter promoCodeId) {
        this.promoCodeId = promoCodeId;
    }

    public LongFilter getGuestId() {
        return guestId;
    }

    public void setGuestId(LongFilter guestId) {
        this.guestId = guestId;
    }

    public LongFilter getPromotorId() {
        return promotorId;
    }

    public void setPromotorId(LongFilter promotorId) {
        this.promotorId = promotorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TicketCriteria that = (TicketCriteria) o;
        return
            Objects.equals(id, that.id) &&
                Objects.equals(uuid, that.uuid) &&
                Objects.equals(ticketUrl, that.ticketUrl) &&
                Objects.equals(enabled, that.enabled) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(disabledAt, that.disabledAt) &&
                Objects.equals(ticketTypeId, that.ticketTypeId) &&
                Objects.equals(promoCodeId, that.promoCodeId) &&
                Objects.equals(promotorId, that.promotorId) &&
                Objects.equals(guestId, that.guestId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            uuid,
            ticketUrl,
            enabled,
            createdAt,
            disabledAt,
            ticketTypeId,
            promoCodeId,
            promotorId,
            guestId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (uuid != null ? "uuid=" + uuid + ", " : "") +
            (ticketUrl != null ? "ticketUrl=" + ticketUrl + ", " : "") +
            (enabled != null ? "enabled=" + enabled + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (disabledAt != null ? "disabledAt=" + disabledAt + ", " : "") +
            (ticketTypeId != null ? "ticketTypeId=" + ticketTypeId + ", " : "") +
            (promoCodeId != null ? "promoCodeId=" + promoCodeId + ", " : "") +
            (promotorId != null ? "promotorId=" + promotorId + ", " : "") +
            (guestId != null ? "guestId=" + guestId + ", " : "") +
            "}";
    }

}
