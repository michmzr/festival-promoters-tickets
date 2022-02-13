package eu.cybershu.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import eu.cybershu.web.rest.TestUtil;

public class GuestTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Guest.class);
        Guest guest1 = new Guest();
        guest1.setId(1L);
        Guest guest2 = new Guest();
        guest2.setId(guest1.getId());
        assertThat(guest1).isEqualTo(guest2);
        guest2.setId(2L);
        assertThat(guest1).isNotEqualTo(guest2);
        guest1.setId(null);
        assertThat(guest1).isNotEqualTo(guest2);
    }
}
