package be.fgov.famhp.imt.gateway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InspectorDossierTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InspectorDossier.class);
        InspectorDossier inspectorDossier1 = new InspectorDossier();
        inspectorDossier1.setId("id1");
        InspectorDossier inspectorDossier2 = new InspectorDossier();
        inspectorDossier2.setId(inspectorDossier1.getId());
        assertThat(inspectorDossier1).isEqualTo(inspectorDossier2);
        inspectorDossier2.setId("id2");
        assertThat(inspectorDossier1).isNotEqualTo(inspectorDossier2);
        inspectorDossier1.setId(null);
        assertThat(inspectorDossier1).isNotEqualTo(inspectorDossier2);
    }
}
