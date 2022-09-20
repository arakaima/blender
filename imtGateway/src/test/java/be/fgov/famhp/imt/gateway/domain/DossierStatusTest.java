package be.fgov.famhp.imt.gateway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DossierStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DossierStatus.class);
        DossierStatus dossierStatus1 = new DossierStatus();
        dossierStatus1.setId("id1");
        DossierStatus dossierStatus2 = new DossierStatus();
        dossierStatus2.setId(dossierStatus1.getId());
        assertThat(dossierStatus1).isEqualTo(dossierStatus2);
        dossierStatus2.setId("id2");
        assertThat(dossierStatus1).isNotEqualTo(dossierStatus2);
        dossierStatus1.setId(null);
        assertThat(dossierStatus1).isNotEqualTo(dossierStatus2);
    }
}
