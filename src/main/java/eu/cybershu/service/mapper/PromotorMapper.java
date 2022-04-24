package eu.cybershu.service.mapper;


import eu.cybershu.domain.PromoCode;
import eu.cybershu.domain.Promotor;
import eu.cybershu.service.dto.PromoCodeDTO;
import eu.cybershu.service.dto.PromotorDTO;
import eu.cybershu.service.dto.PromotorUpdateDTO;
import org.mapstruct.Mapper;

import java.util.Set;

/**
 * Mapper for the entity {@link Promotor} and its DTO {@link PromotorDTO}.
 */
@Mapper(componentModel = "spring", uses = {
    PromoCodeMapper.class
})
public interface PromotorMapper extends EntityMapper<PromotorDTO, Promotor> {
    Promotor toEntity(PromotorDTO promotorDTO);

    Promotor toEntity(PromotorUpdateDTO promotorDTO);

    Set<PromoCodeDTO> map(Set<PromoCode> promoCodes);

    default Promotor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Promotor promotor = new Promotor();
        promotor.setId(id);
        return promotor;
    }
}
