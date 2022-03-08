package eu.cybershu.domain;

import eu.cybershu.web.rest.TestUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TicketTypeTest {

    @Test
    @Disabled
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TicketType.class);
        TicketType ticketType1 = new TicketType();
        ticketType1.setId(1L);
        TicketType ticketType2 = new TicketType();
        ticketType2.setId(ticketType1.getId());
        assertThat(ticketType1).isEqualTo(ticketType2);
        ticketType2.setId(2L);
        assertThat(ticketType1).isNotEqualTo(ticketType2);
        ticketType1.setId(null);
        assertThat(ticketType1).isNotEqualTo(ticketType2);
    }
}
