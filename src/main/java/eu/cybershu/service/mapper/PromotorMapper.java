package eu.cybershu.service.mapper;


import eu.cybershu.domain.*;
import eu.cybershu.service.dto.PromotorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Promotor} and its DTO {@link PromotorDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PromotorMapper extends EntityMapper<PromotorDTO, Promotor> {


    @Mapping(target = "guests", ignore = true)
    @Mapping(target = "removeGuest", ignore = true)
    @Mapping(target = "promoCodes", ignore = true)
    @Mapping(target = "removePromoCode", ignore = true)
    Promotor toEntity(PromotorDTO promotorDTO);

    default Promotor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Promotor promotor = new Promotor();
        promotor.setId(id);
        return promotor;
    }
}
