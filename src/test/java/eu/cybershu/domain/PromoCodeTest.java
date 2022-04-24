package eu.cybershu.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import eu.cybershu.web.rest.TestUtil;

public class PromoCodeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PromoCode.class);
        PromoCode promoCode1 = new PromoCode();
        promoCode1.setId(1L);
        PromoCode promoCode2 = new PromoCode();
        promoCode2.setId(promoCode1.getId());
        assertThat(promoCode1).isEqualTo(promoCode2);
        promoCode2.setId(2L);
        assertThat(promoCode1).isNotEqualTo(promoCode2);
        promoCode1.setId(null);
        assertThat(promoCode1).isNotEqualTo(promoCode2);
    }
}
