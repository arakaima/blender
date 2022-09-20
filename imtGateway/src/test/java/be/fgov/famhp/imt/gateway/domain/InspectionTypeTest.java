package be.fgov.famhp.imt.gateway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InspectionTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InspectionType.class);
        InspectionType inspectionType1 = new InspectionType();
        inspectionType1.setId("id1");
        InspectionType inspectionType2 = new InspectionType();
        inspectionType2.setId(inspectionType1.getId());
        assertThat(inspectionType1).isEqualTo(inspectionType2);
        inspectionType2.setId("id2");
        assertThat(inspectionType1).isNotEqualTo(inspectionType2);
        inspectionType1.setId(null);
        assertThat(inspectionType1).isNotEqualTo(inspectionType2);
    }
}
