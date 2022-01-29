package eu.cybershu.service.mapper;


import eu.cybershu.domain.*;
import eu.cybershu.service.dto.TicketTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TicketType} and its DTO {@link TicketTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TicketTypeMapper extends EntityMapper<TicketTypeDTO, TicketType> {
    TicketType toEntity(TicketTypeDTO ticketTypeDTO);

    default TicketType fromId(Long id) {
        if (id == null) {
            return null;
        }
        TicketType ticketType = new TicketType();
        ticketType.setId(id);
        return ticketType;
    }
}
