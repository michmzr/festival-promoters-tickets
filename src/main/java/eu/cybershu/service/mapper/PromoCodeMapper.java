package eu.cybershu.service.mapper;


import eu.cybershu.domain.*;
import eu.cybershu.service.dto.PromoCodeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PromoCode} and its DTO {@link PromoCodeDTO}.
 */
@Mapper(componentModel = "spring", uses = {PromotorMapper.class})
public interface PromoCodeMapper extends EntityMapper<PromoCodeDTO, PromoCode> {

    @Mapping(source = "promotor.id", target = "promotorId")
    PromoCodeDTO toDto(PromoCode promoCode);

    @Mapping(source = "promotorId", target = "promotor")
    PromoCode toEntity(PromoCodeDTO promoCodeDTO);

    default PromoCode fromId(Long id) {
        if (id == null) {
            return null;
        }
        PromoCode promoCode = new PromoCode();
        promoCode.setId(id);
        return promoCode;
    }
}
