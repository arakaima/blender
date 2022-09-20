package be.fgov.famhp.imt.gateway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InspectionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InspectionDTO.class);
        InspectionDTO inspectionDTO1 = new InspectionDTO();
        inspectionDTO1.setId("id1");
        InspectionDTO inspectionDTO2 = new InspectionDTO();
        assertThat(inspectionDTO1).isNotEqualTo(inspectionDTO2);
        inspectionDTO2.setId(inspectionDTO1.getId());
        assertThat(inspectionDTO1).isEqualTo(inspectionDTO2);
        inspectionDTO2.setId("id2");
        assertThat(inspectionDTO1).isNotEqualTo(inspectionDTO2);
        inspectionDTO1.setId(null);
        assertThat(inspectionDTO1).isNotEqualTo(inspectionDTO2);
    }
}
