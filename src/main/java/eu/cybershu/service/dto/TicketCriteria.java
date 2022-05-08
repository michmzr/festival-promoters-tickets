package eu.cybershu.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Criteria class for the {@link eu.cybershu.domain.Ticket} entity. This class is used
 * in {@link eu.cybershu.web.rest.TicketResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tickets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@EqualsAndHashCode
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

    private StringFilter artistName;

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
}
