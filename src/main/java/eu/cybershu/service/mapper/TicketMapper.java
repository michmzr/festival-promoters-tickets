package eu.cybershu.service.mapper;


import eu.cybershu.domain.*;
import eu.cybershu.service.dto.TicketDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ticket} and its DTO {@link TicketDTO}.
 */
@Mapper(componentModel = "spring", uses = {TicketTypeMapper.class})
public interface TicketMapper extends EntityMapper<TicketDTO, Ticket> {

    @Mapping(source = "ticketType.id", target = "ticketTypeId")
    TicketDTO toDto(Ticket ticket);

    @Mapping(source = "ticketTypeId", target = "ticketType")
    @Mapping(target = "guest", ignore = true)
    Ticket toEntity(TicketDTO ticketDTO);

    default Ticket fromId(Long id) {
        if (id == null) {
            return null;
        }
        Ticket ticket = new Ticket();
        ticket.setId(id);
        return ticket;
    }
}
