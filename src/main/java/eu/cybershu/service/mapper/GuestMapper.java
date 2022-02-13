package eu.cybershu.service.mapper;


import eu.cybershu.domain.*;
import eu.cybershu.service.dto.GuestCreateDTO;
import eu.cybershu.service.dto.GuestDTO;

import eu.cybershu.service.dto.GuestUpdateDTO;
import org.mapstruct.*;

import java.time.Instant;

/**
 * Mapper for the entity {@link Guest} and its DTO {@link GuestDTO}.
 */
@Mapper(componentModel = "spring", uses = {TicketMapper.class, PromotorMapper.class, TicketTypeMapper.class})
public interface GuestMapper extends EntityMapper<GuestDTO, Guest> {

    @Mapping(source = "ticket.id", target = "ticketId")
    @Mapping(source = "promotor.id", target = "promotorId")
    GuestDTO toDto(Guest guest);

    @Mapping(source = "ticketId", target = "ticket")
    @Mapping(source = "promotorId", target = "promotor")
    Guest toEntity(GuestDTO guestDTO);

    GuestDTO toDto(GuestCreateDTO guestCreateDTO);

    default GuestDTO update(GuestDTO guestDTO, GuestUpdateDTO guestUpdateDTO) {
        guestDTO.setName(guestUpdateDTO.getName());
        guestDTO.setLastName(guestUpdateDTO.getLastName());
        guestDTO.setEmail(guestUpdateDTO.getEmail());
        guestDTO.setPhoneNumber(guestUpdateDTO.getPhoneNumber());
        guestDTO.setNotes(guestUpdateDTO.getNotes());

        return guestDTO;
    }

    default Guest fromId(Long id) {
        if (id == null) {
            return null;
        }
        Guest guest = new Guest();
        guest.setId(id);
        return guest;
    }
}
