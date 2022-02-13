package eu.cybershu.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TicketTypeMapperTest {

    private TicketTypeMapper ticketTypeMapper;

    @BeforeEach
    public void setUp() {
        ticketTypeMapper = new TicketTypeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(ticketTypeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(ticketTypeMapper.fromId(null)).isNull();
    }
}
