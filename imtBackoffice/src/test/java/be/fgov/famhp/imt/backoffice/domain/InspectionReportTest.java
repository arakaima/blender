package be.fgov.famhp.imt.backoffice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.backoffice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InspectionReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InspectionReport.class);
        InspectionReport inspectionReport1 = new InspectionReport();
        inspectionReport1.setId("id1");
        InspectionReport inspectionReport2 = new InspectionReport();
        inspectionReport2.setId(inspectionReport1.getId());
        assertThat(inspectionReport1).isEqualTo(inspectionReport2);
        inspectionReport2.setId("id2");
        assertThat(inspectionReport1).isNotEqualTo(inspectionReport2);
        inspectionReport1.setId(null);
        assertThat(inspectionReport1).isNotEqualTo(inspectionReport2);
    }
}
