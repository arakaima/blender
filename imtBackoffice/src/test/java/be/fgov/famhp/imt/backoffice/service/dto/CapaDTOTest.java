package be.fgov.famhp.imt.backoffice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.backoffice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CapaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CapaDTO.class);
        CapaDTO capaDTO1 = new CapaDTO();
        capaDTO1.setId("id1");
        CapaDTO capaDTO2 = new CapaDTO();
        assertThat(capaDTO1).isNotEqualTo(capaDTO2);
        capaDTO2.setId(capaDTO1.getId());
        assertThat(capaDTO1).isEqualTo(capaDTO2);
        capaDTO2.setId("id2");
        assertThat(capaDTO1).isNotEqualTo(capaDTO2);
        capaDTO1.setId(null);
        assertThat(capaDTO1).isNotEqualTo(capaDTO2);
    }
}
