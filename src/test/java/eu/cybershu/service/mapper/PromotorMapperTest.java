package eu.cybershu.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PromotorMapperTest {

    private PromotorMapper promotorMapper;

    @BeforeEach
    public void setUp() {
        promotorMapper = new PromotorMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(promotorMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(promotorMapper.fromId(null)).isNull();
    }
}
