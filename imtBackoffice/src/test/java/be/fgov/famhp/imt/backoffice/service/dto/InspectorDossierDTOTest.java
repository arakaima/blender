package be.fgov.famhp.imt.backoffice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.backoffice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InspectorDossierDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InspectorDossierDTO.class);
        InspectorDossierDTO inspectorDossierDTO1 = new InspectorDossierDTO();
        inspectorDossierDTO1.setId("id1");
        InspectorDossierDTO inspectorDossierDTO2 = new InspectorDossierDTO();
        assertThat(inspectorDossierDTO1).isNotEqualTo(inspectorDossierDTO2);
        inspectorDossierDTO2.setId(inspectorDossierDTO1.getId());
        assertThat(inspectorDossierDTO1).isEqualTo(inspectorDossierDTO2);
        inspectorDossierDTO2.setId("id2");
        assertThat(inspectorDossierDTO1).isNotEqualTo(inspectorDossierDTO2);
        inspectorDossierDTO1.setId(null);
        assertThat(inspectorDossierDTO1).isNotEqualTo(inspectorDossierDTO2);
    }
}
