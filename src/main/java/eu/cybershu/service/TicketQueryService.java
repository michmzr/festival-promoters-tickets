package eu.cybershu.service;

import eu.cybershu.domain.*;
import eu.cybershu.repository.TicketRepository;
import eu.cybershu.service.dto.TicketCriteria;
import eu.cybershu.service.dto.TicketDTO;
import eu.cybershu.service.dto.TicketListingItemDTO;
import eu.cybershu.service.mapper.TicketListingMapper;
import eu.cybershu.service.mapper.TicketMapper;
import io.github.jhipster.service.QueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.JoinType;
import java.util.List;

/**
 * Service for executing complex queries for {@link Ticket} entities in the database.
 * The main input is a {@link TicketCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TicketDTO} or a {@link Page} of {@link TicketDTO} which fulfills the criteria.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class TicketQueryService extends QueryService<Ticket> {
    private final TicketRepository ticketRepository;

    private final TicketMapper ticketMapper;
    private final TicketListingMapper ticketListingMapper;

    public TicketQueryService(TicketRepository ticketRepository,
                              TicketMapper ticketMapper,
                              TicketListingMapper ticketListingMapper) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
        this.ticketListingMapper = ticketListingMapper;
    }

    /**
     * Return a {@link List} of {@link TicketDTO} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TicketListingItemDTO> findByCriteria(TicketCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ticket> specification = createSpecification(criteria);
        return ticketListingMapper.toDto(ticketRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TicketDTO} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TicketDTO> findByCriteria(TicketCriteria criteria, Pageable page) {
        log.info("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ticket> specification = createSpecification(criteria);
        return ticketRepository.findAll(specification, page)
            .map(ticketMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TicketCriteria criteria) {
        log.info("count by criteria : {}", criteria);
        final Specification<Ticket> specification = createSpecification(criteria);
        return ticketRepository.count(specification);
    }

    /**
     * Function to convert {@link TicketCriteria} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Ticket> createSpecification(TicketCriteria criteria) {
        Specification<Ticket> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Ticket_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), Ticket_.uuid));
            }
            if (criteria.getTicketUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTicketUrl(), Ticket_.ticketUrl));
            }
            if (criteria.getEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getEnabled(), Ticket_.enabled));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Ticket_.createdAt));
            }
            if (criteria.getDisabledAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDisabledAt(), Ticket_.disabledAt));
            }
            if (criteria.getTicketTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTicketTypeId(),
                    root -> root.join(Ticket_.ticketType, JoinType.LEFT).get(TicketType_.id)));
            }
            if (criteria.getPromoCodeId() != null) {
                specification = specification.and(buildSpecification(criteria.getPromoCodeId(),
                    root -> root.join(Ticket_.promoCode, JoinType.LEFT).get(PromoCode_.id)));
            }
            if (criteria.getGuestId() != null) {
                specification = specification.and(buildSpecification(criteria.getGuestId(),
                    root -> root.join(Ticket_.guest, JoinType.LEFT).get(Guest_.id)));
            }

            if (criteria.getPromotorId() != null) {
                specification = specification.and(buildSpecification(criteria.getPromotorId(),
                    root -> root.join(Ticket_.promotor, JoinType.LEFT).get(Promotor_.id)));
            }

            if (criteria.getArtistName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getArtistName(), Ticket_.artistName));
            }
        }
        return specification;
    }
}
