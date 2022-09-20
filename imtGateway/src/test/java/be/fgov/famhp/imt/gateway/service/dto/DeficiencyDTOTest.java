package be.fgov.famhp.imt.gateway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeficiencyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeficiencyDTO.class);
        DeficiencyDTO deficiencyDTO1 = new DeficiencyDTO();
        deficiencyDTO1.setId("id1");
        DeficiencyDTO deficiencyDTO2 = new DeficiencyDTO();
        assertThat(deficiencyDTO1).isNotEqualTo(deficiencyDTO2);
        deficiencyDTO2.setId(deficiencyDTO1.getId());
        assertThat(deficiencyDTO1).isEqualTo(deficiencyDTO2);
        deficiencyDTO2.setId("id2");
        assertThat(deficiencyDTO1).isNotEqualTo(deficiencyDTO2);
        deficiencyDTO1.setId(null);
        assertThat(deficiencyDTO1).isNotEqualTo(deficiencyDTO2);
    }
}
