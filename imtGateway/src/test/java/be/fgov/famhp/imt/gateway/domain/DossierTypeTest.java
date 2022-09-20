package be.fgov.famhp.imt.gateway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DossierTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DossierType.class);
        DossierType dossierType1 = new DossierType();
        dossierType1.setId("id1");
        DossierType dossierType2 = new DossierType();
        dossierType2.setId(dossierType1.getId());
        assertThat(dossierType1).isEqualTo(dossierType2);
        dossierType2.setId("id2");
        assertThat(dossierType1).isNotEqualTo(dossierType2);
        dossierType1.setId(null);
        assertThat(dossierType1).isNotEqualTo(dossierType2);
    }
}
