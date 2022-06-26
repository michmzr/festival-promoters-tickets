package eu.cybershu.service.mapper;


import eu.cybershu.domain.Country;
import eu.cybershu.service.dto.CountryDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Country} and its DTO {@link CountryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CountryMapper extends EntityMapper<CountryDTO, Country> {
    default Country fromId(Long id) {
        if (id == null) {
            return null;
        }
        Country country = new Country();
        country.setId(id);
        return country;
    }
}
