package be.fgov.famhp.imt.gateway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DossierTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DossierTypeDTO.class);
        DossierTypeDTO dossierTypeDTO1 = new DossierTypeDTO();
        dossierTypeDTO1.setId("id1");
        DossierTypeDTO dossierTypeDTO2 = new DossierTypeDTO();
        assertThat(dossierTypeDTO1).isNotEqualTo(dossierTypeDTO2);
        dossierTypeDTO2.setId(dossierTypeDTO1.getId());
        assertThat(dossierTypeDTO1).isEqualTo(dossierTypeDTO2);
        dossierTypeDTO2.setId("id2");
        assertThat(dossierTypeDTO1).isNotEqualTo(dossierTypeDTO2);
        dossierTypeDTO1.setId(null);
        assertThat(dossierTypeDTO1).isNotEqualTo(dossierTypeDTO2);
    }
}
