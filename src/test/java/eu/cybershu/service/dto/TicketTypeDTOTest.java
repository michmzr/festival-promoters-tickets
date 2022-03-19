package eu.cybershu.service.dto;

import eu.cybershu.web.rest.TestUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TicketTypeDTOTest {

    @Test
    @Disabled
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TicketTypeDTO.class);
        TicketTypeDTO ticketTypeDTO1 = new TicketTypeDTO();
        ticketTypeDTO1.setId(1L);
        TicketTypeDTO ticketTypeDTO2 = new TicketTypeDTO();
        assertThat(ticketTypeDTO1).isNotEqualTo(ticketTypeDTO2);
        ticketTypeDTO2.setId(ticketTypeDTO1.getId());
        assertThat(ticketTypeDTO1).isEqualTo(ticketTypeDTO2);
        ticketTypeDTO2.setId(2L);
        assertThat(ticketTypeDTO1).isNotEqualTo(ticketTypeDTO2);
        ticketTypeDTO1.setId(null);
        assertThat(ticketTypeDTO1).isNotEqualTo(ticketTypeDTO2);
    }
}
