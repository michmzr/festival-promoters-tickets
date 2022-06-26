package eu.cybershu.service.mapper;


import eu.cybershu.domain.Ticket;
import eu.cybershu.service.dto.TicketCreateDTO;
import eu.cybershu.service.dto.TicketListingItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Ticket} and its DTO {@link TicketListingItemDTO}.
 */
@Mapper(componentModel = "spring", uses = {TicketTypeMapper.class, PromoCodeMapper.class})
public interface TicketListingMapper extends EntityMapper<TicketListingItemDTO, Ticket> {

    @Mapping(source = "ticketType.id", target = "ticketTypeId")
    @Mapping(source = "promoCode.id", target = "promoCodeId")
    @Mapping(source = "guest.id", target = "guestId")
    TicketListingItemDTO toDto(Ticket ticket);

    @Mapping(source = "ticketTypeId", target = "ticketType")
    @Mapping(source = "promoCodeId", target = "promoCode")
    @Mapping(target = "guest", ignore = true)
    Ticket toEntity(TicketListingItemDTO TicketListingItemDTO);

    @Mapping(source = "ticketTypeId", target = "ticketType")
    @Mapping(source = "promoCodeId", target = "promoCode")
    @Mapping(target = "guest", ignore = true)
    Ticket toEntity(TicketCreateDTO TicketListingItemDTO);

    default Ticket fromId(Long id) {
        if (id == null) {
            return null;
        }
        Ticket ticket = new Ticket();
        ticket.setId(id);
        return ticket;
    }
}
