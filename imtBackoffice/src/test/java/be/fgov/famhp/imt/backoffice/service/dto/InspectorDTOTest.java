package be.fgov.famhp.imt.backoffice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.backoffice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InspectorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InspectorDTO.class);
        InspectorDTO inspectorDTO1 = new InspectorDTO();
        inspectorDTO1.setId("id1");
        InspectorDTO inspectorDTO2 = new InspectorDTO();
        assertThat(inspectorDTO1).isNotEqualTo(inspectorDTO2);
        inspectorDTO2.setId(inspectorDTO1.getId());
        assertThat(inspectorDTO1).isEqualTo(inspectorDTO2);
        inspectorDTO2.setId("id2");
        assertThat(inspectorDTO1).isNotEqualTo(inspectorDTO2);
        inspectorDTO1.setId(null);
        assertThat(inspectorDTO1).isNotEqualTo(inspectorDTO2);
    }
}
