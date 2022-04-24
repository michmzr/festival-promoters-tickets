package eu.cybershu.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import eu.cybershu.web.rest.TestUtil;

public class PromoCodeDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PromoCodeDTO.class);
        PromoCodeDTO promoCodeDTO1 = new PromoCodeDTO();
        promoCodeDTO1.setId(1L);
        PromoCodeDTO promoCodeDTO2 = new PromoCodeDTO();
        assertThat(promoCodeDTO1).isNotEqualTo(promoCodeDTO2);
        promoCodeDTO2.setId(promoCodeDTO1.getId());
        assertThat(promoCodeDTO1).isEqualTo(promoCodeDTO2);
        promoCodeDTO2.setId(2L);
        assertThat(promoCodeDTO1).isNotEqualTo(promoCodeDTO2);
        promoCodeDTO1.setId(null);
        assertThat(promoCodeDTO1).isNotEqualTo(promoCodeDTO2);
    }
}
