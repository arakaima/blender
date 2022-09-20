package be.fgov.famhp.imt.backoffice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.backoffice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DossierStatusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DossierStatusDTO.class);
        DossierStatusDTO dossierStatusDTO1 = new DossierStatusDTO();
        dossierStatusDTO1.setId("id1");
        DossierStatusDTO dossierStatusDTO2 = new DossierStatusDTO();
        assertThat(dossierStatusDTO1).isNotEqualTo(dossierStatusDTO2);
        dossierStatusDTO2.setId(dossierStatusDTO1.getId());
        assertThat(dossierStatusDTO1).isEqualTo(dossierStatusDTO2);
        dossierStatusDTO2.setId("id2");
        assertThat(dossierStatusDTO1).isNotEqualTo(dossierStatusDTO2);
        dossierStatusDTO1.setId(null);
        assertThat(dossierStatusDTO1).isNotEqualTo(dossierStatusDTO2);
    }
}
