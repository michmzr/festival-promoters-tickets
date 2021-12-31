package eu.cybershu.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import eu.cybershu.web.rest.TestUtil;

public class PromotorTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Promotor.class);
        Promotor promotor1 = new Promotor();
        promotor1.setId(1L);
        Promotor promotor2 = new Promotor();
        promotor2.setId(promotor1.getId());
        assertThat(promotor1).isEqualTo(promotor2);
        promotor2.setId(2L);
        assertThat(promotor1).isNotEqualTo(promotor2);
        promotor1.setId(null);
        assertThat(promotor1).isNotEqualTo(promotor2);
    }
}
