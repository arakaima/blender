package be.fgov.famhp.imt.backoffice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.backoffice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RiskAssessmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RiskAssessmentDTO.class);
        RiskAssessmentDTO riskAssessmentDTO1 = new RiskAssessmentDTO();
        riskAssessmentDTO1.setId("id1");
        RiskAssessmentDTO riskAssessmentDTO2 = new RiskAssessmentDTO();
        assertThat(riskAssessmentDTO1).isNotEqualTo(riskAssessmentDTO2);
        riskAssessmentDTO2.setId(riskAssessmentDTO1.getId());
        assertThat(riskAssessmentDTO1).isEqualTo(riskAssessmentDTO2);
        riskAssessmentDTO2.setId("id2");
        assertThat(riskAssessmentDTO1).isNotEqualTo(riskAssessmentDTO2);
        riskAssessmentDTO1.setId(null);
        assertThat(riskAssessmentDTO1).isNotEqualTo(riskAssessmentDTO2);
    }
}
