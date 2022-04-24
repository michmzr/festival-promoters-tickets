package eu.cybershu.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PromoCodeMapperTest {

    private PromoCodeMapper promoCodeMapper;

    @BeforeEach
    public void setUp() {
        promoCodeMapper = new PromoCodeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(promoCodeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(promoCodeMapper.fromId(null)).isNull();
    }
}
