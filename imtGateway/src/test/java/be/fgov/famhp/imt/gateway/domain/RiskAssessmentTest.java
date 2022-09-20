package be.fgov.famhp.imt.gateway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RiskAssessmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RiskAssessment.class);
        RiskAssessment riskAssessment1 = new RiskAssessment();
        riskAssessment1.setId("id1");
        RiskAssessment riskAssessment2 = new RiskAssessment();
        riskAssessment2.setId(riskAssessment1.getId());
        assertThat(riskAssessment1).isEqualTo(riskAssessment2);
        riskAssessment2.setId("id2");
        assertThat(riskAssessment1).isNotEqualTo(riskAssessment2);
        riskAssessment1.setId(null);
        assertThat(riskAssessment1).isNotEqualTo(riskAssessment2);
    }
}
