package be.fgov.famhp.imt.gateway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InspectionTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InspectionTypeDTO.class);
        InspectionTypeDTO inspectionTypeDTO1 = new InspectionTypeDTO();
        inspectionTypeDTO1.setId("id1");
        InspectionTypeDTO inspectionTypeDTO2 = new InspectionTypeDTO();
        assertThat(inspectionTypeDTO1).isNotEqualTo(inspectionTypeDTO2);
        inspectionTypeDTO2.setId(inspectionTypeDTO1.getId());
        assertThat(inspectionTypeDTO1).isEqualTo(inspectionTypeDTO2);
        inspectionTypeDTO2.setId("id2");
        assertThat(inspectionTypeDTO1).isNotEqualTo(inspectionTypeDTO2);
        inspectionTypeDTO1.setId(null);
        assertThat(inspectionTypeDTO1).isNotEqualTo(inspectionTypeDTO2);
    }
}
