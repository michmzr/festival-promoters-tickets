package eu.cybershu.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class GuestMapperTest {

    private GuestMapper guestMapper;

    @BeforeEach
    public void setUp() {
        guestMapper = new GuestMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(guestMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(guestMapper.fromId(null)).isNull();
    }
}
