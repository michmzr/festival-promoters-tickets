package eu.cybershu.service.mapper;


import eu.cybershu.domain.*;
import eu.cybershu.service.dto.GuestDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Guest} and its DTO {@link GuestDTO}.
 */
@Mapper(componentModel = "spring", uses = {TicketMapper.class, PromotorMapper.class})
public interface GuestMapper extends EntityMapper<GuestDTO, Guest> {

    @Mapping(source = "ticket.id", target = "ticketId")
    @Mapping(source = "promotor.id", target = "promotorId")
    GuestDTO toDto(Guest guest);

    @Mapping(source = "ticketId", target = "ticket")
    @Mapping(source = "promotorId", target = "promotor")
    Guest toEntity(GuestDTO guestDTO);

    default Guest fromId(Long id) {
        if (id == null) {
            return null;
        }
        Guest guest = new Guest();
        guest.setId(id);
        return guest;
    }
}
