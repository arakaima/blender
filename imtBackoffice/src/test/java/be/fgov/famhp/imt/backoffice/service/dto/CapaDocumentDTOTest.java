package be.fgov.famhp.imt.backoffice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.backoffice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CapaDocumentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CapaDocumentDTO.class);
        CapaDocumentDTO capaDocumentDTO1 = new CapaDocumentDTO();
        capaDocumentDTO1.setId("id1");
        CapaDocumentDTO capaDocumentDTO2 = new CapaDocumentDTO();
        assertThat(capaDocumentDTO1).isNotEqualTo(capaDocumentDTO2);
        capaDocumentDTO2.setId(capaDocumentDTO1.getId());
        assertThat(capaDocumentDTO1).isEqualTo(capaDocumentDTO2);
        capaDocumentDTO2.setId("id2");
        assertThat(capaDocumentDTO1).isNotEqualTo(capaDocumentDTO2);
        capaDocumentDTO1.setId(null);
        assertThat(capaDocumentDTO1).isNotEqualTo(capaDocumentDTO2);
    }
}
