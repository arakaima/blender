package be.fgov.famhp.imt.gateway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DossierDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DossierDTO.class);
        DossierDTO dossierDTO1 = new DossierDTO();
        dossierDTO1.setId("id1");
        DossierDTO dossierDTO2 = new DossierDTO();
        assertThat(dossierDTO1).isNotEqualTo(dossierDTO2);
        dossierDTO2.setId(dossierDTO1.getId());
        assertThat(dossierDTO1).isEqualTo(dossierDTO2);
        dossierDTO2.setId("id2");
        assertThat(dossierDTO1).isNotEqualTo(dossierDTO2);
        dossierDTO1.setId(null);
        assertThat(dossierDTO1).isNotEqualTo(dossierDTO2);
    }
}
