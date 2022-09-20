package be.fgov.famhp.imt.gateway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InspectionReportDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InspectionReportDTO.class);
        InspectionReportDTO inspectionReportDTO1 = new InspectionReportDTO();
        inspectionReportDTO1.setId("id1");
        InspectionReportDTO inspectionReportDTO2 = new InspectionReportDTO();
        assertThat(inspectionReportDTO1).isNotEqualTo(inspectionReportDTO2);
        inspectionReportDTO2.setId(inspectionReportDTO1.getId());
        assertThat(inspectionReportDTO1).isEqualTo(inspectionReportDTO2);
        inspectionReportDTO2.setId("id2");
        assertThat(inspectionReportDTO1).isNotEqualTo(inspectionReportDTO2);
        inspectionReportDTO1.setId(null);
        assertThat(inspectionReportDTO1).isNotEqualTo(inspectionReportDTO2);
    }
}
