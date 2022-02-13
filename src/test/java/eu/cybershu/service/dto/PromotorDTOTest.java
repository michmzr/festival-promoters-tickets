package eu.cybershu.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import eu.cybershu.web.rest.TestUtil;

public class PromotorDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PromotorDTO.class);
        PromotorDTO promotorDTO1 = new PromotorDTO();
        promotorDTO1.setId(1L);
        PromotorDTO promotorDTO2 = new PromotorDTO();
        assertThat(promotorDTO1).isNotEqualTo(promotorDTO2);
        promotorDTO2.setId(promotorDTO1.getId());
        assertThat(promotorDTO1).isEqualTo(promotorDTO2);
        promotorDTO2.setId(2L);
        assertThat(promotorDTO1).isNotEqualTo(promotorDTO2);
        promotorDTO1.setId(null);
        assertThat(promotorDTO1).isNotEqualTo(promotorDTO2);
    }
}
